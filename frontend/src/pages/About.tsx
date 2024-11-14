import React from 'react';
import '../styles/About.css';

const About: React.FC = () => {
    return (
        <div className="container my-5 about-container">
            <h1 className="text-center mb-4">About CarHealth Monitor</h1>
            <p className="lead text-center">
                CarHealth Monitor is a comprehensive vehicle maintenance tracking application designed to help car owners manage and organize their vehicle service history for multiple cars. By centralizing all maintenance records, it ensures that users can keep track of essential repairs and services, helping to maintain their vehicle’s performance and value.
            </p>

            <h2 className="mt-5">Why Use CarHealth Monitor?</h2>
            <p>
                Owning a vehicle requires regular upkeep and timely repairs, which can be challenging to track. CarHealth Monitor offers a user-friendly solution for vehicle owners by:
            </p>
            <ul className="list-group list-group-flush">
                <li className="list-group-item">
                    <strong>Tracking Service Records:</strong> Store and access all your vehicle’s maintenance records, including repair dates, service costs, and mileage.
                </li>
                <li className="list-group-item">
                    <strong>Countdown to Next Service:</strong> Get an automatic countdown to the next recommended maintenance based on mileage or time since the last service.
                </li>
                <li className="list-group-item">
                    <strong>Multi-Car Support:</strong> Manage and view maintenance records for multiple vehicles in a single platform.
                </li>
            </ul>

            <h2 className="mt-5">How It Works</h2>
            <ol className="list-group list-group-numbered">
                <li className="list-group-item">
                    <strong>Log Maintenance and Repairs:</strong> Add details of each service visit, including the type of work done, cost, date, and mileage.
                </li>
                <li className="list-group-item">
                    <strong>Track Service Countdown:</strong> The application calculates countdowns for upcoming maintenance based on past service history, helping you keep your car in top shape.
                </li>
                <li className="list-group-item">
                    <strong>View History at a Glance:</strong> Easily access a summary or detailed history of all service records for each car you own.
                </li>
            </ol>

            <h2 className="mt-5">Getting Started</h2>
            <p>
                To start using CarHealth Monitor:
            </p>
            <ul className="list-group list-group-flush">
                <li className="list-group-item">Install the backend and frontend components (refer to the
                    project’s <a href="https://github.com/mariyamango/capstone-project/blob/master/README.md" target="_blank" rel="noopener noreferrer">README.md</a> for detailed setup instructions).
                </li>
                <li className="list-group-item">Run the backend server and the frontend application locally.</li>
                <li className="list-group-item">Create entries for each vehicle, and start logging service records.</li>
            </ul>

            <h2 className="mt-5">Need Help?</h2>
            <p>
                If you encounter any issues or bugs, please open an issue on the <a href="https://github.com/mariyamango/capstone-project/issues" target="_blank" rel="noopener noreferrer">GitHub Issues page</a>.
            </p>

            <h2 className="mt-5">My Mission</h2>
            <p>
                CarHealth Monitor aims to simplify vehicle maintenance management, making it easy for owners to keep their vehicles in top condition. By helping users stay on top of maintenance schedules, I hope to contribute to safer roads and more reliable vehicles.
            </p>
        </div>
    );
};

export default About;
