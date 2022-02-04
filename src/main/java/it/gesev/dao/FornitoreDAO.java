package it.gesev.dao;

import java.util.List;

import it.gesev.entities.Fornitore;

public interface FornitoreDAO {
	public Fornitore getFornitoreByCodice(Long codice);
	public List<Fornitore> getAllFornitore();

}
