package org.example.server.Controller;

import org.example.server.DTO.TicketDTO;
import org.example.server.DTO.TicketStatisticsDTO;
import org.example.server.Service.TicketPool;
import org.example.server.Service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


/**
 * Controller for handling ticket-related operations such as purchase,
 * retrieving available tickets, and viewing ticket statistics.
 */
@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private TicketPool ticketPool;


    /**
     * Handles ticket purchase requests.
     *
     * @param ticketId The ID of the ticket to purchase.
     * @param customerName The name of the customer making the purchase.
     * @return Details of the purchased ticket.
     */
    @PostMapping("/{ticketId}/purchase")
    public TicketDTO purchaseTicket(@PathVariable Long ticketId, @RequestParam String customerName) {
        return ticketService.purchaseTicket(ticketId, customerName);
    }



    /**
     * Gets the number of available tickets.
     *
     * @return The count of available tickets in the pool.
     */
    @GetMapping("/getAvailableTickets")
    public ResponseEntity<Integer> getAvailableTickets() {
        int availableTickets = ticketPool.getAvailableTicketsCount();
        System.out.println("Available tickets: " + availableTickets);
        return ResponseEntity.ok(availableTickets);
    }

    /**
     * Retrieves the current ticket statistics.
     *
     * @return TicketStatisticsDTO with sold, released, and yet-to-release tickets.
     */
    @GetMapping("/statistics") //ok
    public ResponseEntity<TicketStatisticsDTO> getTicketStatistics() {
        TicketStatisticsDTO stats = ticketPool.getTicketStatistics();
        return ResponseEntity.ok(stats);
    }
}