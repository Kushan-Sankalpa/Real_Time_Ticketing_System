package org.example.server.Service;

import org.example.server.Dto.TicketDTO;
import org.example.server.Entity.Ticket;
import org.example.server.Exception.ResourceNotFoundException;
import org.example.server.Exception.UnauthorizedException;

import org.example.server.Repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketServiceImpl implements TicketService {

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
    @Override
    public List<TicketDTO> getAllTickets() {
        List<Ticket> tickets = ticketRepository.findAll();
        return tickets.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Add a new ticket
    @Override
    public TicketDTO addTicket(TicketDTO ticketDTO) {
        Ticket ticket = new Ticket();
        ticket.setTicketCode(ticketDTO.getTicketCode());
        ticket.setVendorName(ticketDTO.getVendorName());
        ticket.setSold(false); // New tickets are unsold by default
        Ticket savedTicket = ticketRepository.save(ticket);
        return convertToDTO(savedTicket);
    }

    // Purchase a ticket
    @Override
    public TicketDTO purchaseTicket(Long ticketId, String customerName) {
        Ticket ticket = ticketRepository.findById(ticketId).orElse(null);
        if (ticket != null && !ticket.isSold()) {
            ticket.setSold(true);
            ticket.setCustomerName(customerName);
            Ticket savedTicket = ticketRepository.save(ticket);
            return convertToDTO(savedTicket);
        }
        return null; // Or throw an exception if preferred
    }

    // Update a ticket
    @Override
    public TicketDTO updateTicket(Long ticketId, TicketDTO ticketDTO, String currentVendorName) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with ID: " + ticketId));
        if (ticket.getVendorName().equals(currentVendorName)) {
            ticket.setTicketCode(ticketDTO.getTicketCode());
            ticket.setSold(ticketDTO.isSold());
            ticket.setCustomerName(ticketDTO.getCustomerName());
            Ticket updatedTicket = ticketRepository.save(ticket);
            return convertToDTO(updatedTicket);
        } else {
            throw new UnauthorizedException("You are not authorized to update this ticket.");
        }
    }

    // Delete a ticket
    @Override
    public void deleteTicket(Long ticketId, String currentVendorName) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with ID: " + ticketId));
        if (ticket.getVendorName().equals(currentVendorName)) {
            ticketRepository.deleteById(ticketId);
        } else {
            throw new UnauthorizedException("You are not authorized to delete this ticket.");
        }
    }
}
