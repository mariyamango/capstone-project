import React, { useState, useEffect } from 'react';
import axios from 'axios';
import '../styles/Chuck.css';

const Chuck: React.FC = () => {
    const [joke, setJoke] = useState<string | null>(null);

    useEffect(() => {
        const fetchJoke = async () => {
            try {
                const response = await axios.get<string>(`/api/joke`);
                console.log('Joke response:', response.data);
                setJoke(response.data);
            } catch (error) {
                console.error('Error fetching joke data', error);
                setJoke('Failed to load a joke.');
            }
        };
        fetchJoke();
    }, []);

    return (
        <div className="chuck-container">
            <div className="chuck-card">
                <h1 className="chuck-title">Joke of the Day:</h1>
                {joke ? <p className="chuck-text">{joke}</p> : <p>Loading...</p>}
            </div>
        </div>
    );
};

export default Chuck;