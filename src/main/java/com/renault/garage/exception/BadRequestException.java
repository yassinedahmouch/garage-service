package com.renault.garage.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class BadRequestException extends ApiException {

    public BadRequestException(String message, String errorCode) {
        super(BAD_REQUEST, message, errorCode);
    }
}
