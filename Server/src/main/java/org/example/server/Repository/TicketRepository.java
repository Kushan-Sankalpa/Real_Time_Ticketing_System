package org.example.server.Repository;

import org.example.server.Entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    long countByIsSold(boolean isSold);
    List<Ticket> findByIsSold(boolean isSold);

    // Add this method
    long countByVendorName(String vendorName);
}
