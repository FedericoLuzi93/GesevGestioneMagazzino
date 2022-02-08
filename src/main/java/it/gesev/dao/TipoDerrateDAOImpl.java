package it.gesev.dao;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import it.gesev.entities.TipoDerrata;
import it.gesev.enums.ColonneTipoDerrataEnum;
import it.gesev.exc.GesevException;
import it.gesev.repository.TipoDerrateRepositroy;

@Repository
@Component
public class TipoDerrateDAOImpl implements TipoDerrateDAO 
{
	
	@Autowired
	private TipoDerrateRepositroy tipoDerrateRepositroy;
	
	@PersistenceContext
	EntityManager entityManager;
	
	private static final Logger logger = LoggerFactory.getLogger(TipoDerrateDAO.class);

	/* Leggi tutte le Derrate */
	public List<TipoDerrata> leggiTuttiTipiDerrate() 
	{
		logger.info("Accesso al TipoDerrateDAO metodo leggiTuttiTipiDerrate");
		return tipoDerrateRepositroy.findAll();
	}

	/* Crea una nuova derrata */
	public long createTipoDerrata(TipoDerrata tipoDerrata) 
	{
		logger.info("Accesso al TipoDerrateDAO metodo createTipoDerrata");
		if(StringUtils.isBlank(tipoDerrata.getDescrizione()))
		{
			logger.info("Impossibile creare un tipoDerrata con Descrizione vuota");
			throw new GesevException("Impossibile creare un tipoDerrata con Descrizione vuota", HttpStatus.BAD_REQUEST);
		}
		Optional<TipoDerrata> optionalTipoDerrataDescrizione = tipoDerrateRepositroy.findByDescrizione(tipoDerrata.getDescrizione());
		if(optionalTipoDerrataDescrizione.isPresent())
		{
			logger.info("Impossibile creare un tipoDerrata con una descrizione gia' presente");
			throw new GesevException("Impossibile creare un tipoDerrata con una descrizione gia' presente", HttpStatus.BAD_REQUEST);
		}
		logger.info("Creazione tipo derrata in corso...");
		TipoDerrata tipoDerrataRepository =  tipoDerrateRepositroy.save(tipoDerrata);
		if(tipoDerrataRepository == null)
			throw new GesevException("Impossibile inserire un nuovo reocrod nella tabella TipoDerrata", HttpStatus.INTERNAL_SERVER_ERROR);
		return tipoDerrataRepository.getCodice();
	}

	/* Cancella una Derrata by codice */
	public long deleteTipoDerrata(long codiceTipoDerrata) 
	{
		logger.info("Accesso al TipoDerrateDAO metodo deleteTipoDerrata");
		Integer maxCodice = tipoDerrateRepositroy.getMaxCodice();
		
		if(codiceTipoDerrata > maxCodice || codiceTipoDerrata < 0)
		{
			logger.info("Impossibile cancellare un tipoDerrata con questo Codice");
			throw new GesevException("Impossibile cancellare un tipoDerrata con questo Codice", HttpStatus.BAD_REQUEST);
		}
		
		Optional<TipoDerrata> optionalTipoDerrata = tipoDerrateRepositroy.findByCodice(codiceTipoDerrata);
		if(!optionalTipoDerrata.isPresent())
		{
			logger.info("Impossibile cancellare un tipoDerrata, Codice non presente");
			throw new GesevException("Impossibile cancellare un tipoDerrata, Codice non presente", HttpStatus.BAD_REQUEST);
		}
		
		TipoDerrata tipoDerrata = optionalTipoDerrata.get();
		if(tipoDerrata.getListaDerrata().size() > 0)
			throw new GesevException("Impossibile cancellare il tipo derrata, poiche' e' associata ad una derrata", HttpStatus.BAD_REQUEST);
		
		if(optionalTipoDerrata.isPresent())
		{
			logger.info("Cancellazione del tipo derrata con codice " + codiceTipoDerrata + " in corso...");
			tipoDerrateRepositroy.deleteByCodice(codiceTipoDerrata);
			logger.info("Cancellazione del tipo derrata con codice " + codiceTipoDerrata + " riuscita");
		}
		return codiceTipoDerrata;
	}

