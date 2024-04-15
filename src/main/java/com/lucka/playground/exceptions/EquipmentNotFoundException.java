package com.lucka.playground.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Equipment Not Found")
public class EquipmentNotFoundException extends Exception {

    public EquipmentNotFoundException(Long id) {
        super("No equipment was found by id " + id);
    }

}
