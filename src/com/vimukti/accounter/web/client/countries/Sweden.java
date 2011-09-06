package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Sweden extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Blekinge", "Dalarna", "G�vleborg",
				"Gotland", "Halland", "J�mtland", "J�nk�ping", "Kalmar",
				"Kronoberg", "Norrbotten", "�rebro", "�sterg�tland", "Sk�ne",
				"S�dermanland", "Stockholm", "Uppsala", "V�rmland",
				"V�sterbotten", "V�sternorrland", "V�stmanland",
				"V�stra G�taland" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "SEK";
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
