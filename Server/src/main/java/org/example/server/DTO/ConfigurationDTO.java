

package org.example.server.DTO;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ConfigurationDTO {

    @NotNull(message = "Ticket release rate is required.")
    @Positive(message = "Ticket release rate must be positive.")
    private Integer ticketReleaseRate;

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

    private Long id;
}
