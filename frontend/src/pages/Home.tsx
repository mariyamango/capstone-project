import axios from 'axios';
import {useEffect, useState} from "react";
import {Button, Card, Col, Container, Form, Modal, Row} from 'react-bootstrap';
import {Car} from "./types/Car.tsx";
import {Link} from "react-router-dom";

function Home() {
    const [data, setData] = useState<Car[]>([]);
    const [showAddModal, setShowAddModal] = useState(false);
    const [showEditModal, setShowEditModal] = useState(false);
    const [newCar, setNewCar] = useState({model: '', year: 0, vin: '', currentMileage: 0});
    const [editableCar, setEditableCar] = useState<Car | null>(null);

    const fetchData = async () => {
        try {
            const response = await axios.get<Car[]>("/api/cars");
            setData(response.data);
        } catch (error) {
            console.log('Error fetching data', error);
        }
    };

    const handleAddCar = async () => {
        try {
            const response = await axios.post("/api/cars", newCar);
            setData([...data, response.data]);
            setShowAddModal(false);
            setNewCar({model: '', year: 0, vin: '', currentMileage: 0});
        } catch (error) {
            console.log('Error adding car', error);
        }
    };

    const handleEditCarOpen = (car: Car) => {
        setEditableCar(car);
        setShowEditModal(true);
    };

    const handleEditCarSave = async () => {
        if (editableCar) {
            try {
                const response = await axios.put(`/api/cars/${editableCar.id}`, editableCar);
                setData(data.map(car => (car.id === editableCar.id ? response.data : car)));
                setShowEditModal(false);
                setEditableCar(null);
            } catch (error) {
                console.log('Error editing car', error);
            }
        }
    };

    const handleDeleteCar = async (carId: string) => {
        try {
            await axios.delete(`/api/cars/${carId}`);
            setData(data.filter(car => car.id !== carId));
        } catch (error) {
            console.log('Error deleting car', error);
        }
    };

    useEffect(() => {
        fetchData();
    }, []);

    return (
        <Container className="my-5">
            <div className="d-flex justify-content-between align-items-center mb-4">
                <h1>Your cars:</h1>
                <Button onClick={() => setShowAddModal(true)} variant="primary">Add Car</Button>
            </div>

            <Row xs={1} md={2} lg={3} className="g-4">
                {data.map((car) => (
                    <Col key={car.id}>
                        <Card className="h-100">
                            <Link to={`/car/${car.id}`} style={{textDecoration: 'none', color: 'inherit'}}>
                                <Card.Body>
                                    <Card.Title>{car.model}</Card.Title>
                                    <Card.Text><strong>Year:</strong> {car.year}</Card.Text>
                                    <Card.Text><strong>VIN:</strong> {car.vin}</Card.Text>
                                    <Card.Text><strong>Current mileage:</strong> {car.currentMileage}km</Card.Text>
                                </Card.Body>
                            </Link>
                            <Card.Footer>
                                <div className="d-flex justify-content-between">
                                    <Button variant="warning" onClick={() => handleEditCarOpen(car)}>Edit</Button>
                                    <Button variant="danger" onClick={() => handleDeleteCar(car.id)}>Delete</Button>
                                </div>
                            </Card.Footer>
                        </Card>
                    </Col>
                ))}
            </Row>

            <Modal show={showAddModal} onHide={() => setShowAddModal(false)}>
            <Modal.Header closeButton>
                    <Modal.Title>Add New Car</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form>
                        <Form.Group controlId="formModel">
                            <Form.Label>Model</Form.Label>
                            <Form.Control type="text" placeholder="Enter model"
                                          onChange={(e) => setNewCar({...newCar, model: e.target.value})}/>
                        </Form.Group>
                        <Form.Group controlId="formYear">
                            <Form.Label>Year</Form.Label>
                            <Form.Control type="number" placeholder="Enter year"
                                          onChange={(e) => setNewCar({...newCar, year: Number(e.target.value)})}/>
                        </Form.Group>
                        <Form.Group controlId="formVin">
                            <Form.Label>VIN</Form.Label>
                            <Form.Control type="text" placeholder="Enter VIN"
                                          onChange={(e) => setNewCar({...newCar, vin: e.target.value})}/>
                        </Form.Group>
                        <Form.Group controlId="formCurrentMileage">
                            <Form.Label>Current Mileage</Form.Label>
                            <Form.Control type="text" placeholder="Enter current mileage in km"
                                          onChange={(e) => setNewCar({...newCar, currentMileage: Number(e.target.value)})}/>
                        </Form.Group>
                    </Form>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => setShowAddModal(false)}>Close</Button>
                    <Button variant="primary" onClick={handleAddCar}>Add Car</Button>
                </Modal.Footer>
            </Modal>

            <Modal show={showEditModal} onHide={() => setShowEditModal(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>Edit Car</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    {editableCar && (
                        <Form>
                            <Form.Group controlId="formEditModel">
                                <Form.Label>Model</Form.Label>
                                <Form.Control type="text" placeholder="Enter model" value={editableCar.model}
                                              onChange={(e) => setEditableCar({
                                                  ...editableCar,
                                                  model: e.target.value
                                              })}/>
                            </Form.Group>
                            <Form.Group controlId="formEditYear">
                                <Form.Label>Year</Form.Label>
                                <Form.Control type="number" placeholder="Enter year" value={editableCar.year}
                                              onChange={(e) => setEditableCar({
                                                  ...editableCar,
                                                  year: Number(e.target.value)
                                              })}/>
                            </Form.Group>
                            <Form.Group controlId="formEditVin">
                                <Form.Label>VIN</Form.Label>
                                <Form.Control type="text" placeholder="Enter VIN" value={editableCar.vin}
                                              onChange={(e) => setEditableCar({...editableCar, vin: e.target.value})}/>
                            </Form.Group>
                            <Form.Group controlId="formEditCurrentMileage">
                                <Form.Label>Current Mileage</Form.Label>
                                <Form.Control type="text" placeholder="Enter Current Mileage in km" value={editableCar.currentMileage}
                                              onChange={(e) => setEditableCar({...editableCar, currentMileage: Number(e.target.value)})}/>
                            </Form.Group>
                        </Form>
                    )}
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => setShowEditModal(false)}>Cancel</Button>
                    <Button variant="primary" onClick={handleEditCarSave}>Save</Button>
                </Modal.Footer>
            </Modal>
        </Container>
    );
}

export default Home;
