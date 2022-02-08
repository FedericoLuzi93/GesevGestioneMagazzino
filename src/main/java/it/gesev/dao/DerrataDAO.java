package it.gesev.dao;

import java.util.List;

import it.gesev.entities.Derrata;

public interface DerrataDAO 
{
	/* lista della derrata in base al lotto */
	
	
	public List<Derrata> getAllDerrata(long tipoDerrataId);
	public Long creaDerrata(Derrata derrata, int codiceTipoDerrata);
	public Long deleteDerrata(Long derrataId);
	public Long aggiornaDerrata(Derrata derrata);
	public List<Derrata> cercaTipoDerrataConColonna(String colonna, String value, long idLotto);
	
}

