package it.gesev.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import it.gesev.entities.Fornitore;

public interface FornitoreRepository extends JpaRepository<Fornitore, Long> 
{
	public Optional<Fornitore> findByDescrizione(String descrizione);
}
