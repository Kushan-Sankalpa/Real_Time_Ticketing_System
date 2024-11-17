package org.example.server.Dto;

import lombok.Data;

@Data
public class CustomerDTO {
    private Long id;
    private String customerName;
    private int customerRetrievalRate;

}
