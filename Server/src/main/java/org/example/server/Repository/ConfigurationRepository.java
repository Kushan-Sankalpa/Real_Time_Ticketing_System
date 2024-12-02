package org.example.server.Repository;

import org.example.server.Entity.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Repository interface for Configuration entities.
 */
@Repository
public interface ConfigurationRepository extends JpaRepository<Configuration, Long> {
    Configuration findTopByOrderByIdDesc();
}