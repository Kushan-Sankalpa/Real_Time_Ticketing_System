package org.example.server.Implementation;


import org.example.server.Entity.Customer;
import org.example.server.Entity.Ticket;

import org.example.server.Repository.CustomerRepository;
import org.example.server.Service.CustomerService;
import org.example.server.Service.TicketPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Service
public  class CustomerServiceImpl implements CustomerService {


    @Autowired
    private TicketPool ticketPool;

    @Autowired
    private CustomerRepository customerRepository;


    private ExecutorService customerExecutor;
    private List<CustomerRunnable> customerRunnables = new ArrayList<>();



    @Override
    public void startCustomers(int numberOfCustomers, int customerRetrievalRate) {
        // Reinitialize customerExecutor
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

    /**
     * Runnable class for customer threads.
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

        @Override
        public void run() {
            try {
                while (running) {
                    Ticket ticket = ticketPool.removeTicket(customerName);
                    if (ticket == null) {
                        System.out.println(customerName + " found no tickets available. Exiting.");
                        break;
                    }
                    System.out.println(customerName + " purchased " + ticket.getTicketCode());
                    Thread.sleep(customerRetrievalRate);
                }
            } catch (InterruptedException e) {
                System.out.println(customerName + " interrupted and stopping.");
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                System.out.println(customerName + " encountered an error: " + e.getMessage());
            } finally {
                System.out.println(customerName + " stopped.");
            }
        }
    }
}
