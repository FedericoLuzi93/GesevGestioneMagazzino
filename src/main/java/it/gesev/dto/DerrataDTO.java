package it.gesev.dto;

import java.util.Date;

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
	private Date dataAggiornamentoGiacenza;
	private int quantitaMinima;
	private int codiceMensa;
}
