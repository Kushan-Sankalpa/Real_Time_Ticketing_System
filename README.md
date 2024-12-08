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
4. **IntelliJ IDEA**: Recommended for managing all parts of the project.


### CLI Simulation Setup

The CLI simulation allows you to test the core functionalities of the ticketing system outside the web interface.

#### Using IntelliJ IDEA

1. **Open the Project in IntelliJ IDEA**:
    - Launch **IntelliJ IDEA**.
    - Click on `File` > `Open...` and navigate to the `CLI` directory within your main project folder.
    - Select the project and click `OK` to open it.

2. **Compile the Java Files**:
    - IntelliJ IDEA automatically compiles Java files upon saving if configured.
    - Ensure there are no compilation errors in the `CLI` module.

3. **Run the Simulation**:
    - Locate the `RealTimeTicketingSystem` class in the project.
    - Right-click on the `RealTimeTicketingSystem` class and select `Run 'RealTimeTicketingSystem.main()'`.
    - The CLI will start and prompt you to configure the system or load an existing configuration.
    - Follow the on-screen instructions to set up ticketing rates, capacities, and the number of vendors and customers.

---
   

### Backend Setup (Spring Boot)

The backend of the Real-Time Event Ticketing System is built using **Spring Boot**, which was initialized using **Spring Initializr**. Follow the steps below to set up and run the backend application using **IntelliJ IDEA**.

#### Using IntelliJ IDEA

1. **Open the Project in IntelliJ IDEA**:
    - Launch **IntelliJ IDEA**.
    - Click on `File` > `Open...` and navigate to the `server` directory within your main project folder.
    - Select the project and click `OK` to open it.

2. **Import Maven Dependencies**:
    - IntelliJ IDEA typically detects Maven projects automatically. If not, right-click on the `pom.xml` file and select `Maven` > `Reload Project`.

3. **Configure Application Properties**:
    - Open `src/main/resources/application.properties`.
    - Ensure the following configurations are set:
        ```properties
        spring.application.name=Server
        spring.h2.console.enabled=true
        spring.h2.console.path=/h2-console

        spring.datasource.url=jdbc:h2:mem:ticketingsystemdb
        spring.datasource.driverClassName=org.h2.Driver
        spring.datasource.username=sa
        spring.datasource.password=

        spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
        spring.jpa.hibernate.ddl-auto=update
        spring.jpa.open-in-view=false
        ```

4. **Run the Application**:
    - In IntelliJ IDEA, navigate to the `ServerApplication` class located in the `org.example.server` package.
    - Right-click on the `ServerApplication` class and select `Run 'ServerApplication.main()'`.
    - The backend will start and be accessible at `http://localhost:8080`.

5. **Access the H2 Console**:
    - Open your web browser and navigate to `http://localhost:8080/h2-console`.
    - Use the following JDBC URL to connect:
        ```
        jdbc:h2:mem:ticketingsystemdb
        ```
    - Username: `sa`
    - Password: *(leave blank)*

---

### Frontend Setup (React)

1. **Open the Project in IntelliJ IDEA**:
    - Launch **IntelliJ IDEA**.
    - Click on `File` > `Open...` and navigate to the `client/Real-Time-Ticket-Gui` directory within your main project folder.
    - Select the project and click `OK` to open it.

2. **Install Dependencies**:
    - Open the built-in terminal in IntelliJ IDEA.
    - Navigate to the `client/Real-Time-Ticket-Gui` directory if not already there.
    - Run the following command to install the necessary Node.js dependencies:
        ```bash
        npm install
        ```

3. **Start the Application**:
    - After the dependencies are installed, start the React application by running:
        ```bash
        npm run dev
        ```
    - The frontend will start and be accessible at `http://localhost:5173`.

---

## Usage Instructions

### How to Configure and Start the System

#### Configuration via CLI:

1. **Run the CLI Simulation**:
    - In IntelliJ IDEA, ensure the CLI simulation is running.
    - If not, follow the [CLI Simulation Setup](#cli-simulation-setup) steps to start the CLI.

2. **Follow the Prompts in CLI**:
    - **Ticket Release Rate**: Enter the interval (in milliseconds) at which vendors release tickets.
    - **Customer Retrieval Rate**: Enter the interval (in milliseconds) at which customers purchase tickets.
    - **Maximum Ticket Capacity**: Set the maximum number of tickets that the pool can hold.
    - **Total Tickets**: Specify the total number of tickets for the event.
    - **Initial Tickets**: Input the number of tickets to add to the pool initially.
    - **Number of Vendors**: Enter the number of vendor threads to simulate.
    - **Number of Customers**: Enter the number of customer threads to simulate.

3. **Save or Load Configuration**:
    - You can save the current configuration to a JSON file or load an existing configuration based on your preference.

#### Configuration via GUI:

1. **Run the Backend Application**:
    - Ensure the Spring Boot backend is running by following the [Backend Setup](#backend-setup-spring-boot) steps.

2. **Access the Frontend**:
    - Open your web browser and navigate to `http://localhost:5173`.

3. **Fill Out the Configuration Form**:
    - **Ticket Release Rate**: Enter the interval (in milliseconds) at which vendors release tickets.
    - **Customer Retrieval Rate**: Enter the interval (in milliseconds) at which customers purchase tickets.
    - **Maximum Ticket Capacity**: Set the maximum number of tickets that the pool can hold.
    - **Total Tickets**: Specify the total number of tickets for the event.
    - **Initial Tickets**: Input the number of tickets to add to the pool initially.
    - **Number of Vendors**: Enter the number of vendor threads to simulate.
    - **Number of Customers**: Enter the number of customer threads to simulate.

4. **Save the Configuration**:
    - Click on **Save Configuration** to store the settings and initialize the system.

5. **Start the System**:
    - Navigate to the **Control Panel**.
    - Click on the **Start** button to begin ticketing operations.

---


### Explanation of GUI Controls

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
        - Vendor actions (e.g., "Vendor-1 added ticket: Ticket-1, [ Available Tickets in pool: 2 ]").
        - Customer actions (e.g., "Customer-2 purchased ticket: Ticket-1, [ Available Tickets in pool: 1 ]").
        - System notifications (e.g., "All tickets sold. Customers are exiting.").

---

## Contact Information

For any issues, questions, or contributions, please contact the project maintainer:

- **Name**: Kushan Sankalpa
- **Email**: kushansankalpa717@gmail.com
- **GitHub**: [Kushan-Sankalpa](https://github.com/Kushan-Sankalpa)

---

Thank you for using the **Real-Time Event Ticketing System**! I hope it serves your event management needs effectively.
