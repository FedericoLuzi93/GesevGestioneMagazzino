package it.gesev.enums;

public enum ColonneDerrataEnum 
{
	
	DESCRIZIONE("descrizioneDerrata"),
	UNITA_MISURA("unitaMisura"),
	PREZZO("prezzo"),
	GIACENZA("giacenza"),
	QUANTITA_MINIMA("quantitaMinima"),
	DATA_AGGIORNAMENTO_GIACENZA("dataAggiornamentoGiacenza");
	
	
	private String clonnaDerrata;
	
	private ColonneDerrataEnum(String clonnaDerrata)
	{
		this.clonnaDerrata = clonnaDerrata;
	}

	public String getclonnaTipoDerrata() 
	{
		return clonnaDerrata;
	}

}
