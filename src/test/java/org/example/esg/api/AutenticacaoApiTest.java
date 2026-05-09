package org.example.esg.api;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;

public class AutenticacaoApiTest extends BaseIntegracaoTest {

    private String authUrl = "/auth/login";

    @Test
    public void deveRegistrarUsuarioComSuceesso(){
        String corpoRegistro = """
            {
          "email": "usuarioTesteRegistro@email.com",
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
        """;


        given()
                .contentType("application/json")
                .body(corpoRegistro)
                .when()
                .post("/auth/register")
                .then()
                .statusCode(anyOf(is(204),is(409)));


    }

    @Test
    void deveAutenticarComSucessoERetornarToken() {
        String corpoRegistro = """
{
          "email": "usuarioAutenticadoTeste@email.com",
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
        """;


        given()
                .contentType("application/json")
                .body(corpoRegistro)
                .when()
                .post("/auth/register")
                .then()
                .statusCode(anyOf(is(204),is(409)));



        Response response = given()
                .contentType("application/json")
                .body("{\"email\": \"" + "usuarioAutenticadoTeste@email.com" + "\", \"senha\": \"123456\"}")
                .when()
                .post("/auth/login");

        response.then()
                .statusCode(200);





    }

}
