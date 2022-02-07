package it.gesev.dao;

import java.util.List;

import it.gesev.entities.TestataMovimento;

public interface TestataMovimentoDAO 
{
	public List<TestataMovimento> getDettaglioMovimentoByFornitore(Long idFornitore);
}
