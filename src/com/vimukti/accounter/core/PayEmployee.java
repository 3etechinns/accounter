
package com.vimukti.accounter.core;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.json.JSONException;

import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

public class PayEmployee extends Transaction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Employee employee;

	private EmployeeGroup employeeGroup;

	private Account payAccount;

	private List<TransactionPayEmployee> transactionPayEmployee = new ArrayList<TransactionPayEmployee>();

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {

	}

	@Override
	public boolean isPositiveTransaction() {
		return true;
	}

	@Override
	public boolean isDebitTransaction() {
		return true;
	}

	@Override
	public int getTransactionCategory() {
		return Transaction.CATEGORY_EMPLOYEE;
	}

	@Override
	public String toString() {
		return AccounterServerConstants.TYPE_PAY_EMPLOYEE;
	}

	@Override
	public Payee getInvolvedPayee() {
		return null;
	}

	@Override
	public void getEffects(ITransactionEffects e) {

		double unUsedAmount = 0.00D;
		for (TransactionPayEmployee bill : getTransactionPayEmployee()) {

			if (DecimalUtil.isGreaterThan((bill.payment - bill.amountDue), 0)) {
				unUsedAmount += bill.payment - bill.amountDue;
			} else {
				double amount = (bill.payment);
				if (bill.getPayRun() != null) {
					double amountToUpdate = amount
							* bill.getPayRun().currencyFactor;
					double diff = amount * currencyFactor - amountToUpdate;

					e.add(getCompany().getExchangeLossOrGainAccount(), -diff, 1);
					if (getEmployee() != null) {
						e.add(getEmployee().getAccount(), diff, 1);
					} else {
						List<Employee> employees = getEmployeeGroup()
								.getEmployees();
						for (Employee employee : employees) {
							e.add(employee.getAccount(), diff, 1);
						}
					}
				}
			}

		}

		if (getEmployee() != null) {
			e.add(getEmployee(), unUsedAmount - getTotal());
		} else {
			List<Employee> employees = getEmployeeGroup().getEmployees();
			for (Employee employee : employees) {
				e.add(employee, unUsedAmount - getTotal());
			}
		}

		double vendorPayment = getTotal();
		e.add(getPayAccount(), vendorPayment);
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public EmployeeGroup getEmployeeGroup() {
		return employeeGroup;
	}

	public void setEmployeeGroup(EmployeeGroup employeeGroup) {
		this.employeeGroup = employeeGroup;
	}

	public Account getPayAccount() {
		return payAccount;
	}

	public void setPayAccount(Account payAccount) {
		this.payAccount = payAccount;
	}

	public List<TransactionPayEmployee> getTransactionPayEmployee() {
		return transactionPayEmployee;
	}

	public void setTransactionPayEmployee(
			List<TransactionPayEmployee> transactionPayEmployee) {
		this.transactionPayEmployee = transactionPayEmployee;
	}

	@Override
	public boolean onDelete(Session session) throws CallbackException {
		if (!this.isVoid() && this.getSaveStatus() != STATUS_DRAFT) {
			doVoidEffect(session, this);
		}
		return super.onDelete(session);
	}

	private void doVoidEffect(Session session, PayEmployee payBill) {
		payBill.status = Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED;
		for (TransactionPayEmployee transactionPayBill : this.transactionPayEmployee) {
			transactionPayBill.setIsVoid(true);
			transactionPayBill.onUpdate(session);
			session.update(transactionPayBill);
		}

	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		PayEmployee payEmployee = (PayEmployee) clientObject;

		if (this.isVoidBefore && payEmployee.isVoidBefore) {
			throw new AccounterException(
					AccounterException.ERROR_NO_SUCH_OBJECT);
		}

		super.canEdit(clientObject, goingToBeEdit);
		return true;
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		if (this.isOnSaveProccessed)
			return true;
		this.isOnSaveProccessed = true;
		if (isDraft()) {
			super.onSave(session);
			return false;
		}

		if (this.getID() == 0l) {

			if (this.transactionPayEmployee != null) {
				for (TransactionPayEmployee tpb : this.transactionPayEmployee) {
					tpb.setPayEmployee(this);
				}
			}

			this.subTotal = this.total;

			super.onSave(session);

			if (isDraftOrTemplate()) {
				return false;
			}
		}
		return false;
	}

	@Override
	public void onEdit(Transaction clonedObject) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		PayBill payBill = (PayBill) clonedObject;

		if (this.transactionPayEmployee != null) {
			for (TransactionPayEmployee tpb : this.transactionPayEmployee) {
				tpb.setPayEmployee(this);
			}
		}

		if (isDraftOrTemplate()) {
			super.onEdit(payBill);
			return;
		}

		/**
		 * If present transaction is deleted or voided & the previous
		 * transaction is not voided then it will entered into the loop
		 */

		if (this.isVoid() && !payBill.isVoid()) {
			doVoidEffect(session, this);
		}

		super.onEdit(payBill);
	}
}