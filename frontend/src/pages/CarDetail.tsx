import {useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import {Car} from "./types/Car";
import axios from "axios";
import {Work} from "./types/Work";
import {Card, Container, ListGroup} from 'react-bootstrap';

function CarDetail() {
    const {id} = useParams<{ id: string }>();
    const [car, setCar] = useState<Car | null>(null);
    const [works, setWorks] = useState<{ [type: string]: Work[] }>({});
    const [expanded, setExpanded] = useState<{ [type: string]: boolean }>({});

    useEffect(() => {
        const fetchCar = async () => {
            try {
                const response = await axios.get<Car>(`/api/cars/${id}`);
                setCar(response.data);
            } catch (error) {
                console.log('Error fetching car data', error);
            }
        };
        fetchCar();
    }, [id]);

    useEffect(() => {
        const fetchWorks = async () => {
            try {
                const response = await axios.get<Work[]>(`/api/works/${id}`);
                const grouped = response.data.reduce((acc: { [key: string]: Work[] }, work: Work) => {
                    if (!acc[work.type]) acc[work.type] = [];
                    acc[work.type].push(work);
                    return acc;
                }, {});
                setWorks(grouped);
            } catch (error) {
                console.log('Error fetching work data', error);
            }
        };
        fetchWorks();
    }, [id]);

    return (
        <Container className="my-5">
            {car ? (
                <Card>
                    <Card.Header>
                        <h2>{car.model} - {car.year}</h2>
                        <p>VIN: {car.vin}</p>
                    </Card.Header>
                    <Card.Body>
                        <h4>Works:</h4>
                        {Object.entries(works).map(([type, workList]) => {
                            const sortedWorkList = [...workList].sort(
                                (a, b) => new Date(b.date).getTime() - new Date(a.date).getTime()
                            );
                            const lastWork = sortedWorkList[0];

                            return (
                                <div key={type} className="work-group">
                                    <h5
                                        onClick={() => setExpanded({...expanded, [type]: !expanded[type]})}
                                        style={{cursor: 'pointer', color: 'darkblue'}}
                                    >
                                        {type} — Last work: {new Date(lastWork.date).toLocaleDateString()}
                                    </h5>

                                    {!expanded[type] && (
                                        <p>
                                            Last work mileage: {lastWork.mileage} km, Last work cost: €{lastWork.price}
                                        </p>
                                    )}

                                    {expanded[type] && (
                                        <ListGroup>
                                            {sortedWorkList.map(work => (
                                                <ListGroup.Item key={work.id}>
                                                    Date: {new Date(work.date).toLocaleDateString()},
                                                    Mileage: {work.mileage} km, Cost: €{work.price}
                                                </ListGroup.Item>
                                            ))}
                                        </ListGroup>
                                    )}
                                </div>
                            );
                        })}
                    </Card.Body>
                </Card>
            ) : (
                <p>Loading...</p>
            )}
        </Container>
    );
}

export default CarDetail;
