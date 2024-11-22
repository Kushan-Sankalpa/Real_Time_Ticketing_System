// File: src/main/java/org/example/server/Controller/TicketController.java

package org.example.server.Controller;

import org.example.server.DTO.TicketDTO;
import org.example.server.DTO.TicketStatisticsDTO;
import org.example.server.Service.TicketPool;
import org.example.server.Service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private TicketPool ticketPool;

    @GetMapping("/getAllTickets")
    public List<TicketDTO> getAllTickets() {
        return ticketService.getAllTickets();
    }

    @PostMapping("/addTickets")
    public TicketDTO addTicket(@RequestBody TicketDTO ticketDTO) {
        return ticketService.addTicket(ticketDTO);
    }

    @PostMapping("/{ticketId}/purchase")
    public TicketDTO purchaseTicket(@PathVariable Long ticketId, @RequestParam String customerName) {
        return ticketService.purchaseTicket(ticketId, customerName);
    }

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
    @GetMapping("/statistics")
    public ResponseEntity<TicketStatisticsDTO> getTicketStatistics() {
        TicketStatisticsDTO stats = ticketPool.getTicketStatistics();
        return ResponseEntity.ok(stats);
    }
}