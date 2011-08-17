package com.vimukti.accounter.web.client.ui.vat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.AddButton;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientPayee;
import com.vimukti.accounter.web.client.core.ClientPaymentTerms;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.AddressForm;
import com.vimukti.accounter.web.client.ui.EmailForm;
import com.vimukti.accounter.web.client.ui.PhoneFaxForm;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.PaymentTermsCombo;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.combo.VATAgencyAccountCombo;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.grids.ContactGrid;

public class TAXAgencyView extends BaseView<ClientTAXAgency> {

	AccounterConstants companyConstants = Accounter.constants();

	private final String ATTR_PRIMARY = Accounter.constants().primary();
	private final String ATTR_CONTACT_NAME = Accounter.constants()
			.contactName();
	private final String ATTR_TITLE = Accounter.constants().title();
	private final String ATTR_BUSINESS_PHONE = Accounter.constants()
			.businessPhone();
	private final String ATTR_EMAIL = Accounter.constants().email();

	TextItem taxAgencyText, fileAsText;

	private TextItem linksText;
	private TextAreaItem memoArea, billToTextAreaItem;
	private CheckboxItem statusCheck;
	private DynamicForm memoForm, accInfoForm, taxAgencyForm;

	private AddressForm addrsForm;
	private PhoneFaxForm phoneFaxForm;
	private EmailForm emailForm;

	private PaymentTermsCombo paymentTermsCombo;
	private VATAgencyAccountCombo liabilitySalesAccountCombo;
	private VATAgencyAccountCombo liabilityPurchaseAccountCombo;

	private ContactGrid gridView;

	private ClientPaymentTerms selectedPaymentTerm;
	private ClientAccount selectedSalesAccount, selectedPurchaseAccount;

	private AddButton addButton;

	private List<String> vatReturnList;

	private SelectCombo vatReturnCombo;

	private static TAXAgencyView taxAgencyView;

	private ArrayList<DynamicForm> listforms;

	public TAXAgencyView() {
		super();

	}

	private void initPaymentTermsCombo() {

		paymentTermsCombo.initCombo(getCompany().getPaymentsTerms());
		if (isEdit && (data.getPaymentTerm()) != 0) {
			selectedPaymentTerm = getCompany().getPaymentTerms(
					data.getPaymentTerm());
			paymentTermsCombo.setComboItem(selectedPaymentTerm);
		}

	}

	private void createControls() {

		listforms = new ArrayList<DynamicForm>();

		VerticalPanel topLayout = getTopLayout();

		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");
		mainVLay.add(topLayout);

		this.add(mainVLay);
		setSize("100%", "100%");
	}

	@Override
	public void saveAndUpdateView() {
		updateTaxAgency();
		saveOrUpdate(getData());
	}

	@Override
	public void saveFailed(AccounterException exception) {
		super.saveFailed(exception);
		// if (!isEdit)
		// BaseView.errordata.setHTML(FinanceApplication.constants()
		// .duplicationOfTaxAgencyNameAreNotAllowed());
		// addError(this, Accounter.constants()
		// .duplicationOfTaxAgencyNameAreNotAllowed());
		// else
		// BaseView.errordata.setHTML(FinanceApplication.constants()
		// .failedToUpdate());
		// addError(this, Accounter.constants().failedToUpdate());
		// BaseView.commentPanel.setVisible(true);
		// this.errorOccured = true;
		AccounterException accounterException = (AccounterException) exception;
		int errorCode = accounterException.getErrorCode();
		String errorString = AccounterExceptions.getErrorString(errorCode);
		Accounter.showError(errorString);
	}

	@Override
	public void saveSuccess(IAccounterCore result) {
		// if (takenVATAgency == null)
		// Accounter.showInformation(result.getName()
		// + FinanceApplication.constants()
		// .isCreatedSuccessfully());
		// else
		// Accounter.showInformation(result.getName()
		// + FinanceApplication.constants()
		// .isUpdatedSuccessfully());
		super.saveSuccess(result);

	}

