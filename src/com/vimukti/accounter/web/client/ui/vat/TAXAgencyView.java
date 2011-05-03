package com.vimukti.accounter.web.client.ui.vat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientPaymentTerms;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.theme.ThemesUtil;
import com.vimukti.accounter.web.client.ui.AddressForm;
import com.vimukti.accounter.web.client.ui.EmailForm;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.PhoneFaxForm;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.PaymentTermsCombo;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.combo.VATAgencyAccountCombo;
import com.vimukti.accounter.web.client.ui.company.CompanyMessages;
import com.vimukti.accounter.web.client.ui.core.Accounter;
import com.vimukti.accounter.web.client.ui.core.AccounterErrorType;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;
import com.vimukti.accounter.web.client.ui.core.ViewManager;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.grids.ContactGrid;

@SuppressWarnings("unused")
public class TAXAgencyView extends BaseView<ClientTAXAgency> {

	CompanyMessages companyConstants = GWT.create(CompanyMessages.class);

	private final String ATTR_PRIMARY = FinanceApplication.getVATMessages()
			.primary();
	private final String ATTR_CONTACT_NAME = FinanceApplication
			.getVATMessages().contactname();
	private final String ATTR_TITLE = FinanceApplication.getVATMessages()
			.title();
	private final String ATTR_BUSINESS_PHONE = FinanceApplication
			.getVATMessages().businessphone();
	private final String ATTR_EMAIL = FinanceApplication.getVATMessages()
			.email();

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

	private ClientTAXAgency takenVATAgency;

	private ClientPaymentTerms selectedPaymentTerm;
	private ClientAccount selectedSalesAccount, selectedPurchaseAccount;

	private Button addButton;

	private List<String> vatReturnList;

	private SelectCombo vatReturnCombo;

	private static TAXAgencyView taxAgencyView;

	private ArrayList<DynamicForm> listforms;

	public TAXAgencyView() {
		super();
		this.validationCount = 4;

	}

	private void initPaymentTermsCombo() {

		paymentTermsCombo.initCombo(FinanceApplication.getCompany()
				.getPaymentsTerms());
		if (takenVATAgency != null && (takenVATAgency.getPaymentTerm()) != null) {
			selectedPaymentTerm = FinanceApplication.getCompany()
					.getPaymentTerms(takenVATAgency.getPaymentTerm());
			paymentTermsCombo.setComboItem(selectedPaymentTerm);
		}

	}

	private void createControls() {

		listforms = new ArrayList<DynamicForm>();

		VerticalPanel topLayout = getTopLayout();

		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");
		mainVLay.add(topLayout);

		canvas.add(mainVLay);
		setSize("100%", "100%");
	}

