package it.gesev.dao;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import it.gesev.entities.Fornitore;
import it.gesev.exc.GesevException;
import it.gesev.repository.FornitoreRepository;

@Component
public class FornitoreDAOImpl implements FornitoreDAO {

	private final Logger logger = LoggerFactory.getLogger(FornitoreDAOImpl.class);
	
	@Autowired
	FornitoreRepository fornitoreRepository;
	
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

}
