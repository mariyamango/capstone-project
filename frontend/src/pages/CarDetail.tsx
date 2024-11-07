import {useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import {Car} from "./types/Car";
import axios from "axios";
import {WorkCountdown} from "./types/WorkCountdown";
import {Container} from 'react-bootstrap';
import CarHeader from "./components/CarHeader.tsx";
import WorkGroup from "./components/WorkGroup.tsx";
import WorkModal from "./components/WorkModal.tsx";
import {WorkSummaryResponse} from "./types/WorkSummaryResponse.tsx";

function CarDetail() {
    const {id} = useParams<{ id: string }>();
    const [car, setCar] = useState<Car | null>(null);
    const [worksByType, setWorksByType] = useState<{ [key: string]: WorkCountdown[] }>({});
    const [totalByType, setTotalByType] = useState<{ [key: string]: number }>({});
    const [grandTotal, setGrandTotal] = useState<number>(0);
    const [showModal, setShowModal] = useState(false);
    const [editWork, setEditWork] = useState<WorkCountdown | null>(null);
    const [newWork, setNewWork] = useState<Partial<WorkCountdown>>({
        carId: id || '',
        type: '',
        mileage: 0,
        date: '',
        price: 0,
    });

    useEffect(() => {
        const fetchCar = async () => {
            try {
                const response = await axios.get<Car>(`/api/cars/${id}`);
                setCar(response.data);
            } catch (error) {
                console.error('Error fetching car data', error);
            }
        };
        fetchCar();
    }, [id]);

    const fetchWorkCountdowns = async () => {
        try {
            const response = await axios.get<WorkSummaryResponse>(`/api/works/${id}/countdowns`);
            const {workCountdowns, totalByType, grandTotal} = response.data;
            const groupedWorks = workCountdowns.reduce((acc: {
                [key: string]: WorkCountdown[]
            }, work: WorkCountdown) => {
                if (!acc[work.type]) acc[work.type] = [];
                acc[work.type].push(work);
                return acc;
            }, {});
            setWorksByType(groupedWorks);
            setTotalByType(totalByType);
            setGrandTotal(grandTotal);
        } catch (error) {
            console.error('Error fetching work countdown data', error);
        }
    };

    useEffect(() => {
        fetchWorkCountdowns();
    }, [id]);

    const openAddModal = () => {
        setNewWork({carId: id || '', type: '', mileage: 0, date: '', price: 0});
        setEditWork(null);
        setShowModal(true);
    };

    const openEditModal = (work: WorkCountdown) => {
        setEditWork(work);
        setShowModal(true);
    };

    const closeModal = () => {
        setShowModal(false);
        setEditWork(null);
    };

    const handleSaveWork = async () => {
        try {
            if (editWork) {
                await axios.put(`/api/works/${editWork.id}`, editWork);
            } else {
                await axios.post(`/api/works`, newWork);
            }
            closeModal();
            await fetchWorkCountdowns();
        } catch (error) {
            console.error("Error saving work data", error);
        }
    };

    const handleDeleteWork = async (workId: string) => {
        try {
            await axios.delete(`/api/works/${workId}`);
            await fetchWorkCountdowns();
        } catch (error) {
            console.error("Error deleting work", error);
        }
    };

    return (
        <Container className="my-5">
            {car ? (
                <>
                    <CarHeader car={car} onAddWork={openAddModal}/>
                    <hr className="my-4"/>
                    <h4>Works:</h4>
                    {Object.entries(worksByType).map(([type, workList]) => (
                        <WorkGroup
                            key={type}
                            type={type}
                            workCountdowns={workList}
                            onEditWork={openEditModal}
                            onDeleteWork={handleDeleteWork}
                        />
                    ))}
                    <hr className="my-4"/>
                    <div className="my-4">
                        <div>
                            <h5>Total amount for each type of work:</h5>
                            <ul>
                                {totalByType && Object.entries(totalByType).map(([type, total]) => (
                                    <li key={type}>
                                        {worksByType[type]?.[0]?.type || type}: €{total.toFixed(2)}
                                    </li>
                                ))}
                            </ul>
                        </div>
                        <hr className="my-4"/>
                        <div>
                            <h5>Total amount of all work: €{grandTotal.toFixed(2)}</h5>
                        </div>
                    </div>
                </>
            ) : (
                <p>Loading...</p>
            )}

            <WorkModal
                show={showModal}
                editWork={editWork}
                newWork={newWork}
                onClose={closeModal}
                onSave={handleSaveWork}
                onChange={(field, value) => {
                    if (editWork) {
                        setEditWork((prev) => ({...prev, [field]: value} as WorkCountdown));
                    } else {
                        setNewWork((prev) => ({...prev, [field]: value}));
                    }
                }}
            />
        </Container>
    );
}

export default CarDetail;
