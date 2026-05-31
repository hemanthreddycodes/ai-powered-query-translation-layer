package com.queryservice.translator.controller;

import com.queryservice.translator.dto.QueryRequestDTO;
import com.queryservice.translator.dto.QueryResponseDTO;
import com.queryservice.translator.service.TranslatorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/translator")
public class TranslatorController {

    private final TranslatorService translatorService;

    public TranslatorController(
            TranslatorService translatorService) {
        this.translatorService = translatorService;
    }

    @PostMapping("/translate")
    public ResponseEntity<QueryResponseDTO> translate(
            @Valid @RequestBody QueryRequestDTO request) {

        QueryResponseDTO response =
                translatorService.translate(request, "anonymous");

        return ResponseEntity.ok(response);
    }
}