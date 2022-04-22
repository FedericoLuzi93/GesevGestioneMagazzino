package it.gesev.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.gesev.entities.Mensa;

public interface MensaRepository  extends JpaRepository<Mensa, Integer> 
{
//	@Query("select count(m) from Mensa m where m.via =:via and m.cap =:cap and m.citta =:citta and m.numeroCivico =:numeroCivico")
//	public Integer cercaPerIndirizzo(String via, String cap, String citta, int numeroCivico);
//	
//	@Query("select max(codiceMensa) from Mensa")
//	public Integer getMaxMensaId();
//	
//	public Optional<Mensa> findByCodiceMensa(int idMensa);

//	@Query("select m from Mensa m inner join Ente e on m.ente. = m.codiceMensa where e.mensaFK =: idEnte")
//	public List<Mensa> getMensaPerEnte(int idEnte);
}
