package com.vimukti.accounter.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Query;
import org.hibernate.Session;

import com.vimukti.accounter.core.Activation;
import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.utils.HibernateUtil;

public class EmailForActivationServlet extends BaseServlet {
	private static final String VIEW = "/WEB-INF/emailforactivation.jsp";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		dispatch(req, resp, VIEW);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String email = req.getParameter("emailid");
		
		//Getting activation object using the mail id
		Activation activation = getActivationByEmailId(email);
		
		if(activation == null){
			//send error message
			req.setAttribute("errormessage", "Invalid email id, please enter the email id that you used during sign up process.");
			dispatch(req, resp, VIEW);
		}else{
			String token = activation.getToken();
			Client client = getClient(email);
			// Email to that user.
			sendActivationEmail(token, client);
			// Send to SignUp Success View
			req.setAttribute(
					"successmessage",
					"Your activation code is sent to your mail id. Please check your email and enter your activation code below to activate your account.");
			
			//redirect to activation page
			req.getRequestDispatcher(ACTIVATION_URL).include(req, resp);
			
		}
		

	}

	private Activation getActivationByEmailId(String email) {
		Session session = HibernateUtil.getCurrentSession();
		if(session == null){
			session = HibernateUtil.openSession(LOCAL_DATABASE);
		}
		try{
		if (session != null) {
			if(!session.isOpen())
				session = HibernateUtil.openSession(LOCAL_DATABASE);
			Query query = session
					.getNamedQuery("get.activation.by.emailid");
			query.setParameter("emailId", email);
			Activation val = (Activation)query.uniqueResult();
			return val;

		}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
}
