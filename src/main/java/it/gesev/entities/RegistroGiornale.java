package it.gesev.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="registro_giornale")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistroGiornale 
{
	@Id
	@Column(name = "anno_registro_giornale")
	private Integer annoRegistroGiornale;
	
	@Column(name = "numero_registro_giornale")
	private Integer numeroRegistroGiornale;
}
