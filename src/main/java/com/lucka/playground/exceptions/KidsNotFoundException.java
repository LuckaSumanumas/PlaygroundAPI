package com.lucka.playground.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Kids Not Found")
public class KidsNotFoundException extends Exception {
    @Override
    public String getMessage() {
        return "No kids data were found";
    }
}
