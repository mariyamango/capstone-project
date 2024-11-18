import axios from 'axios';
import {useEffect, useState} from "react";
import {Alert, Button, Card, Col, Container, Form, Modal, Row} from 'react-bootstrap';
import {Car} from "./types/Car.tsx";
import {Link} from "react-router-dom";

function Home() {
    const [data, setData] = useState<Car[]>([]);
    const [showAddModal, setShowAddModal] = useState(false);
    const [showEditModal, setShowEditModal] = useState(false);
    const [newCar, setNewCar] = useState({model: '', year: 0, vin: '', currentMileage: 0});
    const [editableCar, setEditableCar] = useState<Car | null>(null);
    const [isFormValid, setIsFormValid] = useState(false);
    const [errorMessage, setErrorMessage] = useState<string | null>(null);

    const fetchData = async () => {
        try {
            const response = await axios.get<Car[]>("/api/cars");
            setData(response.data);
        } catch (error) {
            console.log('Error fetching data', error);
        }
    };

    useEffect(() => {
        fetchData();
    }, []);

    useEffect(() => {
        const validateForm = () => {
            const isAddFormValid = Boolean(newCar.model && newCar.year > 0 && newCar.vin && newCar.currentMileage > 0);
            const isEditFormValid = Boolean(editableCar && editableCar.model && editableCar.year > 0 && editableCar.vin && editableCar.currentMileage > 0);
            setIsFormValid(showAddModal ? isAddFormValid : isEditFormValid);
        };
        validateForm();
    }, [newCar, editableCar, showAddModal, showEditModal]);

    const handleError = (error: unknown) => {
        if (axios.isAxiosError(error)) {
            console.log('Error:', error);
            if (error.response && error.response.status === 400) {
                setErrorMessage("A car with this VIN already exists for the user.");
            } else {
                setErrorMessage("An unexpected error occurred. Please try again.");
            }
        } else {
            console.log('Unexpected error:', error);
            setErrorMessage("An unknown error occurred. Please try again.");
        }
    };

    const handleAddCar = async () => {
        try {
            const response = await axios.post("/api/cars", newCar);
            setData([...data, response.data]);
            setShowAddModal(false);
            setNewCar({model: '', year: 0, vin: '', currentMileage: 0});
            setErrorMessage(null);
        } catch (error: unknown) {
            handleError(error);
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
            } catch (error: unknown) {
                handleError(error);
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

    return (
        <Container className="my-5">
            <div className="d-flex justify-content-between align-items-center mb-4">
                <h1 className="your-cars-text">You cars:</h1>
                <Button onClick={() => setShowAddModal(true)} variant="primary">Add Car</Button>
            </div>

            <Row xs={1} md={2} lg={3} className="g-4">
                {data.map((car) => (
                    <Col key={car.id}>
                        <Card className="h-100">
                            <Link to={`/car/${car.id}`} style={{textDecoration: 'none', color: 'inherit'}}
                                  className="card-link">
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

            <Modal show={showAddModal} onHide={() => setShowAddModal(false)} className="work-modal">
                <Modal.Header closeButton>
                    <Modal.Title>Add New Car</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    {errorMessage && <Alert
                        variant="danger"
                        dismissible
                        onClose={() => setErrorMessage(null)}
                    >
                        {errorMessage}
                    </Alert>
                    }
                    <Form>
                        <Form.Group controlId="formModel">
                            <Form.Label>Model</Form.Label>
                            <Form.Control type="text"
                                          placeholder="Enter car model (e.g., Tesla Model 3)"
                                          onChange={(e) => setNewCar({...newCar, model: e.target.value})}
                                          className="custom-input"
                                          required
                            />
                        </Form.Group>
                        <Form.Group controlId="formYear">
                            <Form.Label>Year</Form.Label>
                            <Form.Control type="number"
                                          placeholder="Enter year of manufacture"
                                          onChange={(e) => setNewCar({...newCar, year: Number(e.target.value)})}
                                          className="custom-input"
                                          required
                            />
                        </Form.Group>
                        <Form.Group controlId="formVin">
                            <Form.Label>VIN</Form.Label>
                            <Form.Control type="text"
                                          placeholder="Enter VIN (Vehicle Identification Number)"
                                          onChange={(e) => setNewCar({...newCar, vin: e.target.value})}
                                          className="custom-input"
                                          required
                            />
                        </Form.Group>
                        <Form.Group controlId="formCurrentMileage">
                            <Form.Label>Current Mileage</Form.Label>
                            <Form.Control type="number"
                                          placeholder="Enter current mileage in km"
                                          onChange={(e) => setNewCar({
                                              ...newCar,
                                              currentMileage: Number(e.target.value)
                                          })}
                                          className="custom-input"
                                          required
                            />
                        </Form.Group>
                    </Form>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="danger" onClick={() => setShowAddModal(false)}>Close</Button>
                    <Button variant="primary" onClick={handleAddCar} disabled={!isFormValid}>Add Car</Button>
                </Modal.Footer>
            </Modal>

            <Modal show={showEditModal} onHide={() => setShowEditModal(false)} className="work-modal">
                <Modal.Header closeButton>
                    <Modal.Title>Edit Car</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    {errorMessage && <Alert
                        variant="danger"
                        dismissible
                        onClose={() => setErrorMessage(null)}
                    >
                        {errorMessage}
                    </Alert>
                    }
                    {editableCar && (
                        <Form>
                            <Form.Group controlId="formEditModel">
                                <Form.Label>Model</Form.Label>
                                <Form.Control type="text" placeholder="Enter model"
                                              value={editableCar.model}
                                              onChange={(e) => setEditableCar({
                                                  ...editableCar,
                                                  model: e.target.value
                                              })}
                                              className="custom-input"
                                              required
                                />
                            </Form.Group>
                            <Form.Group controlId="formEditYear">
                                <Form.Label>Year</Form.Label>
                                <Form.Control type="number"
                                              placeholder="Enter year"
                                              value={editableCar.year || ''}
                                              onChange={(e) => {
                                                  const value = e.target.value;
                                                  if (editableCar) {
                                                      setEditableCar({
                                                          ...editableCar,
                                                          year: value === '' ? 0 : Number(value)
                                                      });
                                                  }
                                              }}
                                              className="custom-input"
                                              required
                                />
                            </Form.Group>
                            <Form.Group controlId="formEditVin">
                                <Form.Label>VIN</Form.Label>
                                <Form.Control type="text"
                                              placeholder="Enter VIN"
                                              value={editableCar.vin}
                                              onChange={(e) => setEditableCar({
                                                  ...editableCar,
                                                  vin: e.target.value
                                              })}
                                              className="custom-input"
                                              required
                                />
                            </Form.Group>
                            <Form.Group controlId="formEditCurrentMileage">
                                <Form.Label>Current Mileage</Form.Label>
                                <Form.Control type="number"
                                              placeholder="Enter Current Mileage in km"
                                              value={editableCar.currentMileage || ''}
                                              onChange={(e) => {
                                                  const value = e.target.value;
                                                  if (editableCar) {
                                                      setEditableCar({
                                                          ...editableCar,
                                                          currentMileage: value === '' ? 0 : Number(value)
                                                      });
                                                  }
                                              }}
                                              className="custom-input"
                                              required
                                />
                            </Form.Group>
                        </Form>
                    )}
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="danger" onClick={() => setShowEditModal(false)}>Cancel</Button>
                    <Button variant="primary" onClick={handleEditCarSave} disabled={!isFormValid}>Save</Button>
                </Modal.Footer>
            </Modal>
        </Container>
    );
}

export default Home;
