package com.vimukti.accounter.web.client.ui.settings;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.ValueCallBack;
import com.vimukti.accounter.web.client.core.ClientBrandingTheme;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.FileUploadDilaog;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.ButtonBar;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

/**
 * 
 * @author Uday Kumar
 * 
 */
@SuppressWarnings( { "deprecation" })
public class NewBrandingThemeView extends BaseView<ClientBrandingTheme> {

	private Label pageSizeLabel, logoLabel, termsLabel;

	private RadioButton a4Button, usLetterButton, leftRadioButton,
			rightRadioButton, cmButton, inchButton;
	private VerticalPanel checkBoxPanel, radioButtonPanel,
			check_radio_textAreaPanel, button_textBoxPanel;
	private HorizontalPanel mainLayoutPanel, check_radioPanel;
	private CheckBox taxNumItem, headingItem, unitPriceItem,// paymentItem,
			columnItem, addressItem, logoItem;
	private TextItem topMarginBox, bottomMarginBox, addressPadBox, overdueBox,
			creditNoteBox, statementBox, paypalTextBox, logoNameBox;
	private TextItem nameItem;
	private String[] fontNameArray, fontSizeArray;
	private SelectCombo fontNameBox, fontSizeBox;
	private HTML paypalEmailHtml, contactDetailHtml;
	private TextArea contactDetailsArea, termsPaymentArea;
	private Label measureLabel;
	private FlexTable textBoxTable;
	private List<String> listOfFontNames, listOfFontSizes;
	private AccounterConstants messages = Accounter.constants();
	private AccounterMessages accounterMessages = Accounter.messages();
	private DynamicForm nameForm;

	public NewBrandingThemeView(String title, String desc) {
	}

	public NewBrandingThemeView(String title, String desc,
			ClientBrandingTheme brandingTheme) {
		super();
		setData(brandingTheme);
	}

	@Override
	public void init() {
		super.init();
		try {
			createControls();
		} catch (Exception e) {
			System.err.println(e);
		}
	}

	@Override
	public void initData() {
		if (getData() == null) {
			setData(new ClientBrandingTheme());
		} else {
			initThemeData(getData());
		}
		/*
		 * else { brandingTheme = new ClientBrandingTheme();
		 * setBrandingTheme(brandingTheme); }
		 */
		super.initData();

	}

