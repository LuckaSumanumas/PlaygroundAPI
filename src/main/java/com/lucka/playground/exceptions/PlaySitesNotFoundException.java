package com.lucka.playground.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Play Sites Not Found")
public class PlaySitesNotFoundException extends Exception {
    @Override
    public String getMessage() {
        return "No play sites were found";
    }
}
