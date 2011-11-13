package com.vimukti.accounter.servlets;

import java.io.IOException;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Query;
import org.hibernate.Session;

import com.gdevelop.gwt.syncrpc.SyncProxy;
import com.vimukti.accounter.core.Activation;
import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.mail.UsersMailSendar;
import com.vimukti.accounter.main.ServerConfiguration;
import com.vimukti.accounter.services.IS2SService;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.utils.SecureUtils;
import com.vimukti.accounter.web.server.FinanceTool;

public class BaseServlet extends HttpServlet {
	public static final String USER_ID = "userID";
	public static final String ACTIVATION_TOKEN = "activationToken";
	public static final String ACTIVATION_TYPE = "resetpassword";
	public static final String OUR_COOKIE = "_accounter_01_infinity_22";

	public static final String COMPANY_COOKIE = "cid";

	public static final String COMPANY_ID = "companyId";

	public static final String CREATE = "create";

	public static final String EMAIL_ID = "emailId";
	public static final String PASSWORD = "password";
	protected static final String COMPANY_CREATION_STATUS = "comCreationStatus";
	protected static final String COMPANY_DELETION_STATUS = "deleteCompanyStatus";

	protected static final String COMPANY_DELETING = "Deleting";
	protected static final String COMPANY_CREATING = "Creating";

	protected static final String PARAM_DESTINATION = "destination";
	protected static final String PARAM_SERVER_COMPANY_ID = "serverCompanyId";
	protected static final String PARAM_COMPANY_TYPE = "companyType";
	protected static final String PARA_COMPANY_NAME = "companyName";
	protected static final String PARAM_FIRST_NAME = "firstName";
	protected static final String PARAM_LAST_NAME = "lastName";
	protected static final String PARAM_COUNTRY = "country";
	protected static final String PARAM_PH_NO = "phNo";
	
	protected static final String IS_TOUCH = "isTouch";

	protected static final String ATTR_MESSAGE = "message";
	protected static final String ATTR_COMPANY_LIST = "companeyList";

	protected static final String LOGIN_URL = "/main/login";
	protected static final String LOGOUT_URL = "/main/logout";
	protected static final String ACCOUNTER_OLD_URL = "/accounter";
	protected static final String ACCOUNTER_URL = "/company/accounter";
	protected static final String MAINTANANCE_URL = "/main/maintanance";

	protected static final String RESET_PASSWORD_URL = "/main/resetpassword";
	public static final String COMPANIES_URL = "/main/companies";
	protected static final String ACTIVATION_URL = "/main/activation";
	protected static final String CREATE_COMPANY_URL = "/main/createcompany";
	protected static final String DELETE_COMPANY_URL = "/main/deletecompany";
	protected String COMPANY_STATUS_URL = "/main/companystatus";
	
	
	public static final String ACT_FROM_SIGNUP="108";
	public static final String ACT_FROM_RESET="109";
	public static final String ACT_FROM_RESEND="110";
	public static final String ACT_FROM_LOGIN="111";

	/**
	 * 
	 */
	protected static final long serialVersionUID = 1L;
	public static final String LOCAL_DATABASE = "accounter";
	public static final int MAIL_ID = 0;
	public static final int NAME = 1;
	public static final int PHONE_NO = 2;
	private static final int ACTIVATION_CODE_SIZE = 10;

	protected Company getCompany(HttpServletRequest req) {
		Long companyID = (Long) req.getSession().getAttribute(COMPANY_ID);
		Session session = HibernateUtil.openSession();
		try {
			Company comapny = (Company) session.get(Company.class, companyID);
			if (comapny != null) {
				return comapny;
			}
		} catch (Exception e) {
			return null;
		} finally {
			session.close();
		}
		return null;
	}

	protected String getCompanyName(HttpServletRequest req) {
		Company company = getCompany(req);
		if (company != null) {
			return company.getTradingName();
		}
		// Query query = session.getNamedQuery("getServerCompany.by.id")
		// .setParameter("id", Long.valueOf(companyID));
		// if (query.list() != null && !query.list().isEmpty()) {
		// ServerCompany company = (ServerCompany) query.list().get(0);
		// return company.getCompanyName();
		// }
		return null;
	}

	protected boolean isCompanyExits(String companyName) {
		if (companyName == null) {
			return false;
		}
		Session openSession = HibernateUtil.openSession();
		try {
			Company uniqueResult = (Company) openSession
					.getNamedQuery("getCompanyName.is.unique")
					.setParameter("companyName", companyName).uniqueResult();
			return uniqueResult == null ? false : true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			openSession.close();
		}
	}

	protected FinanceTool getFinanceTool(HttpServletRequest request) {
		return null;
	}

