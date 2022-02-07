package it.gesev.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import it.gesev.entities.TestataMovimento;
import it.gesev.exc.GesevException;
import it.gesev.repository.TestataMovimentoRepository;

@Component
public class TestataMovimentoDAOImpl implements TestataMovimentoDAO {

	@Autowired
	private TestataMovimentoRepository testataMovimentoRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(TestataMovimentoDAOImpl.class);
	
	@Override
	public List<TestataMovimento> getDettaglioMovimentoByFornitore(Long idFornitore) 
	{
		logger.info("Ricerca testata movimento con ID fornitore " + idFornitore);
		
		List<TestataMovimento> testataMovimento = testataMovimentoRepository.getTestataMovimentoByIdFornitore(idFornitore);
		if(testataMovimento.size() > 0)
			return testataMovimento;
		
		else
			throw new GesevException("Nessun fornitore presente con l'ID fornito", HttpStatus.BAD_REQUEST);
	}

}
