package com.vimukti.accounter.servlets;

import java.awt.Dimension;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.zefer.pd4ml.PD4Constants;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;
import com.vimukti.accounter.core.BrandingTheme;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.CreditNotePDFTemplete;
import com.vimukti.accounter.core.CreditNotePdfGeneration;
import com.vimukti.accounter.core.CustomerCreditMemo;
import com.vimukti.accounter.core.Estimate;
import com.vimukti.accounter.core.ITemplate;
import com.vimukti.accounter.core.Invoice;
import com.vimukti.accounter.core.InvoicePDFTemplete;
import com.vimukti.accounter.core.InvoicePdfGeneration;
import com.vimukti.accounter.core.PrintTemplete;
import com.vimukti.accounter.core.QuotePdfGeneration;
import com.vimukti.accounter.core.QuotePdfTemplate;
import com.vimukti.accounter.core.ReportTemplate;
import com.vimukti.accounter.core.ReportsGenerator;
import com.vimukti.accounter.core.TemplateBuilder;
import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.main.CompanyPreferenceThreadLocal;
import com.vimukti.accounter.main.ServerConfiguration;
import com.vimukti.accounter.utils.Converter;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.server.FinanceTool;

import fr.opensagres.xdocreport.converter.ConverterTypeTo;
import fr.opensagres.xdocreport.converter.ConverterTypeVia;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;

public class GeneratePDFservlet extends BaseServlet {

	public final static String CLASSIC = "Classic";
	public final static String PROFESSIONAL = "Professional";
	public final static String PLAIN = "Plain";
	public final static String MODERN = "Modern";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String PAGE_BREAK = "<pd4ml:page.break>";

