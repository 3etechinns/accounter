package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.company.options.PreferenceSettingsView;
import com.vimukti.accounter.web.client.ui.core.Action;

public class PreferencesAction extends Action {
	private PreferenceSettingsView page;
	public static int CATEGORY;
	public static final int COMPANY = 1;
	public static final int SETTINGS = 2;

	@SuppressWarnings("unchecked")
	public PreferencesAction(String text) {
		super(text);
		if (CATEGORY == COMPANY)
			this.catagory = Accounter.messages().company();
		else if (CATEGORY == SETTINGS)
			this.catagory = Accounter.messages().settings();
	}

	@Override
	public void run() {

		try {
			page = new PreferenceSettingsView();
			MainFinanceWindow.getViewManager().showView(page, null, true,
					PreferencesAction.this);
		} catch (Exception e) {
			e.printStackTrace();
			Accounter.showError(Accounter.messages()
					.failedToLoadCompanyPreferences());
		}

	}

	public ImageResource getBigImage() {
		// NOTHING TO DO.
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().preferences();
	}

	@Override
	public String getHistoryToken() {
		return "companyPreferences";
	}

	@Override
	public String getHelpToken() {
		return "company-preferences";
	}

}
