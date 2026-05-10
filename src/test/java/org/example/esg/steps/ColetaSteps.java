package org.example.esg.steps;

import io.cucumber.java.pt.*;
import org.example.esg.application.dtos.in.ColetaRequestDto;
import org.example.esg.application.services.CriarColetaService;
import org.example.esg.domain.entities.CapacidadePonto;
import org.example.esg.domain.entities.PontoColeta;
import org.example.esg.domain.entities.TipoMaterial;
import org.example.esg.domain.entities.Usuario;
import org.example.esg.infra.persistence.ColetaRepository;
import org.example.esg.infra.persistence.NotificacaoRepository;
import org.example.esg.infra.persistence.PontoColetaRepository;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ColetaSteps {

    @Mock
    private PontoColetaRepository pontoColetaRepository;

    @Mock
    private ColetaRepository coletaRepository;

    @Mock
    private NotificacaoRepository notificacaoRepository;

    @InjectMocks
    private CriarColetaService criarColetaService;

    private PontoColeta ponto;
    private CapacidadePonto capacidade;
    private ColetaRequestDto request;
    private Usuario usuario;

    private Exception exception;

    public ColetaSteps() {
        MockitoAnnotations.openMocks(this);
    }

    // 🔹 GIVEN

    @Dado("existe um ponto de coleta válido")
    public void existe_um_ponto_de_coleta_valido() {
        ponto = new PontoColeta();
        ponto.setId(1L);

        ponto.setCapacidades(new ArrayList<>());

        request = new ColetaRequestDto(1L, 10, TipoMaterial.PLASTICO);
        usuario = new Usuario();

        when(pontoColetaRepository.findById(1L))
                .thenReturn(Optional.of(ponto));
    }

    @Dado("o ponto não aceita o tipo de material informado")
    public void o_ponto_nao_aceita_material() {
        // lista vazia ou sem o material do request
        ponto.setCapacidades(new ArrayList<>());
    }

    @Dado("existe um ponto de coleta válido com capacidade disponível")
    public void existe_ponto_valido_com_capacidade() {
        ponto = new PontoColeta();
        ponto.setId(1L);

        capacidade = new CapacidadePonto();
        capacidade.setTipoMaterial(TipoMaterial.PLASTICO);
        capacidade.setCapacidade(100);
        capacidade.setQuantidadeAtual(50);

        ponto.setCapacidades(List.of(capacidade));

        request = new ColetaRequestDto(1L, 10, TipoMaterial.PLASTICO);
        usuario = new Usuario();

        when(pontoColetaRepository.findById(1L))
                .thenReturn(Optional.of(ponto));
    }

    @Dado("não existe um ponto de coleta com o id informado")
    public void ponto_nao_existe() {
        request = new ColetaRequestDto(1L, 10, TipoMaterial.PLASTICO);
        usuario = new Usuario();

        when(pontoColetaRepository.findById(1L))
                .thenReturn(Optional.empty());
    }

    @Dado("o ponto aceita o tipo de material informado")
    public void o_ponto_aceita_o_tipo_de_material_informado() {
        // nesse caso, você já configurou isso no step anterior
        // então pode deixar vazio mesmo
    }

    @Dado("existe um ponto de coleta com capacidade quase cheia")
    public void capacidade_quase_cheia() {
        ponto = new PontoColeta();
        ponto.setId(1L);

        capacidade = new CapacidadePonto();
        capacidade.setTipoMaterial(TipoMaterial.PLASTICO);
        capacidade.setCapacidade(100);
        capacidade.setQuantidadeAtual(95);

        ponto.setCapacidades(List.of(capacidade));

        request = new ColetaRequestDto(1L, 10, TipoMaterial.PLASTICO);
        usuario = new Usuario();

        when(pontoColetaRepository.findById(1L))
                .thenReturn(Optional.of(ponto));
    }

    @Dado("existe um ponto de coleta com capacidade exata restante")
    public void capacidade_exata() {
        ponto = new PontoColeta();
        ponto.setId(1L);

        capacidade = new CapacidadePonto();
        capacidade.setTipoMaterial(TipoMaterial.PLASTICO);
        capacidade.setCapacidade(100);
        capacidade.setQuantidadeAtual(90);

        ponto.setCapacidades(List.of(capacidade));

        request = new ColetaRequestDto(1L, 10, TipoMaterial.PLASTICO);
        usuario = new Usuario();

        when(pontoColetaRepository.findById(1L))
                .thenReturn(Optional.of(ponto));
    }

    // 🔹 WHEN

    @Quando("o usuário registra uma coleta de {int} unidades")
    public void registra_coleta(int quantidade) {
        try {
            request = new ColetaRequestDto(1L, 10, TipoMaterial.PLASTICO);
            criarColetaService.criarColeta(request, usuario);
        } catch (Exception e) {
            exception = e;
        }
    }

    @Quando("o usuário tenta registrar uma coleta")
    public void tenta_registrar_coleta() {
        try {
            criarColetaService.criarColeta(request, usuario);
        } catch (Exception e) {
            exception = e;
        }
    }

    @Quando("o usuário tenta registrar uma coleta que ultrapassa o limite")
    public void coleta_ultrapassa_limite() {
        try {
            criarColetaService.criarColeta(request, usuario);
        } catch (Exception e) {
            exception = e;
        }
    }

    @Quando("o usuário registra uma coleta que atinge o limite máximo")
    public void coleta_atinge_limite() {
        try {
            criarColetaService.criarColeta(request, usuario);
        } catch (Exception e) {
            exception = e;
        }
    }

    // 🔹 THEN

    @Entao("a coleta deve ser salva com sucesso")
    public void coleta_salva() {
        assertNull(exception);
        verify(coletaRepository, times(1)).save(any());
    }

    @Entao("a quantidade atual do ponto deve ser atualizada")
    public void quantidade_atualizada() {
        assertEquals(60, capacidade.getQuantidadeAtual());
    }

    @Entao("o sistema deve retornar o erro {string}")
    public void retorna_erro(String mensagem) {
        assertNotNull(exception);
        assertTrue(exception.getMessage().contains(mensagem));
    }

    @Entao("uma notificação deve ser criada informando que a capacidade foi atingida")
    public void notificacao_criada() {
        verify(notificacaoRepository, times(1)).save(any());
    }
}