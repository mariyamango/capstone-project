import { Col, Container, Row } from "react-bootstrap";
import { Link } from "react-router-dom";
import Nav from "react-bootstrap/Nav";

const Footer = () => {
    return (
        <footer className="bg-dark text-white mt-auto py-3">
            <Container>
                <Row>
                    <Col md={4}>
                        <h5>About Us</h5>
                        <p>My name is Maria, I am dedicated to crafting innovative web solutions for a better online experience.</p>
                    </Col>
                    <Col md={4}>
                        <h5>Links</h5>
                        <ul className="list-unstyled">
                            <li><Nav.Link as={Link} to="/" className="text-white">Home</Nav.Link></li>
                            <li><Nav.Link as={Link} to="/about" className="text-white">About</Nav.Link></li>
                        </ul>
                    </Col>
                    <Col md={4}>
                        <h5>Contact Us</h5>
                        <p>Email: info@example.com</p>
                    </Col>
                </Row>
            </Container>
        </footer>
    );
};

export default Footer;
