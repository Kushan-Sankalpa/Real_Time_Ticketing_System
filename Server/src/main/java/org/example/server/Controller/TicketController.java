package org.example.server.Controller;


import org.example.server.DTO.TicketDTO;
import org.example.server.Service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @GetMapping("/getAllTickets")
    public List<TicketDTO> getAllTickets() {
        return ticketService.getAllTickets();
    }

    @PostMapping("/addTickets")
    public TicketDTO addTicket(@RequestBody TicketDTO ticketDTO) {
        return ticketService.addTicket(ticketDTO);
    }

    @PostMapping("/{ticketId}/purchase") //tp://localhost:8080/api/tickets/11/purchase?customerName=Kushan
    public TicketDTO purchaseTicket(@PathVariable Long ticketId, @RequestParam String customerName) {
        return ticketService.purchaseTicket(ticketId, customerName);
    }

    // Additional endpoints as needed
}