package it.gesev.dao;

import java.util.Date;
import java.util.List;

import it.gesev.entities.TestataMovimento;

public interface TestataMovimentoDAO 
{
	public List<TestataMovimento> getDettaglioMovimentoByFornitore(Long idFornitore);
	public List<TestataMovimento> cercaDerrateInTestate(Date dataDa, Date dataA, String descrizioneDerrata, Long idFornitore);
}
