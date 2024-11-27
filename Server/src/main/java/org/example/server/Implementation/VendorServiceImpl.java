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

@Service
public class VendorServiceImpl implements VendorService {

    @Autowired
    private TicketPool ticketPool;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate; // Added

    private ExecutorService vendorExecutor;
    private List<VendorRunnable> vendorRunnables = new ArrayList<>();

    @Override
    public void startVendors(int numberOfVendors, int ticketReleaseRate) {

        vendorExecutor = Executors.newCachedThreadPool();
        vendorRunnables.clear();

        for (int i = 1; i <= numberOfVendors; i++) {
            String vendorName = "Vendor-" + i;

            Optional<Vendor> existingVendor = vendorRepository.findByVendorName(vendorName);
            if (!existingVendor.isPresent()) {
                Vendor vendor = new Vendor();
                vendor.setVendorName(vendorName);
                vendor.setTicketReleaseRate(ticketReleaseRate);
                vendorRepository.save(vendor);
            }

            System.out.println("Starting " + vendorName);
            VendorRunnable vendorRunnable = new VendorRunnable(vendorName, ticketReleaseRate);
            vendorRunnables.add(vendorRunnable);
            vendorExecutor.submit(vendorRunnable);
        }
    }

    @Override
    public void stopVendors() {
        if (vendorExecutor != null && !vendorExecutor.isShutdown()) {
            for (VendorRunnable vendorRunnable : vendorRunnables) {
                vendorRunnable.stop();
            }
            vendorExecutor.shutdownNow();
            vendorRunnables.clear();
        }
    }

    private class VendorRunnable implements Runnable {
        private final String vendorName;
        private final int ticketReleaseRate;
        private volatile boolean running = true;
        private int ticketNumber;

        public VendorRunnable(String vendorName, int ticketReleaseRate) {
            this.vendorName = vendorName;
            this.ticketReleaseRate = ticketReleaseRate;
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
                        String logMessage = vendorName + " has released all tickets. Total Tickets Added: ";
                        messagingTemplate.convertAndSend("/topic/logs", logMessage);

                        logMessage = vendorName + " stopped.";
                        messagingTemplate.convertAndSend("/topic/logs", logMessage);

                        System.out.println(logMessage);
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
                        Thread.sleep(500);
                        continue;
                    }

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
