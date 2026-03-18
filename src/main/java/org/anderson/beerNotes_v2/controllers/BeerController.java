package org.anderson.beerNotes_v2.controllers;

import java.util.List;

import org.anderson.beerNotes_v2.entity.Beer;
import org.anderson.beerNotes_v2.services.BeerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/beernotes")
public class BeerController {

    private static final String BEER_LIST_RESPONSE_200 = " GET Operation completed Successfully";
    private static final String BEER_DELETED_RESPONSE_200 = "DELETE operation completed Successfully";
    private static final String BEER_UPDATED_RESPONSE_200 = "UPDATE operation completed Successfully";
    private static final String BEER_SAVED_RESPONSE_201 = "SAVE Operation completed Successfully. Beer saved to DB";
    private static final String INVALID_REQUEST_RESPONSE_400 = "Invalid request possibly due to malformed syntax.";
    private static final String BEER_NOT_FOUND_RESPONSE_404 = "Beer not found. Object not found in DB.";
    private static final String COULD_NOT_FULFILL_REQUEST_RESPONSE_500 = "Server Error. Server encountered an unexpected condition and couldn't fulfill request.";
    private static final String BEER_CONFLICT_RESPONSE_409 = "Conflict: multiple records affected — non-unique beer name.";

    private final BeerService beerService;   //Using constructor injection w/ RequiredArgsConstructor annotation

    @Operation(summary = "Get a list of all beers")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = BEER_LIST_RESPONSE_200, 
        content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Beer.class)) }), 
        @ApiResponse(responseCode = "400", description = INVALID_REQUEST_RESPONSE_400, 
        content = @Content), 
        @ApiResponse(responseCode = "500", description = COULD_NOT_FULFILL_REQUEST_RESPONSE_500, 
        content = @Content) })
    @GetMapping("/beerlist")
    public List<Beer> getAllBeers() {
        log.info("Retrieving all beers from DB");
        return beerService.getBeerList();
    } 

    @Operation(summary = "Save a new beer note")
    @ApiResponses(value = { 
    @ApiResponse(responseCode = "201", description = BEER_SAVED_RESPONSE_201, 
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Beer.class)) }), 
        @ApiResponse(responseCode = "400", description = INVALID_REQUEST_RESPONSE_400, 
            content = @Content), 
        @ApiResponse(responseCode = "500", description = COULD_NOT_FULFILL_REQUEST_RESPONSE_500, 
            content = @Content) })
    @PostMapping("/saveBeer")
    public ResponseEntity<?> saveBeer(@RequestBody Beer beer) {
        log.info("Saving new beer: {}", beer);
        boolean saved = beerService.saveBeer(beer);
        if (saved) {
            return ResponseEntity.status(HttpStatus.CREATED).body(beer);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(COULD_NOT_FULFILL_REQUEST_RESPONSE_500);
    }

    @Operation(summary = "Delete a beer by name")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = BEER_DELETED_RESPONSE_200, 
            content = @Content), 
        @ApiResponse(responseCode = "400", description = INVALID_REQUEST_RESPONSE_400, 
            content = @Content), 
        @ApiResponse(responseCode = "404", description =  BEER_NOT_FOUND_RESPONSE_404, 
            content = @Content), 
        @ApiResponse(responseCode = "409", description = BEER_CONFLICT_RESPONSE_409,
            content = @Content),
        @ApiResponse(responseCode = "500", description = COULD_NOT_FULFILL_REQUEST_RESPONSE_500,
            content = @Content) })
    @DeleteMapping("/deleteBeer/{beerName}")
    public ResponseEntity<?> deleteBeer(@PathVariable String beerName) {
        int result = beerService.deleteBeer(beerName);
        if (result == -1) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(COULD_NOT_FULFILL_REQUEST_RESPONSE_500);
        }
        if (result == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BEER_NOT_FOUND_RESPONSE_404);
        }
        if (result == 1) {
            return ResponseEntity.ok(BEER_DELETED_RESPONSE_200);
        }
        // result > 1
        return ResponseEntity.status(HttpStatus.CONFLICT).body(BEER_CONFLICT_RESPONSE_409 + " Affected: " + result);
    }

    @Operation(summary = "Update an existing beer note")
@ApiResponses(value = { 
    @ApiResponse(responseCode = "200", description = BEER_UPDATED_RESPONSE_200 , 
        content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Beer.class)) }), 
    @ApiResponse(responseCode = "400", description = INVALID_REQUEST_RESPONSE_400, 
        content = @Content), 
    @ApiResponse(responseCode = "404", description = BEER_NOT_FOUND_RESPONSE_404, 
        content = @Content), 
    @ApiResponse(responseCode = "409", description = BEER_CONFLICT_RESPONSE_409,
        content = @Content),
    @ApiResponse(responseCode = "500", description = COULD_NOT_FULFILL_REQUEST_RESPONSE_500,
        content = @Content) })
    @PutMapping("/updateBeerNote/{beerName}")
    public ResponseEntity<?> updateBeerNote(@PathVariable String beerName, @RequestBody String note){
        log.info("Updating note for beer: {}", beerName);
        int result = beerService.updateBeerNote(beerName, note);
        if (result == -1) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(COULD_NOT_FULFILL_REQUEST_RESPONSE_500);
        }
        if (result == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BEER_NOT_FOUND_RESPONSE_404);
        }
        if (result == 1) {
            return ResponseEntity.ok(BEER_UPDATED_RESPONSE_200);
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(BEER_CONFLICT_RESPONSE_409 + " Updated: " + result);
    }
}
