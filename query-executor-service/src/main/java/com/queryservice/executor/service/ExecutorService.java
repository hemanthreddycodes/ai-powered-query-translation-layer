package com.queryservice.executor.service;

import com.queryservice.executor.dto.ExecuteResponseDTO;
import com.queryservice.executor.repository.QueryRepository;
import com.queryservice.executor.util.ResultSetMapper;
import com.queryservice.executor.validator.SQLValidator;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class ExecutorService {

    private final SQLValidator sqlValidator;
    private final QueryRepository queryRepository;
    private final ResultSetMapper resultSetMapper;

    public ExecutorService(SQLValidator sqlValidator,
                           QueryRepository queryRepository,
                           ResultSetMapper resultSetMapper) {
        this.sqlValidator = sqlValidator;
        this.queryRepository = queryRepository;
        this.resultSetMapper = resultSetMapper;
    }

    public ExecuteResponseDTO execute(String sql) {

        //Validate SQL is safe
        sqlValidator.validate(sql);

        //Run SQL on database
        List<Map<String, Object>> results =
                queryRepository.executeQuery(sql);

        //Map results to response
        return resultSetMapper.mapToResponse(results);
    }
}