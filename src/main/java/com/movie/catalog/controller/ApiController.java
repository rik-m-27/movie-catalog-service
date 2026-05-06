package com.movie.catalog.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.movie.catalog.config.AppConfig;
import com.movie.catalog.dto.AppInfoResponseDTO;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping(path = "/api", produces = "application/json")
@Tag(
    name = "System Info",
    description = "Provides runtime application metadata such as service name and version"
)
public class ApiController {

    private final AppConfig appConfig;

    public ApiController(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    @GetMapping("/info")
    @Operation(
        summary = "Get application info",
        description = "Returns the service name and version from configuration."
    )
    public AppInfoResponseDTO getInfo() {
        return new AppInfoResponseDTO(appConfig.name(), appConfig.version());
    }
}