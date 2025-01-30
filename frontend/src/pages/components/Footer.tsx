import { Col, Container, Row } from "react-bootstrap";

const Footer = () => {
    return (
        <footer className="footer bg-dark mt-auto py-3">
            <Container>
                <Row>
                    <Col md={4} xs={4}>
                        <h5>Links</h5>
                        <p><a href="https://github.com/mariyamango" target="_blank"
                              rel="noopener noreferrer">My GitHub</a></p>
                    </Col>
                    <Col md={4} xs={4}>
                        <h5>Check Engine?</h5>
                        <p><a href="/chuck" target="_blank"
                              rel="noopener noreferrer">Nah, Check Jokes!</a></p>
                    </Col>
                    <Col md={4} xs={4}>
                        <h5>Have an issue?</h5>
                        <p><a href="https://github.com/mariyamango/capstone-project/issues" target="_blank" rel="noopener noreferrer">GitHub Issues page</a></p>
                    </Col>
                </Row>
            </Container>
        </footer>
    );
};

export default Footer;
