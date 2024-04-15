package com.lucka.playground.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Play Site Already Exist")
public class PlaySiteAlreadyExistException extends Exception {
    public PlaySiteAlreadyExistException(String code) {
        super("Play site with code " + code + " already exist");
    }
}
