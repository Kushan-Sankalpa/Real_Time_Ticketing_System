package org.example.server.Entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Entity class representing a Ticket stored in the database.
 */
@Entity
@Table(name = "tickets")
@Data
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ticket_code", nullable = false, unique = true)
    private String ticketCode;

    @Column(name = "vendor_name", nullable = false)
    private String vendorName;

    @Column(name = "is_sold", nullable = false)
    private boolean isSold;

    @Column(name = "customer_name")
    private String customerName;
}