import './styles/App.css'
import Home from "./pages/Home.tsx";
import Header from "./pages/components/Header.tsx";
import {Route, Routes} from "react-router-dom";
import Footer from "./pages/components/Footer.tsx";
import CarDetail from "./pages/CarDetail.tsx";
import About from "./pages/About.tsx";

function App() {

    return (
        <div className="app-container d-flex flex-column min-vh-100">
            <Header/>
            <main className="flex-fill">
                <Routes>
                    <Route path="/" element={<Home/>}/>
                    <Route path="/car/:id" element={<CarDetail/>}/>
                    <Route path="/about" element={<About/>}/>
                </Routes>
            </main>
            <Footer/>
        </div>
    )
}

export default App
