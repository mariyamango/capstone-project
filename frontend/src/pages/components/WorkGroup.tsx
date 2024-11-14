import { useEffect, useState } from "react";
import { Button, ListGroup } from "react-bootstrap";
import { WorkCountdown } from "../types/WorkCountdown";

interface WorkGroupProps {
    type: string;
    workCountdowns: WorkCountdown[];
    onEditWork: (work: WorkCountdown) => void;
    onDeleteWork: (workId: string, type: string) => void;
}

function WorkGroup({ type, workCountdowns, onEditWork, onDeleteWork }: WorkGroupProps) {
    const [expanded, setExpanded] = useState(false);
    const [lastWork, setLastWork] = useState<WorkCountdown | null>(null);

    useEffect(() => {
        const sortedByDate = workCountdowns.sort((a, b) => new Date(b.date).getTime() - new Date(a.date).getTime());
        setLastWork(sortedByDate[0] || null);
    }, [workCountdowns]);

    const getMileageColor = (mileageLeft: number) => {
        if (mileageLeft > 3000) return "text-success";
        if (mileageLeft > 1000) return "text-warning";
        return "text-danger";
    };

    const getDaysColor = (daysLeft: number) => {
        if (daysLeft > 60) return "text-success";
        if (daysLeft > 30) return "text-warning";
        return "text-danger";
    };

    return (
        <div key={type} className="work-group">
            <button
                onClick={() => setExpanded(!expanded)}
                className="clickable-header"
                role="heading"
                aria-level={5}
                style={{ display: "flex", justifyContent: "space-between", width: "100%" }}
            >
                <span>
                    {type} — Last work: {lastWork ? new Date(lastWork.date).toLocaleDateString() : "No data"}
                </span>
                <span style={{textAlign: "right"}}>
                    Mileage left: <span className={getMileageColor(lastWork?.mileageLeft || 0)}>
                        {lastWork?.mileageLeft ?? "N/A"}
                    </span> km, Days left: <span className={getDaysColor(lastWork?.daysLeft || 0)}>
                        {lastWork?.daysLeft ?? "N/A"}
                    </span> days
                </span>
            </button>

            {!expanded && lastWork && (
                <p>Last work mileage: {lastWork.mileage} km, Last work cost: €{lastWork.price}</p>
            )}

            {expanded && (
                <ListGroup>
                    {workCountdowns.map(work => (
                        <ListGroup.Item key={work.id} className="d-flex justify-content-between align-items-center">
                            Date: {new Date(work.date).toLocaleDateString()},
                            Mileage: {work.mileage} km, Cost: €{work.price}
                            <div className="d-flex gap-2">
                                <Button variant="warning" onClick={() => onEditWork(work)}>Edit</Button>
                                <Button variant="danger" onClick={() => onDeleteWork(work.id, type)}>Delete</Button>
                            </div>
                        </ListGroup.Item>
                        ))}
                </ListGroup>
            )}
        </div>
    );
}

export default WorkGroup;