	protected String getCookie(HttpServletRequest request, String ourCookie) {
		Cookie[] clientCookies = request.getCookies();
		if (clientCookies != null) {
			for (Cookie cookie : clientCookies) {
				if (cookie.getName().equals(ourCookie)) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}

	protected boolean isValidInputs(int inputType, String... values) {
		for (String value : values) {
			switch (inputType) {
			case MAIL_ID:
				return value
						.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,4}$");

			case NAME:
				return value.matches("^[a-zA-Z ]+$");

			case PHONE_NO:
				return value.matches("^[0-9][0-9-]+$");

			}
		}
		return false;
	}

	/**
	 * Dispatches the ServletRequest to The Corresponding View
	 * 
	 * @param req
	 * @param resp
	 * @param page
	 */
	protected void dispatch(HttpServletRequest req, HttpServletResponse resp,
			String page) {
		try {
			RequestDispatcher reqDispatcher = getServletContext()
					.getRequestDispatcher(page);
			reqDispatcher.forward(req, resp);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ServletException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Dispatches the ServletRequest to The Corresponding View with Message
	 * 
	 * @param message
	 * @param req
	 * @param resp
	 * @param page
	 */
	protected void dispatchMessage(String message, HttpServletRequest req,
			HttpServletResponse resp, String page) {
		req.setAttribute("errormessage", message);
		try {
			req.getRequestDispatcher(page).forward(req, resp);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	protected void saveEntry(Object object) {
		Session currentSession = HibernateUtil.getCurrentSession();
		currentSession.saveOrUpdate(object);
	}

	protected Client getClient(String emailId) {
		Session session = HibernateUtil.getCurrentSession();
		Query namedQuery = session.getNamedQuery("getClient.by.mailId");
		namedQuery.setParameter(EMAIL_ID, emailId);
		Client client = (Client) namedQuery.uniqueResult();
		// session.close();
		return client;
	}

	protected Client getClientById(long id) {
		Session session = HibernateUtil.getCurrentSession();
		Client client = (Client) session.getNamedQuery("getClient.by.Id")
				.setLong(0, id).uniqueResult();
		// session.close();
		return client;
	}

	/**
	 * Redirect to the External URL {@url}
	 * 
	 * @param req
	 * @param resp
	 * @param string
	 * @throws IOException
	 */
	protected void redirectExternal(HttpServletRequest req,
			HttpServletResponse resp, String url) throws IOException {
		resp.sendRedirect(url);
	}

	protected void sendActivationEmail(String token, Client client) {
		UsersMailSendar.sendActivationMail(token, client);
	}

	protected void sendMailToInvitedUser(Client user, String password,
			String companyName) {
		UsersMailSendar.sendMailToInvitedUser(user, password, companyName);
	}

	/**
	 * Creates new Activation with EmailID
	 * 
	 * @param emailID
	 * @return
	 */
	protected String createActivation(String emailID) {
		String token = SecureUtils.createID(ACTIVATION_CODE_SIZE);
		Activation activation = new Activation();
		activation.setEmailId(emailID);
		activation.setToken(token);
		activation.setSignUpDate(new Date());
		saveEntry(activation);
		return token;
	}

	/**
	 * Deletes the All Activation with this EmailID
	 * 
	 * @param emailId
	 */
	protected void deleteActivationsByEmail(String emailId) {
		Session session = HibernateUtil.getCurrentSession();
		session.getNamedQuery("delete.activation.by.emailId")
				.setString("emailId", emailId).executeUpdate();
	}

	protected IS2SService getS2sSyncProxy(String domainName) {
		String url = "http://" + domainName + ":"
				+ ServerConfiguration.getMainServerPort()
				+ "/company/stosservice";
		return (IS2SService) SyncProxy.newProxyInstance(IS2SService.class, url,
				"");
	}

	// /**
	// * @param serverId
	// */
	// protected void updateServers(Server accounterServer, boolean
	// isAddCompany) {
	// Session session = HibernateUtil.openSession();
	// Transaction transaction = null;
	// try {
	// transaction = session.beginTransaction();
	// Server server = (Server) session.get(Server.class,
	// accounterServer.getId());
	// int companiesCount = server.getCompaniesCount();
	// companiesCount += isAddCompany ? 1 : -1;
	// server.setCompaniesCount(companiesCount);
	// session.saveOrUpdate(server);
	// transaction.commit();
	// } catch (Exception e) {
	// if (transaction != null) {
	// transaction.rollback();
	// }
	// } finally {
	// session.close();
	// }
	// }

	/**
	 * Builds the URL for MainServer
	 * 
	 * @param logoutUrl
	 * @return
	 */
	protected String buildMainServerURL(String url) {
		StringBuilder mainServerURL = new StringBuilder("http://");
		mainServerURL.append(ServerConfiguration.getMainServerDomain());
		// mainServerURL.append(':');
		// mainServerURL.append(ServerConfiguration.getMainServerPort());
		mainServerURL.append(url);
		return mainServerURL.toString();
	}

	/**
	 * Builds the URL for COMPANy
	 * 
	 * @param logoutUrl
	 * @return
	 */
	protected String buildCompanyServerURL(String companyServerAddress,
			String url) {

		StringBuilder mainServerURL = new StringBuilder("http://");
		mainServerURL.append(companyServerAddress);
		// mainServerURL.append(':');
		// mainServerURL.append(ServerConfiguration.getMainServerPort());
		mainServerURL.append(url);
		return mainServerURL.toString();
	}

	/**
	 * Reset the password and send mail to user
	 */
	protected void sendForgetPasswordLinkToUser(Client client,
			String activationCode) {

		Session session = HibernateUtil.getCurrentSession();
		client.setRequirePasswordReset(true);

		session.save(client);
		StringBuffer link = new StringBuffer("https://");
		link.append(ServerConfiguration.getMainServerDomain());
		link.append(ACTIVATION_URL);
		System.out.println("@@@ ACTIVATION CODE::" + activationCode);
		UsersMailSendar.sendResetPasswordLinkToUser(link.toString(),
				activationCode, client.getEmailId());
	}

}
