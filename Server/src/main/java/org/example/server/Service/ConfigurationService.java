package org.example.server.Service;

import org.example.server.DTO.ConfigurationDTO;
import org.example.server.Entity.Configuration;
import org.example.server.Repository.ConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfigurationService {

    @Autowired
    private ConfigurationRepository configurationRepository;

    // Get the latest configuration
    public ConfigurationDTO getLatestConfiguration() {
        Configuration config = configurationRepository.findTopByOrderByIdDesc();
        if (config == null) {
            return null;
        }
        return convertToDTO(config);
    }

    // Save a new configuration
    public ConfigurationDTO saveConfiguration(ConfigurationDTO configurationDTO) {
        // Validation Logic
        if (configurationDTO.getTotalTickets() > configurationDTO.getMaxTicketCapacity()) {
            throw new IllegalArgumentException("Total tickets cannot exceed maximum ticket capacity.");
        }
        if (configurationDTO.getInitialTickets() > configurationDTO.getTotalTickets()) {
            throw new IllegalArgumentException("Initial tickets cannot exceed total tickets.");
        }

        Configuration config = new Configuration();
        config.setTotalTickets(configurationDTO.getTotalTickets());
        config.setInitialTickets(configurationDTO.getInitialTickets());
        config.setTicketReleaseRate(configurationDTO.getTicketReleaseRate());
        config.setCustomerRetrievalRate(configurationDTO.getCustomerRetrievalRate());
        config.setMaxTicketCapacity(configurationDTO.getMaxTicketCapacity());
        config.setNumberOfVendors(configurationDTO.getNumberOfVendors());
        config.setNumberOfCustomers(configurationDTO.getNumberOfCustomers());
        Configuration savedConfig = configurationRepository.save(config);
        configurationDTO.setId(savedConfig.getId());
        return configurationDTO;
    }

    private ConfigurationDTO convertToDTO(Configuration config) {
        ConfigurationDTO dto = new ConfigurationDTO();
        dto.setId(config.getId());
        dto.setTotalTickets(config.getTotalTickets());
        dto.setInitialTickets(config.getInitialTickets());
        dto.setTicketReleaseRate(config.getTicketReleaseRate());
        dto.setCustomerRetrievalRate(config.getCustomerRetrievalRate());
        dto.setMaxTicketCapacity(config.getMaxTicketCapacity());
        dto.setNumberOfVendors(config.getNumberOfVendors());
        dto.setNumberOfCustomers(config.getNumberOfCustomers());
        return dto;
    }

    // Additional methods as needed
}
