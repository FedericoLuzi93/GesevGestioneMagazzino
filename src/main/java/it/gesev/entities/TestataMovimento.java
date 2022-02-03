package it.gesev.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="NUMERO_PROGRESSIVO")
	private Long numeroProgressivo;
	
	@Column(name="DATA")
	private Date data;
	
	@Column(name="NUM_ORDINE_LAVORO")
	private int numOrdineLavoro;
	
	@Column(name = "NOTA")
	private String nota;
	
	@Column(name="TOTALE_IMPORTO")
	private double totaleImporto;
	
	@Column(name="UTENTE_OPERATORE")
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
	
	@OneToMany(mappedBy="testataMovimento", fetch = FetchType.LAZY)
	private List<DettaglioMovimento> listaDettaglioMovimento;
	
	public TestataMovimento()
	{
		
	}

	public TestataMovimento(Long numeroProgressivo, Date data, int numOrdineLavoro, String nota, double totaleImporto,
			String utenteOperatore) 
	{
		this.numeroProgressivo = numeroProgressivo;
		this.data = data;
		this.numOrdineLavoro = numOrdineLavoro;
		this.totaleImporto = totaleImporto;
		this.utenteOperatore = utenteOperatore;
		this.nota = nota;
	}

	

}
