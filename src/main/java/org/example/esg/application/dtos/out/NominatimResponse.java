package org.example.esg.application.dtos.out;

import com.fasterxml.jackson.annotation.JsonProperty;

public record NominatimResponse(
        Double lat,
        Double lon,
        @JsonProperty("display_name") String displayName
) {}