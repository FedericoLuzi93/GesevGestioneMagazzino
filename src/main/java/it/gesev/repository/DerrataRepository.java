package it.gesev.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.gesev.entities.Derrata;

public interface DerrataRepository extends JpaRepository<Derrata, Long>
{
	public Optional<Derrata> findByDescrizioneDerrata(String descrizioneDerrata);
	
	
	@Query("select d from Derrata d where d.tipoDerrata.codice = :tipoDerrataId")
	public List<Derrata> findAllByDerrataId(long tipoDerrataId);
	
	@Query("select max(derrataId) from Derrata")
	public Integer getMaxDerrataId();
	

}
