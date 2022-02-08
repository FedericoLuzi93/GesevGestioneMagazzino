package it.gesev.service;

import java.util.List;

import it.gesev.dto.FornitoreDTO;
import it.gesev.dto.MovimentoDTO;
import it.gesev.dto.RicercaColonnaDTO;
import it.gesev.dto.RicercaTestateDTO;

public interface FornitoreService {
	public FornitoreDTO getFornitoreByCodice(String codice);
	public List<FornitoreDTO> getAllFornitore();
	public List<FornitoreDTO> creaFornitore(String descrizione);
	public List<FornitoreDTO> cancellaFornitore(Long idFornitore);
	public List<FornitoreDTO> aggiornaFornitore(FornitoreDTO fornitore);
	public List<FornitoreDTO> cercaFornitorePerColonna(RicercaColonnaDTO ricerca);
	public List<MovimentoDTO> cercaDettagliByFornitore(Long idFornitore);
	public List<MovimentoDTO> cercaDerrateInTestate(RicercaTestateDTO ricerca);
}
