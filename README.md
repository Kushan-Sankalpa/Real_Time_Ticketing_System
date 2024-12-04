# Real-Time Event Ticketing System

## Introduction
The **Real-Time Event Ticketing System** is a comprehensive platform designed to manage event ticket sales in real-time. It consists of a **backend** implemented in **Spring Boot**, a **frontend** built with **React**, and a **CLI-based simulation** for testing core functionalities. The system handles ticket release and purchase processes, allowing vendors to add tickets and customers to purchase them in a synchronized manner. It also includes real-time logs, statistics, and ticket status for effective monitoring.

---

## Setup Instructions

### Prerequisites
Ensure you have the following installed on your system:

1. **Java Development Kit (JDK)**: Version 17 or higher.
2. **Node.js**: Version 18 or higher.
3. **npm (Node Package Manager)**: Included with Node.js.
4. **Maven**: For building the Spring Boot application.

### Backend Setup (Spring Boot)

The backend of the Real-Time Event Ticketing System is built using **Spring Boot**, which was initialized using **Spring Initializr**. Follow the steps below to set up and run the backend application.

1. **Clone the Repository**:
    ```bash
    git clone <repository-url>
    cd <repository-folder>/server
    ```

2. **Project Structure**:
    The backend project was generated using **Spring Initializr** with the following configurations:
    - **Project**: Maven Project
    - **Language**: Java
    - **Spring Boot**: 2.7.x or higher
    - **Dependencies**:
        - Spring Web
        - Spring Data JPA
        - H2 Database (for in-memory testing)
        - Spring Boot DevTools (optional, for development convenience)
        - Lombok (optional, for reducing boilerplate code)

3. **Building the Backend Application**:
    Ensure you are in the `server` directory of the cloned repository.
    ```bash
    mvn clean install
    ```

4. **Running the Backend Application**:
    ```bash
    mvn spring-boot:run
    ```
    The backend will start and be accessible at `http://localhost:8080`.

5. **Configuration**:
    - **application.properties**:
        Ensure the `src/main/resources/application.properties` file is configured correctly. For example, to use the H2 in-memory database:
        ```properties
        spring.application.name=Server
        spring.h2.console.enabled=true
        spring.h2.console.path=/h2-console
        
        spring.datasource.url=jdbc:h2:mem:ticketingsystemdb;
        spring.datasource.driverClassName=org.h2.Driver
        spring.datasource.username=sa
        spring.datasource.password=
        
        spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
        spring.jpa.hibernate.ddl-auto=update
        spring.jpa.open-in-view=false
        ```
    - **Database Access**:
        Access the H2 console at `http://localhost:8080/h2-console` with the JDBC URL `jdbc:h2:mem:ticketingdb`.

### Frontend Setup (React)

1. **Navigate to the Frontend Directory**:
    ```bash
    cd <repository-folder>/client
    ```

2. **Install Dependencies**:
    ```bash
    npm install
    ```

3. **Start the Application**:
    ```bash
    npm run dev
    ```
    The frontend will start and be accessible at `http://localhost:5176`.

4. **Configuration**:
    - Ensure that the frontend is correctly configured to communicate with the backend API at `http://localhost:8080`.
    - Modify the API base URL in the frontend configuration files if necessary.

### CLI Simulation Setup

1. **Navigate to the CLI Directory**:
    ```bash
    cd <repository-folder>/cli
    ```

2. **Compile the Java Files**:
    ```bash
    javac *.java
    ```

3. **Run the Simulation**:
    ```bash
    java Main_TicketingSystem
    ```

    - The CLI will prompt you to configure the system or load an existing configuration.
    - Follow the on-screen instructions to set up ticketing rates, capacities, and the number of vendors and customers.

---

## Usage Instructions

### How to Configure and Start the System

#### Configuration via CLI:
- **Run the CLI Simulation**:
    ```bash
    java Main_TicketingSystem
    ```
- **Follow the Prompts**:
    - **Ticket Release Rate**: Enter the interval (in milliseconds) at which vendors release tickets.
    - **Customer Retrieval Rate**: Enter the interval (in milliseconds) at which customers purchase tickets.
    - **Maximum Ticket Capacity**: Set the maximum number of tickets that the pool can hold.
    - **Total Tickets**: Specify the total number of tickets for the event.
    - **Initial Tickets**: Input the number of tickets to add to the pool initially.
    - **Number of Vendors**: Enter the number of vendor threads to simulate.
    - **Number of Customers**: Enter the number of customer threads to simulate.
- **Save or Load Configuration**:
    - You can save the current configuration to a JSON file or load an existing configuration.

#### Configuration via UI:
1. **Access the Frontend**:
    - Open your web browser and navigate to `http://localhost:5176`.

