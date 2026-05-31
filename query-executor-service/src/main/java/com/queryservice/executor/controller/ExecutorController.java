package com.queryservice.executor.controller;

import com.queryservice.executor.dto.ExecuteRequestDTO;
import com.queryservice.executor.dto.ExecuteResponseDTO;
import com.queryservice.executor.service.ExecutorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/executor")
public class ExecutorController {

    private final ExecutorService executorService;

    public ExecutorController(ExecutorService executorService) {
        this.executorService = executorService;
    }

    @PostMapping("/execute")
    public ResponseEntity<ExecuteResponseDTO> execute(
            @Valid @RequestBody ExecuteRequestDTO request) {

        ExecuteResponseDTO response =
                executorService.execute(request.getSql());

        return ResponseEntity.ok(response);
    }
}