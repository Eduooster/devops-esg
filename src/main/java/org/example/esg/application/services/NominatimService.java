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

    public Mono<NominatimResponse> buscarLatLng(Endereco endereco) {
        return nominatimClient.buscarLatLongPorEndereco(endereco)
                .filter(response -> response != null && response.lat() != 0 && response.lon() != 0)
                .switchIfEmpty(Mono.defer(() -> {

                    return Mono.empty();
                }))
                .onErrorResume(error -> {
                    return Mono.empty();
                });
    }
}
