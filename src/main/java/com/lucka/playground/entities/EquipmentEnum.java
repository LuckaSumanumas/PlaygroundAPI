package com.lucka.playground.entities;

public enum EquipmentEnum {
    DOUBLE_SWINGS("Double Swings"),
    SLIDE("Slide"),
    BALL_PIT("Ball Pit"),
    CAROUSEL("Carousel");
    
    private String name;

    private EquipmentEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
