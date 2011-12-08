package com.vimukti.accounter.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.utils.HibernateUtil;

public class ForgetPasswordServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String view = "/WEB-INF/forgotpassword.jsp";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		dispatch(req, resp, view);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @param getting from httpservlet request object are emailId and company
	 * name form url.
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String emailID = req.getParameter("emailId");
		if (emailID == null) {
			req.setAttribute("errorMessage",
					"we couldn't find any user with the given Email ID. please enter a valid email");
			dispatch(req, resp, view);
			return;
		}
		emailID = emailID.toLowerCase().trim();

		Session serverSession = HibernateUtil.openSession();
		Transaction transaction = null;
		try {
			transaction = serverSession.beginTransaction();
			Client client = getClient(emailID);

			if (client == null) {
				req.setAttribute(
						"errorMessage",
						"we couldn't find any user with the given Email ID. please enter a valid Email. ");
				dispatch(req, resp, view);
				return;
			}

			deleteActivationsByEmail(emailID);

			String token = createActivation(emailID);

			sendForgetPasswordLinkToUser(client, token);

			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (transaction != null) {
				transaction.rollback();
			}
		} finally {
			serverSession.close();
		}

		// String successMessage =
		// "Reset Password link has been sent to the given emailId, Kindly check your Mail box.";
		// req.setAttribute("successmessage", successMessage);
		String message = "?message="+ACT_FROM_RESET;
		redirectExternal(req, resp, ACTIVATION_URL + message);
	}

}
