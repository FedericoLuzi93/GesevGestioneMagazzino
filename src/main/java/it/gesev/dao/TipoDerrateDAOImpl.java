package it.gesev.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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

import it.gesev.dto.StampaDerrateDTO;
import it.gesev.entities.Derrata;
import it.gesev.entities.TipoDerrata;
import it.gesev.enums.ColonneTipoDerrataEnum;
import it.gesev.exc.GesevException;
import it.gesev.repository.DerrataRepository;
import it.gesev.repository.TipoDerrateRepository;

@Repository
@Component
public class TipoDerrateDAOImpl implements TipoDerrateDAO 
{
	@Autowired
	private TipoDerrateRepository tipoDerrateRepository;
	
	@Autowired
	private DerrataRepository derrataRepository;
	
	@PersistenceContext
	EntityManager entityManager;
	
	private static final Logger logger = LoggerFactory.getLogger(TipoDerrateDAO.class);

	/* Leggi tutte le Derrate */
	public List<TipoDerrata> leggiTuttiTipiDerrate() 
	{
		logger.info("Accesso al TipoDerrateDAO metodo leggiTuttiTipiDerrate");
		return tipoDerrateRepository.findAll();
	}

	/* Crea una nuova derrata */
	public int createTipoDerrata(TipoDerrata tipoDerrata) 
	{
		logger.info("Accesso al TipoDerrateDAO metodo createTipoDerrata");
		if(StringUtils.isBlank(tipoDerrata.getDescrizione()))
		{
			logger.info("Impossibile creare un lotto con Descrizione vuota");
			throw new GesevException("Impossibile creare un lotto con Descrizione vuota", HttpStatus.BAD_REQUEST);
		}
		Optional<TipoDerrata> optionalTipoDerrataDescrizione = tipoDerrateRepository.findByDescrizione(tipoDerrata.getDescrizione());
		if(optionalTipoDerrataDescrizione.isPresent())
		{
			logger.info("Impossibile creare un lotto con una descrizione gia' presente");
			throw new GesevException("Impossibile creare un lotto con una descrizione gia' presente", HttpStatus.BAD_REQUEST);
		}
		logger.info("Creazione lotto in corso...");
		TipoDerrata tipoDerrataRepository =  tipoDerrateRepository.save(tipoDerrata);
		if(tipoDerrataRepository == null)
			throw new GesevException("Impossibile inserire un nuovo reocrod nella tabella lotto", HttpStatus.INTERNAL_SERVER_ERROR);
		return tipoDerrataRepository.getCodice();
	}

	/* Cancella una Derrata by codice */
	public int deleteTipoDerrata(int codiceTipoDerrata) 
	{
		logger.info("Accesso al TipoDerrateDAO metodo deleteTipoDerrata");
		Integer maxCodice = tipoDerrateRepository.getMaxCodice();
		
		if(codiceTipoDerrata > maxCodice || codiceTipoDerrata < 0)
		{
			logger.info("Impossibile cancellare un lotto con questo Codice");
			throw new GesevException("Impossibile cancellare un lotto con questo Codice", HttpStatus.BAD_REQUEST);
		}
		
		Optional<TipoDerrata> optionalTipoDerrata = tipoDerrateRepository.findByCodice(codiceTipoDerrata);
		if(!optionalTipoDerrata.isPresent())
		{
			logger.info("Impossibile cancellare un lotto, Codice non presente");
			throw new GesevException("Impossibile cancellare un lotto, Codice non presente", HttpStatus.BAD_REQUEST);
		}
		
		TipoDerrata tipoDerrata = optionalTipoDerrata.get();
		if(tipoDerrata.getListaDerrata().size() > 0)
			throw new GesevException("Impossibile cancellare il lotto, poiche' e' associata ad una derrata", HttpStatus.BAD_REQUEST);
		
		if(optionalTipoDerrata.isPresent())
		{
			logger.info("Cancellazione del lotto con codice " + codiceTipoDerrata + " in corso...");
			tipoDerrateRepository.deleteByCodice(codiceTipoDerrata);
			logger.info("Cancellazione del lotto con codice " + codiceTipoDerrata + " riuscita");
		}
		return codiceTipoDerrata;
	}

	/* Aggiorna un tipo derrata */
	public int updateTipoDerrata(int codiceTipoDerrata, TipoDerrata tipoDerrata) 
	{
		logger.info("Accesso al TipoDerrateDAO metodo updateTipoDerrata");
		Integer maxCodice = tipoDerrateRepository.getMaxCodice();
		
		// codice Massimo
		if(codiceTipoDerrata > maxCodice || codiceTipoDerrata < 0)
		{
			logger.info("Impossibile modificare un lotto con questo Codice");
			throw new GesevException("Impossibile modificare un lotto con questo Codice", HttpStatus.BAD_REQUEST);
		}
		
		Optional<TipoDerrata> optionalTipoDerrataCodice = tipoDerrateRepository.findByCodice(codiceTipoDerrata);
		Optional<TipoDerrata> optionalTipoDerrataDescrizione = tipoDerrateRepository.findByDescrizione(tipoDerrata.getDescrizione());
		
		// Controllo Descrizione
		if(optionalTipoDerrataDescrizione.isPresent() && optionalTipoDerrataDescrizione.get().getDescrizione().equalsIgnoreCase(tipoDerrata.getDescrizione()) && 
				optionalTipoDerrataCodice.get().getCodice() != optionalTipoDerrataDescrizione.get().getCodice())
		{
			logger.info("Impossibile modificare un lotto, Descrizione già presente");
			throw new GesevException("Impossibile modificare un lotto, Descrizione già presente", HttpStatus.BAD_REQUEST);
		}
				
		// Controllo Codice
		if(!optionalTipoDerrataCodice.isPresent())
		{
			logger.info("Impossibile modificare un lotto, Codice non presente");
			throw new GesevException("Impossibile modificare un lotto, Codice non presente", HttpStatus.BAD_REQUEST);
		}
		if(optionalTipoDerrataCodice.isPresent())
		{
			logger.info("Modifica del lotto con codice " + codiceTipoDerrata + " in corso...");
			tipoDerrateRepository.save(tipoDerrata);
			logger.info("Modifica del lotto con codice " + codiceTipoDerrata + " riuscita");	
		}
		return codiceTipoDerrata;
	}
	
