package it.gesev.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name="TIPO_DERRATE")
@Getter
@Setter
public class TipoDerrata 
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="CODICE")
	private int codice;
	
	@Column(name="DESCRIZIONE")
	private String descrizione;
	
	@OneToMany(mappedBy="tipoDerrata")
	private List<Derrata> listaDerrata;
	
	public TipoDerrata()
	{
		
	}

	public TipoDerrata(int codice, String descrizione) 
	{
		this.codice = codice;
		this.descrizione = descrizione;
	}
}
