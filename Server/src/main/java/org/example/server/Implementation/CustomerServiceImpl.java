package org.example.server.Implementation;



import org.example.server.DTO.CustomerDTO;
import org.example.server.Entity.Customer;
import org.example.server.Entity.Ticket;
import org.example.server.Repository.CustomerRepository;
import org.example.server.Service.CustomerService;
import org.example.server.Service.TicketPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TicketPool ticketPool;

    private ExecutorService customerExecutor;
    private List<CustomerRunnable> customerRunnables = new ArrayList<>();

    private CustomerDTO convertToDTO(Customer customer) {
        CustomerDTO dto = new CustomerDTO();
        dto.setId(customer.getId());
        dto.setCustomerName(customer.getCustomerName());
        dto.setCustomerRetrievalRate(customer.getCustomerRetrievalRate());
        return dto;
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        customer.setCustomerName(customerDTO.getCustomerName());
        customer.setCustomerRetrievalRate(customerDTO.getCustomerRetrievalRate());
        Customer savedCustomer = customerRepository.save(customer);
        return convertToDTO(savedCustomer);
    }

    @Override
    public void startCustomers(int numberOfCustomers, int customerRetrievalRate) {
        // Reinitialize customerExecutor
        customerExecutor = Executors.newCachedThreadPool();
        customerRunnables.clear();

        for (int i = 1; i <= numberOfCustomers; i++) {
            CustomerRunnable customerRunnable = new CustomerRunnable("Customer-" + i, customerRetrievalRate);
            customerRunnables.add(customerRunnable);
            customerExecutor.submit(customerRunnable);
        }
    }

    @Override
    public void stopCustomers() {
        if (customerExecutor != null && !customerExecutor.isShutdown()) {
            // Stop all CustomerRunnable threads
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
                    Thread.sleep(customerRetrievalRate); // Simulate delay between ticket purchases
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
