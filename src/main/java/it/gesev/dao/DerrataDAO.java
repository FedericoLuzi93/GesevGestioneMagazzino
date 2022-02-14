package it.gesev.dao;

import java.util.List;
import java.util.Map;

import it.gesev.entities.Derrata;

public interface DerrataDAO 
{
	public List<Derrata> getAllDerrata(long tipoDerrataId);
	public Long creaDerrata(Derrata derrata, int codiceTipoDerrata);
	public Long deleteDerrata(Long derrataId);
	public Long aggiornaDerrata(Derrata derrata, Long idDerrata);
	public List<Derrata> cercaTipoDerrataConColonna(Map<String, String> mappa, Long idLotto);	
}

