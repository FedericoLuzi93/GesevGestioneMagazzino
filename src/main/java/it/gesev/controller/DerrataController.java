package it.gesev.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import it.gesev.dto.DerrataDTO;
import it.gesev.dto.EsitoDTO;
import it.gesev.dto.RicercaColonnaDTO;
import it.gesev.exc.GesevException;
import it.gesev.service.DerrataService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/derrata")
public class DerrataController 
{
	//test
	@Autowired
	private DerrataService derrataService;
	
	private static final Logger logger = LoggerFactory.getLogger(DerrataController.class);
	
	private final String MESSAGGIO_ERRORE_INTERNO = "Si e' verificato un errore interno";
	
	/* Leggi tutte Derrata */
	@GetMapping("leggiTutteDerrata/{tipoDerrataId}")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Dati in ingresso non validi"),
			@ApiResponse(code = 500, message = "Errore interno") })
	public ResponseEntity<EsitoDTO> leggiTutteDerrata(@PathVariable long tipoDerrataId)
	{
		logger.info("Invocato API service leggiTutteDerrata");
		EsitoDTO esito = new EsitoDTO();
		HttpStatus status = null;
		try
		{
			List<DerrataDTO> listaDerrataDTO = derrataService.getAllDerrata(tipoDerrataId);
			esito.setBody(listaDerrataDTO);
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
	
	/* Crea una nuova Derrata */
	@PostMapping("creaDerrata")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Dati in ingresso non validi"),
			@ApiResponse(code = 500, message = "Errore interno") })
	public ResponseEntity<EsitoDTO> creaDerrata(@RequestBody DerrataDTO derrataDTO)
	{
		logger.info("Invocato API service creaDerrata");
		EsitoDTO esito = new EsitoDTO();
		try
		{
			derrataService.creaDerrata(derrataDTO);
			esito.setStatus(HttpStatus.OK.value());
			esito.setMessaggio("INSERIMENTO AVVENUTO CON SUCCESSO");
//			esito.setBody(derrataService.getAllDerrata());
		}
		catch(GesevException exc)
		{
			logger.info("Eccezione nel servizio creaDerrata ", exc);
			esito.setStatus(exc.getStatus().value());
			esito.setMessaggio(exc.getMessage());
		}
		catch(Exception e)
		{
			logger.info("Eccezione nel servizio creaDerrata ", e);
			esito.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			esito.setMessaggio(MESSAGGIO_ERRORE_INTERNO);
		}
		return ResponseEntity.status(esito.getStatus()).body(esito);
	}
	
	/* Cancella una derrata */
	@DeleteMapping("/cancellaDerrata/{derrataId}")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Dati in ingresso non validi"),
			@ApiResponse(code = 500, message = "Errore interno") })
	public ResponseEntity<EsitoDTO> deleteTipoDerrata(@PathVariable long derrataId)
	{
		logger.info("Invocato API service derrataId");
		EsitoDTO esito = new EsitoDTO();
		try
		{
			derrataService.deleteDerrata(derrataId);
			esito.setStatus(HttpStatus.OK.value());
			esito.setMessaggio("CANCELLAZIONE AVVENUTA CON SUCCESSO");
//			esito.setBody(derrataService.getAllDerrata());;
		}
		catch(GesevException exc)
		{
			logger.info("Eccezione nel servizio deleteTipoDerrata ", exc);
			esito.setStatus(exc.getStatus().value());
			esito.setMessaggio(exc.getMessage());
		}
		catch(Exception e)
		{
			logger.info("Eccezione nel servizio deleteTipoDerrata ", e);
			esito.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			esito.setMessaggio(MESSAGGIO_ERRORE_INTERNO);
		}
		return ResponseEntity.status(esito.getStatus()).body(esito);
	}
	
	/* Aggiorna una derrata */
	@PutMapping("/updateDerrata/{idDerrata}")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Dati in ingresso non validi"),
			@ApiResponse(code = 500, message = "Errore interno") })
	public ResponseEntity<EsitoDTO> aggiornaDerrata(@RequestBody DerrataDTO derrataDTO, @PathVariable Long idDerrata)
	{
		logger.info("Invocato API service updateDerrata");
		EsitoDTO esito = new EsitoDTO();
		try
		{
			derrataService.aggiornaDerrata(derrataDTO, idDerrata);
			esito.setStatus(HttpStatus.OK.value());
			esito.setMessaggio("AGGIORNAMENTO AVVENUTO CON SUCCESSO");
//			esito.setBody(derrataService.getAllDerrata());;
		}
		catch(GesevException exc)
		{
			logger.info("Eccezione nel servizio updateDerrata ", exc);
			esito.setStatus(exc.getStatus().value());
			esito.setMessaggio(exc.getMessage());
		}
		catch(Exception e)
		{
			logger.info("Eccezione nel servizio updateDerrata ", e);
			esito.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			esito.setMessaggio(MESSAGGIO_ERRORE_INTERNO);
		}
		return ResponseEntity.status(esito.getStatus()).body(esito);
	}
	
	/* Cerca derrata per Colonna */
	@PostMapping("/cercaPerColonna/{idLotto}")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Dati in ingresso non validi"),
			@ApiResponse(code = 500, message = "Errore interno") })
	public ResponseEntity<EsitoDTO> cercaDerrataPerColonna(@RequestBody List<RicercaColonnaDTO> ricerca, @PathVariable Long idLotto)
	{
		logger.info("Invocato API service cercaPerColonna");
		EsitoDTO esito = new EsitoDTO();
		HttpStatus status = null;
		try
		{
			List<DerrataDTO> listaDerrataDTO = derrataService.cercaTipoDerrataConColonna(ricerca, idLotto);
			esito.setBody(listaDerrataDTO);
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
