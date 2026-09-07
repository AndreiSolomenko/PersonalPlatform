package com.personalplatform.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public String test(
            @RequestParam(required = false) String file,
            @RequestParam(required = false) String text
    ) {

        return """
                FILE:
                %s

                TEXT:
                %s
                """.formatted(file, text);
    }
}
