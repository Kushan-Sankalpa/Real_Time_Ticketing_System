package org.example.server.DTO;

import lombok.Data;

@Data
public class CustomerDTO {
    private Long id;
    private String customerName;
    private int customerRetrievalRate;
}
