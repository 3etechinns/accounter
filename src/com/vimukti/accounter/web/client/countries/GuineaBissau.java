package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class GuineaBissau extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "GWP";
		// XAF also..
	}

	@Override
	public String[] getStates() {
		return new String[] { "Bafat�", "Biombo", "Bissau", "Bolama", "Cacheu",
				"Gab�", "Oio", "Quinara", "Tombali" };
	}

}
