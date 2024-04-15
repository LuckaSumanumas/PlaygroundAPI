package com.lucka.playground.entities;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class KidEntity {
    private Long id;
	private String name;
	private Integer age;
	private Boolean acceptsWaiting;
	private String ticketNumber;
}
