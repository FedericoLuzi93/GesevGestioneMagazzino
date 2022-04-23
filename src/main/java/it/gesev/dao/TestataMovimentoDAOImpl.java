package it.gesev.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import it.gesev.dto.DettaglioMovimentoDTO;
import it.gesev.entities.Derrata;
import it.gesev.entities.DettaglioMovimento;
import it.gesev.entities.Ente;
import it.gesev.entities.Fornitore;
import it.gesev.entities.Mensa;
import it.gesev.entities.RegistroGiornale;
import it.gesev.entities.TestataMovimento;
import it.gesev.entities.TipoMovimento;
import it.gesev.exc.GesevException;
import it.gesev.repository.DerrataRepository;
import it.gesev.repository.DettaglioMovimentoRepository;
import it.gesev.repository.EnteRepository;
import it.gesev.repository.MensaRepository;
import it.gesev.repository.RegistroGiornaleRepository;
import it.gesev.repository.TestataMovimentoRepository;
import it.gesev.repository.TipoMovimentoRepository;

@Component
public class TestataMovimentoDAOImpl implements TestataMovimentoDAO {

	@Autowired
	private TestataMovimentoRepository testataMovimentoRepository;
	
	@Autowired
	private TipoMovimentoRepository tipoMovimentoRepository;
	
	@Autowired
	private DettaglioMovimentoRepository dettaglioTipoMovimentoRepository;
	
	@Autowired
	private DerrataRepository derrataRepository;
	
	@Autowired
	private RegistroGiornaleRepository registroGiornaleRepository;
	
	@Autowired
	private MensaRepository mensaRepository;
	
	@Autowired
	private EnteRepository enteRepository;
	
	@PersistenceContext
	EntityManager entityManager;
	
	private static final Logger logger = LoggerFactory.getLogger(TestataMovimentoDAOImpl.class);
	
	@Override
	public List<TestataMovimento> getDettaglioMovimentoByFornitore(Long idFornitore) 
	{
		logger.info("Ricerca testata movimento con ID fornitore " + idFornitore);
		
		List<TestataMovimento> testataMovimento = testataMovimentoRepository.getTestataMovimentoByIdFornitore(idFornitore.intValue());
		return testataMovimento;
//		if(testataMovimento.size() > 0)
//			return testataMovimento;
//		
//		else
//			throw new GesevException("Nessun fornitore presente con l'ID fornito", HttpStatus.BAD_REQUEST);
	}

	@Override
	public List<TestataMovimento> cercaDerrateInTestate(Date dataDa, Date dataA, String descrizioneDerrata, Long idFornitore) 
	{
		logger.info("Ricerca delle derrate di un fornitore sulla base delle date e della derrata...");
		
		/* controllo fornitore */
		if(idFornitore == null)
			throw new GesevException("Impossibile trovare movimenti. ID fornitore non presente", HttpStatus.BAD_REQUEST);
		
		/* controllo validita' delle date */
		if(dataDa != null && dataA != null && dataDa.after(dataA))
			throw new GesevException("La data DAL deve precedere la data AL", HttpStatus.BAD_REQUEST);
		
		logger.info("Preparazione della query di ricerca...");
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<TestataMovimento> criteriaQuery = criteriaBuilder.createQuery(TestataMovimento.class);
		Root<TestataMovimento> testatataRoot = criteriaQuery.from(TestataMovimento.class);
		
		/* predicato finale */
		Predicate finalPredicate = null;
		
		/* controllo presenza date */
		if(dataDa != null)
			finalPredicate = criteriaBuilder.greaterThanOrEqualTo(testatataRoot.get("data").as(java.sql.Date.class), dataDa);
		
		if(dataA != null)
		{
	
			Predicate dataMinore = criteriaBuilder.lessThanOrEqualTo(testatataRoot.get("data").as(java.sql.Date.class), dataA);
			finalPredicate = finalPredicate == null ? dataMinore : criteriaBuilder.and(finalPredicate, dataMinore);
		}
		
		/* predicato relativo al fornitore */
		Join<TestataMovimento, Fornitore> joinFornitore = testatataRoot.join("fornitore");
		Predicate predicatoFornitore = criteriaBuilder.equal(joinFornitore.get("codice"), idFornitore);
		finalPredicate = finalPredicate == null ? predicatoFornitore : criteriaBuilder.and(finalPredicate, predicatoFornitore);
		
		/* predicato descrizione derrata */
		if(StringUtils.isNotBlank(descrizioneDerrata))
		{
			Join<TestataMovimento, DettaglioMovimento> dettaglioJoin = testatataRoot.joinList("listaDettaglioMovimento");
			Join<DettaglioMovimento, Derrata> derrataJoin = dettaglioJoin.join("derrata");
			Predicate derrataPredicate = criteriaBuilder.equal(derrataJoin.get("descrizioneDerrata"), descrizioneDerrata);
			finalPredicate = criteriaBuilder.and(finalPredicate, derrataPredicate);
		}
		
		logger.info("Esecuzione query...");
		criteriaQuery.where(finalPredicate);
		List<TestataMovimento> listaTestate = entityManager.createQuery(criteriaQuery).getResultList();
		
		logger.info("Numero elementi trovati: " + listaTestate.size());
		
		return listaTestate;
	}

