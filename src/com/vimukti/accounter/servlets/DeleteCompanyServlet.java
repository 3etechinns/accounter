package com.vimukti.accounter.servlets;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Types;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.vimukti.accounter.core.AccounterThreadLocal;
import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.ClientConvertUtil;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.services.S2SServiceImpl;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.core.ClientUser;

/**
 * @author Prasanna Kumar G
 * 
 */

public class DeleteCompanyServlet extends BaseServlet {

	private static final String deleteCompanyView = "/WEB-INF/deleteCompany.jsp";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		HttpSession session = req.getSession();
		String emailID = (String) session.getAttribute(EMAIL_ID);
		if (emailID == null) {
			redirectExternal(req, resp, LOGIN_URL);
			return;
		}

		String parameter = req.getParameter("isCancel");

		if (parameter != null && parameter.equalsIgnoreCase("true")) {
			req.getSession().removeAttribute(COMPANY_ID);
			redirectExternal(req, resp, COMPANIES_URL);
			return;
		}

		String companyId = req.getParameter(COMPANY_ID);
		setOptions(req, companyId, emailID);
		dispatch(req, resp, deleteCompanyView);
	}

	private void setOptions(HttpServletRequest req, String companyId,
			String emailID) {
		boolean canDeleteFromSingle = true, canDeleteFromAll = true;
		Session hibernateSession = HibernateUtil.getCurrentSession();
		try {
			Company company = (Company) hibernateSession.get(Company.class,
					Long.parseLong(companyId));
			User user = (User) hibernateSession
					.getNamedQuery("user.by.emailid")
					.setParameter("emailID", emailID)
					.setParameter("company", company).uniqueResult();

			Query query = hibernateSession.getNamedQuery("get.Admin.Users")
					.setParameter("company", company);
			List<User> adminUsers = query.list();
			if (adminUsers.size() < 2) {
				for (User u : adminUsers) {
					if (u.getID() == user.getID()) {
						canDeleteFromSingle = false;
						break;
					}
				}
			}

			if (user != null && !user.isAdmin()) {
				canDeleteFromAll = false;
			}
		} finally {
		}
		req.setAttribute("canDeleteFromSingle", canDeleteFromSingle);
		req.setAttribute("canDeleteFromAll", canDeleteFromAll);
		req.getSession().setAttribute(COMPANY_ID, Long.parseLong(companyId));

	}

	/**
	 * @param req
	 * @param resp
	 * @throws IOException
	 * @throws ServletException
	 */
	private void deleteComapny(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		final String email = (String) req.getSession().getAttribute(EMAIL_ID);

		String message = "";

		final String companyID = (String) req.getSession().getAttribute(
				COMPANY_ID);
		String delete = req.getParameter("delete");
		if (delete == null) {
			req.setAttribute("message", "Please select the option.");
			setOptions(req, companyID, email);
			dispatch(req, resp, deleteCompanyView);
			return;
		}

		final boolean deleteAllUsers = delete.equals("deleteAllUsers");
		final HttpSession httpSession = req.getSession();
		new Thread(new Runnable() {

			@Override
			public void run() {
				httpSession.setAttribute(COMPANY_DELETION_STATUS,
						COMPANY_DELETING);
				Session session = HibernateUtil.openSession();
				Transaction transaction = null;
				try {
					boolean canDeleteFromSingle = true, canDeleteFromAll = true;

					Company company = (Company) session.get(Company.class,
							Long.parseLong(companyID));
					User user = (User) session.getNamedQuery("user.by.emailid")
							.setParameter("emailID", email)
							.setParameter("company", company).uniqueResult();

					Query query = session.getNamedQuery("get.Admin.Users")
							.setParameter("company", company);
					List<User> adminUsers = query.list();
					if (adminUsers.size() < 2) {
						for (User u : adminUsers) {
							if (u.getID() == user.getID()) {
								canDeleteFromSingle = false;
								break;
							}
						}
					}

					if (user != null && !user.isAdmin()) {
						canDeleteFromAll = false;
					}
					AccounterThreadLocal.set(user);

					transaction = session.beginTransaction();

					Client client = getClient(email);
					Company serverCompany = null;
					if (companyID == null) {
						httpSession.setAttribute(COMPANY_DELETION_STATUS,
								"Fail");
						httpSession.setAttribute("DeletionFailureMessage",
								"Internal Error.");
						return;
					}
					for (User usr : client.getUsers()) {
						if (usr.getCompany().getID() == Long
								.parseLong(companyID)) {
							serverCompany = usr.getCompany();
						}
					}

					if (serverCompany == null) {
						httpSession.setAttribute(COMPANY_DELETION_STATUS,
								"Fail");
						httpSession.setAttribute("DeletionFailureMessage",
								"Company Doesnot Exist");
						return;
					}

					S2SServiceImpl s2sService = new S2SServiceImpl();

					// boolean isAdmin = s2sService.isAdmin(
					// Long.parseLong(companyID), email);

					if (!canDeleteFromSingle && !canDeleteFromAll) {
						httpSession.setAttribute(COMPANY_DELETION_STATUS,
								"Fail");
						httpSession
								.setAttribute("DeletionFailureMessage",
										"You Don't have Permissions to Delete this Company");
						return;
					}

					if (canDeleteFromAll
							&& (deleteAllUsers || serverCompany
									.getNonDeletedUsers().size() == 1)) {

						CallableStatement call = session.connection()
								.prepareCall("{ ? = call delete_company(?) }");
						call.registerOutParameter(1, Types.BOOLEAN);
						call.setLong(2, serverCompany.getId());
						call.execute();
						boolean isDeleted = call.getBoolean(1);
						if (!isDeleted) {
							throw new Exception();
						}

					} else if (canDeleteFromSingle) {

						// Deleting Client from ServerCompany
						// client.getUsers().remove(user);
						// session.saveOrUpdate(client);
						ClientUser clientUser = new ClientConvertUtil()
								.toClientObject(user, ClientUser.class);
						clientUser.setEmail(user.getClient().getEmailId());
						s2sService.deleteUserFromCompany(
								Long.parseLong(companyID), clientUser);
					}
					transaction.commit();
					httpSession
							.setAttribute(COMPANY_DELETION_STATUS, "Success");
				} catch (Exception e) {
					e.printStackTrace();
					if (transaction != null) {
						transaction.rollback();
					}
					httpSession.setAttribute(COMPANY_DELETION_STATUS, "Fail");
					httpSession.setAttribute("DeletionFailureMessage",
							"Internal Error Occured");
				} finally {
					httpSession.removeAttribute(COMPANY_ID);
					session.close();
				}
			}
		}).start();

		if (message.isEmpty()) {
			message = "Success";
		}
		redirectExternal(req, resp, COMPANY_STATUS_URL);
		// req.setAttribute("message", message);
		// dispatch(req, resp, deleteCompanyView);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String emailID = (String) req.getSession().getAttribute(EMAIL_ID);
		if (emailID == null) {
			req.setAttribute("message",
					"Company deletion failed because of invalide session.");
			dispatch(req, resp, deleteCompanyView);
			return;
		}

		deleteComapny(req, resp);
	}
}
