package com.vimukti.accounter.web.client.ui.widgets;

import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.forms.FormItem;
import com.vimukti.accounter.web.client.ui.forms.LabelItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class CurrencyWidget extends FormItem {

	private VerticalPanel widgetPanel;
	
	private CurrencyChangeListener listener;

	private TextItem factorField;
	private SelectCombo currencyCombo;
	private LabelItem baseCurrencyLbl;

	private String baseCurrency;
	private List<String> currencies;

//	public CurrencyWidget(List<ClientCurrency> currencies,
//			final ClientCurrency baseCurrency) {
//		this.currencies = currencies;
//		this.baseCurrency = baseCurrency;
//		// setNumCols(3);
//
//		currencyCombo = new CurrencyCombo("Currency : ");
//		currencyCombo.initCombo(currencies);
//
//		currencyCombo
//				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientCurrency>() {
//
//					@Override
//					public void selectedComboBoxItem(ClientCurrency selectItem) {
//						currencyChanged(selectItem);
//
//					}
//
//				});
//
//		factorField = new TextItem();
//		factorField.addChangedHandler(new ChangeHandler() {
//
//			@Override
//			public void onChange(ChangeEvent event) {
//				if (event.getSource() == CurrencyWidget.this.factorField) {
//					String factorStr = CurrencyWidget.this.factorField
//							.getValue().toString();
//					factorFieldChagned(Double.parseDouble(factorStr));
//				}
//			}
//
//		});
//		baseCurrencyLbl = new LabelItem();
//		baseCurrencyLbl.setTitle(baseCurrency.getName());
//
//		horizontalPanel = new HorizontalPanel();
//		horizontalPanel.add(currencyCombo.getMainWidget());
//		horizontalPanel.add(factorField.getMainWidget());
//		horizontalPanel.add(baseCurrencyLbl.getMainWidget());
//
//		// setFields(currencyCombo, factorField, baseCurrencyLbl);
//	}

	
	public CurrencyWidget(List<String> currencies, String baseCurrency) {
		this.currencies = currencies;
		this.baseCurrency = baseCurrency;
		// setNumCols(3);

		currencyCombo = new SelectCombo("Currency :");
		currencyCombo.initCombo(currencies);

		currencyCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						currencyChanged(selectItem);

					}

				});

		factorField = new TextItem();
		factorField.setTitle("factor");
		factorField.addChangedHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				if (event.getSource() == CurrencyWidget.this.factorField) {
					String factorStr = CurrencyWidget.this.factorField
							.getValue().toString();
					factorFieldChagned(Double.parseDouble(factorStr));
				}
			}

		});
		baseCurrencyLbl = new LabelItem();
		baseCurrencyLbl.setTitle(baseCurrency);

		
		widgetPanel = new VerticalPanel();
		widgetPanel.add(currencyCombo.getMainWidget());
		widgetPanel.add(factorField.getMainWidget());
		widgetPanel.add(baseCurrencyLbl.getMainWidget());

		// setFields(currencyCombo, factorField, baseCurrencyLbl);
	}
	
	@Override
	public Widget getMainWidget() {
		return widgetPanel;
	}

	@Override
	public String getTitle() {
		return "Currency";
	}
	
	private void factorFieldChagned(double factor) {
		if (listener != null) {
			listener.currencyChanged(currencyCombo.getSelectedValue(), factor);
		}
	}

	private void currencyChanged(String selectItem) {

		boolean factorFieldDisableStatus = selectItem.equals(baseCurrency);
		factorField.setDisabled(factorFieldDisableStatus);
		updateFactorFieldTitle(); // 1<SELCTED currency>=
		double factor = getFactorByRPC(selectItem, baseCurrency);
		factorField.setValue(String.valueOf(factor));
		if (listener != null) {
			listener.currencyChanged(selectItem, factor);
		}
	}

	private double getFactorByRPC(String foreginCurrencyCode,
			String baseCurrencyCode) {
		// TODO GET currency factor BY RPC
		return 0;
	}

	public void setListener(CurrencyChangeListener listener) {
		this.listener = listener;
	}

	public void setCurrencies(List<String> currencies) {
		this.currencies = currencies;
		currencyCombo.initCombo(currencies);
	}

	private void updateFactorFieldTitle() {
		String currency = currencyCombo.getSelectedValue();

		StringBuffer sb = new StringBuffer();
		sb.append(' ').append(1).append(currency).append('=');
		factorField.setTitle(sb.toString());
	}

	public void setSelectedCurrency(ClientCurrency clientCurrency) {
		currencyCombo.setSelected(clientCurrency.getName());
	}

	public String getSelectedCurrency() {
		return currencyCombo.getSelectedValue();
	}

	public void setCurrencyFactor(double factor) {
		factorField.setValue(String.valueOf(factor));
	}

	public double getCurrencyFactor() {
		return Double.parseDouble(factorField.getValue().toString());
	}
}
