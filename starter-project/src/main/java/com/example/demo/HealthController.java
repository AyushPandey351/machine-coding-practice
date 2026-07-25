package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Sanity-check endpoint - confirm the app boots and Spring MVC is wired
 * before writing any real business logic.
 */
@RestController
public class HealthController {

    @GetMapping("/")
    public String health() {
        return "OK";
    }
}
