package it.gesev.utility;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.gesev.dto.DerrataDTO;
import it.gesev.dto.TipoDerrataDTO;
import it.gesev.entities.Derrata;
import it.gesev.entities.TipoDerrata;

public class DerrataMapper 
{
	private static final Logger logger = LoggerFactory.getLogger(DerrataMapper.class);
	
	//Entity to DTO
	public static DerrataDTO mapToDTO(Derrata derrata)
	{
		logger.info("Accesso alla classe DerrataMapper e al metodo mapToDTO");
		ModelMapper mapper = new ModelMapper();
		DerrataDTO DerrataDTO = mapper.map(derrata, DerrataDTO.class);
		return DerrataDTO;
	}
	
	//DTO to Entity
	public static Derrata mapToEntity(DerrataDTO derrataDTO)
	{
		logger.info("Accesso alla classe DerrataMapper e al metodo mapToEntity");
		ModelMapper mapper = new ModelMapper();
		Derrata derrata = mapper.map(derrataDTO, Derrata.class);
		return derrata;
	}

}
