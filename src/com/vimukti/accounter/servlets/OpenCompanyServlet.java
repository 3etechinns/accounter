package com.vimukti.accounter.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.zschech.gwt.comet.server.CometServlet;
import net.zschech.gwt.comet.server.CometSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.vimukti.accounter.core.Activity;
import com.vimukti.accounter.core.ActivityType;
import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.main.ServerLocal;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.server.CometManager;
import com.vimukti.accounter.web.server.FinanceTool;

public class OpenCompanyServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected static final Log logger = LogFactory
			.getLog(OpenCompanyServlet.class);
	private static final String REDIRECT_PAGE = "/WEB-INF/Redirect.jsp";
	private static final String USER_NAME = "userName";
	private static final String COMPANY_NAME = "companyName";

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String url = request.getRequestURI().toString();

		String isTouch = (String) request.getSession().getAttribute(IS_TOUCH);
		request.setAttribute(IS_TOUCH, isTouch == null ? "false" : isTouch);

		if (url.equals(ACCOUNTER_OLD_URL)) {
			dispatch(request, response, REDIRECT_PAGE);
			return;
		}
		String emailID = (String) request.getSession().getAttribute(EMAIL_ID);

		if (emailID != null) {

			Long serverCompanyID = (Long) request.getSession().getAttribute(
					COMPANY_ID);
			String create = (String) request.getSession().getAttribute(CREATE);
			if (serverCompanyID == null) {
				if (create != null && create.equals("true")) {
					RequestDispatcher dispatcher = getServletContext()
							.getRequestDispatcher("/WEB-INF/Accounter.jsp");
					dispatcher.forward(request, response);
					return;
				} else {
					response.sendRedirect(COMPANIES_URL);
					return;
				}
			}
			initComet(request.getSession(), serverCompanyID, emailID);

			Session session = HibernateUtil.openSession();
			try {
				Transaction transaction = session.beginTransaction();
				Company company = getCompany(request);
				User user = (User) session.getNamedQuery("user.by.emailid")
						.setParameter("company", company)
						.setParameter("emailID", emailID).uniqueResult();
				if (user == null) {
					response.sendRedirect(COMPANIES_URL);
					return;
				}
				Activity activity = new Activity(getCompany(request), user,
						ActivityType.LOGIN);

				session.save(activity);
				transaction.commit();
				user = HibernateUtil.initializeAndUnproxy(user);
				// there is no session, so do external redirect to login page
				// response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
				// response.setHeader("Location", "/Accounter.jsp");
				
				FinanceTool financeTool = new FinanceTool();
				Query namedQuery = session.getNamedQuery("getClient.by.mailId");
				namedQuery.setParameter(BaseServlet.EMAIL_ID, emailID);
				Client client = (Client) namedQuery.uniqueResult();
				String language = getlocale().getLanguage();
				HashMap<String, String> keyAndValues = null;//financeTool.getKeyAndValues(client.getID(), language);
				
				request.setAttribute("messages", keyAndValues);
				request.setAttribute(EMAIL_ID, user.getClient().getEmailId());
				request.setAttribute(USER_NAME, user.getClient().getFullName());
				request.setAttribute(COMPANY_NAME, company.getDisplayName()
						+ " - " + company.getID());

				RequestDispatcher dispatcher = getServletContext()
						.getRequestDispatcher("/WEB-INF/Accounter.jsp");
				dispatcher.forward(request, response);
			} finally {
				session.close();
			}
		} else {
			response.sendRedirect(LOGIN_URL);
			// Session is there, so show the main page

		}
	}

	private Locale getlocale() {
		return ServerLocal.get();
	}

	/**
	 * Initialising comet stuff
	 * 
	 * @param request
	 * @param identity
	 */
	private void initComet(HttpSession httpSession, long companyID,
			String emailID) {
		// Stream must be created otherwise user will get data
		// Continuously and browser will struck
		CometSession cometSession = CometServlet.getCometSession(httpSession);
		CometManager.initStream(httpSession.getId(), companyID, emailID,
				cometSession);
	}

}
