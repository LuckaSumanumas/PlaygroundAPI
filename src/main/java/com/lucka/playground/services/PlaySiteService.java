package com.lucka.playground.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lucka.playground.MappingHelper;
import com.lucka.playground.domains.Equipment;
import com.lucka.playground.domains.Kid;
import com.lucka.playground.domains.PlaySiteKid;
import com.lucka.playground.domains.PlaySite;
import com.lucka.playground.domains.PlaySiteEquipment;
import com.lucka.playground.entities.EquipmentEntity;
import com.lucka.playground.entities.KidEntity;
import com.lucka.playground.entities.PlaySiteEntity;
import com.lucka.playground.entities.PlaygroundEntity;
import com.lucka.playground.exceptions.EquipmentNotFoundException;
import com.lucka.playground.exceptions.EquipmentsNotFoundException;
import com.lucka.playground.exceptions.KidDataException;
import com.lucka.playground.exceptions.KidsNotFoundException;
import com.lucka.playground.exceptions.PlaySiteAlreadyExistException;
import com.lucka.playground.exceptions.PlaySiteNotFoundException;
import com.lucka.playground.exceptions.PlaySitesNotFoundException;
import com.lucka.playground.repositories.EquipmentsRepository;
import com.lucka.playground.repositories.KidsRepository;
import com.lucka.playground.repositories.PlaySiteEquipmentsRepository;
import com.lucka.playground.repositories.PlaySiteKidsRepository;
import com.lucka.playground.repositories.PlaySitesRepository;

/**
 * @author Mindaugas Lucka
 */
@Service
public class PlaySiteService {

    @Autowired
	EquipmentsRepository equipmentsRepository;

    @Autowired
	PlaySitesRepository playSitesRepository;

    @Autowired
	PlaySiteEquipmentsRepository playSiteEquipmentsRepository;

    @Autowired
    KidsRepository kidsRepository;

    @Autowired
    PlaySiteKidsRepository playSiteKidsRepository;

    private MappingHelper mappingHelper = MappingHelper.getInstance();

    public List<PlaySiteEntity> retrievePlaySites() throws PlaySitesNotFoundException {
        List<PlaySiteEntity> playSites = new ArrayList<>();

        playSitesRepository.findAll().forEach(playSite -> {
            playSites.add(mappingHelper.convertPlaySiteToEntity(playSite));
        });

        if(playSites.size() == 0) {
            throw new PlaySitesNotFoundException();
        }

        return playSites;
    }


    public PlaySiteEntity retrievePlaySite(Long id) throws PlaySiteNotFoundException {
        PlaySiteEntity playSiteEntity = null;
        
        Optional<PlaySite> playSiteOpt = playSitesRepository.findById(id);

        if(playSiteOpt.isPresent()) {
            playSiteEntity = mappingHelper.convertPlaySiteToEntity(playSiteOpt.get());
        } else {
            throw new PlaySiteNotFoundException(id);
        }

        return playSiteEntity;
    }

    public PlaySiteEntity amendPlaySite(PlaySiteEntity entity) throws PlaySiteAlreadyExistException {

        Optional<PlaySite> playSiteOpt = playSitesRepository.findByCode(entity.getCode());

        if(playSiteOpt.isPresent()) {
            throw new PlaySiteAlreadyExistException(entity.getCode());
        }

        PlaySite playSite = mappingHelper.convertEntityToPlaySite(entity);

        playSite = playSitesRepository.save(playSite);
        entity.setId(playSite.getId());

        return entity;
    }

    public List<EquipmentEntity> retrievePlaySiteEquiments(Long playSiteId) 
    throws PlaySiteNotFoundException, EquipmentsNotFoundException {
        List<EquipmentEntity> playSiteEquiments = new ArrayList<>();

        Optional<PlaySite> playSiteOpt = playSitesRepository.findById(playSiteId);

        if(playSiteOpt.isPresent()) {
            List<PlaySiteEquipment> equipments = playSiteOpt.get().getEquipments();
            
            if(equipments.size() > 0) {
                equipments.forEach(psEquipment -> {playSiteEquiments.add(
                    mappingHelper.convertEquipmentToEntity(psEquipment.getEquipment()));
                });
            } else {
                throw new EquipmentsNotFoundException();
            }
        } else {
            throw new PlaySiteNotFoundException(playSiteId);
        }

        return playSiteEquiments;
    }

    public PlaySiteEntity updatePlaySiteEquipments(
        Long playSiteId, EquipmentEntity entity) 
            throws PlaySiteNotFoundException, EquipmentNotFoundException {
        
        PlaySite playSite = getPlaySite(playSiteId);
        Equipment equipment = getEquipment(entity.getId());

        PlaySiteEquipment playSiteEquipment = new PlaySiteEquipment();
        playSiteEquipment.setEquipment(equipment);
        playSiteEquipment.setPlaySite(playSite);
        playSiteEquipment = playSiteEquipmentsRepository.save(playSiteEquipment);
        PlaySiteEntity playSiteEntity = mappingHelper.convertPlaySiteToEntity(playSite);

        return playSiteEntity;
    }

