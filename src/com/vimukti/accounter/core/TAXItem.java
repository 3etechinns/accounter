package com.vimukti.accounter.core;

import java.io.Serializable;

import org.hibernate.CallbackException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.vimukti.accounter.core.change.ChangeTracker;
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.AccounterCoreType;

/**
 * A VATItem is a sub class of VATItemGroup. It consists of VATagency, VAT
 * Return box and VAT rate. It is an entity which involves directly in the
 * transaction when the user want to make a vat liable transaction without any
 * involvement of items or accounts. The VATReturnBox reference is used to mark
 * this vat item to go into its corresponding vat box while filing the VAT.
 * 
 * @author Chandan
 */

public class TAXItem extends TAXItemGroup {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1558444105253843076L;

	/**
	 * VAT agency Every VAT Item consists of VAT Agency. Whenever this VATItem
	 * is used in any transactions the balance for the VAT agency which we have
	 * to pay will increases.
	 */
	TAXAgency taxAgency;

	/**
	 * The rate for assigned for this particular VAT Item.
	 */
	double taxRate;

	/**
	 * The return box where the amount goes when we use this VAT Item in any
	 * transaction.
	 */
	VATReturnBox vatReturnBox;

	/**
	 * @return the vatReturnBox
	 */
	public VATReturnBox getVatReturnBox() {
		return vatReturnBox;
	}

	/**
	 * @param vatReturnBox
	 *            the vatReturnBox to set
	 */
	public void setVatReturnBox(VATReturnBox vatReturnBox) {
		this.vatReturnBox = vatReturnBox;
	}

	/**
	 * @return the id
	 */
	public long getID() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setID(long id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the vatAgency
	 */
	public TAXAgency getTaxAgency() {
		return taxAgency;
	}

	/**
	 * @param vatAgency
	 *            the vatAgency to set
	 */
	public void setTaxAgency(TAXAgency taxAgency) {
		this.taxAgency = taxAgency;
	}

	/**
	 * @return the vatRate
	 */
	public double getTaxRate() {
		return taxRate;
	}

	/**
	 * @param vatRate
	 *            the vatRate to set
	 */
	public void setTaxRate(double taxRate) {
		this.taxRate = taxRate;
	}

	/**
	 * @return the isActive
	 */
	public boolean isActive() {
		return isActive;
	}

	/**
	 * @param isActive
	 *            the isActive to set
	 */
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	@Override
	public boolean onDelete(Session session) throws CallbackException {

		FinanceLogger.log("VATItem with name:" + this.getName()
				+ " has been deleted");

		AccounterCommand accounterCore = new AccounterCommand();
		accounterCore.setCommand(AccounterCommand.DELETION_SUCCESS);
		accounterCore.setID(this.id);
		accounterCore.setObjectType(AccounterCoreType.TAXITEM);
		ChangeTracker.put(accounterCore);

		return false;
	}

	@Override
	public void onLoad(Session arg0, Serializable arg1) {

	}

	@Override
	public boolean onSave(Session session) throws CallbackException {

		if (this.vatReturnBox != null
				&& (this.vatReturnBox.name
						.equals(AccounterConstants.UK_DOMESTIC_SALES)
						|| this.vatReturnBox.name
								.equals(AccounterConstants.UK_EC_SALES_GOODS)
						|| this.vatReturnBox.name
								.equals(AccounterConstants.UK_EC_SALES_SERVICES)
						|| this.vatReturnBox.name
								.equals(AccounterConstants.UK_NOT_REGISTERED_SALES)
						|| this.vatReturnBox.name
								.equals(AccounterConstants.IRELAND_DOMESTIC_SALES)
						|| this.vatReturnBox.name
								.equals(AccounterConstants.IRELAND_EXEMPT_SALES)
						|| this.vatReturnBox.name
								.equals(AccounterConstants.IRELAND_NOT_REGISTERED_SALES) || this.vatReturnBox.name
							.equals(AccounterConstants.IRELAND_EC_SALES_GOODS))) {
			this.isSalesType = true;

		} else {
			this.isSalesType = false;
		}

		if (Company.getCompany() != null
				&& Company.getCompany().getAccountingType() == Company.ACCOUNTING_TYPE_US) {
			this.isSalesType = true;
			TAXCode taxCode = new TAXCode((TAXItemGroup) this);
			session.saveOrUpdate(taxCode);
		}

		ChangeTracker.put(this);
		return false;
	}

	@Override
	public boolean onUpdate(Session session) throws CallbackException {

		if (this.vatReturnBox != null
				&& (this.vatReturnBox.name
						.equals(AccounterConstants.UK_DOMESTIC_SALES)
						|| this.vatReturnBox.name
								.equals(AccounterConstants.UK_EC_SALES_GOODS)
						|| this.vatReturnBox.name
								.equals(AccounterConstants.UK_EC_SALES_SERVICES)
						|| this.vatReturnBox.name
								.equals(AccounterConstants.UK_NOT_REGISTERED_SALES)
						|| this.vatReturnBox.name
								.equals(AccounterConstants.IRELAND_DOMESTIC_SALES)
						|| this.vatReturnBox.name
								.equals(AccounterConstants.IRELAND_EXEMPT_SALES)
						|| this.vatReturnBox.name
								.equals(AccounterConstants.IRELAND_NOT_REGISTERED_SALES) || this.vatReturnBox.name
							.equals(AccounterConstants.IRELAND_EC_SALES_GOODS))) {
			this.isSalesType = true;

		} else {
			this.isSalesType = false;
		}

		if (Company.getCompany() != null
				&& Company.getCompany().getAccountingType() == Company.ACCOUNTING_TYPE_US) {

			Query query = session
					.getNamedQuery(
							"getTaxACode.inTaxitem.by.id")
					.setParameter("id", this.id);
			TAXCode taxCode = (TAXCode) query.uniqueResult();
			if (taxCode != null) {

				taxCode.setName(this.getName());
				taxCode.setDescription(this.getDescription());
				taxCode.setActive(this.isActive());
				session.saveOrUpdate(taxCode);
			}
			this.isSalesType = true;
		}

		ChangeTracker.put(this);
		return false;
	}

}
