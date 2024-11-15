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
        ConfigurationDTO dto = new ConfigurationDTO();
        dto.setTotalTickets(config.getTotalTickets());
        dto.setInitialTickets(config.getInitialTickets());
        dto.setTicketReleaseRate(config.getTicketReleaseRate());
        dto.setCustomerRetrievalRate(config.getCustomerRetrievalRate());
        dto.setMaxTicketCapacity(config.getMaxTicketCapacity());
        dto.setNumberOfVendors(config.getNumberOfVendors());
        dto.setNumberOfCustomers(config.getNumberOfCustomers());
        return dto;
    }

    // Save a new configuration
    public ConfigurationDTO saveConfiguration(ConfigurationDTO configurationDTO) {
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

    // Additional methods as needed
}