	private void initThemeData(ClientBrandingTheme brandingTheme) {

		nameItem.setValue(brandingTheme.getThemeName());
		topMarginBox.setValue(String.valueOf(brandingTheme.getTopMargin()));
		bottomMarginBox.setValue(String
				.valueOf(brandingTheme.getBottomMargin()));
		addressPadBox.setValue(String
				.valueOf(brandingTheme.getAddressPadding()));
		setPazeSize(brandingTheme.getPageSizeType());
		fontNameBox.setComboItem(brandingTheme.getFont());
		fontSizeBox.setComboItem(brandingTheme.getFontSize());
		setLogoType(brandingTheme.getLogoAlignmentType());
		setMeasurementType(brandingTheme.getMarginsMeasurementType());
		creditNoteBox.setValue(brandingTheme.getCreditMemoTitle());
		overdueBox.setValue(brandingTheme.getOverDueInvoiceTitle());
		statementBox.setValue(brandingTheme.getStatementTitle());
		taxNumItem.setValue(brandingTheme.isShowTaxNumber());
		headingItem.setValue(brandingTheme.isShowColumnHeadings());
		unitPriceItem.setValue(brandingTheme.isShowUnitPrice_And_Quantity());
		columnItem.setValue(brandingTheme.isShowTaxColumn());
		addressItem.setValue(brandingTheme.isShowRegisteredAddress());
		logoItem.setValue(brandingTheme.isShowLogo());
		paypalTextBox.setValue(brandingTheme.getPayPalEmailID());
		termsPaymentArea.setValue(brandingTheme.getTerms_And_Payment_Advice());
		contactDetailsArea.setValue(brandingTheme.getContactDetails());
		logoNameBox.setValue(brandingTheme.getFileName());

	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {

	}

	private void createControls() {
		VerticalPanel panel = new VerticalPanel();
		HTML titleHtml = new HTML("New Branding Theme");

		mainLayoutPanel = new HorizontalPanel();
		check_radioPanel = new HorizontalPanel();
		check_radio_textAreaPanel = new VerticalPanel();
		button_textBoxPanel = new VerticalPanel();

		check_radioPanel.add(addCheckBoxTableControls());

		check_radioPanel.add(addRadioBoxTableControls());

		check_radio_textAreaPanel.add(check_radioPanel);
		check_radioPanel.setSpacing(10);
		termsLabel = new Label(messages.termsLabel());
		termsPaymentArea = new TextArea();
		termsPaymentArea.setStyleName("terms-payment-area");
		check_radio_textAreaPanel.add(termsLabel);
		check_radio_textAreaPanel.add(termsPaymentArea);

		// addInputDialogHandler(new InputDialogHandler() {
		//
		// @Override
		// public boolean onOkClick() {
		// try {
		//
		// } catch (Exception e) {
		// System.err.println(e.toString());
		// }
		// return false;
		//
		// }
		//
		// @Override
		// public void onCancelClick() {
		// removeFromParent();
		// HistoryTokenUtils.setPresentToken(MainFinanceWindow
		// .getViewManager().getCurrentView().getAction(),
		// MainFinanceWindow.getViewManager().getCurrentView()
		// .getData());
		// }
		//
		// });

		check_radio_textAreaPanel.setSpacing(10);

		mainLayoutPanel.add(addTextBoxTableControl());
		mainLayoutPanel.add(check_radio_textAreaPanel);
		panel.add(titleHtml);
		panel.add(mainLayoutPanel);
		button_textBoxPanel.add(panel);

		this.add(button_textBoxPanel);

		topMarginBox.addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				if (!UIUtils.isDouble(topMarginBox.getValue().toString())) {
					Accounter.showError(messages.numberForTopMarginField());
					addError(topMarginBox, messages.errorForTopMarginField());
					topMarginBox.setValue("");
				}
			}
		});
		bottomMarginBox.addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				if (!UIUtils.isDouble(bottomMarginBox.getValue().toString())) {
					Accounter.showError(messages.numberForbottomMarginField());
					addError(bottomMarginBox, messages
							.errorForbottomMarginField());
					bottomMarginBox.setValue("");
				}
			}
		});
		addressPadBox.addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				if (!UIUtils.isDouble(addressPadBox.getValue().toString())) {
					Accounter.showError(messages.numberForAddresspadField());
					addError(addressPadBox, messages.errorForaddresspadField());
					addressPadBox.setValue("");
				}
			}
		});

	}

	private int getPageSize() {
		if (a4Button.isChecked()) {
			return 1;
		} else {
			return 2;
		}
	}

	private void setMeasurementType(int i) {
		if (i == 1) {
			cmButton.setChecked(true);
		} else {
			inchButton.setChecked(true);
		}
	}

	private void setPazeSize(int i) {
		if (i == 1) {
			a4Button.setChecked(true);
		} else {
			usLetterButton.setChecked(true);
		}
	}

	private ClientBrandingTheme getBrandingThemeObject() {
		ClientBrandingTheme brandingTheme = getData();
		brandingTheme.setThemeName(String.valueOf(nameItem.getValue()));
		brandingTheme.setPageSizeType(getPageSize());
		brandingTheme.setTopMargin(Double.parseDouble(String
				.valueOf(topMarginBox.getValue())));
		brandingTheme.setBottomMargin(Double.parseDouble(String
				.valueOf(bottomMarginBox.getValue())));
		brandingTheme.setAddressPadding(Double.parseDouble(String
				.valueOf(addressPadBox.getValue())));
		brandingTheme.setFont(fontNameBox.getSelectedValue());
		brandingTheme.setFontSize(fontSizeBox.getSelectedValue());

		brandingTheme.setCreditMemoTitle(String.valueOf(creditNoteBox
				.getValue()));
		brandingTheme.setOverDueInvoiceTitle(String.valueOf(overdueBox
				.getValue()));
		brandingTheme
				.setStatementTitle(String.valueOf(statementBox.getValue()));
		brandingTheme.setShowTaxNumber(taxNumItem.isChecked());
		brandingTheme.setShowColumnHeadings(headingItem.isChecked());
		brandingTheme.setShowUnitPrice_And_Quantity(unitPriceItem.isChecked());
		brandingTheme.setShowTaxColumn(columnItem.isChecked());
		brandingTheme.setShowRegisteredAddress(addressItem.isChecked());
		brandingTheme.setShowLogo(logoItem.isChecked());
		brandingTheme
				.setPayPalEmailID(String.valueOf(paypalTextBox.getValue()));
		brandingTheme.setTerms_And_Payment_Advice(String
				.valueOf(termsPaymentArea.getValue()));
		brandingTheme.setContactDetails(String.valueOf(contactDetailsArea
				.getValue()));
		brandingTheme.setLogoAlignmentType(getLogoType());

		if (logoNameBox.getValue().toString().isEmpty()) {
			brandingTheme.setFileName(null);
			brandingTheme.setLogoAdded(false);
		} else {
			brandingTheme.setFileName(String.valueOf(logoNameBox.getValue()
					.toString()));
			brandingTheme.setLogoAdded(true);
		}

		return brandingTheme;
	}

	private int getLogoType() {
		if (leftRadioButton.isChecked()) {
			return 1;
		} else {
			return 2;
		}
	}

	private void setLogoType(int i) {
		if (i == 1) {
			leftRadioButton.setChecked(true);
		} else {
			rightRadioButton.setChecked(true);
		}
	}

	private VerticalPanel addRadioBoxTableControls() {
		radioButtonPanel = new VerticalPanel();

		measureLabel = new Label(messages.measure());
		logoLabel = new Label(messages.logoAlignment());
		leftRadioButton = new RadioButton(messages.logoType(), messages.left());
		rightRadioButton = new RadioButton(messages.logoType(), messages
				.right());
		leftRadioButton.setChecked(true);
		// taxesLabel = new Label(messages.showTaxesAs());
		// exclusiveButton = new RadioButton(messages.taxType(), messages
		// .exclusive());
		// inclusiveButton = new RadioButton(messages.taxType(), messages
		// .inclusive());
		// inclusiveButton.setChecked(true);

		contactDetailHtml = new HTML(accounterMessages.contactDetailsHtml());
		contactDetailsArea = new TextArea();
		contactDetailsArea.setStyleName("contact-deatils-area");

		radioButtonPanel.add(logoLabel);
		radioButtonPanel.add(leftRadioButton);
		radioButtonPanel.add(rightRadioButton);
		// radioButtonPanel.add(taxesLabel);
		// radioButtonPanel.add(exclusiveButton);
		// radioButtonPanel.add(inclusiveButton);
		radioButtonPanel.add(contactDetailHtml);
		radioButtonPanel.add(contactDetailsArea);
		radioButtonPanel.setSpacing(5);

		return radioButtonPanel;
	}

	private VerticalPanel addCheckBoxTableControls() {

		taxNumItem = new CheckBox(messages.showTaxNumber());
		taxNumItem.setChecked(true);
		headingItem = new CheckBox(messages.showColumnHeadings());
		headingItem.setChecked(true);
		unitPriceItem = new CheckBox(messages.showUnitPrice());
		unitPriceItem.setChecked(true);
		// paymentItem = new CheckBox(messages
		// .showPaymentAdvice());
		// paymentItem.setChecked(true);
		columnItem = new CheckBox(messages.showTaxColumn());
		columnItem.setChecked(true);
		addressItem = new CheckBox(messages.showRegisteredAddress());
		addressItem.setChecked(true);
		logoItem = new CheckBox(messages.showLogo());
		logoItem.setChecked(true);
		paypalEmailHtml = new HTML(accounterMessages.paypalEmailHtml());
		paypalTextBox = new TextItem();
		paypalTextBox.textBox.setTitle(messages.paypalEmail());

		checkBoxPanel = new VerticalPanel();
		checkBoxPanel.add(taxNumItem);
		checkBoxPanel.add(headingItem);
		checkBoxPanel.add(unitPriceItem);
		// checkBoxPanel.add(paymentItem);
		checkBoxPanel.add(columnItem);
		checkBoxPanel.add(addressItem);
		checkBoxPanel.add(logoItem);
		checkBoxPanel.add(paypalEmailHtml);

		DynamicForm paypalForm = new DynamicForm();
		paypalForm.setFields(paypalTextBox);
		checkBoxPanel.add(paypalForm);

		checkBoxPanel.setStyleName("rightBorder");
		checkBoxPanel.setSpacing(5);
		return checkBoxPanel;

	}

	private HorizontalPanel addTextBoxTableControl() {

		nameItem = new TextItem("Name");
		nameItem.addStyleName("name-item");
		pageSizeLabel = new Label(messages.pageSize());
		topMarginBox = new TextItem(messages.topMargin());
		topMarginBox.setValue(messages.topMarginValue());
		bottomMarginBox = new TextItem(messages.bottomMargin());
		bottomMarginBox.setValue(messages.bottomMarginValue());
		addressPadBox = new TextItem(messages.addressPadding());
		addressPadBox.setValue(messages.addressPaddingValue());
		// draftBox = new TextBox();
		// draftBox.setText(messages
		// .draftBoxValue());
		// approvedBox = new TextBox();
		// approvedBox.setText(messages
		// .approvedValue());
		overdueBox = new TextItem(messages.overdueInvoiceTitle());
		overdueBox.setValue(messages.overdueValue());
		creditNoteBox = new TextItem(messages.creditNoteTitle());
		creditNoteBox.setValue(messages.creditNoteValue());
		statementBox = new TextItem(messages.statementTitle());
		statementBox.setValue(messages.statement());

		a4Button = new RadioButton(messages.pageType(), messages.a4());
		usLetterButton = new RadioButton(messages.pageType(), messages
				.usLetter());
		a4Button.setChecked(true);

		cmButton = new RadioButton(messages.measureType(), messages.cm());
		inchButton = new RadioButton(messages.measureType(), messages.inch());
		cmButton.setChecked(true);

		fontNameArray = new String[] { messages.arial(), messages.calibri(),
				messages.cambria(), messages.georgia(), messages.myriad(),
				messages.tahoma(), messages.timesNewRoman(),
				messages.trebuchet() };
		fontSizeArray = new String[] { messages.point8(), messages.point9(),
				messages.point10(), messages.point11(),
		// messages.point13(),
		// messages.point14(),
		// messages.point15()
		};

		fontNameBox = new SelectCombo(messages.font());
		// fontNameBox.setWidth(100);
		listOfFontNames = new ArrayList<String>();
		for (int i = 0; i < fontNameArray.length; i++) {
			listOfFontNames.add(fontNameArray[i]);
		}
		fontNameBox.initCombo(listOfFontNames);
		fontNameBox.setComboItem(fontNameArray[0]);
		fontNameBox
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						if (selectItem != null)
							fontNameBox.setComboItem(selectItem);

					}
				});

		fontSizeBox = new SelectCombo(messages.fontSize());
		fontSizeBox.setWidth(100);
		listOfFontSizes = new ArrayList<String>();
		for (int i = 0; i < fontSizeArray.length; i++) {
			listOfFontSizes.add(fontSizeArray[i]);
		}
		fontSizeBox.initCombo(listOfFontSizes);
		fontSizeBox.setComboItem(fontSizeArray[0]);
		fontSizeBox
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						if (selectItem != null)
							fontSizeBox.setComboItem(selectItem);
					}
				});

		// DynamicForm fontNameForm = new DynamicForm();
		// fontNameForm.setCellSpacing(0);
		// fontNameForm.setNumCols(1);
		// fontNameForm.setFields(fontNameBox);
		// DynamicForm fontSizeForm = new DynamicForm();
		// fontSizeForm.setCellSpacing(0);
		// fontSizeForm.setNumCols(1);
		// fontSizeForm.setFields(fontSizeBox);

		HorizontalPanel unitsPanel = new HorizontalPanel();
		unitsPanel.add(cmButton);
		unitsPanel.add(inchButton);

		VerticalPanel measurePanel = new VerticalPanel();
		measurePanel.add(measureLabel);
		// measurePanel.setCellHorizontalAlignment(measureLabel,
		// HasAlignment.ALIGN_CENTER);
		measurePanel.add(unitsPanel);
		measurePanel.setStyleName("measurePanel");

		nameForm = new DynamicForm();
		// nameForm.setCellSpacing(0);
		nameForm.setNumCols(2);
		nameItem.setRequired(true);
		nameForm.setFields(nameItem);
		nameForm.setWidth("110px");

		logoNameBox = new TextItem(messages.addLogo());
		logoNameBox.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ValueCallBack<ClientBrandingTheme> callback = new ValueCallBack<ClientBrandingTheme>() {
					@Override
					public void execute(ClientBrandingTheme value) {
						logoNameBox.setValue(value.getFileName());
					}
				};
				String[] filetypes = { "png", "jpg", "gif" };

				FileUploadDilaog dilaog = new FileUploadDilaog("Upload Logo",
						"parent", callback, filetypes, getData());
			}
		});

		textBoxTable = new FlexTable();
		DynamicForm dynamicForm = new DynamicForm();

		textBoxTable.setWidget(0, 0, pageSizeLabel);
		textBoxTable.setWidget(0, 1, a4Button);
		textBoxTable.setWidget(1, 1, usLetterButton);
		dynamicForm.setNumCols(2);
		dynamicForm.setFields(topMarginBox, bottomMarginBox, addressPadBox,
				fontNameBox, fontSizeBox, overdueBox, creditNoteBox,
				statementBox, logoNameBox);

		// textBoxTable.setWidget(2, 1, );
		// textBoxTable.setWidget(3, 0, bottomMarginLabel);
		// textBoxTable.setWidget(3, 1, );
		// textBoxTable.setWidget(4, 0, addressPadLabel);
		// textBoxTable.setWidget(4, 1, );
		// textBoxTable.setWidget(5, 0, fontLabel);
		// textBoxTable.setWidget(5, 1, fontNameForm);
		// textBoxTable.setWidget(6, 0, fontSizeLabel);
		// textBoxTable.setWidget(6, 1, fontSizeForm);
		// // textBoxTable.setWidget(8, 0, draftLabel);
		// // textBoxTable.setWidget(8, 1, draftBox);
		// // textBoxTable.setWidget(9, 0, approvedLabel);
		// // textBoxTable.setWidget(9, 1, approvedBox);
		// textBoxTable.setWidget(7, 0, overdueLabel);
		// textBoxTable.setWidget(7, 1, );
		// textBoxTable.setWidget(8, 0, creditNoteLabel);
		// textBoxTable.setWidget(8, 1, );
		// textBoxTable.setWidget(9, 0, statementLabel);
		// textBoxTable.setWidget(9, 1, );
		// textBoxTable.setWidget(10, 0, addLogoLabel);
		// textBoxTable.setWidget(10, 1, );

		HorizontalPanel textBoxHorizontalPanel = new HorizontalPanel();

		VerticalPanel textBoxPanel = new VerticalPanel();
		textBoxPanel.add(nameForm);
		textBoxPanel.add(textBoxTable);
		textBoxTable.setWidth("86%");
		textBoxPanel.add(dynamicForm);
		textBoxHorizontalPanel.add(textBoxPanel);
		textBoxHorizontalPanel.add(measurePanel);
		textBoxHorizontalPanel.setSpacing(10);
		textBoxHorizontalPanel.setStyleName("rightBorder");

		return textBoxHorizontalPanel;
	}

	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		ClientBrandingTheme brandingThemeByName = Accounter.getCompany()
				.getBrandingThemeByName(data.getThemeName());
		if (getData() == null && brandingThemeByName != null) {
			result.addError("TakenTheme", "Theme Name already exists");
			return result;
		}
		result.add(nameForm.validate());
		return result;
	}

	@Override
	public void saveSuccess(IAccounterCore object) {
		NewBrandingThemeView.this.removeFromParent();
		super.saveSuccess(object);
		ActionFactory.getInvoiceBrandingAction().run(null, true);
	}

	@Override
	protected String getViewTitle() {
		return messages.editBrandThemeLabel();
	}

	@Override
	public List<DynamicForm> getForms() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveAndUpdateView() {
		ClientBrandingTheme brandingTheme = getBrandingThemeObject();
		ClientBrandingTheme brandingThemeByName = Accounter.getCompany()
				.getBrandingThemeByName(brandingTheme.getThemeName());
		if (brandingTheme == null) {
			// TODO Do this checking in validation method
		}
		// if (!Utility.isObjectExist(Accounter.getCompany().getBrandingTheme(),
		// brandingTheme.getThemeName())) {
		// // TODO Do this checking in validation method
		// }
		saveOrUpdate(brandingTheme);
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

	@Override
	public void deleteFailed(AccounterException caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(IAccounterCore result){
		// TODO Auto-generated method stub

	}

	@Override
	protected void createButtons(ButtonBar buttonBar) {
		super.createButtons(buttonBar);
		buttonBar.remove(this.saveAndNewButton);
	}
}
