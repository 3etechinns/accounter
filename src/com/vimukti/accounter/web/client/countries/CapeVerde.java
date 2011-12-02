package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class CapeVerde extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "CVE";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Boavista", "Brava", "Fogo", "Maio", "Sal",
				"Santo Ant�o", "S�o Nicolau", "S�o Tiago", "S�o Vicente" };
	}

}
