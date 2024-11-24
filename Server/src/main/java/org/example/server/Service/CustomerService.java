package org.example.server.Service;


public interface CustomerService {
    void startCustomers(int numberOfCustomers, int customerRetrievalRate);
    void stopCustomers();
}