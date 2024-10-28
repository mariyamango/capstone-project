import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import {Link} from "react-router-dom";

const Header = () => {
    return <>
        <Navbar bg="secondary" data-bs-theme="dark" className="mb-3">
            <Container className="w-100">
                <div className="header-container">
                    <div className="header-container__nav">
                        <Nav className="me-auto">
                            <Nav.Link as={Link} to="/">Home</Nav.Link>
                            <Nav.Link as={Link} to="/about">About</Nav.Link>
                        </Nav>
                    </div>
                </div>
            </Container>
        </Navbar>
    </>
}

export default Header;