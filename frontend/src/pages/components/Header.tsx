import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import {Link} from "react-router-dom";
import Logo from "../../../public/tools.svg";
import {Button} from "react-bootstrap";

type HeaderProps = {
    logout: () => void;
}

const Header = ({logout}: HeaderProps) => {
    return (
        <Navbar bg="secondary" data-bs-theme="dark" className="mb-3">
            <Container className="w-100">
                <div className="header-container d-flex justify-content-between align-items-center mb-4">
                    <div className="d-flex">
                        <Navbar.Brand as={Link} to="/">
                            <img src={Logo} alt="" className="logo"/>
                        </Navbar.Brand>
                        <Nav className="ms-3">
                            <Nav.Link as={Link} to="/">Home</Nav.Link>
                            <Nav.Link as={Link} to="/about">About</Nav.Link>
                        </Nav>
                    </div>
                    <div className="d-flex">
                        <Button onClick={logout}>Logout</Button>
                    </div>
                </div>
            </Container>
        </Navbar>
    );
}

export default Header;
