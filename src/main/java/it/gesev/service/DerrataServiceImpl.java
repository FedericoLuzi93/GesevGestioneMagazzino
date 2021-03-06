package it.gesev.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import it.gesev.dao.DerrataDAO;
import it.gesev.dto.DerrataDTO;
import it.gesev.dto.MovimentoDTO;
import it.gesev.dto.RicercaColonnaDTO;
import it.gesev.entities.Derrata;
import it.gesev.exc.GesevException;
import it.gesev.utility.DerrataMapper;

@Service
public class DerrataServiceImpl implements DerrataService {

	@Autowired
	private DerrataDAO derrataDAO;
	
	
	@Value("${gesev.data.format}")
	private String dateFormat;
	
	private static final Logger logger = LoggerFactory.getLogger(DerrataServiceImpl.class);
	
	/* Leggi tutte le Derrata */
	public List<DerrataDTO> getAllDerrata(int tipoDerrataId) 
	{
		logger.info("Acceso alla classe DerrataServiceImpl - metodo getAllDerrata");
		List<Derrata> listaDerrata = derrataDAO.getAllDerrata(tipoDerrataId);
		List<DerrataDTO> listaDerrataDTO =  new ArrayList<>();
		for(Derrata dr : listaDerrata)
		{
			logger.info("Accesso alla classe DerrataServiceImpl - metodo getAllDerrata - ciclo for");			
			listaDerrataDTO.add(DerrataMapper.mapToDTO(dr, dateFormat));
		}
		logger.info("Accesso alla classe DerrataServiceImpl - metodo getAllDerrata - Fine del ciclo for");
		return listaDerrataDTO;
	}

	/* Create nuova derrata */
	@Override
	public int creaDerrata(DerrataDTO derrataDTO) 
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
		return derrataDAO.creaDerrata(derrata, derrataDTO.getTipoDerrataDTO().getCodice());
	}

	/* Cancella una derrata */
	@Override
	public int deleteDerrata(int derrataId) 
	{
		logger.info("Accesso alla classe DerrataServiceImpl - metodo deleteDerrata");
		derrataDAO.deleteDerrata(derrataId);
		logger.info("Accesso alla classe DerrataServiceImpl - Fine del metodo deleteDerrata");
		return derrataId;
	}

	/* Aggiorna una derrata */
	@Override
	public int aggiornaDerrata(DerrataDTO derrataDTO, int idDerrata) 
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
		derrataDAO.aggiornaDerrata(derrata, idDerrata);
		return derrata.getDerrataId();
	}

	/* Cerca una derrata VEDI!!! */
	@Override
	public List<DerrataDTO> cercaTipoDerrataConColonna(List<RicercaColonnaDTO> ricerca, int idLotto) 
	{
		List<RicercaColonnaDTO> listaRicerca = ricerca;
		logger.info("Accesso alla classe DerrateServiceIMPL - metodo cercaTipoDerrataConColonna");
		Map<String, String>	mappaRicerca = new HashMap<>();
		for(RicercaColonnaDTO rc : listaRicerca )
		{
			mappaRicerca.put(rc.getColonna(), rc.getValue());
		}
		List<Derrata> listaDerrata = derrataDAO.cercaTipoDerrataConColonna(mappaRicerca, idLotto);
		List<DerrataDTO> outputList = new ArrayList<>();
		
		for(Derrata derrata : listaDerrata)
			outputList.add(DerrataMapper.mapToDTO(derrata, dateFormat));
		
		return outputList;
	}

	


}
