package org.example.esg.infra.external;


import org.example.esg.application.dtos.out.NominatimResponse;
import org.example.esg.domain.entities.Endereco;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class NominatimClient {

    private final WebClient webClient;

    public NominatimClient() {
        this.webClient = WebClient.builder()
                .baseUrl("https://nominatim.openstreetmap.org")
                .defaultHeader("User-Agent", "Mozilla/5.0 (compatible; MyApp/1.0; +http://localhost)") // obrigatório
                .build();
    }


    public Mono<NominatimResponse> buscarLatLongPorEndereco(Endereco endereco) {

        String query = URLEncoder.encode(
                endereco.getLogradouro() + ", " +
                        endereco.getBairro() + ", " +
                        endereco.getLocalidade() + ", " +
                        endereco.getUf() + ", Brasil",
                StandardCharsets.UTF_8
        );


        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search")
                        .queryParam("q", query)
                        .queryParam("format", "json")
                        .queryParam("addressdetails", 1)
                        .build())
                .retrieve()
                .bodyToFlux(NominatimResponse.class)
                .next();
    }
}
