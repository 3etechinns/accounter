package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Martinique extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "EUR";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Fort-de-France", "La Trinit�", "Le Marin",
				"Saint-Pierre" };
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC-4:00 America/Martinique";
	}

}
