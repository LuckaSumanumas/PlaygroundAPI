package com.lucka.playground.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Kid Data Warning")
public class KidDataException extends Exception {
    public KidDataException(String errorMsg) {
        super(errorMsg);
    }
}
