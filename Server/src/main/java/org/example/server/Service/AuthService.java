package org.example.server.Service;

import org.example.server.Dto.LoginRequestDTO;
import org.example.server.Dto.RegisterRequestDTO;
import org.example.server.Entity.Customer;
import org.example.server.Entity.Vendor;

public interface AuthService {
    String registerCustomer(RegisterRequestDTO registerRequest);
    String registerVendor(RegisterRequestDTO registerRequest);
    Customer loginCustomer(LoginRequestDTO loginRequest);
    Vendor loginVendor(LoginRequestDTO loginRequest);
}
