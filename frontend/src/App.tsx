import './styles/App.css'
import Home from "./pages/Home.tsx";
import Header from "./pages/components/Header.tsx";
import {Route, Routes, useNavigate} from "react-router-dom";
import Footer from "./pages/components/Footer.tsx";
import CarDetail from "./pages/CarDetail.tsx";
import About from "./pages/About.tsx";
import {useEffect, useState} from "react";
import axios, {AxiosError} from "axios";
import {Button} from "react-bootstrap";

function App() {
    const [username, setUsername] = useState<string | undefined>();
    const navigate = useNavigate();

    function loginGithub() {
        const host = window.location.host === 'localhost:5173' ? 'http://localhost:8080' : window.location.origin
        window.open(host + '/oauth2/authorization/github', '_self')
    }

    function loginGoogle() {
        const host = window.location.host === 'localhost:5173' ? 'http://localhost:8080' : window.location.origin
        window.open(host + '/oauth2/authorization/google', '_self')
    }

    const loadUser = () => {
        axios.get('/api/auth/me')
            .then(response => {
                const usernameResponse = response.data;
                setUsername(usernameResponse);
            })
            .catch((error: AxiosError) => {
                console.log(error)
            })
    }

    function logout() {
        axios.post("/api/auth/logout")
            .then(() => {
                setUsername("");
                navigate("/");
            })
            .catch((error: AxiosError) => {
                console.log(error)
            })
    }

    useEffect(() => {
        loadUser();
    }, []);

    return (
        <>
            {username ? (
                    <div className="app-container d-flex flex-column min-vh-100" data-bs-theme="dark">
                        <Header logout={logout}/>
                        <main className="flex-fill">
                            <Routes>
                                <Route path="/" element={<Home/>}/>
                                <Route path="/car/:id" element={<CarDetail/>}/>
                                <Route path="/about" element={<About/>}/>
                            </Routes>
                        </main>
                        <Footer/>
                    </div>) :
                <div className="logout-btn">
                    <h3 className="mb-4">Please log in to see your car list:</h3>
                    <Button className="mt-3" onClick={loginGithub}>Login with GitHub</Button>
                    <Button className="mt-3" onClick={loginGoogle}>Login with Google</Button>
                </div>
            }
        </>
    )
}

export default App
