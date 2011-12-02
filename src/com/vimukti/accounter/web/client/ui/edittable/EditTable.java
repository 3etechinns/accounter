package com.vimukti.accounter.web.client.ui.edittable;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
import com.google.gwt.user.client.ui.HTMLTable.RowFormatter;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.Accounter;

public abstract class EditTable<R> extends SimplePanel {

	protected AccounterMessages messages = Accounter.messages();
	private FlexTable table;
	private List<EditColumn<R>> columns = new ArrayList<EditColumn<R>>();
	private CellFormatter cellFormatter;
	private RowFormatter rowFormatter;
	private List<R> rows = new ArrayList<R>();
	private boolean isDisabled;
	private boolean columnsCreated;

	public EditTable() {
		this.addStyleName("editTable");
		table = new FlexTable();
		table.setWidth("100%");
		this.add(table);
		cellFormatter = table.getCellFormatter();
		rowFormatter = table.getRowFormatter();
		rowFormatter.addStyleName(0, "editheader");
	}

	public void addColumn(EditColumn<R> column) {
		columns.add(column);
		int index = columns.size() - 1;
		column.setTable(this);
		table.setWidget(0, index, column.getHeader());
		// Set width
		int width = column.getWidth();
		if (width != -1) {
			cellFormatter.setWidth(0, index, width + "px");
		}
	}

	public void setDisabled(boolean isDesabled) {
		if (this.isDisabled != isDesabled) {
			this.isDisabled = isDesabled;
			updateHeaderState(isDesabled);
			for (R r : rows) {
				update(r);
			}
		}
	}

	/**
	 * Update a given row
	 * 
	 * @param row
	 */
	public void update(R row) {
		int index = rows.indexOf(row);
		index += 1;// for header
		RenderContext<R> context = new RenderContext<R>(this, row);
		context.setDesable(isDisabled);
		context.setCellFormatter(cellFormatter);
		context.setRowFormatter(rowFormatter);
		for (int x = 0; x < columns.size(); x++) {
			EditColumn<R> column = columns.get(x);
			IsWidget widget = table.getWidget(index, x);
			column.render(widget, context);
		}
	}

	public void updateFromGUI(R row) {
		int index = rows.indexOf(row);
		index += 1;// for header
		for (int x = 0; x < columns.size(); x++) {
			EditColumn<R> column = columns.get(x);
			IsWidget widget = table.getWidget(index, x);
			column.updateFromGUI(widget, row);
		}
	}

	/**
	 * Add a new row
	 * 
	 * @param row
	 */
	public void add(R row) {
		createColumns();
		rows.add(row);
		int index = rows.size() - 1;
		index += 1;// for header
		RenderContext<R> context = new RenderContext<R>(this, row);
		context.setCellFormatter(cellFormatter);
		context.setDesable(isDisabled);
		context.setRowFormatter(rowFormatter);
		for (int x = 0; x < columns.size(); x++) {
			EditColumn<R> column = columns.get(x);
			IsWidget widget = column.getWidget(context);
			table.setWidget(index, x, widget);
			column.render(widget, context);
		}
	}

	/**
	 * Delete given row
	 * 
	 * @param row
	 */
	public void delete(R row) {
		int index = rows.indexOf(row);
		rows.remove(row);
		if (index != -1) {
			index += 1;// For header
			table.removeRow(index);
		}
	}

	/**
	 * Return copy list of all rows
	 * 
	 * @return
	 */
	public List<R> getAllRows() {
		return new ArrayList<R>(rows);
	}

	/**
	 * Remove all rows from table
	 */
	public void clear() {
		for (int x = 1; x <= rows.size(); x++) {
			table.removeRow(1);
		}
		rows.clear();
	}

	public void setAllRows(List<R> rows) {
		clear();
		for (R row : rows) {
			add(row);
		}

	}

	public void addRows(List<R> rows) {
		for (R row : rows) {
			add(row);
		}
	}

	public List<R> getSelectedRecords(int colInd) {
		List<R> selected = new ArrayList<R>();
		for (int x = 0; x < rows.size(); x++) {
			IsWidget widget = table.getWidget(x + 1, colInd);
			if (widget instanceof CheckBox) {
				CheckBox checkedWidget = (CheckBox) widget;
				if (checkedWidget.getValue()) {
					selected.add(rows.get(x));
				}
			}
		}
		return selected;
	}

	public void checkColumn(int row, int colInd, boolean isChecked) {
		IsWidget widget = table.getWidget(row + 1, colInd);
		if (widget instanceof CheckBox) {
			CheckBox checkedWidget = (CheckBox) widget;
			checkedWidget.setValue(isChecked);
			if (isChecked) {
				rowFormatter.removeStyleName(row + 1, "selected");
			} else {
				rowFormatter.addStyleName(row + 1, "selected");
			}
		}
	}

	public boolean isChecked(R row, int colInd) {
		int x = getAllRows().indexOf(row);
		IsWidget widget = table.getWidget(x + 1, colInd);
		if (widget instanceof CheckBox) {
			CheckBox checkedWidget = (CheckBox) widget;
			return checkedWidget.getValue();
		}
		return false;
	}

	public boolean isDisabled() {
		return isDisabled;
	}

	protected void onDelete(R obj) {

	}

	@Override
	protected void onAttach() {
		createColumns();
		super.onAttach();
	}

	protected void createColumns() {
		if (!columnsCreated) {
			initColumns();
		}
		columnsCreated = true;
	}

	protected abstract void initColumns();

	private void updateHeaderState(boolean isDisable) {
		for (int x = 0; x < columns.size(); x++) {
			Widget widget = table.getWidget(0, x);
			if (widget instanceof CheckBox) {
				((CheckBox) widget).setEnabled(!isDisable);
			}
		}
	}

	public List<EditColumn<R>> getColumns() {
		return columns;
	}

	public FlexTable getTable() {
		return table;
	}

	public void reDraw() {
		clear();
		columnsCreated = false;
		columns.clear();
		table.removeAllRows();
		rowFormatter.addStyleName(0, "editheader");
		createColumns();
	}

	public void updateColumnHeaders() {
		for (EditColumn<R> column : columns) {
			column.updateHeader();
		}
	}

	public void addEmptyMessage(String emptyMessage) {
		this.table.setText(1, 0, emptyMessage);
		this.table.getFlexCellFormatter().setStyleName(1, 0,
				"norecord-empty-message");
		this.table.addStyleName("no_records");
	}

	protected abstract boolean isInViewMode();
}
