package it.gesev.service;

import java.util.List;

import it.gesev.dto.FornitoreDTO;

public interface FornitoreService {
	public FornitoreDTO getFornitoreByCodice(String codice);
	public List<FornitoreDTO> getAllFornitore();
}
