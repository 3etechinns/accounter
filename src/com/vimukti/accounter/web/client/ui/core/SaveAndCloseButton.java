/**
 * 
 */
package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.ImageButton;

/**
 * @author Prasanna Kumar G
 * 
 */
public class SaveAndCloseButton extends ImageButton {

	private AbstractBaseView<?> view;

	public SaveAndCloseButton(String save) {
		super(save, Accounter.getFinanceImages().saveAndClose());
		this.addStyleName("saveAndClose-Btn");
	}

	public void setView(AbstractBaseView<?> view) {
		this.view = view;
		addHandler();
	}

	/**
	 * Creates new Instance
	 */
	public SaveAndCloseButton(AbstractBaseView<?> baseView) {

		super(Accounter.messages().saveAndClose(), Accounter.getFinanceImages()
				.saveAndClose());
		this.view = baseView;
		this.addStyleName("saveAndClose-Btn");
		this.setTitle(Accounter.messages().clickThisTo(this.getText(),
				view.getAction().getViewName()));
		addHandler();
	}

	private void addHandler() {
		this.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {

				// Need to clarify after implemented approval process.
				if (view instanceof AbstractTransactionBaseView) {
					AbstractTransactionBaseView<?> transactionView = (AbstractTransactionBaseView<?>) view;
					if (!transactionView.isTemplate) {
						transactionView.getTransactionObject().setSaveStatus(
								ClientTransaction.STATUS_APPROVE);
					}
				}
				view.onSave(false);
			}
		});
	}
}
