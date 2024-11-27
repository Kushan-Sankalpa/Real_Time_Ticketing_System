package org.example.server.Controller;

import org.example.server.DTO.ConfigurationDTO;
import org.example.server.Service.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/configurations")
@Validated
public class ConfigurationController {

    @Autowired
    private ConfigurationService configurationService;


    /**
     * Saves a new configuration.
     *
     * @param configurationDTO The configuration data.
     * @return The saved ConfigurationDTO.
     */
    @PostMapping("/saveConfigurations")
    public ConfigurationDTO saveConfiguration(@Valid @RequestBody ConfigurationDTO configurationDTO) {
        return configurationService.saveConfiguration(configurationDTO);
    }

    /**
     * Deletes the latest configuration.
     *
     * @return A response indicating success or failure.
     */
    @DeleteMapping("/deleteLatest")
    public ResponseEntity<String> deleteLatestConfiguration() {
        boolean deleted = configurationService.deleteLatestConfiguration();
        if (deleted) {
            return ResponseEntity.ok("Latest configuration deleted.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No configuration to delete.");
        }
    }
}