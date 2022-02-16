package it.gesev.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import it.gesev.entities.TipoDerrata;


public interface TipoDerrateRepository extends JpaRepository<TipoDerrata, Integer>
{
	
	@Query("select max(codice) from TipoDerrata")
	public Integer getMaxCodice();
	
	@Query("select t from TipoDerrata t where t.codice = :codiceTipoDerrata")
	public Optional<TipoDerrata> findByCodice(long codiceTipoDerrata);
	
	@Transactional
	@Modifying
	@Query("delete from TipoDerrata t where t.codice = :codiceTipoDerrata")
	void deleteByCodice(@Param("codiceTipoDerrata") long codiceTipoDerrata);
	
	@Transactional
	@Modifying
	@Query("update TipoDerrata t set t.descrizione =:descrizione where t.codice = :codiceTipoDerrata")
	void updateByCodice(@Param("descrizione") TipoDerrata tipoDerrata, @Param("codiceTipoDerrata") long codiceTipoDerrata);
	
	public Optional<TipoDerrata> findByDescrizione(String descrizione);

}
