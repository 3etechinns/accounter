package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.TextDecoration;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.ValueCallBack;
import com.vimukti.accounter.web.client.core.ClientBrandingTheme;
import com.vimukti.accounter.web.client.ui.core.AccounterButton;
import com.vimukti.accounter.web.client.ui.forms.CustomDialog;

@SuppressWarnings( { "deprecation" })
public class FileUploadDilaog extends CustomDialog {

	private String parentID;
	private FormPanel uploadForm;
	private ValueCallBack<ClientBrandingTheme> callback;
	private HorizontalPanel loadingLayout;
	private VerticalPanel mainLayout;
	private VerticalPanel panel;
	private ArrayList<FileUpload> uploadItems = new ArrayList<FileUpload>();
	private int count = 1;
	private VerticalPanel uploadFormLayout;
	private String[] fileTypes;
	private HorizontalPanel buttonHlay;
	private boolean closeAfterUploaded;
	private static ClientBrandingTheme brandingTheme;
	@SuppressWarnings("unused")
	private String title;
	private HTML detailsHtml, helpHtml, chooseHtml;

	@SuppressWarnings("static-access")
	public FileUploadDilaog(String title, String parentID,
			ValueCallBack<ClientBrandingTheme> callback, String[] fileTypes,
			ClientBrandingTheme theme) {
		super(false, true);
		this.title = title;
		setHTML(title);
		this.parentID = parentID;
		this.callback = callback;
		this.fileTypes = fileTypes;
		this.brandingTheme = theme;
		closeAfterUploaded = true;
		doCreateContents();
		center();
	}