	@Override
	protected void service(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {
		super.service(arg0, arg1);
	}

	public void generateHtmlPDF(HttpServletRequest request,
			HttpServletResponse response, String companyName) {

		ITemplate template = null;
		Converter converter = null;
		BrandingTheme brandingTheme = null;
		Invoice invoice = null;
		String fileName = "";
		int transactionType;
		PrintTemplete printTemplete = null;
		ServletOutputStream sos = null;
		boolean pageBreak = false;
		try {

			String footerImg = ("war" + File.separator + "images"
					+ File.separator + "footer-print-img.jpg");

			String style = ("war" + File.separator + "css" + File.separator + "FinancePrint.css");

			Long companyID = (Long) request.getSession().getAttribute(
					COMPANY_ID);
			Session session = HibernateUtil.getCurrentSession();
			StringBuilder outPutString = new StringBuilder();
			transactionType = 0;
			try {
				FinanceTool financetool = new FinanceTool();
				TemplateBuilder.setCmpName(companyName);

				Company company = financetool.getCompany(companyID);

				CompanyPreferenceThreadLocal.set(financetool
						.getCompanyManager().getClientCompanyPreferences(
								company));

				String brandingThemeId = request
						.getParameter("brandingThemeId");
				if (brandingThemeId != null) {
					brandingTheme = (BrandingTheme) financetool.getManager()
							.getServerObjectForid(
									AccounterCoreType.BRANDINGTHEME,
									Long.parseLong(brandingThemeId));

					converter = new Converter(
							getPageSizeType(brandingTheme.getPageSizeType()));
				}
				String objectId = request.getParameter("objectId");
				if (request.getParameter("multipleIds") != null) {
					objectId = request.getParameter("multipleIds");
				}

				String[] ids = null;
				if (objectId != null) {
					ids = objectId.split(",");
					System.err.println("length" + ids.length);
					if (ids.length > 1) {
						pageBreak = true;

					}
				}

				// this is used to print multiple pdf documents at a time
				if (objectId != null) {

					transactionType = Integer.parseInt(request
							.getParameter("type"));

					for (int i = 0; i < ids.length; i++) {

						if (transactionType == Transaction.TYPE_INVOICE) {
							invoice = (Invoice) financetool.getManager()
									.getServerObjectForid(
											AccounterCoreType.INVOICE,
											Long.parseLong(ids[i]));

							printTemplete = getInvoiceReportTemplete(invoice,
									brandingTheme, company);

							fileName = printTemplete.getFileName();

							outPutString = outPutString.append(printTemplete
									.getPdfData());

						}
						if (transactionType == Transaction.TYPE_CUSTOMER_CREDIT_MEMO) {
							CustomerCreditMemo memo = (CustomerCreditMemo) financetool
									.getManager()
									.getServerObjectForid(
											AccounterCoreType.CUSTOMERCREDITMEMO,
											Long.parseLong(ids[i]));

							printTemplete = getCreditReportTemplete(memo,
									brandingTheme, company);

							fileName = printTemplete.getFileName();

							outPutString = outPutString.append(printTemplete
									.getPdfData());

						}

						if (transactionType == Transaction.TYPE_ESTIMATE) {
							Estimate estimate = (Estimate) financetool
									.getManager().getServerObjectForid(
											AccounterCoreType.ESTIMATE,
											Long.parseLong(ids[i]));

							printTemplete = getQuoteReportTemplete(estimate,
									brandingTheme, company);

							fileName = printTemplete.getFileName();

							outPutString = outPutString.append(printTemplete
									.getPdfData());

						}

						if (pageBreak) {
							outPutString.append(PAGE_BREAK);
						}
					}

				}

				// for Reports
				else {
					transactionType = 0;
					converter = new Converter();
					template = getReportTemplate(company, request, financetool,
							footerImg, style);
					fileName = template.getFileName();
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				session.close();
			}

			response.setContentType("application/pdf");
			response.setHeader("Content-disposition", "attachment; filename="
					+ fileName.replace(" ", "") + ".pdf");
			sos = response.getOutputStream();

			switch (transactionType) {
			// for invoice
			case Transaction.TYPE_INVOICE:
			case Transaction.TYPE_CUSTOMER_CREDIT_MEMO:
			case Transaction.TYPE_ESTIMATE:

				String output = outPutString.toString().replaceAll("</html>",
						"");
				output = output.toString().replaceAll("<html>", "");

				output = "<html>" + output + "</html>";
				java.io.InputStream inputStream = new ByteArrayInputStream(
						output.getBytes());
				InputStreamReader reader = new InputStreamReader(inputStream);
				converter.generatePdfDocuments(printTemplete, sos, reader);
				break;

			default:
				// for generating pdf document for reports
				converter.generatePdfReports(template, sos);
				break;
			}

			System.err.println("Pdf created");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
	}

	public void generateCustom2PDF(HttpServletRequest request,
			HttpServletResponse response, String companyName) {
		HashMap map = null;
		ITemplate template = null;
		Converter converter = null;
		String fileName = "";
		int transactionType;
		PrintTemplete printTemplete = null;
		ServletOutputStream sos = null;
		boolean isMultipleId = false;
		List<String> fileNames = new ArrayList<String>();
		try {

			String footerImg = ("war" + File.separator + "images"
					+ File.separator + "footer-print-img.jpg");

			String style = ("war" + File.separator + "css" + File.separator + "FinancePrint.css");

			Long companyID = (Long) request.getSession().getAttribute(
					COMPANY_ID);
			StringBuilder outPutString = new StringBuilder();
			transactionType = 0;
			try {
				FinanceTool financetool = new FinanceTool();
				TemplateBuilder.setCmpName(companyName);

				Company company = financetool.getCompany(companyID);

				CompanyPreferenceThreadLocal.set(financetool
						.getCompanyManager().getClientCompanyPreferences(
								company));

				String objectId = request.getParameter("objectId");
				if (request.getParameter("multipleIds") != null) {
					isMultipleId = true;
					objectId = request.getParameter("multipleIds");
				}

				String[] ids = null;
				if (objectId != null) {
					ids = objectId.split(",");
				}
				String brandingThemeId = request
						.getParameter("brandingThemeId");
				BrandingTheme brandingTheme = null;
				if (brandingThemeId != null) {
					brandingTheme = (BrandingTheme) financetool.getManager()
							.getServerObjectForid(
									AccounterCoreType.BRANDINGTHEME,
									Long.parseLong(brandingThemeId));

					converter = new Converter(
							getPageSizeType(brandingTheme.getPageSizeType()));

				}

				// this is used to print multiple pdf documents at a time
				if (objectId != null) {
					transactionType = Integer.parseInt(request
							.getParameter("type"));

					for (int i = 0; i < ids.length; i++) {

						if (transactionType == Transaction.TYPE_INVOICE) {
							Invoice invoice = (Invoice) financetool
									.getManager().getServerObjectForid(
											AccounterCoreType.INVOICE,
											Long.parseLong(ids[i]));

							fileName = "Invoice_" + invoice.getNumber();

							map = Odt2PdfGeneration(invoice, company,
									brandingTheme, isMultipleId, fileNames);

						}
						if (transactionType == Transaction.TYPE_CUSTOMER_CREDIT_MEMO) {
							CustomerCreditMemo memo = (CustomerCreditMemo) financetool
									.getManager()
									.getServerObjectForid(
											AccounterCoreType.CUSTOMERCREDITMEMO,
											Long.parseLong(ids[i]));
							fileName = "CreditNote" + memo.getNumber();
							map = Odt2PdfGeneration(memo, company,
									brandingTheme, isMultipleId, fileNames);
						}
						if (transactionType == Transaction.TYPE_ESTIMATE) {
							Estimate estimate = (Estimate) financetool
									.getManager().getServerObjectForid(
											AccounterCoreType.ESTIMATE,
											Long.parseLong(ids[i]));
							fileName = "Quote" + estimate.getNumber();
							map = Odt2PdfGeneration(estimate, company,
									brandingTheme, isMultipleId, fileNames);
						}

					}

				}

				// for Reports
				else {
					transactionType = 0;
					converter = new Converter();
					template = getReportTemplate(company, request, financetool,
							footerImg, style);
					fileName = template.getFileName();
				}

				response.setContentType("application/pdf");
				response.setHeader("Content-disposition",
						"attachment; filename=" + fileName.replace(" ", "")
								+ ".pdf");
				sos = response.getOutputStream();

				switch (transactionType) {
				// for invoice or CreditNote
				case Transaction.TYPE_INVOICE:
				case Transaction.TYPE_CUSTOMER_CREDIT_MEMO:
				case Transaction.TYPE_ESTIMATE:

					if (isMultipleId) {// for merging multiple custom pdf
										// documents
						mergePDFDocuments(fileNames, sos, true);
					} else {
						IContext context = (IContext) map.get("context");
						IXDocReport report = (IXDocReport) map.get("report");

						Options options = Options.getTo(ConverterTypeTo.PDF)
								.via(ConverterTypeVia.ITEXT);
						report.convert(context, options, sos);
					}
					break;

				default:
					// for generating pdf document for reports
					converter.generatePdfReports(template, sos);
					break;
				}

				System.err.println("Pdf created");

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} finally {

		}
	}

	private ITemplate getReportTemplate(Company company,
			HttpServletRequest request, FinanceTool financeTool,
			String footerImg, String style) throws IOException {

		long startDate = Long.parseLong(request.getParameter("startDate"));
		int reportType = Integer.parseInt(request.getParameter("reportType"));
		long endDate = Long.parseLong(request.getParameter("endDate"));
		String dateRangeHtml = request.getParameter("dateRangeHtml");
		String navigatedName = request.getParameter("navigatedName");
		String status = request.getParameter("status");
		ReportsGenerator generator;

		if (status == null) {
			generator = new ReportsGenerator(reportType, startDate, endDate,
					navigatedName, ReportsGenerator.GENERATIONTYPEPDF, company);
		} else {
			generator = new ReportsGenerator(reportType, startDate, endDate,
					navigatedName, ReportsGenerator.GENERATIONTYPEPDF, status,
					company);
		}

		String gridTemplate = generator.generate(financeTool,
				ReportsGenerator.GENERATIONTYPEPDF);

		ReportTemplate template = new ReportTemplate(company, reportType,
				new String[] { gridTemplate, footerImg, style, dateRangeHtml });
		return template;
	}

	private Dimension getPageSizeType(int pageSizeType) {
		switch (pageSizeType) {
		case 2:
			return PD4Constants.LETTER;
		default:
			return PD4Constants.A4;
		}
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String companyName = getCompanyName(request);
		BrandingTheme brandingTheme = null;
		if (companyName == null)
			return;

		String brandingThemeId = request.getParameter("brandingThemeId");
		FinanceTool financetool = new FinanceTool();
		if (brandingThemeId != null) {
			// If branding theme is valid, then check for isCustomFile.If
			// isCustomFile is true , then call generateODT2PDF and if false,
			// then call generateHtmlPDF
			brandingTheme = (BrandingTheme) financetool.getManager()
					.getServerObjectForid(AccounterCoreType.BRANDINGTHEME,
							Long.parseLong(brandingThemeId));

			if (brandingTheme.isCustomFile()) {
				// for genearting reports using XdocReport
				generateCustom2PDF(request, response, companyName);
			} else {
				// for generating reports using html templates
				generateHtmlPDF(request, response, companyName);
			}

		} else {
			// for all kinds of other reports
			generateCustom2PDF(request, response, companyName);
		}

	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	private PrintTemplete getInvoiceReportTemplete(Invoice invoice,
			BrandingTheme theme, Company company) {

		try {
			String invStyle = theme.getInvoiceTempleteName();

			if (invStyle.contains(CLASSIC)) {
				return new InvoicePDFTemplete(invoice, theme, company,
						"ClassicInvoice");

			} else if (invStyle.contains(PLAIN)) {
				return new InvoicePDFTemplete(invoice, theme, company,
						"PlainInvoice");
			} else if (invStyle.contains(PROFESSIONAL)) {
				return new InvoicePDFTemplete(invoice, theme, company,
						"ProfessionalInvoice");

			} else if (invStyle.contains(MODERN)) {
				return new InvoicePDFTemplete(invoice, theme, company,
						"ModernInvoice");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	private PrintTemplete getCreditReportTemplete(CustomerCreditMemo memo,
			BrandingTheme theme, Company company) {

		String invStyle = theme.getCreditNoteTempleteName();

		if (invStyle.contains(CLASSIC)) {
			return new CreditNotePDFTemplete(memo, theme, company,
					"ClassicCredit");

		} else if (invStyle.contains(PLAIN)) {
			return new CreditNotePDFTemplete(memo, theme, company,
					"PlainCredit");
		} else if (invStyle.contains(PROFESSIONAL)) {
			return new CreditNotePDFTemplete(memo, theme, company,
					"ProfessionalCredit");
		} else if (invStyle.contains(MODERN)) {
			return new CreditNotePDFTemplete(memo, theme, company,
					"ModernCredit");
		}
		return null;
	}

	private PrintTemplete getQuoteReportTemplete(Estimate estimate,
			BrandingTheme theme, Company company) {

		String invStyle = theme.getQuoteTemplateName();

		if (invStyle.contains(CLASSIC)) {
			return new QuotePdfTemplate(estimate, theme, company,
					"ClassicQuote");
		} else if (invStyle.contains(PLAIN)) {
			return new QuotePdfTemplate(estimate, theme, company, "PlainQuote");
		} else if (invStyle.contains(PROFESSIONAL)) {
			return new QuotePdfTemplate(estimate, theme, company,
					"ProfessionalQuote");
		} else if (invStyle.contains(MODERN)) {
			return new QuotePdfTemplate(estimate, theme, company, "ModernQuote");
		}
		return null;
	}

	private HashMap Odt2PdfGeneration(Transaction transaction, Company company,
			BrandingTheme brandingTheme, boolean multipleIds,
			List<String> fileNames) {

		try {

			CreditNotePdfGeneration creditPdfGeneration = null;
			InvoicePdfGeneration invoicePdfGeneration = null;
			QuotePdfGeneration quotePdfGeneration = null;
			String templeteName = null;
			String fileName = null;

			if (transaction instanceof Invoice) {
				// for Invoice
				if (brandingTheme.getInvoiceTempleteName().contains(
						"Classic Template")) {
					templeteName = "templetes" + File.separator
							+ "InvoiceDocx.docx";
				} else {

					templeteName = ServerConfiguration.getAttachmentsDir()
							+ "/" + company.getId() + "/" + "templateFiles"
							+ "/" + brandingTheme.getID() + "/"
							+ brandingTheme.getInvoiceTempleteName();
				}
				fileName = "Invoice_" + transaction.getNumber();
				invoicePdfGeneration = new InvoicePdfGeneration(
						(Invoice) transaction, company, brandingTheme);
			}

			if (transaction instanceof CustomerCreditMemo) {
				// For CreditNote
				if (brandingTheme.getCreditNoteTempleteName().contains(
						"Classic Template")) {
					templeteName = "templetes" + File.separator
							+ "CreditDocx.docx";
				} else {
					templeteName = ServerConfiguration.getAttachmentsDir()
							+ "/" + company.getId() + "/" + "templateFiles"
							+ "/" + brandingTheme.getID() + "/"
							+ brandingTheme.getCreditNoteTempleteName();
				}
				fileName = "CreditNote_" + transaction.getNumber();
				creditPdfGeneration = new CreditNotePdfGeneration(
						(CustomerCreditMemo) transaction, company,
						brandingTheme);
			}
			if (transaction instanceof Estimate) {
				// For Quote
				if (brandingTheme.getQuoteTemplateName().contains(
						"Classic Template")) {
					templeteName = "templetes" + File.separator
							+ "QuoteDocx.docx";
				} else {
					templeteName = ServerConfiguration.getAttachmentsDir()
							+ "/" + company.getId() + "/" + "templateFiles"
							+ "/" + brandingTheme.getID() + "/"
							+ brandingTheme.getQuoteTemplateName();
				}
				fileName = "Quote_" + transaction.getNumber();
				quotePdfGeneration = new QuotePdfGeneration(
						(Estimate) transaction, company, brandingTheme);
			}

			InputStream in = new BufferedInputStream(new FileInputStream(
					templeteName));

			IXDocReport report = XDocReportRegistry.getRegistry().loadReport(
					in, TemplateEngineKind.Velocity);
			IContext context = report.createContext();

			if (transaction instanceof CustomerCreditMemo) {
				context = creditPdfGeneration.assignValues(context, report);
			} else if (transaction instanceof Invoice) {
				context = invoicePdfGeneration.assignValues(context, report);
			} else if (transaction instanceof Estimate) {
				context = quotePdfGeneration.assignValues(context, report);
			}

			if (multipleIds) {
				Options options = Options.getTo(ConverterTypeTo.PDF).via(
						ConverterTypeVia.ITEXT);

				File file = File.createTempFile(fileName.replace(" ", ""),
						".pdf");
				java.io.FileOutputStream fos = new java.io.FileOutputStream(
						file);
				report.convert(context, options, fos);
				fileNames.add(file.getAbsolutePath());

			}

			HashMap objects = new HashMap();
			objects.put("context", context);
			objects.put("report", report);
			return objects;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

	/**
	 * for merging custom PDF Documents in to Single Pdf Document
	 * 
	 * @param fileNames
	 * @param outPutFileName
	 * @param paginate
	 */
	private void mergePDFDocuments(List<String> fileNames,
			OutputStream outputStream, boolean paginate) {
		Document document = new Document();
		try {
			List<InputStream> pdfs = new ArrayList<InputStream>();

			Iterator<String> fileIterator = fileNames.iterator();
			while (fileIterator.hasNext()) {
				String next = fileIterator.next();
				pdfs.add(new FileInputStream(next));
			}

			List<PdfReader> readers = new ArrayList<PdfReader>();
			int totalPages = 0;
			Iterator<InputStream> iteratorPDFs = pdfs.iterator();

			// Create Readers for the pdfs.
			while (iteratorPDFs.hasNext()) {
				InputStream pdf = iteratorPDFs.next();
				PdfReader pdfReader = new PdfReader(pdf);
				readers.add(pdfReader);
				totalPages += pdfReader.getNumberOfPages();
			}
			// Create a writer for the outputstream
			PdfWriter writer = PdfWriter.getInstance(document, outputStream);

			document.open();
			BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA,
					BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
			PdfContentByte cb = writer.getDirectContent(); // Holds the PDF
			// data

			PdfImportedPage page;
			int currentPageNumber = 0;
			int pageOfCurrentReaderPDF = 0;
			Iterator<PdfReader> iteratorPDFReader = readers.iterator();

			// Loop through the PDF files and add to the output.
			while (iteratorPDFReader.hasNext()) {
				PdfReader pdfReader = iteratorPDFReader.next();

				// Create a new page in the target for each source page.
				while (pageOfCurrentReaderPDF < pdfReader.getNumberOfPages()) {
					document.newPage();
					pageOfCurrentReaderPDF++;
					currentPageNumber++;
					page = writer.getImportedPage(pdfReader,
							pageOfCurrentReaderPDF);
					cb.addTemplate(page, 0, 0);

					// Code for pagination.
					if (paginate) {
						cb.beginText();
						cb.setFontAndSize(bf, 9);
						cb.showTextAligned(PdfContentByte.ALIGN_CENTER, ""
								+ currentPageNumber + " of " + totalPages, 520,
								5, 0);
						cb.endText();
					}
				}
				pageOfCurrentReaderPDF = 0;
			}

			outputStream.flush();
			document.close();
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (document.isOpen())
				document.close();
			try {
				if (outputStream != null)
					outputStream.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}

}
