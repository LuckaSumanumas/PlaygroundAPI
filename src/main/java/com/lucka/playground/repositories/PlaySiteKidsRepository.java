package com.lucka.playground.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.lucka.playground.domains.Kid;
import com.lucka.playground.domains.PlaySite;
import com.lucka.playground.domains.PlaySiteKid;


@Repository
public interface PlaySiteKidsRepository extends CrudRepository<PlaySiteKid, Long> {
    Optional<PlaySiteKid> findByKid(Kid kid);
    Optional<PlaySiteKid> findByTicketNumber(String ticketNumber);
    List<PlaySiteKid> findByPlaySite(PlaySite playSite);
}
