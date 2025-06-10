package com.argo.emspcardaccountservice.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidStatusTransitionException extends RuntimeException {
    public InvalidStatusTransitionException(String entityType, String currentStatus, String newStatus) {
        super(String.format("Invalid status transition for %s from '%s' to '%s'.", entityType, currentStatus, newStatus));
    }
}
