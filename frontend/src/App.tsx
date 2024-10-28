import './styles/App.css'
import Home from "./pages/Home.tsx";
import Header from "./pages/components/Header.tsx";
import {Route, Routes} from "react-router-dom";
import Footer from "./pages/components/Footer.tsx";

function App() {

    return (
        <>
            <Header/>
            <main>
                <Routes>
                    <Route path="/" element={<Home/>}/>
                </Routes>
            </main>
            <Footer/>
        </>
    )
}

export default App
