package com.vimukti.accounter.web.client.externalization;

import com.google.gwt.i18n.client.Constants;

public interface AccounterErrors extends Constants {

	String numberConflict();

	String nameConflict();

	String transactionConflict();

	String permissionDenied();

	String internal();

	String illegalArgument();

	String noSuchObject();

	String depositedFromUndepositedFunds();

	String cantEdit();

	String cantVoid();

	String receivePaymentDiscountUsed();

	String objectInUse();

	String taxGroupInUse();

	String vatItemInUse();

	String objectModified();

	String billPaidSoYouCantEdit();

	String billPaidSoYouCantVoid();

	String transactionReconcilied();

	String usedinInvoiceSoYoucantEdit();

}
