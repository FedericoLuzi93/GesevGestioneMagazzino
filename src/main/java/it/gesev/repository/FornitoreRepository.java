package it.gesev.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import it.gesev.entities.Fornitore;

public interface FornitoreRepository extends JpaRepository<Fornitore, Integer> 
{
	public Optional<Fornitore> findByDescrizione(String descrizione);
	public Optional<Fornitore> findByPiCf(String piCf);
}
