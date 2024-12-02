package org.example.server.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Entity class representing a Vendor stored in the database.
 */
@Entity
@Table(name = "vendors")
@Data
@NoArgsConstructor
public class Vendor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String vendorName;
    private int ticketReleaseRate;
}