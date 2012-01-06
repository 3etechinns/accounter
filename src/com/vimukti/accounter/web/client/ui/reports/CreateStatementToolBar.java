package com.vimukti.accounter.web.client.ui.reports;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.combo.CustomerCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.combo.VendorCombo;
import com.vimukti.accounter.web.client.ui.forms.DateItem;

public class CreateStatementToolBar extends ReportToolbar {
	private DateItem fromItem;
	private DateItem toItem;
	private CustomerCombo customerCombo;
	private VendorCombo vendorCombo;
	private ClientCustomer selectedCusotmer = null;
	private ClientVendor selectedVendor = null;
	private SelectCombo dateRangeItemCombo;
	private List<String> dateRangeItemList;
	private Button updateButton;
	private final boolean isVendor;

	public CreateStatementToolBar(boolean isVendor,
			AbstractReportView reportView) {
		this.isVendor = isVendor;
		this.reportview = reportView;
		createControls();
	}

	public void createControls() {

		String[] dateRangeArray = { messages.all(), messages.thisWeek(),
				messages.thisMonth(), messages.lastWeek(),
				messages.lastMonth(), messages.thisFinancialYear(),
				messages.lastFinancialYear(), messages.thisFinancialQuarter(),
				messages.lastFinancialQuarter(),
				messages.financialYearToDate(), messages.custom() };

		if (isVendor) {
			vendorCombo = new VendorCombo("Choose Vendor", false);
			new StatementReport(true);
			vendorCombo
					.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientVendor>() {

						@Override
						public void selectedComboBoxItem(ClientVendor selectItem) {
							setPayeeId(selectItem.getID());
						}
					});

			if (getPayeeId() != 0) {
				vendorData(Accounter.getCompany().getVendor(getPayeeId()));
			}
		} else {
			customerCombo = new CustomerCombo("Choose Customer", false);
			new StatementReport(false);
			customerCombo
					.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientCustomer>() {

						@Override
						public void selectedComboBoxItem(
								ClientCustomer selectItem) {
							setPayeeId(selectItem.getID());
						}
					});
			if (getPayeeId() != 0) {
				customerData(Accounter.getCompany().getCustomer(getPayeeId()));
			}
		}

		dateRangeItemCombo = new SelectCombo(messages.dateRange());
		dateRangeItemCombo.setHelpInformation(true);
		dateRangeItemList = new ArrayList<String>();
		for (int i = 0; i < dateRangeArray.length; i++) {
			dateRangeItemList.add(dateRangeArray[i]);
		}
		dateRangeItemCombo.initCombo(dateRangeItemList);
		dateRangeItemCombo.setComboItem(messages.thisMonth());
		dateRangeChanged(dateRangeItemCombo.getSelectedValue());
		dateRangeItemCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						dateRangeChanged(dateRangeItemCombo.getSelectedValue());

					}
				});

		fromItem = new DateItem();
		fromItem.setHelpInformation(true);
		fromItem.setDatethanFireEvent(Accounter.getStartDate());
		fromItem.setTitle(messages.from());

		toItem = new DateItem();
		toItem.setHelpInformation(true);
		ClientFinanceDate date = Accounter.getCompany()
				.getCurrentFiscalYearEndDate();
		// .getLastandOpenedFiscalYearEndDate();

		if (date != null)
			toItem.setDatethanFireEvent(date);
		else
			toItem.setDatethanFireEvent(new ClientFinanceDate());

		toItem.setTitle(messages.to());
		toItem.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				startDate = fromItem.getValue();
				endDate = toItem.getValue();
			}
		});
		updateButton = new Button(messages.update());
		updateButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				setStartDate(fromItem.getDate());
				setEndDate(toItem.getDate());
				changeDates(fromItem.getDate(), toItem.getDate());
				dateRangeItemCombo.setDefaultValue(messages.custom());
				dateRangeItemCombo.setComboItem(messages.custom());
				setSelectedDateRange(messages.custom());

			}
		});

		Button printButton = new Button(messages.print());
		printButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

			}

		});

		// if (UIUtils.isMSIEBrowser()) {
		// dateRangeItemCombo.setWidth("200px");
		// }
		if (isVendor) {
			addItems(vendorCombo, dateRangeItemCombo, fromItem, toItem);
		} else {
			addItems(customerCombo, dateRangeItemCombo, fromItem, toItem);
		}
		add(updateButton);
		this.setCellVerticalAlignment(updateButton,
				HasVerticalAlignment.ALIGN_MIDDLE);
		// reportRequest();
	}

	protected void customerData(ClientCustomer selectItem) {
		if (selectItem != null) {
			selectedCusotmer = selectItem;
			ClientFinanceDate startDate = fromItem.getDate();
			ClientFinanceDate endDate = toItem.getDate();
			reportview.makeReportRequest(selectedCusotmer.getID(), startDate,
					endDate);
			reportview.removeEmptyStyle();
			customerCombo.setSelected(selectedCusotmer.getName());
		}
	}

	protected void vendorData(ClientVendor selectItem) {
		if (selectItem != null) {
			selectedVendor = selectItem;
			ClientFinanceDate startDate = fromItem.getDate();
			ClientFinanceDate endDate = toItem.getDate();
			reportview.makeReportRequest(selectedVendor.getID(), startDate,
					endDate);
			reportview.removeEmptyStyle();
			vendorCombo.setSelected(selectedVendor.getName());
		}
	}

	/*
	 * @see
	 * com.vimukti.accounter.web.client.ui.reports.ReportToolbar#changeDates
	 * (java.util.Date, java.util.Date)
	 */
	@Override
	public void changeDates(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		fromItem.setValue(startDate);
		toItem.setValue(endDate);
		if (isVendor) {
			if (selectedVendor != null) {
				reportview.makeReportRequest(selectedVendor.getID(), startDate,
						endDate);
			} else
				reportview.addEmptyMessage("No records to show");
		} else {
			if (selectedCusotmer != null) {
				reportview.makeReportRequest(selectedCusotmer.getID(),
						startDate, endDate);
			} else
				reportview.addEmptyMessage("No records to show");
		}
	}

	@Override
	public void setDefaultDateRange(String defaultDateRange) {
		dateRangeItemCombo.setDefaultValue(defaultDateRange);
		dateRangeItemCombo.setComboItem(defaultDateRange);
		dateRangeChanged(defaultDateRange);
	}

	@Override
	public void setDateRanageOptions(String... dateRanages) {
		dateRangeItemCombo.setValueMap(dateRanages);
	}

	@Override
	public void setStartAndEndDates(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		if (startDate != null && endDate != null) {
			fromItem.setEnteredDate(startDate);
			toItem.setEnteredDate(endDate);
			setStartDate(startDate);
			setEndDate(endDate);
		}

	}

	public void reportRequest() {
		reportview.makeReportRequest(selectedCusotmer.getID(), startDate,
				endDate);
	}

	@Override
	protected void payeeData() {
		if (isVendor) {
			if (getPayeeId() != 0) {
				vendorData(Accounter.getCompany().getVendor(getPayeeId()));
				reportview.makeReportRequest(selectedVendor.getID(), startDate,
						endDate);
			}
		} else {
			if (getPayeeId() != 0) {
				customerData(Accounter.getCompany().getCustomer(getPayeeId()));
				reportview.makeReportRequest(selectedCusotmer.getID(),
						startDate, endDate);
			}
		}
	}
}
