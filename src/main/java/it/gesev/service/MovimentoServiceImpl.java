package it.gesev.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.gesev.dao.TestataMovimentoDAO;
import it.gesev.dto.MovimentoDTO;

@Service
public class MovimentoServiceImpl implements MovimentoService {

	@Autowired
	TestataMovimentoDAO testataDAO;
	
	private static final Logger logger = LoggerFactory.getLogger(MovimentoServiceImpl.class);
	
	@Override
	public void prelevamentoMensa(MovimentoDTO movimento) 
	{
		logger.info("Servizio prelevamento mensa");
		testataDAO.prelevamentoMensa(movimento.getListaDettagli(), movimento.getIdEnte());

	}

}
