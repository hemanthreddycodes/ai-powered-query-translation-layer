package com.queryservice.executor.util;

import com.queryservice.executor.dto.ExecuteResponseDTO;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;

@Component
public class ResultSetMapper {

    public ExecuteResponseDTO mapToResponse(
            List<Map<String, Object>> results) {

        int rowCount = results.size();

        return new ExecuteResponseDTO(results, rowCount);
    }
}