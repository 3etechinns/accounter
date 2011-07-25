package com.vimukti.accounter.web.client.ui.serverreports;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.AccounterButton;
import com.vimukti.accounter.web.client.ui.forms.ComboBoxItem;
import com.vimukti.accounter.web.client.ui.forms.DateItem;
import com.vimukti.accounter.web.client.ui.reports.ReportToolbar;

/**
 * 
 * @author Sujana Bijjam
 */

public class SalesPurchasesReportToolbar extends ReportToolbar {
	private DateItem fromItem;
	private DateItem toItem;
	protected ComboBoxItem statusCombo;
	private ComboBoxItem dateRangeItem;
	private AccounterButton updateButton;
	public static int OPEN = 1;
	public static int COMPLETED = 2;
	public static int CANCELLED = 3;
	public static int ALL = 4;
	private int status = OPEN;

	public SalesPurchasesReportToolbar() {
		createControls();
		// selectedDateRange = FinanceApplication.getReportsMessages().all();
	}

	private void createControls() {
		String[] statusArray = { "Open", "Completed", "Cancelled", "All" };

		String[] dateRangeArray = { "All", "This Week", "This Month",
				"Last Week", "Last Month", "This Financial Year",
				"Last Financial Year", "This Financial Quarter",
				"Last Financial Quarter", "Financial Year To Date", "Custom" };

		statusCombo = new ComboBoxItem();
		statusCombo.setTitle("Status");
		statusCombo.setValueMap(statusArray);
		statusCombo.setDefaultValue(statusArray[0]);
		statusCombo.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {

				if (statusCombo.getValue().toString().equals("Open")) {
					status = OPEN;
				} else if (statusCombo.getValue().toString()
						.equals("Completed")) {
					status = COMPLETED;
				} else if (statusCombo.getValue().toString().equals("All")) {
					status = ALL;
				} else
					status = CANCELLED;
				ClientFinanceDate startDate = fromItem.getDate();
				ClientFinanceDate endDate = toItem.getDate();
				reportview.makeReportRequest(status, startDate, endDate);

			}
		});

		dateRangeItem = new ComboBoxItem();
		dateRangeItem.setTitle("Date Range");
		dateRangeItem.setValueMap(dateRangeArray);
		dateRangeItem.setDefaultValue(dateRangeArray[0]);

		dateRangeItem.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {

				dateRangeChanged(dateRangeItem.getValue().toString());

			}
		});

		fromItem = new DateItem();
		fromItem.setDatethanFireEvent(Accounter.getStartDate());
		fromItem.setTitle("From");

		toItem = new DateItem();
		ClientFinanceDate date = Accounter.getCompany()
				.getLastandOpenedFiscalYearEndDate();

		if (date != null)
			toItem.setDatethanFireEvent(date);
		else
			toItem.setDatethanFireEvent(new ClientFinanceDate());

		toItem.setTitle("To");
		toItem.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				startDate = fromItem.getValue();
				endDate = toItem.getValue();
			}
		});
		updateButton = new AccounterButton("Update");
		updateButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				setStartDate(fromItem.getDate());
				setEndDate(toItem.getDate());

				changeDates(fromItem.getDate(), toItem.getDate());
				dateRangeItem.setDefaultValue("Custom");
				setSelectedDateRange("Custom");

			}
		});

		// fromItem.setDisabled(true);
		// toItem.setDisabled(true);
		// updateButton.setEnabled(false);

		AccounterButton printButton = new AccounterButton("Print");
		// printButton.setTop(2);
		// printButton.setWidth(40);
		printButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {

			}

		});

		if (UIUtils.isMSIEBrowser()) {
			dateRangeItem.setWidth("200px");
			statusCombo.setWidth("200px");
		}
		addItems(statusCombo, dateRangeItem, fromItem, toItem);
		add(updateButton);

		updateButton.enabledButton();

		this.setCellVerticalAlignment(updateButton,
				HasVerticalAlignment.ALIGN_MIDDLE);
	}

	@Override
	public void changeDates(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		fromItem.setValue(startDate);
		toItem.setValue(endDate);
		reportview.makeReportRequest(status, startDate, endDate);

		// itemSelectionHandler.onItemSelectionChanged(TYPE_ACCRUAL, startDate,
		// endDate);
	}

	@Override
	public void setDateRanageOptions(String... dateRanages) {
		dateRangeItem.setValueMap(dateRanages);
	}

	@Override
	public void setDefaultDateRange(String defaultDateRange) {
		dateRangeItem.setDefaultValue(defaultDateRange);
		dateRangeChanged(defaultDateRange);
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
}
