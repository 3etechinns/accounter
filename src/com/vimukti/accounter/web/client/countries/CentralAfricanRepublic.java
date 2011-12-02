package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class CentralAfricanRepublic extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "XAF";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Bamingui-Bangoran", "Bangui", "Basse-Kotto",
				"Haute-Kotto", "Haut-Mbomou", "K�mo", "Lobaye",
				"Mamb�r�-Kad��", "Mbomou", "Nana-Gribizi", "Nana-Mamb�r�",
				"Ombella Mpoko", "Ouaka", "Ouham", "Ouham-Pend�",
				"Sangha-Mba�r�", "Vakaga" };
	}

}