	@Override
	public ValidationResult validate() {

		ValidationResult result = new ValidationResult();

		// already exists?
		// form validation

		String name = taxAgencyText.getValue().toString();

		ClientTAXAgency taxAgenciesByName = getCompany().getTaxAgenciesByName(
				name);

		if (taxAgenciesByName != null) {
			if (!((!isEdit && taxAgenciesByName != null || taxAgenciesByName
					.getID() == this.getData().getID()) ? false : true)
					|| (!isEdit ? (data.getName().equalsIgnoreCase(name) ? true
							: (taxAgenciesByName != null ? false : true))
							: true)) {
				result.addError(taxAgencyText, Accounter.constants()
						.alreadyExist());
				return result;
			}
		}
		List<DynamicForm> forms = this.getForms();
		for (DynamicForm form : forms) {
			if (form != null) {
				result.add(form.validate());
			}
		}
		return result;
	}

	private void updateTaxAgency() {

		// Setting TaxAgency
		data.setName(taxAgencyText.getValue().toString());
		if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
			if (vatReturnCombo.getSelectedValue() == "") {
				data.setVATReturn(ClientTAXAgency.RETURN_TYPE_NONE);
			} else if (vatReturnCombo.getSelectedValue() == "UK VAT") {
				data.setVATReturn(ClientTAXAgency.RETURN_TYPE_UK_VAT);
			} else {
				data.setVATReturn(ClientTAXAgency.RETURN_TYPE_IRELAND_VAT);
			}
		} else
			data.setVATReturn(0);
		// Setting File As
		data.setFileAs(fileAsText.getValue().toString());

		data.setType(ClientPayee.TYPE_TAX_AGENCY);

		// Setting Addresses
		data.setAddress(addrsForm.getAddresss());

		// Setting Phone
		data.setPhoneNo(phoneFaxForm.businessPhoneText.getValue().toString());

		// Setting Fax
		data.setFaxNo(phoneFaxForm.businessFaxText.getValue().toString());

		// Setting Email and Internet
		data.setEmail(emailForm.businesEmailText.getValue().toString());

		// Setting web page Address
		data.setWebPageAddress(emailForm.getWebTextValue());

		// Setting Active
		data.setActive((Boolean) statusCheck.getValue());

		// Setting Payment Terms
		data.setPaymentTerm(selectedPaymentTerm.getID());

		// Setting Sales Liability account
		data.setSalesLiabilityAccount(selectedSalesAccount.getID());

