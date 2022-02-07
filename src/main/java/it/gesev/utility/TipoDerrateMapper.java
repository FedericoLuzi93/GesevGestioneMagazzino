package it.gesev.utility;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import it.gesev.dto.TipoDerrataDTO;
import it.gesev.entities.TipoDerrata;

public class TipoDerrateMapper
{
	private static final Logger logger = LoggerFactory.getLogger(TipoDerrateMapper.class);
			
	//Entity to DTO
	public static TipoDerrataDTO mapToDTO(TipoDerrata tipoDerrata)
	{
		logger.info("Accesso alla classe TipODerrateMapper e al metodo mapToDTO");
		ModelMapper mapper = new ModelMapper();
		TipoDerrataDTO tipoDerrateDTO = mapper.map(tipoDerrata, TipoDerrataDTO.class);
		return tipoDerrateDTO;
	}
	
	//DTO to Entity
	public static TipoDerrata mapToEntity(TipoDerrataDTO tipoDerrataDTO)
	{
		logger.info("Accesso alla classe TipODerrateMapper e al metodo mapToEntity");
		ModelMapper mapper = new ModelMapper();
		TipoDerrata tipoDerrata = mapper.map(tipoDerrataDTO, TipoDerrata.class);
		return tipoDerrata;
	}
	

}
