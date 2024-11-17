package org.example.server.Controller;

import org.example.server.Dto.LoginRequestDTO;
import org.example.server.Dto.RegisterRequestDTO;
import org.example.server.Entity.Customer;
import org.example.server.Entity.Vendor;
import org.example.server.Service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/customer/register")
    public String registerCustomer(@RequestBody RegisterRequestDTO registerRequest) {
        return authService.registerCustomer(registerRequest);
    }

    @PostMapping("/vendor/register")
    public String registerVendor(@RequestBody RegisterRequestDTO registerRequest) {
        return authService.registerVendor(registerRequest);
    }

    @PostMapping("/customer/login")
    public Customer loginCustomer(@RequestBody LoginRequestDTO loginRequest) {
        return authService.loginCustomer(loginRequest);
    }

    @PostMapping("/vendor/login")
    public Vendor loginVendor(@RequestBody LoginRequestDTO loginRequest) {
        return authService.loginVendor(loginRequest);
    }
}
