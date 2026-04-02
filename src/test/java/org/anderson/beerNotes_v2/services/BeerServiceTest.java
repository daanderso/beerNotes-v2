package org.anderson.beerNotes_v2.services;

// src/test/java/org/anderson/beerNotes_v2/services/BeerServiceTest.java

import org.anderson.beerNotes_v2.dto.BeerRequest;
import org.anderson.beerNotes_v2.entity.Beer;
import org.anderson.beerNotes_v2.repos.BeerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BeerServiceTest {

    @Mock
    private BeerRepository beerRepo;

    @InjectMocks
    private BeerService beerService;

    @Mock
    private Beer beer;


    @Test
    void testGetBeerList() {
        // Arrange
        when(beerRepo.findAll()).thenReturn(Collections.singletonList(beer));

        // Act
        List<Beer> beerList = beerService.getBeerList();

        // Assert
        assertEquals(1, beerList.size());
        assertEquals(beer, beerList.getFirst());
        verify(beerRepo, times(1)).findAll();
    }

    @Test
    void testGetBeerList_Empty() {
        // Arrange
        when(beerRepo.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Beer> beerList = beerService.getBeerList();

        //Assert
        assertEquals(0, beerList.size());
        verify(beerRepo, times(1)).findAll();
    }

    @Test
    void testSaveBeer() {
        // Arrange – build a request DTO (no id field, so id=0 is impossible)
        BeerRequest request = new BeerRequest();
        request.setName("Test Beer");
        request.setStyle("Lager");
        request.setBrewery("Test Brewery");
        request.setOrigin("USA");
        request.setNote("Tasty");

        Beer savedBeer = new Beer();
        savedBeer.setId(1L);         // simulates the DB assigning a real generated id
        savedBeer.setName("Test Beer");
        when(beerRepo.save(any(Beer.class))).thenReturn(savedBeer);

        // Act
        Beer result = beerService.saveBeer(request);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId()); // real DB-generated id is returned to the caller
        assertEquals("Test Beer", result.getName());
        verify(beerRepo, times(1)).save(any(Beer.class));
    }

    // testSaveBeer_WithIdZero_StillInsertsNewRecord is intentionally removed —
    // BeerRequest has no id field so it is impossible for the frontend to supply one.



    @Test
    void testDeleteBeer() {
        // Arrange
        when(beerRepo.deleteByName("Test Beer")).thenReturn(1);

        // Act
        int result = beerService.deleteBeer("Test Beer");

        // Assert
        assertEquals(1, result);
        verify(beerRepo, times(1)).deleteByName("Test Beer");
    }



    @Test
    void testUpdateBeerNote() {
        // Arrange
        when(beerRepo.updateBeerNote("Test Beer", "New Note")).thenReturn(1);

        // Act
        int result = beerService.updateBeerNote("Test Beer", "New Note");

        // Assert
        assertEquals(1, result);
        verify(beerRepo, times(1)).updateBeerNote("Test Beer", "New Note");
    }

    @Test
    void testDeleteBeer_NotFound() {
        when(beerRepo.deleteByName("Not Found")).thenReturn(0);
        int result = beerService.deleteBeer("Not Found");
        assertEquals(0, result);
        verify(beerRepo, times(1)).deleteByName("Not Found");
    }

    @Test
    void testUpdateBeerNote_NotFound() {
        when(beerRepo.updateBeerNote("Not Found", "Note")).thenReturn(0);
        int result = beerService.updateBeerNote("Not Found", "Note");
        assertEquals(0, result);
        verify(beerRepo, times(1)).updateBeerNote("Not Found", "Note");
    }

    @Test
    void testDeleteBeerById_Success() {
        // Arrange
        when(beerRepo.existsById(1L)).thenReturn(true);

        // Act
        boolean result = beerService.deleteBeerById(1L);

        // Assert
        assertTrue(result);
        verify(beerRepo, times(1)).existsById(1L);
        verify(beerRepo, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteBeerById_NotFound() {
        // Arrange – no beer exists with this id
        when(beerRepo.existsById(99L)).thenReturn(false);

        // Act
        boolean result = beerService.deleteBeerById(99L);

        // Assert
        assertFalse(result);
        verify(beerRepo, times(1)).existsById(99L);
        verify(beerRepo, never()).deleteById(any()); // deleteById should NOT be called
    }

    @Test
    void testDeleteBeerById_Exception() {
        // Arrange – existsById blows up unexpectedly
        when(beerRepo.existsById(1L)).thenThrow(new RuntimeException("DB error"));

        // Act
        boolean result = beerService.deleteBeerById(1L);

        // Assert
        assertFalse(result);                          // service swallows the exception and returns false
        verify(beerRepo, times(1)).existsById(1L);
        verify(beerRepo, never()).deleteById(any()); // deleteById should NOT be reached
    }


    @Test
    void testGetBeerList_Exception() {
        // Arrange – repo blows up
        when(beerRepo.findAll()).thenThrow(new RuntimeException("DB error"));

        // Act
        List<Beer> result = beerService.getBeerList();

        // Assert – service catches the exception and returns an empty list
        assertTrue(result.isEmpty());
        verify(beerRepo, times(1)).findAll();
    }


    @Test
    void testSaveBeer_Exception() {
        // Arrange
        BeerRequest request = new BeerRequest();
        request.setName("Error Beer");
        when(beerRepo.save(any(Beer.class))).thenThrow(new RuntimeException("DB error"));

        // Act
        Beer result = beerService.saveBeer(request);

        // Assert – service catches the exception and returns null
        assertNull(result);
        verify(beerRepo, times(1)).save(any(Beer.class));
    }


    @Test
    void testDeleteBeer_MultipleAffected() {
        // Arrange – two rows share the same name (non-unique)
        when(beerRepo.deleteByName("Duplicate Beer")).thenReturn(2);

        // Act
        int result = beerService.deleteBeer("Duplicate Beer");

        // Assert – service warns but still returns the affected count
        assertEquals(2, result);
        verify(beerRepo, times(1)).deleteByName("Duplicate Beer");
    }

    @Test
    void testDeleteBeer_Exception() {
        // Arrange – repo blows up
        when(beerRepo.deleteByName("Error Beer")).thenThrow(new RuntimeException("DB error"));

        // Act
        int result = beerService.deleteBeer("Error Beer");

        // Assert – service catches the exception and returns -1
        assertEquals(-1, result);
        verify(beerRepo, times(1)).deleteByName("Error Beer");
    }


    @Test
    void testUpdateBeerNote_MultipleAffected() {
        // Arrange – two rows share the same name (non-unique)
        when(beerRepo.updateBeerNote("Duplicate Beer", "Note")).thenReturn(2);

        // Act
        int result = beerService.updateBeerNote("Duplicate Beer", "Note");

        // Assert – service warns but still returns the affected count
        assertEquals(2, result);
        verify(beerRepo, times(1)).updateBeerNote("Duplicate Beer", "Note");
    }

    @Test
    void testUpdateBeerNote_Exception() {
        // Arrange – repo blows up
        when(beerRepo.updateBeerNote("Error Beer", "Note")).thenThrow(new RuntimeException("DB error"));

        // Act
        int result = beerService.updateBeerNote("Error Beer", "Note");

        // Assert – service catches the exception and returns -1
        assertEquals(-1, result);
        verify(beerRepo, times(1)).updateBeerNote("Error Beer", "Note");
    }

}