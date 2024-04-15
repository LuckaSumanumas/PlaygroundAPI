package com.lucka.playground.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.lucka.playground.domains.PlaySiteEquipment;

@Repository
public interface PlaySiteEquipmentsRepository extends CrudRepository<PlaySiteEquipment, Long> {

}
