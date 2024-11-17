package org.example.server.Controller;


import org.example.server.Dto.TicketDTO;
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

    @PostMapping("/{ticketId}/purchase")
    public TicketDTO purchaseTicket(@PathVariable Long ticketId, @RequestParam String customerName) {
        return ticketService.purchaseTicket(ticketId, customerName);
    }

<<<<<<< HEAD
    // Additional endpoints as needed
}
=======
    @PutMapping("/{ticketId}")
    public TicketDTO updateTicket(@PathVariable Long ticketId, @RequestBody TicketDTO ticketDTO, @RequestParam String vendorName) {
        return ticketService.updateTicket(ticketId, ticketDTO, vendorName);
    }

    @DeleteMapping("/{ticketId}")
    public String deleteTicket(@PathVariable Long ticketId, @RequestParam String vendorName) {
        ticketService.deleteTicket(ticketId, vendorName);
        return "Ticket deleted successfully.";
    }
}
>>>>>>> 01450f842c042e9d4f9b6c56835f9b566a897e8e
