package it.gesev.enums;

public enum ColonneTipoDerrataEnum 
{
	CODICE("codice"),
	DESCRIZIONE("descrizione");
	
	private String clonnaTipoDerrata;
	
	private ColonneTipoDerrataEnum(String clonnaTipoDerrata)
	{
		this.clonnaTipoDerrata = clonnaTipoDerrata;
	}

	public String getclonnaTipoDerrata() 
	{
		return clonnaTipoDerrata;
	}

}
