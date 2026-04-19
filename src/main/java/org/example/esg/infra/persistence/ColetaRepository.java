package org.example.esg.infra.persistence;

import org.example.esg.domain.entities.Coleta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColetaRepository extends JpaRepository<Coleta, Long> {
}