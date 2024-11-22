package org.example.server.DTO;

import lombok.Data;

@Data
public class TicketStatisticsDTO {
    private int totalTicketsSold;
    private int totalTicketsReleased;
    private int ticketsYetToRelease;
}