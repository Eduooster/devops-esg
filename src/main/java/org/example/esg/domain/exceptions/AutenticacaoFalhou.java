package org.example.esg.domain.exceptions;

public class AutenticacaoFalhou extends RuntimeException {
    public AutenticacaoFalhou(String message) {
        super(message);
    }
}
