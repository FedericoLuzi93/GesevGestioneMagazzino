package it.gesev.dao;

import java.util.List;
import java.util.Map;

import it.gesev.entities.Fornitore;

public interface FornitoreDAO {
	public Fornitore getFornitoreByCodice(Long codice);
	public List<Fornitore> getAllFornitore();
	public Long creaFornitore(String descrizione);
	public List<Fornitore> cercaFornitoreConColonna(Map<String, String> mappaColonne);
	public void cancellaFornitore(Long idFornitore);
	public void aggiornaFornitore(Fornitore fornitore);

}
