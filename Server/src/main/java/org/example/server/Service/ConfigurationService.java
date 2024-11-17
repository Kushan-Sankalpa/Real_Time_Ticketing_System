package org.example.server.Service;

<<<<<<< HEAD
import org.example.server.DTO.ConfigurationDTO;
=======

import org.example.server.Dto.ConfigurationDTO;
>>>>>>> 01450f842c042e9d4f9b6c56835f9b566a897e8e
import org.example.server.Entity.Configuration;
import org.example.server.Repository.ConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfigurationService {

    @Autowired
    private ConfigurationRepository configurationRepository;

    public ConfigurationDTO getLatestConfiguration() {
        Configuration config = configurationRepository.findTopByOrderByIdDesc();
        if (config == null) {
            return null;
        }
<<<<<<< HEAD
        return convertToDTO(config);
=======
        ConfigurationDTO dto = new ConfigurationDTO();
        dto.setId(config.getId());
        dto.setTotalTickets(config.getTotalTickets());
        dto.setTicketReleaseRate(config.getTicketReleaseRate());
        dto.setCustomerRetrievalRate(config.getCustomerRetrievalRate());
        dto.setMaxTicketCapacity(config.getMaxTicketCapacity());
        return dto;
>>>>>>> 01450f842c042e9d4f9b6c56835f9b566a897e8e
    }

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
        config.setTicketReleaseRate(configurationDTO.getTicketReleaseRate());
        config.setCustomerRetrievalRate(configurationDTO.getCustomerRetrievalRate());
        config.setMaxTicketCapacity(configurationDTO.getMaxTicketCapacity());

        Configuration savedConfig = configurationRepository.save(config);
        configurationDTO.setId(savedConfig.getId());
        return configurationDTO;
    }
<<<<<<< HEAD

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
=======
>>>>>>> 01450f842c042e9d4f9b6c56835f9b566a897e8e
}
    // Additional methods as needed