    private Equipment getEquipment(Long equipmentId) throws EquipmentNotFoundException {
        Equipment equipment = null;

        Optional<Equipment> equipmentOpt = 
        equipmentsRepository.findById(equipmentId);

        if(equipmentOpt.isPresent()) {
            equipment = equipmentOpt.get();
        } else {
            throw new EquipmentNotFoundException(equipmentId);
        }

        return equipment;
    }

    private PlaySite getPlaySite(Long playSiteId) throws PlaySiteNotFoundException {

        Optional<PlaySite> playSiteOpt = 
            playSitesRepository.findById(playSiteId);

        if(!playSiteOpt.isPresent()) {
            throw new PlaySiteNotFoundException(playSiteId);
        }

        return playSiteOpt.get();
    }

    private Kid storeKidDetails(KidEntity kidEntity) {
        
        Kid kid = mappingHelper.convertEntityToKidData(kidEntity);
        Optional<Kid> kidOpt = kidsRepository.findByName(kidEntity.getName());

        if(kidOpt.isPresent()) {
            kid.setId(kidOpt.get().getId());
        }

        kid = kidsRepository.save(kid);

        return kid;
    }

    public PlaySiteEntity addKidToPlaySite(
        KidEntity kidEntity, Long playSiteId) throws PlaySiteNotFoundException, KidDataException {
       
        PlaySite playSite = getPlaySite(playSiteId);
        Kid kid = storeKidDetails(kidEntity);

        if(isKidOnOtherPlaySite(playSite, kid)) {
            throw new KidDataException("Kid " + kid.getName() + " is on other play site");
        } else if(isKidOnPlaySite(playSite.getKids(), kid)) {
            throw new KidDataException("Kid " + kid.getName() + " is already on the play site");
        } else {
            List<PlaySiteKid> playSiteKids = playSiteKidsRepository.findByPlaySite(playSite);
            PlaySiteKid playSiteKid = retrievePlaySiteKid(kid);
            playSiteKid.setPlaySite(playSite);
            playSiteKid.setTicketNumber(kidEntity.getTicketNumber());
            playSiteKid.setAcceptsWaiting(kidEntity.getAcceptsWaiting());

            if(playSiteKids.size() >= playSite.getMaxNumberOfKids() 
                && !playSiteKid.getAcceptsWaiting()) {
                throw new KidDataException("The play site is already engaged, kid " + 
                    kid.getName() + " not accepted waiting in a queue");
            } else {
                playSiteKid = mappingHelper.setIsInPlaySite(playSiteKid);
                if(playSiteKid.getIsInPlaySite() == null) {
                    throw new KidDataException("Kid with id " + kid.getId() + " not accept waiting in the queue");
                }

                if(playSiteKidsRepository.findByTicketNumber(kidEntity.getTicketNumber()).isPresent()) {
                    throw new KidDataException("Kid with ticket number " + kidEntity.getTicketNumber() + " already exist");
                }

                playSiteKid = playSiteKidsRepository.save(playSiteKid);
                playSite.getKids().add(playSiteKid);

                if(playSiteKid.getIsInPlaySite()) {
                    playSite.setThisDayVisitors(playSite.getThisDayVisitors() + 1);
                    playSitesRepository.save(playSite);
                }
            }
    }

        PlaySiteEntity playSiteEntity = mappingHelper.convertPlaySiteToEntity(playSite);

        return playSiteEntity;
    }

    private Boolean isKidOnOtherPlaySite(PlaySite playSite, Kid kid) {

        Optional<PlaySiteKid> playSiteKidOpt = playSiteKidsRepository.findByKid(kid);

        if(playSiteKidOpt.isPresent()) {
            PlaySiteKid playSiteKid =  playSiteKidOpt.get();
            return playSiteKid.getPlaySite().getId() != playSite.getId() && 
                playSiteKid.getKid().getId() == kid.getId();
        } else {
           return false; 
        }
    }

    private Boolean isKidOnPlaySite(List<PlaySiteKid> kids, Kid kid) {
        return kids.stream().filter((k) -> k.getKid().getName().equals(kid.getName()))
            .collect(Collectors.toList()).size() > 0;
    }

    public List<EquipmentEntity> retrieveEquipments() throws EquipmentsNotFoundException {
        List<EquipmentEntity> equipments = new ArrayList<>();

        equipmentsRepository.findAll().forEach(equipment -> {
            equipments.add(mappingHelper.convertEquipmentToEntity(equipment));
        });

        if(equipments.size() == 0) {
            throw new EquipmentsNotFoundException();
        }

        return equipments;
    }

