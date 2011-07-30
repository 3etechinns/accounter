package com.vimukti.accounter.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Suffixservlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		super.service(request, response);
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String requestURI = request.getRequestURI();
		if (!(requestURI.contains(".css") || requestURI.contains("."))) {
			request.getRequestDispatcher(
					"/WEB-INF" + request.getPathInfo() + ".jsp").forward(
					request, response);
		} else {
			if (requestURI.contains("site"))
				request.getRequestDispatcher("/sites" + request.getPathInfo())
						.forward(request, response);
		}
	}
}
