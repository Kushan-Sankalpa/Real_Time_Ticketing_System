package org.example.server.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Entity class representing a Customer stored in the database.
 */
@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName;
    private int customerRetrievalRate;
}