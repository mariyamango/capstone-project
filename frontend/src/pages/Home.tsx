import axios from 'axios';
import { useEffect, useState } from "react";
import { Card, Col, Row, Container } from 'react-bootstrap';
import {Car} from "./types/Car.tsx";

function Home() {
    const [data, setData] = useState<Car[]>([]);

    const fetchData = async () => {
        axios.get<Car[]>("/api/cars")
            .then((response) => {
                setData(response.data);
            })
            .catch((error) => {
                console.log('Error fetching data', error);
            });
    };

    useEffect(() => {
        fetchData();
    }, [])

    return (
        <Container className="my-5">
            <h1 className="text-center mb-4">Your cars:</h1>
            <Row xs={1} md={2} lg={3} className="g-4">
                {data.map((car, index) => (
                    <Col key={index}>
                        <Card className="h-100">
                            <Card.Body>
                                <Card.Title>{car.model}</Card.Title>
                                <Card.Text>
                                    <strong>Year:</strong> {car.year}
                                </Card.Text>
                                <Card.Text>
                                    <strong>VIN:</strong> {car.vin}
                                </Card.Text>
                            </Card.Body>
                        </Card>
                    </Col>
                ))}
            </Row>
        </Container>
    );
}

export default Home;
