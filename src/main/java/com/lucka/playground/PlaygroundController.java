package com.lucka.playground;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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
import com.lucka.playground.services.PlaySiteService;

import io.swagger.v3.oas.annotations.Operation;

/**
 * @author Mindaugas Lucka
 */
@CrossOrigin
@RestController
@RequestMapping("/playground/api")
public class PlaygroundController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlaygroundController.class);

    @Autowired
    private PlaySiteService playSiteService; 

    @GetMapping("/playsites")
    @Operation(
        description = "Finds play sites.",
        tags = { "Play sites" }
    )
    List<PlaySiteEntity> getPlaySites() {
        List<PlaySiteEntity> playSites = new ArrayList<>();
        
        try {
            playSites = playSiteService.retrievePlaySites();
        } catch (PlaySitesNotFoundException e) {
            LOGGER.warn(e.getMessage());
        }

        return playSites;
    }

    @PostMapping("/playsites")
    @Operation(
        description = "Saves play site data.",
        tags = { "Play sites" }
    )
    ResponseEntity<PlaySiteEntity> amendPlaySite(@RequestBody PlaySiteEntity entity) {
        try {
            entity = playSiteService.amendPlaySite(entity);
        } catch (PlaySiteAlreadyExistException e) {
            LOGGER.warn(e.getMessage());
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }

        return new ResponseEntity<>(entity, HttpStatus.CREATED);
    }

    @GetMapping("/playsites/{playSiteId}")
    @Operation(
        description = "Finds a play site by Id.",
        tags = { "Play sites" }
    )
    ResponseEntity<PlaySiteEntity> getPlaySite(@PathVariable Long playSiteId) {
        PlaySiteEntity entity = new PlaySiteEntity();
        
        try {
            entity = playSiteService.retrievePlaySite(playSiteId);
        } catch (PlaySiteNotFoundException e) {
            LOGGER.warn(e.getMessage());
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }

        return new ResponseEntity<>(entity, HttpStatus.OK);
    }

    @GetMapping("/playsites/{playSiteId}/equipments")
    @Operation(
        description = "Finds a play site's equipments.",
        tags = { "Play sites" }
    )
    List<EquipmentEntity> getPlaySiteEquipments(@PathVariable Long playSiteId) {
        List<EquipmentEntity> equipments = new ArrayList<>();

        try {
            equipments = playSiteService.retrievePlaySiteEquiments(playSiteId);
        } catch (PlaySiteNotFoundException e) {
            LOGGER.warn(e.getMessage());
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (EquipmentsNotFoundException e) {
            LOGGER.warn(e.getMessage());
        }

        return equipments;
    }

    @PatchMapping("/playsites/{playSiteId}/equipments")
    @Operation(
        description = "Amends an equipment for a play site.",
        tags = { "Play sites" }
    )
    ResponseEntity<PlaySiteEntity> updatePlaySiteEquipments(
        @PathVariable Long playSiteId, 
        @RequestBody EquipmentEntity entity) {

        PlaySiteEntity playSiteEntity = new PlaySiteEntity();

        try {
            playSiteEntity = playSiteService.updatePlaySiteEquipments(playSiteId, entity);
        } catch (PlaySiteNotFoundException | EquipmentNotFoundException e) {
            LOGGER.warn(e.getMessage());
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
      
        return new ResponseEntity<>(playSiteEntity, HttpStatus.CREATED);
    }

    @PatchMapping("/playsites/{playSiteId}/kids")
    @Operation(
        description = "Adds a kid for a play site.",
        tags = { "Play sites" }
    )
    ResponseEntity<PlaySiteEntity> addKidToPlaySite(
        @RequestBody KidEntity kid, 
        @PathVariable Long playSiteId) {

        PlaySiteEntity playSiteEntity = new PlaySiteEntity();

        try {
            playSiteEntity = playSiteService.addKidToPlaySite(kid, playSiteId);
        } catch (PlaySiteNotFoundException | 
                KidDataException e) {
            LOGGER.warn(e.getMessage());
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
      
        return new ResponseEntity<>(playSiteEntity, HttpStatus.CREATED);
    }

    @DeleteMapping("/playsites/{playSiteId}/kids/{kidId}")
    @Operation(
        description = "Withdraws a kid from a play site.",
        tags = { "Play sites" }
    )
    ResponseEntity<PlaySiteEntity> withdrawKidFromPlaySite(
        @PathVariable Long playSiteId,    
        @PathVariable Long kidId
    ) {
        PlaySiteEntity playSiteEntity = new PlaySiteEntity();
        try {
            playSiteEntity = playSiteService.withdrawKidFromPlaySite(playSiteId, kidId);
        } catch (PlaySiteNotFoundException | KidDataException e) {
            LOGGER.warn(e.getMessage());
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }

        return new ResponseEntity<>(playSiteEntity, HttpStatus.OK);
    }


    @GetMapping("/equipments")
    @Operation(
        description = "Finds equipments.",
        tags = { "Equipments" }
    )
    List<EquipmentEntity> getEquipments() {
        List<EquipmentEntity> equipments = new ArrayList<>();

        try {
            equipments = playSiteService.retrieveEquipments();
        } catch (EquipmentsNotFoundException e) {
            LOGGER.warn(e.getMessage());
        }

        return equipments;
    }

    @GetMapping("/kids")
    @Operation(
        description = "Find data of kids.",
        tags = { "Kids" }
    )
    List<KidEntity> getDataOfKids() {
        List<KidEntity> kids = new ArrayList<>();

        try {
            kids = playSiteService.retrieveKids();
        } catch (KidsNotFoundException e) {
            LOGGER.warn(e.getMessage());
        }

        return kids;
    }

    @PatchMapping("/kids")
    @Operation(
        description = "Update data of a kid.",
        tags = { "Kids" }
    )
    ResponseEntity<KidEntity> updateDataOfKid(
        @RequestBody KidEntity kid) {
        
        try {
            kid = playSiteService.updateKidData(kid);
        } catch (KidDataException e) {
            LOGGER.warn(e.getMessage());
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }

        return new ResponseEntity<>(kid, HttpStatus.CREATED);
    }

    @GetMapping("/playgrounds")
    @Operation(
        description = "Data about playground's play sites.",
        tags = { "Playgrounds" }
    )
    List<PlaygroundEntity> getPlaygrounds() {
        return playSiteService.retrievePlaygrounds();
    }

}
