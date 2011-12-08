package com.vimukti.accounter.mobile.commands;
import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.Unit;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.requirements.ShowListRequirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.settings.StockAdjustmentList;
import com.vimukti.accounter.web.server.FinanceTool;

public class StockAdjustmentListCommand extends NewAbstractCommand {

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return null;
	}

	@Override
	protected String getDetailsMessage() {
		return null;
	}

	@Override
	protected void setDefaultValues(Context context) {
	}

	@Override
	public String getSuccessMessage() {
		return "Success";
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new ShowListRequirement<StockAdjustmentList>(
				"stockadjustments", getMessages().pleaseSelect(
						"any " + getMessages().stockAdjustment()
								+ " to view details"), 40) {

			@Override
			protected String onSelection(StockAdjustmentList value) {
				return "update stock adjustment " + value.getStockAdjustment();
			}

			@Override
			protected String getShowMessage() {
				return getMessages().stockAdjustments();
			}

			@Override
			protected String getEmptyString() {
				return getMessages().noRecordsToShow();
			}

			@Override
			protected Record createRecord(StockAdjustmentList value) {
				Record record = new Record(value);
				record.add(getMessages().wareHouse(), value.getWareHouse());
				record.add(getMessages().itemName(), value.getItem());
				Unit unit = (Unit) CommandUtils.getServerObjectById(value
						.getQuantity().getUnit(), AccounterCoreType.UNIT);
				record.add(getMessages().adjustmentQty(), value.getQuantity()
						.getValue() + " " + unit.getType());
				return record;
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add("create stockadjustment");
			}

			@Override
			protected boolean filter(StockAdjustmentList e, String name) {
				return false;
			}

			@Override
			protected ArrayList<StockAdjustmentList> getLists(Context context) {
				try {
					return new FinanceTool().getInventoryManager()
							.getStockAdjustments(getCompanyId());
				} catch (AccounterException e) {
					e.printStackTrace();
				}
				return new ArrayList<StockAdjustmentList>();

			}
		});
	}
}