package it.gesev.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import it.gesev.entities.Derrata;
import it.gesev.entities.TipoDerrata;
import it.gesev.enums.ColonneDerrataEnum;
import it.gesev.exc.GesevException;
import it.gesev.repository.DerrataRepository;
import it.gesev.repository.TipoDerrateRepositroy;

@Repository
@Component
public class DerrataDAOImpl implements DerrataDAO 
{
	private final Logger logger = LoggerFactory.getLogger(DerrataDAO.class);
	
	@Autowired
	DerrataRepository derrataRepository;
	
	@Autowired
	TipoDerrateRepositroy tipoDerrateRepositroy;
	
	@PersistenceContext
	EntityManager entityManager;
	
	@Value("${gesev.data.format}")
	private String dateFormat;
	
	/* Cerca tutte le derrata */
	@Override
	public List<Derrata> getAllDerrata(long tipoDerrataId) 
	{
		logger.info("Rierca di tutte le Derrate presenti sul database");
		return derrataRepository.findAllByDerrataId(tipoDerrataId);
	}

	/* Crea la Derrata */
	@Override
	public Long creaDerrata(Derrata derrata, int codiceTipoDerrata) 
	{
		Optional<TipoDerrata> optionalTipoDerrata = tipoDerrateRepositroy.findByCodice(codiceTipoDerrata);
		if(!optionalTipoDerrata.isPresent())
		{
			logger.info("Impossibile creare una derrata. Tipo derrata non presente");
			throw new GesevException("Impossibile creare una derrata. Tipo derrata non presente", HttpStatus.BAD_REQUEST);
		}
		
		logger.info("Creazione nuova derrata in corso...");
		if(StringUtils.isBlank(derrata.getDescrizioneDerrata()) || StringUtils.isBlank(derrata.getUnitaMisura())
				|| derrata.getPrezzo() <= 0 || derrata.getQuantitaMinima() <= 0)
		{
			logger.info("Impossibile creare una derrata. Campi inseriti non validi");
			throw new GesevException("Impossibile creare una derrata. Campi inseriti non validi", HttpStatus.BAD_REQUEST);
		}
		
		Optional<Derrata> optionalTipoDerrataDescrizione = derrataRepository.findByDescrizioneDerrata(derrata.getDescrizioneDerrata());
		if(optionalTipoDerrataDescrizione.isPresent())
			throw new GesevException("Descrizione associata alla derrata gia' esistente", HttpStatus.BAD_REQUEST);
		
		logger.info("Inserimento nuovo record derrata in corso...");
		derrata.setDataAggiornamentoGiacenza(new Date());
		Derrata derrataObj = derrata;
		
		if(derrataObj == null)
			throw new GesevException("Impossibile inserire un nuovo reocrod nella tabella derrata", HttpStatus.INTERNAL_SERVER_ERROR);

		derrataObj.setDescrizioneDerrata(derrata.getDescrizioneDerrata());
		derrataObj.setUnitaMisura(derrata.getUnitaMisura());
		derrataObj.setPrezzo(derrata.getPrezzo());
		derrataObj.setGiacenza(derrata.getGiacenza());
		derrataObj.setDataAggiornamentoGiacenza(derrata.getDataAggiornamentoGiacenza());
		derrataObj.setQuantitaMinima(derrata.getQuantitaMinima());
		derrataObj.setCodiceMensa(derrata.getCodiceMensa());
		
		derrataObj.setDettaglioPrelevamento(derrata.getDettaglioPrelevamento());
		derrataObj.setDettaglioMovimento(derrata.getDettaglioMovimento());
		derrataObj.setTipoDerrata(optionalTipoDerrata.get());
			
		derrataRepository.save(derrataObj);
		
		return derrataObj.getDerrataId();
	}

