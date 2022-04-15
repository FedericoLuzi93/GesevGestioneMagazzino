package it.gesev.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
	@PostMapping("/prelevamentoMensa")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Dati in ingresso non validi"),
			@ApiResponse(code = 500, message = "Errore interno") })
	public ResponseEntity<EsitoDTO> prelevamentoMensa(@RequestBody MovimentoDTO movimento)
	{
		logger.info("Invocato API service prelevamentoMensa");
		EsitoDTO esito = new EsitoDTO();
		HttpStatus status = null;
		try
		{
			movimentoService.prelevamentoMensa(movimento);
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
