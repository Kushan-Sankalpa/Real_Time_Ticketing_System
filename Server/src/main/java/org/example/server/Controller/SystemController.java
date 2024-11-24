// File: src/main/java/org/example/server/Controller/SystemController.java

package org.example.server.Controller;

import org.example.server.DTO.ConfigurationDTO;
import org.example.server.Service.ConfigurationService;
import org.example.server.Service.CustomerService;
import org.example.server.Service.VendorService;
import org.example.server.Service.TicketPool;
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

    @Autowired
    private TicketPool ticketPool;

    /**
     * Starts the ticketing system by initializing the TicketPool and starting vendor and customer threads.
     *
     * @return A message indicating the system has started.
     */
    @GetMapping("/start")
    public String startSystem() {
        ConfigurationDTO config = configurationService.getLatestConfiguration();
        if (config == null) {
            return "No configuration found. Please configure the system first.";
        }
        ticketPool.initialize(config);
        vendorService.startVendors(config.getNumberOfVendors(), config.getTicketReleaseRate());
        customerService.startCustomers(config.getNumberOfCustomers(), config.getCustomerRetrievalRate());

        return "System started with " + config.getNumberOfVendors() + " vendors and " + config.getNumberOfCustomers() + " customers.";
    }

    /**
     * Stops the ticketing system by stopping vendor and customer threads.
     *
     * @return A message indicating the system has stopped.
     */
    @GetMapping("/stop")
    public String stopSystem() {
        vendorService.stopVendors();
        customerService.stopCustomers();
        return "System stopped.";
    }

    /**
     * Resets the ticketing system by stopping all threads, resetting the TicketPool,
     * deleting all tickets from the database, and removing the latest configuration.
     *
     * @return A message indicating the reset status.
     */
    @DeleteMapping("/reset")
    public String resetSystem() {

        vendorService.stopVendors();
        customerService.stopCustomers();


        ticketPool.reset();
        configurationService.deleteAllTickets();
        configurationService.deleteAllVendors();
        configurationService.deleteAllCustomers();
        boolean deleted = configurationService.deleteLatestConfiguration();
        if (deleted) {
            return "System has been reset successfully.";
        } else {
            return "No configuration found to reset.";
        }
    }

}
