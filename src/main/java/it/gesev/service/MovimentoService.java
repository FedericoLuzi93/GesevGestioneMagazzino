package it.gesev.service;

import it.gesev.dto.MovimentoDTO;

public interface MovimentoService 
{
	public Integer prelevamentoMensa(MovimentoDTO movimento);
	public byte[] downloadDettaglioPrelevamento(Integer idTestata);
	public String getNumeroBuono();
}
