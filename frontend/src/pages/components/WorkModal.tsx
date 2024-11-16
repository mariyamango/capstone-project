import {Button, Form, Modal, Alert} from "react-bootstrap";
import {Work} from "../types/Work";
import {WorkType} from "../types/WorkType";
import {ChangeEvent, useEffect, useState} from "react";
import axios from "axios";

interface WorkModalProps {
    show: boolean;
    editWork: Work | null;
    newWork: Partial<Work>;
    currentMileage: number;
    onClose: () => void;
    onSave: () => void;
    onChange: (field: keyof Work, value: number | string) => void;
}


function WorkModal({show, editWork, newWork, currentMileage, onClose, onSave, onChange}: WorkModalProps) {
    const [showAlert, setShowAlert] = useState(false);
    const [alertMessage, setAlertMessage] = useState("");
    const [workTypes, setWorkTypes] = useState<WorkType[]>([]);
    const [isFormValid, setIsFormValid] = useState(false);
    const today = new Date().toISOString().split('T')[0];

    useEffect(() => {
        const fetchWorkTypes = async () => {
            try {
                const response = await axios.get('/api/work-types');
                setWorkTypes(response.data);
            } catch (error) {
                console.error("Error fetching work types:", error);
            }
        };

        fetchWorkTypes();
    }, []);

    useEffect(() => {
        const work = editWork || newWork;
        const isValid = Boolean(work.type) && (work.mileage !== undefined && work.mileage > 0) && Boolean(work.date) && (work.price !== undefined && work.price >= 0);
        setIsFormValid(isValid);
    }, [editWork, newWork]);

    const handleWorkTypeChange = (e: ChangeEvent<HTMLSelectElement>) => {
        const selectedTypeId = e.target.value;
        const selectedType = workTypes.find(type => type.id === selectedTypeId);

        if (selectedType) {
            onChange('workTypeId', selectedType.id);
            onChange('type', selectedType.workTypeName);
        } else {
            onChange('workTypeId', "");
            onChange('type', "");
        }
    };

    const handleSave = () => {
        if (isFormValid) {
            onSave();
        }
    };

    const handleDateChange = (e: ChangeEvent<HTMLInputElement>) => {
        const selectedDate = e.target.value;
        if (selectedDate > today) {
            setAlertMessage("Date cannot be in the future");
            setShowAlert(true);
        } else {
            onChange('date', selectedDate);
            setShowAlert(false); // Reset alert
        }
    };

    const handleMileageChange = (e: ChangeEvent<HTMLInputElement>) => {
        const selectedMileage = parseInt(e.target.value);
        if (selectedMileage > currentMileage) {
            setAlertMessage("Mileage cannot exceed the current mileage of the car");
            setShowAlert(true);
        } else {
            onChange('mileage', selectedMileage);
            setShowAlert(false); // Reset alert
        }
    };

    return (
        <>
            <Modal show={show} onHide={onClose} className="work-modal">
                <Modal.Header closeButton>
                    <Modal.Title>{editWork ? 'Edit Work' : 'Add Work'}</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form>
                        <Form.Group>
                            <Form.Label>Type</Form.Label>
                            <Form.Select
                                value={editWork ? editWork.workTypeId : newWork.workTypeId || ""}
                                className="custom-input"
                                onChange={handleWorkTypeChange}
                            >
                                <option value="">Select work type</option>
                                {workTypes.map(type => (
                                    <option key={type.id} value={type.id}>{type.workTypeName}</option>
                                ))}
                            </Form.Select>
                        </Form.Group>
                        <Form.Group>
                            <Form.Label>Mileage</Form.Label>
                            <Form.Control
                                type="number"
                                value={editWork ? editWork.mileage : newWork.mileage || ""}
                                onChange={handleMileageChange}
                                placeholder="Enter the mileage in km at which the work was performed"
                                className="custom-input"
                                max={currentMileage}
                                required
                            />
                        </Form.Group>
                        <Form.Group>
                            <Form.Label>Date</Form.Label>
                            <Form.Control
                                type="date"
                                value={editWork ? editWork.date : newWork.date || ""}
                                onChange={handleDateChange}
                                className="custom-input"
                                max={today}
                                required
                            />
                        </Form.Group>
                        <Form.Group>
                            <Form.Label>Price</Form.Label>
                            <Form.Control
                                type="number"
                                inputMode="decimal"
                                step="0.01"
                                value={editWork ? editWork.price : newWork.price || ""}
                                onChange={(e: ChangeEvent<HTMLInputElement>) => {
                                    const value = parseFloat(e.target.value);
                                    onChange('price', isNaN(value) ? 0 : value);
                                }}
                                placeholder="Enter the cost of the work"
                                className="custom-input"
                                required
                            />
                        </Form.Group>
                    </Form>
                    {showAlert && (
                        <Alert variant="danger" onClose={() => setShowAlert(false)} dismissible>
                            {alertMessage}
                        </Alert>
                    )}
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="danger" onClick={onClose}>Cancel</Button>
                    <Button variant="primary" onClick={handleSave} disabled={!isFormValid}>
                        {editWork ? 'Save Changes' : 'Add Work'}
                    </Button>
                </Modal.Footer>
            </Modal>
        </>
    );
}


export default WorkModal;