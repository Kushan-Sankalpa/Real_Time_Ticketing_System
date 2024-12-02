package org.example.server.Service;

/**
 * Interface for managing customer operations in the ticketing system.
 */
public interface CustomerService {
    void startCustomers(int numberOfCustomers, int customerRetrievalRate);
    void stopCustomers();
}