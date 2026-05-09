package org.example.esg.api;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;

public class PontoColetaApiTest extends BaseIntegracaoTest {

    private String pontoColetaUrl = "/ponto-coleta";

    @Test
    public void deveCriarPontoColetaComSucesso() {
        Response responsePontoColeta = given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body("""
                {
                  "nome":"Ponto coleta criado via api teste3",
                  "endereco": {
                    "cep": "01001-000",
                    "logradouro": "Praça da Sé",
                    "bairro": "Sé",
                    "localidade": "São Paulo",
                    "uf": "SP",
                    "lat": -23.55052,
                    "lng": -46.633308
                  },
                  "capacidades": [
                    {
                      "tipoMaterial": "PAPEL",
                      "quantidadeAtual": 50,
                      "statusCapacidade": "ATIVO",
                      "capacidade": 100
                    }
                  ],
                  "statusPontoGeral": "ABERTO"
                }
                """)
                .when()
                .post(pontoColetaUrl);
        responsePontoColeta.then().statusCode(201);


        pontoColetaId = ((Number) responsePontoColeta.path("id")).longValue();
    }

    @Test
    public void deveListarPontosColetasComSucesso() {
        Response responsePontosColeta =
                given()
                        .header("Authorization", "Bearer " + token)
                        .contentType("application/json")
                        .when()
                        .get(pontoColetaUrl);

        responsePontosColeta.then()
                .statusCode(200);

    }

    @Test
    public void deveListarPontoColetaFiltradosComSucesso(){
        Response responsePontosColeta =
                given()
                        .header("Authorization", "Bearer " + token)
                        .contentType("application/json")
                        .when()
                        .get("%s/filtrar?status=ATIVO&uf=SP&material=PAPEL".formatted(pontoColetaUrl));

        responsePontosColeta.then()
                .statusCode(200);
    }





}
