package com.lucka.playground.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Play Site Not Found")
public class PlaySiteNotFoundException extends Exception {

    public PlaySiteNotFoundException(Long id) {
        super("No play site was found by id " + id);
    }
}
