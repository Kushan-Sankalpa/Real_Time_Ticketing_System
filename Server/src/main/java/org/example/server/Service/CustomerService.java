package org.example.server.Service;

import org.example.server.DTO.CustomerDTO;
import org.example.server.Entity.Customer;
import org.example.server.Repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    // Convert Customer to CustomerDTO
    private CustomerDTO convertToDTO(Customer customer) {
        CustomerDTO dto = new CustomerDTO();
        dto.setId(customer.getId());
        dto.setCustomerName(customer.getCustomerName());
        dto.setCustomerRetrievalRate(customer.getCustomerRetrievalRate());
        dto.setIsVIP(customer.isVIP());
        return dto;
    }

    // Get all customers
    public List<CustomerDTO> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Create a new customer
    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        customer.setCustomerName(customerDTO.getCustomerName());
        customer.setCustomerRetrievalRate(customerDTO.getCustomerRetrievalRate());
        customer.setVIP(customerDTO.isVIP());
        Customer savedCustomer = customerRepository.save(customer);
        return convertToDTO(savedCustomer);
    }

    // Additional methods as needed
}

