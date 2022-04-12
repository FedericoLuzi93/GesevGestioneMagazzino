package it.gesev.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StampaDerrateDTO 
{
	private String descrizioneLotto;
	private String codiceLotto;
	private String derrataId;
	private String descrizioneDerrata;
	private String unitaMisura;
	private String prezzo;
}
