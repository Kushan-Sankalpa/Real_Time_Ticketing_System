package org.example.server.Service;

import jakarta.annotation.PostConstruct;
import org.example.server.DTO.CustomerDTO;
import org.example.server.Entity.Customer;
import org.example.server.Entity.Ticket;
import org.example.server.Repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TicketPool ticketPool;

    private ExecutorService customerExecutor;

    @PostConstruct
    public void init() {
        customerExecutor = Executors.newCachedThreadPool();
    }

    // Convert Customer to CustomerDTO
    private CustomerDTO convertToDTO(Customer customer) {
        CustomerDTO dto = new CustomerDTO();
        dto.setId(customer.getId());
        dto.setCustomerName(customer.getCustomerName());
        dto.setCustomerRetrievalRate(customer.getCustomerRetrievalRate());
        // Removed isVIP field
        return dto;
    }

    // Get all customers
    @Override
    public List<CustomerDTO> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Create a new customer
    @Override
    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        customer.setCustomerName(customerDTO.getCustomerName());
        customer.setCustomerRetrievalRate(customerDTO.getCustomerRetrievalRate());
        // Removed isVIP field
        Customer savedCustomer = customerRepository.save(customer);
        return convertToDTO(savedCustomer);
    }

    // Start customer threads
    @Override
    public void startCustomers(int numberOfCustomers, int customerRetrievalRate) {
        for (int i = 1; i <= numberOfCustomers; i++) {
            CustomerRunnable customerRunnable = new CustomerRunnable("Customer-" + i, customerRetrievalRate);
            customerExecutor.submit(customerRunnable);
        }
    }

    // Stop customer threads
    @Override
    public void stopCustomers() {
        customerExecutor.shutdownNow();
    }

    private class CustomerRunnable implements Runnable {
        private final String customerName;
        private final int customerRetrievalRate;

        public CustomerRunnable(String customerName, int customerRetrievalRate) {
            this.customerName = customerName;
            this.customerRetrievalRate = customerRetrievalRate;
        }

        @Override
        public void run() {
            try {
                while (true) {
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
            }
        }
    }
}
