package com.lucka.playground.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Equipments Not Found")
public class EquipmentsNotFoundException extends Exception {
    @Override
    public String getMessage() {
        return "No equipments were found";
    }
}