	/* Cancellazione derrata */
	@Override
	public Long deleteDerrata(Long derrataId) 
	{
		logger.info("Accesso alla classe DerrataDAOImpl - Cancellazione deleteDerrata con ID " + derrataId);
		logger.info("Ricerca derrata con ID scpecificato...");
		Optional<Derrata> optionalDerrata = derrataRepository.findById(derrataId);
		if(!optionalDerrata.isPresent())
			throw new GesevException("Nessuna derrata trovato con l'ID specificato", HttpStatus.BAD_REQUEST);
		
		logger.info("Derrata trovata. Cancellazione in corso...");
		Derrata derrata = optionalDerrata.get();
		if(derrata.getDettaglioMovimento() != null)
			throw new GesevException("Impossibile cancellare la derrata, poiche' e' associata ad un dettaglio movimento", HttpStatus.BAD_REQUEST);
		
		derrataRepository.delete(derrata);
		return derrataId;
	}

	/* Aggiorna Derrata */
	@Override
	public Long aggiornaDerrata(Derrata derrata, Long idDerrata)
	{
		logger.info("Accesso alla classe DerrataDAOImpl metodo aggiornaDerrata");
		
		Integer maxCodice = derrataRepository.getMaxDerrataId();
		if(idDerrata > maxCodice || idDerrata < 0)
		{
			logger.info("Impossibile modificare la derrata, codice non presente");
			throw new GesevException("Impossibile modificare la derrata, codice non presente", HttpStatus.BAD_REQUEST);
		}
		
		if(derrata == null || StringUtils.isBlank(derrata.getDescrizioneDerrata()) || StringUtils.isBlank(derrata.getUnitaMisura())
				|| derrata.getPrezzo() <= 0 || derrata.getQuantitaMinima() <= 0)
			throw new GesevException("Impossibile aggiornare la derrata, dati non validi", HttpStatus.BAD_REQUEST);
		
		logger.info("Controllo dell'unicita' della descrizione fornita...");
		Optional<Derrata> optionalDerrata = derrataRepository.findByDescrizioneDerrata(derrata.getDescrizioneDerrata());
		if(optionalDerrata.isPresent() && optionalDerrata.get().getDescrizioneDerrata().equalsIgnoreCase(derrata.getDescrizioneDerrata()) && 
				optionalDerrata.get().getDerrataId() != derrata.getDerrataId())
			throw new GesevException("La descrizione fornita risulta gia' associata ad un altra derrata oppure codice non presente", HttpStatus.BAD_REQUEST);
		
		logger.info("Aggiornamento in corso...");
		Derrata derrataMom = null;
		if(optionalDerrata.isPresent() && optionalDerrata.get().getDerrataId() == derrata.getDerrataId())
			derrataMom = optionalDerrata.get();
		else
		{
			Optional<Derrata> derrataCercata = derrataRepository.findById(derrata.getDerrataId());
			if(derrataCercata.isPresent())
				derrataMom = derrataCercata.get();
		}
		
		if(derrataMom == null)
			throw new GesevException("Nessuna derrata presente con l'ID " + derrata.getDerrataId(), HttpStatus.BAD_REQUEST);
		
		derrataMom.setDescrizioneDerrata(derrata.getDescrizioneDerrata());
		derrataMom.setUnitaMisura(derrata.getUnitaMisura());
		derrataMom.setPrezzo(derrata.getPrezzo());
		derrataMom.setQuantitaMinima(derrata.getQuantitaMinima());
		
		derrataRepository.save(derrataMom);
		
		logger.info("Fine aggiornamento");
		return derrata.getDerrataId();
	}
	