	@SuppressWarnings("unused")
	@Override
	@Transactional
	public TestataMovimento prelevamentoMensa(List<DettaglioMovimentoDTO> listaMovimenti, Integer idEnte, Integer idMensa) {
		logger.info("Inserimento dei movimenti...");
		
		if(listaMovimenti == null || listaMovimenti.size() == 0)
			return null;
		
		logger.info("Creazione testata...");
		TestataMovimento testataMovimento = new TestataMovimento();
		testataMovimento.setData(new Date());
		
		/* ricerca tipo movimento */
		Optional<TipoMovimento> optTipoMovimento = tipoMovimentoRepository.findById(0);
		if(!optTipoMovimento.isPresent())
			throw new GesevException("Impossibile trovare il tipo movimento con ID = 0", HttpStatus.BAD_REQUEST);
		
		testataMovimento.setTipoMovimento(optTipoMovimento.get());
		
		/* ricerca ente */
		if(idEnte == null)
			throw new GesevException("Valore dell'ID ente non valido", HttpStatus.BAD_REQUEST);
			
		Optional<Ente> optEnte = enteRepository.findById(idEnte);
		if(!optEnte.isPresent())
			throw new GesevException("Impossibile trovare un ente con ID = " + idEnte, HttpStatus.BAD_REQUEST);
		
		testataMovimento.setEnte(optEnte.get());
		
		/* ricerca mensa */
		if(idMensa == null)
			throw new GesevException("ID mensa non valido", HttpStatus.BAD_REQUEST);
		
		Optional<Mensa> optMensa = mensaRepository.findById(idMensa);
		if(!optMensa.isPresent())
			throw new GesevException("ID mensa non valido", HttpStatus.BAD_REQUEST);
		
		testataMovimento.setMensa(optMensa.get());
		
		testataMovimento.setNumOrdineLavoro(0);
		testataMovimento.setNota("Prelevamento mensa");
		testataMovimento.setUtenteOperatore("Utente");
		testataMovimento.setTrasferito("Y");
		
		/* ricerca del numero giornale */
		logger.info("Ricerca del numero del giornale...");
		RegistroGiornale registro = null;
		
		/* creazione Calendar con data odierna */
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		
		Optional<RegistroGiornale> optRegistro = registroGiornaleRepository.findById(calendar.get(Calendar.YEAR));
		if(!optRegistro.isPresent())
		{
			logger.info("Creazione di un nuovo giornale per l'anno " + calendar.get(Calendar.YEAR));
			RegistroGiornale nuovoRegistro = new RegistroGiornale(calendar.get(Calendar.YEAR), 1);
			registro = registroGiornaleRepository.save(nuovoRegistro);
		}
		
		else
			registro = optRegistro.get();
		
		testataMovimento.setNumOrdineRegistro(registro.getNumeroRegistroGiornale());
		testataMovimento.setAnno(calendar.get(Calendar.YEAR));
		
		testataMovimentoRepository.save(testataMovimento);
		
		List<DettaglioMovimento> listaDettagli = new ArrayList<>();
		
		Double totaleTestata = 0.0;
		
		logger.info("Creazione dettagli movimento...");
		for(DettaglioMovimentoDTO dettaglio : listaMovimenti)
		{
			/* creazione nuovo dettaglio */
			if(StringUtils.isBlank(dettaglio.getCodiceDerrata()) || StringUtils.isBlank(dettaglio.getQuantita()))
				throw new GesevException("I valori del codice derrata o della quantita' non sono validi", HttpStatus.BAD_REQUEST);
			
			DettaglioMovimento dettaglioMovimento = new DettaglioMovimento();
			dettaglioMovimento.setTestataMovimento(testataMovimento);
			dettaglioMovimento.setDate(new Date());
			
			/* ricerca derrata e inserimento quantita' */
			Integer idDerrata = null;
			Double quantita = null;
			
			try
			{
				idDerrata = Integer.valueOf(dettaglio.getCodiceDerrata());
				quantita = Double.valueOf(dettaglio.getQuantita());
			}
			
			catch(Exception ex)
			{
				throw new GesevException("Valore dell'ID derrata o della quantita' non validi", HttpStatus.BAD_REQUEST);
			}
			
			Optional<Derrata> optDerrata = derrataRepository.findById(idDerrata);
			if(!optDerrata.isPresent())
				throw new GesevException("Impossibile trovare una derrata con l'ID " + idDerrata, HttpStatus.BAD_REQUEST);
			
			if(quantita > optDerrata.get().getGiacenza())
				throw new GesevException("La quantita' richiesta e' maggiore della giacenza della derrata " + optDerrata.get().getDescrizioneDerrata(), 
						                 HttpStatus.BAD_REQUEST);
			
			/* aggiornamento derrata */
			optDerrata.get().setGiacenza(optDerrata.get().getGiacenza() - quantita);
			derrataRepository.save(optDerrata.get());
			
			dettaglioMovimento.setDerrata(optDerrata.get());
			dettaglioMovimento.setQuantitaEffettiva(quantita);
			dettaglioMovimento.setQuantitaRichiesta(quantita);
			dettaglioMovimento.setPrezzoUnitario(optDerrata.get().getPrezzo());
			
			dettaglioMovimento.setTotaleValore(quantita * optDerrata.get().getPrezzo());
			totaleTestata += (quantita * optDerrata.get().getPrezzo());
			
			listaDettagli.add(dettaglioTipoMovimentoRepository.save(dettaglioMovimento));
			
		}
		
		/* aggiornamento testata */
		testataMovimento.setTotaleImporto(totaleTestata);
		testataMovimento.setListaDettaglioMovimento(listaDettagli);
		TestataMovimento testataMovimentoFinale = testataMovimentoRepository.save(testataMovimento);
		
		/* aggiornamento giornale */
		registro.setNumeroRegistroGiornale(registro.getNumeroRegistroGiornale() + 1);
		registroGiornaleRepository.save(registro);
		
		return testataMovimentoFinale;
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getDerrateTestate(Integer idTestata) {
		logger.info("Ricerca derrate associate alla testata");
		
		if(idTestata == null)
			throw new GesevException("ID testata non valido", HttpStatus.BAD_REQUEST);
		
		String queryString = "select d.descrizione_derrata, d.unita_misura, dm.quantita_effettiva, d.prezzo, dm.totale_valore  "
				+ "from testata_movimento tm left join dettaglio_movimento dm on dm.testata_movimento_fk = tm.numero_progressivo  "
				+ "left join derrata d on dm.derrata = d.derrata_id  "
				+ "where tm.numero_progressivo = :idTestata";
		
		Query query = entityManager.createNativeQuery(queryString).setParameter("idTestata", idTestata);
		
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getDatiFirmaDC8(Integer idMensa) 
	{
		logger.info("Ricerca firme per modello DC4...");
		
		if(idMensa == null)
			throw new GesevException("ID mensa non valido", HttpStatus.BAD_REQUEST);
		
		String queryString = "select  "
				+ "case when arrm.flag_approvatore = 'Y' then 'Approvatore' else 'Firmatario' end, "
				+ "rm.descrizione_ruolo_mensa , "
				+ "d.nome, "
				+ "d.cognome,  "
				+ "arrm.ordine_firma  "
				+ "from ass_report_ruolo_mensa arrm left join ass_dipendente_ruolo adr on arrm.ruolo_fk = adr.ruolo_fk  "
				+ "left join dipendente d on d.codice_dipendente = adr.dipendente_fk  "
				+ "left join ruolo_mensa rm on arrm.ruolo_fk = rm.codice_ruolo_mensa  "
				+ "where arrm.report_fk = 'DC8' and adr.mensa_fk = :idMensa "
				+ "order by arrm.ordine_firma ";
		
		Query query = entityManager.createNativeQuery(queryString).setParameter("idMensa", idMensa);
		
		return query.getResultList();
	}

	@Override
	public TestataMovimento getTestataByNumeroProgressivo(Integer numeroProgressivo) 
	{
		logger.info("Ricerca testata dal numero progressivo...");
		Optional<TestataMovimento> optTestata = testataMovimentoRepository.findById(numeroProgressivo);
		if(optTestata.isPresent())
			return optTestata.get();
		
		else
			return null;
	}

	
	
}
