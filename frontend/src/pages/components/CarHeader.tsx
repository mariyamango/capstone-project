import {Button, Card} from "react-bootstrap";
import {Car} from "../types/Car.tsx";

interface CarHeaderProps {
    car: Car;
    onAddWork: () => void;
}

function CarHeader({ car, onAddWork }: CarHeaderProps) {
    return (
        <Card.Header>
            <h2>{car.model} - {car.year}</h2>
            <p>VIN: {car.vin}</p>
            <p>Current mileage: {car.currentMileage}km</p>
            <Button variant="primary" onClick={onAddWork}>Add Work</Button>
        </Card.Header>
    );
}

export default CarHeader;
