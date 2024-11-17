package org.example.server.Dto;

import lombok.Data;

@Data
public class TicketDTO {
    private Long id;
    private String ticketCode;
    private String vendorName;
    private String customerName;
    private boolean isSold;

}
