import {useState} from "react";
import {Button, ListGroup} from "react-bootstrap";
import {Work} from "../types/Work";

interface WorkGroupProps {
    type: string;
    works: Work[];
    onEditWork: (work: Work) => void;
    onDeleteWork: (workId: string, type: string) => void;
}

function WorkGroup({ type, works, onEditWork, onDeleteWork }: WorkGroupProps) {
    const [expanded, setExpanded] = useState(false);
    const sortedWorkList = [...works].sort((a, b) => new Date(b.date).getTime() - new Date(a.date).getTime());
    const lastWork = sortedWorkList[0];

    return (
        <div key={type} className="work-group">
            <button
                onClick={() => setExpanded(!expanded)}
                className="clickable-header"
                role="heading"
                aria-level={5}
            >
                {type} — Last work: {new Date(lastWork.date).toLocaleDateString()}
            </button>

            {!expanded && (
                <p>Last work mileage: {lastWork.mileage} km, Last work cost: €{lastWork.price}</p>
            )}

            {expanded && (
                <ListGroup>
                    {sortedWorkList.map(work => (
                        <ListGroup.Item key={work.id}>
                            Date: {new Date(work.date).toLocaleDateString()},
                            Mileage: {work.mileage} km, Cost: €{work.price}
                            <Button variant="link" onClick={() => onEditWork(work)}>Edit</Button>
                            <Button variant="link" onClick={() => onDeleteWork(work.id, type)}>Delete</Button>
                        </ListGroup.Item>
                    ))}
                </ListGroup>
            )}
        </div>
    );
}

export default WorkGroup;
