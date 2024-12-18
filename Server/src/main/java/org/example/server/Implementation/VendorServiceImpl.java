package org.example.server.Implementation;

import org.example.server.Entity.Ticket;
import org.example.server.Entity.Vendor;
import org.example.server.Repository.TicketRepository;
import org.example.server.Repository.VendorRepository;
import org.example.server.Service.TicketPool;
import org.example.server.Service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Implementation of the VendorService interface for managing vendor interactions with the ticket pool.
 */

@Service
public class VendorServiceImpl implements VendorService {

    @Autowired
    private TicketPool ticketPool;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private ExecutorService vendorExecutor;
    private List<VendorRunnable> vendorRunnables = new ArrayList<>();


    /**
     * Starts vendor threads to release tickets into the ticket pool at a specified rate.
     *
     * @param numberOfVendors The number of vendor threads to start.
     * @param ticketReleaseRate The interval (in milliseconds) at which each vendor releases tickets.
     */
    @Override
    public void startVendors(int numberOfVendors, int ticketReleaseRate) {

        vendorExecutor = Executors.newCachedThreadPool(); // Create thread pool
        vendorRunnables.clear();  // Clear any existing vendor tasks


        // Create and start threads for each vendor
        for (int i = 1; i <= numberOfVendors; i++) {
            String vendorName = "Vendor-" + i;


            // Check if vendor already exists in the repository
            Optional<Vendor> existingVendor = vendorRepository.findByVendorName(vendorName);
            if (!existingVendor.isPresent()) {

                // Create and save a new vendor
                Vendor vendor = new Vendor();
                vendor.setVendorName(vendorName);
                vendor.setTicketReleaseRate(ticketReleaseRate);
                vendorRepository.save(vendor);
            }

            // Start a new vendor thread
            System.out.println("Starting " + vendorName);
            VendorRunnable vendorRunnable = new VendorRunnable(vendorName, ticketReleaseRate);
            vendorRunnables.add(vendorRunnable);
            vendorExecutor.submit(vendorRunnable);
        }
    }

    /**
     * Stops all vendor threads and clears the list of runnables.
     */
    @Override
    public void stopVendors() {
        if (vendorExecutor != null && !vendorExecutor.isShutdown()) {
            // Stop each vendor task.
            for (VendorRunnable vendorRunnable : vendorRunnables) {
                vendorRunnable.stop();
            }
            vendorExecutor.shutdownNow();
            vendorRunnables.clear();
        }
    }




    /**
     * Inner class representing a vendor's runnable task
     */
    private class VendorRunnable implements Runnable {
        private final String vendorName;
        private final int ticketReleaseRate;
        private volatile boolean running = true;
        private int ticketNumber;

        public VendorRunnable(String vendorName, int ticketReleaseRate) {
            this.vendorName = vendorName;
            this.ticketReleaseRate = ticketReleaseRate;
            // Initialize ticket count based on the repository
            this.ticketNumber = (int) ticketRepository.countByVendorName(vendorName) + 1;
        }

        public void stop() {
            running = false;
        }

        /**
         * Main logic for vendor threads.
         * Vendors add tickets to the ticket pool until the total ticket limit is reached.
         */
        @Override
        public void run() {
            try {
                while (running) {
                    // Check if the total ticket limit is reached
                    int totalTicketsAdded = ticketPool.getTotalTicketsAdded();
                    int totalTicketsToRelease = ticketPool.getTotalTicketsToRelease();

                    if (totalTicketsAdded >= totalTicketsToRelease) {
                        // Log when all tickets are released
                        String logMessage = vendorName + " has released all tickets. Total Tickets Added: ";
                        messagingTemplate.convertAndSend("/topic/logs", logMessage);

                        logMessage = vendorName + " stopped.";
                        messagingTemplate.convertAndSend("/topic/logs", logMessage);

                        System.out.println(logMessage);
                        break;
                    }

                    // Create and add a new ticket to the pool
                    String ticketCode = vendorName + "-Ticket-" + ticketNumber++;
                    Ticket ticket = new Ticket();
                    ticket.setTicketCode(ticketCode);
                    ticket.setVendorName(vendorName);
                    ticket.setSold(false);

                    boolean added = ticketPool.addTicket(ticket);
                    if (!added) {
                        // If the pool is full, wait before retrying
                        System.out.println(vendorName + " waiting to add tickets.");
                        Thread.sleep(500);
                        continue;
                    }

                    // Wait before releasing the next ticket
                    Thread.sleep(ticketReleaseRate);
                }
            } catch (InterruptedException e) {
                String logMessage = vendorName + " interrupted and stopping.";
                messagingTemplate.convertAndSend("/topic/logs", logMessage);
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                String logMessage = vendorName + " encountered an error: " + e.getMessage();
                messagingTemplate.convertAndSend("/topic/logs", logMessage);
            } finally {
                String logMessage = vendorName + " stopped.";
                messagingTemplate.convertAndSend("/topic/logs", logMessage);
                System.out.println(logMessage);
            }
        }
    }
}
