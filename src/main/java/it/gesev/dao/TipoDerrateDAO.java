package it.gesev.dao;

import java.util.List;
import java.util.Map;

import it.gesev.entities.TipoDerrata;

public interface TipoDerrateDAO 
{
	public List<TipoDerrata> leggiTuttiTipiDerrate();
	public int createTipoDerrata(TipoDerrata tipoDerrata);
	public int deleteTipoDerrata(int codiceTipoDerrata);
	public int updateTipoDerrata(int codiceTipoDerrata, TipoDerrata tipoDerrata);
	public List<TipoDerrata> cercaTipoDerrataConColonna(Map<String, String> mappa);
}
