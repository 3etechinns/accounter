package com.vimukti.accounter.migration;

import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.CustomerRefund;

public class CustomerRefundMigrator extends TransactionMigrator<CustomerRefund> {
	@Override
	public JSONObject migrate(CustomerRefund obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObj = super.migrate(obj, context);

		JSONObject jsonAddr = new JSONObject();
		Address addr = obj.getAddress();
		if (addr != null) {
			jsonAddr.put("street", addr.getStreet());
			jsonAddr.put("city", addr.getCity());
			jsonAddr.put("stateOrProvince", addr.getStateOrProvinence());
			jsonAddr.put("zipOrPostalCode", addr.getZipOrPostalCode());
			jsonAddr.put("country", addr.getCountryOrRegion());
			jsonObj.put("address", jsonAddr);
		}
		jsonObj.put("amount", obj.getTotal());
		// PaymentableTransaction
		String paymentMethod = obj.getPaymentMethod();
		if (paymentMethod != null) {
			jsonObj.put("paymentMethod", PicklistUtilMigrator
					.getPaymentMethodIdentifier(paymentMethod));
		}

		Long checkNumber = 0L;
		try {
			checkNumber = Long.parseLong(obj.getCheckNumber());
		} catch (Exception e) {
			// Nothing to do
		}
		jsonObj.put("checkNumber", checkNumber);
		Customer payTo = obj.getPayTo();
		if (payTo != null) {
			jsonObj.put("payee", context.get("Customer", payTo.getID()));
		}
		Account payFrom = obj.getPayFrom();
		if (payFrom != null) {
			JSONObject account = new JSONObject();
			account.put("name", payFrom.getName());
			jsonObj.put("payFrom", account);
		}
		jsonObj.put("toBePrinted", obj.getIsToBePrinted());
		jsonObj.put("paymentStatus", PicklistUtilMigrator
				.getPaymentStatusIdentifier(obj.getStatus()));
		jsonObj.put("date", obj.getDate().getAsDateObject().getTime());
		return jsonObj;
	}
}
