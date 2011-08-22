package com.vimukti.accounter.web.client.ui;

import java.util.LinkedHashMap;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.core.ViewConfiguration;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.RadioGroupItem;

/**
 * 
 * @author Mandeep Singh
 * @modified by Ravi kiran.G
 * 
 */

public class SelectAccountTypeDialog extends BaseDialog {

	DynamicForm incomeAndExpenseForm;
	RadioGroupItem incomeAndExpenseRadioGroup;

	private LinkedHashMap<String, String> accountTypes;
	private List<Integer> options;

	private ViewConfiguration configuration;
	private String defaultId;

	public SelectAccountTypeDialog(List<Integer> options,
			ViewConfiguration configuration) {
		super(Accounter.messages().selectAccountType(Global.get().Account()),
				Accounter.messages().selectAccountType(Global.get().Account()));
		this.options = options;
		this.configuration = configuration;
		createControls();
		center();
	}

	private void createControls() {
		setWidth("420px");
		// setAutoSize(Boolean.TRUE);

		incomeAndExpenseForm = new DynamicForm();
		incomeAndExpenseForm.setIsGroup(true);
		incomeAndExpenseForm.setGroupTitle(Accounter.messages()
				.incomeAndExpenseAccounts(Global.get().Account()));
		// incomeAndExpenseForm.setWrapItemTitles(false);
		// incomeAndExpenseForm.setItemLayout(FormLayoutType.ABSOLUTE);
		// incomeAndExpenseForm.setMargin(10);
		incomeAndExpenseForm.setWidth("100%");

		incomeAndExpenseRadioGroup = new RadioGroupItem();
		incomeAndExpenseRadioGroup.setShowTitle(false);
		// incomeAndExpenseRadioGroup.setTop(10);
		// incomeAndExpenseRadioGroup.setLeft(70);
		// incomeAndExpenseRadioGroup.setWidth("*");

		accountTypes = new LinkedHashMap<String, String>();
		if (options != null && options.size() != 0) {
			for (int type : options) {
				accountTypes.put(String.valueOf(type),
						" " + UIUtils.nbsp(Utility.getAccountTypeString(type))
								+ " ");
			}
			defaultId = String.valueOf(options.get(0));

		} else {
			for (int i = 0; i < UIUtils.accountTypes.length - 2; i++) {
				accountTypes
						.put(String.valueOf(UIUtils.accountTypes[i]),
								" "
										+ UIUtils.nbsp(Utility
												.getAccountTypeString(UIUtils.accountTypes[i]))
										+ " ");

			}
			defaultId = String.valueOf(UIUtils.accountTypes[0]);
		}

		incomeAndExpenseRadioGroup.setValueMap(accountTypes);
		incomeAndExpenseRadioGroup.setDefaultValue(defaultId);
		incomeAndExpenseForm.setFields(incomeAndExpenseRadioGroup);

		VerticalPanel MVLay = new VerticalPanel();
		MVLay.setWidth("420px");
		MVLay.add(incomeAndExpenseForm);

		setBodyLayout(MVLay);

		headerLayout.setWidth("420px");
		headerLayout.setHeight("100px");
		footerLayout.setWidth("420px");
		// setAutoHeight();
		show();
	}

	@Override
	public Object getGridColumnValue(IsSerializable obj, int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean onOK() {
		String val = incomeAndExpenseRadioGroup.getValue().toString();
		int type = Integer.parseInt(val);
		String typeName = Utility.getAccountTypeString(type);
		typeName = UIUtils.unbsp(typeName);

		NewAccountView accountView = new NewAccountView();
		// FIX ME
		// UIUtils.setCanvas(accountView, configuration);
		return true;
	}
}
