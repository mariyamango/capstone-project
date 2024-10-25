import axios from 'axios';
import {useEffect, useState} from "react";

function Home(){
    const [data, setData] = useState([]);

    const fetchData = async () => {
        axios.get("/api/cars")
            .then((response) => {
                setData(response.data);
            })
            .catch((error) => {
                console.log('Error fetching data',error);
            });
    };

    useEffect(() => {
        fetchData();
    },[])

    return <>
        <div className="container">
            <h1>Cars List:</h1>
            <ul>
                {data}
            </ul>
        </div>
    </>
}

export default Home;