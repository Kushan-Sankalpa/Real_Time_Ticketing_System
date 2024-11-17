package org.example.server.Service;


import org.example.server.Dto.ConfigurationDTO;
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
        ConfigurationDTO dto = new ConfigurationDTO();
        dto.setId(config.getId());
        dto.setTotalTickets(config.getTotalTickets());
        dto.setTicketReleaseRate(config.getTicketReleaseRate());
        dto.setCustomerRetrievalRate(config.getCustomerRetrievalRate());
        dto.setMaxTicketCapacity(config.getMaxTicketCapacity());
        return dto;
    }

    public ConfigurationDTO saveConfiguration(ConfigurationDTO configurationDTO) {
        Configuration config = new Configuration();
        config.setTotalTickets(configurationDTO.getTotalTickets());
        config.setTicketReleaseRate(configurationDTO.getTicketReleaseRate());
        config.setCustomerRetrievalRate(configurationDTO.getCustomerRetrievalRate());
        config.setMaxTicketCapacity(configurationDTO.getMaxTicketCapacity());

        Configuration savedConfig = configurationRepository.save(config);
        configurationDTO.setId(savedConfig.getId());
        return configurationDTO;
    }
}
    // Additional methods as needed

