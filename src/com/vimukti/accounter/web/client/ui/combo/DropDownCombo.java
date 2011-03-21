package com.vimukti.accounter.web.client.ui.combo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.PopupListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientTAXItemGroup;
import com.vimukti.accounter.web.client.core.ClientVATCode;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;

public abstract class DropDownCombo<T> extends TextItem {

	protected IAccounterComboSelectionChangeHandler<T> handler;

	protected AccounterComboConstants comboConstants = GWT
			.create(AccounterComboConstants.class);
	private boolean isAddNewRequire;
	private DropDownTable<T> dropDown;
	private int cols;
	List<T> comboItems = new ArrayList<T>();
	private PopupPanel popup;
	ListGrid grid = null;

	String selectedName;
	int selectedIndex = -1;
	protected T selectedObject;

	List<T> maincomboItems = new ArrayList<T>();

	private ScrollPanel panel;

	public DropDownCombo(String title, boolean isAddNewRequire, int noOfcols) {
		super();
		createControls(title, isAddNewRequire, noOfcols);
		init();
	}

	@SuppressWarnings("unchecked")
	private void createControls(String title, boolean isAddNewRequire,
			int noOfcols) {
		this.isAddNewRequire = isAddNewRequire;
		this.cols = noOfcols;
		super.setName(title);
		setTitle(title);
		this.addStyleName("custom-combo");
		this.addStyleName("dropdown-button");

		dropDown = new DropDownTable(this) {
			@Override
			public void cellClicked(int row, int col) {
				if (DropDownCombo.this.comboItems.size() >= row)
					selectedObject = DropDownCombo.this.comboItems.get(row);
				super.cellClicked(row, col);
			}

			@Override
			public Column[] getColumns() {
				Column[] columns = new Column[cols];
				int i;
				for (i = 0; i < cols; i++) {
					columns[i] = createColumn(i);
				}
				setColumnActions(columns);
				return columns;
			}

			private Column createColumn(final int col) {
				Column<T, String> column = new Column<T, String>(
						new ClickableTextCell()) {

					@Override
					public String getValue(T object) {
						if (object.equals("emptyRow"))
							return "    ";
						else if (object.equals("addNewCaption")) {
							if (cols > 1)
								return (col == 1) ? getDefaultAddNewCaption()
										: "  ";
							else
								return getDefaultAddNewCaption();
						}
						return getColumnData(object, 0, col);
					}

				};
				return column;
			}

			private void setColumnActions(Column[] columns) {
				for (int i = 0; i < cols; i++) {
					columns[i].setFieldUpdater(new FieldUpdater() {

						@Override
						public void update(int index, Object object,
								Object value) {
							selectedIndex = index;
							eventFired(index);
						}
					});
				}
			}

		};

		panel = new ScrollPanel();
		panel.getElement().removeAttribute("style");
		panel.addStyleName("dropdownTable");
		panel.add(dropDown);

		dropDown.setNoColumns(noOfcols);
		dropDown.init(isAddNewRequire);

		// this.setWidth("99.5%");

		popup = new PopupPanel(true) {
			@Override
			protected void onUnload() {
				super.onUnload();
				// dropDown.resetPopupStyle();
				resetComboList();
			}

			@Override
			protected void onLoad() {
				super.onLoad();
			}
		};

		if (!UIUtils.isMSIEBrowser()) {
			popup.setWidth("100%");
		}
		popup.add(panel);
		popup.setStyleName("popup");
		popup.getElement().setAttribute("id", "popuppaneldropdown");
		dropDown.getElement().getStyle().setCursor(Cursor.POINTER);

		addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				showPopup();
			}
		});

		addKeyPressHandler();
		// addBlurHandler();
		// addChangeHandler();

		this.removeStyleName("gwt-TextBox");
	}

	@SuppressWarnings("deprecation")
	protected void showPopup() {
		if (DropDownCombo.this.getDisabled())
			return;
		dropDown.getRowElement(0).getStyle().setHeight(15, Unit.PX);
		int x = getMainWidget().getAbsoluteLeft();
		int y = getMainWidget().getAbsoluteTop() + 22;
		dropDown.setWidth(getMainWidget().getOffsetWidth() + "px");
		// dropDown.setHeight(getMainWidget().getOffsetHeight() + "px");
		//
		popup.setPopupPosition(x + 1, y);

		popup.show();
		popup.addPopupListener(new PopupListener() {

			@Override
			public void onPopupClosed(PopupPanel sender, boolean autoClosed) {
				if ((selectedName == null || !selectedName.equals(getValue()
						.toString())) && selectedIndex == -1)
					setRelatedComboItem(getValue().toString());
			}
		});
		int clientwidth = Window.getClientWidth();
		int clientHeight = Window.getClientHeight();
		int popupWdth = popup.getWidget().getOffsetWidth();
		int popupHeight = 0;
		if (DropDownCombo.this instanceof AccountCombo) {
			popupHeight = 200;
		} else
			popupHeight = popup.getWidget().getOffsetHeight();

		if (UIUtils.isMSIEBrowser()) {
			dropDown.setHeight(Math.min((comboItems.size() * 10), 100) + "px");
			popup.setHeight(Math.min(dropDown.getOffsetHeight(), 100) + "px");
			panel.setWidth(dropDown.getOffsetWidth() + "px");
		}
		panel.setHeight(Math.min(dropDown.getOffsetHeight(), 200) + "px");

		if ((x + popupWdth) > clientwidth) {
			x = x - (popup.getOffsetWidth() - getMainWidget().getOffsetWidth());
			popup.setPopupPosition(x + 1, y);
		}

		if ((y + popupHeight) > clientHeight) {
			y = y
					- (popup.getOffsetHeight() - getMainWidget()
							.getOffsetHeight());
			popup.setPopupPosition(x, y - 42);
		}

		panel.setHeight(Math.min(dropDown.getOffsetHeight(), 200) + "px");

	}

	private void addDiv() {
		TextBox box = (TextBox) getMainWidget();
		com.google.gwt.user.client.Element parent = (com.google.gwt.user.client.Element) box
				.getElement().getParentElement();
		if (DOM.getChildCount(parent) <= 1) {
			Element div = DOM.createDiv();
			div.setClassName("downarrow-button");
			box.getElement().getParentElement().appendChild(div);
		}
	}

	private void resetComboList() {
		if (!maincomboItems.isEmpty()) {
			initCombo(maincomboItems);
			maincomboItems.clear();
		}
	}

	private void addKeyPressHandler() {

		setKeyBoardHandler(new KeyPressHandler() {

			@Override
			public void onKeyPress(KeyPressEvent event) {

				char key = event.getCharCode();
				// if ((key >= 48 && key <= 57) || (key >= 65 && key <= 90)
				// || (key >= 96 && key <= 122)) {
				if (key >= 32 && key <= 126) {
					onKeyEnter(key);
				} else if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_BACKSPACE
						|| event.getNativeEvent().getKeyCode() == KeyCodes.KEY_DELETE) {

					Timer timer = new Timer() {

						@Override
						public void run() {
							onKeyEnter('/');
						}
					};
					timer.schedule(200);
					// key codes 38 for up key 40 for down key
				} else if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_DOWN
						&& popup.isShowing()) {
					dropDown.setKeyboardSelected(0, true, true);

				} else if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_UP
						&& popup.isShowing()) {
					dropDown.setKeyboardSelected(dropDown.getDisplayedItems()
							.size() - 1, true, true);
				}
			}

		});

		if (UIUtils.isMSIEBrowser()) {
			setKeyDownHandler(new KeyDownHandler() {

				@Override
				public void onKeyDown(KeyDownEvent event) {
					if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_BACKSPACE
							|| event.getNativeEvent().getKeyCode() == KeyCodes.KEY_DELETE) {

						Timer timer = new Timer() {

							@Override
							public void run() {
								onKeyEnter('/');
							}
						};
						timer.schedule(200);
						// key codes 38 for up key 40 for down key
					} else if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_DOWN
							&& popup.isShowing()) {
						dropDown.setKeyboardSelected(0, true, true);

					} else if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_UP
							&& popup.isShowing()) {
						dropDown.setKeyboardSelected(dropDown
								.getDisplayedItems().size() - 1, true, true);
					}
				}
			});
		}

	}

	public abstract SelectItemType getSelectItemType();

	/**
	 * Override this method to do anything before the other overridden methods
	 * are called from constructor.
	 */
	protected void init() {
	}

	public void setWidth(String width) {
		super.setWidth(width);

	}

	/**
	 * "" Should be Called only Once For Re-Initializing use, setComboItems
	 * 
	 * @param list
	 */
	public void initCombo(List<T> list) {
		// this.setValue(null);
		this.comboItems.clear();
		// this.dropDown.clear();
		if (list != null)
			for (T object : list) {
				if (object != null)
					checkObject(object);
			}
		setRowsData(comboItems);
	}

	@SuppressWarnings("unchecked")
	private void setRowsData(List<T> cmbItems) {
		this.dropDown.setRowCount(cmbItems.size()
				+ dropDown.getDummyRecords().size());
		this.dropDown.setPageSize(dropDown.getRowCount());
		this.dropDown.setRowData(dropDown.getDummyRecords().size(), cmbItems);

	}

	public List<T> getComboItems() {
		return comboItems;
	}

	public void addItem(T object) {
		// checks the object is conains in comboitems
		checkObject(object);

		setRowsData(comboItems);

	}

	private void checkObject(T object) {
		if (object == null)
			return;
		// String text;
		// int rowCount = comboItems.contains(object) ?
		// comboItems.indexOf(object) > 0 ? comboItems
		// .indexOf(object) + 1
		// : comboItems.indexOf(object)
		// : this.dropDown.getRowCount();

		if (!comboItems.contains(object))
			comboItems.add(object);

	}

	/**
	 * This method is invoked from AbstractBaseView to setPreviousOutput
	 * 
	 * @param obj
	 */
	@SuppressWarnings("unchecked")
	public void addItemThenfireEvent(T obj) {
		boolean usTaxCode = FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US
				&& obj instanceof ClientTAXItemGroup;
		if (obj instanceof IAccounterCore) {
			IAccounterCore core = (IAccounterCore) obj;
			if (usTaxCode)
				obj = (T) FinanceApplication.getCompany()
						.getTAXCodeForTAXItemGroup((ClientTAXItemGroup) obj);
			if (core.getObjectType() == AccounterCoreType.ACCOUNT) {
				UIUtils.updateAccountsInSortedOrder(
						(List<ClientAccount>) this.comboItems,
						(ClientAccount) core);
			} else {
				if (!usTaxCode)
					Utility.updateClientList((IAccounterCore) obj,
							(List<IAccounterCore>) this.comboItems);
			}
		}
		this.initCombo(new ArrayList<T>(this.comboItems));
		if (!usTaxCode)
			setComboItem(obj);
		if (handler != null) {
			handler.selectedComboBoxItem(obj);
		}
	}

	@Override
	public void setValue(Object value) {

		if (value == null) {
			super.setValue("");
		} else {
			if (!value.equals(getDefaultAddNewCaption()))
				super.setValue(value);
		}
	}

	/**
	 * This method is invoked when a new item is added to the listbox
	 * 
	 * @param object
	 *            -- object to be added
	 * @param addIfNotinList
	 */

	public void setComboItem(T obj) {
		selectedObject = obj;
		if (comboItems != null && obj != null) {
			addComboItem(obj);
			int index = comboItems.indexOf(obj);
			if (isAddNewRequire)
				setSelectedItem(obj, index + 1);
			else
				setSelectedItem(obj, index);
		} else
			addItem(obj);

	}

	@SuppressWarnings("unused")
	private void setSelectedIndex(int i) {

	}

	/**
	 * This method will be invoked when a new item about to add in effect of
	 * "addNewXXX" option
	 * 
	 * @param obj
	 */
	public void addComboItem(T obj) {

		if (!hasItem(obj)) {
			addItem(obj);
		}
	}

	public boolean hasItem(T object) {
		return this.comboItems.contains(object);
	}

	public IAccounterComboSelectionChangeHandler<T> getHandler() {
		return handler;
	}

	public void addSelectionChangeHandler(
			IAccounterComboSelectionChangeHandler<T> handler) {
		this.handler = handler;
	}

	protected abstract String getColumnData(T object, int row, int col);

	protected abstract String getDisplayName(T object);

	public void setNoColumns(int cols) {
		this.cols = cols;
		dropDown.setNoColumns(cols);
	}

	public abstract String getDefaultAddNewCaption();

	public abstract void onAddNew();

	public String getDefaultAddNewCaption(SelectItemType type) {

		String string = "";

		switch (type) {

		case CUSTOMER:
			string = comboConstants.newCustomer();
			break;

		case VENDOR:
			string = comboConstants.newVendor();
			break;

		case ACCOUNT:
			string = comboConstants.newAccount();
			break;

		case BANK_NAME:
			string = comboConstants.newBankName();
			break;

		case CREDIT_RATING:
			string = comboConstants.newCreditRating();
			break;

		case CUSTOMER_GROUP:
			string = comboConstants.newCustomerGroup();
			break;

		case SALES_PERSON:
			string = comboConstants.newSalesPerson();
			break;

		case ITEM:
			string = comboConstants.newItem();
			break;

		case ITEM_GROUP:
			string = comboConstants.newItemGroup();
			break;

		case ITEM_TAX:
			string = comboConstants.newItemTax();
			break;

		case PAYEE:
			string = comboConstants.newPayee();
			break;

		case PAYMENT_TERMS:
			string = comboConstants.newPaymentTerms();
			break;

		case PRICE_LEVEL:
			string = comboConstants.newPriceLevel();
			break;

		case SHIPPING_METHOD:
			string = comboConstants.newShippingMethod();
			break;

		case SHIPPING_TERMS:
			string = comboConstants.newShippingTerms();
			break;

		case TAX_AGENCY:
			string = comboConstants.newTaxAgency();
			break;

		case TAX_CODE:
			string = comboConstants.newTaxCode();
			break;

		case TAX_GROUP:
			string = comboConstants.newTaxGroup();
			break;

		case VENDOR_GROUP:
			string = comboConstants.newVendorGroup();
			break;

		// case VAT_CODE:
		// string = constants.newVatCode();
		// break;

		default:
			break;
		}

		return string;
	}

	public void changeValue(int rowIndex) {
		// int index = listbox.getSelectedIndex();

		IAccounterComboSelectionChangeHandler<T> handler = getHandler();
		if (rowIndex > 1)
			selectedObject = comboItems.get(rowIndex
					- (isAddNewRequire ? 2 : 1));
		switch (rowIndex) {

		case 1:
			if (isAddNewRequire) {
				if (popup.isShowing())
					popup.hide();
				setValue(getDefaultAddNewCaption());
				onAddNew();
			} else if (handler != null) {
				selectedObject = comboItems.get(rowIndex - 1);
				handler.selectedComboBoxItem(selectedObject);
				setSelectedItem(selectedObject, rowIndex);
				if (popup.isShowing())
					popup.hide();
			}

			break;

		case 0:

			if (handler != null) {
				selectedObject = null;
				setSelectedItem(null, rowIndex);
				// <<<<<<< .working
				// handler.selectedComboBoxItem(selectedObject);
				if (popup.isShowing())
					popup.hide();
				// =======
				// if (popup.isShowing())
				// popup.hide();
				// >>>>>>> .merge-right.r20318
			}

			break;

		default:

			if (handler != null) {
				try {
					selectedObject = comboItems.get(rowIndex
							- (isAddNewRequire ? 2 : 1));
					handler.selectedComboBoxItem(selectedObject);
					setSelectedItem(selectedObject, rowIndex);
					if (popup.isShowing())
						popup.hide();

				} catch (Exception e) {

				}
			}

			break;
		}

		if (handler == null) {
			if (popup.isShowing())
				popup.hide();
			setSelectedItem(selectedObject, rowIndex);
		}
		if (grid != null) {
			grid.remove(getMainWidget());
			setSelectedItem(selectedObject, 0);
			if (popup.isShowing())
				popup.hide();
		}
		selectedIndex = -1;
	}

	protected void setSelectedItem(T obj, int row) {
		String displayName = "";
		if (obj == null) {
			setValue("");

		} else {
			// <<<<<<< .working
			if (obj instanceof ClientAccount || obj instanceof ClientVATCode)
				displayName = getOnlyName(obj);
			else {
				for (int i = 0; i < cols; i++) {
					displayName += getColumnData(obj, row, i);
					if (i < cols - 1)
						displayName += " - ";
					// =======
					// if (obj instanceof ClientVATCode)
					// displayName = getOnlyName(obj);
					// else {
					// for (int i = 0; i < cols; i++) {
					// displayName += getColumnData(obj, row, i);
					// if (i < cols - 1)
					// displayName += " - ";
					// >>>>>>> .merge-right.r20318

				}
			}
			// }
			setValue(displayName);
		}

	}

	private String getOnlyName(T obj) {
		if (obj instanceof ClientAccount)
			return ((ClientAccount) obj).getName();
		else if (obj instanceof ClientVATCode)
			return ((ClientVATCode) obj).getName();
		return "";
	}

	@SuppressWarnings("unchecked")
	public void setGrid(ListGrid grid) {
		this.grid = grid;
	}

	@SuppressWarnings("unchecked")
	public void removeComboItem(T coreObject) {

		int index = comboItems.indexOf(Utility.getObject(
				(List<IAccounterCore>) comboItems,
				((IAccounterCore) coreObject).getStringID()));
		if (index > 0) {
			comboItems.remove(index);
			dropDown.removeRow(isAddNewRequire ? index + 2 : index + 1);
		}

	}

	public void updateComboItem(T coreObject) {
		for (T item : comboItems) {
			if (((IAccounterCore) item).getStringID().equals(
					((IAccounterCore) coreObject).getStringID())) {

				if (this.getSelectedValue() != null ? this.getSelectedValue()
						.equals(item) : true) {
					removeComboItem(item);
					addItemThenfireEvent(coreObject);
				} else {
					removeComboItem(item);
					addComboItem(coreObject);
				}
				break;
			} else if (((IAccounterCore) coreObject).getStringID() != null) {
				addComboItem(coreObject);
				break;
			}

			// if((IAccounterCore) item.getSt)
		}

	}

	public T getSelectedValue() {
		return selectedObject;
	}

	@Override
	public void setDisabled(boolean b) {
		if (b)
			this.getMainWidget().addStyleName("dropdown-disabled");
		else
			this.getMainWidget().removeStyleName("dropdown-disabled");
		super.setDisabled(b);
	}

	public void setWidth(int width) {
		this.getMainWidget().setWidth(width + "%");
	}

	public void setPopupWidth(String width) {
		// this.panel.setWidth(width);
	}

	protected void onKeyEnter(char key) {
		filterValues(key);
	}

	private void filterValues(char key) {

		String val = getValue() != null ? getValue().toString()
				+ String.valueOf(key).replace("/", "").trim() : String
				.valueOf(key).replace("/", "").trim();

		resetComboList();
		if (key == '/') {
			if (val.replace("/", "").trim().isEmpty()) {
				showPopup();
				return;
			}
		}

		final String val1 = val.toLowerCase();
		List<T> autocompleteItems = getComboitemsByName(val);

		updateComboItemsInSorted(autocompleteItems, val1);

		maincomboItems.addAll(comboItems);
		initCombo(autocompleteItems);

		showPopup();

	}

	// protected List<T> getComboitemsByName(String value) {
	// List<T> autocompleteItems = new ArrayList<T>();
	// for (T t : comboItems) {
	// if (getDisplayName(t).toLowerCase().contains(value.toLowerCase())) {
	// autocompleteItems.add(t);
	// }
	// }
	// return autocompleteItems;
	// }

	// private void addBlurHandler() {
	// addBlurHandler(new BlurHandler() {
	//
	// @Override
	// public void onBlur(BlurEvent event) {
	// if (!getValue().toString().isEmpty() && !popup.isShowing())
	// setRelatedComboItem(getValue().toString());
	// }
	// });
	// }

	private void setRelatedComboItem(final String value) {
		int index = 0;
		if (!getValue().toString().isEmpty()) {
			List<T> combos = getComboitemsByName(value);
			for (T t : combos) {
				String name;
				if (t instanceof ClientVATCode)
					name = getOnlyName(t);
				else
					name = getDisplayName(t);
				if (name.toLowerCase().equals(value.toLowerCase())) {
					combos.clear();
					combos.add(t);

					break;
				}
			}
			updateComboItemsInSorted(combos, value);
			if (combos != null && combos.size() > 0)
				index = comboItems.indexOf(combos.get(0))
						+ (isAddNewRequire ? 2 : 1);
		}
		selectedName = value;
		changeValue(index);
	}

	private void updateComboItemsInSorted(List<T> comboObjects,
			final String value) {
		Collections.sort(comboObjects, new Comparator<T>() {

			@Override
			public int compare(T obj1, T obj2) {
				String name = getDisplayName(obj1).toLowerCase();
				String name1 = getDisplayName(obj2).toLowerCase();

				if (name.indexOf(value) == name1.indexOf(value))
					return 0;
				if (name.indexOf(value) < name1.indexOf(value))
					return -1;
				if (name.indexOf(value) > name1.indexOf(value))
					return 1;

				return 0;
			}
		});
	}

	// protected void onKeyEnter(char key) {
	// filterValues(key);
	// }
	//
	// private void filterValues(char key) {
	//
	// String val = getValue() != null ? getValue().toString()
	// + String.valueOf(key).replace("/", "").trim() : String.valueOf(
	// key).replace("/", "").trim();
	//
	// resetComboList();
	// if (key == '/') {
	// if (val.replace("/", "").trim().isEmpty()) {
	// showPopup();
	// return;
	// }
	// }

	// final String val1 = val.toLowerCase();
	// List<T> autocompleteItems = getComboitemsByName(val);
	//
	// updateComboItemsInSorted(autocompleteItems, val1);
	//
	// maincomboItems.addAll(comboItems);
	// initCombo(autocompleteItems);
	//
	// showPopup();

	// }

	protected List<T> getComboitemsByName(String value) {
		List<T> autocompleteItems = new ArrayList<T>();
		for (T t : comboItems) {
			if (t instanceof ClientAccount || t instanceof ClientVATCode) {
				if (getDisplayNameForAccountVatCode(t).toLowerCase().contains(
						value.toLowerCase()))
					autocompleteItems.add(t);
			} else {
				if (getDisplayName(t).toLowerCase().contains(
						value.toLowerCase())) {
					autocompleteItems.add(t);
				}
			}
		}
		return autocompleteItems;
	}

	// private void addBlurHandler() {
	// addBlurHandler(new BlurHandler() {
	//
	// @Override
	// public void onBlur(BlurEvent event) {
	// if (!getValue().toString().isEmpty() && !popup.isShowing())
	// setRelatedComboItem(getValue().toString());
	// }
	// });
	// }

	// private void setRelatedComboItem(final String value) {
	// int index = 0;
	// if (!getValue().toString().isEmpty()) {
	// List<T> combos = getComboitemsByName(value);
	// for (T t : combos) {
	// String name;
	// if (t instanceof ClientVATCode)
	// name = getOnlyName(t);
	// else
	// name = getDisplayName(t);
	// if (name.toLowerCase().equals(value.toLowerCase())) {
	// combos.clear();
	// combos.add(t);
	//
	// break;
	// }
	// }
	// updateComboItemsInSorted(combos, value);
	// if (combos != null && combos.size() > 0)
	// index = comboItems.indexOf(combos.get(0))
	// + (isAddNewRequire ? 2 : 1);
	// }
	// selectedName = value;
	// changeValue(index);
	// }

	// private void updateComboItemsInSorted(List<T> comboObjects,
	// final String value) {
	// Collections.sort(comboObjects, new Comparator<T>() {
	//
	// @Override
	// public int compare(T obj1, T obj2) {
	// String name = getDisplayName(obj1).toLowerCase();
	// String name1 = getDisplayName(obj2).toLowerCase();
	//
	// if (name.indexOf(value) == name1.indexOf(value))
	// return 0;
	// if (name.indexOf(value) < name1.indexOf(value))
	// return -1;
	// if (name.indexOf(value) > name1.indexOf(value))
	// return 1;
	//
	// return 0;
	// }
	// });
	// }

	private String getDisplayNameForAccountVatCode(T obj) {
		String displayName = "";
		for (int i = 0; i < cols; i++) {
			displayName += getColumnData(obj, 0, i);
			if (i < cols - 1)
				displayName += " - ";
		}
		return displayName;
	}

	@Override
	protected void onAttach() {
		super.onAttach();
		addDiv();
	}
}
