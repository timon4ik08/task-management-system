package com.task_management_system.task_management_system.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProfileLogger {

    @Autowired
    private Environment env;

    @PostConstruct
    public void logActiveProfile() {
        log.info("Active profile: {}", String.join(", ", env.getActiveProfiles()));
    }
}