2. **Fill Out the Configuration Form**:
    - **Ticket Release Rate**: Enter the interval (in milliseconds) at which vendors release tickets.
    - **Customer Retrieval Rate**: Enter the interval (in milliseconds) at which customers purchase tickets.
    - **Maximum Ticket Capacity**: Set the maximum number of tickets that the pool can hold.
    - **Total Tickets**: Specify the total number of tickets for the event.
    - **Initial Tickets**: Input the number of tickets to add to the pool initially.
    - **Number of Vendors**: Enter the number of vendor threads to simulate.
    - **Number of Customers**: Enter the number of customer threads to simulate.

3. **Save the Configuration**:
    - Click on **Save Configuration** to store the settings and initialize the system.

4. **Start the System**:
    - Navigate to the **Control Panel**.
    - Click on the **Start** button to begin ticketing operations.

---

### Explanation of UI Controls

1. **Configuration Form**:
    - **Save Configuration**: Saves the provided configuration and initializes the system with these settings.
    - **Clear Configuration**: Clears all input fields in the form for reconfiguration.

2. **Control Panel**:
    - **Start**: Starts the ticket release and purchase processes based on the saved configuration.
    - **Stop**: Stops all vendor and customer operations gracefully.
    - **Reset**: Resets the system by clearing all tickets, vendors, customers, and configurations.

3. **Ticket Statistics**:
    - **Real-Time Statistics**: Displays up-to-date information on:
        - **Total Tickets Sold**: Number of tickets that have been purchased by customers.
        - **Total Tickets Released**: Number of tickets that have been released by vendors.
        - **Tickets Yet to Release**: Number of tickets remaining to be released by vendors.

4. **Real-Time Ticket Status**:
    - **Available Tickets in Pool**: Shows the current number of tickets available in the ticket pool in real-time.
    - **Updates**: The number updates dynamically as tickets are added by vendors and purchased by customers.
    - **Visualization**: Includes a progress bar or numerical display to easily track ticket availability.

5. **Real-Time Log Display**:
    - Located on the frontend dashboard, it shows logs of ticket releases and purchases in real-time.
    - **Logs Include**:
        - Vendor actions (e.g., "Vendor-1 added ticket: Ticket-1").
        - Customer actions (e.g., "Customer-2 purchased ticket: Ticket-1").
        - System notifications (e.g., "All tickets sold. Customers are exiting.").

---

## Additional Information

### Error Handling
- The system includes validation for configuration inputs to ensure all values are positive integers and within logical bounds.
- Error messages are displayed in the UI for any invalid inputs.

### WebSocket Communication
- The frontend and backend communicate using WebSocket for real-time updates.
- This ensures immediate feedback on ticket status, logs, and statistics without needing to refresh the page.

### Thread Management
- The backend handles vendor and customer operations using multithreading to simulate concurrent ticket release and purchase processes.
- Threads are managed efficiently to prevent resource leaks and ensure smooth operation.

---

## API Documentation

### Configuration Endpoints
- **Save Configuration**
    - **URL**: `/api/configurations/saveConfigurations`
    - **Method**: `POST`
    - **Body**: `ConfigurationDTO` JSON
    - **Description**: Saves a new configuration.

- **Delete Latest Configuration**
    - **URL**: `/api/configurations/deleteLatest`
    - **Method**: `DELETE`
    - **Description**: Deletes the latest configuration.

### System Control Endpoints
- **Start System**
    - **URL**: `/api/system/start`
    - **Method**: `GET`
    - **Description**: Starts the ticketing system with the latest configuration.

- **Stop System**
    - **URL**: `/api/system/stop`
    - **Method**: `GET`
    - **Description**: Stops the ticketing system.

- **Reset System**
    - **URL**: `/api/system/reset`
    - **Method**: `DELETE`
    - **Description**: Resets the ticketing system by clearing all data.

- **Get Available Tickets**
    - **URL**: `/api/tickets/getAvailableTickets`
    - **Method**: `GET`
    - **Description**: Retrieves the number of available tickets.

- **Get Ticket Statistics**
    - **URL**: `/api/tickets/statistics`
    - **Method**: `GET`
    - **Description**: Retrieves current ticket statistics.

### Log Endpoint
- **Real-Time Logs**
    - **WebSocket Endpoint**: `/topic/logs`
    - **Description**: Streams real-time logs of ticket releases and purchases.

---

## Contact Information

For any issues, questions, or contributions, please contact the project maintainer:

- **Name**: Kushan Sankalpa
- **Email**: kushansankalpa717@gmail.com
- **GitHub**:https://github.com/Kushan-Sankalpa


