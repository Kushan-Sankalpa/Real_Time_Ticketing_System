package org.example.server.Service;



/**
 * Interface for managing vendor operations in the ticketing system.
 */
public interface VendorService {
    void startVendors(int numberOfVendors, int ticketReleaseRate);
    void stopVendors();
}