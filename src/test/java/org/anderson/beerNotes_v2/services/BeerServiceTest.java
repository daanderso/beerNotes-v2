package org.anderson.beerNotes_v2.services;

// src/test/java/org/anderson/beerNotes_v2/services/BeerServiceTest.java

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
        assertEquals(beer, beerList.get(0));
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
        // Arrange
        when(beerRepo.save(any(Beer.class))).thenReturn(beer);

        // Act
        boolean result = beerService.saveBeer(beer);

        // Assert
        assertTrue(result);
        verify(beerRepo, times(1)).save(any(Beer.class));
    }



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

}