package com.argo.emspcardaccountservice.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidEMAIDFormatException extends RuntimeException {
    public InvalidEMAIDFormatException(String message) {
        super(message);
    }
}
