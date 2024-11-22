// File: src/main/java/org/example/server/Controller/VendorController.java

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

    /**
     * Retrieves all vendors.
     *
     * @return List of VendorDTOs.
     */
    @GetMapping("/GetVendors")
    public List<VendorDTO> getAllVendors() {
        return vendorService.getAllVendors();
    }

    /**
     * Creates a new vendor.
     *
     * @param vendorDTO The vendor data.
     * @return The created VendorDTO.
     */
    @PostMapping("/createVendors")
    public VendorDTO createVendor(@RequestBody VendorDTO vendorDTO) {
        return vendorService.createVendor(vendorDTO);
    }
}
