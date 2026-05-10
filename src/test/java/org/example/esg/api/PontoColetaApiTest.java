package org.example.esg.api;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;

public class PontoColetaApiTest extends BaseIntegracaoTest {

    private String pontoColetaUrl = "/ponto-coleta";

    @Test
    public void deveCriarPontoColetaComSucesso() {
        String sufixo = String.valueOf(System.currentTimeMillis());
        String nomePonto = "Ponto Coleta " + sufixo;

        Response responsePontoColeta = given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body("""
                {
                  "nome":"%s",
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
                """.formatted(nomePonto))
                .when()
                .post(pontoColetaUrl);
        responsePontoColeta.then()
                .statusCode(201)
                .body(matchesJsonSchemaInClasspath("schemas/ponto-coleta-criado-schema.json"));


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
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/listar-pontos-coleta-schema.json"));

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
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/listar-pontos-coleta-schema.json"));
    }

    @Test
    public void deveDeletarPontoColetaComSucesso(){
        given()
                .basePath("/ponto-coleta") // ajuste pro seu controller real
                .pathParam("id", pontoColetaId)
                .when()
                .delete("/{id}")
                .then()
                .statusCode(204);
    }
}






