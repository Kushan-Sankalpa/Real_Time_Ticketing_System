package org.example.server.Service;

import org.example.server.Dto.VendorDTO;

import java.util.List;

public interface VendorService {
    List<VendorDTO> getAllVendors();
    VendorDTO createVendor(VendorDTO vendorDTO);
    void startVendors(int numberOfVendors, int ticketReleaseRate);
    void stopVendors();
}