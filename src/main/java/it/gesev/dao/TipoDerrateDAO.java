package it.gesev.dao;

import java.util.List;

import it.gesev.entities.TipoDerrata;

public interface TipoDerrateDAO 
{
	public List<TipoDerrata> leggiTuttiTipiDerrate();
	public long createTipoDerrata(TipoDerrata tipoDerrata);
	public long deleteTipoDerrata(long codiceTipoDerrata);
	public long updateTipoDerrata(long codiceTipoDerrata, TipoDerrata tipoDerrata);
	public List<TipoDerrata> cercaTipoDerrataConColonna(String colonna, String valore);

}
