package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class CzechRepublic extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Jihocesk�", "Jihomoravsk�",
				"Karlovarsk�", "Kr�lov�hradeck�", "Libereck�",
				"Moravskoslezsk�", "Olomouck�", "Pardubick�", "Plzensk�",
				"Prag", "Stredocesk�", "�steck�", "Vysocina", "Zl�nsk�" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "CSK";
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
