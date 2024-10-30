import './styles/App.css'
import Home from "./pages/Home.tsx";
import Header from "./pages/components/Header.tsx";
import {Route, Routes} from "react-router-dom";
import Footer from "./pages/components/Footer.tsx";
import CarDetail from "./pages/CarDetail.tsx";

function App() {

    return (
        <>
            <Header/>
            <main>
                <Routes>
                    <Route path="/" element={<Home/>}/>
                    <Route path="/car/:id" element={<CarDetail/>}/>
                </Routes>
            </main>
            <Footer/>
        </>
    )
}

export default App
