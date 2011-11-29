package com.vimukti.accounter.web.client.ui.reports;

import java.util.Date;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Calendar;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.FormItem;

public abstract class ReportToolbar extends HorizontalPanel {
	protected ClientFinanceDate startDate;
	protected ClientFinanceDate endDate;
	private DynamicForm form;
	protected ReportToolBarItemSelectionHandler itemSelectionHandler;
	public static final int TYPE_ACCRUAL = 0;
	public static final int TYPE_CASH = 1;
	private String selectedDateRange = "";
	protected AbstractReportView<?> reportview;
	private long payeeId;

	public boolean isToolBarComponentChanged;

	public ReportToolbar() {
		createControls();
	}

	private void createControls() {
		setSize("100%", "10px");
		// setBackgroundColor("#dedede");

		form = new DynamicForm();
		form.setSize("100%", "100%");
		form.setNumCols(8);

		startDate = Accounter.getStartDate();
		endDate = Accounter.getCompany().getCurrentFiscalYearEndDate();
		// getLastandOpenedFiscalYearEndDate();
		itemSelectionHandler = new ReportToolBarItemSelectionHandler() {

			public void onItemSelectionChanged(int type,
					ClientFinanceDate startDate, ClientFinanceDate endDate) {

			}
		};
		add(form);
	}

	public ReportToolBarItemSelectionHandler getItemSelectionHandler() {
		return itemSelectionHandler;
	}

	public void setItemSelectionHandler(
			ReportToolBarItemSelectionHandler itemSelectionHandler) {
		this.itemSelectionHandler = itemSelectionHandler;
	}

	public void addItems(FormItem... items) {
		form.setItems(items);
	}

	public ClientFinanceDate getStartDate() {
		return startDate != null && startDate.getDate() == 0 ? new ClientFinanceDate()
				: startDate;
	}

	public void setStartDate(ClientFinanceDate startDate) {
		this.startDate = startDate;
	}

	public ClientFinanceDate getEndDate() {
		return (endDate != null && endDate.getDate() == 0) ? new ClientFinanceDate()
				: endDate;
	}

	public void setEndDate(ClientFinanceDate endDate) {
		this.endDate = endDate;
	}

