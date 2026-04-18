package org.anderson.beerNotes_v2.controllers;

import org.anderson.beerNotes_v2.dto.BeerRequest;
import org.anderson.beerNotes_v2.entity.Beer;
import org.anderson.beerNotes_v2.services.BeerService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BeerControllerTest {
    @Mock
    private BeerService beerService;

    @InjectMocks
    private BeerController beerController;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    void getAllBeers_returnsListFromService() {
        Beer beer = new Beer();
        when(beerService.getBeerList()).thenReturn(List.of(beer));
        List<Beer> result = beerController.getAllBeers();
        assertEquals(1, result.size());
        assertSame(beer, result.getFirst());
        verify(beerService).getBeerList();
    }

    @Test
    void saveBeer_returnsCreated_whenServiceReturnsBeer() {
        BeerRequest req = new BeerRequest();
        Beer beer = new Beer();
        when(beerService.saveBeer(any())).thenReturn(beer);
        ResponseEntity<?> response = beerController.saveBeer(req);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertSame(beer, response.getBody());
    }

    @Test
    void saveBeer_returnsInternalServerError_whenServiceReturnsNull() {
        when(beerService.saveBeer(any())).thenReturn(null);
        ResponseEntity<?> response = beerController.saveBeer(new BeerRequest());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().toString().contains("Server Error"));
    }

    @Nested
    class DeleteBeerByName {
        @Test
        void returnsInternalServerError_whenServiceReturnsMinusOne() {
            when(beerService.deleteBeer(any())).thenReturn(-1);
            ResponseEntity<?> response = beerController.deleteBeer("test");
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        }
        @Test
        void returnsNotFound_whenServiceReturnsZero() {
            when(beerService.deleteBeer(any())).thenReturn(0);
            ResponseEntity<?> response = beerController.deleteBeer("test");
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
        @Test
        void returnsOk_whenServiceReturnsOne() {
            when(beerService.deleteBeer(any())).thenReturn(1);
            ResponseEntity<?> response = beerController.deleteBeer("test");
            assertEquals(HttpStatus.OK, response.getStatusCode());
        }
        @Test
        void returnsConflict_whenServiceReturnsGreaterThanOne() {
            when(beerService.deleteBeer(any())).thenReturn(2);
            ResponseEntity<?> response = beerController.deleteBeer("test");
            assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
            assertNotNull(response.getBody());
            assertTrue(response.getBody().toString().contains("Conflict"));
        }
    }

    @Nested
    class DeleteBeerById {
        @Test
        void returnsNotFound_whenServiceReturnsFalse() {
            when(beerService.deleteBeerById(any())).thenReturn(false);
            ResponseEntity<?> response = beerController.deleteBeerById(1L);
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
        @Test
        void returnsOk_whenServiceReturnsTrue() {
            when(beerService.deleteBeerById(any())).thenReturn(true);
            ResponseEntity<?> response = beerController.deleteBeerById(1L);
            assertEquals(HttpStatus.OK, response.getStatusCode());
        }
    }

    @Nested
    class UpdateBeerNote {
        @Test
        void returnsInternalServerError_whenServiceReturnsMinusOne() {
            when(beerService.updateBeerNote(any(), any())).thenReturn(-1);
            ResponseEntity<?> response = beerController.updateBeerNote("test", "note");
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        }
        @Test
        void returnsNotFound_whenServiceReturnsZero() {
            when(beerService.updateBeerNote(any(), any())).thenReturn(0);
            ResponseEntity<?> response = beerController.updateBeerNote("test", "note");
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
        @Test
        void returnsOk_whenServiceReturnsOne() {
            when(beerService.updateBeerNote(any(), any())).thenReturn(1);
            ResponseEntity<?> response = beerController.updateBeerNote("test", "note");
            assertEquals(HttpStatus.OK, response.getStatusCode());
        }
        @Test
        void returnsConflict_whenServiceReturnsGreaterThanOne() {
            when(beerService.updateBeerNote(any(), any())).thenReturn(2);
            ResponseEntity<?> response = beerController.updateBeerNote("test", "note");
            assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
            assertNotNull(response.getBody());
            assertTrue(response.getBody().toString().contains("Conflict"));
        }
    }

    @Nested
    class UpdateFullBeer {
        @Test
        void returnsInternalServerError_whenServiceReturnsMinusOne() {
            when(beerService.updateFullBeer(any())).thenReturn(-1);
            ResponseEntity<?> response = beerController.updateFullBeer(new BeerRequest());
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        }
        @Test
        void returnsNotFound_whenServiceReturnsZero() {
            when(beerService.updateFullBeer(any())).thenReturn(0);
            ResponseEntity<?> response = beerController.updateFullBeer(new BeerRequest());
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
        @Test
        void returnsOk_whenServiceReturnsOne() {
            when(beerService.updateFullBeer(any())).thenReturn(1);
            ResponseEntity<?> response = beerController.updateFullBeer(new BeerRequest());
            assertEquals(HttpStatus.OK, response.getStatusCode());
        }
        @Test
        void returnsConflict_whenServiceReturnsGreaterThanOne() {
            when(beerService.updateFullBeer(any())).thenReturn(2);
            ResponseEntity<?> response = beerController.updateFullBeer(new BeerRequest());
            assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
            assertNotNull(response.getBody());
            assertTrue(response.getBody().toString().contains("Conflict"));
        }
    }
}
