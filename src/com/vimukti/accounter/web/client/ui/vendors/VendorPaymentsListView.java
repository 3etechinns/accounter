package com.vimukti.accounter.web.client.ui.vendors;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.Window;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.core.Lists.PaymentsList;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.TransactionsListView;
import com.vimukti.accounter.web.client.ui.grids.VendorPaymentsListGrid;

/**
 * @modified by Ravi kiran.G
 * 
 */

public class VendorPaymentsListView extends TransactionsListView<PaymentsList> {

	protected List<PaymentsList> allPayments;
	private int transactionType;
	private int viewType;

	private VendorPaymentsListView() {
		super(messages.notIssued());
	}

	public VendorPaymentsListView(int transactionType) {
		super(messages.notIssued());
		this.transactionType = transactionType;
	}

	public static VendorPaymentsListView getInstance() {
		return new VendorPaymentsListView();
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected Action getAddNewAction() {
		return ActionFactory.getNewVendorPaymentAction();
	}

	@Override
	protected String getAddNewLabelString() {
		return messages.addANewVendorPayment(Global.get().Vendor());
	}

	@Override
	protected String getListViewHeading() {
		return messages.payeePaymentList(Global.get().Vendor());
	}

	@Override
	public void initListCallback() {
		onPageChange(0, getPageSize());

	}

	@Override
	public void updateInGrid(PaymentsList objectTobeModified) {

	}

	@Override
	protected void initGrid() {
		grid = new VendorPaymentsListGrid(false, transactionType);
		grid.init();
		grid.setViewType(messages.notIssued());
	}

	@Override
	public void onSuccess(PaginationList<PaymentsList> result) {
		grid.removeAllRecords();
		grid.setRecords(result);
		if (grid.getRecords().isEmpty())
			grid.addEmptyMessage(messages.noRecordsToShow());

		grid.sort(12, false);
		Window.scrollTo(0, 0);
		updateRecordsCount(result.getStart(), grid.getTableRowCount(),
				result.getTotalCount());

	}

	@Override
	protected List<String> getViewSelectTypes() {
		List<String> listOfTypes = new ArrayList<String>();
		listOfTypes.add(messages.notIssued());
		listOfTypes.add(messages.issued());
		listOfTypes.add(messages.voided());
		listOfTypes.add(messages.all());
		return listOfTypes;
	}

	@Override
	protected void filterList(String text) {

		grid.removeAllRecords();
		if (viewSelect.getSelectedValue().equalsIgnoreCase("Not Issued")) {
			viewType = ClientTransaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED;
		} else if (viewSelect.getSelectedValue().equalsIgnoreCase("Issued")) {
			viewType = ClientTransaction.STATUS_PAID_OR_APPLIED_OR_ISSUED;
		} else if (viewSelect.getSelectedValue().equalsIgnoreCase("Voided")) {
			viewType = ClientTransaction.VIEW_VOIDED;
		} else if (viewSelect.getSelectedValue().equalsIgnoreCase("All")) {
			viewType = ClientTransaction.TYPE_ALL;
		}
		onPageChange(0, getPageSize());
	}

	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);

	}

	@Override
	public void onEdit() {

	}

	@Override
	public void print() {

	}

	@Override
	public void printPreview() {

	}

	@Override
	protected String getViewTitle() {
		return messages.payeePayments(Global.get().Vendor());
	}

	@Override
	protected int getPageSize() {
		return 10;
	}

	@Override
	protected void onPageChange(int start, int length) {
		Accounter.createHomeService().getVendorPaymentsList(
				getStartDate().getDate(), getEndDate().getDate(), start,
				length, viewType, this);
	}
}
