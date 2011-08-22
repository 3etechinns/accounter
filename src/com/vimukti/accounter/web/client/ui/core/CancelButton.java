/**
 * 
 */
package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.ImageButton;

/**
 * @author Prasanna Kumar G
 * 
 */
public class CancelButton extends ImageButton {

	private AbstractBaseView<?> currentView;

	/**
	 * Creates new Instance
	 */
	public CancelButton(AbstractBaseView<?> view) {
		super(Accounter.constants().cancel(), Accounter.getFinanceImages()
				.rejected());
		this.currentView = view;
		this.addStyleName("cancle-Btn");
		this.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				currentView.cancel();
			}
		});
	}

}
