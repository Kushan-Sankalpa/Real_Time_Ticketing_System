package org.example.server.Entity;

// src/main/java/com/example/ticketingsystem/entity/Configuration.java


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

    private int totalTickets;

    private int ticketReleaseRate;

    private int customerRetrievalRate;

    private int maxTicketCapacity;


}

