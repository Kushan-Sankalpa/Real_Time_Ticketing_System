package org.example.server.Service;

import org.example.server.Dto.CustomerDTO;
import java.util.List;

public interface CustomerService {
    List<CustomerDTO> getAllCustomers();
    CustomerDTO createCustomer(CustomerDTO customerDTO);
    void startCustomers(int numberOfCustomers, int customerRetrievalRate);
    void stopCustomers();
}