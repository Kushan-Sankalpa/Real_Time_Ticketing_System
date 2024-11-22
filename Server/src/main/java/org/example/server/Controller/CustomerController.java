// File: src/main/java/org/example/server/Controller/CustomerController.java

package org.example.server.Controller;

import org.example.server.DTO.CustomerDTO;
import org.example.server.Service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    /**
     * Retrieves all customers.
     *
     * @return List of CustomerDTOs.
     */
    @GetMapping("/getAllCustomer")
    public List<CustomerDTO> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    /**
     * Creates a new customer.
     *
     * @param customerDTO The customer data.
     * @return The created CustomerDTO.
     */
    @PostMapping("/createCustomer")
    public CustomerDTO createCustomer(@RequestBody CustomerDTO customerDTO) {
        return customerService.createCustomer(customerDTO);
    }
}
