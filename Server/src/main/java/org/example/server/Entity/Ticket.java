package org.example.server.Entity;



import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tickets")
@Data
@NoArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ticketCode;

    private String vendorName; // Vendor who released the ticket

    private String customerName; // Customer who purchased the ticket, nullable if not sold

    private boolean isSold;

    // Additional fields as needed
}
