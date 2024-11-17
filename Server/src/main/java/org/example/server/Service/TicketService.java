package org.example.server.Service;

import org.example.server.Dto.TicketDTO;

import java.util.List;

public interface TicketService {
    List<TicketDTO> getAllTickets();
    TicketDTO addTicket(TicketDTO ticketDTO);
    TicketDTO purchaseTicket(Long ticketId, String customerName);
    TicketDTO updateTicket(Long ticketId, TicketDTO ticketDTO, String currentVendorName);
    void deleteTicket(Long ticketId, String currentVendorName);
}
