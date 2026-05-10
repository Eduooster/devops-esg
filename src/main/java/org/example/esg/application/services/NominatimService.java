package org.example.esg.application.services;

import org.example.esg.application.dtos.out.NominatimResponse;
import org.example.esg.domain.entities.Endereco;
import org.example.esg.infra.external.NominatimClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class NominatimService {

    @Autowired
    NominatimClient nominatimClient;

    public NominatimResponse buscarLatLng(Endereco endereco) {
        try {

            NominatimResponse response = nominatimClient.buscarLatLongPorEndereco(endereco);


            if (response != null && response.lat() != 0 && response.lon() != 0) {
                return response;
            }

            return null; // Equivalente ao switchIfEmpty(Mono.empty())

        } catch (Exception error) {
            // Equivalente ao onErrorResume
            return null;
        }
    }
}