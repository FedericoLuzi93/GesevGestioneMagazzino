package it.gesev.utility;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.gesev.dto.DettaglioMovimentoDTO;
import it.gesev.dto.MovimentoDTO;
import it.gesev.entities.DettaglioMovimento;
import it.gesev.entities.TestataMovimento;

public class ConversionUtils 
{
	public static MovimentoDTO convertToMovimentoDTO(TestataMovimento testata, SimpleDateFormat formatter, DecimalFormat decimalFormatter) 
	{
			MovimentoDTO movimento = new MovimentoDTO();
			movimento.setIdTestata(StringUtils.leftPad(String.valueOf(testata.getNumOrdineLavoro()), 4, "0"));
			movimento.setDataTestata(formatter.format(testata.getData()));
			movimento.setTotaleTestata(decimalFormatter.format(testata.getTotaleImporto()));
			
			List<DettaglioMovimentoDTO> listaDettagli = new ArrayList<>();
			for(DettaglioMovimento dettaglio : testata.getListaDettaglioMovimento())
			{
				DettaglioMovimentoDTO dettaglioDTO = new DettaglioMovimentoDTO();
				dettaglioDTO.setCodiceDerrata(StringUtils.leftPad(String.valueOf(dettaglio.getDerrata().getDerrataId()), 4, "0"));
				dettaglioDTO.setDescrizioneDerrata(dettaglio.getDerrata().getDescrizioneDerrata());
				dettaglioDTO.setPrezzo(decimalFormatter.format(dettaglio.getPrezzoUnitario()));
				dettaglioDTO.setQuantita(decimalFormatter.format(dettaglio.getQuantitaEffettiva()));
				dettaglioDTO.setValore(decimalFormatter.format(dettaglio.getTotaleValore()));
				
				listaDettagli.add(dettaglioDTO);
			}
			
			movimento.setListaDettagli(listaDettagli);
			
			return movimento;
		
	}
}
