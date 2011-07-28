package com.vimukti.accounter.web.client.ui.settings;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

/**
 * 
 * @author Uday Kumar
 * 
 */
public class InvoiceBrandingAction extends Action {

	private InvoiceBrandingView view;

	public InvoiceBrandingAction(String text) {
		super(text);
		this.catagory = Accounter.constants().settings();
	}

	@Override
	public ImageResource getBigImage() {
		// NOTHING TO DO.
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		// NOTHING TO DO.
		return null;
	}

//	@Override
//	public ParentCanvas getView() {
//		// NOTHING TO DO.
//		return null;
//	}

	@Override
	public void run(Object data, Boolean isDependent) {
		runAsync(data, isDependent);
	}

	private void runAsync(Object data, Boolean isDependent) {
		try {
			view = new InvoiceBrandingView();
			MainFinanceWindow.getViewManager().showView(view, data, false,
					InvoiceBrandingAction.this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getHistoryToken() {
		return "invoiceBranding";
	}

}
