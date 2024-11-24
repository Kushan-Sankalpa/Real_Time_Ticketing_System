// File: src/main/java/org/example/server/Service/ConfigurationService.java

package org.example.server.Service;

import org.example.server.DTO.ConfigurationDTO;
import org.example.server.Entity.Configuration;
import org.example.server.Repository.ConfigurationRepository;
import org.example.server.Repository.CustomerRepository;
import org.example.server.Repository.TicketRepository;
import org.example.server.Repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfigurationService {

    @Autowired
    private ConfigurationRepository configurationRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private CustomerRepository customerRepository;

    /**
     * Retrieves the latest configuration from the database.
     *
     * @return ConfigurationDTO or null if none exists.
     */
    public ConfigurationDTO getLatestConfiguration() {
        Configuration config = configurationRepository.findTopByOrderByIdDesc();
        if (config == null) {
            return null;
        }
        return convertToDTO(config);
    }

    /**
     * Saves a new configuration after validating constraints.
     *
     * @param configurationDTO The configuration data.
     * @return The saved ConfigurationDTO.
     */
    public ConfigurationDTO saveConfiguration(ConfigurationDTO configurationDTO) {
        // Validation Logic
        if (configurationDTO.getTotalTickets() > configurationDTO.getMaxTicketCapacity()) {
            throw new IllegalArgumentException("Total tickets cannot exceed maximum ticket capacity.");
        }
        if (configurationDTO.getInitialTickets() > configurationDTO.getTotalTickets()) {
            throw new IllegalArgumentException("Initial tickets cannot exceed total tickets.");
        }

        Configuration config = new Configuration();
        config.setTicketReleaseRate(configurationDTO.getTicketReleaseRate());
        config.setCustomerRetrievalRate(configurationDTO.getCustomerRetrievalRate());
        config.setMaxTicketCapacity(configurationDTO.getMaxTicketCapacity());
        config.setTotalTickets(configurationDTO.getTotalTickets());
        config.setInitialTickets(configurationDTO.getInitialTickets());
        config.setNumberOfVendors(configurationDTO.getNumberOfVendors());
        config.setNumberOfCustomers(configurationDTO.getNumberOfCustomers());
        Configuration savedConfig = configurationRepository.save(config);
        configurationDTO.setId(savedConfig.getId());
        return configurationDTO;
    }

    /**
     * Converts Configuration entity to DTO.
     *
     * @param config The Configuration entity.
     * @return The ConfigurationDTO.
     */
    private ConfigurationDTO convertToDTO(Configuration config) {
        ConfigurationDTO dto = new ConfigurationDTO();
        dto.setId(config.getId());
        dto.setTicketReleaseRate(config.getTicketReleaseRate());
        dto.setCustomerRetrievalRate(config.getCustomerRetrievalRate());
        dto.setMaxTicketCapacity(config.getMaxTicketCapacity());
        dto.setTotalTickets(config.getTotalTickets());
        dto.setInitialTickets(config.getInitialTickets());
        dto.setNumberOfVendors(config.getNumberOfVendors());
        dto.setNumberOfCustomers(config.getNumberOfCustomers());
        return dto;
    }

    /**
     * Deletes the latest configuration from the database.
     *
     * @return True if deletion was successful, false otherwise.
     */
    public boolean deleteLatestConfiguration() {
        Configuration config = configurationRepository.findTopByOrderByIdDesc();
        if (config != null) {
            configurationRepository.delete(config);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Deletes all tickets from the database.
     */
    public void deleteAllTickets() {
        ticketRepository.deleteAll();
    }

    public void deleteAllVendors() {
        vendorRepository.deleteAll();
    }

    public void deleteAllCustomers() {
        customerRepository.deleteAll();
    }
}