	/* Aggiorna un tipo derrata */
	public long updateTipoDerrata(long codiceTipoDerrata, String descrizione) 
	{
		logger.info("Accesso al TipoDerrateDAO metodo updateTipoDerrata");
		Integer maxCodice = tipoDerrateRepositroy.getMaxCodice();
		//Codice Max e Min di 0
		if(codiceTipoDerrata > maxCodice || codiceTipoDerrata < 0)
		{
			logger.info("Impossibile modificare un tipoDerrata con questo Codice");
			throw new GesevException("Impossibile modificare un tipoDerrata con questo Codice", HttpStatus.BAD_REQUEST);
		}
		
		//Recupero l'oggetto dal Codice
		Optional<TipoDerrata> optionalTipoDerrataCodice = tipoDerrateRepositroy.findByCodice(codiceTipoDerrata);
		
		//Recupero l'oggetto dalla Descrizione
		Optional<TipoDerrata> optionalTipoDerrataDescrizione = tipoDerrateRepositroy.findByDescrizione(descrizione);
		
		//Descrizione già presente
		if(optionalTipoDerrataDescrizione.isPresent() && optionalTipoDerrataDescrizione.get().getDescrizione().equalsIgnoreCase(descrizione) && 
				optionalTipoDerrataCodice.get().getCodice() != optionalTipoDerrataDescrizione.get().getCodice())
		{
			logger.info("Impossibile modificare un tipoDerrata, Descrizione già presente");
			throw new GesevException("Impossibile modificare un tipoDerrata, Descrizione già presente", HttpStatus.BAD_REQUEST);
		}
				
		//Codice non presente
		if(!optionalTipoDerrataCodice.isPresent())
		{
			logger.info("Impossibile modificare un tipoDerrata, Codice non presente");
			throw new GesevException("Impossibile modificare un tipoDerrata, Codice non presente", HttpStatus.BAD_REQUEST);
		}
		
		//Codice presente
		if(optionalTipoDerrataCodice.isPresent())
		{
			logger.info("Modifica del tipo derrata con codice " + codiceTipoDerrata + " in corso...");
			tipoDerrateRepositroy.updateByCodice(descrizione, codiceTipoDerrata);
			logger.info("Modifica del tipo derrata con codice " + codiceTipoDerrata + " riuscita");	
		}
		return 1;
	}
	
	/* Cerca una derrata */
	public List<TipoDerrata> cercaTipoDerrataConColonna(String colonna, String valore) 
	{
		logger.info("Ricerca del tipo derrata sulla base della colonna " + colonna.toUpperCase() + " e del valore " + valore);
		logger.info("Controllo esistenza colonna...");
		ColonneTipoDerrataEnum colonnaEnum = null;
		
		try 
		{
			colonnaEnum = ColonneTipoDerrataEnum.valueOf(colonna.toUpperCase());
		} 
		
		catch (Exception e) 
		{
			throw new GesevException("Si e' verificato un errore. " + ExceptionUtils.getStackFrames(e)[0], HttpStatus.BAD_REQUEST);
		}
		
		logger.info("Composizione della query di ricerca...");
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<TipoDerrata> criteriaQuery = criteriaBuilder.createQuery(TipoDerrata.class);
		Root<TipoDerrata> tipoDerrataRoot = criteriaQuery.from(TipoDerrata.class);
		
		//predicato finale per la ricerca
		Predicate finalPredicate = null;
		
		switch(colonnaEnum)
		{
			case CODICE:
				/* creazione di un'espressione per effettuare LPAD */
				Expression<String> eTaskID = tipoDerrataRoot.get(ColonneTipoDerrataEnum.CODICE.getclonnaTipoDerrata()).as(String.class);
		        Expression<Integer> length = criteriaBuilder.literal(6);
		        Expression<String> fillText = criteriaBuilder.literal("0");
		        Expression<String> expressionToGetPaddedCodice = criteriaBuilder.function("LPAD", String.class, eTaskID, length, fillText);
		        
				finalPredicate = criteriaBuilder.like(expressionToGetPaddedCodice, valore + "%");
				break;
				
			case DESCRIZIONE:
				finalPredicate = criteriaBuilder.like(tipoDerrataRoot.get(ColonneTipoDerrataEnum.DESCRIZIONE.getclonnaTipoDerrata()), valore + "%");
		}
		
		//esecuzione query
		criteriaQuery.where(finalPredicate);
		List<TipoDerrata> items = entityManager.createQuery(criteriaQuery).getResultList();
		
		logger.info("Numero elementi trovati: " + items.size());
		
		//restituzione
		return items;
	}
}
