import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


/**
 * Main class for the Real-Time Event Ticketing System.
 * Handles user interactions and system control.
 */
public class Main_TicketingSystem {
    private Configuration config;
    private TicketPool ticketPool;
    private List<Thread> userThreads;
    private List<User> users;
    private Scanner scanner;

    public Main_TicketingSystem () {
        userThreads = new ArrayList<>();
        users = new ArrayList<>();
        scanner = new Scanner(System.in);
    }


    /**
     * Starts the ticketing system.
     * Handles configuration and initializes user threads.
     */
    public void start() {
        config = new Configuration();

        System.out.println("*****************************************");
        System.out.println("*   Real- Time Event Ticketing System   *");
        System.out.println("*****************************************\n");
        System.out.print("Do you want to load an existing configuration? (yes/no): ");
        String loadConfigChoice = scanner.nextLine().trim().toLowerCase();

        if (loadConfigChoice.equals("yes")) {
            System.out.print("Enter the configuration filename (e.g., config.json): ");
            String filename = scanner.nextLine().trim();
            Configuration loadedConfig = Configuration.loadConfiguration(filename);
            if (loadedConfig != null) {
                config = loadedConfig;
            } else {
                System.out.println("Failed to load configuration. Proceeding with new configuration.!");
                config.configure(scanner);
            }
        } else {
            config.configure(scanner);
        }


        ticketPool = new TicketPool(
                config.getInitialTickets(),
                config.getMaxTicketCapacity(),
                config.getTotalTickets()
        );


        System.out.print("Do you want to save the current configuration? (yes/no): ");
        String saveConfigChoice = scanner.nextLine().trim().toLowerCase();
        if (saveConfigChoice.equals("yes")) {
            System.out.print("Enter the filename to save configuration (e.g., config.json): ");
            String saveFilename = scanner.nextLine().trim();
            config.saveConfiguration(saveFilename);
        }

        System.out.println("Enter commands: start, stop, status, exit");


        Thread inputThread = new Thread(new InputHandler(this, scanner), "InputHandler");
        inputThread.start();
    }

    /**
     * Starts vendor and customer threads.
     * Synchronized to prevent concurrent modifications.
     */
    public synchronized void startUsers() {
        if (!userThreads.isEmpty()) {
            System.out.println("System is already running.");
            return;
        }


        int numberOfVendors = config.getNumberOfVendors();
        int numberOfCustomers = config.getNumberOfCustomers();

        // Start Vendors
        for (int i = 0; i < numberOfVendors; i++) {
            Vendor vendor = new Vendor(ticketPool, config.getTicketReleaseRate());
            users.add(vendor);
            Thread vendorThread = new Thread(vendor, "Vendor-" + (i + 1));
            userThreads.add(vendorThread);
            vendorThread.start();
        }

        // Start Customers
        for (int i = 0; i < numberOfCustomers; i++) {
            Customer customer = new Customer(ticketPool, config.getCustomerRetrievalRate());
            users.add(customer);
            Thread customerThread = new Thread(customer, "Customer-" + (i + 1));
            userThreads.add(customerThread);
            customerThread.start();
        }

    }

    /**
     * Stops all vendor and customer threads.
     * Synchronized to prevent concurrent modifications.
     */
    public synchronized void stopUsers() {
        if (userThreads.isEmpty()) {
            System.out.println("System is not running.");
            return;
        }


        for (User user : users) {
            user.stop();
        }


        for (Thread thread : userThreads) {
            thread.interrupt();
        }


        for (Thread thread : userThreads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.out.println("Interrupted while waiting for threads to terminate.");
                Thread.currentThread().interrupt();
            }
        }

        userThreads.clear();
        users.clear();

        System.out.println("Vendors and Customers have stopped.");
    }

    /**
     * Displays the current status of tickets in the system.
     * Synchronized to prevent inconsistent reads.
     */
    public synchronized void displayStatus() {
        int currentTickets = ticketPool.getCurrentTicketCount();
        int totalTicketsAdded = ticketPool.getTotalTicketsAdded();
        int totalTicketsSold = ticketPool.getTotalTicketsSold();
        System.out.println("Current Ticket Count in Pool: " + currentTickets);
        System.out.println("Total Tickets Added: " + totalTicketsAdded);
        System.out.println("Total Tickets Sold: " + totalTicketsSold);
    }

    /**
     * Inner class for handling user inputs.
     * Processes commands like start, stop, status, and exit.
     */
    private class InputHandler implements Runnable {
        private final Main_TicketingSystem ticketingSystem;
        private final Scanner scanner;

        /**
         * Constructs a new InputHandler.
         *
         * @param ticketingSystem the main ticketing system instance.
         * @param scanner         the shared Scanner object.
         */
        public InputHandler(Main_TicketingSystem ticketingSystem, Scanner scanner) {
            this.ticketingSystem = ticketingSystem;
            this.scanner = scanner;
        }

        @Override
        public void run() {
            String command;

            while (true) {
                System.out.print(">> ");
                command = scanner.nextLine().trim().toLowerCase();
                switch (command) {
                    case "start":
                        ticketingSystem.startUsers();
                        break;
                    case "stop":
                        ticketingSystem.stopUsers();
                        break;
                    case "status":
                        ticketingSystem.displayStatus();
                        break;
                    case "exit":
                        ticketingSystem.stopUsers();
                        System.out.println("Exiting the system.");
                        scanner.close();
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Unknown command. Available commands: start, stop, status, exit");
                }
            }
        }
    }

    /**
     * The main method to run the ticketing system.
     *
     * @param args command-line arguments (not used).
     */
    public static void main(String[] args) {
        Main_TicketingSystem system = new Main_TicketingSystem();
        system.start();
    }
}
