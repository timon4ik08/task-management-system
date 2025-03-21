package com.task_management_system.task_management_system.exception.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "Response object for exception handling. Contains error details such as title, message, and optional stack trace.")
public class ResponseException {
    @Schema(description = "Title of the error", example = "Validation Error")
    private String title;

    @Schema(description = "Detailed error message", example = "The 'email' field must be a valid email address.")
    private String message;

    @Schema(description = "Stack trace of the error (optional). Only included if 'enableStackTrace' is true.", example = "[\"[2025-03-21 14:30:00] - com.example.MyClass.myMethod(MyClass.java:123)\"]")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> stackTrace;

    @JsonIgnore
    private boolean enableStackTrace = false; //ToDo

    public ResponseException(String title, String message) {
        this.title = title;
        this.message = message;
    }

    @Override
    public String toString() {
        if (enableStackTrace) {
            try {
                return new ObjectMapper().writeValueAsString(this);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                this.setStackTrace(null);
                return new ObjectMapper().writeValueAsString(new ResponseException(title, message));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private ResponseException(Builder builder) {
        this.title = builder.title;
        this.message = builder.message;
        this.stackTrace = builder.stackTrace;
    }

    public static class Builder {
        private String title;
        private String message;
        private List<String> stackTrace;

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setStackTrace(StackTraceElement[] stackTrace) {
            List<String> stackTraceList = Arrays.stream(stackTrace).map(StackTraceElement::toString).toList();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[yyyy-MM-dd HH:mm:ss] - ");
            this.stackTrace = stackTraceList.stream()
                    .map(line -> LocalDateTime.now().format(formatter) + " " + line)
                    .collect(Collectors.toList());

            return this;
        }

        public ResponseException build() {
            return new ResponseException(this);
        }
    }
}