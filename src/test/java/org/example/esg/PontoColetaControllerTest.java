package org.example.esg;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PontoColetaControllerTest {

    @Autowired
    private MockMvc mockMvc;




    @Test

    void deveCriarPontoColeta() throws Exception {

        String json = """
        {
          "nome": "Ponto Teste2",
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
              "quantidadeAtual": 0,
              "statusCapacidade": "ATIVO",
              "capacidade": 100
            }
          ],
          "statusPontoGeral": "ABERTO"
        }
        """;

        mockMvc.perform(post("/ponto-coleta") // ajusta a URL se necessário
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated()); // ou isCreated()
    }


    @Test

    void deveListarPontosColeta() throws Exception {

        mockMvc.perform(get("/ponto-coleta")) // ajusta a URL
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }
}
