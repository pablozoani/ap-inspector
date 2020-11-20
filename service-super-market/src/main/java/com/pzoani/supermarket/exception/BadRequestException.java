package com.pzoani.supermarket.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class BadRequestException extends ResponseStatusException {

    public BadRequestException(String reason) {
        super(HttpStatus.BAD_REQUEST, reason);
    }

    public static BadRequestException exc(String reason) {
        return new BadRequestException(reason);
    }
}
