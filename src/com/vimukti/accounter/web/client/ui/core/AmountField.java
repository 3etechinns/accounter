package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.WidgetWithErrors;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class AmountField extends TextItem {

	private Double doubleAmount;

	private ClientCurrency currency;

	private BlurHandler blurHandler;

	private WidgetWithErrors errorsWidget;

	private boolean showCurrency = true;

	public AmountField(final String name, WidgetWithErrors errorsViewGrid,
			ClientCurrency currency) {
		this.errorsWidget = errorsViewGrid;
		setName(name);
		if (showCurrency) {
			setTitle(name + "(" + currency.getFormalName() + ")");
		} else {
			setTitle(name);
		}
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
		this.currency = currency;
	}

	public AmountField(final String name, WidgetWithErrors errorsViewGrid) {
		this.errorsWidget = errorsViewGrid;
		setName(name);
		setTitle(name);
		addBlurHandler(getBlurHandler());
		setKeyPressFilter("[0-9.]");
		setAmount(0.00D);
		((TextBox) getMainWidget()).setTextAlignment(TextBoxBase.ALIGN_RIGHT);
	}

	private void setKeyPressFilter(String string) {
	}

	protected BlurHandler getBlurHandler() {

		BlurHandler blurHandler = new BlurHandler() {

			Object value = null;

			public void onBlur(BlurEvent event) {
				try {
					errorsWidget.clearError(this);
					value = getValue();

					if (value == null)
						return;
					Double amount = DataUtils.getAmountStringAsDouble(value
							.toString());
					if (!AccounterValidator.isAmountTooLarge(amount)
							&& !AccounterValidator.isAmountNegative(amount)) {
						setAmount(amount);
					}
					if (AmountField.this.blurHandler != null) {
						AmountField.this.blurHandler.onBlur(event);
					}
				} catch (Exception e) {
					if (e instanceof InvalidEntryException) {
						// BaseView.errordata.setHTML("<li>" + e.getMessage());
						// BaseView.commentPanel.setVisible(true);
						errorsWidget.addError(this, e.getMessage());
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

	public void setCurrency(ClientCurrency currency) {
		this.currency = currency;
		if (showCurrency) {
			setTitle(getName() + "(" + currency.getFormalName() + ")");
		} else {
			setTitle(getName());
		}
	}

	public ClientCurrency getCurrency() {
		return currency;
	}

	public void setShowCurrency(boolean showCurrency) {
		this.showCurrency = showCurrency;
	}

	public boolean isShowCurrency() {
		return showCurrency;
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
	// //
	// }
	//
	// }
	// };
	// return focusHandler;
	//
	// }

}
