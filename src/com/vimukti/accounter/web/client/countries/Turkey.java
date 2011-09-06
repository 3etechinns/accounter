package com.vimukti.accounter.web.client.countries;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.util.AbstractCountryPreferences;

public class Turkey extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Adana", "Adiyaman", "Afyonkarahisar",
				"Agri", "Aksaray", "Amasya", "Ankara", "Antalya", "Ardahan",
				"Artvin", "Aydin", "Balikesir", "Bartin", "Batman", "Bayburt",
				"Bilecik", "Bing�l", "Bitlis", "Bolu", "Burdur", "Bursa",
				"�anakkale", "�ankiri", "�orum", "Denizli", "Diyarbakir",
				"D�zce", "Edirne", "Elazig", "Erzincan", "Erzurum",
				"Eskisehir", "Gaziantep", "Giresun", "G�m�shane", "Hakkari",
				"Hatay", "Igdir", "Isparta", "Istanbul", "Izmir",
				"Kahramanmaras", "Karab�k", "Karaman", "Kars", "Kastamonu",
				"Kayseri", "Kilis", "Kirikkale", "Kirklareli", "Kirsehir",
				"Kocaeli", "Konya", "K�tahya", "Malatya", "Manisa", "Mardin",
				"Mersin", "Mugla", "Mus", "Nevsehir", "Nigde", "Ordu",
				"Osmaniye", "Rize", "Sakarya", "Samsun", "Sanliurfa", "Siirt",
				"Sinop", "Sirnak", "Sivas", "Tekirdag", "Tokat", "Trabzon",
				"Tunceli", "Usak", "Van", "Yalova", "Yozgat", "Zonguldak" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "TRL";
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
