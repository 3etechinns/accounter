package com.vimukti.accounter.web.client.ui.edittable.tables;

import java.util.List;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.core.ClientUnit;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.edittable.CheckboxEditColumn;
import com.vimukti.accounter.web.client.ui.edittable.DeleteColumn;
import com.vimukti.accounter.web.client.ui.edittable.EditTable;
import com.vimukti.accounter.web.client.ui.edittable.RenderContext;
import com.vimukti.accounter.web.client.ui.edittable.TextEditColumn;

public abstract class UnitsTable extends EditTable<ClientUnit> {

	@Override
	protected void initColumns() {

		this.addColumn(new CheckboxEditColumn<ClientUnit>() {

			@Override
			protected void onChangeValue(boolean value, ClientUnit obj) {
				onSelectionChanged(obj, value);
			}

			@Override
			public IsWidget getHeader() {
				Label columnHeader = new Label(messages
						.defaultWare());
				return columnHeader;
			}

			@Override
			public void render(IsWidget widget,
					RenderContext<ClientUnit> context) {
				super.render(widget, context);
				((CheckBox) widget).setValue(context.getRow().isDefault());
			}
		});
		this.addColumn(new TextEditColumn<ClientUnit>() {

			@Override
			protected String getValue(ClientUnit row) {
				return row.getType();
			}

			@Override
			protected void setValue(ClientUnit row, String value) {
				row.setType(value);
			}

			@Override
			protected String getColumnName() {
				return messages.unitName();
			}
		});
		this.addColumn(new TextEditColumn<ClientUnit>() {

			@Override
			protected String getValue(ClientUnit row) {
				return String.valueOf(row.getFactor());
			}

			@Override
			protected void setValue(ClientUnit row, String value) {
				try {
					row.setFactor(DataUtils.getAmountStringAsDouble(value));
					getTable().update(row);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			protected String getColumnName() {
				return messages.factor();
			}

		});

		this.addColumn(new DeleteColumn<ClientUnit>());
	}

	private void onSelectionChanged(ClientUnit obj, boolean isChecked) {

		List<ClientUnit> records = getSelectedRecords(0);
		for (ClientUnit contact : records) {
			int index = indexOf(contact);
			checkColumn(index, 0, false);
			contact.setDefault(false);
		}

		int row = indexOf(obj);
		obj.setDefault(isChecked);
		super.checkColumn(row, 0, isChecked);
	}

	public int indexOf(ClientUnit selectedObject) {
		return getAllRows().indexOf(selectedObject);
	}

	public List<ClientUnit> getRecords() {
		return getAllRows();
	}

	public boolean isEmpty() {
		return false;
	}

	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		ClientUnit defaultUnit = getDefaultUnit();
		if (getRecords() == null || getRecords().isEmpty()) {
			result.addError(this, messages
					.pleaseEnterAtleastOneUnit());
		} else if (defaultUnit == null) {
			result.addError(this, messages
					.pleaseSelectDefaultUnit());
		}
		// else if (defaultUnit.getFactor() == 0) {
		// result.addError(this, messages
		// .factorForDefaultUnitShouldNotbeZero());
		// }
		else {
			for (ClientUnit unit : getRecords()) {
				if (unit.getName() == null || unit.getName().isEmpty()) {
					result.addError(
							this,
							messages.pleaseEnter(
									messages.unitName()));
					break;
				} else if (DecimalUtil.isEquals(unit.getFactor(), 0)) {
					result.addError(this, messages
							.factorsShouldNotbeZero());
					break;
				}
			}
		}
		return result;
	}

	public ClientUnit getDefaultUnit() {
		List<ClientUnit> allRows = getAllRows();
		for (ClientUnit unit : allRows) {
			if (unit.isDefault()) {
				return unit;
			}
		}
		return null;
	}
}
