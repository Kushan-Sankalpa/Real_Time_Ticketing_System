// File: src/main/java/org/example/server/Repository/CustomerRepository.java

package org.example.server.Repository;

import org.example.server.Entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
