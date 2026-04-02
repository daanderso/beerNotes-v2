package org.anderson.beerNotes_v2.dto;

import lombok.Data;

/**
 * DTO for incoming save requests from the frontend.
 */

@Data
public class BeerRequest {
    private String name;
    private String style;
    private String brewery;
    private String origin;
    private String note;
}

