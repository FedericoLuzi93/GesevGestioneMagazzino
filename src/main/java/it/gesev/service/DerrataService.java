package it.gesev.service;

import java.util.List;

import it.gesev.dto.DerrataDTO;
import it.gesev.dto.RicercaColonnaDTO;
import it.gesev.entities.Derrata;

public interface DerrataService 
{
	public List<DerrataDTO> getAllDerrata(long tipoDerrataId);
	public Long creaDerrata(DerrataDTO derrataDTO);
	public Long deleteDerrata(Long derrataId);
	public Long aggiornaDerrata(DerrataDTO derrataDTO, Long idDerrata);
	public List<DerrataDTO> cercaTipoDerrataConColonna(RicercaColonnaDTO ricerca, Long idLotto);
}
