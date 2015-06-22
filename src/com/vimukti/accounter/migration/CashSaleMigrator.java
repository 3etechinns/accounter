package com.vimukti.accounter.migration;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.CashSales;
import com.vimukti.accounter.core.Estimate;
import com.vimukti.accounter.core.ShippingMethod;
import com.vimukti.accounter.core.ShippingTerms;

public class CashSaleMigrator extends TransactionMigrator<CashSales> {
	@Override
	public JSONObject migrate(CashSales obj, MigratorContext context)
			throws JSONException {
		JSONObject jsonObject = super.migrate(obj, context);
		Account depositIn = obj.getDepositIn();
		jsonObject.put("depositIn", context.get("Account", depositIn.getID()));
		jsonObject.put("phone", obj.getPhone());

		Address billingAddress = obj.getBillingAddress();
		if (billingAddress != null) {
			JSONObject jsonAddress = new JSONObject();
			jsonAddress.put("street", billingAddress.getStreet());
			jsonAddress.put("city", billingAddress.getCity());
			jsonAddress.put("stateOrProvince",
					billingAddress.getStateOrProvinence());
			jsonAddress.put("zipOrPostalCode",
					billingAddress.getZipOrPostalCode());
			jsonAddress.put("country", billingAddress.getCountryOrRegion());
			jsonObject.put("billTo", jsonAddress);
		}
		Address shipingTo = obj.getShippingAdress();
		if (shipingTo != null) {
			JSONObject jsonShipTo = new JSONObject();
			jsonShipTo.put("street", shipingTo.getStreet());
			jsonShipTo.put("city", shipingTo.getCity());
			jsonShipTo.put("stateOrProvince", shipingTo.getStateOrProvinence());
			jsonShipTo.put("zipOrPostalCode", shipingTo.getZipOrPostalCode());
			jsonShipTo.put("country", shipingTo.getCountryOrRegion());
			jsonShipTo.put("shipTo", jsonShipTo);
		}

		List<Estimate> salesOrders = obj.getSalesOrders();
		JSONArray array = new JSONArray();
		for (Estimate quot : salesOrders) {
			JSONObject jsonObject2 = new JSONObject();
			jsonObject2.put("salesOrder",
					context.get("SalesOrder", quot.getID()));
			array.put(jsonObject2);
		}
		jsonObject.put("salesOrders", array);
		ShippingTerms shippingTerm = obj.getShippingTerm();
		if (shippingTerm != null) {
			jsonObject.put("shippingTerm",
					context.get("ShippingTerm", shippingTerm.getID()));
		}
		ShippingMethod shippingMethod = obj.getShippingMethod();
		if (shippingMethod != null) {
			jsonObject.put("shippingMethod",
					context.get("ShippingMethod", shippingMethod.getID()));
		}
		jsonObject.put("deliveryDate", obj.getDeliverydate().getAsDateObject());
		// super (CustomerTransaction field)
		if (obj.getContact() != null) {
			jsonObject.put("contact",
					context.get("Contact", obj.getContact().getID()));
		}
		jsonObject.put("payee",
				context.get("Customer", obj.getCustomer().getID()));
		jsonObject.put(
				"paymentMethod",
				context.getPickListContext().get("PaymentMethod",
						obj.getPaymentMethod()));
		jsonObject.put("chequeNumber", obj.getCheckNumber());
		return jsonObject;
	}
}