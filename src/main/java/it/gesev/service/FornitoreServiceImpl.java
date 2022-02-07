package it.gesev.service;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import it.gesev.dao.FornitoreDAO;
import it.gesev.dao.TestataMovimentoDAO;
import it.gesev.dto.DettaglioMovimentoDTO;
import it.gesev.dto.FornitoreDTO;
import it.gesev.dto.MovimentoDTO;
import it.gesev.dto.RicercaColonnaDTO;
import it.gesev.entities.DettaglioMovimento;
import it.gesev.entities.Fornitore;
import it.gesev.entities.TestataMovimento;
import it.gesev.exc.GesevException;

@Service
public class FornitoreServiceImpl implements FornitoreService {

	@Autowired
	private FornitoreDAO fornitoreDAO;
	
	@Autowired
	private TestataMovimentoDAO testataDAO;
	
	@Value("${gesev.data.format}")
	private String dataFormat;
	
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

	@Override
	public List<FornitoreDTO> creaFornitore(String descrizione) {
		logger.info("Avvio del service per la creazione di un nuovo fornitore...");
		
		if(StringUtils.isAllBlank(descrizione))
			throw new GesevException("Descrizione non valida", HttpStatus.BAD_REQUEST);
		
		Long nuovoFornitore = fornitoreDAO.creaFornitore(descrizione);
		if(nuovoFornitore == null || nuovoFornitore < 1)
			throw new GesevException("Errore nella creazione del fornitore", HttpStatus.INTERNAL_SERVER_ERROR);
		
		return getAllFornitore();
		
		
	}

	@Override
	public List<FornitoreDTO> cancellaFornitore(Long idFornitore) 
	{
		logger.info("Avvio service per cancellazione fornitore...");
		
		if(idFornitore == null)
			throw new GesevException("ID fornitore non valido", HttpStatus.BAD_REQUEST);
		
		fornitoreDAO.cancellaFornitore(idFornitore);
		
		return getAllFornitore();
		
	}

	@Override
	public List<FornitoreDTO> aggiornaFornitore(FornitoreDTO fornitore) {
		logger.info("Avvio del service per l'aggiornamento del fornitore...");
		
		ModelMapper mapper = new ModelMapper();
		fornitoreDAO.aggiornaFornitore(mapper.map(fornitore, Fornitore.class));
		
		return getAllFornitore();
	}

	@Override
	public List<FornitoreDTO> cercaFornitorePerColonna(RicercaColonnaDTO ricerca) {
		logger.info("Avvio del servizio per la ricerca in base alla colonna...");
		
		List<Fornitore> listaFornitori = fornitoreDAO.cercaFornitoreConColonna(ricerca.getColonna(), ricerca.getValue());
		List<FornitoreDTO> outputList = new ArrayList<>();
		
		ModelMapper mapper = new ModelMapper();
		for(Fornitore fornitore : listaFornitori)
			outputList.add(mapper.map(fornitore, FornitoreDTO.class));
		
		return outputList;
	}

	@Override
	public List<MovimentoDTO> cercaDettagliByFornitore(Long idFornitore) 
	{
		logger.info("Avvio del servizio per la ricerca delle testate del fornitore...");
		
		List<MovimentoDTO> listaMovimenti = new ArrayList<>();
		List<TestataMovimento> listaTestate = testataDAO.getDettaglioMovimentoByFornitore(idFornitore);
		SimpleDateFormat formatter = new SimpleDateFormat(this.dataFormat);
		DecimalFormat decimalFormatter = new DecimalFormat("###,###.00");
		
		/* conversione dei dati nella lista in uscita */
		for(TestataMovimento testata : listaTestate)
		{
			MovimentoDTO movimento = new MovimentoDTO();
			movimento.setIdTestata(StringUtils.leftPad(String.valueOf(testata.getNumOrdineLavoro()), 4, "0"));
			movimento.setDataTestata(formatter.format(testata.getData()));
			movimento.setTotaleTestata(decimalFormatter.format(testata.getTotaleImporto()));
			
			List<DettaglioMovimentoDTO> listaDettagli = new ArrayList<>();
			for(DettaglioMovimento dettaglio : testata.getListaDettaglioMovimento())
			{
				DettaglioMovimentoDTO dettaglioDTO = new DettaglioMovimentoDTO();
				dettaglioDTO.setCodiceDerrata(StringUtils.leftPad(String.valueOf(dettaglio.getDerrata().getDerrataId()), 4, "0"));
				dettaglioDTO.setDescrizioneDerrata(dettaglio.getDerrata().getDescrizioneDerrata());
				dettaglioDTO.setPrezzo(decimalFormatter.format(dettaglio.getPrezzoUnitario()));
				dettaglioDTO.setQuantita(decimalFormatter.format(dettaglio.getQuantitaEffettiva()));
				dettaglioDTO.setValore(decimalFormatter.format(dettaglio.getTotaleValore()));
				
				listaDettagli.add(dettaglioDTO);
			}
			
			movimento.setListaDettagli(listaDettagli);
			listaMovimenti.add(movimento);
		}
		
		return listaMovimenti;
		
	}

}
