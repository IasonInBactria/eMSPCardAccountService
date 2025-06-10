package com.argo.emspcardaccountservice.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resourceName, String identifier) {
        super(String.format("%s with identifier '%s' not found.", resourceName, identifier));
    }
}
