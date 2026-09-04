package com.personalplatform.controllers;

import com.personalplatform.model.TestData;
import com.personalplatform.repos.TestDataRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private final TestDataRepository repository;

    public TestController(TestDataRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/test")
    public String test() {
        return repository.findAll()
                .stream()
                .filter(item -> "test".equals(item.getKey()))
                .map(TestData::getValue)
                .findFirst()
                .orElse("Key not found");
    }
}
