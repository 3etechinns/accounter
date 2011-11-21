package com.vimukti.accounter.web.client.ui.company;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientSalesPerson;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.AddressDialog;
import com.vimukti.accounter.web.client.ui.EmailForm;
import com.vimukti.accounter.web.client.ui.PhoneFaxForm;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.GridAccountsCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.widgets.DateValueChangeHandler;

/**
 * @modified by Ravi Kiran.G
 * 
 */
public class NewSalesPersonView extends BaseView<ClientSalesPerson> {

	private DynamicForm salesPersonForm;

	private DynamicForm expenseAccountForm;
	private TextAreaItem memoArea;

	private DynamicForm salesPersonInfoForm, memoForm;
	private SelectCombo genderSelect;
	protected ClientAccount selectedExpenseAccount;
	private GridAccountsCombo expenseSelect;
	private TextItem fileAsText, employeeNameText, jobTitleText;
	DateField dateOfBirth, dateOfHire, dateOfLastReview, dateOfRelease;

	protected boolean isClose;
	private CheckboxItem statusCheck;
	private DynamicForm addrsForm;
	private PhoneFaxForm fonFaxForm;
	private EmailForm emailForm;
	protected String gender;
	private List<String> listOfgenders;
	private String[] genderTypes = { messages.unspecified(), messages.male(),
			messages.female() };
	private List<ClientAccount> listOfAccounts;

	private ArrayList<DynamicForm> listforms;
	// private String salesPersonName;

	TextAreaItem addrArea;
	private LinkedHashMap<Integer, ClientAddress> allAddresses;

	public NewSalesPersonView() {
		super();
	}

