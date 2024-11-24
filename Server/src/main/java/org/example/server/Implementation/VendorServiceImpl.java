package org.example.server.Implementation;

import org.example.server.Entity.Ticket;
import org.example.server.Entity.Vendor;
import org.example.server.Repository.TicketRepository;
import org.example.server.Repository.VendorRepository;
import org.example.server.Service.TicketPool;
import org.example.server.Service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class VendorServiceImpl implements VendorService {

    @Autowired
    private TicketPool ticketPool;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private VendorRepository vendorRepository;




    private ExecutorService vendorExecutor;
    private List<VendorRunnable> vendorRunnables = new ArrayList<>();

    @Override
    public void startVendors(int numberOfVendors, int ticketReleaseRate) {

        vendorExecutor = Executors.newCachedThreadPool();
        vendorRunnables.clear();

        for (int i = 1; i <= numberOfVendors; i++) {
            String vendorName = "Vendor-" + i;

            // Check if vendor already exists
            Optional<Vendor> existingVendor = vendorRepository.findByVendorName(vendorName);
            if (!existingVendor.isPresent()) {
                // Create and save vendor entity
                Vendor vendor = new Vendor();
                vendor.setVendorName(vendorName);
                vendor.setTicketReleaseRate(ticketReleaseRate);
                vendorRepository.save(vendor);
            }

            // Start the vendor thread
            System.out.println("Starting " + vendorName);
            VendorRunnable vendorRunnable = new VendorRunnable(vendorName, ticketReleaseRate);
            vendorRunnables.add(vendorRunnable);
            vendorExecutor.submit(vendorRunnable);
        }
    }

    @Override
    public void stopVendors() {
        if (vendorExecutor != null && !vendorExecutor.isShutdown()) {
            // Stop all VendorRunnable threads
            for (VendorRunnable vendorRunnable : vendorRunnables) {
                vendorRunnable.stop();
            }
            vendorExecutor.shutdownNow();
            vendorRunnables.clear();
        }
    }

    /**
     * Runnable class for vendor threads.
     */
    private class VendorRunnable implements Runnable {
        private final String vendorName;
        private final int ticketReleaseRate;
        private volatile boolean running = true;
        private int ticketNumber;

        public VendorRunnable(String vendorName, int ticketReleaseRate) {
            this.vendorName = vendorName;
            this.ticketReleaseRate = ticketReleaseRate;
            // Initialize ticketNumber based on tickets already released by this vendor
            this.ticketNumber = (int) ticketRepository.countByVendorName(vendorName) + 1;
        }

        public void stop() {
            running = false;
        }

        @Override
        public void run() {
            try {
                while (running) {
                    int totalTicketsAdded = ticketPool.getTotalTicketsAdded();
                    int totalTicketsToRelease = ticketPool.getTotalTicketsToRelease();

                    if (totalTicketsAdded >= totalTicketsToRelease) {
                        System.out.println(vendorName + " has released all tickets. Total Tickets Added: " + totalTicketsAdded + ", Total Tickets To Release: " + totalTicketsToRelease);
                        break;
                    }

                    String ticketCode = vendorName + "-Ticket-" + ticketNumber++;
                    Ticket ticket = new Ticket();
                    ticket.setTicketCode(ticketCode);
                    ticket.setVendorName(vendorName);
                    ticket.setSold(false);

                    boolean added = ticketPool.addTicket(ticket);
                    if (!added) {
                        System.out.println(vendorName + " waiting to add tickets.");
                        Thread.sleep(500); // Wait before retrying if the pool is full
                        continue;
                    }

                    System.out.println(vendorName + " released ticket: " + ticketCode);
                    Thread.sleep(ticketReleaseRate); // Release tickets based on the configured rate
                }
            } catch (InterruptedException e) {
                System.out.println(vendorName + " interrupted and stopping.");
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                System.out.println(vendorName + " encountered an error: " + e.getMessage());
            } finally {
                System.out.println(vendorName + " stopped.");
            }
        }
    }
}
