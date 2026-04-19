package org.example.esg.infra.persistence;

import org.example.esg.domain.entities.CapacidadePonto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CapacidadePontoRepository extends JpaRepository<CapacidadePonto, Long> {
}