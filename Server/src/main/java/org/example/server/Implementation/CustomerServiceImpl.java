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

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private TicketPool ticketPool;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate; // Added

    private ExecutorService customerExecutor;
    private List<CustomerRunnable> customerRunnables = new ArrayList<>();

    @Override
    public void startCustomers(int numberOfCustomers, int customerRetrievalRate) {
        customerExecutor = Executors.newCachedThreadPool();
        customerRunnables.clear();

        for (int i = 1; i <= numberOfCustomers; i++) {
            String customerName = "Customer-" + i;

            Optional<Customer> existingCustomer = customerRepository.findByCustomerName(customerName);
            if (!existingCustomer.isPresent()) {
                Customer customer = new Customer();
                customer.setCustomerName(customerName);
                customer.setCustomerRetrievalRate(customerRetrievalRate);
                customerRepository.save(customer);
            }

            CustomerRunnable customerRunnable = new CustomerRunnable(customerName, customerRetrievalRate);
            customerRunnables.add(customerRunnable);
            customerExecutor.submit(customerRunnable);
        }
    }

    @Override
    public void stopCustomers() {
        if (customerExecutor != null && !customerExecutor.isShutdown()) {
            for (CustomerRunnable customerRunnable : customerRunnables) {
                customerRunnable.stop();
            }
            customerExecutor.shutdownNow();
            customerRunnables.clear();
        }
    }

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

        @Override
        public void run() {
            try {
                while (running) {
                    Ticket ticket = ticketPool.removeTicket(customerName);
                    if (ticket == null) {
                        String logMessage = customerName + " found no tickets available. Exiting.";
                        messagingTemplate.convertAndSend("/topic/logs", logMessage);
                        System.out.println(logMessage);
                        break;
                    }
                    Thread.sleep(customerRetrievalRate);
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
