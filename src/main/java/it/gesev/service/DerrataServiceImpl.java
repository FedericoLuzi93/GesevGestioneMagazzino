package it.gesev.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import it.gesev.dao.DerrataDAO;
import it.gesev.dto.DerrataDTO;
import it.gesev.dto.RicercaColonnaDTO;
import it.gesev.dto.TipoDerrataDTO;
import it.gesev.entities.Derrata;
import it.gesev.entities.TipoDerrata;
import it.gesev.exc.GesevException;
import it.gesev.utility.DerrataMapper;
import it.gesev.utility.TipoDerrateMapper;

@Service
public class DerrataServiceImpl implements DerrataService {

	@Autowired
	private DerrataDAO derrataDAO;
	
	private static final Logger logger = LoggerFactory.getLogger(DerrataServiceImpl.class);
	
	/* Leggi tutte le Derrata */
	public List<DerrataDTO> getAllDerrata(long tipoDerrataId) 
	{
		logger.info("Acceso alla classe DerrataServiceImpl - metodo getAllDerrata");
		List<Derrata> listaDerrata = derrataDAO.getAllDerrata(tipoDerrataId);
		List<DerrataDTO> listaDerrataDTO =  new ArrayList<>();
		for(Derrata dr : listaDerrata)
		{
			logger.info("Accesso alla classe DerrataServiceImpl - metodo getAllDerrata - ciclo for");
			listaDerrataDTO.add(DerrataMapper.mapToDTO(dr));	
		}
		logger.info("Accesso alla classe DerrataServiceImpl - metodo getAllDerrata - Fine del ciclo for");
		return listaDerrataDTO;
	}

	/* Create nuova derrata */
	@Override
	public Long creaDerrata(DerrataDTO derrataDTO) 
	{
		Derrata derrata = null;
		try
		{
			logger.info("Accesso alla classe TipODerrateServiceIMPL - metodo creaDerrata");
			derrata = DerrataMapper.mapToEntity(derrataDTO);
		}
		catch(GesevException exc)
		{
			logger.info("Eccezione nel servizio creaDerrata" + exc);
			throw new GesevException("Non è stato possibile inserire la derrata" + exc, HttpStatus.BAD_REQUEST);
		}
		logger.info("Fine del metodo createTipoDerrata - creazione in corso...");
		return derrataDAO.creaDerrata(derrata);
	}

	/* Cancella una derrata */
	@Override
	public Long deleteDerrata(Long derrataId) 
	{
		logger.info("Accesso alla classe DerrataServiceImpl - metodo deleteDerrata");
		derrataDAO.deleteDerrata(derrataId);
		logger.info("Accesso alla classe DerrataServiceImpl - Fine del metodo deleteDerrata");
		return derrataId;
	}

	/* Aggiorna una derrata */
	@Override
	public Long aggiornaDerrata(DerrataDTO derrataDTO) 
	{
		Derrata derrata = null;
		try
		{
			logger.info("Accesso alla classe TipODerrateServiceIMPL - metodo aggiornaDerrata");
			derrata = DerrataMapper.mapToEntity(derrataDTO);
		}
		catch(GesevException exc)
		{
			logger.info("Eccezione nel servizio aggiornaDerrata" + exc);
			throw new GesevException("Non è stato possibile aggiornare la derrata" + exc, HttpStatus.BAD_REQUEST);
		}
		logger.info("Fine del metodo aggiornaDerrata - aggiornamento in corso...");
		derrataDAO.aggiornaDerrata(derrata);
		return derrata.getDerrataId();
	}

	/* Cerca una derrata */
	@Override
	public List<DerrataDTO> cercaTipoDerrataConColonna(RicercaColonnaDTO ricerca) 
	{
		logger.info("Accesso alla classe DerrateServiceIMPL - metodo cercaTipoDerrataConColonna");
		
		List<Derrata> listaDerrata = derrataDAO.cercaTipoDerrataConColonna(ricerca.getColonna(), ricerca.getValue());
		List<DerrataDTO> outputList = new ArrayList<>();
		
		ModelMapper mapper = new ModelMapper();
		for(Derrata derrata : listaDerrata)
			outputList.add(mapper.map(derrata, DerrataDTO.class));
		
		return outputList;
	}

}
