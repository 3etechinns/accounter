package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class TrinidadAndTobago extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Arima", "Chaguanas",
				"Couva-Tabaquite-Talparo", "Diego Mart�n", "Mayaro-R�o Claro",
				"Pe�al D�b�", "Point Fort�n", "Port of Spain", "Princes Town",
				"San Fernando", "Sangre Grande", "San Juan-Laventville",
				"Siparia", "Tobago", "Tunapuna-Piarco" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "TTD";
	}

	@Override
	public boolean allowFlexibleFiscalYear() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {
		return Accounter.messages().october();
	}

}
