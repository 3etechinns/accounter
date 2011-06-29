/**
 * @author Murali.A
 *This class displays the log records.
 *Each time 20 records only will be displayed in the grid
 */
package com.vimukti.accounter.web.client.ui;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientFinanceLogger;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.core.AccounterButton;
import com.vimukti.accounter.web.client.ui.forms.DateItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.grids.FinanceLogginGrid;

/**
 * @author Murali.A
 * 
 */
public class FinanceLogView extends AbstractBaseView<ClientFinanceLogger> {

	public ScrollPanel messageTxtPnl;
	private FinanceLogginGrid grid;
	protected long lastRecordID;
	protected long firstRecordID;
	private DateItem dateItm;
	protected boolean isDateChanged;
	protected boolean isNext = true;
	protected boolean isPrevious;
	protected boolean isDateChanged2;
	private Anchor prvsHyprLink;
	private Anchor nextHyprLnk;

	@Override
	public void init() {
		super.init();
		createControls();
	}

	private void createControls() {

		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.setWidth("100%");
		grid = new FinanceLogginGrid(false);
		grid.isEnable = false;
		grid.init();
		grid.setWidth("100%");
		grid.setHeight("280px");
		grid.setView(this);
		Label label = new Label(FinanceApplication.getCompanyMessages()
				.detailedLog());
		label.addStyleName("bold");
		messageTxtPnl = new ScrollPanel();
		messageTxtPnl.addStyleName("logview-border");

		prvsHyprLink = new Anchor() {
			@Override
			public void setEnabled(boolean enabled) {
				isPrevious = enabled;
				if (!enabled)
					this.getElement().getStyle().setColor("gray");
				else {
					this.getElement().getStyle().clearColor();
				}
				super.setEnabled(enabled);
			}
		};
		prvsHyprLink.setHTML("<b>Previous</b>");
		prvsHyprLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (!isPrevious)
					return;
				nextHyprLnk.setEnabled(true);
				// isNext = false;
				// isPrevious = true;
				if (isDateChanged2) {
					getLogsForDate(UIUtils.dateToString(dateItm
							.getEnteredDate()));
				} else {
					fillGridWithPreviousRecords();
				}
			}
		});

		nextHyprLnk = new Anchor() {
			@Override
			public void setEnabled(boolean enabled) {
				isNext = enabled;
				if (!enabled)
					this.getElement().getStyle().setColor("gray");
				else {
					this.getElement().getStyle().clearColor();
				}
				super.setEnabled(enabled);
			}
		};
		nextHyprLnk.setHTML("<b>Next</b>");
		nextHyprLnk.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (!isNext)
					return;
				prvsHyprLink.setEnabled(true);
				// isNext = true;
				// isPrevious = false;
				if (isDateChanged2) {
					getLogsForDate(UIUtils.dateToString(dateItm
							.getEnteredDate()));
				} else {
					fillGridWithNextRecords();
				}
			}
		});

		HorizontalPanel buttonLayout = new HorizontalPanel();
		buttonLayout.setSpacing(15);
		buttonLayout.add(prvsHyprLink);
		buttonLayout.add(nextHyprLnk);

		HorizontalPanel datePanel = new HorizontalPanel();
		datePanel.setWidth("100%");

		dateItm = new DateItem();
		dateItm.setTitle(FinanceApplication.getCompanyMessages().getLogUpto());
		DynamicForm dateForm = new DynamicForm();
		dateForm.setFields(dateItm);
		datePanel.add(dateForm);
		datePanel.setCellHorizontalAlignment(dateForm,
				HasHorizontalAlignment.ALIGN_RIGHT);

		AccounterButton getLogByDateBtn = new AccounterButton();
		getLogByDateBtn.setText(FinanceApplication.getCompanyMessages().get());
		getLogByDateBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				prvsHyprLink.setEnabled(true);
				nextHyprLnk.setEnabled(true);
				isDateChanged = true;
				isNext = true;
				getLogsForDate(UIUtils.dateToString(dateItm.getEnteredDate()));
				isDateChanged2 = true;
			}
		});
		datePanel.add(getLogByDateBtn);
		getLogByDateBtn.enabledButton();
		datePanel.setCellHorizontalAlignment(dateForm,
				HasHorizontalAlignment.ALIGN_RIGHT);

		ScrollPanel gridPanel = new ScrollPanel();
		gridPanel.add(grid);

		mainPanel.add(datePanel);
		mainPanel.add(gridPanel);
		mainPanel.add(buttonLayout);
		mainPanel.setCellHorizontalAlignment(buttonLayout,
				HasHorizontalAlignment.ALIGN_RIGHT);
		mainPanel.addStyleName("margin-b");
		mainPanel.add(label);
		mainPanel.add(messageTxtPnl);

		setSize("100%", "100%");
		add(mainPanel);
	}

	protected void getLogsForDate(String dateToString) {
		long id = isDateChanged ? -1 : (isNext ? lastRecordID : firstRecordID);
		rpcUtilService.getLog(dateToString, id, isNext,
				new AsyncCallback<List<ClientFinanceLogger>>() {

					@Override
					public void onSuccess(List<ClientFinanceLogger> result) {
						if (result != null && result.size() != 0) {
							grid.setRecords(result);
							firstRecordID = result.get(0).getId();
							lastRecordID = result.get(result.size() - 1)
									.getId();
							isDateChanged = false;
						} else
							onFailure(null);
					}

					@Override
					public void onFailure(Throwable caught) {
						grid.clear();
						grid.addEmptyMessage(FinanceApplication
								.getCustomersMessages().norecordstoshow());
					}
				});
	}

	@Override
	public void initData() {
		fillGrid();
		super.initData();
	}

	private void fillGrid() {
		rpcUtilService.getLog(-1, true,
				new AsyncCallback<List<ClientFinanceLogger>>() {

					@Override
					public void onSuccess(List<ClientFinanceLogger> result) {
						if (result != null && result.size() != 0) {
							if (result.size() < 20) {
								prvsHyprLink.setEnabled(false);
								nextHyprLnk.setEnabled(false);
							} else {
								prvsHyprLink.setEnabled(true);
								nextHyprLnk.setEnabled(true);
							}
							grid.setRecords(result);
							lastRecordID = result.get(result.size() - 1)
									.getId();
							firstRecordID = result.get(0).getId();
						} else
							onFailure(null);
					}

					@Override
					public void onFailure(Throwable caught) {
						prvsHyprLink.setEnabled(false);
						nextHyprLnk.setEnabled(false);
						firstRecordID = -1;
						lastRecordID = -1;
						grid.clear();
						grid.addEmptyMessage(FinanceApplication
								.getCustomersMessages().norecordstoshow());
					}
				});
	}

	public void fillGridWithNextRecords() {

		rpcUtilService.getLog(lastRecordID, true,
				new AsyncCallback<List<ClientFinanceLogger>>() {

					@Override
					public void onSuccess(List<ClientFinanceLogger> result) {
						if (result != null && result.size() != 0) {
							if (result.size() < 20)
								nextHyprLnk.setEnabled(false);
							grid.setRecords(result);
							firstRecordID = result.get(0).getId();
							lastRecordID = result.get(result.size() - 1)
									.getId();
						} else
							onFailure(null);
					}

					@Override
					public void onFailure(Throwable caught) {
						firstRecordID = lastRecordID;
						nextHyprLnk.setEnabled(false);
						grid.clear();
						grid.addEmptyMessage(FinanceApplication
								.getCustomersMessages().norecordstoshow());
					}
				});
	}

	public void fillGridWithPreviousRecords() {
		rpcUtilService.getLog(firstRecordID, false,
				new AsyncCallback<List<ClientFinanceLogger>>() {

					@Override
					public void onSuccess(List<ClientFinanceLogger> result) {
						if (result != null && result.size() != 0) {
							if (result.size() < 20)
								prvsHyprLink.setEnabled(false);
							grid.setRecords(result);
							firstRecordID = result.get(0).getId();
							lastRecordID = result.get(result.size() - 1)
									.getId();
						} else {
							onFailure(null);
						}
					}

					@Override
					public void onFailure(Throwable caught) {
						prvsHyprLink.setEnabled(false);
						lastRecordID = firstRecordID;
						grid.clear();
						grid.addEmptyMessage(FinanceApplication
								.getCustomersMessages().norecordstoshow());
					}
				});
	}

	@Override
	public void deleteFailed(Throwable caught) {

	}

	@Override
	public void deleteSuccess(Boolean result) {

	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {

	}

	@Override
	public void fitToSize(int height, int width) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEdit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void print() {
		// TODO Auto-generated method stub

	}

	@Override
	public void printPreview() {
		// TODO Auto-generated method stub

	}

	@Override
	protected String getViewTitle() {
		return FinanceApplication.getCompanyMessages().showLog();
	}
}
