package com.felipeheld.grades.api.response.error;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiError {
    
    private LocalDateTime timestamp;
    private String message;
}
