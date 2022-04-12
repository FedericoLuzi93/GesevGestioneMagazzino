package it.gesev.controller;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import it.gesev.dto.EsitoDTO;
import it.gesev.dto.RicercaColonnaDTO;
import it.gesev.dto.TipoDerrataDTO;
import it.gesev.exc.GesevException;
import it.gesev.service.TipoDerrateService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/tipoDerrate")

public class TipoDerrateController 
{
	@Autowired
	private TipoDerrateService tipoDerrateService;
	
	private static final Logger logger = LoggerFactory.getLogger(TipoDerrateController.class);
	
	private final String MESSAGGIO_ERRORE_INTERNO = "Si e' verificato un errore interno";
	
	/* Leggi tutte le Derrate */
	@GetMapping("/leggiTuttiTipiDerrate")
	public List<TipoDerrataDTO> leggiTuttiTipiDerrate()
	{
		logger.info("Accesso alla classe TipoDerrateController - metodo leggiTuttiTipiDerrate");
		return tipoDerrateService.leggiTuttiTipiDerrate();
	}
			
	/* Crea una Derrata con Eccezione */
	@PostMapping("/creaTipoDerrata")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Dati in ingresso non validi"),
			@ApiResponse(code = 500, message = "Errore interno") })
	public ResponseEntity<EsitoDTO> creaTipoDerrata(@RequestBody TipoDerrataDTO tipoDerrataDTO)
	{
		logger.info("Accesso alla classe TipoDerrateController - metodo creaTipoDerrata");
		EsitoDTO esito = new EsitoDTO();
		try
		{
			tipoDerrateService.createTipoDerrata(tipoDerrataDTO);
			esito.setStatus(HttpStatus.OK.value());
			esito.setMessaggio("INSERIMENTO AVVENUTO CON SUCCESSO");
			esito.setBody(tipoDerrateService.leggiTuttiTipiDerrate());
		}
		catch(GesevException exc)
		{
			logger.info("Eccezione nel servizio creaTipoDerrata ", exc);
			esito.setStatus(exc.getStatus().value());
			esito.setMessaggio(exc.getMessage());
		}
		catch(Exception e)
		{
			logger.info("Eccezione nel servizio creaTipoDerrata ", e);
			esito.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			esito.setMessaggio(MESSAGGIO_ERRORE_INTERNO);
		}
		return ResponseEntity.status(esito.getStatus()).body(esito);
	}
	
	/* Cancella un tipo derrata by Codice */
	@DeleteMapping("/cancellaTipoDerrata/{codiceTipoDerrata}")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Dati in ingresso non validi"),
			@ApiResponse(code = 500, message = "Errore interno") })
	public ResponseEntity<EsitoDTO> deleteTipoDerrata(@PathVariable int codiceTipoDerrata)
	{
		logger.info("Accesso alla classe TipoDerrateController - metodo deleteTipoDerrata");
		EsitoDTO esito = new EsitoDTO();
		try
		{
			tipoDerrateService.deleteTipoDerrata(codiceTipoDerrata);
			esito.setStatus(HttpStatus.OK.value());
			esito.setMessaggio("CANCELLAZIONE AVVENUTA CON SUCCESSO");
			esito.setBody(tipoDerrateService.leggiTuttiTipiDerrate());
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
	
	/* Aggiorna una Derrata */
	@PutMapping("/updateTipoDerrata/{codiceTipoDerrata}")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Dati in ingresso non validi"),
			@ApiResponse(code = 500, message = "Errore interno") })
	public ResponseEntity<EsitoDTO> updateTipODerrata(@PathVariable int codiceTipoDerrata, @RequestBody TipoDerrataDTO tipoDerrataDTO)
	{
		logger.info("Accesso alla classe TipoDerrateController - metodo updateTipODerrata");
		EsitoDTO esito = new EsitoDTO();
		try
		{
			tipoDerrateService.updateTipoDerrata(codiceTipoDerrata, tipoDerrataDTO);
			esito.setStatus(HttpStatus.OK.value());
			esito.setMessaggio("MODIFICA AVVENUTA CON SUCCESSO");
			esito.setBody(tipoDerrateService.leggiTuttiTipiDerrate());
		}
		catch(GesevException exc)
		{
			logger.info("Eccezione nel servizio updateTipODerrata ", exc);
			esito.setStatus(exc.getStatus().value());
			esito.setMessaggio(exc.getMessage());
		}
		catch(Exception e)
		{
			logger.info("Eccezione nel servizio updateTipODerrata ", e);
			esito.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			esito.setMessaggio(MESSAGGIO_ERRORE_INTERNO);
		}
		return ResponseEntity.status(esito.getStatus()).body(esito);	
	}
	
	/* Cerca tipo derrata per Colonna */
	@PostMapping("/cercaPerColonna")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Dati in ingresso non validi"),
			@ApiResponse(code = 500, message = "Errore interno") })
	public ResponseEntity<EsitoDTO> cercaTipoDerrataPerColonna(@RequestBody List<RicercaColonnaDTO> ricerca)
	{
		logger.info("Invocato API service cercaPerColonna");
		
		EsitoDTO esito = new EsitoDTO();
		HttpStatus status = null;
		
		try
		{
			List<TipoDerrataDTO> listaTipoDerrataDTO = tipoDerrateService.cercaTipoDerrataConColonna(ricerca);
			esito.setBody(listaTipoDerrataDTO);
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
	
	@PostMapping(value = "/stampaDerrate")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Richiesta download elenco derrate fallita"),
			@ApiResponse(code = 400, message = "Dati in ingresso non validi"),
			@ApiResponse(code = 500, message = "Errore interno") })
	public ResponseEntity<Resource> stampaDerrate(@RequestBody TipoDerrataDTO tipoDerrata) throws ParseException, FileNotFoundException
	{
		logger.info("Accesso al servizio stampaDerrate");
		HttpHeaders headers = new HttpHeaders();
		ResponseEntity<Resource> esito = null;

		try 
		{
			byte[] fileContent = tipoDerrateService.getStampaDerrata(tipoDerrata.getCodice());
			String fileName = tipoDerrata.getCodice() != null ? "derrate_lotto_" + StringUtils.leftPad(String.valueOf(tipoDerrata.getCodice()), 4, "0") :
			                  "derrate_lotto";
			
			
			headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName + ".pdf");
			headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);

			esito =  ResponseEntity.ok().headers(headers).contentLength(fileContent.length)
					.contentType(MediaType.APPLICATION_OCTET_STREAM).body(new ByteArrayResource(fileContent));
		} 
		
		catch(GesevException gex)
		{
			logger.info("Si e' verificata un'eccezione", gex);
		
		}
		
		
		catch(Exception ex)
		{
			logger.info("Si e' verificata un'eccezione interna", ex);
		}
		
		return esito;
	}
}
