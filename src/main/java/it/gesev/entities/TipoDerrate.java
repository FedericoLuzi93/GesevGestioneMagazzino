package it.gesev.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name="tipo_derrate")
@Getter
@Setter
public class TipoDerrate 
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="codice")
	private int codice;
	
	@Column(name="desrcizione")
	private String desrcizione;
	
	public TipoDerrate()
	{
		
	}

	public TipoDerrate(int codice, String desrcizione) 
	{
		this.codice = codice;
		this.desrcizione = desrcizione;
	}
}
