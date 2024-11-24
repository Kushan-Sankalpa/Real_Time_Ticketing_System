// File: src/main/java/org/example/server/Service/TicketService.java

package org.example.server.Service;

import org.example.server.DTO.TicketDTO;
import org.example.server.Entity.Ticket;
import org.example.server.Repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    private TicketDTO convertToDTO(Ticket ticket) {
        TicketDTO dto = new TicketDTO();
        dto.setId(ticket.getId());
        dto.setTicketCode(ticket.getTicketCode());
        dto.setVendorName(ticket.getVendorName());
        dto.setCustomerName(ticket.getCustomerName());
        dto.setSold(ticket.isSold());
        return dto;
    }


    /**
     * Purchases a ticket.
     *
     * @param ticketId     The ID of the ticket to purchase.
     * @param customerName The name of the customer purchasing the ticket.
     * @return The updated TicketDTO.
     */
    public TicketDTO purchaseTicket(Long ticketId, String customerName) {
        Ticket ticket = ticketRepository.findById(ticketId).orElse(null);
        if (ticket != null && !ticket.isSold()) {
            ticket.setSold(true);
            ticket.setCustomerName(customerName);
            Ticket savedTicket = ticketRepository.save(ticket);
            return convertToDTO(savedTicket);
        } else if (ticket != null && ticket.isSold()) {
            throw new IllegalArgumentException("Ticket is already sold.");
        } else {
            throw new IllegalArgumentException("Ticket not found.");
        }
    }
}
