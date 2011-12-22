package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;
import com.vimukti.accounter.web.client.util.DayAndMonthUtil;

public class NetherlandsAntilles extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = { "Bonaire", "Cura�ao", "Saba", "Sint Eustatius",
				"Sint Maarten" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "ANG";
	}

	@Override
	public boolean allowFlexibleFiscalYear() {
		return true;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {
		return DayAndMonthUtil.january();
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC-4:00 America/Curacao";
	}
}