	private void createControls() {
		allAddresses = new LinkedHashMap<Integer, ClientAddress>();

		listforms = new ArrayList<DynamicForm>();

		employeeNameText = new TextItem(messages.salesPersonName());
		// employeeNameText.setWidth("200px");
		employeeNameText.setRequired(true);
		employeeNameText.setDisabled(isInViewMode());

		fileAsText = new TextItem(messages.fileAs());
		// fileAsText.setWidth("200px");
		fileAsText.setDisabled(isInViewMode());
		employeeNameText.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				if (event != null) {
					String val = employeeNameText.getValue().toString();
					fileAsText.setValue(val);
				}
			}
		});

		jobTitleText = new TextItem(messages.jobTitle());
		// jobTitleText.setWidth("200px");
		jobTitleText.setDisabled(isInViewMode());

		salesPersonForm = UIUtils.form(messages.salesPerson());
		// salesPersonForm.setWidth("80%");
		salesPersonForm.setFields(employeeNameText, fileAsText, jobTitleText);
		// salesPersonForm.getCellFormatter().setWidth(0, 0, "200px");

		expenseAccountForm = UIUtils.form(messages.expenseAccount());
		// expenseAccountForm.setWidth("80%");
		expenseSelect = new GridAccountsCombo(messages.expenseAccount());
		expenseSelect.setWidth("185px");
		expenseSelect.setDisabled(isInViewMode());
		expenseSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {
					public void selectedComboBoxItem(ClientAccount selectItem) {
						selectedExpenseAccount = selectItem;

					}

				});

		// expenseAccountForm.getCellFormatter().setWidth(0, 0, "200px");
		expenseAccountForm.setFields(expenseSelect);

		memoForm = new DynamicForm();
		memoForm.setWidth("100%");
		memoArea = new TextAreaItem();
		memoArea.setMemo(true, this);
		memoArea.setWidth(100);
		memoArea.setTitle(messages.memo());
		memoArea.setToolTip(messages.writeCommentsForThis(this.getAction()
				.getViewName()));
		memoArea.setDisabled(isInViewMode());
		memoForm.setFields(memoArea);
		memoForm.getCellFormatter().getElement(0, 0).getStyle()
				.setVerticalAlign(VerticalAlign.TOP);
		// memoForm.getCellFormatter().getElement(0, 1).getStyle().setWidth(239,
		// Unit.PX);
		// memoForm.getCellFormatter().setWidth(0, 0, "200");
		salesPersonInfoForm = UIUtils.form(messages.salesPersonInformation());
		salesPersonInfoForm.setStyleName("align-form");
		// salesPersonInfoForm.setWidth("100%");
		statusCheck = new CheckboxItem(messages.active());
		statusCheck.setValue(true);
		statusCheck.setDisabled(isInViewMode());
		genderSelect = new SelectCombo(messages.gender());
		genderSelect.setDisabled(isInViewMode());
		// genderSelect.setWidth(45);
		listOfgenders = new ArrayList<String>();
		for (int i = 0; i < genderTypes.length; i++) {
			listOfgenders.add(genderTypes[i]);
		}
		genderSelect.initCombo(listOfgenders);
		genderSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						gender = selectItem;
					}
				});

		dateOfBirth = new DateField(messages.dateofBirth());
		dateOfBirth.setToolTip(messages.selectDateOfBirth(this.getAction()
				.getViewName()));
		dateOfBirth.setDisabled(isInViewMode());
		dateOfBirth.setEnteredDate(new ClientFinanceDate());

		// dateOfBirth.setEndDate(new ClientFinanceDate(19910101));
		// dateOfBirth.setStartDate(new ClientFinanceDate(18910101));
		dateOfBirth.addDateValueChangeHandler(new DateValueChangeHandler() {

			@Override
			public void onDateValueChange(ClientFinanceDate date) {
				long mustdate = new ClientFinanceDate().getDate() - 180000;
				if (new ClientFinanceDate(mustdate).before(dateOfBirth
						.getEnteredDate())) {
					addError(dateOfBirth, messages
							.dateofBirthshouldshowmorethan18years());
				} else {
					clearError(dateOfBirth);
				}
			}
		});

		dateOfHire = new DateField(messages.dateofHire());
		dateOfHire.setToolTip(messages.selectDateOfHire(this.getAction()
				.getViewName()));
		dateOfHire.setDisabled(isInViewMode());
		dateOfHire.setEnteredDate(new ClientFinanceDate());
		// dateOfHire.setUseTextField(true);

		dateOfLastReview = new DateField(messages.dateofLastReview());
		dateOfLastReview.setDisabled(isInViewMode());
		dateOfLastReview.setEnteredDate(new ClientFinanceDate());
		// dateOfLastReview.setUseTextField(true);

		dateOfRelease = new DateField(messages.dateofRelease());
		dateOfRelease.setDisabled(isInViewMode());
		// dateOfRelease.setUseTextField(true);

		dateOfRelease.setEnteredDate(new ClientFinanceDate());

		salesPersonInfoForm.setFields(statusCheck, genderSelect, dateOfBirth,
				dateOfHire, dateOfLastReview, dateOfRelease);

		// XXX
		addrsForm = new DynamicForm();
		addrArea = new TextAreaItem(messages.address());
		// addrArea.setWidth("200px");
		addrArea.setHelpInformation(true);
		addrArea.setWidth(100);
		addrArea.setDisabled(isInViewMode());
		addrArea.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				new AddressDialog("", "", addrArea, "Bill to", allAddresses);

			}
		});

		addrArea.addFocusHandler(new FocusHandler() {

			@Override
			public void onFocus(FocusEvent event) {
				new AddressDialog("", "", addrArea, "Bill to", allAddresses);

			}
		});

		if (getData() != null) {

			employeeNameText.setValue(data.getFirstName());
			// salesPersonName = data.getFirstName();
			jobTitleText.setValue(data.getJobTitle() != null ? data
					.getJobTitle() : "");
			fileAsText.setValue(data.getFileAs());
			Set<ClientAddress> addresses = new HashSet<ClientAddress>();
			addresses.add(data.getAddress());
			setAddresses(addresses);

			ClientAddress toBeShown = allAddresses
					.get(ClientAddress.TYPE_BILL_TO);
			if (toBeShown != null) {
				String toToSet = new String();
				if (toBeShown.getAddress1() != null
						&& !toBeShown.getAddress1().isEmpty()) {
					toToSet = toBeShown.getAddress1().toString() + "\n";
				}

				if (toBeShown.getStreet() != null
						&& !toBeShown.getStreet().isEmpty()) {
					toToSet += toBeShown.getStreet().toString() + "\n";
				}

				if (toBeShown.getCity() != null
						&& !toBeShown.getCity().isEmpty()) {
					toToSet += toBeShown.getCity().toString() + "\n";
				}

				if (toBeShown.getStateOrProvinence() != null
						&& !toBeShown.getStateOrProvinence().isEmpty()) {
					toToSet += toBeShown.getStateOrProvinence() + "\n";
				}
				if (toBeShown.getZipOrPostalCode() != null
						&& !toBeShown.getZipOrPostalCode().isEmpty()) {
					toToSet += toBeShown.getZipOrPostalCode() + "\n";
				}
				if (toBeShown.getCountryOrRegion() != null
						&& !toBeShown.getCountryOrRegion().isEmpty()) {
					toToSet += toBeShown.getCountryOrRegion();
				}
				addrArea.setValue(toToSet);
			}

			fonFaxForm = new PhoneFaxForm(null, null, this, this.getAction()
					.getViewName());
			// fonFaxForm.setWidth("80%");
			fonFaxForm.setHeight("60px");
			// fonFaxForm.getCellFormatter().setWidth(0, 0, "200");
			// fonFaxForm.getCellFormatter().setWidth(0, 1, "125");
			fonFaxForm.setDisabled(isInViewMode());
			fonFaxForm.businessPhoneText.setValue(data.getPhoneNo());
			fonFaxForm.businessFaxText.setValue(data.getFaxNo());
			emailForm = new EmailForm(null, data.getWebPageAddress(), this,
					this.getAction().getViewName());
			emailForm.setWidth("100%");
			// emailForm.getCellFormatter().setWidth(0, 0, "159");
			// emailForm.getCellFormatter().setWidth(0, 1, "125");
			emailForm.businesEmailText.setValue(data.getEmail());
			emailForm.webText.setValue(data.getWebPageAddress());
			emailForm.setDisabled(isInViewMode());
			statusCheck.setValue(data.isActive());
			if (data.getExpenseAccount() != 0) {
				selectedExpenseAccount = getCompany().getAccount(
						data.getExpenseAccount());
				expenseSelect.setComboItem(selectedExpenseAccount);
			}
			genderSelect.setComboItem(data.getGender());
			dateOfBirth.setValue(new ClientFinanceDate(data.getDateOfBirth()));
			dateOfHire.setValue(new ClientFinanceDate(data.getDateOfHire()));
			dateOfLastReview.setValue(new ClientFinanceDate(data
					.getDateOfLastReview()));
			dateOfRelease.setValue(new ClientFinanceDate(data
					.getDateOfRelease()));
			memoArea.setValue(data.getMemo());

		} else {
			setData(new ClientSalesPerson());
			// addrsForm = new AddressForm(null);
			fonFaxForm = new PhoneFaxForm(null, null, this, this.getAction()
					.getViewName());
			// fonFaxForm.setWidth("80%");
			// fonFaxForm.getCellFormatter().setWidth(0, 0, "200");
			// fonFaxForm.getCellFormatter().setWidth(0, 1, "125");
			emailForm = new EmailForm(null, null, this, this.getAction()
					.getViewName());
			emailForm.setWidth("100%");
			// emailForm.getCellFormatter().setWidth(0, 0, "200");
			// emailForm.getCellFormatter().setWidth(0, 1, "125");
			genderSelect.setDefaultToFirstOption(Boolean.TRUE);
			// gender = ClientSalesPerson.GENDER_UNSPECIFIED;
		}
		addrsForm.setWidth("100%");
		// addrsForm.getCellFormatter().setWidth(0, 0, "200");
		// addrsForm.getCellFormatter().setWidth(0, 1, "125");
		addrsForm.setFields(addrArea);
		VerticalPanel leftVLay = new VerticalPanel();
		leftVLay.add(salesPersonForm);
		leftVLay.add(addrsForm);
		addrsForm.getCellFormatter().addStyleName(0, 0, "addrsFormCellAlign");
		addrsForm.getCellFormatter().addStyleName(0, 1, "addrsFormCellAlign");
		leftVLay.add(fonFaxForm);
		fonFaxForm.setStyleName("phone-fax-formatter");
		leftVLay.add(expenseAccountForm);

		VerticalPanel rightVLay = new VerticalPanel();
		rightVLay.getElement().getStyle().setMarginLeft(35, Unit.PX);
		rightVLay.add(emailForm);
		rightVLay.add(salesPersonInfoForm);
		// salesPersonInfoForm.getCellFormatter().setWidth(0, 0, "200");
		HorizontalPanel topHLay = new HorizontalPanel();
		topHLay.addStyleName("fields-panel");
		// topHLay.setSpacing(5);
		topHLay.add(leftVLay);
		topHLay.add(rightVLay);
		topHLay.setCellWidth(leftVLay, "50%");
		topHLay.setCellWidth(rightVLay, "50%");
		topHLay.setCellHorizontalAlignment(rightVLay, ALIGN_RIGHT);

		VerticalPanel mainVlay = new VerticalPanel();
		mainVlay.setWidth("100%");
		mainVlay.setSpacing(10);
		mainVlay.add(topHLay);
		mainVlay.add(memoForm);
		// if (UIUtils.isMSIEBrowser()) {
		// emailForm.getCellFormatter().setWidth(0, 2, "150px");
		// emailForm.getCellFormatter().setWidth(1, 0, "195px");
		// emailForm.getCellFormatter().setWidth(1, 1, "150px");
		// }
		this.add(mainVlay);

		/* Adding dynamic forms in list */
		listforms.add(salesPersonForm);

		listforms.add(addrsForm);
		listforms.add(fonFaxForm);
		listforms.add(expenseAccountForm);
		listforms.add(memoForm);
	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();

		// check whether the sales person is already added or not
		// valid date of birth?

		String name = employeeNameText.getValue().toString();
		ClientSalesPerson clientSalesPerson = getCompany()
				.getSalesPersonByName(name);
		if (!(isInViewMode() ? (data.getName().equalsIgnoreCase(name) ? true
				: clientSalesPerson == null) : true)) {
			result.addError(employeeNameText, messages.alreadyExist());
			return result;
		}

		if (dateOfBirth.getValue().getDate() != 0) {
			long mustdate = new ClientFinanceDate().getDate() - 180000;
			if (dateOfBirth.getValue().getDateAsObject().after(
					new ClientFinanceDate().getDateAsObject())) {
				result.addError(dateOfBirth, messages.invalidDateOfBirth());
			} else if ((new ClientFinanceDate(mustdate).before(dateOfBirth
					.getEnteredDate()))) {
				result.addError(dateOfBirth, messages
						.dateofBirthshouldshowmorethan18years()
						+ ". Because Sales Person should have 18 years");
			}
		}
		result.add(salesPersonForm.validate());
		return result;
	}

	@Override
	public void saveAndUpdateView() {
		updateSalesPersonObject();
		saveOrUpdate(getData());

	}

	@Override
	public void saveFailed(AccounterException exception) {
		super.saveFailed(exception);
		// if (!isEdit)
		// BaseView.errordata.setHTML(FinanceApplication.constants()
		// .DuplicationOfSalesPesonNotAllowed());
		// addError(this, messages
		// .duplicationOfSalesPersonNotAllowed());
		// else
		// BaseView.errordata.setHTML(FinanceApplication.constants()
		// .salesPersonUpdationFailed());
		// addError(this, messages.salesPersonUpdationFailed());
		// BaseView.commentPanel.setVisible(true);
		// this.errorOccured = true;
		AccounterException accounterException = (AccounterException) exception;
		int errorCode = accounterException.getErrorCode();
		String errorString = AccounterExceptions.getErrorString(errorCode);
		Accounter.showError(errorString);
	}

	@Override
	public void saveSuccess(IAccounterCore result) {
		ClientSalesPerson salesPerson = (ClientSalesPerson) result;
		if (salesPerson.getID() != 0) {
			// if (takenSalesperson == null)
			// Accounter.showInformation(((ClientSalesPerson) result)
			// .getFirstName()
			// + FinanceApplication.constants()
			// .isCreatedSuccessfully());
			// else
			// Accounter.showInformation(((ClientSalesPerson) result)
			// .getFirstName()
			// + FinanceApplication.constants()
			// .isUpdatedSuccessfully());
			super.saveSuccess(result);

		} else
			saveFailed(new AccounterException(messages.failed()));

	}

	private void reload() {
		try {
			ActionFactory.getNewSalesperSonAction().run(null, true);
		} catch (Throwable e) {

			e.printStackTrace();
		}
	}

	private void updateSalesPersonObject() {

		data.setMemo(UIUtils.toStr(memoArea.getValue()));
		if (selectedExpenseAccount != null)
			data.setExpenseAccount(selectedExpenseAccount.getID());
		data.setJobTitle(jobTitleText.getValue().toString());
		data.setFileAs(fileAsText.getValue().toString());
		data.setDateOfBirth(dateOfBirth.getValue());
		data.setDateOfHire(UIUtils.toDate(dateOfHire.getValue()));
		data.setDateOfLastReview(UIUtils.toDate(dateOfLastReview.getValue()));
		data.setDateOfRelease(UIUtils.toDate(dateOfRelease.getValue()));
		if (statusCheck.getValue() != null)
			data.setActive((Boolean) statusCheck.getValue());

		data.setFirstName(employeeNameText.getValue().toString());
		data.setAddress(getAddresss());
		data.setPhoneNo(fonFaxForm.businessPhoneText.getValue().toString());
		data.setGender(genderSelect.getSelectedValue());

		data.setFaxNo(fonFaxForm.businessFaxText.getValue().toString());

		data.setEmail(emailForm.businesEmailText.getValue().toString());
		data.setWebPageAddress(emailForm.getWebTextValue());

	}

	@Override
	public void init() {
		super.init();
		createControls();
		setSize("100%", "100%");
	}

	@Override
	public void initData() {

		super.initData();
		initGridAccounts();

	}

	private void initGridAccounts() {
		listOfAccounts = expenseSelect.getAccounts();
		expenseSelect.initCombo(listOfAccounts);
	}

	protected void adjustFormWidths(int titlewidth, int listBoxWidth) {

		// addrsForm.getCellFormatter().getElement(0, 0).setAttribute(
		// messages.width(), titlewidth + "");
		// addrsForm.getCellFormatter().getElement(0, 1).setAttribute(
		// messages.width(), listBoxWidth + "");
		//
		// fonFaxForm.getCellFormatter().getElement(0, 0).setAttribute(
		// messages.width(), titlewidth + "");
		// fonFaxForm.getCellFormatter().getElement(0, 1).setAttribute(
		// messages.width(), listBoxWidth + "");
		//
		// salesPersonForm.getCellFormatter().getElement(0, 0).getStyle()
		// .setWidth(titlewidth + listBoxWidth, Unit.PX);
		// expenseAccountForm.getCellFormatter().getElement(0, 0).setAttribute(
		// "width", titlewidth + listBoxWidth + "");
		// memoForm.getCellFormatter().getElement(0, 0).setAttribute(
		// messages.width(), titlewidth + listBoxWidth + "");
		// emailForm.getCellFormatter().getElement(0, 0).setAttribute(
		// messages.width(), "");
		// emailForm.getCellFormatter().getElement(0, 1).setAttribute(
		// messages.width(), "");

	}

	public static NewSalesPersonView getInstance() {

		return new NewSalesPersonView();
	}

	// @Override
	// protected void onLoad() {
	// int titlewidth = fonFaxForm.getCellFormatter().getElement(0, 0)
	// .getOffsetWidth();
	// int listBoxWidth = fonFaxForm.getCellFormatter().getElement(0, 1)
	// .getOffsetWidth();
	//
	// adjustFormWidths(titlewidth, listBoxWidth);
	// super.onLoad();
	// }
	//
	// @Override
	// protected void onAttach() {
	//
	// int titlewidth = fonFaxForm.getCellFormatter().getElement(0, 0)
	// .getOffsetWidth();
	// int listBoxWidth = fonFaxForm.getCellFormatter().getElement(0, 1)
	// .getOffsetWidth();
	//
	// adjustFormWidths(titlewidth, listBoxWidth);
	//
	// super.onAttach();
	// }

	public List<DynamicForm> getForms() {

		return listforms;
	}

	/**
	 * call this method to set focus in View
	 */
	@Override
	public void setFocus() {
		this.employeeNameText.setFocus();
	}

	@Override
	public void deleteFailed(AccounterException caught) {
		// Not required for thos class

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		// Not required for this class

	}

	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);
	}

	@Override
	public void onEdit() {
		AccounterAsyncCallback<Boolean> editCallBack = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onException(AccounterException caught) {
				Accounter.showError(caught.getMessage());
			}

			@Override
			public void onResultSuccess(Boolean result) {
				if (result)
					enableFormItems();
			}

		};

		this.rpcDoSerivce.canEdit(AccounterCoreType.SALES_PERSON, data.getID(),
				editCallBack);

	}

	protected void enableFormItems() {
		setMode(EditMode.EDIT);
		employeeNameText.setDisabled(isInViewMode());
		fileAsText.setDisabled(isInViewMode());
		jobTitleText.setDisabled(isInViewMode());
		memoArea.setDisabled(isInViewMode());
		statusCheck.setDisabled(isInViewMode());
		genderSelect.setDisabled(isInViewMode());
		dateOfBirth.setDisabled(isInViewMode());
		dateOfHire.setDisabled(isInViewMode());
		dateOfLastReview.setDisabled(isInViewMode());
		dateOfRelease.setDisabled(isInViewMode());
		addrArea.setDisabled(isInViewMode());
		expenseSelect.setDisabled(isInViewMode());
		fonFaxForm.setDisabled(isInViewMode());
		emailForm.setDisabled(isInViewMode());
		super.onEdit();

	}

	@Override
	public void print() {
		// Not required for this class

	}

	/**
	 * THIS METHOD DID N'T USED ANY WHERE IN THE PROJECT.
	 */
	@Override
	public void printPreview() {
		// Not required for this class.

	}

	@Override
	protected String getViewTitle() {
		return messages.newSalesPerson();
	}

	private void setAddresses(Set<ClientAddress> addresses) {
		if (addresses != null) {
			Iterator<ClientAddress> it = addresses.iterator();
			while (it.hasNext()) {
				ClientAddress addr = (ClientAddress) it.next();
				if (addr != null) {
					allAddresses.put(addr.getType(), addr);
				}
			}
		}
	}

	public ClientAddress getAddresss() {
		ClientAddress selectedAddress = allAddresses.get(UIUtils
				.getAddressType("company"));
		if (selectedAddress != null) {
			selectedAddress.setIsSelected(true);
			allAddresses
					.put(UIUtils.getAddressType("company"), selectedAddress);
		}
		Collection<ClientAddress> add = allAddresses.values();
		Iterator<ClientAddress> it = add.iterator();
		while (it.hasNext()) {
			ClientAddress a = (ClientAddress) it.next();
			return a;
			// toBeSet.add(a);
			// System.out.println("Sending Address  Type " + a.getType()
			// + " Street is " + a.getStreet() + " Is Selected"
			// + a.getIsSelected());
		}
		return null;
	}
}
