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

    public synchronized void initialize(ConfigurationDTO config) {
        if (config == null) {
            System.out.println("No configuration provided. TicketPool not initialized.");
            return;
        }

        this.maxCapacity = config.getMaxTicketCapacity();
        this.totalTicketsToRelease = config.getTotalTickets();
        this.initialTickets = config.getInitialTickets();

        List<Ticket> existingTickets = ticketRepository.findByIsSold(false);
        tickets.clear();
        tickets.addAll(existingTickets);

        totalTicketsAdded = (int) ticketRepository.count();
        totalTicketsSold = (int) ticketRepository.countByIsSold(true);

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

    public synchronized void reset() {
        tickets.clear();
        totalTicketsAdded = 0;
        totalTicketsSold = 0;
        ticketRepository.deleteAll();
        System.out.println("TicketPool has been reset.");
    }

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

        ticketRepository.save(ticket);
        tickets.add(ticket);
        totalTicketsAdded++;

        String logMessage = ticket.getVendorName() + " added ticket: " + ticket.getTicketCode() + ", [ Available Tickets in pool: " + tickets.size() + "]";
        messagingTemplate.convertAndSend("/topic/logs", logMessage);

        System.out.println(ticket.getVendorName() + " added ticket: " + ticket.getTicketCode() + ", [ Available Tickets in pool: " + tickets.size() + "]");

        notifyAll();
        return true;
    }

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
            ticketRepository.save(ticket);
            totalTicketsSold++;

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

    public synchronized TicketStatisticsDTO getTicketStatistics() {
        TicketStatisticsDTO stats = new TicketStatisticsDTO();
        stats.setTotalTicketsSold(totalTicketsSold);
        stats.setTotalTicketsReleased(totalTicketsAdded);
        stats.setTicketsYetToRelease(totalTicketsToRelease - totalTicketsAdded);
        return stats;
    }
}
