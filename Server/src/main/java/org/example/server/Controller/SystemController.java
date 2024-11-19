// File: src/main/java/org/example/server/Controller/SystemController.java

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
        var config = configurationService.getLatestConfiguration();
        if (config == null) {
            return "No configuration found. Please configure the system first.";
        }

        vendorService.startVendors(config.getNumberOfVendors(), config.getTicketReleaseRate());
        customerService.startCustomers(config.getNumberOfCustomers(), config.getCustomerRetrievalRate());

        return "System started with " + config.getNumberOfVendors() + " vendors and " + config.getNumberOfCustomers() + " customers.";
    }

    @GetMapping("/stop")
    public String stopSystem() {
        vendorService.stopVendors();
        customerService.stopCustomers();
        return "System stopped.";
    }
}
