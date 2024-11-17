package org.example.server.Dto;


import lombok.Data;

@Data
public class ConfigurationDTO {
    private Long id;
    private int totalTickets;
    private int ticketReleaseRate;
    private int customerRetrievalRate;
    private int maxTicketCapacity;


}

