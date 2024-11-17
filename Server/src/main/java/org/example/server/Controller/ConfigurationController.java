package org.example.server.Controller;



import org.example.server.DTO.ConfigurationDTO;
import org.example.server.Service.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/configurations")
public class ConfigurationController {

    @Autowired
    private ConfigurationService configurationService;

    @GetMapping("/latest")
    public ConfigurationDTO getLatestConfiguration() {
        return configurationService.getLatestConfiguration();
    }

    @PostMapping("/saveConfigurations")
    public ConfigurationDTO saveConfiguration(@RequestBody ConfigurationDTO configurationDTO) {
        return configurationService.saveConfiguration(configurationDTO);
    }

    // Additional endpoints as needed
}