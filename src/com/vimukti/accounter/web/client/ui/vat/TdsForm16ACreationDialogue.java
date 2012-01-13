package com.vimukti.accounter.web.client.ui.vat;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.combo.VendorCombo;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.util.DayAndMonthUtil;

public class TdsForm16ACreationDialogue extends BaseDialog {

	private DynamicForm form;
	private TextItem location;
	private TextItem tdsCertificateNumber;
	private DynamicForm form0;
	private DateField form16AprintDate;
	private VendorCombo vendorCombo;

	private long vendorID;
	private String datesRange;
	private String place;
	private String printDate;

	int dateRAngeType = 1;

	private SelectCombo chalanQuarterPeriod;
	private Button emailBUtton;
	private Button coveringLetter;
	private SelectCombo monthlyCombo;
	private DateField fromDate;
	private DateField toDate;

	public TdsForm16ACreationDialogue() {
		super("TDS Acknowledgement Form",
				"Add the details you get from the TIN Website and press create 16A form");
		setWidth("650px");

		okbtn.setText("Generate 16A form");
		okbtn.setWidth("150px");

		emailBUtton = new Button(messages.email());
		emailBUtton.setWidth("80px");
		emailBUtton.setFocus(true);

		emailBUtton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {

				sendEmail();
			}
		});

		coveringLetter = new Button("Generate covering letter");
		coveringLetter.setFocus(true);

		coveringLetter.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {

				generateCoveringLetter();
			}
		});

		footerLayout.add(emailBUtton);
		footerLayout.add(coveringLetter);
		createControls();
		center();
	}

	protected void generateCoveringLetter() {

		printDate = form16AprintDate.getDate().toString();
		UIUtils.generateForm16A(vendorID, "", "", printDate, 1);

	}

	private void createControls() {
		form = new DynamicForm();
		form.setWidth("100%");
		form.setHeight("100%");

		HorizontalPanel layout = new HorizontalPanel();
		HorizontalPanel layout1 = new HorizontalPanel();
		VerticalPanel vPanel = new VerticalPanel();

		List<ClientVendor> vendors = getCompany().getActiveVendors();
		vendorCombo = new VendorCombo("Select Deductee");
		vendorCombo.initCombo(vendors);
		vendorCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientVendor>() {

					@Override
					public void selectedComboBoxItem(ClientVendor selectItem) {
						vendorID = selectItem.getID();
					}
				});

		chalanQuarterPeriod = new SelectCombo("Select Quarter");
		chalanQuarterPeriod.setHelpInformation(true);
		chalanQuarterPeriod.initCombo(getFinancialQuatersList());
		chalanQuarterPeriod.setSelectedItem(0);
		chalanQuarterPeriod.setPopupWidth("500px");
		chalanQuarterPeriod
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						if (selectItem.equals(getFinancialQuatersList().get(
								chalanQuarterPeriod.getSelectedIndex()))) {
						}
					}
				});

		monthlyCombo = new SelectCombo("Select Months");
		monthlyCombo.setHelpInformation(true);
		monthlyCombo.initCombo(getMonthsList());
		monthlyCombo.setPopupWidth("500px");
		monthlyCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {

					}
				});

		location = new TextItem("Place");
		location.setHelpInformation(true);

		ClientFinanceDate todaysDate = new ClientFinanceDate();

		form16AprintDate = new DateField(messages.date());
		form16AprintDate.setHelpInformation(true);
		form16AprintDate.setColSpan(1);
		form16AprintDate.setTitle(messages.date());
		form16AprintDate.setValue(todaysDate);

		fromDate = new DateField("From Date");
		fromDate.setHelpInformation(true);
		fromDate.setColSpan(1);
		fromDate.setTitle("From Date");
		fromDate.setValue(todaysDate);

		toDate = new DateField("To Date");
		toDate.setHelpInformation(true);
		toDate.setColSpan(1);
		toDate.setTitle("To Date");
		toDate.setValue(todaysDate);

		tdsCertificateNumber = new TextItem("TDS Certificate No.");
		tdsCertificateNumber.setHelpInformation(true);

		form0 = new DynamicForm();
		form0.setWidth("100%");

		form.setItems(tdsCertificateNumber, location, form16AprintDate);
		form0.setItems(vendorCombo, chalanQuarterPeriod, monthlyCombo,
				fromDate, toDate);

		HorizontalPanel radioButtonPanel = new HorizontalPanel();

		String[] sports = { "Quarterly", "Monthly", "Between Dates" };

		final RadioButton button1 = new RadioButton("group", sports[0]);
		button1.setValue(true);
		final RadioButton button2 = new RadioButton("group", sports[1]);
		button2.setValue(false);
		final RadioButton button3 = new RadioButton("group", sports[2]);
		button3.setValue(false);

		ClickHandler handler = new ClickHandler() {
			public void onClick(ClickEvent e) {
				if (e.getSource() == button1) {
					dateRAngeType = 1;
					chalanQuarterPeriod.show();
					monthlyCombo.hide();
					fromDate.hide();
					toDate.hide();
				} else if (e.getSource() == button2) {
					dateRAngeType = 2;
					chalanQuarterPeriod.hide();
					monthlyCombo.show();
					fromDate.hide();
					toDate.hide();
				} else if (e.getSource() == button3) {
					dateRAngeType = 3;
					chalanQuarterPeriod.hide();
					monthlyCombo.hide();
					fromDate.show();
					toDate.show();
				}
			}
		};

		chalanQuarterPeriod.show();
		monthlyCombo.hide();
		fromDate.hide();
		toDate.hide();

		button1.addClickHandler(handler);
		button2.addClickHandler(handler);
		button3.addClickHandler(handler);

		radioButtonPanel.add(button1);
		radioButtonPanel.add(button2);
		radioButtonPanel.add(button3);

		layout.add(form0);
		layout1.add(form);
		vPanel.add(radioButtonPanel);
		vPanel.add(layout);
		vPanel.add(layout1);

		setBodyLayout(vPanel);

	}

	private List<String> getMonthsList() {

		ArrayList<String> list = new ArrayList<String>();

		list.add(DayAndMonthUtil.january());
		list.add(DayAndMonthUtil.february());
		list.add(DayAndMonthUtil.march());
		list.add(DayAndMonthUtil.april());
		list.add(DayAndMonthUtil.may_full());
		list.add(DayAndMonthUtil.june());
		list.add(DayAndMonthUtil.july());
		list.add(DayAndMonthUtil.august());
		list.add(DayAndMonthUtil.september());
		list.add(DayAndMonthUtil.october());
		list.add(DayAndMonthUtil.november());
		list.add(DayAndMonthUtil.december());

		return list;
	}

	protected void sendEmail() {

	}

	@Override
	protected ValidationResult validate() {
		ValidationResult result = new ValidationResult();

		return result;
	}

	@Override
	protected boolean onOK() {

		if (location.getValue() != null) {
			place = location.getValue();
		} else {
			place = "";
		}

		printDate = form16AprintDate.getDate().toString();
		String frmDt = null, toDt = null;
		int finYear = new ClientFinanceDate().getYear();
		if (dateRAngeType == 1) {
			int qtrSelected = chalanQuarterPeriod.getSelectedIndex();
			switch (qtrSelected) {
			case 0:
				frmDt = "1/4/" + Integer.toString(finYear);
				toDt = "30/6/" + Integer.toString(finYear);
				break;
			case 1:
				frmDt = "1/7/" + Integer.toString(finYear);
				toDt = "30/9/" + Integer.toString(finYear);
				break;
			case 2:
				frmDt = "1/10/" + Integer.toString(finYear);
				toDt = "30/12/" + Integer.toString(finYear);
				break;
			case 3:
				frmDt = "1/1/" + Integer.toString(finYear);
				toDt = "30/3/" + Integer.toString(finYear);
				break;

			default:
				break;
			}

		} else if (dateRAngeType == 2) {
			int mnSelected = monthlyCombo.getSelectedIndex();
			frmDt = "1/" + Integer.toString(mnSelected + 1) + "/"
					+ Integer.toString(finYear);
			toDt = "30/" + Integer.toString(mnSelected + 1) + "/"
					+ Integer.toString(finYear);

		} else if (dateRAngeType == 3) {
			frmDt = fromDate.getDate().toString();
			toDt = toDate.getDate().toString();
		}

		datesRange = frmDt + "-" + toDt;
		UIUtils.generateForm16A(vendorID, datesRange, place, printDate, 0);
		return false;

	}

	@Override
	public void setFocus() {

	}

	private List<String> getFormTypes() {
		ArrayList<String> list = new ArrayList<String>();

		list.add("26Q");
		list.add("27Q");
		list.add("27EQ");

		return list;
	}

}
