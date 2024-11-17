package org.example.server.Dto;



import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;



@Data
public class ConfigurationDTO {
<<<<<<< HEAD:Server/src/main/java/org/example/server/DTO/ConfigurationDTO.java

    @NotNull(message = "Ticket release rate is required.")
    @Positive(message = "Ticket release rate must be positive.")
    private Integer ticketReleaseRate;
=======
    private Long id;
    private int totalTickets;
    private int ticketReleaseRate;
    private int customerRetrievalRate;
    private int maxTicketCapacity;

>>>>>>> 01450f842c042e9d4f9b6c56835f9b566a897e8e:Server/src/main/java/org/example/server/Dto/ConfigurationDTO.java

    @NotNull(message = "Customer retrieval rate is required.")
    @Positive(message = "Customer retrieval rate must be positive.")
    private Integer customerRetrievalRate;

    @NotNull(message = "Maximum ticket capacity is required.")
    @Positive(message = "Maximum ticket capacity must be positive.")
    private Integer maxTicketCapacity;

    @NotNull(message = "Total tickets are required.")
    @Positive(message = "Total tickets must be positive.")
    private Integer totalTickets;

    @NotNull(message = "Initial tickets are required.")
    @PositiveOrZero(message = "Initial tickets must be zero or positive.")
    private Integer initialTickets;

    @NotNull(message = "Number of vendors is required.")
    @Positive(message = "Number of vendors must be positive.")
    private Integer numberOfVendors;

    @NotNull(message = "Number of customers is required.")
    @Positive(message = "Number of customers must be positive.")
    private Integer numberOfCustomers;

    private Long id; // Include this if necessary
}

