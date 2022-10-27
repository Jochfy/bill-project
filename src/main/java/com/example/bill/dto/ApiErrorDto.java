package com.example.bill.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.http.HttpStatus;
import java.util.Arrays;
import java.util.List;

@Data
public class ApiErrorDto {
    @Schema(description = "HttpStatus",example = "HttpStatus.INTERNAL_SERVER_ERROR")
    private HttpStatus status;
    @Schema(description = "Message detail the error.",example = "Internal Server Error")
    private String message;
    @Schema(description = "List of errors.")
    private List<String> errors;

    public ApiErrorDto(HttpStatus status, String message, List<String> errors) {
        super();
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    public ApiErrorDto(HttpStatus status, String message, String error) {
        super();
        this.status = status;
        this.message = message;
        errors = Arrays.asList(error);
    }
}
