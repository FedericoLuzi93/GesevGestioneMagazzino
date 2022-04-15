package it.gesev.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MovimentoDTO 
{
	private String idTestata;
	private String dataTestata;
	private String totaleTestata;
	private Integer idEnte;
	private List<DettaglioMovimentoDTO> listaDettagli;
}
