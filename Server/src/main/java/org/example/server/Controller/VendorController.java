package org.example.server.Controller;


import org.example.server.DTO.VendorDTO;
import org.example.server.Service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendors")
public class VendorController {

    @Autowired
    private VendorService vendorService;

    @GetMapping("/GetVendors")
    public List<VendorDTO> getAllVendors() {
        return vendorService.getAllVendors();
    }

    @PostMapping("createVendors")
    public VendorDTO createVendor(@RequestBody VendorDTO vendorDTO) {
        return vendorService.createVendor(vendorDTO);
    }

    // Additional endpoints as needed
}
