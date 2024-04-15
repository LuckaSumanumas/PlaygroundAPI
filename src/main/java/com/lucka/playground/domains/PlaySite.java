package com.lucka.playground.domains;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity @Table(name = "PlaySite")
public class PlaySite {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @Column(name = "playground", nullable = false)
	private String playground = "Playground1";

    @Column(name = "name", nullable = false)
	private String name;

    @Column(name = "code", nullable = false)
	private String code;

    @OneToMany(mappedBy = "playSite", cascade = CascadeType.ALL, orphanRemoval = true)
    @Column(name = "equipments", nullable = false)
	private List<PlaySiteEquipment> equipments = new ArrayList<>();

    @OneToMany(mappedBy = "playSite", cascade = CascadeType.ALL, orphanRemoval = true)
    @Column(name = "kids", nullable = true)
	private List<PlaySiteKid> kids = new ArrayList<>();

    @Column(name = "maxNmberOfKids", nullable = false)
	private Integer maxNumberOfKids = 1;

    @Column(name = "thisDayVisitors", nullable = false)
	private Integer thisDayVisitors = 0;

    @Override
    public String toString() {
        return this.id.toString() + ", " + this.name;
    }
}
