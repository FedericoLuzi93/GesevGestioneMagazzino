package it.gesev.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.gesev.entities.TestataMovimento;

public interface TestataMovimentoRepository extends JpaRepository<TestataMovimento,Long> 
{
	@Query("select tm from TestataMovimento tm where tm.fornitore.codice = :idFornitore")
	public List<TestataMovimento> getTestataMovimentoByIdFornitore(@Param("idFornitore") Long idFornitore);
}
