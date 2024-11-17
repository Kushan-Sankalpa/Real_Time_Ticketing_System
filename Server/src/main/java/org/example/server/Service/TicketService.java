package org.example.server.Service;

import org.example.server.DTO.TicketDTO;
import org.example.server.Entity.Ticket;
import org.example.server.Repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    // Convert Ticket to TicketDTO
    private TicketDTO convertToDTO(Ticket ticket) {
        TicketDTO dto = new TicketDTO();
        dto.setId(ticket.getId());
        dto.setTicketCode(ticket.getTicketCode());
        dto.setVendorName(ticket.getVendorName());
        dto.setCustomerName(ticket.getCustomerName());
        dto.setSold(ticket.isSold());
        return dto;
    }

    // Get all tickets
    public List<TicketDTO> getAllTickets() {
        List<Ticket> tickets = ticketRepository.findAll();
        return tickets.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Add a new ticket
    public TicketDTO addTicket(TicketDTO ticketDTO) {
        Ticket ticket = new Ticket();
        ticket.setTicketCode(ticketDTO.getTicketCode());
        ticket.setVendorName(ticketDTO.getVendorName());
        ticket.setSold(false);
        Ticket savedTicket = ticketRepository.save(ticket);
        return convertToDTO(savedTicket);
    }

    // Purchase a ticket
    public TicketDTO purchaseTicket(Long ticketId, String customerName) {
        Ticket ticket = ticketRepository.findById(ticketId).orElse(null);
        if (ticket != null && !ticket.isSold()) {
            ticket.setSold(true);
            ticket.setCustomerName(customerName);
            Ticket savedTicket = ticketRepository.save(ticket);
            return convertToDTO(savedTicket);
        }
        return null;
    }

    // Additional methods as needed
}