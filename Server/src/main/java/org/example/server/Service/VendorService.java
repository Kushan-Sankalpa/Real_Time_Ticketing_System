package org.example.server.Service;



public interface VendorService {
    void startVendors(int numberOfVendors, int ticketReleaseRate);
    void stopVendors();
}