package it.gesev.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import it.gesev.dao.TipoDerrateDAO;
import it.gesev.dto.RicercaColonnaDTO;
import it.gesev.dto.TipoDerrataDTO;
import it.gesev.entities.TipoDerrata;
import it.gesev.exc.GesevException;
import it.gesev.utility.TipoDerrateMapper;

@Service
public class TipoDerrateServiceImpl implements TipoDerrateService
{
	@Autowired
	private TipoDerrateDAO tipoDerrateDAO;
	
	private static final Logger logger = LoggerFactory.getLogger(TipoDerrateServiceImpl.class);
	
	/* Leggi tutti i tipi derrata */
	public List<TipoDerrataDTO> leggiTuttiTipiDerrate() 
	{
		logger.info("Acceso alla classe TipODerrateServiceIMPL - metodo leggiTuttiTipiDerrate");
		List<TipoDerrata> listaTipoDerrata = tipoDerrateDAO.leggiTuttiTipiDerrate();
		List<TipoDerrataDTO> listaTipoDerrataDTO =  new ArrayList<>();
		for(TipoDerrata td : listaTipoDerrata)
		{
			logger.info("Accesso alla classe TipODerrateServiceIMPL - metodo leggiTuttiTipiDerrate - ciclo for");
			listaTipoDerrataDTO.add(TipoDerrateMapper.mapToDTO(td));	
		}
		logger.info("Accesso alla classe TipODerrateServiceIMPL - metodo leggiTuttiTipiDerrate - Fine del ciclo for");
		return listaTipoDerrataDTO;
	}

	/* Crea un tipo derrata */
	public long createTipoDerrata(TipoDerrataDTO tipoDerrataDTO) 
	{
		TipoDerrata tipoDerrata = null;
		try
		{
			logger.info("Accesso alla classe TipODerrateServiceIMPL - metodo createTipoDerrata");
			tipoDerrata = TipoDerrateMapper.mapToEntity(tipoDerrataDTO);	
		}
		catch(GesevException exc)
		{
			logger.info("Eccezione nel servizio createTipoDerrata" + exc);
			throw new GesevException("Non è stato possibile inserire il TipoDerrata" + exc, HttpStatus.BAD_REQUEST);
		}
		logger.info("Accesso alla classe TipODerrateServiceIMPL - Fine del metodo createTipoDerrata");
		return tipoDerrateDAO.createTipoDerrata(tipoDerrata);
	}

	/* Cancella un tipo derrata by Codice */
	public long deleteTipoDerrata(long codiceTipoDerrata) 
	{
		logger.info("Accesso alla classe TipODerrateServiceIMPL - metodo deleteTipoDerrata");
		tipoDerrateDAO.deleteTipoDerrata(codiceTipoDerrata);
		logger.info("Accesso alla classe TipODerrateServiceIMPL - Fine del metodo deleteTipoDerrata");
		return 1;
	}

	/* Update un tipo derrata */
	public long updateTipoDerrata(int codiceTipoDerrata, TipoDerrataDTO tipoDerrataDTO) 
	{
		TipoDerrata tipoDerrata = null;
		try
		{
			logger.info("Accesso alla classe TipODerrateServiceIMPL - metodo updateTipoDerrata");
			tipoDerrata = TipoDerrateMapper.mapToEntity(tipoDerrataDTO);	
		}
		catch(GesevException exc)
		{
			logger.info("Eccezione nel servizio updateTipoDerrata" + exc);
			throw new GesevException("Non è stato possibile modificare il TipoDerrata" + exc, HttpStatus.BAD_REQUEST);
		}
		logger.info("Accesso alla classe TipODerrateServiceIMPL - Fine del metodo updateTipoDerrata");
		return tipoDerrateDAO.updateTipoDerrata(codiceTipoDerrata, tipoDerrata);
	}

	/* Cerca tipo derrata */
	@Override
	public List<TipoDerrataDTO> cercaTipoDerrataConColonna(List<RicercaColonnaDTO> ricerca) 
	{
		logger.info("Accesso alla classe TipODerrateServiceIMPL - metodo cercaTipoDerrataConColonna");
		List<RicercaColonnaDTO> listaRicerca = ricerca;
		Map<String, String>	mappaRicerca = new HashMap<>();
		for(RicercaColonnaDTO rc : listaRicerca )
		{
			mappaRicerca.put(rc.getColonna(), rc.getValue());
		}
		List<TipoDerrata> listaTipoDerrata = tipoDerrateDAO.cercaTipoDerrataConColonna(mappaRicerca);
		List<TipoDerrataDTO> outputList = new ArrayList<>();
		
		ModelMapper mapper = new ModelMapper();
		for(TipoDerrata tipoDerrata : listaTipoDerrata)
			outputList.add(mapper.map(tipoDerrata, TipoDerrataDTO.class));
		
		return outputList;
	}
}
