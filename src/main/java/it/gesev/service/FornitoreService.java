package it.gesev.service;

import java.util.List;

import it.gesev.dto.FornitoreDTO;

public interface FornitoreService {
	public FornitoreDTO getFornitoreByCodice(String codice);
	public List<FornitoreDTO> getAllFornitore();
	public List<FornitoreDTO> creaFornitore(String descrizione);
	public List<FornitoreDTO> cancellaFornitore(Long idFornitore);
	public List<FornitoreDTO> aggiornaFornitore(FornitoreDTO fornitore);
}
