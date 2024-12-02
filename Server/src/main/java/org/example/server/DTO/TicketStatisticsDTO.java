package org.example.server.DTO;

import lombok.Data;

/**
 * Data Transfer Object (DTO) for TicketStatics
 */
@Data
public class TicketStatisticsDTO {
    private int totalTicketsSold;
    private int totalTicketsReleased;
    private int ticketsYetToRelease;
}