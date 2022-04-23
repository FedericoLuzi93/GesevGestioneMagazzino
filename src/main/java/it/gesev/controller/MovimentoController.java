package it.gesev.controller;


import java.text.SimpleDateFormat;
import java.util.Date;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import it.gesev.dto.DerrataDTO;
import it.gesev.dto.EsitoDTO;
import it.gesev.dto.MovimentoDTO;
import it.gesev.exc.GesevException;
import it.gesev.service.MovimentoService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/movimento")
public class MovimentoController 
{
	@Autowired
	private MovimentoService movimentoService;
	
	private static final Logger logger = LoggerFactory.getLogger(MovimentoController.class);
	private final String MESSAGGIO_ERRORE_INTERNO = "Si e' verificato un errore interno";
	
	/* Leggi tutte Derrata */
	@PostMapping("/downloadDettaglioPrelevamento/{idTestata}")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Dati in ingresso non validi"),
			@ApiResponse(code = 500, message = "Errore interno") })
	public ResponseEntity<Resource> prelevamentoMensa(@PathVariable("idTestata") Integer idTestata)
	{
		logger.info("Invocato API service prelevamentoMensa");
		HttpHeaders headers = new HttpHeaders();
		SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy_HH_mm");
		headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + "prelevamento_" + formatter.format(new Date()) + ".pdf");
		headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
		
		ResponseEntity<Resource> result = null;
		
		try
		{
			byte[] fileBytes = movimentoService.downloadDettaglioPrelevamento(idTestata);
			result = ResponseEntity.ok().headers(headers).contentLength(fileBytes.length)
					.contentType(MediaType.APPLICATION_OCTET_STREAM).body(new ByteArrayResource(fileBytes));
		}
		
		catch(GesevException gex)
		{
			logger.info("Si e' verificata un'eccezione", gex);
			result = ResponseEntity.badRequest().body(null);
			
		}
		catch(Exception ex)
		{
			logger.info("Si e' verificata un'eccezione interna", ex);
			result = ResponseEntity.internalServerError().body(null);
			
		}
		
		return result;
		
	}
	
	@PostMapping("prelevamentoMensa")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Dati in ingresso non validi"),
			@ApiResponse(code = 500, message = "Errore interno") })
	public ResponseEntity<EsitoDTO> prelevamentoMensa(@RequestBody MovimentoDTO movimento)
	{
		logger.info("Invocato API service creaDerrprelevamentoMensaata");
		EsitoDTO esito = new EsitoDTO();
		HttpStatus status = null;
		
		try
		{
			esito.setBody(movimentoService.prelevamentoMensa(movimento));
			status = HttpStatus.OK;
		}
		
		catch(GesevException exc)
		{
			logger.info("Eccezione nel servizio creaDerrata ", exc);
			status = exc.getStatus();
			esito.setMessaggio(exc.getMessage());
		}
		catch(Exception e)
		{
			logger.info("Eccezione nel servizio creaDerrata ", e);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			esito.setMessaggio(MESSAGGIO_ERRORE_INTERNO);
		}
		
		return ResponseEntity.status(status).body(esito);
	}
	
}
