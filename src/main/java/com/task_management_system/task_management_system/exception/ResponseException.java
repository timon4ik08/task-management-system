package com.task_management_system.task_management_system.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class ResponseException {
    private String title;
    private String message;

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