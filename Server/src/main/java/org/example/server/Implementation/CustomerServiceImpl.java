package org.example.server.Implementation;

import org.example.server.Entity.Customer;
import org.example.server.Entity.Ticket;
import org.example.server.Repository.CustomerRepository;
import org.example.server.Service.CustomerService;
import org.example.server.Service.TicketPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Implementation of the CustomerService interface for managing customer interactions with the ticket pool.
 */
@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private TicketPool ticketPool;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private ExecutorService customerExecutor;
    private List<CustomerRunnable> customerRunnables = new ArrayList<>();


    /**
     * Starts customer threads to interact with the ticket pool.
     * Customers retrieve tickets at a specified rate.
     *
     * @param numberOfCustomers The number of customer threads to start.
     * @param customerRetrievalRate The interval (in milliseconds) at which each customer retrieves tickets.
     */
    @Override
    public void startCustomers(int numberOfCustomers, int customerRetrievalRate) {

        // Thread pool for customer tasks.
        customerExecutor = Executors.newCachedThreadPool();
        // Clear any previous tasks.
        customerRunnables.clear();


        // Loop to create customer threads
        for (int i = 1; i <= numberOfCustomers; i++) {
            String customerName = "Customer-" + i;

            // Check if customer already exists in the repository
            Optional<Customer> existingCustomer = customerRepository.findByCustomerName(customerName);
            if (!existingCustomer.isPresent()) {

                // If not, create a new customer and save it to the repository
                Customer customer = new Customer();
                customer.setCustomerName(customerName);
                customer.setCustomerRetrievalRate(customerRetrievalRate);
                customerRepository.save(customer);
            }


            // Create and submit a new customer task to the thread pool
            CustomerRunnable customerRunnable = new CustomerRunnable(customerName, customerRetrievalRate);
            customerRunnables.add(customerRunnable);
            customerExecutor.submit(customerRunnable);
        }
    }

    /**
     * Stops all customer threads and clears the list of runnables.
     */
    @Override
    public void stopCustomers() {
        if (customerExecutor != null && !customerExecutor.isShutdown()) {
            for (CustomerRunnable customerRunnable : customerRunnables) {
                customerRunnable.stop(); // Stop each customer task.
            }
            customerExecutor.shutdownNow();
            customerRunnables.clear();
        }
    }


    /**
     * Inner class representing a customer's runnable task.
     */
    private class CustomerRunnable implements Runnable {
        private final String customerName;
        private final int customerRetrievalRate;
        private volatile boolean running = true;

        public CustomerRunnable(String customerName, int customerRetrievalRate) {
            this.customerName = customerName;
            this.customerRetrievalRate = customerRetrievalRate;
        }

        public void stop() {
            running = false;
        }

        /**
         * Main logic for customer threads.
         * Customers attempt to purchase tickets from the ticket pool.
         */
        @Override
        public void run() {
            try {
                while (running) {
                    // Attempt to remove a ticket from the ticket pool
                    Ticket ticket = ticketPool.removeTicket(customerName);
                    if (ticket == null) {
                        // If no tickets are available, log and exit
                        String logMessage = customerName + " found no tickets available. Exiting.";
                        messagingTemplate.convertAndSend("/topic/logs", logMessage);
                        System.out.println(logMessage);
                        break;
                    }
                    Thread.sleep(customerRetrievalRate); // Wait before next ticket retrieval
                }
            } catch (InterruptedException e) {
                String logMessage = customerName + " interrupted and stopping.";
                messagingTemplate.convertAndSend("/topic/logs", logMessage);
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                String logMessage = customerName + " encountered an error: " + e.getMessage();
                messagingTemplate.convertAndSend("/topic/logs", logMessage);
            } finally {
                String logMessage = customerName + " stopped.";
                messagingTemplate.convertAndSend("/topic/logs", logMessage);
                System.out.println(logMessage);
            }
        }
    }
}
