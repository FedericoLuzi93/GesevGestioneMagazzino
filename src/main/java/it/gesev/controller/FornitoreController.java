package it.gesev.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.gesev.dto.EsitoDTO;
import it.gesev.dto.FornitoreDTO;
import it.gesev.exc.GesevException;
import it.gesev.service.FornitoreService;

@RestController
@RequestMapping("/fornitore")
public class FornitoreController 
{
	@Autowired
	private FornitoreService fornitoreService;
	
	private static final Logger logger = LoggerFactory.getLogger(FornitoreController.class);
	private final String MESSAGGIO_ERRORE_INTERNO = "Errore interno";
	
	@GetMapping("/findFornitoreById/{codice}")
	public ResponseEntity<EsitoDTO> getFornitoreById(@PathVariable String codice)
	{
		EsitoDTO esito = new EsitoDTO();
		HttpStatus status = null;
		try
		{
			FornitoreDTO fornitore = fornitoreService.getFornitoreByCodice(codice);
			esito.setBody(fornitore);
			esito.setStatus(HttpStatus.OK.value());
			status = HttpStatus.OK;
		}
		
		catch(GesevException gex)
		{
			logger.info("Si e' verificata un'eccezione", gex);
			
			esito.setStatus(gex.getStatus().value());
			esito.setMessaggio(gex.getMessage());
			status = gex.getStatus();
		}
		
		catch(Exception ex)
		{
			logger.info("Si e' verificata un'eccezione interna", ex);
			
			esito.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			esito.setMessaggio(MESSAGGIO_ERRORE_INTERNO);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			
		}
		
		return ResponseEntity.status(status).headers(new HttpHeaders()).body(esito);
	}
	
	@GetMapping("/getAllFornitore")
	public ResponseEntity<EsitoDTO> getListaFornitori()
	{
		EsitoDTO esito = new EsitoDTO();
		HttpStatus status = null;
		
		try
		{
			List<FornitoreDTO> listaFornitori = fornitoreService.getAllFornitore();
			esito.setBody(listaFornitori);
			status = HttpStatus.OK;
		}
		
		catch(GesevException gex)
		{
			logger.info("Si e' verificata un'eccezione", gex);
			
			esito.setMessaggio(gex.getMessage());
			status = gex.getStatus();
		}
		
		catch(Exception ex)
		{
			logger.info("Si e' verificata un'eccezione interna", ex);
			
			esito.setMessaggio(MESSAGGIO_ERRORE_INTERNO);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			
		}
		
		esito.setStatus(status.value());
		return ResponseEntity.status(status).headers(new HttpHeaders()).body(esito);
	}
}
