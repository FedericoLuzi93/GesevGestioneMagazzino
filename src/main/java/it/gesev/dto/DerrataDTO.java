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
	private double giacenza;
	private String dataAggiornamentoGiacenza;
	private double quantitaMinima;
	private int codiceMensa;
	private TipoDerrataDTO tipoDerrataDTO;
	
	/* dati per generazione report */
	private double quantitaImpiegate;
	private double totaleValore;
}
