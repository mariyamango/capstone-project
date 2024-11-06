import {Button, Form, Modal} from "react-bootstrap";
import {Work} from "../types/Work";
import {WorkType} from "../types/WorkType";
import {ChangeEvent, useEffect, useState} from "react";
import axios from "axios";

interface WorkModalProps {
    show: boolean;
    editWork: Work | null;
    newWork: Partial<Work>;
    onClose: () => void;
    onSave: () => void;
    onChange: (field: keyof Work, value: number | string) => void;
}


function WorkModal({ show, editWork, newWork, onClose, onSave, onChange }: WorkModalProps) {
    const [workTypes, setWorkTypes] = useState<WorkType[]>([]);
    const [isFormValid, setIsFormValid] = useState(false);

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

    return (
        <Modal show={show} onHide={onClose}>
            <Modal.Header closeButton>
                <Modal.Title>{editWork ? 'Edit Work' : 'Add Work'}</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Form>
                    <Form.Group>
                        <Form.Label>Type</Form.Label>
                        <Form.Select
                            value={editWork ? editWork.workTypeId : newWork.workTypeId || ""}
                            onChange={handleWorkTypeChange}
                            required
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
                            onChange={(e: ChangeEvent<HTMLInputElement>) => onChange('mileage', parseInt(e.target.value))}
                            required
                        />
                    </Form.Group>
                    <Form.Group>
                        <Form.Label>Date</Form.Label>
                        <Form.Control
                            type="date"
                            value={editWork ? editWork.date : newWork.date || ""}
                            onChange={(e: ChangeEvent<HTMLInputElement>) => onChange('date', e.target.value)}
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
                            required
                        />
                    </Form.Group>
                </Form>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={onClose}>Cancel</Button>
                <Button variant="primary" onClick={handleSave} disabled={!isFormValid}>
                    {editWork ? 'Save Changes' : 'Add Work'}
                </Button>
            </Modal.Footer>
        </Modal>
    );
}



export default WorkModal;