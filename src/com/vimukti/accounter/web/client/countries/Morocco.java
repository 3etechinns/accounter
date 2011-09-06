package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Morocco extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Casablanca", "Chaouia-Ouardigha",
				"Doukkala-Abda", "F�s-Boulemane", "Gharb-Chrarda-B�ni Hssen",
				"Guelmim", "Marrakech-Tensift-Al Haouz", "Meknes-Tafilalet",
				"Oriental", "Rabat-Sal�-Zammour-Zaer", "Souss Massa-Dra�",
				"Tadla-Azilal", "Tangier-T�touan", "Taza-Al Hoceima-Taounate" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "MAD";
	}

	@Override
	public boolean allowFlexibleFiscalYear() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {

		return Accounter.constants().january();
	}

}
