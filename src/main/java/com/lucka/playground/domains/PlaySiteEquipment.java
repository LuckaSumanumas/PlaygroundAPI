package com.lucka.playground.domains;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity @Table(name = "PlaySiteEquipment")
public class PlaySiteEquipment {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "play_site_id")
    private PlaySite playSite;
    
    @ManyToOne
    @JoinColumn(name = "equipment_id")
    private Equipment equipment;

    @Override
    public String toString() {
        return this.playSite + ", " + this.equipment;
    }
}
