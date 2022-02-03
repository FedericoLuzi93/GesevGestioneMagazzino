package it.gesev.entities;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="TESTATA_MOVIMENTO")
@Getter
@Setter
public class TestataMovimento 
{
	private int numeroProgressivo;
	
	private Date data;
	
	private int numOrdineLavoro;
	
	private double totaleImporto;
	
	private String utenteOperatore;
	
	@OneToOne(cascade={CascadeType.PERSIST, CascadeType.DETACH,
		 	CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name="CODICE_ENTE")
	private Ente ente;
	
	@ManyToOne(	cascade={CascadeType.PERSIST, CascadeType.DETACH,
		 	CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name="TIPO_MOVIMENTO")
	private TipoMovimento tipoMovimento;
	
	@OneToOne(cascade={CascadeType.PERSIST, CascadeType.DETACH,
		 	CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name="CODICE_FORNITORE")
	private Fornitore fornitore;
	
	@OneToMany(mappedBy="testataMovimento")
	private DettaglioMovimento dettaglioMovimento;
	
	public TestataMovimento()
	{
		
	}

	public TestataMovimento(int numeroProgressivo, Date data, int numOrdineLavoro, double totaleImporto,
			String utenteOperatore, Ente ente, TipoMovimento tipoMovimento, Fornitore fornitore,
			DettaglioMovimento dettaglioMovimento) 
	{
		this.numeroProgressivo = numeroProgressivo;
		this.data = data;
		this.numOrdineLavoro = numOrdineLavoro;
		this.totaleImporto = totaleImporto;
		this.utenteOperatore = utenteOperatore;
		this.ente = ente;
		this.tipoMovimento = tipoMovimento;
		this.fornitore = fornitore;
		this.dettaglioMovimento = dettaglioMovimento;
	}

	

}
