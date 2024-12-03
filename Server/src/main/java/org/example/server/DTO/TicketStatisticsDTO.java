package org.example.server.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) for TicketStatics
 */
@Data
@NoArgsConstructor
public class TicketStatisticsDTO {
    private int totalTicketsSold;
    private int totalTicketsReleased;
    private int ticketsYetToRelease;


}