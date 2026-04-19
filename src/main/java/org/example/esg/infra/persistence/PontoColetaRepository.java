package org.example.esg.infra.persistence;

import org.example.esg.domain.entities.PontoColeta;
import org.example.esg.domain.entities.StatusCapacidade;
import org.example.esg.domain.entities.TipoMaterial;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PontoColetaRepository extends JpaRepository<PontoColeta, Long> {
    boolean existsByNome(String nome);

    Page<PontoColeta> findAllBy(Pageable pageable);

    @Query("""
    SELECT p FROM PontoColeta p
    JOIN p.capacidades c
    WHERE (:status IS NULL OR c.statusCapacidade = :status)
      AND (:uf IS NULL OR p.endereco.uf = :uf)
      AND (:tipo IS NULL OR c.tipoMaterial = :tipo)
""")
    Page<PontoColeta> findByFiltros(

            @Param("status") StatusCapacidade status,
            @Param("uf") String uf,
            @Param("tipo") TipoMaterial tipo,
            Pageable pageable
    );



    Page<PontoColeta> findDistinctByEndereco_UfAndCapacidades_statusCapacidadeIn(
            String uf,
            List<StatusCapacidade> status,
            Pageable pageable
    );
}