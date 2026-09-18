package com.personalplatform.controllers;

import com.personalplatform.services.CsvImportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.personalplatform.repos.StoreDataRepository;

import java.util.Map;

@RestController
public class DataController {

    private final CsvImportService csvImportService;
    private final StoreDataRepository storeDataRepository;

    public DataController(CsvImportService csvImportService, StoreDataRepository storeDataRepository) {
        this.csvImportService = csvImportService;
        this.storeDataRepository = storeDataRepository;
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

    @GetMapping("/all_data")
    public ResponseEntity<?> getAllData() {

        return ResponseEntity.ok(
                storeDataRepository.findAll()
        );
    }









}
