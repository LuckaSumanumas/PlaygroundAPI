package com.lucka.playground.entities;

import lombok.Setter;
import lombok.Getter;

@Getter @Setter
public class EquipmentEntity {
  
    private Long id;
	private String name;
	private String code;

    @Override
    public String toString() {
        return this.id != null ? this.id.toString() : "" + ", " + this.name;
    }
}
