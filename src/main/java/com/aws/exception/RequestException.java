package com.aws.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
@Getter
public class RequestException extends RuntimeException {

    private final String message;

    public RequestException(String message) {
        this.message = message;
    }
}
