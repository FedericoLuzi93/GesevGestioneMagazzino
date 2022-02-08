package it.gesev.enums;

public enum ColonneDerrataEnum 
{
	
	DESCRIZIONE("descrizioneDerrata"),
	UNITA_MISURA("unitaMisura"),
	PREZZO("prezzo"),
	GIACENZA("giacenza"),
	DATA("dataAggiornamentoGiacenza"),
	QUANTITA_MINIMA("quantitaMinima");
	
	
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