package org.example.server.Controller;

import org.example.server.Service.ConfigurationService;
import org.example.server.Service.CustomerService;
import org.example.server.Service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/system")
public class SystemController {

    @Autowired
    private VendorService vendorService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ConfigurationService configurationService;

    @GetMapping("/start")
    public String startSystem() {
        // Fetch latest configuration
        var config = configurationService.getLatestConfiguration();
        if (config == null) {
            return "No configuration found. Please configure the system first.";
        }

        return "Starting";
    }

    @GetMapping("/stop")
    public String stopSystem() {
        vendorService.stopVendors();
        customerService.stopCustomers();
        return "System stopped.";
    }
}