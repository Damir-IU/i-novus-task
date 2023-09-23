package com.task.inovus.controllers;

import com.task.inovus.service.GeneratedNumberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NumberRestController {
    private final GeneratedNumberService generatedNumberService;

    public static final String RANDOM = "/random";
    public static final String NEXT = "/next";

    @GetMapping(RANDOM)
    public ResponseEntity<String> random() {
        return ResponseEntity.ok(generatedNumberService.generateRandomNumber());
    }

    @GetMapping(NEXT)
    public ResponseEntity<String> next() {
        return ResponseEntity.ok(generatedNumberService.generateNextNumber());
    }
}