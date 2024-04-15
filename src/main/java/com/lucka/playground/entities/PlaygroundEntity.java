package com.lucka.playground.entities;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter 
public class PlaygroundEntity {
    private String code = "Playground1";
    private Integer numberPlaySites = 0;
    private Integer currentUtilisation = 0;
    private Integer totalVisitors = 0;
}
