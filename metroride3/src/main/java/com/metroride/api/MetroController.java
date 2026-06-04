package com.metroride.api;

import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/metro")
public class MetroController {

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "UP", "service", "MetroRide API", "version", "1.0.0");
    }

    @GetMapping("/timings")
    public List<Map<String, String>> getTimings() {
        return List.of(
            Map.of("station", "Central", "next_train", "10:05 AM", "line", "Blue"),
            Map.of("station", "Airport", "next_train", "10:12 AM", "line", "Red"),
            Map.of("station", "City Park", "next_train", "10:08 AM", "line", "Green")
        );
    }

    @GetMapping("/timings/{station}")
    public Map<String, String> getStationTiming(@PathVariable String station) {
        return Map.of("station", station, "next_train", "10:15 AM", "line", "Blue");
    }
}
