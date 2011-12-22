package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;
import com.vimukti.accounter.web.client.util.DayAndMonthUtil;

public class CzechRepublic extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Jihocesky", "Jihomoravsky",
				"Karlovarsky", "Kr�lov�hradecky", "Liberecky",
				"Moravskoslezsky", "Olomoucky", "Pardubicky", "Plzensky",
				"Prag", "Stredocesky", "Ustecky", "Vysocina", "Zlinsky" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "CZK";
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
		return "UTC+1:00 Europe/Prague";
	}

}
