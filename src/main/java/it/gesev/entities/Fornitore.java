package it.gesev.entities;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="FORNITORE")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Fornitore implements Serializable
{
	
	private static final long serialVersionUID = 3098607660112617547L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="CODICE")
	private Long codice;
	
	@Column(name="DESCRIZIONE")
	private String descrizione;
	
	@OneToOne(mappedBy="fornitore", cascade={CascadeType.PERSIST, CascadeType.DETACH,
		 	CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.LAZY)
	private TestataMovimento testataMovimento;
	
	
}
