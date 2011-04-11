package com.vimukti.accounter.web.client.ui.core;

import com.vimukti.accounter.web.client.ui.customers.SalesOrderAction;
import com.vimukti.accounter.web.client.ui.customers.SalesOrderListAction;
import com.vimukti.accounter.web.client.ui.reports.SalesOpenOrderAction;

public class SalesOrderActionFactory extends AbstractActionFactory {

	public static SalesOrderAction getSalesOrderAction() {
		return new SalesOrderAction("Sales Order",
				"/images/Sales-order.png");
	}

	public static SalesOrderListAction getSalesOrderListAction() {
		return new SalesOrderListAction("SalesOrder List",
				"/images/Sales-order-list.png");
	}

	public static SalesOpenOrderAction getSalesOpenOrderAction() {
		return new SalesOpenOrderAction("SalesOrder Report",
				"/images/icons/report/reports.png");
	}
}
