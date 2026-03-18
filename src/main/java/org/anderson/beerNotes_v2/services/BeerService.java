package org.anderson.beerNotes_v2.services;

import java.util.Collections;
import java.util.List;

import org.anderson.beerNotes_v2.entity.Beer;
import org.anderson.beerNotes_v2.repos.BeerRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class BeerService {

    private final BeerRepository beerRepo; //Using constructor injection w/ Lombok  RequiredArgsConstructor annotation

    public List<Beer> getBeerList() {
        try {
            log.info("Attempting to retrieve all beer notes from DB");
            List <Beer> beerList = beerRepo.findAll();
            log.info("Beer list retrieved successfully");
            return beerList;
        } catch (Exception e) {
            log.error("Error retrieving beer list", e);
              return Collections.emptyList();
        }
    }

    public boolean saveBeer(Beer beer){
        try{
            log.info("Attempting to save new beer note for {}", beer.getName());
            beerRepo.save(beer);
            log.info("Beer {} saved successfully to DB", beer.getName());
            return true;
        }catch(Exception e){
            log.error("Error saving beer. Beer was not saved successfully");
            log.error("EXCEPTION - error saving beer note", e);
            return false;
        }
    }

    /**
     * Delete beers by name and return the affected row count.
     * Returns: >=0 number of deleted rows, -1 on error
     */
    public int deleteBeer(String beerName) {
        try{
            log.info("Attempting to delete beer {}", beerName);
            int affected = beerRepo.deleteByName(beerName);
            if (affected == 0) {
                log.warn("No beer found with name '{}'; nothing deleted", beerName);
            }
            if (affected > 1) {
                log.warn("Deleted {} records for beer name '{}'. Possible non-unique names.", affected, beerName);
            }
            log.info("Delete by name completed for {} ({} rows)", beerName, affected);
            return affected;
        }catch(Exception e){
            log.error("Beer was not deleted successfully. No Beer found in DB by that name", e);
            log.error("EXCEPTION - error deleting beer note", e);
            return -1;
        }
    }

    public boolean deleteBeerbyId(Long id) {
        try{
            log.info("Attempting to delete beer by id: {}", id);
            if (!beerRepo.existsById(id)) {
                log.warn("No beer with id {} exists", id);
                return false;
            }
            beerRepo.deleteById(id);
            log.info("Beer deleted successfully");
            return true;
        }catch(Exception e){
            log.error("Beer was not deleted successfully. Entry may not exist", e);
            log.error("EXCEPTION - error deleting beer note by id", e);
            return false;
        }
    }

    /**
     * Update beer note(s) identified by name. Returns number of rows updated, or -1 on error.
     */
    public int updateBeerNote(String beerName, String note) {
        try{
            log.info("Attempting to update note for beer: {}", beerName);
            int affected = beerRepo.updateBeerNote(beerName, note);
            if (affected == 0) {
                log.warn("No beer found with name '{}'; note not updated", beerName);
            }
            if (affected > 1) {
                log.warn("Updated {} records for beer name '{}'. Possible non-unique names.", affected, beerName);
            }
            log.info("Beer note update completed for {} ({} rows)", beerName, affected);
            return affected;
        }catch(Exception e){
            log.error("Beer note was not updated successfully. Entry may not exist", e);
            log.error("EXCEPTION - error updating beer note update", e);
            return -1;
        }
    }
}
