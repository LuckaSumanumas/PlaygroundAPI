package com.lucka.playground.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.lucka.playground.domains.PlaySite;

@Repository
public interface PlaySitesRepository extends CrudRepository<PlaySite, Long> {
    Optional<PlaySite> findByCode(String code);
}
