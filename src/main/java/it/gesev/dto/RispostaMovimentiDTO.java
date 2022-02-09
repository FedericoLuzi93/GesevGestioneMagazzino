package it.gesev.dto;

import java.util.List;
import java.util.Set;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RispostaMovimentiDTO 
{
	private List<MovimentoDTO> listaMovimenti;
	private Set<String> listaDerrateFornitore;
}
