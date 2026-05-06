package com.movie.catalog.dto;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Standard structure for all error responses")
public class ErrorResponseDTO {

    @Schema(
        description = "UTC timestamp of the error in ISO-8601 format",
        example = "2026-05-07T10:15:30Z"
    )
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX") 
    private OffsetDateTime timestamp;

    @Schema(description = "HTTP Status Code", example = "404")
    private int status;

    @Schema(description = "Short error definition", example = "Not Found")
    private String error;

    @Schema(description = "Detailed error message", example = "Movie with ID 5 not found")
    private String message;

    @Schema(description = "The URI path where the error occurred", example = "/api/movies/5")
    private String path;
}