package org.example.server.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "configurations")
@Data
@NoArgsConstructor
public class Configuration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

<<<<<<< HEAD
=======
    private int totalTickets;

>>>>>>> 01450f842c042e9d4f9b6c56835f9b566a897e8e
    private int ticketReleaseRate;
    private int customerRetrievalRate;
    private int maxTicketCapacity;
<<<<<<< HEAD
    private int totalTickets;
    private int initialTickets;
    private int numberOfVendors;
    private int numberOfCustomers;

    // Additional fields as needed
=======


>>>>>>> 01450f842c042e9d4f9b6c56835f9b566a897e8e
}
