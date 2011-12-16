package com.vimukti.accounter.core;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.vimukti.accounter.main.ServerConfiguration;

import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.images.ClassPathImageProvider;
import fr.opensagres.xdocreport.document.images.IImageProvider;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

public class CreditNotePdfGeneration {

	private CustomerCreditMemo memo;
	private Company company;
	private BrandingTheme brandingTheme;
	private int maxDecimalPoints;

	public CreditNotePdfGeneration(CustomerCreditMemo memo, Company company,
			BrandingTheme brandingTheme) {
		this.memo = memo;
		this.company = company;
		this.brandingTheme = brandingTheme;
		this.maxDecimalPoints = getMaxDecimals(memo);

	}

	public IContext assignValues(IContext context, IXDocReport report) {

		try {

			IImageProvider logo = new ClassPathImageProvider(
					InvoicePdfGeneration.class, getImage());
			IImageProvider footerImg = new ClassPathImageProvider(
					InvoicePdfGeneration.class, "templetes" + File.separator
							+ "footer-print-img.jpg");

			FieldsMetadata imgMetaData = new FieldsMetadata();
			imgMetaData.addFieldAsImage("logo");
			imgMetaData.addFieldAsImage("companyImg");
			report.setFieldsMetadata(imgMetaData);

			// assigning the original values
			DummyCredit i = new DummyCredit();

			String title = brandingTheme.getCreditMemoTitle() == null ? "CreditNote"
					: brandingTheme.getCreditMemoTitle().toString();

			i.setTitle(title);
			i.setCustomerNameNBillAddress(getBillingAddress());
			i.setCreditNoteNumber(memo.getNumber());
			i.setCreditNoteDate(memo.getDate().toString());

			// for primary curreny
			Currency currency = memo.getCustomer().getCurrency();
			if (currency != null)
				if (currency.getFormalName().trim().length() > 0) {
					i.setCurrency(currency.getFormalName().trim());
				}

			// for transactions

			FieldsMetadata headersMetaData = new FieldsMetadata();
			headersMetaData.addFieldAsList("item.name");
			headersMetaData.addFieldAsList("item.description");
			headersMetaData.addFieldAsList("item.quantity");
			headersMetaData.addFieldAsList("item.itemUnitPrice");
			headersMetaData.addFieldAsList("item.discount");
			headersMetaData.addFieldAsList("item.itemTotalPrice");
			headersMetaData.addFieldAsList("item.itemVatRate");
			headersMetaData.addFieldAsList("item.itemVatAmount");
			report.setFieldsMetadata(headersMetaData);
			List<ItemList> itemList = new ArrayList<ItemList>();
			List<TransactionItem> transactionItems = memo.getTransactionItems();

			double currencyFactor = memo.getCurrencyFactor();

			for (Iterator iterator = transactionItems.iterator(); iterator
					.hasNext();) {

				TransactionItem item = (TransactionItem) iterator.next();

				String description = forNullValue(item.getDescription());
				description = description.replaceAll("\n", "<br/>");

				String qty = "";
				if (item.getQuantity() != null) {
					qty = String.valueOf(item.getQuantity().getValue());
				}
				String unitPrice = Utility.decimalConversation(item
						.getUnitPrice() / currencyFactor);
				String totalPrice = Utility.decimalConversation(item
						.getLineTotal() / currencyFactor);

				Double vaTfraction = item.getVATfraction();
				String vatAmount = " ";
				if (vaTfraction != null) {
					vatAmount = Utility.decimalConversation(item
							.getVATfraction() / currencyFactor);
				}
				String name = item.getItem() != null ? item.getItem().getName()
						: item.getAccount().getName();

				String discount = Utility.decimalConversation(item
						.getDiscount());

				TAXCode taxCode = item.getTaxCode();
				String vatRate = " ";
				if (taxCode != null) {
					vatRate = item.getTaxCode().getName();
				}
				itemList.add(new ItemList(name, description, qty, unitPrice,
						discount, totalPrice, vatRate, vatAmount));
			}

			context.put("item", itemList);
			String total = Utility.decimalConversation(memo.getTotal());

			i.setTotal(total);

			String subtotal = Utility.decimalConversation(memo.getNetAmount()
					/ currencyFactor);
			i.setNetAmount(subtotal);

			i.setMemo(memo.getMemo());
			String termsNCondn = forNullValue(brandingTheme
					.getTerms_And_Payment_Advice());

			if (termsNCondn.equalsIgnoreCase("(None Added)")) {
				termsNCondn = " ";
			}
			i.setAdviceTerms(termsNCondn);

			String paypalEmail = forNullValue(brandingTheme.getPayPalEmailID());
			if (paypalEmail.equalsIgnoreCase("(None Added)")) {
				paypalEmail = " ";
			}
			i.setEmail(paypalEmail);

			i.setVatCode(getVatNumber());
			i.setSortCode(getSortCode());
			i.setBankAccountNo(getBankAccountNumbere());
			i.setRegistrationAddress(getRegistrationAddress());

			context.put("logo", logo);
			context.put("credit", i);
			context.put("companyImg", footerImg);

			return context;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private String getRegistrationAddress() {
		String regestrationAddress = "";
		Address reg = company.getRegisteredAddress();

		if (reg != null)
			regestrationAddress = ("Registered Address: " + reg.getAddress1()
					+ forUnusedAddress(reg.getStreet(), true)
					+ forUnusedAddress(reg.getCity(), true)
					+ forUnusedAddress(reg.getStateOrProvinence(), true)
					+ forUnusedAddress(reg.getZipOrPostalCode(), true)
					+ forUnusedAddress(reg.getCountryOrRegion(), true) + ".");

		regestrationAddress = (company.getTradingName() + " "
				+ regestrationAddress + ((company.getRegistrationNumber() != null && !company
				.getRegistrationNumber().equals("")) ? "\n Company Registration No: "
				+ company.getRegistrationNumber()
				: ""));

		return regestrationAddress;

	}

	private String getSortCode() {
		String sortCode = " Sort Code: " + forNullValue(company.getSortCode());
		if (company.getSortCode() != null) {
			if (company.getSortCode().length() > 0) {
				return sortCode;
			}
		}
		return "";
	}

	private String getBankAccountNumbere() {
		String bankAccountNum = "Bank Account No: "
				+ forNullValue(company.getBankAccountNo());
		if (company.getBankAccountNo() != null) {
			if (company.getBankAccountNo().length() > 0) {
				return bankAccountNum;
			}
		}
		return "";
	}

	private String getVatNumber() {
		String vatString = "Tax No: "
				+ forNullValue(company.getPreferences()
						.getVATregistrationNumber());
		if (company.getPreferences().getVATregistrationNumber().length() > 0) {
			if (brandingTheme.isShowTaxNumber()) {
				return vatString;
			}
		}
		return "";
	}

	private int getMaxDecimals(CustomerCreditMemo memo) {
		String qty;
		String max;
		int temp = 0;
		for (TransactionItem item : memo.getTransactionItems()) {
			qty = String.valueOf(item.getQuantity());
			max = qty.substring(qty.indexOf(".") + 1);
			if (!max.equals("0")) {
				if (temp < max.length()) {
					temp = max.length();
				}
			}

		}
		return temp;
	}

	public String getImage() {
		StringBuffer original = new StringBuffer();

		original.append(ServerConfiguration.getAttachmentsDir() + "/"
				+ company.getId() + "/" + brandingTheme.getFileName());

		return original.toString();

	}

	private String getBillingAddress() {
		// To get the selected contact name form Invoice
		String cname = "";
		String phone = "";
		boolean hasPhone = false;
		Contact selectedContact = memo.getContact();
		if (selectedContact != null) {
			cname = selectedContact.getName().trim();
			if (selectedContact.getBusinessPhone().trim().length() > 0)
				phone = selectedContact.getBusinessPhone();
			if (phone.trim().length() > 0) {
				// If phone variable has value, then only we need to display
				// the text 'phone'
				hasPhone = true;
			}
		}

		// setting billing address
		Address bill = memo.getBillingAddress();
		String customerName = forUnusedAddress(memo.getCustomer().getName(),
				false);
		StringBuffer billAddress = new StringBuffer();
		if (bill != null) {
			billAddress = billAddress.append(forUnusedAddress(cname, false)
					+ customerName
					+ forUnusedAddress(bill.getAddress1(), false)
					+ forUnusedAddress(bill.getStreet(), false)
					+ forUnusedAddress(bill.getCity(), false)
					+ forUnusedAddress(bill.getStateOrProvinence(), false)
					+ forUnusedAddress(bill.getZipOrPostalCode(), false)
					+ forUnusedAddress(bill.getCountryOrRegion(), false));
			if (hasPhone) {
				billAddress.append(forUnusedAddress("Phone : " + phone, false));
			}

			String billAddres = billAddress.toString();

			if (billAddres.trim().length() > 0) {
				return billAddres;
			}
		} else {
			// If there is no Bill Address, then display only customer and
			// contact name
			StringBuffer contact = new StringBuffer();
			contact = contact.append(forUnusedAddress(cname, false)
					+ customerName);
			return contact.toString();
		}
		return "";
	}

	public String forUnusedAddress(String add, boolean isFooter) {
		if (isFooter) {
			if (add != null && !add.equals(""))
				return ", " + add;
		} else {
			if (add != null && !add.equals(""))
				return add + "\n";
		}
		return "";
	}

	public String forNullValue(String value) {
		return value != null ? value : "";
	}

	private String getLogoAlignment() {
		String logoAlignment = null;
		if (brandingTheme.getLogoAlignmentType() == 1) {
			logoAlignment = "left";
		} else {
			logoAlignment = "right";
		}
		return logoAlignment;
	}

	private String getDecimalsUsingMaxDecimals(double quantity, String amount,
			int maxDecimalPoint) {
		String qty = "";
		String max;
		if (maxDecimalPoint != 0) {
			if (amount == null)
				qty = String.valueOf(quantity);
			else
				qty = amount;
			max = qty.substring(qty.indexOf(".") + 1);
			if (maxDecimalPoint > max.length()) {
				for (int i = max.length(); maxDecimalPoint != i; i++) {
					qty = qty + "0";
				}
			}
		} else {
			qty = String.valueOf((long) quantity);
		}

		String temp = qty.contains(".") ? qty.replace(".", "-").split("-")[0]
				: qty;
		return insertCommas(temp)
				+ (qty.contains(".") ? "."
						+ qty.replace(".", "-").split("-")[1] : "");
	}

	public String forZeroAmounts(String amount) {
		String[] amt = amount.replace(".", "-").split("-");
		if (amt[0].equals("0")) {
			return "";
		}
		return amount;
	}

	private static String insertCommas(String str) {

		if (str.length() < 4) {
			return str;
		}
		return insertCommas(str.substring(0, str.length() - 3)) + ","
				+ str.substring(str.length() - 3, str.length());
	}

	private String largeAmountConversation(double amount) {
		String amt = Utility.decimalConversation(amount);
		amt = getDecimalsUsingMaxDecimals(0.0, amt, 2);
		return (amt);
	}

	public class DummyCredit {

		private String title;
		private String creditNoteNumber;
		private String creditNoteDate;
		private String creditNoteCustomerNumber;
		private String currency;
		private String customerNameNBillAddress;
		private String total;
		private String netAmount;
		private String memo;
		private String adviceTerms;
		private String email;
		private String vatCode;
		private String sortCode;
		private String BankAccountNo;
		private String registrationAddress;

		public String getCreditNoteNumber() {
			return creditNoteNumber;
		}

		public void setCreditNoteNumber(String creditNoteNumber) {
			this.creditNoteNumber = creditNoteNumber;
		}

		public String getCreditNoteDate() {
			return creditNoteDate;
		}

		public void setCreditNoteDate(String creditNoteDate) {
			this.creditNoteDate = creditNoteDate;
		}

		public String getCreditNoteCustomerNumber() {
			return creditNoteCustomerNumber;
		}

		public void setCreditNoteCustomerNumber(String creditNoteCustomerNumber) {
			this.creditNoteCustomerNumber = creditNoteCustomerNumber;
		}

		public String getCurrency() {
			return currency;
		}

		public void setCurrency(String currency) {
			this.currency = currency;
		}

		public String getCustomerNameNBillAddress() {
			return customerNameNBillAddress;
		}

		public void setCustomerNameNBillAddress(String customerNameNBillAddress) {
			this.customerNameNBillAddress = customerNameNBillAddress;
		}

		public String getTotal() {
			return total;
		}

		public void setTotal(String total) {
			this.total = total;
		}

		public String getNetAmount() {
			return netAmount;
		}

		public void setNetAmount(String netAmount) {
			this.netAmount = netAmount;
		}

		public String getMemo() {
			return memo;
		}

		public void setMemo(String memo) {
			this.memo = memo;
		}

		public String getAdviceTerms() {
			return adviceTerms;
		}

		public void setAdviceTerms(String adviceTerms) {
			this.adviceTerms = adviceTerms;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getVatCode() {
			return vatCode;
		}

		public void setVatCode(String vatCode) {
			this.vatCode = vatCode;
		}

		public String getSortCode() {
			return sortCode;
		}

		public void setSortCode(String sortCode) {
			this.sortCode = sortCode;
		}

		public String getBankAccountNo() {
			return BankAccountNo;
		}

		public void setBankAccountNo(String bankAccountNo) {
			BankAccountNo = bankAccountNo;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getRegistrationAddress() {
			return registrationAddress;
		}

		public void setRegistrationAddress(String registrationAddress) {
			this.registrationAddress = registrationAddress;
		}

	}

	public class ItemList {
		private String name;
		private String description;
		private String quantity;
		private String itemUnitPrice;
		private String discount;
		private String itemTotalPrice;
		private String itemVatRate;
		private String itemVatAmount;

		ItemList(String name, String description, String quantity,
				String itemUnitPrice, String discount, String itemTotalPrice,
				String itemVatRate, String itemVatAmount) {
			this.name = name;
			this.description = description;
			this.quantity = quantity;
			this.itemUnitPrice = itemUnitPrice;
			this.discount = discount;
			this.itemTotalPrice = itemTotalPrice;
			this.itemVatRate = itemVatRate;
			this.itemVatAmount = itemVatAmount;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getQuantity() {
			return quantity;
		}

		public void setQuantity(String quantity) {
			this.quantity = quantity;
		}

		public String getItemUnitPrice() {
			return itemUnitPrice;
		}

		public void setItemUnitPrice(String itemUnitPrice) {
			this.itemUnitPrice = itemUnitPrice;
		}

		public String getDiscount() {
			return discount;
		}

		public void setDiscount(String discount) {
			this.discount = discount;
		}

		public String getItemTotalPrice() {
			return itemTotalPrice;
		}

		public void setItemTotalPrice(String itemTotalPrice) {
			this.itemTotalPrice = itemTotalPrice;
		}

		public String getItemVatRate() {
			return itemVatRate;
		}

		public void setItemVatRate(String itemVatRate) {
			this.itemVatRate = itemVatRate;
		}

		public String getItemVatAmount() {
			return itemVatAmount;
		}

		public void setItemVatAmount(String itemVatAmount) {
			this.itemVatAmount = itemVatAmount;
		}

	}
}