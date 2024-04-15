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
import lombok.Setter;
import lombok.Getter;

@Getter @Setter
@Entity @Table(name = "Equipment")
public class Equipment {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @Column(name = "name", nullable = false)
	private String name;

    @Column(name = "code", nullable = false)
	private String code;

    @OneToMany(mappedBy = "equipment", cascade = CascadeType.ALL, orphanRemoval = true)
    @Column(name = "playSites", nullable = false)
	private List<PlaySiteEquipment> playSites = new ArrayList<>();

    @Override
    public String toString() {
        return this.id.toString() + ", " + this.name;
    }
}
