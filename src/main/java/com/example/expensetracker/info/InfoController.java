package com.example.expensetracker.info;

import org.springframework.boot.info.BuildProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InfoController {

    private final BuildProperties buildProperties;

    public InfoController(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @GetMapping("/api/v1/info")
    public InfoResponse info() {
        return new InfoResponse(buildProperties.getName(), buildProperties.getVersion());
    }
}
