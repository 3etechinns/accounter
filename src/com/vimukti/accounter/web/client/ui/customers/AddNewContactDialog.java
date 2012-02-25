package com.vimukti.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.ValueCallBack;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.core.EmailField;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class AddNewContactDialog extends BaseDialog<ClientContact> {

	private DynamicForm form;
	private ValueCallBack<ClientContact> successCallback;
	private TextItem nameItem;
	private TextItem titleItem;
	private TextItem businessPhoneItem;
	private EmailField emailItem;

	public AddNewContactDialog(String title, String descript) {
		super(title, descript);
		setWidth("400px");
		createControls();
		center();
	}

	private void createControls() {
		form = new DynamicForm();
		form.setWidth("100%");
		VerticalPanel layout = new VerticalPanel();

		form.setItems(getTextItems());
		layout.add(form);
		setBodyLayout(layout);
	}

	private TextItem[] getTextItems() {
		List<TextItem> items = new ArrayList<TextItem>();

		nameItem = new TextItem(messages.name());
		nameItem.setHelpInformation(true);
		nameItem.setRequired(true);
		items.add(nameItem);

		titleItem = new TextItem(messages.title());
		titleItem.setHelpInformation(true);
		titleItem.setRequired(false);
		items.add(titleItem);

		businessPhoneItem = new TextItem(messages.businessPhone());
		businessPhoneItem.setHelpInformation(true);
		businessPhoneItem.setRequired(false);
		items.add(businessPhoneItem);

		emailItem = new EmailField(messages.email());
		emailItem.setHelpInformation(true);
		emailItem.setRequired(false);
		items.add(emailItem);

		// emailItem.addBlurHandler(new BlurHandler() {
		//
		// @Override
		// public void onBlur(BlurEvent event) {
		//
		// });
		return items.toArray(new TextItem[items.size()]);
	}

	// private String getValidMail(String email) {
	//
	// if (!UIUtils.isValidEmail(email)) {
	// // Accounter.showError(messages.invalidEmail());
	// return "";
	// } else
	// return email;
	//
	// }

	@Override
	protected ValidationResult validate() {

		ValidationResult result = new ValidationResult();

		if (nameItem.getValue().isEmpty()) {
			result.addError(nameItem, messages.Pleaseenterthecontactname());
		}

		if (emailItem.getValue().length() > 0) {
			if (!UIUtils.isValidEmail(emailItem.getValue())) {
				result.addError(emailItem, messages.invalidEmail());
			}
		}

		if (!businessPhoneItem.getValue().isEmpty()) {
			if (!UIUtils.isValidPhone(businessPhoneItem.getValue())) {
				result.addError(nameItem, messages.invalidBusinessPhoneVal());
				businessPhoneItem.setValue("");
			}
		}
		return result;

	}

	protected boolean onOK() {
		if (successCallback != null) {
			successCallback.execute(createContact());
		}
		return true;
	}

	/**
	 * @return
	 */
	protected ClientContact createContact() {
		ClientContact contact = new ClientContact();
		contact.setName(nameItem.getValue());
		contact.setTitle(titleItem.getValue());
		contact.setBusinessPhone(businessPhoneItem.getValue());
		contact.setEmail(emailItem.getValue());
		return contact;
	}

	/**
	 * @param newContactHandler
	 */
	public void addSuccessCallback(
			ValueCallBack<ClientContact> newContactHandler) {
		this.successCallback = newContactHandler;
	}

	@Override
	public void setFocus() {
		nameItem.setFocus();

	}

	@Override
	protected boolean onCancel() {
		return true;
	}
}
