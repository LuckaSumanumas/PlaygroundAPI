package com.lucka.playground.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.lucka.playground.domains.Equipment;

@Repository
public interface EquipmentsRepository extends CrudRepository<Equipment, Long> {
    Optional<Equipment> findByCode(String code);
    
}
