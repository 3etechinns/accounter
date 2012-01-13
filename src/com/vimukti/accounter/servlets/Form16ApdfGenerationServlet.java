package com.vimukti.accounter.servlets;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lowagie.text.FontFactory;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.Form16ApdfTemplate;
import com.vimukti.accounter.core.TDSCoveringLetterTemplate;
import com.vimukti.accounter.core.TemplateBuilder;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.main.CompanyPreferenceThreadLocal;
import com.vimukti.accounter.servlets.GeneratePDFservlet.FontFactoryImpEx;
import com.vimukti.accounter.web.client.core.ClientTDSDeductorMasters;
import com.vimukti.accounter.web.client.core.ClientTDSResponsiblePerson;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.server.FinanceTool;

import fr.opensagres.xdocreport.converter.ConverterTypeTo;
import fr.opensagres.xdocreport.converter.ConverterTypeVia;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;

public class Form16ApdfGenerationServlet extends BaseServlet {

	/**
	 * this file is required to generate the form 16A pdf
	 */
	private static final long serialVersionUID = 1L;
	private String fileName;
	private Long companyID;
	private FinanceTool financetool;
	private Company company;
	private long vendorID;
	private int type;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String companyName = getCompanyName(req);
		if (companyName == null)
			return;
		try {
			generatePDF(req, resp, companyName);
		} catch (AccounterException e) {
			e.printStackTrace();
		}
	}

	private void generatePDF(HttpServletRequest req, HttpServletResponse resp,
			String companyName) throws AccounterException {
		try {
			ServletOutputStream sos = null;
			List<String> fileNames = new ArrayList<String>();

			String vendorIDString = req.getParameter("vendorID");
			String dateRangeString = req.getParameter("datesRange");
			String placeString = req.getParameter("place");
			String printDateString = req.getParameter("printDate");
			String typeString = req.getParameter("type");

			type = Integer.parseInt(typeString);

			vendorID = Long.parseLong(vendorIDString);
			companyID = (Long) req.getSession().getAttribute(COMPANY_ID);
			financetool = new FinanceTool();
			TemplateBuilder.setCmpName(companyName);
			company = financetool.getCompany(companyID);
			CompanyPreferenceThreadLocal.set(financetool.getCompanyManager()
					.getClientCompanyPreferences(company));

			Set<Vendor> vendors = company.getVendors();
			IXDocReport report = null;
			IContext context = null;
			if (type == 0) {

				fileName = "Form16A";
				String templeteName = null;
				templeteName = "templetes" + File.separator + "Form16a.docx";

				ClientTDSDeductorMasters tdsDeductorMasterDetails = financetool
						.getTDSDeductorMasterDetails(company.getID());
				Form16ApdfTemplate form16APdfGeneration = null;
				form16APdfGeneration = new Form16ApdfTemplate();

				for (Vendor vendor : vendors) {
					if (vendor.getID() == vendorID) {
						form16APdfGeneration.setVendorandCompanyData(vendor,
								tdsDeductorMasterDetails);
						break;
					}
				}
				form16APdfGeneration.setDateYear(dateRangeString);

				InputStream in = new BufferedInputStream(new FileInputStream(
						templeteName));

				try {
					report = XDocReportRegistry.getRegistry().loadReport(in,
							TemplateEngineKind.Velocity);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (XDocReportException e) {
					e.printStackTrace();
				}
				context = report.createContext();
				context = form16APdfGeneration.assignValues(context, report);

			} else {

				Vendor ven = null;
				for (Vendor vendor : vendors) {
					if (vendor.getID() == vendorID) {
						ven = vendor;
						fileName = "CoveringLetter" + vendor.getName();
						break;
					}
				}

				String templeteName = null;
				templeteName = "templetes" + File.separator
						+ "CoveringLetter.docx";

				ClientTDSResponsiblePerson responsiblePersonDetails = financetool
						.getResponsiblePersonDetails(company.getID());

				TDSCoveringLetterTemplate coveringLetterTemp = new TDSCoveringLetterTemplate();
				coveringLetterTemp.setValues(ven, company,
						responsiblePersonDetails);

				InputStream in = new BufferedInputStream(new FileInputStream(
						templeteName));

				try {
					report = XDocReportRegistry.getRegistry().loadReport(in,
							TemplateEngineKind.Velocity);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (XDocReportException e) {
					e.printStackTrace();
				}
				context = report.createContext();
				context = coveringLetterTemp.assignValues(context, report);

			}
			FontFactory.setFontImp(new FontFactoryImpEx());
			Options options = Options.getTo(ConverterTypeTo.PDF).via(
					ConverterTypeVia.ITEXT);

			resp.setContentType("application/pdf");
			resp.setHeader("Content-disposition", "attachment; filename="
					+ fileName.replace(" ", "") + ".pdf");

			sos = resp.getOutputStream();

			report.convert(context, options, sos);
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.err.println("Pdf created");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
}