	/* Derrata per un Lotto */
	@Override
	public List<Derrata> cercaTipoDerrataConColonna(Map<String, String> mappa, Long idLotto)
	{
		
		logger.info("Controllo esistenza Derrata...");
	
		/* Controllo idLotto */
		if(idLotto == null)
			throw new GesevException("Il valore idLotto non e' valido", HttpStatus.BAD_REQUEST);
		
		Optional<TipoDerrata> optionalTipoDerrata = tipoDerrateRepositroy.findByCodice(idLotto);
		if(!optionalTipoDerrata.isPresent())
			throw new GesevException("Il valore idLotto non e' associato a nessun tipo derrata", HttpStatus.BAD_REQUEST);
		
		/* Crezione della query */
		logger.info("Composizione della query di ricerca...");
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Derrata> criteriaQuery = criteriaBuilder.createQuery(Derrata.class);
		Root<Derrata> derrataRoot = criteriaQuery.from(Derrata.class);
		
		/* predicato del lotto */ 
		Join<Derrata, TipoDerrata> derrataJoin = derrataRoot.join("tipoDerrata");
		
		/* predicato finale per la ricerca */
		Predicate finalPredicate = criteriaBuilder.equal(derrataJoin.get("codice"), idLotto); 	
		
		/* Switch colonna */
		for(Entry<String, String> entryMap : mappa.entrySet())
		{
			ColonneDerrataEnum colonnaEnum = null;
			String colonna = entryMap.getKey();
			String value = entryMap.getValue();
			try 
			{
				colonnaEnum = ColonneDerrataEnum.valueOf(colonna.toUpperCase());
				logger.info("Ricerca della derrata sulla base della colonna " + colonna.toUpperCase() + " e del valore " + value);
			} 
			catch (Exception e) 
			{
				throw new GesevException("Si e' verificato un errore. " + ExceptionUtils.getStackFrames(e)[0], HttpStatus.BAD_REQUEST);
			}
			
			if(StringUtils.isBlank(value))
				throw new GesevException("Il valore fornito per la colonna non e' valido", HttpStatus.BAD_REQUEST);
							
			switch(colonnaEnum)
			{
				case UNITA_MISURA:
					finalPredicate = criteriaBuilder.and(finalPredicate, criteriaBuilder.like(derrataRoot.get(ColonneDerrataEnum.UNITA_MISURA.getclonnaTipoDerrata()), value + "%"));
					break;
					
				case DESCRIZIONE:
					finalPredicate = criteriaBuilder.and(finalPredicate, criteriaBuilder.like(derrataRoot.get(ColonneDerrataEnum.DESCRIZIONE.getclonnaTipoDerrata()), value + "%"));
					break;
				
				case PREZZO:
					Double prezzo = null;
					try
					{
						prezzo = Double.valueOf(value);
					}
					catch(Exception e)
					{
						throw new GesevException("Impossibile convertire il valore prezzo", HttpStatus.BAD_REQUEST);
					}
					finalPredicate = criteriaBuilder.and(finalPredicate, criteriaBuilder.equal(derrataRoot.get(ColonneDerrataEnum.PREZZO.getclonnaTipoDerrata()), prezzo));
					break;
				
				case GIACENZA:
					Double giacenza = null;
					try
					{
						giacenza = Double.valueOf(value);
					}
					catch(Exception e)
					{
						throw new GesevException("Impossibile convertire il valore giacenza", HttpStatus.BAD_REQUEST);
					}
					finalPredicate = criteriaBuilder.and(finalPredicate, criteriaBuilder.equal(derrataRoot.get(ColonneDerrataEnum.GIACENZA.getclonnaTipoDerrata()), giacenza));
					break;
				
				case QUANTITA_MINIMA:
					Double quantitaMinima = null;
					try
					{
						quantitaMinima = Double.valueOf(value);
					}
					catch(Exception e)
					{
						throw new GesevException("Impossibile convertire il valore quantita minima", HttpStatus.BAD_REQUEST);
					}
					finalPredicate = criteriaBuilder.and(finalPredicate, criteriaBuilder.equal(derrataRoot.get(ColonneDerrataEnum.QUANTITA_MINIMA.getclonnaTipoDerrata()), quantitaMinima));
					break;
				
				case DATA_AGGIORNAMENTO_GIACENZA:
					Date dataAggiornamento = null;
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat(this.dateFormat);
					try
					{
						dataAggiornamento = simpleDateFormat.parse(value);
					}
					catch(Exception e)
					{
						throw new GesevException("Impossibile convertire il valore data aggiornamento giacenza", HttpStatus.BAD_REQUEST);
					}
					finalPredicate = criteriaBuilder.and(finalPredicate, criteriaBuilder.equal(derrataRoot.get(ColonneDerrataEnum.DATA_AGGIORNAMENTO_GIACENZA.getclonnaTipoDerrata()).as(java.sql.Date.class), dataAggiornamento));
					break;
			}
		}
		
		/* esecuzione query */
		criteriaQuery.where(finalPredicate);
		
		List<Derrata> items = entityManager.createQuery(criteriaQuery).getResultList();
		logger.info("Numero elementi trovati: " + items.size());
		
		/* restituzione */
		return items; 
	}
}