	protected void doCreateContents() {

		uploadForm = new FormPanel();
		uploadForm.setStyleName("fileuploaddialog-uploadform");
		final String fileID = createID();

		uploadForm.setEncoding(FormPanel.ENCODING_MULTIPART);
		uploadForm.setMethod(FormPanel.METHOD_POST);

		// Create a panel to hold all of the form widgets.
		VerticalPanel vpaPanel = new VerticalPanel();

		panel = new VerticalPanel();
		/* make space small */
		panel.setSpacing(2);
		panel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		// Create a FileUpload widget.
		detailsHtml = new HTML(Accounter.constants()
				.logoComment());
		helpHtml = new HTML(Accounter.constants()
				.helpContent());
		helpHtml.addMouseOverHandler(new MouseOverHandler() {

			@Override
			public void onMouseOver(MouseOverEvent event) {
				helpHtml.getElement().getStyle().setCursor(Cursor.POINTER);
				helpHtml.getElement().getStyle().setTextDecoration(
						TextDecoration.UNDERLINE);
			}
		});
		helpHtml.addMouseOutHandler(new MouseOutHandler() {

			@Override
			public void onMouseOut(MouseOutEvent event) {
				helpHtml.getElement().getStyle().setTextDecoration(
						TextDecoration.NONE);
			}
		});
		helpHtml.setVisible(false);
		chooseHtml = new HTML(Accounter.constants()
				.chooseLogo());
		final FileUpload upload = new FileUpload();
		/* Default height of upload text box 26 */
		upload.getElement().setAttribute("size", "33");
		upload.setName(fileID);
		uploadItems.add(upload);
		panel.add(detailsHtml);
		panel.add(helpHtml);
		panel.add(chooseHtml);
		panel.add(upload);
		vpaPanel.add(panel);

		// Add a 'submit' button.
		AccounterButton uploadSubmitButton = new AccounterButton("Upload");
		uploadSubmitButton.setWidth("80px");
		// vpaPanel.add(uploadSubmitButton);

		AccounterButton closeButton = new AccounterButton(Accounter
				.constants().close());
		closeButton.setWidth("80px");
		buttonHlay = new HorizontalPanel();
		buttonHlay.add(uploadSubmitButton);
		buttonHlay.add(closeButton);
		buttonHlay.setStyleName("panel-right-align");
		uploadSubmitButton.enabledButton();
		closeButton.enabledButton();
		vpaPanel.add(buttonHlay);
		/* Make align three Element on there position */
		// buttonHlay.setCellWidth(uploadSubmitButton);

		buttonHlay.setCellHorizontalAlignment(closeButton,
				HasHorizontalAlignment.ALIGN_RIGHT);
		buttonHlay.setCellHorizontalAlignment(uploadSubmitButton,
				HasHorizontalAlignment.ALIGN_RIGHT);

		uploadSubmitButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				processOnUpload();

			}
		});
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				close();

			}
		});

		uploadForm.setWidget(vpaPanel);

		uploadForm.addFormHandler(new FormHandler() {
			public void onSubmit(FormSubmitEvent event) {

			}

			public void onSubmitComplete(FormSubmitCompleteEvent event) {

				String value = event.getResults();
				if (brandingTheme != null) {
					brandingTheme.setFileName(value);
					brandingTheme.setLogoAdded(true);
				} else {
					brandingTheme = new ClientBrandingTheme();
					brandingTheme.setFileName(value);
					brandingTheme.setLogoAdded(true);
				}

				if (value == null) {
					getFileInfo();
				} else {
					if (value.trim().length() != 0) {
						processUploadAttachments(brandingTheme, callback);
					} else {
						loadingLayout.setVisible(false);
					}

				}
			}

		});
		uploadFormLayout = new VerticalPanel();
		uploadFormLayout.add(uploadForm);
		mainLayout = new VerticalPanel();
		mainLayout.add(uploadFormLayout);
		add(mainLayout);
		show();
	}

	protected void getFileInfo() {
		getFileInfo(parentID, new AsyncCallback<ClientBrandingTheme>() {
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onSuccess(ClientBrandingTheme result) {
				processUploadAttachments(result, callback);
			}
		});

	}

	public HorizontalPanel getFileItem() {
		final HorizontalPanel panel = new HorizontalPanel();
		// panel.setHeight("30");
		panel.setStyleName("fileuploaddialog-fileitem");

		final FileUpload upload = new FileUpload();
		String fileID = createID();
		upload.setName(fileID);
		upload.getElement().setAttribute("size", "50");

		HTML label = new HTML(
				"<font color='blue'><u style='cursor:pointer;'><nobr>Remove<nobr></u></font>");
		label.setWidth("60");
		label.setHeight("25");
		label.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				panel.removeFromParent();
				uploadItems.remove(upload);
				count--;
				uploadFormLayout.setHeight(new StringBuilder(uploadFormLayout
						.getOffsetHeight() - 30).toString());
			}
		});

		uploadItems.add(upload);
		panel.add(upload);
		panel.add(label);

		return panel;

	}

	private boolean checkFileType(String name, String[] types) {
		String type = name.substring(name.lastIndexOf('.') + 1);
		for (String fileType : types) {
			if (type.equalsIgnoreCase(fileType))
				return true;

		}
		return false;
	}

	private void processUploadAttachments(ClientBrandingTheme result,
			final ValueCallBack<ClientBrandingTheme> callback2) {

		if (callback2 != null) {
			callback2.execute(result);
		}
		if (closeAfterUploaded) {
			close();
		}

	}

	protected void processOnUpload() {
		if (!validateFileItems()) {
			Accounter.showInformation("No Image is selected.");
			return;
		}

		if (fileTypes != null) {
			for (FileUpload fileUpload : uploadItems) {
				if (!checkFileType(fileUpload.getFilename(), fileTypes)) {
					return;
				}
			}

		}
		uploadForm.submit();

	}

	private boolean validateFileItems() {
		// String ids="";
		boolean fileSelected = false;
		for (FileUpload upload : uploadItems) {
			String filename = upload.getFilename();

			if (filename != null && filename.length() > 0) {
				fileSelected = true;
			}
		}

		if (fileSelected) {
			uploadForm.setAction("/do/uploadfile");
			return fileSelected;
		}

		return fileSelected;

	}

	public void close() {
		this.removeFromParent();
	}

	public native static String createID()/*-{
		var MES_UNIQUE_IDS = {};
		var mes_generateUniqueId = function(charset, len, isNotInDOM) {
			var i = 0;
			if (!charset) {
				charset = 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';
			}
			if (!len) {
				len = 40;
			}
			var id = '', charsetlen = charset.length, charIndex;

			// iterate on the length and get a random character for each position
			for (i = 0; len > i; i += 1) {
				charIndex = Math.random() * charsetlen;
				id += charset.charAt(charIndex);
			}

			if (MES_UNIQUE_IDS[id] || (isNotInDOM)) {
				MES_UNIQUE_IDS[id] = true; // add DOM ids to the map
				return mes_generateUniqueId(charset, len, isNotInDOM);
			}

			MES_UNIQUE_IDS[id] = true;

			return id;
		};
		return mes_generateUniqueId();
	}-*/;

	public void getFileInfo(String parentID,
			final AsyncCallback<ClientBrandingTheme> callback) {
		post("/do/uploadfile?parentId=" + parentID,
				new AsyncCallback<ClientBrandingTheme>() {

					public void onSuccess(ClientBrandingTheme value) {
						callback.onSuccess(value);
					}

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
					}
				}, parentID);
	}

	public static <E, T> Object post(String url,
			final AsyncCallback<ClientBrandingTheme> callback, String parentId) {
		RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, url);
		builder.setHeader("Accept", "text/html");
		// Create a callback object to handle the result
		RequestCallback requestCallback = new RequestCallback() {
			public void onError(Request request, Throwable exception) {
				exception.printStackTrace();
			}

			@Override
			public void onResponseReceived(Request request, Response response) {
				// ClientBrandingTheme attachment =
				// parseStringAttachment(response
				// .getText());
				callback.onSuccess(brandingTheme);
			}
		};
		try {
			Request request = builder.sendRequest("", requestCallback);
			return request;
		} catch (RequestException e) {
			e.printStackTrace();
		}
		return null;
	}
}