	/* Cerca una derrata */
	public List<TipoDerrata> cercaTipoDerrataConColonna(Map<String, String> mappa) 
	{
		logger.info("Composizione della query di ricerca...");
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<TipoDerrata> criteriaQuery = criteriaBuilder.createQuery(TipoDerrata.class);
		Root<TipoDerrata> tipoDerrataRoot = criteriaQuery.from(TipoDerrata.class);
		
		//predicato finale per la ricerca
		Predicate finalPredicate = criteriaBuilder.and();
		
		for(Entry<String, String> entryMap : mappa.entrySet())
		{
			ColonneTipoDerrataEnum colonnaEnum = null;
			String colonna = entryMap.getKey();
			String valore = entryMap.getValue();
	
			if(StringUtils.isBlank(valore))
				throw new GesevException("Inserire un valore per la ricerca", HttpStatus.BAD_REQUEST);
			
			logger.info("Ricerca del lotto sulla base della colonna " + colonna.toUpperCase() + " e del valore " + valore);
			logger.info("Controllo esistenza colonna...");
			
			try 
			{
				colonnaEnum = ColonneTipoDerrataEnum.valueOf(colonna.toUpperCase());
			} 
			
			catch (Exception e) 
			{
				throw new GesevException("Si e' verificato un errore. " + ExceptionUtils.getStackFrames(e)[0], HttpStatus.BAD_REQUEST);
			}
			
			switch(colonnaEnum)
			{
				case CODICE:
					// creazione di un'espressione per effettuare LPAD
					Expression<String> eTaskID = tipoDerrataRoot.get(ColonneTipoDerrataEnum.CODICE.getclonnaTipoDerrata()).as(String.class);
			        Expression<Integer> length = criteriaBuilder.literal(6);
			        Expression<String> fillText = criteriaBuilder.literal("0");
			        Expression<String> expressionToGetPaddedCodice = criteriaBuilder.function("LPAD", String.class, eTaskID, length, fillText);
			        
					finalPredicate = criteriaBuilder.and(finalPredicate, criteriaBuilder.like(expressionToGetPaddedCodice, valore + "%"));
					break;
					
				case DESCRIZIONE:
					finalPredicate = criteriaBuilder.and(finalPredicate, criteriaBuilder.like(tipoDerrataRoot.get(ColonneTipoDerrataEnum.DESCRIZIONE.getclonnaTipoDerrata()), valore + "%"));
					break;
			}
		}
		
		//esecuzione query
		criteriaQuery.where(finalPredicate);
		List<TipoDerrata> items = entityManager.createQuery(criteriaQuery).getResultList();
		
		logger.info("Numero elementi trovati: " + items.size());
		
		//restituzione
		return items;
	}

	@Override
	public List<StampaDerrateDTO> getRigheStampaDerrate(Integer idTipoDerrata) 
	{
		logger.info("Preparazione stampa...");
		List<StampaDerrateDTO> listaStampe = new ArrayList<>();
		
		List<TipoDerrata> listaTipoDerrate = null;
		
		if(idTipoDerrata == null)
			listaTipoDerrate = tipoDerrateRepository.findAllByOrderByCodiceAsc();
		
		else
		{
			Optional<TipoDerrata> optTipoDerrata = tipoDerrateRepository.findByCodice(idTipoDerrata);
			if(!optTipoDerrata.isPresent())
				throw new GesevException("Impossibile trovare un lotto con l'ID specificato", HttpStatus.BAD_REQUEST);
			
			listaTipoDerrate = Arrays.asList(optTipoDerrata.get());
		}
		
		for(TipoDerrata tipo : listaTipoDerrate)
		{
			StampaDerrateDTO stampaTipoDerrata = new StampaDerrateDTO();
			stampaTipoDerrata.setCodiceLotto(StringUtils.leftPad(String.valueOf(tipo.getCodice()), 4, "0"));
			stampaTipoDerrata.setDescrizioneLotto(tipo.getDescrizione());
			
			stampaTipoDerrata.setDerrataId("");
			stampaTipoDerrata.setDescrizioneDerrata("");
			stampaTipoDerrata.setPrezzo("");
			stampaTipoDerrata.setUnitaMisura("");
			
			listaStampe.add(stampaTipoDerrata);
			
			List<Derrata> listaDerrate = derrataRepository.findByTipoDerrataSortByDerrataId(tipo.getCodice());
			if(listaDerrate != null && listaDerrate.size() > 0)
			{
				for(Derrata derrata : listaDerrate)
				{
					StampaDerrateDTO stampaDerrata = new StampaDerrateDTO();
					stampaDerrata.setCodiceLotto("");
					stampaDerrata.setDescrizioneLotto("");
					
					
					stampaDerrata.setDerrataId(StringUtils.leftPad(String.valueOf(derrata.getDerrataId()), 4, "0"));
					stampaDerrata.setDescrizioneDerrata(derrata.getDescrizioneDerrata());
					stampaDerrata.setPrezzo(String.valueOf(derrata.getPrezzo()));
					stampaDerrata.setUnitaMisura(derrata.getUnitaMisura());
					
					listaStampe.add(stampaDerrata);
				}
			}
		}
		
		return listaStampe;
	}
}
