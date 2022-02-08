package it.gesev.dao;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import it.gesev.entities.Fornitore;
import it.gesev.enums.ColonneFornitoreEnum;
import it.gesev.exc.GesevException;
import it.gesev.repository.FornitoreRepository;

@Component
public class FornitoreDAOImpl implements FornitoreDAO {

	private final Logger logger = LoggerFactory.getLogger(FornitoreDAOImpl.class);
	
	@Autowired
	FornitoreRepository fornitoreRepository;
	
	@PersistenceContext
	EntityManager entityManager;
	
	@Override
	public Fornitore getFornitoreByCodice(Long codice) 
	{
		logger.info("Ricerca fornitore con codice " + codice);
		Optional<Fornitore> optionalFornitore = fornitoreRepository.findById(codice);
		if(optionalFornitore.isPresent())
			return optionalFornitore.get();
		
		else
			throw new GesevException("Nessun fornitore presente con codice " + codice + ".", HttpStatus.BAD_REQUEST);
		
	}

	@Override
	public List<Fornitore> getAllFornitore() 
	{
		logger.info("Ricerca di tutti i fornitori presenti saul database...");
		return fornitoreRepository.findAll();
	}

	@Override
	public Long creaFornitore(String descrizione) 
	{
		logger.info("Creazione nuovo fornitore con descrizione: " + descrizione);
		if(StringUtils.isBlank(descrizione))
		{
			logger.info("La descrizione fornita non e' valida");
			throw new GesevException("La descrizione fornita non e' valida", HttpStatus.BAD_REQUEST);
		}
		
		logger.info("Controllo presenza fornitori con stessa descrizione...");
		Optional<Fornitore> controlloFornitore = fornitoreRepository.findByDescrizione(descrizione);
		if(controlloFornitore.isPresent())
			throw new GesevException("Descizione associata a fornitore gia' esistente", HttpStatus.BAD_REQUEST);
		
		logger.info("Inserimento nuovo record...");
		Fornitore fornitore = new Fornitore();
		fornitore.setDescrizione(descrizione);
		
		Fornitore afterStoring = fornitoreRepository.save(fornitore);
		return afterStoring.getCodice();
	}

	@Override
	public void cancellaFornitore(Long idFornitore) 
	{
		logger.info("Cancellazione fornitore con ID " + idFornitore);
		
		logger.info("Ricerca fornitore con ID scpecificato...");
		Optional<Fornitore> fornitoreOpt = fornitoreRepository.findById(idFornitore);
		if(!fornitoreOpt.isPresent())
			throw new GesevException("Nessun fornitore trovato con l'ID specificato", HttpStatus.BAD_REQUEST);
		
		logger.info("Fornitore trovato. Cancellazione in corso...");
		Fornitore fornitore = fornitoreOpt.get();
		if(fornitore.getTestataMovimento() != null && fornitore.getTestataMovimento().size() > 0)
			throw new GesevException("Impossibile rimuovere il fonitore, poiche' e' associato ad un movimento derrate", HttpStatus.BAD_REQUEST);
		
		fornitoreRepository.delete(fornitore);
	}

	@Override
	public void aggiornaFornitore(Fornitore fornitore) 
	{
		logger.info("Aggiornamento fornitore");
		if(fornitore == null || fornitore.getCodice() == null || StringUtils.isBlank(fornitore.getDescrizione()))
			throw new GesevException("Impossibile aggiornare il fornitore, dati non validi", HttpStatus.BAD_REQUEST);
		
		logger.info("Controllo dell'unicita' della descrizione fornita...");
		Optional<Fornitore> optionalFornitore = fornitoreRepository.findByDescrizione(fornitore.getDescrizione());
		if(optionalFornitore.isPresent() && optionalFornitore.get().getDescrizione().equalsIgnoreCase(fornitore.getDescrizione()) && 
	       fornitore.getCodice() != optionalFornitore.get().getCodice())
			throw new GesevException("La descrizione fornita risulta gia' associata ad un altro fornitore", HttpStatus.BAD_REQUEST);
		
		logger.info("Aggiornamento in corso...");
		Fornitore localFornitore = null;
		if(optionalFornitore.isPresent() && optionalFornitore.get().getCodice() == fornitore.getCodice())
			localFornitore = optionalFornitore.get();
		
		else
		{
			Optional<Fornitore> soughtFornitore = fornitoreRepository.findById(fornitore.getCodice());
			if(soughtFornitore.isPresent())
				localFornitore = soughtFornitore.get();
		}
		
		if(localFornitore == null)
			throw new GesevException("Nessun fornitore presente con l'ID " + fornitore.getCodice(), HttpStatus.BAD_REQUEST);
		
		localFornitore.setDescrizione(fornitore.getDescrizione());
		fornitoreRepository.save(localFornitore);
		
		logger.info("Fine aggiornamento");
		
	}

	@Override
	public List<Fornitore> cercaFornitoreConColonna(String colonna, String valore) {
		logger.info("Ricerca dei fornitore sulla base della colonna " + colonna.toUpperCase() + " e del valore " + valore);
		
		logger.info("Controllo esistenza colonna...");
		ColonneFornitoreEnum colonnaEnum = null;
		
		try 
		{
			colonnaEnum = ColonneFornitoreEnum.valueOf(colonna.toUpperCase());
		} 
		
		catch (Exception e) 
		{
			throw new GesevException("Si e' verificato un errore. " + ExceptionUtils.getStackFrames(e)[0], HttpStatus.BAD_REQUEST);
		}
		
		logger.info("Composizione della query di ricerca...");
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Fornitore> criteriaQuery = criteriaBuilder.createQuery(Fornitore.class);
		Root<Fornitore> fornitoreRoot = criteriaQuery.from(Fornitore.class);
		
		/* predicato finale per la ricerca */
		Predicate finalPredicate = null;
		
		switch(colonnaEnum)
		{
			case CODICE:
				/* creazione di un'espressione per effettuare LPAD */
				Expression<String> eTaskID = fornitoreRoot.get(ColonneFornitoreEnum.CODICE.getColonnaFornitore()).as(String.class);
		        Expression<Integer> length = criteriaBuilder.literal(6);
		        Expression<String> fillText = criteriaBuilder.literal("0");
		        Expression<String> expressionToGetPaddedCodice = criteriaBuilder.function("LPAD", String.class, eTaskID, length, fillText);
		        
				finalPredicate = criteriaBuilder.like(expressionToGetPaddedCodice, valore + "%");
				break;
				
			case DESCRIZIONE:
				finalPredicate = criteriaBuilder.like(fornitoreRoot.get(ColonneFornitoreEnum.DESCRIZIONE.getColonnaFornitore()), valore + "%");
		}
		
		/* esecuzione query */
		criteriaQuery.where(finalPredicate);
		List<Fornitore> items = entityManager.createQuery(criteriaQuery).getResultList();
		
		logger.info("Numero elementi trovati: " + items.size());
		
		/* restituzione */
		return items;
	}
	
	

}