package it.gesev.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DerrataDTO 
{
	private long derrataId;
	private String descrizioneDerrata;
	private String unitaMisura;
	private double prezzo;
	private int giacenza;
	private String dataAggiornamentoGiacenza;
	private int quantitaMinima;
	private int codiceMensa;
	private TipoDerrataDTO tipoDerrataDTO;
}
