package it.gesev.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import it.gesev.dao.FornitoreDAO;
import it.gesev.dto.FornitoreDTO;
import it.gesev.entities.Fornitore;
import it.gesev.exc.GesevException;

@Service
public class FornitoreServiceImpl implements FornitoreService {

	@Autowired
	private FornitoreDAO fornitoreDAO;
	
	private static final Logger logger = LoggerFactory.getLogger(FornitoreServiceImpl.class);
	
	@Override
	public FornitoreDTO getFornitoreByCodice(String codice) 
	{
		logger.info("Avvio del service di ricerca del fornitore sulla base dell'id");
		Long codiceConvertito = null;
		
		try
		{
			codiceConvertito = Long.valueOf(codice);
		}
		
		catch(Exception ex)
		{
			logger.info("Errore nei dati forniti per la ricerca");
			throw new GesevException("Errore nei dati forniti per la ricerca", HttpStatus.BAD_REQUEST);
		}
		
		Fornitore fornitore = fornitoreDAO.getFornitoreByCodice(codiceConvertito);
		
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(fornitore, FornitoreDTO.class);
	}

	@Override
	public List<FornitoreDTO> getAllFornitore() 
	{
		logger.info("Avvio del service di ricerca di tutti i fornitori");
		List<FornitoreDTO> outputList = new ArrayList<>();
		ModelMapper mapper = new ModelMapper();
		
		List<Fornitore> listaFornitori = fornitoreDAO.getAllFornitore();
		for(Fornitore fornitore : listaFornitori)
			outputList.add(mapper.map(fornitore, FornitoreDTO.class));
		
		return outputList;
		
	}

}
