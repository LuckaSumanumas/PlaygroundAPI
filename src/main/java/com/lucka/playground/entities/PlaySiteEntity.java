package com.lucka.playground.entities;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter 
public class PlaySiteEntity {

    private Long id;
	private String playground = "Playground1";
	private String name;
    private String code;
	private List<EquipmentEntity> equipments = new ArrayList<>();
	private List<KidEntity> kidsOnPlaySide = new ArrayList<>();
    private List<KidEntity> kidsOnQueue = new ArrayList<>();
	private Integer maxNumberOfKids;
    private Integer thisDayVisitors;

    @Override
    public String toString() {
        return this.id.toString() + ", " + this.name;
    }
}
