import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import { Link } from "react-router-dom";
import Logo from "../../../public/tools.svg";

const Header = () => {
    return (
        <Navbar bg="secondary" data-bs-theme="dark" className="mb-3">
            <Container className="w-100">
                <div className="header-container d-flex align-items-center">
                    <Navbar.Brand as={Link} to="/">
                        <img src={Logo} alt="" className="logo" />
                    </Navbar.Brand>
                    <Nav className="ms-3"> {/* Добавлен отступ слева от логотипа */}
                        <Nav.Link as={Link} to="/">Home</Nav.Link>
                        <Nav.Link as={Link} to="/about">About</Nav.Link>
                    </Nav>
                </div>
            </Container>
        </Navbar>
    );
}

export default Header;
