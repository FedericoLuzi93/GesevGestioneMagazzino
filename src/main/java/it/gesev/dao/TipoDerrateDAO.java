package it.gesev.dao;

import java.util.List;
import java.util.Map;

import it.gesev.entities.TipoDerrata;

public interface TipoDerrateDAO 
{
	public List<TipoDerrata> leggiTuttiTipiDerrate();
	public long createTipoDerrata(TipoDerrata tipoDerrata);
	public long deleteTipoDerrata(long codiceTipoDerrata);
	public long updateTipoDerrata(int codiceTipoDerrata, TipoDerrata tipoDerrata);
	public List<TipoDerrata> cercaTipoDerrataConColonna(Map<String, String> mappa);
}
