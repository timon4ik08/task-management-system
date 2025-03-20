package com.task_management_system.task_management_system.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;

import java.util.Map;

public class ResponseHeader {
    public  static HttpHeaders headers = new HttpHeaders(MultiValueMap.fromSingleValue(Map.of(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)));
}