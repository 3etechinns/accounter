package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class AmountField extends TextItem {

	private Double doubleAmount;

	private BlurHandler blurHandler;

	public AmountField(final String name) {

		setName(name);
		setTitle(name);
		// addFocusHandler(getFocusHandler());
		addBlurHandler(getBlurHandler());

		// Set Default Values
		setKeyPressFilter("[0-9.]");
		setAmount(0.00D);
		((TextBox) getMainWidget()).setTextAlignment(TextBoxBase.ALIGN_RIGHT);
		// setKeyPressHandler(new KeyPressListener() {
		//
		// @Override
		// public void onKeyPress(int keyCode) {
		// if (!((keyCode >= 48 && keyCode <= 57) && keyCode != 190)) {
		// setValue("0.00");
		// }
		// }
		// });
	}

	private void setKeyPressFilter(String string) {
		// FIXME allow only nummbers
	}

	private BlurHandler getBlurHandler() {

		BlurHandler blurHandler = new BlurHandler() {

			Object value = null;

			public void onBlur(BlurEvent event) {
				try {

					value = getValue();

					if (value == null)
						return;
					Double amount = DataUtils.getAmountStringAsDouble(value
							.toString());
					if (!AccounterValidator.isAmountTooLarge(amount)) {
						setAmount(DataUtils.isValidAmount(amount + "") ? amount
								: 0.00);
					}
					if (!AccounterValidator.isAmountNegative(amount)) {
						setAmount(DataUtils.isValidAmount(amount + "") ? amount
								: 0.00);
					}
					if (AmountField.this.blurHandler != null) {
						AmountField.this.blurHandler.onBlur(event);
					}
				} catch (Exception e) {
					if (e instanceof InvalidEntryException) {
						BaseView.errordata.setHTML("<li>" + e.getMessage());
						BaseView.commentPanel.setVisible(true);
						// Accounter.showError(e.getMessage());
					}

					// else if (value.toString().length() != 0) {
					// Accounter
					// .showError(AccounterErrorType.INCORRECTINFORMATION);
					// }
					setAmount(0.00);
				}
			}
		};
		return blurHandler;
	}

	public void setAmount(Double amount) {

		this.doubleAmount = amount;
		setValue(DataUtils.getAmountAsString(amount));

	}

	public Double getAmount() {
		return this.doubleAmount;
	}

	public void setBlurHandler(BlurHandler blurHandler) {
		this.blurHandler = blurHandler;
	}

	// private FocusHandler getFocusHandler() {
	// FocusHandler focusHandler = new FocusHandler() {
	// Object value = null;
	//
	// @Override
	// public void onFocus(FocusEvent event) {
	// try {
	//
	// value = ((TextItem) event.getSource()).getValue();
	//
	// if (value == null)
	// return;
	//
	// setValue(DataUtils.getAmountStringAsDouble(DataUtils
	// .removeDollar(value.toString())));
	//
	// } catch (Exception e) {
	// // TODO: handle exception
	// }
	//
	// }
	// };
	// return focusHandler;
	//
	// }

}
