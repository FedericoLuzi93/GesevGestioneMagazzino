package it.gesev.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import it.gesev.entities.Derrata;
import it.gesev.entities.DettaglioMovimento;
import it.gesev.entities.Fornitore;
import it.gesev.entities.TestataMovimento;
import it.gesev.exc.GesevException;
import it.gesev.repository.TestataMovimentoRepository;

@Component
public class TestataMovimentoDAOImpl implements TestataMovimentoDAO {

	@Autowired
	private TestataMovimentoRepository testataMovimentoRepository;
	
	@PersistenceContext
	EntityManager entityManager;
	
	private static final Logger logger = LoggerFactory.getLogger(TestataMovimentoDAOImpl.class);
	
	@Override
	public List<TestataMovimento> getDettaglioMovimentoByFornitore(Long idFornitore) 
	{
		logger.info("Ricerca testata movimento con ID fornitore " + idFornitore);
		
		List<TestataMovimento> testataMovimento = testataMovimentoRepository.getTestataMovimentoByIdFornitore(idFornitore.intValue());
//		if(testataMovimento.size() > 0)
//			return testataMovimento;
//		
//		else
//			throw new GesevException("Nessun fornitore presente con l'ID fornito", HttpStatus.BAD_REQUEST);
	}

	@Override
	public List<TestataMovimento> cercaDerrateInTestate(Date dataDa, Date dataA, String descrizioneDerrata, Long idFornitore) 
	{
		logger.info("Ricerca delle derrate di un fornitore sulla base delle date e della derrata...");
		
		/* controllo fornitore */
		if(idFornitore == null)
			throw new GesevException("Impossibile trovare movimenti. ID fornitore non presente", HttpStatus.BAD_REQUEST);
		
		/* controllo validita' delle date */
		if(dataDa != null && dataA != null && dataDa.after(dataA))
			throw new GesevException("La data DAL deve precedere la data AL", HttpStatus.BAD_REQUEST);
		
		logger.info("Preparazione della query di ricerca...");
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<TestataMovimento> criteriaQuery = criteriaBuilder.createQuery(TestataMovimento.class);
		Root<TestataMovimento> testatataRoot = criteriaQuery.from(TestataMovimento.class);
		
		/* predicato finale */
		Predicate finalPredicate = null;
		
		/* controllo presenza date */
		if(dataDa != null)
			finalPredicate = criteriaBuilder.greaterThanOrEqualTo(testatataRoot.get("data").as(java.sql.Date.class), dataDa);
		
		if(dataA != null)
		{
	
			Predicate dataMinore = criteriaBuilder.lessThanOrEqualTo(testatataRoot.get("data").as(java.sql.Date.class), dataA);
			finalPredicate = finalPredicate == null ? dataMinore : criteriaBuilder.and(finalPredicate, dataMinore);
		}
		
		/* predicato relativo al fornitore */
		Join<TestataMovimento, Fornitore> joinFornitore = testatataRoot.join("fornitore");
		Predicate predicatoFornitore = criteriaBuilder.equal(joinFornitore.get("codice"), idFornitore);
		finalPredicate = finalPredicate == null ? predicatoFornitore : criteriaBuilder.and(finalPredicate, predicatoFornitore);
		
		/* predicato descrizione derrata */
		if(StringUtils.isNotBlank(descrizioneDerrata))
		{
			Join<TestataMovimento, DettaglioMovimento> dettaglioJoin = testatataRoot.joinList("listaDettaglioMovimento");
			Join<DettaglioMovimento, Derrata> derrataJoin = dettaglioJoin.join("derrata");
			Predicate derrataPredicate = criteriaBuilder.equal(derrataJoin.get("descrizioneDerrata"), descrizioneDerrata);
			finalPredicate = criteriaBuilder.and(finalPredicate, derrataPredicate);
		}
		
		logger.info("Esecuzione query...");
		criteriaQuery.where(finalPredicate);
		List<TestataMovimento> listaTestate = entityManager.createQuery(criteriaQuery).getResultList();
		
		logger.info("Numero elementi trovati: " + listaTestate.size());
		
		return listaTestate;
	}

	
}
