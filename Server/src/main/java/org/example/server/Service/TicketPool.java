package org.example.server.Service;

import org.example.server.Dto.ConfigurationDTO;
import org.example.server.Entity.Ticket;
import org.example.server.Repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.LinkedList;
import java.util.Queue;

@Service
public class TicketPool {

    private final Queue<Ticket> tickets;
    private int maxCapacity;
    private int totalTicketsToRelease;
    private int totalTicketsAdded;
    private int totalTicketsSold;
    private int initialTickets;
    private int vendorTicketsReleased;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private ConfigurationService configurationService;

    public TicketPool() {
        this.tickets = new LinkedList<>();
        this.totalTicketsAdded = 0;
        this.totalTicketsSold = 0;
        this.vendorTicketsReleased = 0;
    }

    @PostConstruct
    public void initialize() {
        ConfigurationDTO config = configurationService.getLatestConfiguration();
        if (config != null) {
            this.maxCapacity = config.getMaxTicketCapacity();
            this.totalTicketsToRelease = config.getTotalTickets();
<<<<<<< HEAD
            this.initialTickets = config.getInitialTickets();

            for (int i = 1; i <= initialTickets; i++) {
                Ticket ticket = new Ticket();
                ticket.setTicketCode("Ticket-" + i);
                ticket.setVendorName("Initial");
                ticket.setSold(false);
                ticketRepository.save(ticket);
                tickets.add(ticket);
                totalTicketsAdded++;
            }
            System.out.println(initialTickets + " Tickets added to the Ticket pool");
            System.out.println("Current Tickets in the Ticket pool : " + tickets.size());
=======
            System.out.println("TicketPool initialized with max capacity: " + maxCapacity + " and total tickets to release: " + totalTicketsToRelease);
>>>>>>> 01450f842c042e9d4f9b6c56835f9b566a897e8e
        } else {
            System.out.println("No configuration found. TicketPool not initialized.");
        }
    }

    // Synchronized method to add a ticket
    public synchronized boolean addTicket(Ticket ticket) throws InterruptedException {
        // Check if the total tickets released have reached the limit
        int totalReleasedTickets = initialTickets + vendorTicketsReleased;
        if (totalReleasedTickets >= totalTicketsToRelease) {
            return false; // No more tickets can be added
        }

        while (tickets.size() >= maxCapacity) {
            wait(); // Wait if the pool is full
        }

        ticketRepository.save(ticket);
        tickets.add(ticket);
        totalTicketsAdded++;
        vendorTicketsReleased++;
        System.out.println("Vendor added: " + ticket.getTicketCode() + ". Tickets in pool: " + tickets.size());
        notifyAll(); // Notify consumers that a ticket is available
        return true;
    }

    // Synchronized method to remove a ticket
    public synchronized Ticket removeTicket(String customerName) throws InterruptedException {
        while (tickets.isEmpty()) {
            if (totalTicketsSold >= totalTicketsToRelease) {
                return null; // All tickets have been sold
            }
            wait(); // Wait if no tickets are available
        }
        Ticket ticket = tickets.poll();
        ticket.setSold(true);
        ticket.setCustomerName(customerName);
        ticketRepository.save(ticket);
        totalTicketsSold++;
        System.out.println("Customer purchased: " + ticket.getTicketCode() + ". Tickets in pool: " + tickets.size());
        notifyAll(); // Notify producers that space is available
        return ticket;
    }

    // Get current ticket count
    public synchronized int getCurrentTicketCount() {
        return tickets.size();
    }

    public synchronized int getTotalTicketsAdded() {
        return totalTicketsAdded;
    }

    public synchronized int getTotalTicketsSold() {
        return totalTicketsSold;
    }
}
