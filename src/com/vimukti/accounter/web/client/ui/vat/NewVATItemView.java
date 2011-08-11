package com.vimukti.accounter.web.client.ui.vat;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.core.ClientVATReturnBox;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.TAXAgencyCombo;
import com.vimukti.accounter.web.client.ui.combo.VatReturnBoxCombo;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.PercentageField;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

/**
 * 
 * @author Raj Vimal
 * 
 */

public class NewVATItemView extends BaseView<ClientTAXItem> {

	private TextItem vatItemNameText;
	private TextAreaItem descriptionText;
	private AmountField vatRateText;
	private PercentageField vatRateTextPerT;
	private TAXAgencyCombo vatAgencyCombo;
	private CheckboxItem statusCheck;
	private VatReturnBoxCombo vatReturnBoxCombo;
	protected ClientVATReturnBox selectedBox;
	protected ClientTAXAgency selectedVATAgency;
	private CheckboxItem isPercentatateAmtCheck;
	private String vatName;

	private ArrayList<DynamicForm> listforms;
	final int accounttype = getCompany().getAccountingType();
	final DynamicForm form1 = UIUtils.form(Accounter.constants().type());

	public NewVATItemView() {
		super();
	}

	private void createControls() {
		Label infoLabel = null;
		Label infolabel1 = null;

		if (accounttype == 1) {
			infoLabel = new Label(Accounter.constants().vatItem());
			infoLabel.setStyleName(Accounter.constants().labelTitle());
			// infoLabel.setHeight("35px");
		}

		else {
			infolabel1 = new Label(Accounter.constants().taxItem());

			infolabel1.setStyleName(Accounter.constants().labelTitle());
			// infolabel1.setHeight("50px");
		}

		listforms = new ArrayList<DynamicForm>();

		vatItemNameText = new TextItem(Accounter.constants().vatItemName());
		vatItemNameText.setHelpInformation(true);
		vatItemNameText.setWidth(80);
		vatItemNameText.setRequired(true);

		descriptionText = new TextAreaItem(Accounter.constants().description());
		descriptionText.setHelpInformation(true);
		descriptionText.setWidth(80);

		vatRateText = new AmountField(Accounter.constants().vatAmount(), this);
		vatRateText.setHelpInformation(true);
		vatRateText.setWidth(80);
		vatRateText.setRequired(true);

		vatRateTextPerT = new PercentageField(this, Accounter.constants()
				.vatRateInPerc());
		vatRateTextPerT.setHelpInformation(true);
		vatRateTextPerT.setWidth(80);
		vatRateTextPerT.setRequired(true);

		vatAgencyCombo = new TAXAgencyCombo(Accounter.constants().vatAgency());
		vatAgencyCombo.setHelpInformation(true);
		vatAgencyCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientTAXAgency>() {

					@Override
					public void selectedComboBoxItem(ClientTAXAgency selectItem) {
						selectedVATAgency = (ClientTAXAgency) selectItem;
						loadVATReturnBoxes(selectedVATAgency);
					}

				});
		// vatAgencyCombo.setWidth(80);
		vatAgencyCombo.setRequired(true);

		// Label label = new Label(
		// "Assign This item to box of VAT agency's VAT Return");

