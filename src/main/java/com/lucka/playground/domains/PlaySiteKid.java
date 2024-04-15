package com.lucka.playground.domains;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity @Table(name = "PlaySiteKid")
public class PlaySiteKid {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "play_site_id")
    private PlaySite playSite;

    @OneToOne
    @JoinColumn(name = "kid_id")
    private Kid kid;

    @Column(name = "isInPlaySite", nullable = false)
    private Boolean isInPlaySite = false;

    @Column(name = "acceptsWaiting", nullable = false)
	private Boolean acceptsWaiting = true;

    @Column(name = "ticketNumber", nullable = false)
	private String ticketNumber;

}
