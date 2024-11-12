# CarHealth Monitor

CarHealth Monitor is a web application designed to help users track and manage their car maintenance and repair history, providing an overview of service records, including dates and costs. It also offers countdowns to the next required service based on mileage and time.

## Why CarHealth Monitor?

CarHealth Monitor simplifies vehicle maintenance tracking, allowing users to:
- View all service activities, including repair costs and dates.
- Receive automatic countdowns to the next recommended maintenance.
- Easily access their car's service history in one place.

## Getting Started

To set up the project locally:

1. **Clone the repository**:
    ```bash
    git clone https://github.com/mariyamango/capstone-project.git
    ```
2. **Backend**:
    - Navigate to the backend folder and install dependencies via Maven:
      ```bash
      cd capstone-project/backend
      mvn install
      ```
    - Start the Spring Boot application:
      ```bash
      mvn spring-boot:run
      ```

3. **Frontend**:
    - Navigate to the frontend folder, install dependencies, and run the application:
      ```bash
      cd capstone-project/frontend
      npm install
      npm start
      ```

4. **Database**:
    - Ensure MongoDB is running locally or configure the database connection in the backend. If using MongoDB Atlas, you may need to update the application.properties file with the correct database URI.

5. **Access**:
   - Access the application locally at `http://localhost:5173`.
   - You can access the live version of the CarHealth Monitor on Heroku at: https://carhealth-monitor-395543a457fa.herokuapp.com/

## Need Help?

For assistance, please open an issue on the [GitHub Issues page](https://github.com/mariyamango/capstone-project/issues).

## Contributing

CarHealth Monitor is supported by [Mariia Somkova](https://github.com/mariyamango). External contributions are currently limited, but users are welcome to submit suggestions or report issues on the Issues page.