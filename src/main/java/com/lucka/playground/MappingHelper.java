package com.lucka.playground;

import com.lucka.playground.domains.Equipment;
import com.lucka.playground.domains.Kid;
import com.lucka.playground.domains.PlaySite;
import com.lucka.playground.domains.PlaySiteKid;
import com.lucka.playground.entities.EquipmentEntity;
import com.lucka.playground.entities.KidEntity;
import com.lucka.playground.entities.PlaySiteEntity;
import com.lucka.playground.exceptions.KidDataException;

/**
 * @author Mindaugas Lucka
 */
public class MappingHelper {

    private static MappingHelper helperInstance;

    private MappingHelper() {        
    }

    public static MappingHelper getInstance() {
        if(helperInstance == null) {
            helperInstance = new MappingHelper();
        }
        
        return helperInstance;
    }

    public EquipmentEntity convertEquipmentToEntity(Equipment equipment) {
        EquipmentEntity entity = new EquipmentEntity();

        entity.setId(equipment.getId());
        entity.setName(equipment.getName());
        entity.setCode(equipment.getCode());

        return entity;
    }

    public PlaySiteEntity convertPlaySiteToEntity(PlaySite playSite) {
        PlaySiteEntity entity = new PlaySiteEntity();

        entity.setId(playSite.getId());
        entity.setName(playSite.getName());
        entity.setCode(playSite.getCode());
        entity.setMaxNumberOfKids(playSite.getMaxNumberOfKids());
        entity.setPlayground(playSite.getPlayground());

        playSite.getEquipments().forEach(equipment -> {
            entity.getEquipments().add(convertEquipmentToEntity(equipment.getEquipment()));
        });

        for(PlaySiteKid playSiteKid : playSite.getKids()) {
            KidEntity kidEntity = convertKidDataToEntity(playSiteKid);

            if(playSiteKid.getIsInPlaySite()) {
                entity.getKidsOnPlaySide().add(kidEntity); 
            } else {
                entity.getKidsOnQueue().add(kidEntity);
            }
        }

        return entity;
    }

    public PlaySite convertEntityToPlaySite(PlaySiteEntity entity) {
        PlaySite playSite = new PlaySite();

        playSite.setId(entity.getId() != null ? 
            entity.getId() : playSite.getId());
        playSite.setPlayground(entity.getPlayground() != null ? 
            entity.getPlayground() : playSite.getPlayground());
        playSite.setName(entity.getName() != null ? 
            entity.getName() : playSite.getName());
        playSite.setCode(entity.getCode() != null ? 
            entity.getCode() : playSite.getCode());
        playSite.setMaxNumberOfKids(entity.getMaxNumberOfKids() != null ? 
            entity.getMaxNumberOfKids() : playSite.getMaxNumberOfKids());

        return playSite;
    }

    public Kid convertEntityToKidData(KidEntity kidEntity) {
        Kid kid = new Kid();

        kid.setId(kidEntity.getId() != null ? 
            kidEntity.getId() : kid.getId());
        kid.setAge(kidEntity.getAge() != null ? 
            kidEntity.getAge() : kid.getAge());
        kid.setName(kidEntity.getName() != null ? 
            kidEntity.getName() : kid.getName());

        return kid;
    }

    public KidEntity convertKidDataToEntity(PlaySiteKid playSiteKid) {
        KidEntity kidEntity = new KidEntity();

        Kid kid = playSiteKid.getKid();

        kidEntity.setId(kid.getId());
        kidEntity.setAge(kid.getAge());
        kidEntity.setName(kid.getName());
        kidEntity.setAcceptsWaiting(playSiteKid.getAcceptsWaiting());
        kidEntity.setTicketNumber(playSiteKid.getTicketNumber());

        return kidEntity;
    }

    public PlaySiteKid setIsInPlaySite(
        PlaySiteKid playSiteKid) throws KidDataException {
        
        Integer maxKids = playSiteKid.getPlaySite().getMaxNumberOfKids();
        Integer current = playSiteKid.getPlaySite().getKids().size();

        if(current < maxKids) {
            playSiteKid.setIsInPlaySite(true);
        } else if(playSiteKid.getAcceptsWaiting()) {
            playSiteKid.setIsInPlaySite(false);
        } else {
            playSiteKid.setIsInPlaySite(null);
        }

        return playSiteKid;
    }

}
