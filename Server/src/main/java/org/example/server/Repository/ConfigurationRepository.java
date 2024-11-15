package org.example.server.Repository;


import org.example.server.Entity.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigurationRepository extends JpaRepository<Configuration, Long> {
    // Assuming only one configuration exists
    Configuration findTopByOrderByIdDesc();
}
