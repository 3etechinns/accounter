package com.vimukti.accounter.web.client.ui.settings;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientBrandingTheme;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;

public class DeleteThemeAction extends Action {

	public DeleteThemeAction() {
		super();
	}

	@Override
	public ImageResource getBigImage() {
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return null;
	}

	// @Override
	// public ParentCanvas getView() {
	// return null;
	// }

	@Override
	public void run() {
		try {
			DeleteThemeDialog deleteThemeDialog = new DeleteThemeDialog(
					Accounter.messages().deleteThemeLabel(), "",
					(ClientBrandingTheme) data);
			deleteThemeDialog.show();
			deleteThemeDialog.center();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	@Override
	public String getHistoryToken() {
		return null;
	}

	@Override
	public String getHelpToken() {
		return "delete-theme";
	}

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return messages.users();
	}
}
