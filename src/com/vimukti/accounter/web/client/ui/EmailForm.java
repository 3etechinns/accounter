package com.vimukti.accounter.web.client.ui;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.vimukti.accounter.web.client.core.ClientEmail;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.Accounter;
import com.vimukti.accounter.web.client.ui.core.AccounterErrorType;
import com.vimukti.accounter.web.client.ui.core.EmailField;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.LinkItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

/**
 * 
 * @author Venki.p
 * 
 */
public class EmailForm extends DynamicForm {
	private SelectCombo businesEmailSelect;
	private LinkedHashMap<Integer, ClientEmail> allEmails;
	private ClientEmail toBeShownEmail = null;
	public EmailField businesEmailText;
	public TextItem webText;

	public EmailForm(Set<ClientEmail> emails, String webTextValue) {
		allEmails = new LinkedHashMap<Integer, ClientEmail>();
		setEmails(emails);
		setIsGroup(true);
		setGroupTitle(FinanceApplication.getFinanceUIConstants()
				.emailAndInternet());
		setNumCols(3);

		businesEmailSelect = new SelectCombo(FinanceApplication
				.getFinanceUIConstants().email());
		businesEmailSelect.setHelpInformation(true);
		businesEmailSelect.setWidth(85);
		businesEmailSelect.getMainWidget().removeStyleName("gwt-ListBox");
		businesEmailSelect.initCombo(new ClientEmail().getEmailTypes());

		businesEmailSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						ClientEmail e = allEmails.get(UIUtils
								.getEmailType(businesEmailSelect
										.getSelectedValue().toString()));
						if (e != null) {
							businesEmailText.setEmail(e.getEmail());
						} else
							businesEmailText.setText("");
					}
				});

		businesEmailText = new EmailField("E-mail");
		businesEmailText.setHelpInformation(true);
		businesEmailText.setWidth(100);
		businesEmailText.setShowTitle(true);

		businesEmailText.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				if (event != null) {
					String em = businesEmailText.getValue().toString();
					if (!UIUtils.isValidEmail(em)) {
						Accounter.showError(AccounterErrorType.INVALID_EMAIL);
						businesEmailText.setText("");
					} else {
						ClientEmail email = new ClientEmail();
						email.setType(UIUtils.getEmailType(businesEmailSelect
								.getSelectedValue()));
						email.setEmail(em);
						allEmails.put(UIUtils.getEmailType(businesEmailSelect
								.getSelectedValue()), email);
					}
				}
			}
		});
		webText = new TextItem(FinanceApplication.getFinanceUIConstants()
				.webPageAddress());
		webText.setHelpInformation(true);
		webText.setWidth(100);
		LinkItem emptyItem = new LinkItem();
		emptyItem.setShowTitle(false);

		if (toBeShownEmail != null) {
			businesEmailSelect.setSelected(toBeShownEmail.getEmailTypes().get(
					toBeShownEmail.getType()));
			businesEmailText.setEmail(toBeShownEmail.getEmail());
		} else
			businesEmailSelect.setDefaultToFirstOption(true);
		if (webTextValue != null)
			webText.setValue(webTextValue);

		setFields(businesEmailText, emptyItem, webText);
		setWidth("100%");
//		this.getCellFormatter().getElement(1, 0).setAttribute("colspan", "2");
//		this.setCellPadding(5);
		// setHeight("100px");
	}

	private void setEmails(Set<ClientEmail> emails) {
		if (emails != null) {
			for (ClientEmail em : emails) {
				if (em.getIsSelected()) {
					toBeShownEmail = em;
				}
				allEmails.put(em.getType(), em);
				// System.out.println("Existing Emails  Type " + em.getType()
				// + " Email name  is " + em.getEmail() + " Is Selected"
				// + em.getIsSelected());
			}
		}

	}

	public Set<ClientEmail> getAllEmails() {
		int selectedType = UIUtils.getEmailType(businesEmailSelect
				.getSelectedValue());
		ClientEmail selectedEmailFromSelect = allEmails.get(selectedType);
		if (selectedEmailFromSelect != null) {
			selectedEmailFromSelect.setIsSelected(true);
			allEmails.put(selectedType, selectedEmailFromSelect);

		}
		Collection<ClientEmail> emails = allEmails.values();
		Set<ClientEmail> toBeSetEmails = new HashSet<ClientEmail>();
		for (ClientEmail e : emails) {
			toBeSetEmails.add(e);
			// System.out.println("Sending Emails  Type " + e.getType()
			// + " Email name  is " + e.getEmail() + " Is Selected"
			// + e.getIsSelected());
		}
		return toBeSetEmails;

	}

	public String getWebTextValue() {
		if (webText.getValue() != null)
			return webText.getValue().toString();
		return "";
	}

}
