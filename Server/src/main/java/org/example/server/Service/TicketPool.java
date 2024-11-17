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

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private ConfigurationService configurationService;

    public TicketPool() {
        this.tickets = new LinkedList<>();
        this.totalTicketsAdded = 0;
        this.totalTicketsSold = 0;
    }

    @PostConstruct
    public void initialize() {
        ConfigurationDTO config = configurationService.getLatestConfiguration();
        if (config != null) {
            this.maxCapacity = config.getMaxTicketCapacity();
            this.totalTicketsToRelease = config.getTotalTickets();
            System.out.println("TicketPool initialized with max capacity: " + maxCapacity + " and total tickets to release: " + totalTicketsToRelease);
        } else {
            System.out.println("No configuration found. TicketPool not initialized.");
        }
    }

    // Synchronized method to add a ticket
    public synchronized boolean addTicket(Ticket ticket) throws InterruptedException {
        while (tickets.size() >= maxCapacity) {
            wait(); // Wait if the pool is full
        }
        if (totalTicketsAdded >= totalTicketsToRelease) {
            return false; // No more tickets can be added
        }
        ticketRepository.save(ticket);
        tickets.add(ticket);
        totalTicketsAdded++;
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
