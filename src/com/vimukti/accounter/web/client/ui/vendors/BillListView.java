package com.vimukti.accounter.web.client.ui.vendors;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Lists.BillsList;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.AccounterWarningType;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.BaseListView;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.VendorsActionFactory;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.SelectItem;
import com.vimukti.accounter.web.client.ui.grids.BillsListGrid;

/**
 * 
 * @author venki.p
 * @modified by Ravi kiran.G
 * 
 */
public class BillListView extends BaseListView<BillsList> {
	SelectItem viewSelect;
	DynamicForm form;
	Label lab1;
	protected List<BillsList> allEnterBills;
	private VendorsMessages vendorsConstants = GWT
			.create(VendorsMessages.class);
	private SelectItem currentView;
	public String viewType;

	private BillListView() {
		super();
	}

	public BillListView(String viewType) {
		super();
		this.viewType = viewType;
	}

	public static BillListView getInstance() {
		return new BillListView();
	}

	@Override
	protected Action getAddNewAction() {
		return VendorsActionFactory.getEnterBillsAction();
	}

	@Override
	protected String getAddNewLabelString() {
		return vendorsConstants.addaNewBill();
	}

	@Override
	protected String getListViewHeading() {
		return vendorsConstants.billsAndItemReceiptsList();
	}

	@Override
	public void initListCallback() {
		super.initListCallback();
		FinanceApplication.createHomeService().getBillsAndItemReceiptList(this);

	}

	@Override
	public void updateInGrid(BillsList objectTobeModified) {
	}

	@Override
	protected void initGrid() {
		grid = new BillsListGrid(false);
		grid.init();
		grid.setViewType(FinanceApplication.getVendorsMessages().all());
	}

	@Override
	protected SelectItem getSelectItem() {
		currentView = new SelectItem(FinanceApplication.getVendorsMessages()
				.currentView());
		currentView.setHelpInformation(true);
		currentView.setValueMap(FinanceApplication.getVendorsMessages().open(),
				FinanceApplication.getVendorsMessages().Voided(),
				FinanceApplication.getVendorsMessages().overDue(),
				FinanceApplication.getVendorsMessages().all()
		// , "Deleted"
				);
		if (UIUtils.isMSIEBrowser())
			currentView.setWidth("150px");

		if (this.viewType != null)
			currentView.setSelected(viewType);
		else
			currentView.setSelected(FinanceApplication.getCustomersMessages()
					.all());

		currentView.addChangeHandler(new ChangeHandler() {

			@SuppressWarnings("unchecked")
			@Override
			public void onChange(ChangeEvent event) {
				if (currentView.getValue() != null) {
					grid.setViewType(currentView.getValue().toString());
					filterList(currentView.getValue().toString());
				}
			}
		});
		return currentView;
	}

	protected void filterList(String text) {
		grid.removeAllRecords();
		if (text.equalsIgnoreCase("Open")) {
			List<BillsList> openRecs = new ArrayList<BillsList>();
			List<BillsList> allRecs = initialRecords;
			for (BillsList rec : allRecs) {
				if (rec.getType() == ClientTransaction.TYPE_ENTER_BILL
						|| rec.getType() == ClientTransaction.TYPE_VENDOR_CREDIT_MEMO
						&& DecimalUtil.isGreaterThan(rec.getBalance(), 0)) {
					if (!rec.isDeleted() && !rec.isVoided())
						openRecs.add(rec);
				}
			}
			grid.setRecords(openRecs);

		} else if (text.equalsIgnoreCase("Voided")) {
			List<BillsList> voidedRecs = new ArrayList<BillsList>();
			List<BillsList> allRecs = initialRecords;
			for (BillsList rec : allRecs) {
				if (rec.isVoided() && !rec.isDeleted()) {
					voidedRecs.add(rec);
				}
			}
			grid.setRecords(voidedRecs);

		} else if (text.equalsIgnoreCase("Over Due")) {
			List<BillsList> overDueRecs = new ArrayList<BillsList>();
			List<BillsList> allRecs = initialRecords;
			for (BillsList rec : allRecs) {
				if (rec.getType() == ClientTransaction.TYPE_ENTER_BILL
						&& new ClientFinanceDate().after(rec.getDueDate())
						&& DecimalUtil.isGreaterThan(rec.getBalance(), 0)) {
					overDueRecs.add(rec);
				}
			}
			grid.setRecords(overDueRecs);

		}
		// else if (currentView.getValue().toString().equalsIgnoreCase(
		// "Deleted")) {
		// List<BillsList> deletedRecs = new ArrayList<BillsList>();
		// List<BillsList> allRecs = initialRecords;
		// for (BillsList rec : allRecs) {
		// if (rec.isDeleted()) {
		// deletedRecs.add(rec);
		// }
		// }
		//
		// grid.setRecords(deletedRecs);
		// }
		if (text.equalsIgnoreCase("All")) {
			grid.setRecords(initialRecords);

		}
		if (grid.getRecords().isEmpty())
			grid.addEmptyMessage(AccounterWarningType.RECORDSEMPTY);

	}

	@Override
	public void updateGrid(IAccounterCore core) {
		initListCallback();
	}

	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);

	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {

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
}
