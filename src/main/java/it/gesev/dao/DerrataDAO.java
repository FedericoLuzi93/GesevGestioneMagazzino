package it.gesev.dao;

import java.util.List;
import java.util.Map;

import it.gesev.entities.Derrata;

public interface DerrataDAO 
{
	public List<Derrata> getAllDerrata(int tipoDerrataId);
	public int creaDerrata(Derrata derrata, int codiceTipoDerrata);
	public int deleteDerrata(int derrataId);
	public int aggiornaDerrata(Derrata derrata, int idDerrata);
	public List<Derrata> cercaTipoDerrataConColonna(Map<String, String> mappa, int idLotto);	
}

