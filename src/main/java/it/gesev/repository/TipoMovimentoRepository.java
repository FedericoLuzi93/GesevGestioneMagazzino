package it.gesev.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.gesev.entities.TipoMovimento;

public interface TipoMovimentoRepository extends JpaRepository<TipoMovimento, Integer> {

}
