package org.example.esg.domain.exceptions;

public class NominatimFailSearch extends RuntimeException {
    public NominatimFailSearch(String message) {
        super(message);
    }
}
