package org.example.server.DTO;


import lombok.Data;

@Data
public class VendorDTO {
    private Long id;
    private String vendorName;
    private int ticketReleaseRate;

}