		vatReturnBoxCombo = new VatReturnBoxCombo(Accounter.constants()
				.vatReturnBox());
		vatReturnBoxCombo.setHelpInformation(true);
		vatReturnBoxCombo.setRequired(true);
		vatReturnBoxCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientVATReturnBox>() {

					@Override
					public void selectedComboBoxItem(
							ClientVATReturnBox selectItem) {
						selectedBox = (ClientVATReturnBox) selectItem;
					}

				});
		// vatReturnBoxCombo.setWidth(80);

		statusCheck = new CheckboxItem(Accounter.constants().itemIsActive());
		statusCheck.setValue(true);

		form1.setWidth("80%");
		form1.setIsGroup(true);

		if (accounttype == 0) {

			vatItemNameText.setTitle(Accounter.constants().taxItemName());
			vatRateText.setTitle(Accounter.constants().taxAmount());

			vatRateTextPerT.setTitle(Accounter.constants().taxRateP());
			vatAgencyCombo.setTitle(Accounter.constants().taxAgency());

		}

		isPercentatateAmtCheck = new CheckboxItem(Accounter.constants()
				.isConsiderAsPercentange());
		isPercentatateAmtCheck.setValue(true);

		isPercentatateAmtCheck
				.addChangeHandler(new ValueChangeHandler<Boolean>() {

					@Override
					public void onValueChange(ValueChangeEvent<Boolean> event) {
						form1.clear();
						if (event.getValue()) {
							vatRateTextPerT.setPercentage(0.0);
							if (accounttype == 0)
								form1.setFields(vatItemNameText,
										descriptionText,
										isPercentatateAmtCheck,
										vatRateTextPerT, vatAgencyCombo,
										statusCheck);
							else
								form1.setFields(vatItemNameText,
										descriptionText,
										isPercentatateAmtCheck,
										vatRateTextPerT, vatAgencyCombo,
										vatReturnBoxCombo, statusCheck);

						} else {
							vatRateText.setAmount(0.0);
							if (accounttype == 0)
								form1.setFields(vatItemNameText,
										descriptionText,
										isPercentatateAmtCheck, vatRateText,
										vatAgencyCombo, statusCheck);
							else
								form1.setFields(vatItemNameText,
										descriptionText,
										isPercentatateAmtCheck, vatRateText,
										vatAgencyCombo, vatReturnBoxCombo,
										statusCheck);
						}
					}
				});

		form1.getCellFormatter().setWidth(0, 0, "250px");
		form1.getCellFormatter().addStyleName(1, 0, "memoFormAlign");

		if (accounttype == 0)
			form1.setFields(vatItemNameText, descriptionText,
					isPercentatateAmtCheck, vatRateTextPerT, vatAgencyCombo,
					statusCheck);
		else
			form1.setFields(vatItemNameText, descriptionText,
					isPercentatateAmtCheck, vatRateTextPerT, vatAgencyCombo,
					vatReturnBoxCombo, statusCheck);

		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.setSpacing(25);
		mainPanel.setWidth("100%");
		if (accounttype == 1)
			mainPanel.add(infoLabel);
		else
			mainPanel.add(infolabel1);
		mainPanel.add(form1);

		if (UIUtils.isMSIEBrowser()) {
			form1.getCellFormatter().setWidth(0, 1, "270px");
			form1.setWidth("50%");
		}

		this.add(mainPanel);

		/* Adding dynamic forms in list */
		listforms.add(form1);

	}

	protected void loadVATReturnBoxes(ClientTAXAgency vatAgency) {
		List<ClientVATReturnBox> vatBoxes = getCompany().getVatReturnBoxes();
		List<ClientVATReturnBox> vatBoxes2 = new ArrayList<ClientVATReturnBox>();
		for (ClientVATReturnBox vatBox : vatBoxes) {
			if (vatAgency.getVATReturn() == vatBox.getVatReturnType())
				vatBoxes2.add(vatBox);
		}

		vatReturnBoxCombo.initCombo(vatBoxes2);
	}

	@Override
	public void init() {
		super.init();
		createControls();
		setSize("100%", "100%");
	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();

		result.add(DynamicForm.validate(this.getForms().toArray(
				new DynamicForm[getForms().size()])));

		String name = vatItemNameText.getValue().toString() != null ? vatItemNameText
				.getValue().toString() : "";
		if (!((!isEdit && Utility.isObjectExist(getCompany().getTaxItems(),
				name)) ? false : true)
				|| !(isEdit ? (data.getName().equalsIgnoreCase(name) ? true
						: (Utility.isObjectExist(getCompany().getTaxItems(),
								name) ? false : true)) : true)) {
			result.addError(vatItemNameText, Accounter.constants()
					.alreadyExist());
		}

		if (!isEdit) {
			if (Utility.isObjectExist(getCompany().getTaxItems(),
					this.vatItemNameText.getValue().toString())) {
				result.addError(vatItemNameText, Accounter.constants()
						.alreadyExist());
			}
		}
		result.add(DynamicForm.validate(this.getForms().toArray(
				new DynamicForm[getForms().size()])));
		return result;
	}

	@Override
	public void saveAndUpdateView() {
		updateObject();
		saveOrUpdate(data);
	}

	@Override
	public void saveSuccess(IAccounterCore result) {
		if (result != null) {

			// if (takenVATItem == null) {
			// Accounter.showInformation(FinanceApplication.constants()
			// .newVATItemCreated());
			//
			// } else {
			// Accounter.showInformation(FinanceApplication.constants()
			// .VATItemUpdatedSuccessfully());
			//
			// }
			super.saveSuccess(result);

		} else {
			saveFailed(new AccounterException());
		}
	}

	@Override
	public void saveFailed(AccounterException exception) {
		super.saveFailed(exception);
		String exceptionMessage = exception.getMessage();
		// addError(this,
		// "A Vat Name already exists with this name");
		// BaseView.errordata.setHTML(exception.getMessage());
		// BaseView.commentPanel.setVisible(true);
		// this.errorOccured = true;
		// addError(this, exception.getMessage());
		AccounterException accounterException = (AccounterException) exception;
		int errorCode = accounterException.getErrorCode();
		String errorString = AccounterExceptions.getErrorString(errorCode);
		Accounter.showError(errorString);
		updateObject();
		if (exceptionMessage.contains("name")) {
			data.setName(vatName);
			System.out.println(vatName + "After saving");
		}

	}

	private void updateObject() {

		data.setName(vatItemNameText.getValue().toString() != null ? vatItemNameText
				.getValue().toString() : "");
		data.setDescription(descriptionText.getValue().toString() != null ? descriptionText
				.getValue().toString() : "");
		data.setVatReturnBox(selectedBox != null ? selectedBox.getID()
				: data != null ? data.getVatReturnBox() : null);
		data.setTaxAgency(selectedVATAgency != null ? selectedVATAgency.getID()
				: data != null ? data.getTaxAgency() : null);
		data.setActive((Boolean) statusCheck.getValue());

		data.setTaxRate((Boolean) this.isPercentatateAmtCheck.getValue() ? vatRateTextPerT
				.getPercentage() : vatRateText.getAmount());
		data.setPercentage((Boolean) this.isPercentatateAmtCheck.getValue());
	}

	public List<DynamicForm> getForms() {

		return listforms;
	}

	/**
	 * call this method to set focus in View
	 */
	@Override
	public void setFocus() {
		this.vatItemNameText.setFocus();
	}

	@Override
	public void deleteFailed(AccounterException caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(Boolean result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);
	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {

		switch (command) {
		case AccounterCommand.CREATION_SUCCESS:

			if (core.getObjectType() == AccounterCoreType.TAXAGENCY)
				this.vatAgencyCombo.addComboItem((ClientTAXAgency) core);

			break;
		case AccounterCommand.DELETION_SUCCESS:

			if (core.getObjectType() == AccounterCoreType.TAXAGENCY)
				this.vatAgencyCombo.removeComboItem((ClientTAXAgency) core);

			break;
		case AccounterCommand.UPDATION_SUCCESS:

			if (core.getObjectType() == AccounterCoreType.TAXAGENCY)
				this.vatAgencyCombo.updateComboItem((ClientTAXAgency) core);
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
		// NOTHING TO DO.
	}

	@Override
	protected String getViewTitle() {
		String flag;
		if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK)
			flag = Accounter.constants().newVATItem();
		else
			flag = Accounter.constants().newTaxItem();

		return flag;
	}

	@Override
	public void initData() {
		if (data == null) {
			setData(new ClientTAXItem());
		}

		vatItemNameText.setValue(data.getName() != null ? data.getName() : "");
		vatName = data.getName() != null ? data.getName() : "";
		descriptionText.setValue(data.getDescription() != null ? data
				.getDescription() : "");
		isPercentatateAmtCheck.setValue(data.isPercentage());

		if (data.getTaxAgency() != 0) {
			selectedVATAgency = getCompany().getTaxAgency(data.getTaxAgency());
			vatAgencyCombo.setComboItem(selectedVATAgency);
			loadVATReturnBoxes(selectedVATAgency);
		}

		if (data.getVatReturnBox() != 0) {
			selectedBox = getCompany().getVatReturnBox(data.getVatReturnBox());
			vatReturnBoxCombo.setComboItem(selectedBox);
		}
		statusCheck.setValue(data.isActive());

		vatRateTextPerT.setPercentage(data.getTaxRate());

		vatRateText.setAmount(data.getTaxRate());

	}

}
