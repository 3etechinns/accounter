package com.vimukti.accounter.web.client.ui.vendors;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.Lists.PaymentsList;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterWarningType;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.BaseListView;
import com.vimukti.accounter.web.client.ui.core.VendorsActionFactory;
import com.vimukti.accounter.web.client.ui.grids.VendorPaymentsListGrid;

/**
 * @modified by Ravi kiran.G
 * 
 */

public class VendorPaymentsListView extends BaseListView<PaymentsList> {

	protected List<PaymentsList> allPayments;
	private SelectCombo currentView;

	private VendorPaymentsListView() {

		super();
	}

	public static VendorPaymentsListView getInstance() {

		return new VendorPaymentsListView();
	}

	@Override
	protected Action getAddNewAction() {

		return VendorsActionFactory.getNewVendorPaymentAction();
	}

	@Override
	protected String getAddNewLabelString() {
		return UIUtils.getVendorString(Accounter.constants()
				.addANewSupplierPayment(), Accounter.constants()
				.addANewVendorPayment());
	}

	@Override
	protected String getListViewHeading() {

		return UIUtils.getVendorString(Accounter.constants()
				.supplierPaymentList(), Accounter.constants()
				.vendorPaymentsList());
	}

	@Override
	public void initListCallback() {
		super.initListCallback();
		Accounter.createHomeService().getVendorPaymentsList(this);

	}

	@Override
	public void updateInGrid(PaymentsList objectTobeModified) {

	}

	@Override
	protected void initGrid() {
		grid = new VendorPaymentsListGrid(false);
		grid.init();
		grid.setViewType(Accounter.constants().notIssued());
	}

	@Override
	public void onSuccess(List<PaymentsList> result) {
		super.onSuccess(result);
		grid.setViewType(Accounter.constants().all());
		filterList(Accounter.constants().all());
	}

	@Override
	protected SelectCombo getSelectItem() {
		currentView = new SelectCombo(Accounter.constants().currentView());
		currentView.setHelpInformation(true);
		listOfTypes = new ArrayList<String>();
		listOfTypes.add(Accounter.constants().notIssued());
		listOfTypes.add(Accounter.constants().issued());
		listOfTypes.add(Accounter.constants().Voided());
		listOfTypes.add(Accounter.constants().all());
		currentView.initCombo(listOfTypes);
		if (UIUtils.isMSIEBrowser())
			currentView.setWidth("150px");

		currentView.setComboItem(Accounter.constants().all());
		currentView
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						if (viewSelect.getSelectedValue() != null) {
							grid.setViewType(currentView.getSelectedValue());
							filterList(currentView.getSelectedValue());
						}
					}
				});
		return currentView;
	}

	@SuppressWarnings("unchecked")
	protected void filterList(String text) {
		grid.removeAllRecords();
		if (currentView.getSelectedValue().equalsIgnoreCase("Not Issued")) {
			List<PaymentsList> notIssuedRecs = new ArrayList<PaymentsList>();
			List<PaymentsList> allRecs = initialRecords;
			for (PaymentsList rec : allRecs) {
				if (Utility.getStatus(rec.getType(), rec.getStatus())
						.equalsIgnoreCase("Not Issued")) {
					notIssuedRecs.add(rec);
				}
			}
			grid.setRecords(notIssuedRecs);
		} else if (currentView.getSelectedValue().equalsIgnoreCase("Issued")) {
			List<PaymentsList> issued = new ArrayList<PaymentsList>();
			List<PaymentsList> allRecs = initialRecords;
			for (PaymentsList rec : allRecs) {
				if (Utility.getStatus(rec.getType(), rec.getStatus())
						.equalsIgnoreCase("Issued")) {
					issued.add(rec);
				}
			}
			grid.setRecords(issued);
		} else if (currentView.getSelectedValue().equalsIgnoreCase("Voided")) {
			List<PaymentsList> voidedRecs = new ArrayList<PaymentsList>();
			List<PaymentsList> allRecs = initialRecords;
			for (PaymentsList rec : allRecs) {
				if (rec.isVoided()
						&& rec.getStatus() != ClientTransaction.STATUS_DELETED) {
					voidedRecs.add(rec);
				}
			}
			grid.setRecords(voidedRecs);
		}
		// else if (currentView.getValue().toString().equalsIgnoreCase(
		// "Deleted")) {
		// List<PaymentsList> deletedRecs = new
		// ArrayList<PaymentsList>();
		// List<PaymentsList> allRecs = initialRecords;
		// for (PaymentsList rec : allRecs) {
		// if (rec.getStatus() == ClientTransaction.STATUS_DELETED) {
		// deletedRecs.add(rec);
		// }
		// }
		// grid.setRecords(deletedRecs);
		//
		// }
		if (currentView.getSelectedValue().equalsIgnoreCase("All")) {
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

	}

	@Override
	public void print() {

	}

	@Override
	public void printPreview() {

	}

	@Override
	protected String getViewTitle() {
		return UIUtils.getVendorString(
				Accounter.constants().supplierPayments(), Accounter.constants()
						.vendorPayments());
	}
}
