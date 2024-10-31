import {Modal, Button, Form} from "react-bootstrap";
import {Work} from "../types/Work";
import {ChangeEvent} from "react";

interface WorkModalProps {
    show: boolean;
    editWork: Work | null;
    newWork: Partial<Work>;
    onClose: () => void;
    onSave: () => void;
    onChange: (field: keyof Work, value : number | string) => void;
}

function WorkModal({ show, editWork, newWork, onClose, onSave, onChange }: WorkModalProps) {
    return (
        <Modal show={show} onHide={onClose}>
            <Modal.Header closeButton>
                <Modal.Title>{editWork ? 'Edit Work' : 'Add Work'}</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Form>
                    <Form.Group>
                        <Form.Label>Type</Form.Label>
                        <Form.Control
                            type="text"
                            value={editWork ? editWork.type : newWork.type}
                            onChange={(e: ChangeEvent<HTMLInputElement>) => onChange('type', e.target.value)}
                        />
                    </Form.Group>
                    <Form.Group>
                        <Form.Label>Mileage</Form.Label>
                        <Form.Control
                            type="number"
                            value={editWork ? editWork.mileage : newWork.mileage}
                            onChange={(e: ChangeEvent<HTMLInputElement>) => onChange('mileage', parseInt(e.target.value))}
                        />
                    </Form.Group>
                    <Form.Group>
                        <Form.Label>Date</Form.Label>
                        <Form.Control
                            type="date"
                            value={editWork ? editWork.date : newWork.date}
                            onChange={(e: ChangeEvent<HTMLInputElement>) => onChange('date', e.target.value)}
                        />
                    </Form.Group>
                    <Form.Group>
                        <Form.Label>Price</Form.Label>
                        <Form.Control
                            type="number"
                            value={editWork ? editWork.price : newWork.price}
                            onChange={(e: ChangeEvent<HTMLInputElement>) => onChange('price', parseFloat(e.target.value))}
                        />
                    </Form.Group>
                </Form>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={onClose}>Cancel</Button>
                <Button variant="primary" onClick={onSave}>{editWork ? 'Save Changes' : 'Add Work'}</Button>
            </Modal.Footer>
        </Modal>
    );
}

export default WorkModal;