	@Override
	public void saveAndUpdateView() throws Exception {
		try {
			ClientTAXAgency vatAgency = getTaxAgency();
			if (takenVATAgency == null) {
				if (Utility.isObjectExist(FinanceApplication.getCompany()
						.getTaxAgencies(), vatAgency.getName())) {
					throw new InvalidEntryException(
							AccounterErrorType.ALREADYEXIST);
				}
				ViewManager.getInstance().createObject(vatAgency, this);
			}

			else
				ViewManager.getInstance().alterObject(vatAgency, this);

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public void saveFailed(Throwable exception) {
		super.saveFailed(exception);
		if (takenVATAgency == null)
			// BaseView.errordata.setHTML(FinanceApplication.getVATMessages()
			// .duplicationOfTaxAgencyNameAreNotAllowed());
			MainFinanceWindow.getViewManager().showError(
					FinanceApplication.getVATMessages()
							.duplicationOfTaxAgencyNameAreNotAllowed());
		else
			// BaseView.errordata.setHTML(FinanceApplication.getVATMessages()
			// .failedToUpdate());
			MainFinanceWindow.getViewManager().showError(
					FinanceApplication.getVATMessages().failedToUpdate());
		// BaseView.commentPanel.setVisible(true);
		// this.errorOccured = true;
	}

	@Override
	public void saveSuccess(IAccounterCore result) {
		// if (takenVATAgency == null)
		// Accounter.showInformation(result.getName()
		// + FinanceApplication.getVATMessages()
		// .isCreatedSuccessfully());
		// else
		// Accounter.showInformation(result.getName()
		// + FinanceApplication.getVATMessages()
		// .isUpdatedSuccessfully());
		super.saveSuccess(result);

	}

	@Override
	public boolean validate() throws InvalidEntryException {

		/* checking VAT agency Duplication */

		switch (this.validationCount) {

		case 4:
			List<DynamicForm> forms = this.getForms();
			boolean validate = true;
			for (DynamicForm form : forms) {
				if (form != null) {
					if (!form.validate(false)) {
						validate = false;
						// throw new InvalidEntryException(
						// AccounterErrorType.REQUIRED_FIELDS);
					}

				}
			}
			return validate;

		case 3:
			String name = taxAgencyText.getValue().toString();
			if (((takenVATAgency == null && Utility.isObjectExist(
					FinanceApplication.getCompany().getTaxAgencies(), name)) ? false
					: true)
					|| (takenVATAgency != null ? (takenVATAgency.getName()
							.equalsIgnoreCase(name) ? true : (Utility
							.isObjectExist(FinanceApplication.getCompany()
									.getTaxAgencies(), name) ? false : true))
							: true)) {
				return true;
			} else
				throw new InvalidEntryException(AccounterErrorType.ALREADYEXIST);
		case 2:
			// return AccounterValidator.validateForm(accInfoForm);
			return true;
		case 1:
			return gridView.validateGrid();
		default:
			return false;

		}

	}

	private ClientTAXAgency getTaxAgency() {
		ClientTAXAgency vatAgency;
		if (takenVATAgency != null)
			vatAgency = takenVATAgency;
		else
			vatAgency = new ClientTAXAgency();
		// Setting Company

		// Setting TaxAgency
		vatAgency.setName(taxAgencyText.getValue().toString());
		if (FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
			if (vatReturnCombo.getSelectedValue() == "") {
				vatAgency.setVATReturn(ClientTAXAgency.RETURN_TYPE_NONE);
			} else if (vatReturnCombo.getSelectedValue() == "UK VAT") {
				vatAgency.setVATReturn(ClientTAXAgency.RETURN_TYPE_UK_VAT);
			} else {
				vatAgency.setVATReturn(ClientTAXAgency.RETURN_TYPE_IRELAND_VAT);
			}
		} else
			vatAgency.setVATReturn(0);
		// Setting File As
		vatAgency.setFileAs(fileAsText.getValue().toString());

		// Setting Addresses
		vatAgency.setAddress(addrsForm.getAddresss());

		// Setting Phone
		vatAgency.setPhoneNumbers(phoneFaxForm.getAllPhones());

		// Setting Fax
		vatAgency.setFaxNumbers(phoneFaxForm.getAllFaxes());

		// Setting Email and Internet
		vatAgency.setEmail(emailForm.businesEmailText.getValue().toString());

		// Setting web page Address
		vatAgency.setWebPageAddress(emailForm.getWebTextValue());

		// Setting Active
		vatAgency.setActive((Boolean) statusCheck.getValue());

		// Setting Payment Terms
		vatAgency.setPaymentTerm(selectedPaymentTerm.getStringID());

		// Setting Sales Liability account
		vatAgency.setSalesLiabilityAccount(selectedSalesAccount.getStringID());

		// Setting Purchase Liability account
		if (FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK)
			vatAgency.setPurchaseLiabilityAccount(selectedPurchaseAccount
					.getStringID());
		else
			vatAgency.setPurchaseLiabilityAccount(null);

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
		vatAgency.setContacts(allContacts);

		// Setting Memo
		vatAgency.setMemo(UIUtils.toStr(memoArea.getValue()));

		return vatAgency;
	}

	private VerticalPanel getTopLayout() {
		Label lab;
		if (FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
			lab = new Label(FinanceApplication.getVATMessages().VatAgency());
			taxAgencyText = new TextItem(FinanceApplication.getVATMessages()
					.VATAgency());
			taxAgencyText.setHelpInformation(true);
		} else {
			lab = new Label(FinanceApplication.getCompanyMessages().taxAgency());
			taxAgencyText = new TextItem(FinanceApplication
					.getCompanyMessages().taxAgency());
			taxAgencyText.setHelpInformation(true);
		}
		lab.addStyleName(FinanceApplication.getVATMessages().lableTitle());
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
		if (FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
			taxAgencyForm.getCellFormatter().setWidth(0, 0, "204px");
		} else
			taxAgencyForm.getCellFormatter().setWidth(0, 0, "190px");
		taxAgencyForm.setFields(taxAgencyText);

		accInfoForm = new DynamicForm();
		accInfoForm = UIUtils.form(companyConstants.accountInformation());

		statusCheck = new CheckboxItem(companyConstants.active());
		statusCheck.setValue(true);

		paymentTermsCombo = new PaymentTermsCombo(companyConstants
				.paymentTerm());
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

		vatReturnCombo = new SelectCombo(FinanceApplication.getVATMessages()
				.VATReturn());
		vatReturnCombo.setHelpInformation(true);
		vatReturnCombo.setRequired(true);
		vatReturnList = new ArrayList<String>();
		vatReturnList.add(FinanceApplication.getVATMessages().UKVAT());
		vatReturnList.add(FinanceApplication.getVATMessages().vAT3Ireland());
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
		liabilitySalesAccountCombo = new VATAgencyAccountCombo(
				FinanceApplication.getVATMessages().salesLiabilityAccount());
		liabilitySalesAccountCombo.setHelpInformation(true);
		liabilitySalesAccountCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {

					@Override
					public void selectedComboBoxItem(ClientAccount selectItem) {
						selectedSalesAccount = (ClientAccount) selectItem;
					}

				});

		liabilitySalesAccountCombo.setRequired(true);

		liabilityPurchaseAccountCombo = new VATAgencyAccountCombo(
				FinanceApplication.getVATMessages().purchaseLiabilityAccount());
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
		if (FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
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
		memoArea.setTitle(FinanceApplication.getVATMessages().memo());
		memoArea.setWidth("400px");
		memoForm.setFields(memoArea);
		memoForm.getCellFormatter().addStyleName(0, 0, "memoFormAlign");

		addButton = new Button(FinanceApplication.getVATMessages().add());
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
		if (takenVATAgency != null) {

			// Setting TaxAgency Name
			taxAgencyText
					.setValue(takenVATAgency.getName() != null ? takenVATAgency
							.getName() : "");

			// Setting File as
			fileAsText
					.setValue(takenVATAgency.getFileAs() != null ? takenVATAgency
							.getFileAs()
							: "");

			// Setting AddressForm
			addrsForm = new AddressForm(takenVATAgency.getAddress());
			addrsForm.setWidth("100%");
			// Setting Phone Fax Form
			phoneFaxForm = new PhoneFaxForm(takenVATAgency.getPhoneNumbers(),
					takenVATAgency.getFaxNumbers());
			phoneFaxForm.setWidth("100%");

			// Setting Email Form
			emailForm = new EmailForm(null, takenVATAgency.getWebPageAddress());
			emailForm.businesEmailText.setValue(takenVATAgency.getEmail());
			emailForm.setWidth("100%");

			// Setting Status Check
			statusCheck.setValue(takenVATAgency.isActive());

			// // Setting Payment terms Combo
			// selectedPaymentTerm = takenTaxAgency.getPaymentTerm();
			// paymentTermsCombo.setPaymentTerms(selectedPaymentTerm);

			// Setting Liability accounts Combo
			// selectedAccount = takenTaxAgency.getLiabilityAccount();
			// liabilityAccountCombo.setComboItem(selectedAccount);

			// Setting contacts

			Set<ClientContact> contactsOfEditableTaxAgency = takenVATAgency
					.getContacts();
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
			if (takenVATAgency.getPaymentTerm() != null) {
				ClientPaymentTerms payment = FinanceApplication.getCompany()
						.getPaymentTerms(takenVATAgency.getPaymentTerm());
				paymentTermsCombo.setComboItem(payment);
			}

			if (takenVATAgency.getVATReturn() == ClientTAXAgency.RETURN_TYPE_NONE)
				vatReturnCombo.setSelected("");
			else if (takenVATAgency.getVATReturn() == ClientTAXAgency.RETURN_TYPE_UK_VAT)
				vatReturnCombo.setSelected(FinanceApplication.getVATMessages()
						.uKVAT());
			else
				vatReturnCombo.setSelected(FinanceApplication.getVATMessages()
						.vAT3Ireland());

			if (takenVATAgency.getSalesLiabilityAccount() != null) {
				ClientAccount account = FinanceApplication.getCompany()
						.getAccount(takenVATAgency.getSalesLiabilityAccount());
				liabilitySalesAccountCombo.setComboItem(account);
			}

			if (takenVATAgency.getPurchaseLiabilityAccount() != null) {
				ClientAccount account = FinanceApplication.getCompany()
						.getAccount(takenVATAgency.getSalesLiabilityAccount());
				liabilityPurchaseAccountCombo.setComboItem(account);
			}

			// gridView.
			gridView.setRecords(Arrays.asList(records));
			gridView.setRecords(Arrays.asList(records));
			// if (temp != null)
			// gridView.selectRecord(temp);
			// Setting Memo
			memoArea.setValue(takenVATAgency.getMemo() != null ? takenVATAgency
					.getMemo() : "");

		} else { // For Creating TaxAgency
			addrsForm = new AddressForm(null);
			addrsForm.setWidth("100%");
			phoneFaxForm = new PhoneFaxForm(null, null);
			phoneFaxForm.setWidth("100%");
			emailForm = new EmailForm(null, null);
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
		mainVlay.add(panel);
		// mainVlay.add(memoForm);

		addButton.getElement().getParentElement().addClassName("add-button");
		if (addButton.isEnabled()) {
			Element addseparator = DOM.createSpan();
			addseparator.addClassName("add-separator");
			DOM.appendChild(addButton.getElement(), addseparator);

			Element addimage = DOM.createSpan();
			addimage.addClassName("add-image");
			DOM.appendChild(addButton.getElement(), addimage);

			ThemesUtil.addDivToButton(addButton, FinanceApplication
					.getThemeImages().button_right_blue_image(),
					"blue-right-image");
		}
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
		if (this.takenVATAgency != null) {
			this.selectedSalesAccount = FinanceApplication.getCompany()
					.getAccount(this.takenVATAgency.getSalesLiabilityAccount());
			this.selectedPurchaseAccount = FinanceApplication.getCompany()
					.getAccount(
							this.takenVATAgency.getPurchaseLiabilityAccount());
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

	@Override
	public void setData(ClientTAXAgency data) {
		super.setData(data);
		if (data != null)
			takenVATAgency = data;
		else
			takenVATAgency = null;
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
	public void deleteFailed(Throwable caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(Boolean result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void fitToSize(int height, int width) {
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub

	}

	@Override
	public void print() {
		// TODO Auto-generated method stub

	}

	@Override
	public void printPreview() {
		// TODO Auto-generated method stub

	}

}