	public void dateRangeChanged(String dateRange) {
		try {
			ClientFinanceDate date = new ClientFinanceDate();
			if (!getSelectedDateRange().equals(Accounter.messages().all())
					&& dateRange.equals(Accounter.messages().all())) {
				setSelectedDateRange(Accounter.messages().all());
				// startDate = FinanceApplication.getStartDate();
				// endDate = Utility.getLastandOpenedFiscalYearEndDate();
				// if (endDate == null)
				// endDate = new ClientFinanceDate();
				startDate = new ClientFinanceDate(0);
				endDate = new ClientFinanceDate(0);

			} else if (!getSelectedDateRange().equals(
					Accounter.messages().today())
					&& dateRange.equals(Accounter.messages().today())) {
				startDate = new ClientFinanceDate();
				endDate = Accounter.getCompany().getCurrentFiscalYearEndDate();
				// .getLastandOpenedFiscalYearEndDate();
				setSelectedDateRange(Accounter.messages().today());
			} else if (!getSelectedDateRange().equals(
					Accounter.messages().endThisWeek())
					&& dateRange.equals(Accounter.messages().endThisWeek())) {
				startDate = getWeekStartDate();
				endDate = new ClientFinanceDate(new Date(
						(long) getWeekEndDate()));
				setSelectedDateRange(Accounter.messages().endThisWeek());
			} else if (!getSelectedDateRange().equals(
					Accounter.messages().endThisWeekToDate())
					&& dateRange.equals(Accounter.messages()
							.endThisWeekToDate())) {
				startDate = getWeekStartDate();
				endDate = new ClientFinanceDate();
				setSelectedDateRange(Accounter.messages().endThisWeekToDate());
			} else if (!getSelectedDateRange().equals(
					Accounter.messages().endThisMonth())
					&& dateRange.equals(Accounter.messages().endThisMonth())) {
				int lastDay = getMonthLastDate(date.getMonth(), date.getYear());
				startDate = new ClientFinanceDate(date.getYear(),
						date.getMonth(), 1);
				endDate = new ClientFinanceDate(date.getYear(),
						date.getMonth(), lastDay);
				setSelectedDateRange(Accounter.messages().endThisMonth());
			} else if (!getSelectedDateRange().equals(
					Accounter.messages().endThisMonthToDate())
					&& dateRange.equals(Accounter.messages()
							.endThisMonthToDate())) {
				startDate = new ClientFinanceDate(date.getYear(),
						date.getMonth(), 1);
				endDate = Accounter.getCompany().getCurrentFiscalYearEndDate();
				// .getLastandOpenedFiscalYearEndDate();
				setSelectedDateRange(Accounter.messages().endThisMonthToDate());
			} else if (!getSelectedDateRange().equals(
					Accounter.messages().endThisFiscalQuarter())
					&& dateRange.equals(Accounter.messages()
							.endThisFiscalQuarter())) {
				// changes are needed for calculating Fiscal Quarter,
				// according
				// to user preferences.
				int month = (date.getMonth()) % 3;
				int startMonth = date.getMonth() - month;
				int endMonth = startMonth + 2;
				startDate = new ClientFinanceDate(date.getYear(), startMonth, 1);
				endDate = new ClientFinanceDate(date.getYear(), endMonth,
						getMonthLastDate(endMonth, date.getYear()));
				setSelectedDateRange(Accounter.messages()
						.endThisFiscalQuarter());
			} else if (!getSelectedDateRange().equals(
					Accounter.messages().endThisFiscalQuarterToDate())
					&& dateRange.equals(Accounter.messages()
							.endThisFiscalQuarterToDate())) {
				int month = (date.getMonth()) % 3;
				int startMonth = date.getMonth() - month;

				int endMonth = startMonth + 2;
				startDate = new ClientFinanceDate(date.getYear(), startMonth, 1);
				endDate = Accounter.getCompany().getCurrentFiscalYearEndDate();
				// .getLastandOpenedFiscalYearEndDate();
				setSelectedDateRange(Accounter.messages()
						.endThisFiscalQuarterToDate());
			} else if (!getSelectedDateRange().equals(
					Accounter.messages().endThisCalanderQuarter())
					&& dateRange.equals(Accounter.messages()
							.endThisCalanderQuarter())) {
				int month = (date.getMonth()) % 3;
				int startMonth = date.getMonth() - month;
				int endMonth = startMonth + 2;
				startDate = new ClientFinanceDate(date.getYear(), startMonth, 1);
				endDate = new ClientFinanceDate(date.getYear(), endMonth,
						getMonthLastDate(endMonth, date.getYear()));
				setSelectedDateRange(Accounter.messages()
						.endThisCalanderQuarter());
			} else if (!getSelectedDateRange().equals(
					Accounter.messages().endThisCalanderQuarterToDate())
					&& dateRange.equals(Accounter.messages()
							.endThisCalanderQuarterToDate())) {
				int month = (date.getMonth()) % 3;
				int startMonth = date.getMonth() - month;
				startDate = new ClientFinanceDate(date.getYear(), startMonth, 1);
				endDate = Accounter.getCompany().getCurrentFiscalYearEndDate();
				// .getLastandOpenedFiscalYearEndDate();
				setSelectedDateRange(Accounter.messages()
						.endThisCalanderQuarterToDate());
			} else if (!getSelectedDateRange().equals(
					Accounter.messages().endThisFiscalYear())
					&& dateRange.equals(Accounter.messages()
							.endThisFiscalYear())) {

				startDate = new ClientFinanceDate(date.getYear(), 0, 1);
				endDate = new ClientFinanceDate(date.getYear(), 11, 31);
				setSelectedDateRange(Accounter.messages().endThisFiscalYear());
			} else if (!getSelectedDateRange().equals(
					Accounter.messages().endThisFiscalYearToDate())
					&& dateRange.equals(Accounter.messages()
							.endThisFiscalYearToDate())) {
				startDate = new ClientFinanceDate(date.getYear(), 0, 1);
				endDate = Accounter.getCompany().getCurrentFiscalYearEndDate();
				// .getLastandOpenedFiscalYearEndDate();
				setSelectedDateRange(Accounter.messages()
						.endThisFiscalYearToDate());
			} else if (!getSelectedDateRange().equals(
					Accounter.messages().endThisCalanderYear())
					&& dateRange.equals(Accounter.messages()
							.endThisCalanderYear())) {
				startDate = new ClientFinanceDate(date.getYear(), 0, 1);
				endDate = new ClientFinanceDate(date.getYear(), 11, 31);
				setSelectedDateRange(Accounter.messages().endThisCalanderYear());
			} else if (!getSelectedDateRange().equals(
					Accounter.messages().endThisCalanderYearToDate())
					&& dateRange.equals(Accounter.messages()
							.endThisCalanderYearToDate())) {
				startDate = new ClientFinanceDate(date.getYear(), 0, 1);
				endDate = Accounter.getCompany().getCurrentFiscalYearEndDate();
				// .getLastandOpenedFiscalYearEndDate();
				setSelectedDateRange(Accounter.messages()
						.endThisCalanderYearToDate());
				changeDates(startDate, endDate);
			} else if (!getSelectedDateRange().equals(
					Accounter.messages().endYesterday())
					&& dateRange.equals(Accounter.messages().endYesterday())) {
				// startDate = new ClientFinanceDate(date.getYear(),
				// date.getMonth(), date
				// .getDate() - 1);
				startDate = Accounter.getStartDate();
				endDate = Accounter.getCompany().getCurrentFiscalYearEndDate();
				// .getLastandOpenedFiscalYearEndDate();
				int day = endDate.getDay();
				endDate.setDay(day - 1);
				setSelectedDateRange((Accounter.messages().endYesterday()));
			} else if (!getSelectedDateRange().equals(
					Accounter.messages().endPreviousFiscalQuarter())
					&& dateRange.equals(Accounter.messages()
							.endPreviousFiscalQuarter())) {
				startDate = new ClientFinanceDate();
				endDate = Accounter.getCompany().getCurrentFiscalYearEndDate();
				// .getLastandOpenedFiscalYearEndDate();
				setSelectedDateRange(Accounter.messages()
						.endPreviousFiscalQuarter());
				getCurrentQuarter();
			} else if (!getSelectedDateRange().equals(
					Accounter.messages().endLastCalendarQuarter())
					&& dateRange.equals(Accounter.messages()
							.endLastCalendarQuarter())) {
				startDate = new ClientFinanceDate();
				endDate = Accounter.getCompany().getCurrentFiscalYearEndDate();
				// .getLastandOpenedFiscalYearEndDate();
				setSelectedDateRange(Accounter.messages()
						.endLastCalendarQuarter());
				getCurrentQuarter();
			} else if (!getSelectedDateRange().equals(
					Accounter.messages().previousFiscalYearSameDates())
					&& dateRange.equals(Accounter.messages()
							.previousFiscalYearSameDates())) {
				setSelectedDateRange(Accounter.messages()
						.previousFiscalYearSameDates());
				getCurrentQuarter();
			} else if (!getSelectedDateRange().equals(
					Accounter.messages().previousFiscalYearSameDates())
					&& dateRange.equals(Accounter.messages()
							.previousFiscalYearSameDates())) {

				startDate = new ClientFinanceDate(this.startDate.getYear() - 1,
						this.startDate.getMonth(), this.startDate.getDay());
				endDate = new ClientFinanceDate(this.endDate.getYear() - 1,
						this.endDate.getMonth(), this.endDate.getDay());
				// startDate = new ClientFinanceDate(date.getYear() - 1, 0, 1);
				// endDate = new ClientFinanceDate(date.getYear() - 1, 11, 1);
				setSelectedDateRange(Accounter.messages()
						.previousFiscalYearSameDates());
			} else if (!getSelectedDateRange().equals(
					Accounter.messages().lastCalenderYear())
					&& dateRange
							.equals(Accounter.messages().lastCalenderYear())) {
				startDate = new ClientFinanceDate(date.getYear(), 0, 1);
				endDate = new ClientFinanceDate(date.getYear(), 11, 31);

				setSelectedDateRange(Accounter.messages().lastCalenderYear());
			} else if (!getSelectedDateRange().equals(
					Accounter.messages().previousCalenderYear())
					&& dateRange.equals(Accounter.messages()
							.previousCalenderYear())) {
				startDate = new ClientFinanceDate(date.getYear() - 1, 0, 1);
				endDate = new ClientFinanceDate(date.getYear() - 1, 11, 31);
				// startDate = new ClientFinanceDate(this.startDate.getYear() -
				// 1,
				// this.startDate
				// .getMonth(), this.startDate.getDate());
				// startDate = new ClientFinanceDate(this.endDate.getYear() - 1,
				// this.endDate
				// .getMonth(), this.endDate.getDate());
				// startDate.setYear(this.startDate.getYear() - 1);
				// endDate.setYear(this.endDate.getYear() - 1);
				setSelectedDateRange(Accounter.messages()
						.previousCalenderYear());
			} else if (!getSelectedDateRange().equals(
					Accounter.messages().lastMonth())
					&& dateRange.equals(Accounter.messages().lastMonth())) {
				int day;
				if (date.getMonth() == 0) {
					day = getMonthLastDate(11, date.getYear() - 1);
					startDate = new ClientFinanceDate(date.getYear() - 1, 11, 1);
					endDate = new ClientFinanceDate(date.getYear() - 1, 11, day);
				} else {
					day = getMonthLastDate(date.getMonth() - 1, date.getYear());
					startDate = new ClientFinanceDate(date.getYear(),
							date.getMonth() - 1, 1);
					endDate = new ClientFinanceDate(date.getYear(),
							date.getMonth() - 1, day);
				}
				setSelectedDateRange(Accounter.messages().lastMonth());
			} else if (!getSelectedDateRange().equals(
					Accounter.messages().last3Months())
					&& dateRange.equals(Accounter.messages().last3Months())) {
				int day;
				if (date.getMonth() == 0) {
					startDate = new ClientFinanceDate(date.getYear() - 1, 9, 1);
					day = getMonthLastDate(11, date.getYear() - 1);
					endDate = new ClientFinanceDate(date.getYear() - 1, 11, day);
				} else if (date.getMonth() == 1) {
					startDate = new ClientFinanceDate(date.getYear() - 1, 10, 1);
					day = getMonthLastDate(date.getMonth() - 1, date.getYear());
					endDate = new ClientFinanceDate(date.getYear(),
							date.getMonth() - 1, day);
				} else if (date.getMonth() == 2) {
					startDate = new ClientFinanceDate(date.getYear() - 1, 11, 1);
					day = getMonthLastDate(date.getMonth() - 1, date.getYear());
					endDate = new ClientFinanceDate(date.getYear(),
							date.getMonth() - 1, day);
				} else {
					startDate = new ClientFinanceDate(date.getYear(),
							date.getMonth() - 3, 1);
					day = getMonthLastDate(date.getMonth() - 1, date.getYear());
					endDate = new ClientFinanceDate(date.getYear(),
							date.getMonth() - 1, day);
				}
				setSelectedDateRange(Accounter.messages().last3Months());
			} else if (!getSelectedDateRange().equals(
					Accounter.messages().last6Months())
					&& dateRange.equals(Accounter.messages().last6Months())) {
				int day;
				if (date.getMonth() == 0) {
					startDate = new ClientFinanceDate(date.getYear() - 1, 6, 1);
					day = getMonthLastDate(11, date.getYear() - 1);
					endDate = new ClientFinanceDate(date.getYear() - 1, 11, day);
				} else if (date.getMonth() == 1) {
					startDate = new ClientFinanceDate(date.getYear() - 1, 7, 1);
					day = getMonthLastDate(date.getMonth() - 1, date.getYear());
					endDate = new ClientFinanceDate(date.getYear(),
							date.getMonth() - 1, day);
				} else if (date.getMonth() == 2) {
					startDate = new ClientFinanceDate(date.getYear() - 1, 8, 1);
					day = getMonthLastDate(date.getMonth() - 1, date.getYear());
					endDate = new ClientFinanceDate(date.getYear(),
							date.getMonth() - 1, day);
				} else if (date.getMonth() == 3) {
					startDate = new ClientFinanceDate(date.getYear() - 1, 9, 1);
					day = getMonthLastDate(date.getMonth() - 1, date.getYear());
					endDate = new ClientFinanceDate(date.getYear(),
							date.getMonth() - 1, day);
				} else if (date.getMonth() == 4) {
					startDate = new ClientFinanceDate(date.getYear() - 1, 10, 1);
					day = getMonthLastDate(date.getMonth() - 1, date.getYear());
					endDate = new ClientFinanceDate(date.getYear(),
							date.getMonth() - 1, day);
				} else if (date.getMonth() == 5) {
					startDate = new ClientFinanceDate(date.getYear() - 1, 11, 1);
					day = getMonthLastDate(date.getMonth() - 1, date.getYear());
					endDate = new ClientFinanceDate(date.getYear(),
							date.getMonth() - 1, day);
				} else {
					startDate = new ClientFinanceDate(date.getYear(),
							date.getMonth() - 6, 1);
					day = getMonthLastDate(date.getMonth() - 1, date.getYear());
					endDate = new ClientFinanceDate(date.getYear(),
							date.getMonth() - 1, day);
				}
				setSelectedDateRange(Accounter.messages().last6Months());
			} else if (!getSelectedDateRange().equals(
					Accounter.messages().lastYear())
					&& dateRange.equals(Accounter.messages().lastYear())) {
				startDate = new ClientFinanceDate(date.getYear() - 1, 0, 1);
				endDate = new ClientFinanceDate(date.getYear() - 1, 11, 31);
				setSelectedDateRange(Accounter.messages().lastYear());
			} else if (!getSelectedDateRange().equals(
					Accounter.messages().present())
					&& dateRange.equals(Accounter.messages().present())) {
				startDate = new ClientFinanceDate();
				endDate = new ClientFinanceDate();
				setSelectedDateRange(Accounter.messages().present());

			} else if (!getSelectedDateRange().equals(
					Accounter.messages().untilEndOfYear())
					&& dateRange.equals(Accounter.messages().untilEndOfYear())) {
				startDate = new ClientFinanceDate();
				endDate = new ClientFinanceDate(startDate.getYear(), 11, 31);
				setSelectedDateRange(Accounter.messages().untilEndOfYear());

			} else if (!getSelectedDateRange().equals(
					Accounter.messages().thisWeek())
					&& dateRange.equals(Accounter.messages().thisWeek())) {
				startDate = getWeekStartDate();
				endDate.setDay(startDate.getDay() + 6);
				endDate.setMonth(startDate.getMonth());
				endDate.setYear(startDate.getYear());
				setSelectedDateRange(Accounter.messages().thisWeek());
			} else if (!getSelectedDateRange().equals(
					Accounter.messages().thisMonth())
					&& dateRange.equals(Accounter.messages().thisMonth())) {
				startDate = new ClientFinanceDate(date.getYear(),
						date.getMonth(), 1);
				Calendar endCal = Calendar.getInstance();
				endCal.setTime(new ClientFinanceDate().getDateAsObject());
				endCal.set(Calendar.DAY_OF_MONTH,
						endCal.getActualMaximum(Calendar.DAY_OF_MONTH));
				endDate = new ClientFinanceDate(endCal.getTime());
				setSelectedDateRange(Accounter.messages().thisMonth());
			} else if (!getSelectedDateRange().equals(
					Accounter.messages().lastWeek())
					&& dateRange.equals(Accounter.messages().lastWeek())) {

				endDate = getWeekStartDate();
				endDate.setDay(endDate.getDay() - 1);

				Calendar startCal = Calendar.getInstance();
				startCal.setTime(endDate.getDateAsObject());
				startCal.set(Calendar.DAY_OF_MONTH,
						startCal.get(Calendar.DAY_OF_MONTH) - 6);
				startDate = new ClientFinanceDate(startCal.getTime());

				setSelectedDateRange(Accounter.messages().lastWeek());

			} else if (!getSelectedDateRange().equals(
					Accounter.messages().thisFinancialYear())
					&& dateRange.equals(Accounter.messages()
							.thisFinancialYear())) {
				startDate = Accounter.getCompany()
						.getCurrentFiscalYearStartDate();
				endDate = Accounter.getCompany().getCurrentFiscalYearEndDate();
				setSelectedDateRange(Accounter.messages().thisFinancialYear());
			} else if (!getSelectedDateRange().equals(
					Accounter.messages().lastFinancialYear())
					&& dateRange.equals(Accounter.messages()
							.lastFinancialYear())) {
				startDate = Accounter.getCompany()
						.getCurrentFiscalYearStartDate();
				startDate.setYear(startDate.getYear() - 1);
				Calendar endCal = Calendar.getInstance();
				endCal.setTime(Accounter.getCompany()
						.getCurrentFiscalYearEndDate().getDateAsObject());
				endCal.set(Calendar.DAY_OF_MONTH,
						endCal.getActualMaximum(Calendar.DAY_OF_MONTH));
				endDate = new ClientFinanceDate(endCal.getTime());
				endDate.setYear(endDate.getYear() - 1);
				// startDate = new ClientFinanceDate(date.getYear() - 1, 0, 1);
				// endDate = new ClientFinanceDate(date.getYear() - 1, 11, 31);
				setSelectedDateRange(Accounter.messages().lastFinancialYear());
			} else if (!getSelectedDateRange().equals(
					Accounter.messages().thisFinancialQuarter())
					&& dateRange.equals(Accounter.messages()
							.thisFinancialQuarter())) {
				startDate = new ClientFinanceDate();
				endDate = Accounter.getCompany().getCurrentFiscalYearEndDate();
				// .getLastandOpenedFiscalYearEndDate();
				setSelectedDateRange(Accounter.messages()
						.thisFinancialQuarter());
				getCurrentFiscalYearQuarter();
			} else if (!getSelectedDateRange().equals(
					Accounter.messages().lastFinancialQuarter())
					&& dateRange.equals(Accounter.messages()
							.lastFinancialQuarter())) {
				getCurrentFiscalYearQuarter();
				Calendar startDateCal = Calendar.getInstance();
				startDateCal.setTime(startDate.getDateAsObject());
				startDateCal.set(Calendar.MONTH,
						startDateCal.get(Calendar.MONTH) - 3);
				Calendar endDateCal = Calendar.getInstance();
				endDateCal.setTime(endDate.getDateAsObject());
				endDateCal.set(Calendar.MONTH,
						endDateCal.get(Calendar.MONTH) - 3);
				startDate = new ClientFinanceDate(startDateCal.getTime());
				endDate = new ClientFinanceDate(endDateCal.getTime());
				setSelectedDateRange(Accounter.messages()
						.lastFinancialQuarter());
				// getCurrentQuarter();
				// startDate.setYear(startDate.getYear() - 1);
				// endDate.setYear(endDate.getYear() - 1);

			} else if (!getSelectedDateRange().equals(
					Accounter.messages().financialYearToDate())
					&& dateRange.equals(Accounter.messages()
							.financialYearToDate())) {
				startDate = Accounter.getCompany()
						.getCurrentFiscalYearStartDate();
				endDate = new ClientFinanceDate();
				setSelectedDateRange(Accounter.messages().financialYearToDate());
			} else if (!getSelectedDateRange().equals(
					Accounter.messages().thisVATQuarter())
					&& dateRange.equals(Accounter.messages().thisVATQuarter())) {
				startDate = new ClientFinanceDate();
				endDate = Accounter.getCompany().getCurrentFiscalYearEndDate();
				setSelectedDateRange(Accounter.messages().thisVATQuarter());
				getCurrentQuarter();
			} else if (!getSelectedDateRange().equals(
					Accounter.messages().thisVATQuarterToDate())
					&& dateRange.equals(Accounter.messages()
							.thisVATQuarterToDate())) {
				startDate = Accounter.getCompany()
						.getCurrentFiscalYearStartDate();
				endDate = new ClientFinanceDate();
				setSelectedDateRange(Accounter.messages()
						.thisVATQuarterToDate());
				getCurrentQuarter();
				endDate = new ClientFinanceDate();

			} else if (!getSelectedDateRange().equals(
					Accounter.messages().lastVATQuarter())
					&& dateRange.equals(Accounter.messages().lastVATQuarter())) {
				startDate = Accounter.getCompany()
						.getCurrentFiscalYearStartDate();
				endDate = new ClientFinanceDate();
				setSelectedDateRange(Accounter.messages().lastVATQuarter());
				getPreviousQuarter();

			} else if (!getSelectedDateRange().equals(
					Accounter.messages().lastVATQuarterToDate())
					&& dateRange.equals(Accounter.messages()
							.lastVATQuarterToDate())) {
				startDate = Accounter.getCompany()
						.getCurrentFiscalYearStartDate();
				endDate = new ClientFinanceDate();
				setSelectedDateRange(Accounter.messages()
						.lastVATQuarterToDate());
				getPreviousQuarter();
				endDate = new ClientFinanceDate();
			} else if (!getSelectedDateRange().equals(
					Accounter.messages().nextVATQuarter())
					&& dateRange.equals(Accounter.messages().nextVATQuarter())) {
				startDate = Accounter.getCompany()
						.getCurrentFiscalYearStartDate();
				endDate = new ClientFinanceDate();
				setSelectedDateRange(Accounter.messages().nextVATQuarter());
				getNextQuarter();
			} else if (!getSelectedDateRange().equals(
					Accounter.messages().custom())
					&& dateRange.equals(Accounter.messages().custom())) {
				startDate = this.getStartDate();
				endDate = this.getEndDate();
				setSelectedDateRange(Accounter.messages().custom());
			}
			setStartDate(startDate);
			setEndDate(endDate);
			changeDates(startDate, endDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getCurrentFiscalYearQuarter() {

		ClientFinanceDate date = new ClientFinanceDate();
		ClientFinanceDate start = Accounter.getCompany()
				.getCurrentFiscalYearStartDate();
		ClientFinanceDate end = Accounter.getCompany()
				.getCurrentFiscalYearEndDate();

		int currentQuarter = 0;
		ClientFinanceDate quarterStart = Accounter.getCompany()
				.getCurrentFiscalYearStartDate();
		ClientFinanceDate quarterEnd;

		Calendar quarterEndCal = Calendar.getInstance();
		quarterEndCal.setTime(quarterStart.getDateAsObject());
		quarterEndCal
				.set(Calendar.MONTH, quarterEndCal.get(Calendar.MONTH) + 3);
		quarterEndCal.set(Calendar.DAY_OF_MONTH, quarterStart.getDay() - 1);

		quarterEnd = new ClientFinanceDate(quarterEndCal.getTime());

		if (date.after(quarterStart) && date.before(quarterEnd)) {
			currentQuarter = 1;
		}
		quarterStart.setMonth(quarterStart.getMonth() + 3);
		quarterEndCal.setTime(quarterStart.getDateAsObject());
		quarterEndCal
				.set(Calendar.MONTH, quarterEndCal.get(Calendar.MONTH) + 3);
		quarterEndCal.set(Calendar.DAY_OF_MONTH, quarterStart.getDay() - 1);

		quarterEnd = new ClientFinanceDate(quarterEndCal.getTime());
		if (date.after(quarterStart) && date.before(quarterEnd)) {
			currentQuarter = 2;
		}
		quarterStart.setMonth(quarterStart.getMonth() + 3);
		quarterEndCal.setTime(quarterStart.getDateAsObject());
		quarterEndCal
				.set(Calendar.MONTH, quarterEndCal.get(Calendar.MONTH) + 3);
		quarterEndCal.set(Calendar.DAY_OF_MONTH, quarterStart.getDay() - 1);

		quarterEnd = new ClientFinanceDate(quarterEndCal.getTime());
		if (date.after(quarterStart) && date.before(quarterEnd)) {
			currentQuarter = 3;
		}
		quarterStart.setMonth(quarterStart.getMonth() + 3);
		quarterEndCal.setTime(quarterStart.getDateAsObject());
		quarterEndCal
				.set(Calendar.MONTH, quarterEndCal.get(Calendar.MONTH) + 3);
		quarterEndCal.set(Calendar.DAY_OF_MONTH, quarterStart.getDay() - 1);

		quarterEnd = new ClientFinanceDate(quarterEndCal.getTime());
		if (date.after(quarterStart) && date.before(quarterEnd)) {
			currentQuarter = 4;
		}
		switch (currentQuarter) {
		case 1:
			startDate = start;
			Calendar endCal = Calendar.getInstance();
			endCal.setTime(start.getDateAsObject());
			endCal.set(Calendar.MONTH, endCal.get(Calendar.MONTH) + 3);
			endCal.set(Calendar.DAY_OF_MONTH, startDate.getDay() - 1);
			endDate = new ClientFinanceDate(endCal.getTime());
			break;

		case 2:
			startDate = start;
			startDate.setMonth(start.getMonth() + 3);
			Calendar endCal2 = Calendar.getInstance();
			endCal2.setTime(startDate.getDateAsObject());
			endCal2.set(Calendar.MONTH, endCal2.get(Calendar.MONTH) + 3);
			endCal2.set(Calendar.DAY_OF_MONTH, startDate.getDay() - 1);
			endDate = new ClientFinanceDate(endCal2.getTime());
			break;

		case 3:
			startDate = start;
			startDate.setMonth(start.getMonth() + 6);
			Calendar endCal3 = Calendar.getInstance();
			endCal3.setTime(startDate.getDateAsObject());
			endCal3.set(Calendar.MONTH, endCal3.get(Calendar.MONTH) + 3);
			endCal3.set(Calendar.DAY_OF_MONTH, startDate.getDay() - 1);
			endDate = new ClientFinanceDate(endCal3.getTime());
			break;
		default:
			startDate = start;
			startDate.setMonth(start.getMonth() + 9);
			endDate = end;
			break;
		}
	}

	public void getCurrentQuarter() {

		ClientFinanceDate date = new ClientFinanceDate();

		int currentQuarter;
		if ((date.getMonth() + 1) % 3 == 0) {
			currentQuarter = (date.getMonth() + 1) / 3;
		} else {
			currentQuarter = ((date.getMonth() + 1) / 3) + 1;
		}
		switch (currentQuarter) {
		case 1:
			startDate = new ClientFinanceDate(date.getYear(), 0, 1);
			endDate = new ClientFinanceDate(date.getYear(), 2, 31);
			break;

		case 2:
			startDate = new ClientFinanceDate(date.getYear(), 3, 1);
			endDate = new ClientFinanceDate(date.getYear(), 5, 30);
			break;

		case 3:
			startDate = new ClientFinanceDate(date.getYear(), 6, 1);
			endDate = new ClientFinanceDate(date.getYear(), 8, 30);
			break;
		default:
			startDate = new ClientFinanceDate(date.getYear(), 9, 1);
			endDate = new ClientFinanceDate(date.getYear(), 11, 31);
			break;
		}
	}

	public void getPreviousQuarter() {

		ClientFinanceDate date = new ClientFinanceDate();

		int currentQuarter;
		if ((date.getMonth() + 1) % 3 == 0) {
			currentQuarter = (date.getMonth() + 1) / 3;
		} else {
			currentQuarter = ((date.getMonth() + 1) / 3) + 1;
		}
		switch (currentQuarter) {
		case 1:
			startDate = new ClientFinanceDate(date.getYear() - 1, 9, 1);
			endDate = new ClientFinanceDate(date.getYear() - 1, 11, 31);
			break;

		case 2:
			startDate = new ClientFinanceDate(date.getYear(), 0, 1);
			endDate = new ClientFinanceDate(date.getYear(), 2, 31);
			break;

		case 3:
			startDate = new ClientFinanceDate(date.getYear(), 3, 1);
			endDate = new ClientFinanceDate(date.getYear(), 5, 30);
			break;
		default:
			startDate = new ClientFinanceDate(date.getYear(), 6, 1);
			endDate = new ClientFinanceDate(date.getYear(), 8, 30);
			break;
		}
	}

	public void getNextQuarter() {

		ClientFinanceDate date = new ClientFinanceDate();

		int currentQuarter;
		if ((date.getMonth() + 1) % 3 == 0) {
			currentQuarter = (date.getMonth() + 1) / 3;
		} else {
			currentQuarter = ((date.getMonth() + 1) / 3) + 1;
		}
		switch (currentQuarter) {
		case 1:
			startDate = new ClientFinanceDate(date.getYear(), 3, 1);
			endDate = new ClientFinanceDate(date.getYear(), 5, 30);
			break;

		case 2:
			startDate = new ClientFinanceDate(date.getYear(), 6, 1);
			endDate = new ClientFinanceDate(date.getYear(), 8, 30);
			break;

		case 3:
			startDate = new ClientFinanceDate(date.getYear(), 9, 1);
			endDate = new ClientFinanceDate(date.getYear(), 11, 31);
			break;
		default:
			startDate = new ClientFinanceDate(date.getYear() + 1, 0, 1);
			endDate = new ClientFinanceDate(date.getYear() + 1, 2, 31);
			break;
		}
	}

	public int getMonthLastDate(int month, int year) {
		int lastDay;
		switch (month) {
		case 0:
		case 2:
		case 4:
		case 6:
		case 7:
		case 9:
		case 11:
			lastDay = 31;
			break;
		case 1:
			if (year % 4 == 0 && year % 100 == 0)
				lastDay = 29;
			else
				lastDay = 28;
			break;

		default:
			lastDay = 30;
			break;
		}
		return lastDay;
	}

	public ClientFinanceDate getWeekStartDate() {
		ClientFinanceDate date = new ClientFinanceDate();
		int day = date.getDay() % 6;
		ClientFinanceDate newDate = new ClientFinanceDate();
		if (day != 1) {
			newDate.setDay(date.getDay() - day);
		} else {
			newDate.setDay(date.getDay());
		}

		return newDate;
	}

	// public native double getWeekStartDate()/*-{
	// var date= new ClientFinanceDate();
	// var day=date.getDay();
	// var newDate=new ClientFinanceDate();
	// newDate.setDate(date.getDate()-day);
	// var tmp=newDate.getTime();
	// return tmp;
	// }-*/;

	public native double getWeekEndDate()/*-{
		var date = new ClientFinanceDate();
		var day = date.getDay();
		var remainingDays = 6 - day;
		var newDate = new ClientFinanceDate();
		newDate.setDate(date.getDate() + remainingDays);
		var tmp = newDate.getTime();
		return tmp;
	}-*/;

	public abstract void changeDates(ClientFinanceDate startDate,
			ClientFinanceDate endDate);

	public abstract void setStartAndEndDates(ClientFinanceDate startDate,
			ClientFinanceDate endDate);

	public abstract void setDefaultDateRange(String defaultDateRange);

	public void setDateRanageOptions(String... dateRanages) {

	}

	public AbstractReportView<?> getView() {
		return reportview;
	}

	public void setView(AbstractReportView<?> view) {
		this.reportview = view;
	}

	/**
	 * @param selectedDateRange
	 *            the selectedDateRange to set
	 */
	public void setSelectedDateRange(String selectedDateRange) {
		this.selectedDateRange = selectedDateRange;
	}

	/**
	 * @return the selectedDateRange
	 */
	public String getSelectedDateRange() {
		return selectedDateRange;
	}

	public long getPayeeId() {
		return payeeId;
	}

	public void setPayeeId(long payeeId) {
		this.payeeId = payeeId;
		payeeData();
	}

	protected void payeeData() {

	}
}
