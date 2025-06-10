package com.argo.emspcardaccountservice.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String resourceName, String fieldName, String fieldValue) {
        super(String.format("A %s with %s '%s' already exists.", resourceName, fieldName, fieldValue));
    }
}
