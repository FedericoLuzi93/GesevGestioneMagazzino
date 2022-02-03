package it.gesev.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="FORNITORE")
@Getter
@Setter
public class Fornitore 
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="CODICE")
	private Long codice;
	
	@Column(name="DESCRIZIONE")
	private String Descrizione;
	
	@OneToOne(mappedBy="fornitore", cascade={CascadeType.PERSIST, CascadeType.DETACH,
		 	CascadeType.MERGE, CascadeType.REFRESH})
	private TestataMovimento testataMovimento;
	
	public Fornitore()
	{
		
	}

	public Fornitore(Long codice, String descrizione) 
	{
		this.codice = codice;
		Descrizione = descrizione;
	}
}
