package org.example.esg.api;



import io.restassured.RestAssured;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

import org.junit.jupiter.api.Test;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;


import org.junit.jupiter.api.BeforeEach;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ColetaApiTest extends BaseIntegracaoTest{

    @Test
    public void deveCriarColetaComSucesso() {
        given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(String.format("""
            {
              "pontoColetaId": %d,
              "tipoMaterial": "PAPEL",
              "quantidadeDepositada": 10
            }
            """, pontoColetaId))
                .when()
                .post("/coleta")
                .then()
                .statusCode(201);
    }
    @Test
    public void deveGarantirQueSetupFoiCarregado() {
        assertNotNull(pontoColetaId, "O pontoColetaId não deveria ser nulo após o setup");

        assertNotNull(token, "O token JWT não deveria ser nulo");
        assertFalse(token.isEmpty(), "O token JWT não deveria estar vazio");

    }

    @Test
    public void deveRetornar404QuandoPontoNaoExiste() {
        given()
                .contentType("application/json")
                .body("""
            {
              "pontoColetaId": 999,
              "tipoMaterial": "PLASTICO",
              "quantidadeDepositada": 10
            }
        """)
                .when()
                .post("/coleta")
                .then()
                .statusCode(404)
                .log().all()
                .body("mensagem", containsString("Ponto não encontrado"));
    }


}


