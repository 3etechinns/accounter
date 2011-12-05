package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.Accounter.AccounterType;
import com.vimukti.accounter.web.client.ui.ISaveCallback;
import com.vimukti.accounter.web.client.ui.ImageButton;

public class VoidButton extends ImageButton {
	private AbstractBaseView<?> view;
	private IAccounterCore obj;

	/**
	 * Creates new Instance
	 */
	public VoidButton(AbstractBaseView<?> view, IAccounterCore obj) {
		super(Accounter.messages().void1(), Accounter.getFinanceImages()
				.voided());
		this.view = view;
		this.obj = obj;
		String name = obj != null ? obj.getName() : "";
		this.setTitle(Accounter.messages().clickThisTo(
				Accounter.messages().void1(), name));
		// this.addStyleName("saveAndNew-Btn");
		addClichHandler();
	}

	/**
	 * 
	 */
	private void addClichHandler() {
		this.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {

				Accounter.showWarning(Accounter.messages()
						.doyouwanttoVoidtheTransaction(),
						AccounterType.WARNING, new ErrorDialogHandler() {

							@Override
							public boolean onYesClick() {
								executeVoid(obj);
								return true;
							}

							@Override
							public boolean onNoClick() {
								return true;
							}

							@Override
							public boolean onCancelClick() {
								return false;
							}
						});

			}
		});

	}

	private void executeVoid(IAccounterCore obj) {
		Accounter.voidTransaction(new ISaveCallback() {

			@Override
			public void saveFailed(AccounterException exception) {
				int errorCode = exception.getErrorCode();
				String errorString = AccounterExceptions
						.getErrorString(errorCode);
				Accounter.showError(errorString);
				exception.fillInStackTrace();

			}

			@Override
			public void saveSuccess(IAccounterCore object) {
				view.cancel();
			}
		}, obj.getObjectType(), obj.getID());
	}

}
