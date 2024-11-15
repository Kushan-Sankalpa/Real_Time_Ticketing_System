package org.example.server.DTO;


import lombok.Data;

@Data
public class ConfigurationDTO {
    private int totalTickets;
    private int initialTickets;
    private int ticketReleaseRate;
    private int customerRetrievalRate;
    private int maxTicketCapacity;
    private int numberOfVendors;
    private int numberOfCustomers;

    public void setId(Long id) {
    }

}
