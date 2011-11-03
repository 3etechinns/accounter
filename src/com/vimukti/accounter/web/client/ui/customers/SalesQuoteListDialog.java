package com.vimukti.accounter.web.client.ui.customers;

import java.util.List;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientEstimate;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.grids.DialogGrid;

public class SalesQuoteListDialog extends BaseDialog<ClientEstimate> {

	private SalesOrderView salesView;
	public DialogGrid<ClientEstimate> grid;
	private List<ClientEstimate> estimates;
	private ClientEstimate selectedEstimate;

	public SalesQuoteListDialog(SalesOrderView parentView,
			List<ClientEstimate> estimates) {
		super(Accounter.constants().quoteList());
		salesView = parentView;
		this.estimates = estimates;
		createControl();
		setWidth("600px");
		setQuoteList(estimates);
		show();
		center();
	}

	private void createControl() {

		VerticalPanel mainLayout = new VerticalPanel();
		mainLayout.setSize("100%", "100%");
		mainLayout.setSpacing(3);
		Label infoLabel = new Label(Accounter.constants().selectQuote());

		mainLayout.add(infoLabel);

		grid = new DialogGrid<ClientEstimate>(true);
		grid.addColumns(constants.date(), constants.no(), constants.type(),
				messages.payeeName(Global.get().Customer()), constants
						.total());
		grid.setCellsWidth(70, 30, 60, -1, 60);
		grid.setView(this);
		grid.init();
		setQuoteList(estimates);
		mainLayout.add(grid);
		mainLayout.setSize("100%", "100%");
		setBodyLayout(mainLayout);
	}

	protected ClientEstimate getEstimate(long estimateId) {
		for (ClientEstimate estimate : estimates) {
			if (estimate != null) {
				if (estimate.getID() == estimateId)
					return estimate;
			}
		}
		return null;
	}

	public void setQuoteList(List<ClientEstimate> result) {
		if (result == null)
			return;
		this.estimates = result;
		grid.removeAllRecords();
		for (ClientEstimate rec : estimates) {
			grid.addData(rec);
		}
	}

	public Object getGridColumnValue(ClientEstimate estimate, int index) {
		if (estimate != null) {
			switch (index) {
			case 0:
				return UIUtils.dateFormat(estimate.getDate());
			case 1:
				return estimate.getNumber();
			case 2:
				return Utility.getTransactionName(estimate.getType());
			case 3:
				return company.getCustomer(estimate.getCustomer()).getName();
			case 4:
				return amountAsString(estimate.getTotal());
			}
		}
		return null;

	}

	public void setFocus() {

	}

	@Override
	protected boolean onOK() {
		List<ClientEstimate> selectedEstimates = (List<ClientEstimate>) grid
				.getSelectedRecords();
		for (ClientEstimate estimate : selectedEstimates) {
			if (salesView != null || selectedEstimate != null)
				salesView.selectedQuote(estimate);
		}
		return true;
	}

}
