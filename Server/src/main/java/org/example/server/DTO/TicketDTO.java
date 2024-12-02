package org.example.server.DTO;

import lombok.Data;

/**
 * Data Transfer Object (DTO) for Ticket Information
 */
@Data
public class TicketDTO {
    private Long id;
    private String ticketCode;
    private String vendorName;
    private String customerName;
    private boolean isSold;
}