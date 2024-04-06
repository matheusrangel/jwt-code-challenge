package com.matheusrangel.jwtcodechallenge.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private LocalDateTime timestamp;
    private Integer status;
    private String path;
    private String trace;
    private String span;
    private List<ErrorDetail> errors;

    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ErrorDetail {

        private String origin;
        private String errorCode;
        private String message;
        private List<String> fields;
    }
}
