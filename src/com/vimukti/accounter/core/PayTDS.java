package com.vimukti.accounter.core;

import org.hibernate.classic.Lifecycle;
import org.json.JSONException;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;

public class PayTDS extends Transaction implements IAccounterServerCore,
		Lifecycle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3752025091243352402L;

	/**
	 * PayFrom {@link Account},
	 */
	@ReffereredObject
	Account payFrom;
	/**
	 * The Default TaxAgency Set for Transaction
	 * 
	 */
	@ReffereredObject
	TAXAgency taxAgency;

	
	
	@Override
	public boolean isPositiveTransaction() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDebitTransaction() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Account getEffectingAccount() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Payee getPayee() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getTransactionCategory() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Payee getInvolvedPayee() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		AccounterMessages messages = Global.get().messages();
		
		w.put(messages.type(), messages.payTDS()).gap().gap();
		w.put(messages.no(), this.number);
		w.put(messages.date(), this.transactionDate.toString()).gap().gap();
		w.put(messages.currency(), this.currencyFactor).gap().gap();
		w.put(messages.amount(), this.total).gap().gap();
		w.put(messages.paymentMethod(), this.paymentMethod).gap().gap();
		w.put(messages.memo(), this.memo).gap().gap();
	}
}
