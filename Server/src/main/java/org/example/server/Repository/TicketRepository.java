// File: src/main/java/org/example/server/Repository/TicketRepository.java

package org.example.server.Repository;

import org.example.server.Entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
