package com.modzo.jsonxlsconverter;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ResponseStatus(value = INTERNAL_SERVER_ERROR, reason = "Internal server error")
public class InternalServerError extends RuntimeException {
    public InternalServerError(String message) {
        super(message);
    }
}
