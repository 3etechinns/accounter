package com.vimukti.accounter.web.client.ui.vendors;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientItemReceipt;
import com.vimukti.accounter.web.client.core.ClientPurchaseOrder;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.Lists.PurchaseOrdersAndItemReceiptsList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.grids.DialogGrid;
import com.vimukti.accounter.web.client.ui.grids.DialogGrid.RecordDoubleClickHandler;

public class VendorBillListDialog extends BaseDialog {

	private VendorBillView view;
	// private CustomersMessages customerConstants = GWT
	// .create(CustomersMessages.class);
	private DialogGrid grid;
	private List<PurchaseOrdersAndItemReceiptsList> list;
	public ClientVendor preVendor;

	public VendorBillListDialog(VendorBillView view,
			List<PurchaseOrdersAndItemReceiptsList> list) {
		this.view = view;
		this.list = list;
		// setTitle("");
		setText(Accounter.messages().purchaseOrderList());
		initRPCService();
		createControls();
		setWidth("700px");
		setQuoteList(list);
		show();
		center();
	}

	private void createControls() {
		VerticalPanel mainLayout = new VerticalPanel();
		mainLayout.setSize("100%", "100%");
		mainLayout.setSpacing(3);
		Label infoLabel = new Label(Accounter.messages().selectPurchaseOrder()
				+ Accounter.messages().selectDocument());

		mainLayout.add(infoLabel);

		grid = new DialogGrid(false);
		grid.addColumns(Accounter.messages().date(), Accounter.messages()
				.no(), Accounter.messages().type(), messages.payeeName(Global
				.get().Vendor()), Accounter.messages().total(), Accounter
				.messages().remainingTotal());
		grid.setCellsWidth(new Integer[] { 70, 50, 90, -1, 90, 95 });
		grid.setView(this);
		grid.init();

		grid
				.addRecordDoubleClickHandler(new RecordDoubleClickHandler<PurchaseOrdersAndItemReceiptsList>() {

					@Override
					public void OnCellDoubleClick(
							PurchaseOrdersAndItemReceiptsList core, int column) {

						setRecord(core);
						// try {
						// ClientEstimate record = (ClientEstimate) core;
						//
						// String estimateId = record.getID();
						// selectedEstimate = getEstimate(estimateId);
						//
						// if (invoiceView != null && selectedEstimate != null)
						// invoiceView.selectedQuote(selectedEstimate);
						//
						// removeFromParent();
						//
						// } catch (Exception e) {
						// Accounter.showError("Error Loading Quote...");
						// }

					}
				});

		// getGridData();
		// setQuoteList(estimates);

		mainLayout.add(grid);

		HorizontalPanel helpButtonLayout = new HorizontalPanel();

		Button helpButton = new Button(messages.help());
		helpButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				Accounter.showError(Accounter.messages().sorryNoHelp());

			}

		});
		helpButtonLayout.add(helpButton);

		HorizontalPanel okButtonLayout = new HorizontalPanel();
		okButtonLayout.setSpacing(3);

		Button okButton = new Button(messages.ok());
		okButton.setWidth("100px");
		okButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {

				PurchaseOrdersAndItemReceiptsList record = (PurchaseOrdersAndItemReceiptsList) grid
						.getSelection();
				setRecord(record);

				// try {
				// ClientEstimate selectedEstimate = (ClientEstimate) grid
				// .getSelection();
				// if (invoiceView != null && selectedEstimate != null)
				// invoiceView.selectedQuote(selectedEstimate);
				// removeFromParent();
				//
				// } catch (Exception e) {
				// Accounter.showError("Error Loading Quote...");
				// }

			}

		});
		okButtonLayout.add(okButton);

		Button cancelButton = new Button(messages.cancel());
		cancelButton.setWidth("100px");
		cancelButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				removeFromParent();
			}

		});
		okButtonLayout.add(cancelButton);
		HorizontalPanel buttonLayout = new HorizontalPanel();
		buttonLayout.setWidth("100%");
		// buttonLayout.add(helpButtonLayout);
		buttonLayout.add(okButtonLayout);
		buttonLayout.setCellHorizontalAlignment(okButtonLayout,
				HasHorizontalAlignment.ALIGN_RIGHT);

		mainLayout.add(buttonLayout);
		mainLayout.setSize("100%", "100%");
		add(mainLayout);

	}

	protected void setRecord(PurchaseOrdersAndItemReceiptsList record) {

		if (record != null) {
			if (record.getType() == ClientTransaction.TYPE_PURCHASE_ORDER) {
				getPurchaseOrder(record);

			} else {
				getItemReceipt(record);
			}
		}

		removeFromParent();

	}

	private void getItemReceipt(PurchaseOrdersAndItemReceiptsList record) {
		AccounterAsyncCallback<ClientItemReceipt> callback = new AccounterAsyncCallback<ClientItemReceipt>() {

			@Override
			public void onException(AccounterException caught) {
				Accounter.showError(Accounter.messages()
						.errorLoadingItemReceipt());
			}

			@Override
			public void onResultSuccess(ClientItemReceipt result) {
				if (result != null)
					view.selectedItemReceipt(result);

			}

		};
		rpcGetService.getObjectById(AccounterCoreType.ITEMRECEIPT, record
				.getTransactionId(), callback);

	}

	private void getPurchaseOrder(PurchaseOrdersAndItemReceiptsList record) {
		AccounterAsyncCallback<ClientPurchaseOrder> callback = new AccounterAsyncCallback<ClientPurchaseOrder>() {

			@Override
			public void onException(AccounterException caught) {
				Accounter.showError(Accounter.messages()
						.errorLoadingPurchaseOrder());
			}

			@Override
			public void onResultSuccess(ClientPurchaseOrder result) {
				if (result != null)
					view.selectedPurchaseOrder(result);

			}

		};
		rpcGetService.getObjectById(AccounterCoreType.PURCHASEORDER, record
				.getTransactionId(), callback);

	}

	public void setQuoteList(List<PurchaseOrdersAndItemReceiptsList> result) {
		if (result == null)
			return;
		this.list = result;
		grid.removeAllRecords();
		if (list.size() > 0) {
			for (PurchaseOrdersAndItemReceiptsList rec : list) {
				grid.addData(rec);
			}
		} else
			grid.addEmptyMessage(Accounter.messages().noRecordsToShow());
	}

	public Object getGridColumnValue(IAccounterCore obj, int index) {
		PurchaseOrdersAndItemReceiptsList record = (PurchaseOrdersAndItemReceiptsList) obj;
		if (record != null) {
			switch (index) {
			case 0:
				return UIUtils.dateFormat(record.getDate());
			case 1:
				return record.getTransactionNumber();
			case 2:
				return Utility.getTransactionName(record.getType());
			case 3:
				return record.getVendorName();
			case 4:
				return amountAsString(record.getTotal());
			case 5:
				return amountAsString(record.getRemainingTotal());
			}
		}
		return null;

	}

	public void setFocus() {
		// cancelBtn.setFocus(true);
	}

	@Override
	protected boolean onOK() {
		// TODO Auto-generated method stub
		return true;
	}
}