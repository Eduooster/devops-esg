package org.example.esg.api;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BaseIntegracaoTest {
    protected String token;
    protected Long pontoColetaId;
    @LocalServerPort
    private int port;


    String sufixo = String.valueOf(System.currentTimeMillis());

    String email = "usuario" + sufixo + "@email.com";

    String nomePonto = "Ponto Coleta " + sufixo;

    @BeforeEach
    public  void setup() {
        RestAssured.port = port;

        String corpoRegistro = """
{
          "email": "%s",
          "senha": "123456",
          "cep": "01001-000",
          "nome": "João Silva",
          "enderecoDto": {
            "cep": "01001-000",
            "logradouro": "Praça da Sé",
            "bairro": "Sé",
            "localidade": "São Paulo",
            "uf": "SP"
          }
        }
        """.formatted(email);


        given()
                .contentType("application/json")
                .body(corpoRegistro)
                .when()
                .post("/auth/register")
                .then()
                .statusCode(anyOf(is(204),is(409)));




        Response response = given()
                .contentType("application/json")
                .body("{\"email\": \"" + email + "\", \"senha\": \"123456\"}")
                .when()
                .post("/auth/login");

        response.then()
                .statusCode(200);


        token = response.jsonPath().getString("token");


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
                .post("/ponto-coleta");

        response.then()
                .statusCode(anyOf(is(201),is(200)));




        pontoColetaId = ((Number) responsePontoColeta.path("id")).longValue();



        RestAssured.requestSpecification = new RequestSpecBuilder()
                .addHeader("Authorization", "Bearer " + token)
                .setContentType("application/json")
                .build();

    }
}
