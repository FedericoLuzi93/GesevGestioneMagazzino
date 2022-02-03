package it.gesev.entities;

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
@Table(name="TIPO_MOVIMENTO")
@Getter
@Setter
public class TipoMovimento 
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="CODICE")
	private int codice;
	
	@Column(name="TIPO_DESCRIZIONE")
	private String descrizione;
	
	@Column(name="SEGNO")
	private String segno;
	
	@OneToOne(mappedBy="tipoMovimento")
	private TestataMovimento testataMovimento;
	
	public TipoMovimento()
	{
		
	}

	public TipoMovimento(int codice, String descrizione, String segno, TestataMovimento testataMovimento) 
	{
		this.codice = codice;
		this.descrizione = descrizione;
		this.segno = segno;
		this.testataMovimento = testataMovimento;
	}
	
	

}
