package com.lucka.playground.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.lucka.playground.domains.Kid;

@Repository
public interface KidsRepository extends CrudRepository<Kid, Long> {
    Optional<Kid> findByName(String name);
}
