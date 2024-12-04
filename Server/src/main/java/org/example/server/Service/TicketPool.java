package org.example.server.Service;

import org.example.server.DTO.ConfigurationDTO;
import org.example.server.DTO.TicketStatisticsDTO;
import org.example.server.Entity.Ticket;
import org.example.server.Repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Service class responsible for managing the Ticket Pool.
 * Handles operations like ticket initialization, addition, removal, and statistics.
 */
@Service
public class TicketPool {

    private final Queue<Ticket> tickets;
    private int maxCapacity;
    private int totalTicketsToRelease;
    private int totalTicketsAdded;
    private int totalTicketsSold;
    private int initialTickets;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public TicketPool() {
        this.tickets = new LinkedList<>();
        this.totalTicketsAdded = 0;
        this.totalTicketsSold = 0;
    }


    /**
     * Initializes the Ticket Pool based on the provided configuration.
     * Loads existing tickets from the database or adds initial tickets if none exist.
     *
     * @param config The configuration details for the system.
     */
    public synchronized void initialize(ConfigurationDTO config) {
        if (config == null) {
            System.out.println("No configuration provided. TicketPool not initialized.");
            return;
        }

        // Set ticket pool configurations
        this.maxCapacity = config.getMaxTicketCapacity();
        this.totalTicketsToRelease = config.getTotalTickets();
        this.initialTickets = config.getInitialTickets();

        // Load existing unsold tickets from the database
        List<Ticket> existingTickets = ticketRepository.findByIsSold(false);
        tickets.clear();
        tickets.addAll(existingTickets);

        // Update counters based on database records
        totalTicketsAdded = (int) ticketRepository.count();
        totalTicketsSold = (int) ticketRepository.countByIsSold(true);

        // If no tickets exist in the database, add initial tickets
        if (totalTicketsAdded == 0) {
            int ticketsToAdd = Math.min(initialTickets, maxCapacity);
            for (int i = 1; i <= ticketsToAdd; i++) {
                Ticket ticket = new Ticket();
                ticket.setTicketCode("Initial-Ticket-" + i);
                ticket.setVendorName("Initial");
                ticket.setSold(false);
                ticketRepository.save(ticket);
                tickets.add(ticket);
                totalTicketsAdded++;
            }
            System.out.println(ticketsToAdd + " initial tickets added to the Ticket pool.");
        } else {
            System.out.println("Loaded " + tickets.size() + " existing tickets into the Ticket pool.");
        }

        System.out.println("Current Tickets in the Ticket pool: " + tickets.size());
    }


    /**
     * Resets the Ticket Pool by clearing the queue and deleting all tickets from the database.
     */

    public synchronized void reset() {
        tickets.clear();
        totalTicketsAdded = 0;
        totalTicketsSold = 0;
        ticketRepository.deleteAll();
        System.out.println("TicketPool has been reset.");
    }




    /**
     * Adds a ticket to the pool.
     * Waits if the pool is full and notifies other threads once the ticket is added.
     *
     * @param ticket The ticket to be added to the pool.
     * @return True if the ticket was added successfully, otherwise false.
     */
    public synchronized boolean addTicket(Ticket ticket) {
        if (totalTicketsAdded >= totalTicketsToRelease) {
            System.out.println("All tickets have been released. Vendor cannot add more tickets.");
            return false;
        }

        while (tickets.size() >= maxCapacity) {
            try {
                System.out.println("Ticket pool is full. Vendor is waiting to add tickets.");
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }

        // Save the ticket to the database and add it to the queue
        ticketRepository.save(ticket);
        tickets.add(ticket);
        totalTicketsAdded++;

        // Log the addition of the ticket
        String logMessage = ticket.getVendorName() + " added ticket: " + ticket.getTicketCode() + ", [ Available Tickets in pool: " + tickets.size() + "]";
        messagingTemplate.convertAndSend("/topic/logs", logMessage);

        System.out.println(ticket.getVendorName() + " added ticket: " + ticket.getTicketCode() + ", [ Available Tickets in pool: " + tickets.size() + "]");

        notifyAll();
        return true;
    }

    /**
     * Removes a ticket from the pool.
     * Waits if no tickets are available and notifies other threads once a ticket is removed.
     *
     * @param customerName The name of the customer purchasing the ticket.
     * @return The purchased ticket, or null if no tickets are available or all tickets are sold.
     */

    public synchronized Ticket removeTicket(String customerName) {
        while (tickets.isEmpty()) {
            if (totalTicketsSold >= totalTicketsToRelease) {
                System.out.println("All tickets sold. Customer " + customerName + " is exiting.");
                return null;
            }
            try {
                System.out.println("No tickets available. Customer " + customerName + " is waiting.");
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        }

        // Remove a ticket from the queue
        Ticket ticket = tickets.poll();
        if (ticket != null) {
            ticket.setSold(true);
            ticket.setCustomerName(customerName);
            ticketRepository.save(ticket); // Update the ticket as sold in the database
            totalTicketsSold++;

            // Log the ticket purchase.
            String logMessage =  customerName + " purchased ticket: " + ticket.getTicketCode() + ", [ Available Tickets in pool: " + tickets.size() + "]";
            messagingTemplate.convertAndSend("/topic/logs", logMessage);

            System.out.println( customerName +  " purchased ticket: " + ticket.getTicketCode() + ", [ Available Tickets in pool: " + tickets.size() + "]");
        }

        notifyAll();
        return ticket;
    }

    public synchronized int getAvailableTicketsCount() {
        return tickets.size();
    }

    public synchronized int getTotalTicketsAdded() {
        return totalTicketsAdded;
    }

    public synchronized int getTotalTicketsToRelease() {
        return totalTicketsToRelease;
    }

    /**
     * Retrieves the ticket statistics including sold, released, and yet to release tickets.
     *
     * @return A TicketStatisticsDTO object containing the statistics.
     */
    public synchronized TicketStatisticsDTO getTicketStatistics() {
        TicketStatisticsDTO stats = new TicketStatisticsDTO();
        stats.setTotalTicketsSold(totalTicketsSold);
        stats.setTotalTicketsReleased(totalTicketsAdded);
        stats.setTicketsYetToRelease(totalTicketsToRelease - totalTicketsAdded);
        return stats;
    }
}
