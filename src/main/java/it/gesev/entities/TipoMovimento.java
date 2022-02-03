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
	
	@OneToMany(mappedBy="tipoMovimento")
	private List<TestataMovimento> listaTestataMovimento;
	
	public TipoMovimento()
	{
		
	}

	public TipoMovimento(int codice, String descrizione, String segno) 
	{
		this.codice = codice;
		this.descrizione = descrizione;
		this.segno = segno;
	}
}
