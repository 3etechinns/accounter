package com.vimukti.accounter.web.client.ui;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientBrandingTheme;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class NewCustomThemeDialog extends BaseDialog {

	private DynamicForm form;
	private TextItem themeName, overdueBox, creditNoteBox, statementBox,
			payPalEmail;
	private ClientBrandingTheme brandingTheme;
	private boolean isEdit;

	public NewCustomThemeDialog(String title,
			ClientBrandingTheme brandingTheme, boolean isNew) {
		super(title, "");
		this.brandingTheme = brandingTheme;
		this.isEdit = isNew;
		setWidth("10px");
		createControls();
		center();
	}

	private void createControls() {
		form = new DynamicForm();
		form.setWidth("100%");
		themeName = new TextItem("Custom Theme");
		themeName.setHelpInformation(true);
		themeName.setRequired(true);

		if (isEdit == true) {

			themeName.setValue(brandingTheme.getThemeName().trim());

			overdueBox = new TextItem(messages.overdueInvoiceTitle());
			overdueBox.setValue(messages.overdueValue());

			creditNoteBox = new TextItem(messages.creditNoteTitle());
			creditNoteBox.setValue(messages.creditNoteValue());

			statementBox = new TextItem(messages.statementTitle());
			statementBox.setValue(messages.statement());

			payPalEmail = new TextItem("PayPal Email");
			payPalEmail.setHelpInformation(true);
			String emailId = brandingTheme.getPayPalEmailID() != null ? brandingTheme
					.getThemeName().trim() : "";
			payPalEmail.setValue(emailId);

			form.setItems(themeName, overdueBox, creditNoteBox, statementBox,
					payPalEmail);
		} else {
			form.setItems(themeName);
		}

		VerticalPanel layout = new VerticalPanel();
		layout.add(form);
		setBodyLayout(layout);
	}

	@Override
	protected boolean onOK() {
		if (brandingTheme == null) {
			brandingTheme = new ClientBrandingTheme();
			brandingTheme.setInvoiceTempleteName("Classic Template");
			brandingTheme.setCreditNoteTempleteName("Classic Template");

		}

		if (isEdit == true) {
			brandingTheme.setOverDueInvoiceTitle(this.overdueBox.getValue()
					.toString());
			brandingTheme.setCreditMemoTitle(this.creditNoteBox.getValue()
					.toString());
			brandingTheme.setStatementTitle(this.statementBox.getValue()
					.toString());
			brandingTheme.setPayPalEmailID(this.payPalEmail.getValue()
					.toString());
		}

		brandingTheme.setThemeName(this.themeName.getValue().toString());
		brandingTheme.setCustomFile(true);
		saveOrUpdate(brandingTheme);

		return true;

	}

	@Override
	public void saveSuccess(IAccounterCore object) {
		removeFromParent();
		super.saveSuccess(object);
		ActionFactory.getInvoiceBrandingAction().run(null, true);
	}

	@Override
	protected ValidationResult validate() {

		ValidationResult result = new ValidationResult();
		String name = themeName.getValue().toString();
		if (name.isEmpty())
			result.addError(this, "Please Enter Valid Theme Name");
		return result;
	}

	public String getLocationGroupName() {
		return this.themeName.getValue().toString();
	}

	@Override
	public void setFocus() {
		themeName.setFocus();

	}
}
