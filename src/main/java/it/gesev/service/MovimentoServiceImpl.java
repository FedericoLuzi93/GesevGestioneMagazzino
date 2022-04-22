package it.gesev.service;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import it.gesev.dao.TestataMovimentoDAO;
import it.gesev.dto.DerrataDTO;
import it.gesev.dto.MovimentoDTO;
import it.gesev.dto.jasper.FirmaDC4Jasper;
import it.gesev.exc.GesevException;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class MovimentoServiceImpl implements MovimentoService {

	@Autowired
	TestataMovimentoDAO testataDAO;
	
	private static final Logger logger = LoggerFactory.getLogger(MovimentoServiceImpl.class);
	
	@Override
	public byte[] prelevamentoMensa(MovimentoDTO movimento) 
	{
		logger.info("Servizio prelevamento mensa");
		//TestataMovimento testata = testataDAO.prelevamentoMensa(movimento.getListaDettagli(), movimento.getIdEnte());
		
		logger.info("Generazione datasource per report DC8...");
//		List<Object[]> recordDerrate = testataDAO.getDerrateTestate(testata.getNumeroProgressivo());
		List<Object[]> recordDerrate = testataDAO.getDerrateTestate(16);
		List<Object[]> recordFirme = testataDAO.getDatiFirmaDC8(265);
		List<DerrataDTO> listaDerrate = new ArrayList<>();
		List<FirmaDC4Jasper> listaFirme = new ArrayList<>();
		
		for(Object[] record : recordDerrate)
		{
			DerrataDTO derrata = new DerrataDTO();
			derrata.setDescrizioneDerrata((String)record[0]);
			derrata.setUnitaMisura((String)record[1]);
			derrata.setQuantitaImpiegate(((BigDecimal)record[2]).doubleValue());
			derrata.setPrezzo(((BigDecimal)record[3]).doubleValue());
			derrata.setTotaleValore(((BigDecimal)record[4]).doubleValue());
			
			listaDerrate.add(derrata);
		}
		
		for(Object[] record : recordFirme)
		{
			FirmaDC4Jasper firma = new FirmaDC4Jasper();
			firma.setdescrizioneFirma((String)record[1]);
			firma.setnomeFirma((String)record[2]);
			firma.setCognomeFirma((String)record[3] + " (" + (String)record[0] + ")");
			
			listaFirme.add(firma);
		}
		
		logger.info("Generazione report...");
		byte[] arrayb = null;
		
		try
		{
			/* lettura template */
			File fileDerrate = ResourceUtils.getFile("classpath:Prelevamento.jrxml");
			
			/* creazione sorgente per template */
			JRBeanCollectionDataSource JRBlistaRighe = new JRBeanCollectionDataSource(listaDerrate);
			JRBeanCollectionDataSource JRBlistaFirme = new JRBeanCollectionDataSource(listaFirme);
			
			/* parametri */
			Map<String, Object> parameters = new HashMap<>();
//			parameters.put("ente", testata.getEnte().getDescrizioneEnte().toUpperCase());
			parameters.put("ente", "Ente");
//			parameters.put("buono", StringUtils.leftPad(String.valueOf(testata.getNumOrdineRegistro()), 4));
			parameters.put("buono", StringUtils.leftPad(String.valueOf(3), 4, "0"));
			parameters.put("totale", listaDerrate.stream().mapToDouble(d -> d.getTotaleValore()).reduce(0, (a, b) -> a + b));
			
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
//			parameters.put("giorno", formatter.format(testata.getData()));
			parameters.put("giorno", formatter.format(new Date()));
			parameters.put("TabellaPrelevamento", JRBlistaRighe);
			parameters.put("TabellaListaFirme", JRBlistaFirme);
			
			/* generazione file */
			JasperReport report = JasperCompileManager.compileReport(fileDerrate.getAbsolutePath());
			JasperPrint print = JasperFillManager.fillReport(report, parameters, new JREmptyDataSource());
			arrayb = JasperExportManager.exportReportToPdf(print);
			
			
		}
		
		catch(Exception ex)
		{
			logger.info("Si e' verificata un'eccezione", ex);
			throw new GesevException("Errore nella generazione del report DC8...", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return arrayb;

	}

}
