package com.vimukti.accounter.web.client.ui.edittable.tables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCreditsAndPayments;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransactionCreditsAndPayments;
import com.vimukti.accounter.web.client.core.ClientTransactionReceivePayment;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.CashDiscountDialog;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.ICurrencyProvider;
import com.vimukti.accounter.web.client.ui.core.InputDialogHandler;
import com.vimukti.accounter.web.client.ui.customers.NewApplyCreditsDialog;
import com.vimukti.accounter.web.client.ui.customers.WriteOffDialog;
import com.vimukti.accounter.web.client.ui.edittable.AmountColumn;
import com.vimukti.accounter.web.client.ui.edittable.AnchorEditColumn;
import com.vimukti.accounter.web.client.ui.edittable.CheckboxEditColumn;
import com.vimukti.accounter.web.client.ui.edittable.EditTable;
import com.vimukti.accounter.web.client.ui.edittable.RenderContext;
import com.vimukti.accounter.web.client.ui.edittable.TextEditColumn;
import com.vimukti.accounter.web.client.ui.widgets.DateUtills;

public abstract class TransactionReceivePaymentTable extends
		EditTable<ClientTransactionReceivePayment> {
	private ClientCompany company;
	private boolean canEdit;

	ClientCustomer customer;
	List<Integer> selectedValues = new ArrayList<Integer>();
	protected boolean gotCreditsAndPayments;
	private CashDiscountDialog cashDiscountDialog;
	private WriteOffDialog writeOffDialog;

	// public CustomerCreditsAndPaymentsDialiog creditsAndPaymentsDialiog;
	public NewApplyCreditsDialog newAppliedCreditsDialiog;
	public List<ClientCreditsAndPayments> updatedCustomerCreditsAndPayments = new ArrayList<ClientCreditsAndPayments>();
	public Map<ClientTransactionReceivePayment, List<ClientCreditsAndPayments>> value;

	/* This stack tracks the recently applied credits */
	public Stack<Map<Integer, Object>> creditsStack;
	public Map<Integer, Object> revrtedCreditsMap;
	private Stack<Map<Integer, Object>> revertedCreditsStack;

	List<ClientTransactionReceivePayment> tranReceivePayments = new ArrayList<ClientTransactionReceivePayment>();
	private ICurrencyProvider currencyProvider;
	private boolean enableDiscount;

	public TransactionReceivePaymentTable(boolean enableDisCount,
			boolean canEdit, ICurrencyProvider currencyProvider) {
		this.currencyProvider = currencyProvider;
		this.canEdit = canEdit;
		this.enableDiscount = enableDisCount;
		this.company = Accounter.getCompany();
	}

	protected void initColumns() {
		this.addColumn(new CheckboxEditColumn<ClientTransactionReceivePayment>() {

			@Override
			protected void onChangeValue(boolean value,
					ClientTransactionReceivePayment row) {
				onSelectionChanged(row, value);
			}

			@Override
			public void render(IsWidget widget,
					RenderContext<ClientTransactionReceivePayment> context) {
				super.render(widget, context);
				if (isInViewMode()) {
					((CheckBox) widget).setValue(true);
				}
			}
		});
		if (canEdit) {
			TextEditColumn<ClientTransactionReceivePayment> dateCoulmn = new TextEditColumn<ClientTransactionReceivePayment>() {

				@Override
				protected void setValue(ClientTransactionReceivePayment row,
						String value) {
				}

				@Override
				protected String getValue(ClientTransactionReceivePayment row) {
					return DateUtills.getDateAsString(new ClientFinanceDate(row
							.getDueDate()).getDateAsObject());
				}

				@Override
				public int getWidth() {
					return 100;
				}

				@Override
				protected boolean isEnable() {
					return false;
				}

				@Override
				protected String getColumnName() {
					return Accounter.messages().dueDate();
				}
			};
			this.addColumn(dateCoulmn);
		}

		TextEditColumn<ClientTransactionReceivePayment> invoiceNumber = new TextEditColumn<ClientTransactionReceivePayment>() {

			@Override
			protected String getValue(ClientTransactionReceivePayment row) {
				return row.getNumber();
			}

			@Override
			protected void setValue(ClientTransactionReceivePayment row,
					String value) {
				// No Need
			}

			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			public int getWidth() {
				return 100;
			}

			@Override
			protected String getColumnName() {
				return Accounter.messages().invoice();
			}
		};
		this.addColumn(invoiceNumber);

		AmountColumn<ClientTransactionReceivePayment> invoiceAmountColumn = new AmountColumn<ClientTransactionReceivePayment>(
				currencyProvider, false) {

			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			public int getWidth() {
				return 100;
			}

			@Override
			protected String getColumnName() {
				return getColumnNameWithCurrency(Accounter.messages()
						.invoiceAmount());
			}

			@Override
			protected Double getAmount(ClientTransactionReceivePayment row) {
				return row.getInvoiceAmount();
			}

			@Override
			protected void setAmount(ClientTransactionReceivePayment row,
					Double value) {

			}
		};
		this.addColumn(invoiceAmountColumn);

		if (canEdit) {
			AmountColumn<ClientTransactionReceivePayment> amountDueColumn = new AmountColumn<ClientTransactionReceivePayment>(
					currencyProvider, false) {

				@Override
				protected boolean isEnable() {
					return false;
				}

				@Override
				public int getWidth() {
					return 100;
				}

				@Override
				protected String getColumnName() {
					return getColumnNameWithCurrency(Accounter.messages()
							.amountDue());
				}

				@Override
				protected Double getAmount(ClientTransactionReceivePayment row) {
					return row.getAmountDue();
				}

				@Override
				protected void setAmount(ClientTransactionReceivePayment row,
						Double value) {

				}
			};
			this.addColumn(amountDueColumn);
		}

		TextEditColumn<ClientTransactionReceivePayment> discountDateColumn = new TextEditColumn<ClientTransactionReceivePayment>() {

			@Override
			protected void setValue(ClientTransactionReceivePayment row,
					String value) {
			}

			@Override
			protected String getValue(ClientTransactionReceivePayment row) {
				return "";// row.getDiscountDate();
			}

			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			public int getWidth() {
				return 100;
			}

			@Override
			protected String getColumnName() {
				return Accounter.messages().discountDate();
			}
		};
		// this.addColumn(discountDateColumn);

		AnchorEditColumn<ClientTransactionReceivePayment> cashDiscountColumn = new AnchorEditColumn<ClientTransactionReceivePayment>() {

			@Override
			protected void onClick(ClientTransactionReceivePayment row) {
				openCashDiscountDialog(row);
			}

			@Override
			protected String getValue(ClientTransactionReceivePayment row) {
				return DataUtils.getAmountAsStringInPrimaryCurrency(row
						.getCashDiscount());
			}

			@Override
			public int getWidth() {
				return 93;
			}

			@Override
			protected String getColumnName() {
				return Accounter.messages().cashDiscount();
			}

			@Override
			protected boolean isEnable(ClientTransactionReceivePayment row) {
				return selectedValues.contains(indexOf(row));
			}
		};
		if (enableDiscount) {
			this.addColumn(cashDiscountColumn);
		}

		AnchorEditColumn<ClientTransactionReceivePayment> writeOffColumn = new AnchorEditColumn<ClientTransactionReceivePayment>() {

			@Override
			protected void onClick(ClientTransactionReceivePayment row) {
				openWriteOffDialog(row);
			}

			@Override
			protected String getValue(ClientTransactionReceivePayment row) {
				return DataUtils.getAmountAsStringInCurrency(row.getWriteOff(),
						null);
			}

			@Override
			public int getWidth() {
				return 100;
			}

			@Override
			protected String getColumnName() {
				return Accounter.messages().writeOff();
			}

			@Override
			protected boolean isEnable(ClientTransactionReceivePayment row) {
				return selectedValues.contains(indexOf(row));
			}
		};
		this.addColumn(writeOffColumn);

		AnchorEditColumn<ClientTransactionReceivePayment> appliedCreditsColumn = new AnchorEditColumn<ClientTransactionReceivePayment>() {

			@Override
			protected void onClick(ClientTransactionReceivePayment row) {
				openCreditsDialog(row);
			}

			@Override
			protected String getValue(ClientTransactionReceivePayment row) {
				return DataUtils.getAmountAsStringInCurrency(
						row.getAppliedCredits(), null);
			}

			@Override
			public int getWidth() {
				return 100;
			}

			@Override
			protected String getColumnName() {
				return Accounter.messages().appliedCredits();
			}

			@Override
			protected boolean isEnable(ClientTransactionReceivePayment row) {
				return selectedValues.contains(indexOf(row));
			}
		};
		this.addColumn(appliedCreditsColumn);

		TextEditColumn<ClientTransactionReceivePayment> paymentColumn = new AmountColumn<ClientTransactionReceivePayment>(
				currencyProvider, false) {

			@Override
			public void render(IsWidget widget,
					RenderContext<ClientTransactionReceivePayment> context) {
				TextBoxBase box = (TextBoxBase) widget;
				String value = getValue(context.getRow());
				box.setEnabled(isEnable(context.getRow())
						&& !context.isDesable());
				box.setText(value);
			}

			private boolean isEnable(ClientTransactionReceivePayment row) {
				return selectedValues.contains(indexOf(row));
			}

			@Override
			protected String getColumnName() {
				return getColumnNameWithCurrency(Accounter.messages().payment());
			}

			@Override
			protected Double getAmount(ClientTransactionReceivePayment row) {
				return row.getPayment();
			}

			@Override
			protected void setAmount(ClientTransactionReceivePayment item,
					Double value) {
				if (isInViewMode()) {
					return;
				}
				double amt, originalPayment;
				try {
					originalPayment = item.getPayment();
					amt = value;
					if (!isSelected(item)) {
						item.setPayment(item.getAmountDue());
						onSelectionChanged(item, true);
					}
					item.setPayment(amt);
					updateAmountDue(item);

					double totalValue = item.getCashDiscount()
							+ item.getWriteOff() + item.getAppliedCredits()
							+ item.getPayment();
					if (AccounterValidator.validatePayment(item.getAmountDue(),
							totalValue)) {
						recalculateGridAmounts();
						updateTotalPayment(0.0);
					} else {
						item.setPayment(originalPayment);
					}
					update(item);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		this.addColumn(paymentColumn);
	}

	protected abstract boolean isInViewMode();

	public ValidationResult validateGrid() {
		ValidationResult result = new ValidationResult();
		// Validates receive amount exceeds due amount
		for (ClientTransactionReceivePayment transactionReceivePayment : this
				.getSelectedRecords()) {
			double totalValue = getTotalValue(transactionReceivePayment);
			if (DecimalUtil.isLessThan(totalValue, 0.00)) {
				result.addError(this,
						messages.valueCannotBe0orlessthan0(messages.amount()));
			} else if (DecimalUtil.isGreaterThan(totalValue,
					transactionReceivePayment.getAmountDue())
					|| DecimalUtil.isEquals(totalValue, 0)) {
				result.addError(this, Accounter.messages()
						.receivePaymentExcessDue());
			}
		}
		return result;
	}

	private double getTotalValue(ClientTransactionReceivePayment payment) {
		double totalValue = payment.getWriteOff() + payment.getAppliedCredits()
				+ payment.getPayment();
		if (enableDiscount) {
			totalValue += payment.getCashDiscount();
		}
		return totalValue;
	}

	public void initCreditsAndPayments(final ClientCustomer customer) {

		Accounter
				.createHomeService()
				.getCustomerCreditsAndPayments(
						customer.getID(),
						new AccounterAsyncCallback<ArrayList<ClientCreditsAndPayments>>() {

							public void onException(AccounterException caught) {
								Accounter.showInformation(Accounter.messages()
										.failedTogetCreditsListAndPayments(
												customer.getName()));

								gotCreditsAndPayments = false;
								return;

							}

							public void onResultSuccess(
									ArrayList<ClientCreditsAndPayments> result) {
								if (result == null)
									onFailure(null);

								updatedCustomerCreditsAndPayments = result;
								creditsStack = new Stack<Map<Integer, Object>>();
								gotCreditsAndPayments = true;
								calculateUnusedCredits();
							}

						});

	}

	protected abstract void calculateUnusedCredits();

	public void openCashDiscountDialog(
			final ClientTransactionReceivePayment selectedObject) {
		cashDiscountDialog = new CashDiscountDialog(canEdit,
				selectedObject.getCashDiscount(),
				getCashDiscountAccount(selectedObject), currencyProvider);
		// } else {
		// cashDiscountDialog.setCanEdit(canEdit);
		// cashDiscountDialog.setCashDiscountValue(selectedObject
		// .getCashDiscount());
		// cashDiscountDialog.
		// }
		cashDiscountDialog.addInputDialogHandler(new InputDialogHandler() {

			@Override
			public void onCancel() {

			}

			@Override
			public boolean onOK() {
				if (canEdit) {
					if (cashDiscountDialog.getSelectedDiscountAccount() != null) {
						selectedObject.setPayment(0.0);
						selectedObject.setCashDiscount(cashDiscountDialog
								.getCashDiscount());
						selectedObject.setDiscountAccount(cashDiscountDialog
								.getSelectedDiscountAccount().getID());
						if (validatePaymentValue(selectedObject)) {
							updatePayment(selectedObject);
						} else {
							selectedObject.setCashDiscount(0.0D);
							updatePayment(selectedObject);
						}
						recalculateGridAmounts();

						update(selectedObject);
					} else
						return false;
				}
				return true;
			}
		});
		cashDiscountDialog.show();
	}

	private ClientAccount getCashDiscountAccount(
			ClientTransactionReceivePayment selectedObject) {
		ClientAccount cashDiscountAccount = this.company
				.getAccount(selectedObject.getDiscountAccount());
		return cashDiscountAccount;
	}

	private ClientAccount getWriteOffAccount(
			ClientTransactionReceivePayment selectedObject) {
		ClientAccount writeOffAccount = this.company.getAccount(selectedObject
				.getWriteOffAccount());
		return writeOffAccount;
	}

	public void openCreditsDialog(
			final ClientTransactionReceivePayment selectedObject) {
		if (gotCreditsAndPayments) {
			if (/*
				 * creditsAndPaymentsDialiog == null ||
				 */newAppliedCreditsDialiog == null) {
				for (ClientCreditsAndPayments rec : updatedCustomerCreditsAndPayments) {
					rec.setActualAmt(rec.getBalance());
					rec.setRemaoningBalance(rec.getBalance());
				}
				// creditsAndPaymentsDialiog = new
				// CustomerCreditsAndPaymentsDialiog(
				// this.customer, updatedCustomerCreditsAndPayments,
				// canEdit, selectedObject);
				newAppliedCreditsDialiog = new NewApplyCreditsDialog(
						this.customer, updatedCustomerCreditsAndPayments,
						canEdit, selectedObject, currencyProvider);

			} else {
				if (selectedObject.isCreditsApplied()) {
					Map<Integer, Object> appliedCredits = selectedObject
							.getTempCredits();
					int size = updatedCustomerCreditsAndPayments.size();
					for (int i = 0; i < size; i++) {
						if (appliedCredits.containsKey(i)) {
							ClientCreditsAndPayments selectdCredit = updatedCustomerCreditsAndPayments
									.get(i);
							TempCredit tmpCr = (TempCredit) appliedCredits
									.get(i);
							selectdCredit.setAmtTouse(tmpCr.getAmountToUse());
							selectdCredit.setBalance(selectdCredit
									.getRemaoningBalance());
						} else {
							ClientCreditsAndPayments unSelectdCredit = updatedCustomerCreditsAndPayments
									.get(i);
							unSelectdCredit.setAmtTouse(0);
						}
					}
				} else {
					if (revertedCreditsStack != null
							&& revertedCreditsStack.size() != 0) {
						Map<Integer, Object> stkCredit = revertedCreditsStack
								.peek();

						for (Integer indx : stkCredit.keySet()) {

							TempCredit tempCrt = (TempCredit) stkCredit
									.get(indx);
							ClientCreditsAndPayments rec = updatedCustomerCreditsAndPayments
									.get(indx.intValue());
							rec.setBalance(tempCrt.getRemainingBalance()
									+ tempCrt.getAmountToUse());
							rec.setRemaoningBalance(rec.getBalance());
							rec.setAmtTouse(0);
						}
						revertedCreditsStack.clear();
					} else if (creditsStack != null && creditsStack.size() != 0) {
						Map<Integer, Object> stkCredit = creditsStack.peek();

						for (Integer indx : stkCredit.keySet()) {

							TempCredit tempCrt = (TempCredit) stkCredit
									.get(indx);
							ClientCreditsAndPayments rec = updatedCustomerCreditsAndPayments
									.get(indx.intValue());
							rec.setBalance(tempCrt.getRemainingBalance());
							rec.setRemaoningBalance(rec.getBalance());
							rec.setAmtTouse(0);
						}
					}
				}
				// creditsAndPaymentsDialiog
				// .setUpdatedCreditsAndPayments(updatedCustomerCreditsAndPayments);
				// creditsAndPaymentsDialiog.setCanEdit(canEdit);
				// creditsAndPaymentsDialiog.setRecord(selectedObject);
				// creditsAndPaymentsDialiog.setCustomer(customer);
				// creditsAndPaymentsDialiog.updateFields();

				newAppliedCreditsDialiog
						.setUpdatedCreditsAndPayments(updatedCustomerCreditsAndPayments);
				newAppliedCreditsDialiog.setCanEdit(canEdit);
				newAppliedCreditsDialiog.setRecord(selectedObject);
				newAppliedCreditsDialiog.setCustomer(customer);
				newAppliedCreditsDialiog.updateFields();
			}
		} else if (!gotCreditsAndPayments && canEdit) {
			Accounter.showInformation(Accounter.messages()
					.noCreditsforthiscustomer(Global.get().customer()));
		}
		if (!canEdit) {
			// creditsAndPaymentsDialiog = new
			// CustomerCreditsAndPaymentsDialiog(
			// this.customer,
			// getSelectedCreditsAndPayments(selectedObject), canEdit,
			// selectedObject);

			newAppliedCreditsDialiog = new NewApplyCreditsDialog(this.customer,
					getSelectedCreditsAndPayments(selectedObject), canEdit,
					selectedObject, currencyProvider);
		}

		if (newAppliedCreditsDialiog == null)
			return;

		newAppliedCreditsDialiog
				.addInputDialogHandler(new InputDialogHandler() {

					@Override
					public boolean onOK() {

						List<ClientCreditsAndPayments> appliedCreditsForThisRec = newAppliedCreditsDialiog
								.getAppliedCredits();
						Map<Integer, Object> appliedCredits = new HashMap<Integer, Object>();
						TempCredit creditRec = null;

						for (ClientCreditsAndPayments rec : appliedCreditsForThisRec) {
							try {
								checkBalance(rec.getAmtTouse());
							} catch (Exception e) {
								Accounter.showError(e.getMessage());
								return false;
							}
							Integer recordIndx = newAppliedCreditsDialiog.grid
									.indexOf(rec);
							creditRec = new TempCredit();
							for (ClientTransactionReceivePayment rcvp : getSelectedRecords()) {
								if (rcvp.isCreditsApplied()) {
									for (Integer idx : rcvp.getTempCredits()
											.keySet()) {
										if (recordIndx == idx)
											((TempCredit) rcvp.getTempCredits()
													.get(idx))
													.setRemainingBalance(rec
															.getBalance());
									}
								}
							}
							creditRec.setRemainingBalance(rec.getBalance());
							creditRec.setAmountToUse(rec.getAmtTouse());
							appliedCredits.put(recordIndx, creditRec);
						}
						selectedObject.setTempCredits(appliedCredits);
						selectedObject.setCreditsApplied(true);

						creditsStack.push(appliedCredits);

						try {

							newAppliedCreditsDialiog.okClicked = true;

							// creditsAndPaymentsDialiog.validateTransaction();

						} catch (Exception e) {
							Accounter.showError(e.getMessage());
							return false;
						}

						selectedObject
								.setAppliedCredits(newAppliedCreditsDialiog
										.getTotalCreditAmount());
						updatePayment(selectedObject);
						recalculateGridAmounts();
						update(selectedObject);
						return false;
					}

					@Override
					public void onCancel() {
						newAppliedCreditsDialiog.cancelClicked = true;

					}
				});

		newAppliedCreditsDialiog.show();

	}

	public void revertCredits() {
		if (revertedCreditsStack != null && revertedCreditsStack.size() != 0) {
			Map<Integer, Object> stkCredit = revertedCreditsStack.peek();

			for (Integer indx : stkCredit.keySet()) {

				TempCredit tempCrt = (TempCredit) stkCredit.get(indx);
				ClientCreditsAndPayments rec = updatedCustomerCreditsAndPayments
						.get(indx.intValue());
				rec.setBalance(rec.getBalance() + tempCrt.getAmountToUse());
				rec.setRemaoningBalance(rec.getBalance());
				rec.setAmtTouse(0);
			}
			if (creditsStack.contains(stkCredit)) {
				creditsStack.remove(stkCredit);
			}
			revertedCreditsStack.clear();
		}
	}

	public void checkBalance(double amount) throws Exception {
		if (DecimalUtil.isEquals(amount, 0))
			throw new Exception(Accounter.messages()
					.youdnthaveBalToApplyCredits());
	}

	public class TempCredit {
		double remainingBalance;
		double amountToUse;

		public double getRemainingBalance() {
			return remainingBalance;
		}

		public void setRemainingBalance(double remainingBalance) {
			this.remainingBalance = remainingBalance;
		}

		public double getAmountToUse() {
			return amountToUse;
		}

		public void setAmountToUse(double amountToUse) {
			this.amountToUse = amountToUse;
		}

	}

	private List<ClientCreditsAndPayments> getSelectedCreditsAndPayments(
			ClientTransactionReceivePayment selectedObject) {
		List<ClientCreditsAndPayments> createdCreditsAndPayments = new ArrayList<ClientCreditsAndPayments>();
		ClientTransactionReceivePayment tranReceivePayment;
		tranReceivePayment = this.tranReceivePayments
				.get(indexOf(selectedObject));
		for (ClientTransactionCreditsAndPayments trancreditsAndPaymnets : tranReceivePayment
				.getTransactionCreditsAndPayments()) {
			ClientCreditsAndPayments creditsAmdPayment = trancreditsAndPaymnets
					.getCreditsAndPayments();
			creditsAmdPayment.setAmtTouse(trancreditsAndPaymnets
					.getAmountToUse());
			createdCreditsAndPayments.add(creditsAmdPayment);
		}
		return createdCreditsAndPayments;
	}

	public void setTranReceivePayments(
			List<ClientTransactionReceivePayment> recievePayments) {
		for (ClientTransactionReceivePayment payment : recievePayments) {
			payment.setID(0);
		}
		this.tranReceivePayments = recievePayments;
	}

	protected boolean validatePaymentValue(
			ClientTransactionReceivePayment selectedObject) {
		double totalValue = getTotalValue(selectedObject);
		if (AccounterValidator.isValidReceive_Payment(selectedObject
				.getAmountDue(), totalValue, Accounter.messages()
				.receiveAmountPayDue())) {
			return true;
		} else
			return false;

	}

	private void updatePayment(ClientTransactionReceivePayment payment) {
		payment.setPayment(0);
		double paymentValue = payment.getAmountDue() - getTotalValue(payment);
		payment.setPayment(paymentValue);
		updateAmountDue(payment);
		updateTotalPayment(payment.getPayment());
	}

	public void openWriteOffDialog(
			final ClientTransactionReceivePayment selectedObject) {
		writeOffDialog = new WriteOffDialog(this.company.getActiveAccounts(),
				selectedObject, canEdit, getWriteOffAccount(selectedObject),
				currencyProvider);
		writeOffDialog.addInputDialogHandler(new InputDialogHandler() {

			@Override
			public void onCancel() {

			}

			@Override
			public boolean onOK() {
				if (canEdit) {
					if (writeOffDialog.getSelectedWriteOffAccount() != null) {
						selectedObject.setPayment(0.0);
						selectedObject.setWriteOff(writeOffDialog
								.getCashDiscountValue());
						selectedObject.setWriteOffAccount(writeOffDialog
								.getSelectedWriteOffAccount().getID());
						if (validatePaymentValue(selectedObject)) {
							updatePayment(selectedObject);
						} else {
							selectedObject.setWriteOff(0.0D);
							updatePayment(selectedObject);
						}
						recalculateGridAmounts();
						update(selectedObject);
					}

				}
				return true;
			}
		});
		writeOffDialog.show();
	}

	public void updateValue(ClientTransactionReceivePayment obj) {
		updateTotalPayment(obj.getPayment());
		obj.setDummyDue(obj.getAmountDue() - obj.getPayment());
		update(obj);
	}

	public abstract void updateTotalPayment(Double payment);

	private void onHeaderCheckBoxClick(boolean isChecked) {
		resetValues();
		if (isChecked) {
			List<ClientTransactionReceivePayment> allRows = getAllRows();
			for (ClientTransactionReceivePayment row : allRows) {
				onSelectionChanged(row, true);
			}
		}
	}

	private void resetValues() {
		for (ClientCreditsAndPayments crdt : updatedCustomerCreditsAndPayments) {
			crdt.setBalance(crdt.getActualAmt());
			crdt.setRemaoningBalance(crdt.getBalance());
			crdt.setAmtTouse(0);
		}
		for (ClientTransactionReceivePayment obj : this.getRecords()) {
			resetValue(obj);
			recalculateGridAmounts();
			// if (creditsAndPaymentsDialiog != null
			// && creditsAndPaymentsDialiog.grid.getRecords().size() == 0)
			if (newAppliedCreditsDialiog != null
					&& newAppliedCreditsDialiog.grid.getRecords().size() == 0)
				creditsStack.clear();
			selectedValues.remove((Integer) indexOf(obj));
		}
		newAppliedCreditsDialiog = null;
		cashDiscountDialog = null;
		writeOffDialog = null;
	}

	protected abstract void recalculateGridAmounts();

	public int indexOf(ClientTransactionReceivePayment selectedObject) {
		return getAllRows().indexOf(selectedObject);
	}

	public void resetValue(ClientTransactionReceivePayment obj) {
		if (obj.isCreditsApplied()) {
			int size = updatedCustomerCreditsAndPayments.size();
			Map<Integer, Object> toBeRvrtMap = obj.getTempCredits();
			/* 'i' is creditRecord(in creditGrid) index */
			for (int i = 0; i < size; i++) {
				if (toBeRvrtMap.containsKey(i)) {
					TempCredit toBeAddCr = (TempCredit) toBeRvrtMap.get(i);
					/*
					 * search for this revertedCreditRecord in all selected
					 * payBill record's credits
					 */
					if (getSelectedRecords().size() != 0) {
						for (int j = 0; j < getSelectedRecords().size(); j++) {
							Map<Integer, Object> rcvCrsMap = getSelectedRecords()
									.get(j).getTempCredits();
							if (rcvCrsMap.containsKey(i)) {
								TempCredit chngCrd = (TempCredit) rcvCrsMap
										.get(i);
								chngCrd.setRemainingBalance(chngCrd
										.getRemainingBalance()
										+ toBeAddCr.getAmountToUse());
							}
						}
					}
					revertedCreditsStack = new Stack<Map<Integer, Object>>();
					revertedCreditsStack.push(toBeRvrtMap);
				}
			}

			obj.setCreditsApplied(false);
			revertCredits();
		}
		if (newAppliedCreditsDialiog != null
				&& newAppliedCreditsDialiog.grid.getRecords().size() == 0)
			creditsStack.clear();

		// setAccountDefaultValues(obj);
		deleteTotalPayment(obj.getPayment());
		obj.setPayment(0.0d);
		// obj.setCashDiscount(0.0d);
		obj.setWriteOff(0.0d);
		obj.setAppliedCredits(0.0d);
		obj.setDummyDue(obj.getAmountDue());
	}

	protected abstract void deleteTotalPayment(double d);

	public List<ClientTransactionReceivePayment> getSelectedRecords() {
		return super.getSelectedRecords(0);
	}

	public void updateAmountDue(ClientTransactionReceivePayment item) {
		double totalValue = item.getCashDiscount() + item.getWriteOff()
				+ item.getAppliedCredits() + item.getPayment();
		double amount = item.getAmountDue();
		if (!DecimalUtil.isGreaterThan(totalValue, amount)) {
			if (!DecimalUtil.isLessThan(item.getPayment(), 0.00))
				item.setDummyDue(amount - totalValue);
			else
				item.setDummyDue(amount + item.getPayment() - totalValue);

		}
	}

	public void setCustomer(ClientCustomer customer) {
		this.customer = customer;
	}

	public void removeAllRecords() {
		this.clear();
	}

	public void selectRow(int count) {
		onSelectionChanged(getAllRows().get(count), true);
	}

	private void onSelectionChanged(ClientTransactionReceivePayment obj,
			boolean isChecked) {

		int row = indexOf(obj);
		if (isChecked) {
			selectedValues.add(row);
			if (!isInViewMode()) {
				updatePayment(obj);
			}
		} else {
			selectedValues.remove((Integer) row);
			resetValue(obj);
		}
		update(obj);
		super.checkColumn(row, 0, isChecked);
		recalculateGridAmounts();
	}

	// private void updateAmountReceived() {
	// double toBeSetAmount = 0.0;
	// for (ClientTransactionReceivePayment receivePayment :
	// getSelectedRecords()) {
	// toBeSetAmount += receivePayment.getPayment();
	// }
	// setAmountRecieved(toBeSetAmount);
	// }

	protected abstract void setAmountRecieved(double toBeSetAmount);

	public void addLoadingImagePanel() {
		// TODO Auto-generated method stub

	}

	public List<ClientTransactionReceivePayment> getRecords() {
		return getAllRows();
	}

	public boolean isSelected(ClientTransactionReceivePayment trprecord) {
		return super.isChecked(trprecord, 0);
	}

	public void updateAmountsFromGUI() {
		for (ClientTransactionReceivePayment item : this.getAllRows()) {
			if (selectedValues.contains(indexOf(item))) {
				updatePayment(item);
			}
			updateFromGUI(item);
		}
		updateColumnHeaders();
	}
}
