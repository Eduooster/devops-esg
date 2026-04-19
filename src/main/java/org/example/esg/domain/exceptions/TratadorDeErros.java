package org.example.esg.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class TratadorDeErros {

    public TratadorDeErros() {}

    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<Map<String, String>> ResourceNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(this.detalhar(ex));
    }
    @ExceptionHandler({EmailJaCadastrado.class})
    public ResponseEntity<Map<String, String>> EmailJaCadastrado(EmailJaCadastrado ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(this.detalhar(ex));
    }

    @ExceptionHandler({AutenticacaoFalhou.class})
    public ResponseEntity<Map<String, String>> AutenticacaoFalhou(AutenticacaoFalhou ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(this.detalhar(ex));
    }
    @ExceptionHandler({NominatimFailSearch.class})
    public ResponseEntity<Map<String, String>> NominatimFailSearch(NominatimFailSearch ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(this.detalhar(ex));
    }

    @ExceptionHandler({CapacidadePontoAtingida.class})
    public ResponseEntity<Map<String, String>> CapacidadePontoAtingida(NominatimFailSearch ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(this.detalhar(ex));
    }







    public Map<String, String> detalhar(Exception ex) {
        Map<String, String> map = new HashMap();
        map.put("erro", ex.getClass().getSimpleName());
        map.put("mensagem", ex.getMessage());
        return map;
    }
}
