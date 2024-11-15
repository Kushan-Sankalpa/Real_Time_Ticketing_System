package org.example.server.Service;


import org.example.server.DTO.VendorDTO;
import org.example.server.Entity.Vendor;
import org.example.server.Repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VendorService {

    @Autowired
    private VendorRepository vendorRepository;

    // Convert Vendor to VendorDTO
    private VendorDTO convertToDTO(Vendor vendor) {
        VendorDTO dto = new VendorDTO();
        dto.setId(vendor.getId());
        dto.setVendorName(vendor.getVendorName());
        dto.setTicketReleaseRate(vendor.getTicketReleaseRate());
        return dto;
    }

    // Get all vendors
    public List<VendorDTO> getAllVendors() {
        List<Vendor> vendors = vendorRepository.findAll();
        return vendors.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Create a new vendor
    public VendorDTO createVendor(VendorDTO vendorDTO) {
        Vendor vendor = new Vendor();
        vendor.setVendorName(vendorDTO.getVendorName());
        vendor.setTicketReleaseRate(vendorDTO.getTicketReleaseRate());
        Vendor savedVendor = vendorRepository.save(vendor);
        return convertToDTO(savedVendor);
    }

    //
}
