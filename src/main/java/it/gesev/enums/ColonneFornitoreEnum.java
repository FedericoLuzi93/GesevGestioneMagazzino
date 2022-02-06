package it.gesev.enums;

public enum ColonneFornitoreEnum 
{
	CODICE("codice"),
	DESCRIZIONE("descrizione");
	
	private String colonnaFornitore;
	
	private ColonneFornitoreEnum(String colonnaFornitore)
	{
		this.colonnaFornitore = colonnaFornitore;
	}

	public String getColonnaFornitore() {
		return colonnaFornitore;
	}
	
	
}
