package org.example.server.Service;

import jakarta.annotation.PostConstruct;
import org.example.server.Dto.VendorDTO;
import org.example.server.Entity.Vendor;
import org.example.server.Repository.VendorRepository;
import org.example.server.Entity.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class VendorServiceImpl implements VendorService {

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private TicketPool ticketPool;

    private ExecutorService vendorExecutor;

    @PostConstruct
    public void init() {
        vendorExecutor = Executors.newCachedThreadPool();
    }

    // Convert Vendor to VendorDTO
    private VendorDTO convertToDTO(Vendor vendor) {
        VendorDTO dto = new VendorDTO();
        dto.setId(vendor.getId());
        dto.setVendorName(vendor.getVendorName());
        dto.setTicketReleaseRate(vendor.getTicketReleaseRate());
        return dto;
    }

    // Get all vendors
    @Override
    public List<VendorDTO> getAllVendors() {
        List<Vendor> vendors = vendorRepository.findAll();
        return vendors.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Create a new vendor
    @Override
    public VendorDTO createVendor(VendorDTO vendorDTO) {
        Vendor vendor = new Vendor();
        vendor.setVendorName(vendorDTO.getVendorName());
        vendor.setTicketReleaseRate(vendorDTO.getTicketReleaseRate());
        Vendor savedVendor = vendorRepository.save(vendor);
        return convertToDTO(savedVendor);
    }

    // Start vendor threads
    @Override
    public void startVendors(int numberOfVendors, int ticketReleaseRate) {
        for (int i = 1; i <= numberOfVendors; i++) {
            VendorRunnable vendorRunnable = new VendorRunnable("Vendor-" + i, ticketReleaseRate);
            vendorExecutor.submit(vendorRunnable);
        }
    }

    // Stop vendor threads
    @Override
    public void stopVendors() {
        vendorExecutor.shutdownNow();
    }

    private class VendorRunnable implements Runnable {
        private final String vendorName;
        private final int ticketReleaseRate;

        public VendorRunnable(String vendorName, int ticketReleaseRate) {
            this.vendorName = vendorName;
            this.ticketReleaseRate = ticketReleaseRate;
        }

        @Override
        public void run() {
            try {
                int ticketNumber = 1;
                while (true) {
                    String ticketCode = vendorName + "-Ticket-" + ticketNumber++;
                    Ticket ticket = new Ticket();
                    ticket.setTicketCode(ticketCode);
                    ticket.setVendorName(vendorName);
                    ticket.setSold(false);
                    boolean added = ticketPool.addTicket(ticket);
                    if (!added) {
                        System.out.println(vendorName + " has released all tickets.");
                        break;
                    }
                    Thread.sleep(ticketReleaseRate);
                }
            } catch (InterruptedException e) {
                System.out.println(vendorName + " interrupted and stopping.");
                Thread.currentThread().interrupt();
            }
        }
    }
}
