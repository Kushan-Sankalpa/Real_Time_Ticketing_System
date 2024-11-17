package org.example.server.Entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName;
<<<<<<< HEAD

    private int customerRetrievalRate; // in milliseconds


}
=======
    private String email;
    private String password; // Store hashed passwords in production
    private int customerRetrievalRate;
    private boolean isVIP;
}
>>>>>>> 01450f842c042e9d4f9b6c56835f9b566a897e8e