    public List<KidEntity> retrieveKids() throws KidsNotFoundException {
        List<KidEntity> kids = new ArrayList<>();

        kidsRepository.findAll().forEach(kid -> {
            PlaySiteKid playSiteKid = retrievePlaySiteKid(kid);
            kids.add(mappingHelper.convertKidDataToEntity(playSiteKid));
        });

        if(kids.size() == 0) {
            throw new KidsNotFoundException();
        }

        return kids;
    }

    private PlaySiteKid retrievePlaySiteKid(Kid kid) {
        Optional<PlaySiteKid> playSiteKidOpt = playSiteKidsRepository.findByKid(kid);
        PlaySiteKid playSiteKid = new PlaySiteKid();

        if(playSiteKidOpt.isPresent()) {
            playSiteKid = playSiteKidOpt.get();
        } else {
            playSiteKid.setKid(kid);
        }

        return playSiteKid;
    } 

    public KidEntity updateKidData(KidEntity kidEntity) throws KidDataException {
        
        Optional<Kid> kidOpt = kidsRepository.findById(kidEntity.getId());

        if(kidOpt.isPresent()) {
            Kid kid = kidOpt.get();
            kid = mappingHelper.convertEntityToKidData(kidEntity);
            kid = kidsRepository.save(kid);
            PlaySiteKid playSiteKid = retrievePlaySiteKid(kid);
            if(playSiteKid.getId() != null) {
                playSiteKid.setTicketNumber(kidEntity.getTicketNumber());
                playSiteKid.setAcceptsWaiting(kidEntity.getAcceptsWaiting());
                kidEntity = mappingHelper.convertKidDataToEntity(playSiteKid);
            }
        } else {
            throw new KidDataException("Kid with id " + kidEntity.getId() + " data was not found");
        }

        return kidEntity;
    }

    public PlaySiteEntity withdrawKidFromPlaySite(Long playSiteId, Long kidId) 
        throws PlaySiteNotFoundException, KidDataException {

        Optional<PlaySite> playSiteOpt = playSitesRepository.findById(playSiteId);

        if(!playSiteOpt.isPresent()) {
            throw new PlaySiteNotFoundException(playSiteId);
        }

        Optional<Kid> kidOpt = kidsRepository.findById(kidId);

        if(!kidOpt.isPresent()) {
            throw new KidDataException("Kid with id " + kidId + " data do not exist");
        }

        withdrawAndUpdatePlaysiteKid(playSiteOpt.get(), kidOpt.get());

        return mappingHelper.convertPlaySiteToEntity(playSitesRepository.findById(playSiteId).get());
    }

    private void withdrawAndUpdatePlaysiteKid(PlaySite playSite, Kid kid) {
        List<PlaySiteKid> playSiteKids = playSiteKidsRepository.findByPlaySite(playSite);

        for(PlaySiteKid playSiteKid : playSiteKids) {
            if(playSiteKid.getKid().getId() == kid.getId()) {
                Boolean isNotInQueue = playSiteKid.getIsInPlaySite();
                playSiteKidsRepository.delete(playSiteKid);

                if(isNotInQueue) {
                    Optional<PlaySiteKid> firstInQueueOpt = 
                        playSiteKids.stream().filter(p -> !p.getIsInPlaySite()).findFirst();
                    if(firstInQueueOpt.isPresent()) {
                        PlaySiteKid firstInQueue = firstInQueueOpt.get();
                        firstInQueue.setIsInPlaySite(true);
                        playSiteKidsRepository.save(firstInQueue);
                        playSite.setThisDayVisitors(playSite.getThisDayVisitors() + 1);
                        playSitesRepository.save(playSite);
                    }
                }
                break;
            }
        }
    }

    public List<PlaygroundEntity> retrievePlaygrounds() {
        List<PlaygroundEntity> playgrounds = new ArrayList<>();

        PlaygroundEntity playground = new PlaygroundEntity();

        Integer numberPlaySites = 0;
        Integer totalVisitors = 0;
        Integer currentUtilisation = 0;

        for(PlaySite playSite : playSitesRepository.findAll()) {
            numberPlaySites++;
            totalVisitors += playSite.getThisDayVisitors();

            Integer kidsOnPlaySite = 0; 

            for(PlaySiteKid kid : playSite.getKids()) {
                if(kid.getIsInPlaySite()) {
                    kidsOnPlaySite++;
                }
            }

            currentUtilisation += kidsOnPlaySite*100/playSite.getMaxNumberOfKids();
        }

        playground.setNumberPlaySites(numberPlaySites);
        playground.setTotalVisitors(totalVisitors);
        if(numberPlaySites != 0) {
            playground.setCurrentUtilisation(currentUtilisation/numberPlaySites);
        }

        playgrounds.add(playground);

        return playgrounds;
    }

}
