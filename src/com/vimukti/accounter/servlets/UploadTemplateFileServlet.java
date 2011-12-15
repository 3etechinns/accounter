package com.vimukti.accounter.servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import com.vimukti.accounter.main.ServerConfiguration;
import com.vimukti.accounter.utils.HibernateUtil;

/**
 * used to upload custom templates files
 * 
 */
public class UploadTemplateFileServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Session session = null;
		try {
			Long companyID = (Long) request.getSession().getAttribute(
					COMPANY_ID);

			String theme = request.getParameter("themeId");
			if (theme == null)
				return;

			Long id = Long.valueOf(theme);
			long themeId = id.longValue();

			if (companyID == null)
				return;
			session = HibernateUtil.openSession();
			StringBuilder builder = new StringBuilder();
			MultipartRequest multi = new MultipartRequest(request,
					ServerConfiguration.getTmpDir(), 50 * 1024 * 1024,
					"ISO-8859-1", new DefaultFileRenamePolicy());

			Enumeration<?> files = multi.getFileNames();
			while (files.hasMoreElements()) {
				String fileID = (String) files.nextElement();
				File file = multi.getFile(fileID);
				if (file != null) {
					String fileName = file.getName();
					File attachmentDir = new File(
							ServerConfiguration.getAttachmentsDir()
									+ "/"
									+ companyID
									+ "/"
									+ ServerConfiguration
											.getTemplateFilesPath() + "/"
									+ themeId);
					if (!attachmentDir.exists()) {
						attachmentDir.mkdirs();
					}
					FileOutputStream fout = new FileOutputStream(attachmentDir
							+ File.separator + fileName);
					response.setContentType("text/html");
					builder.append(fileName);
					InputStream in = new FileInputStream(file);
					OutputStream out = fout;
					byte[] buf = new byte[1024];
					int len;
					while ((len = in.read(buf)) > 0) {
						out.write(buf, 0, len);
					}
					in.close();
					out.close();
				}
			}
			response.getWriter().print(builder);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

}
