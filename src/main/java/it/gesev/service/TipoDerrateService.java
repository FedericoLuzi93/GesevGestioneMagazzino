package it.gesev.service;

import java.util.List;

import it.gesev.dto.RicercaColonnaDTO;
import it.gesev.dto.TipoDerrataDTO;

public interface TipoDerrateService 
{
	public List<TipoDerrataDTO> leggiTuttiTipiDerrate();
	public long createTipoDerrata(TipoDerrataDTO tipoDerrataDTO);
	public long deleteTipoDerrata(long codiceTipoDerrata);
	public long updateTipoDerrata(long codiceTipoDerrata, String descrizione);
	public List<TipoDerrataDTO> cercaTipoDerrataConColonna(RicercaColonnaDTO ricerca);
	

}
