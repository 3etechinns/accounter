package com.vimukti.accounter.web.client.ui.settings;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientBrandingTheme;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

public class CopyThemeAction extends Action {

	public CopyThemeAction(String text) {
		super(text);
	}

	@Override
	public ImageResource getBigImage() {
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public ParentCanvas getView() {
		return null;
	}

	@Override
	public void run(Object data, Boolean isDependent) {
		try {
			CopyThemeDialog copyThemeDialog = new CopyThemeDialog(Accounter
					.getSettingsMessages().copyTheme(), "",
					(ClientBrandingTheme) data);
			copyThemeDialog.center();
			copyThemeDialog.show();
		} catch (Exception e) {
			System.out.println(e.toString());
		}

	}

	@Override
	public String getHistoryToken() {
		return null;
	}

}
