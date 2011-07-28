package com.vimukti.accounter.web.client.ui.settings;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

public class GeneralSettingsAction extends Action {
	public GeneralSettingsView view;

	public GeneralSettingsAction(String text) {
		super(text);
		this.catagory = Accounter.constants().settings();
	}

	/**
	 * THIS METHOD DID N'T USED ANY WHERE IN THE PROJECT.
	 */
	@Override
	public ImageResource getBigImage() {
		return null;
	}

	/**
	 * THIS METHOD DID N'T USED ANY WHERE IN THE PROJECT.
	 */
	@Override
	public ImageResource getSmallImage() {
		return null;
	}

	/**
	 * THIS METHOD DID N'T USED ANY WHERE IN THE PROJECT.
	 */
	
//	@Override
//	public ParentCanvas getView() {
//		return null;
//	}

	@Override
	public void run(Object data, Boolean isDependent) {
		runAsync(data, isDependent);
	}

	public void runAsync(final Object data, final Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreated() {

				try {
					view = new GeneralSettingsView();
					MainFinanceWindow.getViewManager().showView(view, data,
							isDependent, GeneralSettingsAction.this);

				} catch (Throwable e) {
					onCreateFailed(e);
				}

			}

			public void onCreateFailed(Throwable t) {
			}
		});
	}

	@Override
	public String getHistoryToken() {
		return "generalSettings";
	}

}
