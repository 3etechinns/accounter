package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.util.ICountryPreferences;
import com.vimukti.accounter.web.client.util.OrganizationType;

public class Brazil implements ICountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Acre", "Alagoas", "Amap�",
				"Amazonas", "Bahia", "Cear�", "Distrito Federal",
				"Esp�rito Santo", "Goi�s", "Maranh�o", "Mato Grosso",
				"Mato Grosso do Sul", "Minas Gerais", "Par�", "Para�ba",
				"Paran�", "Pernambuco", "Piau�", "Rio de Janeiro",
				"Rio Grande do Norte", "Rio Grande do Sul", "Rond�nia",
				"Roraima", "Santa Catarina", "S�o Paulo", "Sergipe",
				"Tocantins" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDefaultTimeZone(String state) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OrganizationType[] getOrganizationTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean allowFlexibleFiscalYear() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {
		// TODO Auto-generated method stub
		return null;
	}

}
