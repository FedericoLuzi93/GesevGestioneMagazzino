package it.gesev.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DettaglioMovimentoDTO 
{
	private String quantita;
	private String prezzo;
	private String valore;
	private String codiceDerrata;
	private String descrizioneDerrata;
}
