package com.vimukti.accounter.web.client.ui.setup;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SetupTrackBillsAndTimePage extends AbstractSetupPage {

	private static final String TRACK_BIllS = "Track bills";
	private static final String TRACKING_TIME = "Tracling Time";
	private VerticalPanel mainPanel;
	private HTML billsdescription, billsmanageCashflowHtml1,
			billsmanageCashflowHtml2, billsmanageCashflowHtml3,
			billssubtitleHtml, timedescription, timemanageCashflowHtml1,
			timemanageCashflowHtml2, timemanageCashflowHtml3, timesubtitleHtml;
	private RadioButton billsyesRadioButton, billsnoRadioButton,
			timeyesRadioButton, timenoRadioButton;

	@Override
	public String getHeader() {
		return this.accounterConstants.managingBills();
	}

	@Override
	public VerticalPanel getPageBody() {
		createControls();
		return mainPanel;
	}

	/**
	 * Create all Controls
	 */
	public void createControls() {
		mainPanel = new VerticalPanel();

		billsdescription = new HTML(
				accounterConstants.billstrackingdescription());
		billsmanageCashflowHtml1 = new HTML(
				accounterConstants.billstrackingmanageCashflowStep1());
		billsmanageCashflowHtml2 = new HTML(
				accounterConstants.billstrackingmanageCashflowStep2());
		billsmanageCashflowHtml3 = new HTML(
				accounterConstants.billstrackingmanageCashflowStep3());
		mainPanel.add(billsdescription);
		mainPanel.add(billsmanageCashflowHtml1);
		mainPanel.add(billsmanageCashflowHtml2);
		mainPanel.add(billsmanageCashflowHtml3);

		billssubtitleHtml = new HTML(accounterConstants.doyouwantTrackBills());
		billssubtitleHtml.setStyleName("BOLD");
		mainPanel.add(billssubtitleHtml);
		billsyesRadioButton = new RadioButton(TRACK_BIllS,
				accounterConstants.yes());

		mainPanel.add(billsyesRadioButton);

		billsnoRadioButton = new RadioButton(TRACK_BIllS,
				accounterConstants.no());

		mainPanel.add(billsnoRadioButton);

		timedescription = new HTML(accounterConstants.timetrackingdescription());
		timedescription.setStyleName("BOLD");
		timemanageCashflowHtml1 = new HTML(
				accounterConstants.timetrackingflowStep1());
		timemanageCashflowHtml2 = new HTML(
				accounterConstants.timetrackingflowStep2());
		timemanageCashflowHtml3 = new HTML(
				accounterConstants.timetrackingflowStep3());
		mainPanel.add(timedescription);
		mainPanel.add(timemanageCashflowHtml1);
		mainPanel.add(timemanageCashflowHtml2);
		mainPanel.add(timemanageCashflowHtml3);

		timesubtitleHtml = new HTML(accounterConstants.doyouwantTrackTime());
		billssubtitleHtml.setStyleName("BOLD");
		mainPanel.add(timesubtitleHtml);
		timeyesRadioButton = new RadioButton(TRACKING_TIME,
				accounterConstants.yes());

		mainPanel.add(timeyesRadioButton);

		timenoRadioButton = new RadioButton(TRACKING_TIME,
				accounterConstants.no());

		mainPanel.add(timenoRadioButton);

	}

	@Override
	public void onLoad() {

		if (preferences.isDoyouKeepTrackofBills()) {
			billsyesRadioButton.setValue(true);
		} else {
			billsnoRadioButton.setValue(true);
		}
		if (preferences.isDoYouKeepTrackOfTime()) {
			timeyesRadioButton.setValue(true);
		} else {
			timenoRadioButton.setValue(true);
		}
	}

	@Override
	public void onSave() {
		if (billsyesRadioButton.getValue()) {
			preferences.setDoyouKeepTrackofBills(true);
		} else {
			preferences.setDoyouKeepTrackofBills(false);
		}

		if (timeyesRadioButton.getValue()) {
			preferences.setDoYouKeepTrackOfTime(true);
		} else {
			preferences.setDoYouKeepTrackOfTime(false);
		}
	}

	@Override
	public boolean doShow() {
		// TODO Auto-generated method stub
		return true;
	}

}
