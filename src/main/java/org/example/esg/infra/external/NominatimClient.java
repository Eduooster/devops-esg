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


    public NominatimResponse buscarLatLongPorEndereco(Endereco endereco) {

        String query  =
                endereco.getLogradouro() + ", " +
                        endereco.getBairro() + ", " +
                        endereco.getLocalidade() + ", " +
                        endereco.getUf() + ", Brasil";




        System.out.println("QUERY ENCODED -> " + query);

        NominatimResponse response = webClient.get()
                .uri(uriBuilder -> {
                    var uri = uriBuilder
                            .path("/search")
                            .queryParam("q", query)
                            .queryParam("format", "json")
                            .queryParam("addressdetails", 1)
                            .build();

                    System.out.println("URI FINAL -> " + uri);

                    return uri;
                })
                .retrieve()
                .bodyToFlux(NominatimResponse.class)
                .doOnNext(item -> System.out.println("ITEM RECEBIDO -> " + item))
                .doOnError(error -> {
                    System.out.println("ERRO NA REQUISICAO");
                    error.printStackTrace();
                })
                .next()
                .block();

        System.out.println("RESPONSE FINAL -> " + response);

        return response;
    }
}