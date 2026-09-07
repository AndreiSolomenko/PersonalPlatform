package com.personalplatform.controllers;

import com.personalplatform.services.CsvImportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
public class DataController {

    private final CsvImportService csvImportService;

    public DataController(CsvImportService csvImportService) {
        this.csvImportService = csvImportService;
    }

    @PostMapping("/add_data")
    public ResponseEntity<?> addData(
            @RequestParam("file") MultipartFile file
    ) {

        try {

            int count =
                    csvImportService.importCsv(file);

            return ResponseEntity.ok(
                    Map.of(
                            "success", true,
                            "message", "CSV imported successfully",
                            "records", count
                    )
            );

        } catch (Exception e) {

            return ResponseEntity.badRequest().body(
                    Map.of(
                            "success", false,
                            "error", e.getMessage()
                    )
            );
        }
    }
}
