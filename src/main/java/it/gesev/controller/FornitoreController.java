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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.gesev.dto.EsitoDTO;
import it.gesev.dto.FornitoreDTO;
import it.gesev.dto.RicercaColonnaDTO;
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
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"),
			               @ApiResponse(responseCode = "400", description = "Dati in ingresso non validi"),
			               @ApiResponse(responseCode = "500", description = "Errore interno") })
	public ResponseEntity<EsitoDTO> getFornitoreById(@PathVariable String codice)
	{
		logger.info("Invocato API service findFornitoreById");
		
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
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"),
                           @ApiResponse(responseCode = "400", description = "Dati in ingresso non validi"),
                           @ApiResponse(responseCode = "500", description = "Errore interno")})
	public ResponseEntity<EsitoDTO> getListaFornitori()
	{
		logger.info("Invocato API service getAllFornitore");
		
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
	
	@PostMapping("/addFornitore")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"),
                           @ApiResponse(responseCode = "400", description = "Dati in ingresso non validi"),
                           @ApiResponse(responseCode = "500", description = "Errore interno")})
	public ResponseEntity<EsitoDTO> addFornitore(@RequestBody FornitoreDTO fornitore)
	{
		logger.info("Invocato API service addFornitore");
		
		EsitoDTO esito = new EsitoDTO();
		HttpStatus status = null;
		
		try
		{
			List<FornitoreDTO> listaFornitori = fornitoreService.creaFornitore(fornitore.getDescrizione());
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
	
	@PostMapping("/deleteFornitore")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"),
                           @ApiResponse(responseCode = "400", description = "Dati in ingresso non validi"),
                           @ApiResponse(responseCode = "500", description = "Errore interno")})
	public ResponseEntity<EsitoDTO> deleteFornitore(@RequestBody FornitoreDTO fornitore)
	{
		logger.info("Invocato API service deleteFornintore");
		
		EsitoDTO esito = new EsitoDTO();
		HttpStatus status = null;
		
		try
		{
			List<FornitoreDTO> listaFornitori = fornitoreService.cancellaFornitore(fornitore.getCodice());
			esito.setMessaggio("Messaggio cancellato con successo");
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
	
	@PostMapping("/updateFornitore")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"),
                           @ApiResponse(responseCode = "400", description = "Dati in ingresso non validi"),
                           @ApiResponse(responseCode = "500", description = "Errore interno")})
	public ResponseEntity<EsitoDTO> updateFornitore(@RequestBody FornitoreDTO fornitore)
	{
		logger.info("Invocato API service updateFornitore");
		
		EsitoDTO esito = new EsitoDTO();
		HttpStatus status = null;
		
		try
		{
			List<FornitoreDTO> listaFornitori = fornitoreService.aggiornaFornitore(fornitore);
			esito.setBody(listaFornitori);
			esito.setMessaggio("Fornitore aggiornato correttamente");
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
	
	@PostMapping("/cercaPerColonna")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"),
                           @ApiResponse(responseCode = "400", description = "Dati in ingresso non validi"),
                           @ApiResponse(responseCode = "500", description = "Errore interno")})
	public ResponseEntity<EsitoDTO> updateFornitore(@RequestBody RicercaColonnaDTO ricerca)
	{
		logger.info("Invocato API service cercaPerColonna");
		
		EsitoDTO esito = new EsitoDTO();
		HttpStatus status = null;
		
		try
		{
			List<FornitoreDTO> listaForniitori = fornitoreService.cercaFornitorePerColonna(ricerca);
			esito.setBody(listaForniitori);
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
