package org.example.server.Implementation;



import org.example.server.DTO.VendorDTO;
import org.example.server.Entity.Ticket;
import org.example.server.Entity.Vendor;
import org.example.server.Repository.VendorRepository;
import org.example.server.Service.TicketPool;
import org.example.server.Service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class VendorServiceImpl implements VendorService {

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private TicketPool ticketPool;

    private ExecutorService vendorExecutor;
    private List<VendorRunnable> vendorRunnables = new ArrayList<>();

    @Override
    public void startVendors(int numberOfVendors, int ticketReleaseRate) {
        // Reinitialize vendorExecutor
        vendorExecutor = Executors.newCachedThreadPool();
        vendorRunnables.clear();

        for (int i = 1; i <= numberOfVendors; i++) {
            VendorRunnable vendorRunnable = new VendorRunnable("Vendor-" + i, ticketReleaseRate);
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

    private VendorDTO convertToDTO(Vendor vendor) {
        VendorDTO dto = new VendorDTO();
        dto.setId(vendor.getId());
        dto.setVendorName(vendor.getVendorName());
        dto.setTicketReleaseRate(vendor.getTicketReleaseRate());
        return dto;
    }

    @Override
    public List<VendorDTO> getAllVendors() {
        List<Vendor> vendors = vendorRepository.findAll();
        return vendors.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public VendorDTO createVendor(VendorDTO vendorDTO) {
        Vendor vendor = new Vendor();
        vendor.setVendorName(vendorDTO.getVendorName());
        vendor.setTicketReleaseRate(vendorDTO.getTicketReleaseRate());
        Vendor savedVendor = vendorRepository.save(vendor);
        return convertToDTO(savedVendor);
    }

    /**
     * Runnable class for vendor threads.
     */
    private class VendorRunnable implements Runnable {
        private final String vendorName;
        private final int ticketReleaseRate;
        private volatile boolean running = true;

        public VendorRunnable(String vendorName, int ticketReleaseRate) {
            this.vendorName = vendorName;
            this.ticketReleaseRate = ticketReleaseRate;
        }

        public void stop() {
            running = false;
        }

        @Override
        public void run() {
            try {
                int ticketNumber = 1;
                while (running) {
                    synchronized (ticketPool) {
                        if (ticketPool.getTotalTicketsAdded() >= ticketPool.getTotalTicketsToRelease()) {
                            System.out.println(vendorName + " has released all tickets.");
                            break;
                        }
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
