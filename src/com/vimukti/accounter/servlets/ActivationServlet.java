package com.vimukti.accounter.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.vimukti.accounter.core.Activation;
import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.utils.HibernateUtil;

public class ActivationServlet extends BaseServlet {

	protected static final Log LOG = LogFactory.getLog(ActivationServlet.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// private static final String VIEW = "/WEB-INF/resetpassword.jsp";
	private static final String VIEW = "/WEB-INF/activation.jsp";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		HttpSession session = req.getSession(true);
		String parameter = req.getParameter("message");
		if (parameter != null && parameter.equals("108")) {
			req.setAttribute(
					"successmessage",
					"Thanks for registering with Accounter!<br>To complete the sign up process, please check your email and enter your activation code here to activate your Account.");
			session.setAttribute(ACTIVATION_TYPE, "signup");
		}
		if (parameter != null && parameter.equals("109")) {
			req.setAttribute(
					"successmessage",
					"Reset Password link has been sent to the given emailId, Kindly check your Mail box.");
			session.setAttribute(ACTIVATION_TYPE, "resetpassword");
		}
		dispatch(req, resp, VIEW);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		// get the token
		LOG.info(req);
		String token = req.getParameter("code");

		if (token == null) {
			dispatch(req, resp, VIEW);
			return;
		}

		// get activation record
		Session hibernateSession = HibernateUtil.openSession(LOCAL_DATABASE);
		try {
			Activation activation = getActivation(token);
			// If it is null
			if (activation == null) {
				// set Error "Token has expired"
				// We check him and if invalid code we show him form to enter
				// valid code.
				redirectExternal(req, resp, ACTIVATION_URL);
			} else {
				// If code is valid we create the user and set the session and
				// external redirect him to <dest> param or /login
				HttpSession session = req.getSession(true);
				session.setAttribute(ACTIVATION_TOKEN, token);
				session.setAttribute(EMAIL_ID, activation.getEmailId());
				String activationType = (String) session
						.getAttribute(ACTIVATION_TYPE);
				if (activationType != null
						&& activationType.equals("resetpassword")) {
					session.removeAttribute(ACTIVATION_TYPE);
					redirectExternal(req, resp, RESET_PASSWORD_URL);
					return;
				}

				Session hbSession = HibernateUtil.openSession(LOCAL_DATABASE);
				Transaction transaction = hbSession.beginTransaction();
				try {
					Client client = (Client) hbSession
							.getNamedQuery("getClient.by.mailId")
							.setParameter(EMAIL_ID, activation.getEmailId())
							.uniqueResult();
					client.setActive(true);
					saveEntry(client);

					// delete activation object
					
					Query query = hbSession
							.getNamedQuery("delete.activation.by.emailId");
					query.setParameter("emailId", activation.getEmailId()
							.trim());
					int updatedRows = query.executeUpdate();
					LOG.debug("No of updated rows = " + updatedRows);
					transaction.commit();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (hbSession != null)
						hbSession.close();
					
				}

				// redirect To ActivationPage.
				// dispatch(req, resp, VIEW);
				String destUrl = req.getParameter(PARAM_DESTINATION);
				if (destUrl == null || destUrl.isEmpty()) {
					redirectExternal(req, resp, LOGIN_URL);
					return;
				} else {
					redirectExternal(req, resp, destUrl);
					return;
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (hibernateSession != null) {
				hibernateSession.close();
			}
		}

	}

	private Activation getActivation(String token) {
		Session session = HibernateUtil.getCurrentSession();
		if (session != null) {
			Activation val = (Activation) session
					.getNamedQuery("get.activation.by.token")
					.setString("token", token).uniqueResult();
			return val;

		}
		return null;
	}
}
