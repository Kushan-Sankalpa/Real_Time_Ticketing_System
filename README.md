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
4. **Maven**: For building the Spring Boot application (optional if using an IDE like IntelliJ).

### Backend Setup (Spring Boot)

The backend of the Real-Time Event Ticketing System is built using **Spring Boot**, which was initialized using **Spring Initializr**. Follow the steps below to set up and run the backend application using **IntelliJ IDEA** or the **command line**.

#### Option 1: Using IntelliJ IDEA

1. **Clone the Repository**:
    ```bash
    git clone https://github.com/Kushan-Sankalpa/Real-Time-Event-Ticketing-System.git
    cd Real-Time-Event-Ticketing-System/server
    ```
    > **Note**: Replace the URL with your actual repository URL if different.

2. **Open the Project in IntelliJ IDEA**:
    - Launch **IntelliJ IDEA**.
    - Click on `File` > `Open...` and navigate to the `server` directory of the cloned repository.
    - Select the project and click `OK` to open it.

3. **Import Maven Dependencies**:
    - IntelliJ IDEA typically detects Maven projects automatically. If not, right-click on the `pom.xml` file and select `Maven` > `Relod Project`.

4. **Configure Application Properties**:
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

5. **Run the Application**:
    - In IntelliJ IDEA, navigate to the `ServerApplication` class located in the `org.example.server` package.
    - Right-click on the `ServerApplication` class and select `Run 'ServerApplication.main()'`.
    - The backend will start and be accessible at `http://localhost:8080`.

6. **Access the H2 Console**:
    - Open your web browser and navigate to `http://localhost:8080/h2-console`.
    - Use the following JDBC URL to connect:
        ```
        jdbc:h2:mem:ticketingsystemdb
        ```
    - Username: `sa`
    - Password: *(leave blank)*

#### Option 2: Using Command Line (Maven)

1. **Clone the Repository**:
    ```bash
    git clone https://github.com/Kushan-Sankalpa/Real-Time-Event-Ticketing-System.git
    cd Real-Time-Event-Ticketing-System/server
    ```
    > **Note**: Replace the URL with your actual repository URL if different.

2. **Build the Backend Application**:
    ```bash
    mvn clean install
    ```

3. **Run the Application**:
    ```bash
    mvn spring-boot:run
    ```
    - The backend will start and be accessible at `http://localhost:8080`.

4. **Access the H2 Console**:
    - Open your web browser and navigate to `http://localhost:8080/h2-console`.
    - Use the following JDBC URL to connect:
        ```
        jdbc:h2:mem:ticketingsystemdb
        ```
    - Username: `sa`
    - Password: *(leave blank)*

---

### Frontend Setup (React)

1. **Navigate to the Frontend Directory**:
    ```bash
    cd ../client /Realp-time-ticket-Gui
    
    ```
    > **Note**: Ensure you are in the root directory of the cloned repository before navigating.

2. **Install Dependencies**:
    ```bash
    npm install
    ```

3. **Start the Application**:
    ```bash
    npm run dev
    ```
    - The frontend will start and be accessible at `http://localhost:5176`.

4. **Configuration**:
    - Ensure that the frontend is correctly configured to communicate with the backend API at `http://localhost:8080`.
    - 

---

### CLI Simulation Setup

The CLI simulation allows you to test the core functionalities of the ticketing system outside the web interface.

#### Option 1: Using IntelliJ IDEA

1. **Navigate to the CLI Directory**:
    ```bash
    cd ../cli
    ```

2. **Open the CLI Project in IntelliJ IDEA**:
    - Launch **IntelliJ IDEA**.
    - Click on `File` > `Open...` and navigate to the `cli` directory of the cloned repository.
    - Select the project and click `OK` to open it.

3. **Compile the Java Files**:
    - IntelliJ IDEA automatically compiles Java files upon saving if configured.

4. **Run the Simulation**:
    - Locate the `Main_TicketingSystem` class in the project.
    - Right-click on the `Main_TicketingSystem` class and select `Run 'Main_TicketingSystem.main()'`.
    - The CLI will start and prompt you to configure the system or load an existing configuration.
    - Follow the on-screen instructions to set up ticketing rates, capacities, and the number of vendors and customers.

#### Option 2: Using Command Line

1. **Navigate to the CLI Directory**:
    ```bash
    cd ../cli
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
    - If using IntelliJ IDEA, follow the [CLI Simulation Setup Option 1](#option-1-using-intellij-idea).

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

1. ** Run the Springboot Application
    -  - Right-click on the `ServerApplication` class and select `Run 'ServerApplication.main()'`.

2. **Access the Frontend**:
    - Open your web browser and navigate to `http://localhost:5176`.

3. **Fill Out the Configuration Form**:
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

Thank you for using the **Real-Time Event Ticketing System**! We hope it serves your event management needs effectively.
