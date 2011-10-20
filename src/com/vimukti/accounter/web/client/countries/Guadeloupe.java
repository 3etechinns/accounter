package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Guadeloupe extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "EUR";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Basse-Terre", "Grande-Terre",
				"�les des Saintes", "La D�sirade", "Marie-Galante",
				"Saint Barth�lemy", "Saint Martin" };
	}

}
