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
	private List<DettaglioMovimentoDTO> listaDettagli;
}
