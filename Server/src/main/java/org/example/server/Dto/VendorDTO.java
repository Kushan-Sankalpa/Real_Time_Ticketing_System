package org.example.server.Dto;


import lombok.Data;

@Data
public class VendorDTO {
    private Long id;
    private String vendorName;
    private int ticketReleaseRate;

}