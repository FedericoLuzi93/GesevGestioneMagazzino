package it.gesev.service;

import java.util.List;

import it.gesev.dto.RicercaColonnaDTO;
import it.gesev.dto.TipoDerrataDTO;

public interface TipoDerrateService 
{
	public List<TipoDerrataDTO> leggiTuttiTipiDerrate();
	public int createTipoDerrata(TipoDerrataDTO tipoDerrataDTO);
	public int deleteTipoDerrata(int codiceTipoDerrata);
	public int updateTipoDerrata(int codiceTipoDerrata, TipoDerrataDTO tipoDerrataDTO);
	public List<TipoDerrataDTO> cercaTipoDerrataConColonna(List<RicercaColonnaDTO> ricerca);
}
