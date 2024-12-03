package org.example.server.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) for Ticket Information
 */
@Data
@NoArgsConstructor
public class TicketDTO {
    private Long id;
    private String ticketCode;
    private String vendorName;
    private String customerName;
    private boolean isSold;


}