// File: src/main/java/org/example/server/Service/TicketPool.java

package org.example.server.Service;

import org.example.server.DTO.ConfigurationDTO;
import org.example.server.DTO.TicketStatisticsDTO;
import org.example.server.Entity.Ticket;
import org.example.server.Repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
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



    public TicketPool() {
        this.tickets = new LinkedList<>();
        this.totalTicketsAdded = 0;
        this.totalTicketsSold = 0;
        this.vendorTicketsReleased = 0;
    }

    /**
     * Initializes the TicketPool based on the provided configuration.
     *
     * @param config The configuration DTO containing system parameters.
     */
    public synchronized void initialize(ConfigurationDTO config) {
        if (config == null) {
            System.out.println("No configuration provided. TicketPool not initialized.");
            return;
        }

        this.maxCapacity = config.getMaxTicketCapacity();
        this.totalTicketsToRelease = config.getTotalTickets();
        this.initialTickets = config.getInitialTickets();

        // Load existing tickets from the database
        List<Ticket> existingTickets = ticketRepository.findByIsSold(false);
        tickets.clear();
        tickets.addAll(existingTickets);

        // Update totalTicketsAdded and totalTicketsSold based on existing tickets
        totalTicketsAdded = (int) ticketRepository.count();
        totalTicketsSold = (int) ticketRepository.countByIsSold(true);

        if (totalTicketsAdded == 0) {
            // If no tickets have been added before, add initial tickets
            int ticketsToAdd = Math.min(initialTickets, maxCapacity);
            for (int i = 1; i <= ticketsToAdd; i++) {
                Ticket ticket = new Ticket();
                ticket.setTicketCode("Initial-Ticket-" + i);
                ticket.setVendorName("Initial");
                ticket.setSold(false);
                ticketRepository.save(ticket); // Persist to DB
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
     * Resets the TicketPool by clearing the in-memory queue and deleting all tickets from the database.
     */
    public synchronized void reset() {
        tickets.clear();
        totalTicketsAdded = 0;
        totalTicketsSold = 0;
        vendorTicketsReleased = 0;
        ticketRepository.deleteAll(); // Clear all tickets from DB
        System.out.println("TicketPool has been reset.");
    }

    /**
     * Adds a ticket to the pool and persists it to the database.
     *
     * @param ticket The ticket to be added.
     * @return True if the ticket was added successfully, false otherwise.
     */
    public synchronized boolean addTicket(Ticket ticket) {
        if (totalTicketsAdded >= totalTicketsToRelease) {
            // All tickets have been added
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

        ticketRepository.save(ticket); // Persist to DB
        tickets.add(ticket);
        totalTicketsAdded++;
        vendorTicketsReleased++;
        System.out.println("Vendor added ticket: " + ticket.getTicketCode() + ". Tickets in pool: " + tickets.size());

        notifyAll(); // Notify waiting customers
        return true;
    }

    /**
     * Removes a ticket from the pool, marks it as sold, and updates the database.
     *
     * @param customerName The name of the customer purchasing the ticket.
     * @return The purchased ticket, or null if no tickets are available.
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

        Ticket ticket = tickets.poll();
        if (ticket != null) {
            ticket.setSold(true);
            ticket.setCustomerName(customerName);
            ticketRepository.save(ticket); // Update in DB
            totalTicketsSold++;
            System.out.println("Customer " + customerName + " purchased ticket: " + ticket.getTicketCode() + ". Tickets left in pool: " + tickets.size());
        }

        notifyAll(); // Notify waiting vendors
        return ticket;
    }


    /**
     * Gets the current number of available tickets in the pool.
     *
     * @return The number of available tickets.
     */
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
     * Retrieves the current ticket statistics.
     *
     * @return TicketStatisticsDTO containing sold, released, and yet-to-release tickets.
     */
    public synchronized TicketStatisticsDTO getTicketStatistics() {
        TicketStatisticsDTO stats = new TicketStatisticsDTO();
        stats.setTotalTicketsSold(totalTicketsSold);
        stats.setTotalTicketsReleased(totalTicketsAdded);
        stats.setTicketsYetToRelease(totalTicketsToRelease - totalTicketsAdded);
        return stats;
    }
}
