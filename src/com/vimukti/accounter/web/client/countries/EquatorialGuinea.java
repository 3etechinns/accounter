package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class EquatorialGuinea extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "XAF";
		// GAQ also...
	}

	@Override
	public String[] getStates() {
		return new String[] { "Annob�n", "Bioko Norte", "Bioko Sur",
				"Centro Sur", "Ki�-Ntem", "Litoral", "Wele-Nzas" };
	}

}
