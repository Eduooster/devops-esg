package org.example.esg.domain.exceptions;

public class MaterialNaoSuportadoException extends RuntimeException {
    public MaterialNaoSuportadoException(String message) {
        super(message);
    }
}
