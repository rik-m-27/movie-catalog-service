package com.movie.catalog.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.movie.catalog.config.AppConfig;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api")
@Tag(name = "System Info", description = "Application metadata and configuration info")
public class ApiController {

    private final AppConfig appConfig;

    public ApiController(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    @GetMapping("/info")
    @Operation(summary = "Get application info")
    public Map<String, String> getInfo() {
        return Map.of(
            "name", appConfig.name(),
            "version", appConfig.version()
        );
    }
}