		// Setting Purchase Liability account
		if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK)
			data.setPurchaseLiabilityAccount(selectedPurchaseAccount.getID());
		else
			data.setPurchaseLiabilityAccount(0);

		// Setting Contacts

		Set<ClientContact> allContacts = new HashSet<ClientContact>();

		// FIXME--The records from contactgrid are added here
		for (ClientContact record : gridView.getRecords()) {
			ClientContact contact = new ClientContact();
			if (record.isPrimary())
				contact.setPrimary(true);
			else
				contact.setPrimary(false);
			contact.setName(record.getName());
			contact.setTitle(record.getTitle());
			contact.setBusinessPhone(record.getBusinessPhone());
			contact.setEmail(record.getEmail());
			allContacts.add(contact);
		}
		data.setContacts(allContacts);

		// Setting Memo
		data.setMemo(UIUtils.toStr(memoArea.getValue()));

	}

	private VerticalPanel getTopLayout() {
		Label lab;
		if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
			lab = new Label(Accounter.constants().vatAgency());
			taxAgencyText = new TextItem(Accounter.constants().vatAgency());
			taxAgencyText.setHelpInformation(true);
		} else {
			lab = new Label(Accounter.constants().taxAgency());
			taxAgencyText = new TextItem(Accounter.constants().taxAgency());
			taxAgencyText.setHelpInformation(true);
		}
		lab.removeStyleName("gwt-Label");
		lab.addStyleName(Accounter.constants().labelTitle());
		lab.setHeight("35px");
		taxAgencyText.setWidth(100);
		taxAgencyText.setRequired(true);

		fileAsText = new TextItem(companyConstants.fileAs());
		fileAsText.setHelpInformation(true);
		fileAsText.setWidth(100);
		taxAgencyText.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				if (((TextBox) event.getSource()).getValue() != null) {
					String val = ((TextBox) event.getSource()).getValue()
							.toString();
					fileAsText.setValue(val);
				}
			}
		});

		taxAgencyForm = UIUtils.form(companyConstants.taxAgency());
		taxAgencyForm.setWidth("100%");
		if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
			taxAgencyForm.getCellFormatter().setWidth(0, 0, "182px");
		} else
			taxAgencyForm.getCellFormatter().setWidth(0, 0, "181px");
		taxAgencyForm.setFields(taxAgencyText);

		accInfoForm = new DynamicForm();
		accInfoForm = UIUtils.form(companyConstants.accountInformation());

		statusCheck = new CheckboxItem(companyConstants.active());
		statusCheck.setValue(true);

		paymentTermsCombo = new PaymentTermsCombo(
				companyConstants.paymentTerm());
		paymentTermsCombo.setHelpInformation(true);
		paymentTermsCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientPaymentTerms>() {

					@Override
					public void selectedComboBoxItem(
							ClientPaymentTerms selectItem) {
						selectedPaymentTerm = (ClientPaymentTerms) selectItem;

					}
				});

		paymentTermsCombo.setRequired(true);

		vatReturnCombo = new SelectCombo(Accounter.constants().vatReturn());
		vatReturnCombo.setHelpInformation(true);
		vatReturnCombo.setRequired(true);
		vatReturnList = new ArrayList<String>();
		vatReturnList.add(Accounter.constants().ukVAT());
		vatReturnList.add(Accounter.constants().vat3Ireland());
		vatReturnCombo.initCombo(vatReturnList);
		vatReturnCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						if (vatReturnCombo.getSelectedValue() != null) {
							vatReturnCombo.setSelected(vatReturnCombo
									.getSelectedValue());
						}

					}
				});
		liabilitySalesAccountCombo = new VATAgencyAccountCombo(Accounter
				.constants().salesLiabilityAccount());
		liabilitySalesAccountCombo.setHelpInformation(true);
		liabilitySalesAccountCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {

					@Override
					public void selectedComboBoxItem(ClientAccount selectItem) {
						selectedSalesAccount = (ClientAccount) selectItem;
					}

				});

		liabilitySalesAccountCombo.setRequired(true);

		liabilityPurchaseAccountCombo = new VATAgencyAccountCombo(Accounter
				.constants().purchaseLiabilityAccount());
		liabilityPurchaseAccountCombo.setHelpInformation(true);
		liabilityPurchaseAccountCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {

					@Override
					public void selectedComboBoxItem(ClientAccount selectItem) {
						selectedPurchaseAccount = (ClientAccount) selectItem;
					}

				});

		liabilityPurchaseAccountCombo.setRequired(true);

		Label contacts = new Label(companyConstants.contacts());
		initListGrid();
		if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
			accInfoForm.setFields(statusCheck, paymentTermsCombo,
					vatReturnCombo, liabilitySalesAccountCombo,
					liabilityPurchaseAccountCombo);
		} else
			accInfoForm.setFields(statusCheck, paymentTermsCombo,
					liabilitySalesAccountCombo);

		accInfoForm.setWidth("94%");
		accInfoForm.setStyleName("align-form");

		memoForm = new DynamicForm();
		memoForm.setWidth("50%");
		memoArea = new TextAreaItem();
		memoArea.setHelpInformation(true);
		memoArea.setTitle(Accounter.constants().memo());
		memoArea.setWidth("400px");
		memoForm.setFields(memoArea);
		memoForm.getCellFormatter().addStyleName(0, 0, "memoFormAlign");

		addButton = new AddButton(this);
		// addButton.setStyleName("addButton");
		addButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ClientContact clientContact = new ClientContact();
				gridView.setDisabled(false);
				// clientContact.setName("");
				gridView.addData(clientContact);
			}
		});

		// For Editing taxAgency
		if (getData() != null) {

			// Setting TaxAgency Name
			taxAgencyText
					.setValue(data.getName() != null ? data.getName() : "");

			// Setting File as
			fileAsText.setValue(data.getFileAs() != null ? data.getFileAs()
					: "");

			// Setting AddressForm
			addrsForm = new AddressForm(data.getAddress());
			addrsForm.setWidth("100%");
			// Setting Phone Fax Form
			phoneFaxForm = new PhoneFaxForm(null, null, this);
			phoneFaxForm.setWidth("100%");
			phoneFaxForm.businessPhoneText.setValue(data.getPhoneNo());
			phoneFaxForm.businessFaxText.setValue(data.getFaxNo());

			// Setting Email Form
			emailForm = new EmailForm(null, data.getWebPageAddress(), this);
			emailForm.businesEmailText.setValue(data.getEmail());
			emailForm.setWidth("100%");

			// Setting Status Check
			statusCheck.setValue(data.isActive());

			// // Setting Payment terms Combo
			// selectedPaymentTerm = takenTaxAgency.getPaymentTerm();
			// paymentTermsCombo.setPaymentTerms(selectedPaymentTerm);

			// Setting Liability accounts Combo
			// selectedAccount = takenTaxAgency.getLiabilityAccount();
			// liabilityAccountCombo.setComboItem(selectedAccount);

			// Setting contacts

			Set<ClientContact> contactsOfEditableTaxAgency = data.getContacts();
			ClientContact records[] = new ClientContact[contactsOfEditableTaxAgency
					.size()];
			int i = 0;
			ClientContact temp = null;
			for (ClientContact contact : contactsOfEditableTaxAgency) {
				records[i] = new ClientContact();
				// FIXME--the contactgrid fields values are populated here

				if (contact.isPrimary()) {
					temp = records[i];
					records[i].setPrimary(Boolean.TRUE);
				}
				records[i].setTitle(contact.getTitle());
				records[i].setName(contact.getName());
				records[i].setBusinessPhone(contact.getBusinessPhone());
				records[i++].setEmail(contact.getEmail());

			}
			if (data.getPaymentTerm() != 0) {
				ClientPaymentTerms payment = getCompany().getPaymentTerms(
						data.getPaymentTerm());
				paymentTermsCombo.setComboItem(payment);
			}

			if (data.getVATReturn() == ClientTAXAgency.RETURN_TYPE_NONE)
				vatReturnCombo.setComboItem("");
			else if (data.getVATReturn() == ClientTAXAgency.RETURN_TYPE_UK_VAT)
				vatReturnCombo.setComboItem(Accounter.constants().ukVAT());
			else
				vatReturnCombo
						.setComboItem(Accounter.constants().vat3Ireland());

			if (data.getSalesLiabilityAccount() != 0) {
				ClientAccount account = getCompany().getAccount(
						data.getSalesLiabilityAccount());
				liabilitySalesAccountCombo.setComboItem(account);
			}

			if (data.getPurchaseLiabilityAccount() != 0) {
				ClientAccount account = getCompany().getAccount(
						data.getSalesLiabilityAccount());
				liabilityPurchaseAccountCombo.setComboItem(account);
			}

			// gridView.
			gridView.setRecords(Arrays.asList(records));
			gridView.setRecords(Arrays.asList(records));
			// if (temp != null)
			// gridView.selectRecord(temp);
			// Setting Memo
			memoArea.setValue(data.getMemo() != null ? data.getMemo() : "");

		} else {
			// For Creating TaxAgency
			setData(new ClientTAXAgency());
			addrsForm = new AddressForm(null);
			addrsForm.setWidth("100%");
			phoneFaxForm = new PhoneFaxForm(null, null, this);
			phoneFaxForm.setWidth("100%");
			emailForm = new EmailForm(null, null, this);
			emailForm.setWidth("100%");
		}

		phoneFaxForm.getCellFormatter().setWidth(0, 0, "235");
		phoneFaxForm.getCellFormatter().setWidth(0, 1, "");

		addrsForm.getCellFormatter().setWidth(0, 0, "50");
		addrsForm.getCellFormatter().setWidth(0, 1, "125");

		emailForm.getCellFormatter().setWidth(0, 0, "235");
		emailForm.getCellFormatter().setWidth(0, 1, "");

		VerticalPanel leftVLay = new VerticalPanel();
		leftVLay.setWidth("100%");
		leftVLay.add(taxAgencyForm);
		leftVLay.add(accInfoForm);

		VerticalPanel rightVLay = new VerticalPanel();
		rightVLay.setWidth("100%");
		rightVLay.setHorizontalAlignment(ALIGN_RIGHT);
		rightVLay.add(addrsForm);
		rightVLay.add(phoneFaxForm);
		rightVLay.add(emailForm);
		addrsForm.getCellFormatter().addStyleName(0, 0, "addrsFormCellAlign");
		addrsForm.getCellFormatter().addStyleName(0, 1, "addrsFormCellAlign");

		HorizontalPanel topHLay = new HorizontalPanel();
		topHLay.setWidth("100%");
		topHLay.setSpacing(5);
		topHLay.add(leftVLay);
		topHLay.add(rightVLay);
		topHLay.setCellWidth(rightVLay, "50%");

		HorizontalPanel contHLay = new HorizontalPanel();
		contHLay.setSpacing(5);
		contHLay.add(contacts);

		VerticalPanel mainVlay = new VerticalPanel();
		mainVlay.add(lab);
		mainVlay.add(topHLay);
		mainVlay.add(contHLay);

		HorizontalPanel panel = new HorizontalPanel();
		panel.setHorizontalAlignment(ALIGN_RIGHT);
		panel.add(addButton);
		panel.getElement().getStyle().setMarginTop(8, Unit.PX);
		panel.getElement().getStyle().setFloat(Float.RIGHT);

		mainVlay.add(gridView);
		mainVlay.setWidth("100%");
		mainVlay.add(panel);
		// mainVlay.add(memoForm);

		/* Adding dynamic forms in list */
		listforms.add(taxAgencyForm);
		listforms.add(accInfoForm);
		listforms.add(memoForm);

		if (UIUtils.isMSIEBrowser()) {
			accInfoForm.getCellFormatter().setWidth(0, 1, "200px");
			accInfoForm.setWidth("68%");
		}

		return mainVlay;
	}

	private void initListGrid() {
		gridView = new ContactGrid();
		gridView.setDisabled(true);
		gridView.setCanEdit(true);
		gridView.isEnable = false;
		gridView.init();
		gridView.setHeight("175px");

	}

	@Override
	public void init() {
		super.init();
		createControls();
		setSize("100%", "100%");
	}

	@Override
	public void initData() {

		initPaymentTermsCombo();
		if (isEdit) {
			this.selectedSalesAccount = getCompany().getAccount(
					data.getSalesLiabilityAccount());
			this.selectedPurchaseAccount = getCompany().getAccount(
					data.getPurchaseLiabilityAccount());
		}
		// initLiabilityAccounts();
		super.initData();

	}

	// private void initLiabilityAccounts() {
	// List<ClientAccount> liabilityAccounts = new ArrayList<ClientAccount>();
	// for (ClientAccount account : FinanceApplication.getCompany()
	// .getAccounts()) {
	//
	// if (account.getType() != ClientAccount.TYPE_CASH
	// && account.getType() != ClientAccount.TYPE_BANK
	// && account.getType() != ClientAccount.TYPE_INVENTORY_ASSET
	// && account.getType() != ClientAccount.TYPE_ACCOUNT_RECEIVABLE
	// && account.getType() != ClientAccount.TYPE_ACCOUNT_PAYABLE) {
	// liabilityAccounts.add(account);
	// }
	//
	// }
	//
	// liabilitySalesAccountCombo.initCombo(liabilityAccounts);
	// if (takenTaxAgency != null
	// && (takenTaxAgency.getSalesLiabilityAccount()) != null) {
	// selectedAccount = FinanceApplication.getCompany().getAccount(
	// takenTaxAgency.getSalesLiabilityAccount());
	// liabilitySalesAccountCombo.setComboItem(selectedAccount);
	// }
	//
	// }

	public static TAXAgencyView getInstance() {

		taxAgencyView = new TAXAgencyView();

		return taxAgencyView;

	}

	// protected void adjustFormWidths(int titlewidth, int listBoxWidth) {
	//
	// addrsForm.getCellFormatter().getElement(0, 0).setAttribute("width",
	// titlewidth + "");
	// addrsForm.getCellFormatter().getElement(0, 1).setAttribute("width",
	// listBoxWidth + "");
	//
	// phoneFaxForm.getCellFormatter().getElement(0, 0).setAttribute("width",
	// titlewidth + "");
	// phoneFaxForm.getCellFormatter().getElement(0, 1).setAttribute("width",
	// listBoxWidth + "");
	//
	// taxAgencyForm.getCellFormatter().getElement(0, 0).getStyle().setWidth(
	// titlewidth + listBoxWidth, Unit.PX);
	// emailForm.getCellFormatter().getElement(0, 0).setAttribute("width",
	// titlewidth + titlewidth + "");
	// emailForm.getCellFormatter().getElement(0, 1).setAttribute("width",
	// listBoxWidth + "");
	// accInfoForm.getCellFormatter().getElement(0, 0).setAttribute("width",
	// listBoxWidth + "");
	// memoForm.getCellFormatter().getElement(0, 0).setAttribute("width",
	// titlewidth + "");
	//
	// }

	// @Override
	// protected void onLoad() {
	// int titlewidth = phoneFaxForm.getCellFormatter().getElement(0, 0)
	// .getOffsetWidth();
	// int listBoxWidth = phoneFaxForm.getCellFormatter().getElement(0, 1)
	// .getOffsetWidth();
	// adjustFormWidths(titlewidth, listBoxWidth);
	//
	// adjustEmailFormWidths();
	//
	// super.onLoad();
	// }
	//
	// @Override
	// protected void onAttach() {
	//
	// int titlewidth = phoneFaxForm.getCellFormatter().getElement(0, 0)
	// .getOffsetWidth();
	// int listBoxWidth = phoneFaxForm.getCellFormatter().getElement(0, 1)
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
		this.taxAgencyText.setFocus();
	}

	// private void adjustEmailFormWidths() {
	// String width = this.accInfoForm.getCellFormatter().getElement(3, 0)
	// .getOffsetWidth()
	// + "px";
	// this.emailForm.getCellFormatter().getElement(0, 0).setAttribute(
	// "width", width);
	// String w = this.taxAgencyForm.getCellFormatter().getElement(0, 1)
	// .getOffsetWidth()
	// + "px";
	// this.addrsForm.getCellFormatter().getElement(0, 2).setAttribute(
	// "width", w);
	//
	// }

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(Boolean result) {

	}

	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);
	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {

		switch (command) {
		case AccounterCommand.CREATION_SUCCESS:

			if (core.getObjectType() == AccounterCoreType.PAYMENT_TERM)
				this.paymentTermsCombo.addComboItem((ClientPaymentTerms) core);

			if (core.getObjectType() == AccounterCoreType.ACCOUNT)
				this.liabilitySalesAccountCombo
						.addComboItem((ClientAccount) core);
			if (core.getObjectType() == AccounterCoreType.ACCOUNT)
				this.liabilityPurchaseAccountCombo
						.addComboItem((ClientAccount) core);

			break;
		case AccounterCommand.DELETION_SUCCESS:

			if (core.getObjectType() == AccounterCoreType.PAYMENT_TERM)
				this.paymentTermsCombo
						.removeComboItem((ClientPaymentTerms) core);

			if (core.getObjectType() == AccounterCoreType.ACCOUNT)
				this.liabilitySalesAccountCombo
						.removeComboItem((ClientAccount) core);
			if (core.getObjectType() == AccounterCoreType.ACCOUNT)
				this.liabilityPurchaseAccountCombo
						.removeComboItem((ClientAccount) core);
			break;

		case AccounterCommand.UPDATION_SUCCESS:

			if (core.getObjectType() == AccounterCoreType.PAYMENT_TERM)
				this.paymentTermsCombo
						.updateComboItem((ClientPaymentTerms) core);

			if (core.getObjectType() == AccounterCoreType.ACCOUNT)
				this.liabilitySalesAccountCombo
						.updateComboItem((ClientAccount) core);
			if (core.getObjectType() == AccounterCoreType.ACCOUNT)
				this.liabilityPurchaseAccountCombo
						.updateComboItem((ClientAccount) core);
			break;
		}
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
		return UIUtils.getVendorString(Accounter.constants().vatAgency(),
				Accounter.constants().taxAgency());
	}

}
