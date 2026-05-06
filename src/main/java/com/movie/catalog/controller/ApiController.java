package com.movie.catalog.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.movie.catalog.config.AppConfig;

@RestController
@RequestMapping("/api")
public class ApiController {

    private AppConfig appConfig;

    public ApiController(AppConfig appConfig){
        this.appConfig = appConfig;
    }

    @GetMapping("/info")
    public Map<String, String> getInfo() {
        return Map.of(
            "name", appConfig.name(),
            "version", appConfig.version()
        );
    }

}
