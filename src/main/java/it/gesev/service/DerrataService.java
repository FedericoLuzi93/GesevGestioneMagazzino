package it.gesev.service;

import java.util.List;

import it.gesev.dto.DerrataDTO;
import it.gesev.dto.RicercaColonnaDTO;

public interface DerrataService 
{
	public List<DerrataDTO> getAllDerrata(int tipoDerrataId);
	public int creaDerrata(DerrataDTO derrataDTO);
	public int deleteDerrata(int derrataId);
	public int aggiornaDerrata(DerrataDTO derrataDTO, int idDerrata);
	public List<DerrataDTO> cercaTipoDerrataConColonna(List<RicercaColonnaDTO> ricerca, int idLotto);
}
