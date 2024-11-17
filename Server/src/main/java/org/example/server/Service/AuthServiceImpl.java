package org.example.server.Service;

import org.example.server.Dto.LoginRequestDTO;
import org.example.server.Dto.RegisterRequestDTO;
import org.example.server.Entity.Customer;
import org.example.server.Entity.Vendor;
import org.example.server.Repository.CustomerRepository;
import org.example.server.Repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private VendorRepository vendorRepository;

    @Override
    public String registerCustomer(RegisterRequestDTO registerRequest) {
        if (customerRepository.findByEmail(registerRequest.getEmail()) != null) {
            return "Email already in use.";
        }
        Customer customer = new Customer();
        customer.setCustomerName(registerRequest.getName());
        customer.setEmail(registerRequest.getEmail());
        customer.setPassword(registerRequest.getPassword()); // Hash in production
        customerRepository.save(customer);
        return "Customer registered successfully.";
    }

    @Override
    public String registerVendor(RegisterRequestDTO registerRequest) {
        if (vendorRepository.findByEmail(registerRequest.getEmail()) != null) {
            return "Email already in use.";
        }
        Vendor vendor = new Vendor();
        vendor.setVendorName(registerRequest.getName());
        vendor.setEmail(registerRequest.getEmail());
        vendor.setPassword(registerRequest.getPassword()); // Hash in production
        vendorRepository.save(vendor);
        return "Vendor registered successfully.";
    }

    @Override
    public Customer loginCustomer(LoginRequestDTO loginRequest) {
        Customer customer = customerRepository.findByEmail(loginRequest.getEmail());
        if (customer != null && customer.getPassword().equals(loginRequest.getPassword())) {
            return customer;
        }
        return null;
    }

    @Override
    public Vendor loginVendor(LoginRequestDTO loginRequest) {
        Vendor vendor = vendorRepository.findByEmail(loginRequest.getEmail());
        if (vendor != null && vendor.getPassword().equals(loginRequest.getPassword())) {
            return vendor;
        }
        return null;
    }
}
