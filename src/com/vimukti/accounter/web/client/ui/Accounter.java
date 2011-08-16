package com.vimukti.accounter.web.client.ui;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.IAccounterCRUDService;
import com.vimukti.accounter.web.client.IAccounterCRUDServiceAsync;
import com.vimukti.accounter.web.client.IAccounterCompanyInitializationService;
import com.vimukti.accounter.web.client.IAccounterCompanyInitializationServiceAsync;
import com.vimukti.accounter.web.client.IAccounterGETService;
import com.vimukti.accounter.web.client.IAccounterGETServiceAsync;
import com.vimukti.accounter.web.client.IAccounterHomeViewService;
import com.vimukti.accounter.web.client.IAccounterHomeViewServiceAsync;
import com.vimukti.accounter.web.client.IAccounterReportService;
import com.vimukti.accounter.web.client.IAccounterReportServiceAsync;
import com.vimukti.accounter.web.client.ValueCallBack;
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientUser;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.images.FinanceImages;
import com.vimukti.accounter.web.client.images.FinanceMenuImages;
import com.vimukti.accounter.web.client.theme.ThemeImages;
import com.vimukti.accounter.web.client.ui.core.AccounterDialog;
import com.vimukti.accounter.web.client.ui.core.ErrorDialogHandler;
import com.vimukti.accounter.web.client.ui.forms.CustomDialog;
import com.vimukti.accounter.web.client.ui.setup.SetupWizard;

/**
 * 
 * 
 */
public class Accounter implements EntryPoint {

	private static MainFinanceWindow mainWindow;
	protected ValueCallBack<Accounter> callback;
	private ClientFinanceDate endDate;
	private static PlaceController placeController;
	private static SetupWizard setupWizard;

	private static ClientUser user = null;
	private static ClientCompany company = null;

	public final static String CI_SERVICE_ENTRY_POINT = "/do/accounter/ci/rpc/service";
	public final static String CRUD_SERVICE_ENTRY_POINT = "/do/accounter/crud/rpc/service";
	public final static String GET_SERVICE_ENTRY_POINT = "/do/accounter/get/rpc/service";
	public final static String HOME_SERVICE_ENTRY_POINT = "/do/accounter/home/rpc/service";
	public final static String REPORT_SERVICE_ENTRY_POINT = "/do/accounter/report/rpc/service";
	public final static String USER_MANAGEMENT_ENTRY_POINT = "/do/accounter/user/rpc/service";

	private static IAccounterCRUDServiceAsync crudService;
	private static IAccounterCompanyInitializationServiceAsync cIService;
	private static IAccounterGETServiceAsync getService;
	private static IAccounterHomeViewServiceAsync homeViewService;
	private static IAccounterReportServiceAsync reportService;

	private static AccounterMessages messages;
	private static AccounterConstants constants;
	private static FinanceImages financeImages;
	private static FinanceMenuImages financeMenuImages;

	private static ThemeImages themeImages;
	private static ClientFinanceDate startDate;

	public void getCompany(String name) {
		final IAccounterGETServiceAsync getService = (IAccounterGETServiceAsync) GWT
				.create(IAccounterGETService.class);
		((ServiceDefTarget) getService)
				.setServiceEntryPoint(Accounter.GET_SERVICE_ENTRY_POINT);

		final AccounterAsyncCallback<ClientCompany> getCompanyCallback = new AccounterAsyncCallback<ClientCompany>() {
			public void onException(AccounterException caught) {
				showError(constants.unableToLoadCompany());
				// //UIUtils.log(caught.toString());
				caught.printStackTrace();
			}

			public void onResultSuccess(ClientCompany company) {
				if (company == null) {
					// TODO Redirect to Companies Servlet
				}

				if (company.getName() == null && company.getID() == 0) {
					// TODO Launch to Company SetUp
				}

				// We got the company, set it for all further references.
				// company.setAccountingType(ClientCompany.ACCOUNTING_TYPE_US);
				Accounter.setCompany(company);

				Accounter.setUser(company.getLoggedInUser());
				startDate = company.getTransactionStartDate();
				endDate = company.getTransactionStartDate();

				// and, now we are ready to start the application.
				removeLoadingImage();
				initGUI();

			}

		};
		getService.getCompany(getCompanyCallback);

	}

	public static ClientUser getUser() {
		return user;
	}

	public static ClientCompany getCompany() {
		return company;
	}

	public static void setUser(ClientUser u) {
		user = u;
	}

	public static void setCompany(ClientCompany c) {
		company = c;
	}

	private void initGUI() {
		setupWizard = new SetupWizard();
		RootPanel.get("mainWindow").add(setupWizard);
	}

	public static void loadAccounter(ClientCompanyPreferences preferences) {
		cIService = createCompanyInitializationService();
		cIService.initalizeCompany(preferences, new AsyncCallback<Boolean>() {
			@Override
			public void onSuccess(Boolean result) {
				if (result) {
					RootPanel.get("mainWindow").remove(setupWizard);
					mainWindow = new MainFinanceWindow();
					RootPanel.get("mainWindow").add(mainWindow);
				}
			}

			@Override
			public void onFailure(Throwable caught) {

			}
		});
	}

	public static IAccounterCompanyInitializationServiceAsync createCompanyInitializationService() {
		if (cIService == null) {
			cIService = (IAccounterCompanyInitializationServiceAsync) GWT
					.create(IAccounterCompanyInitializationService.class);
			((ServiceDefTarget) cIService)
					.setServiceEntryPoint(Accounter.CI_SERVICE_ENTRY_POINT);
		}

		return cIService;

	}

	public static IAccounterCRUDServiceAsync createCRUDService() {
		if (crudService == null) {
			crudService = (IAccounterCRUDServiceAsync) GWT
					.create(IAccounterCRUDService.class);
			((ServiceDefTarget) crudService)
					.setServiceEntryPoint(Accounter.CRUD_SERVICE_ENTRY_POINT);
		}

		return crudService;

	}

	public static IAccounterGETServiceAsync createGETService() {
		if (getService == null) {
			getService = (IAccounterGETServiceAsync) GWT
					.create(IAccounterGETService.class);
			((ServiceDefTarget) getService)
					.setServiceEntryPoint(Accounter.GET_SERVICE_ENTRY_POINT);
		}
		return getService;
	}

	public static IAccounterHomeViewServiceAsync createHomeService() {
		if (homeViewService == null) {
			homeViewService = (IAccounterHomeViewServiceAsync) GWT
					.create(IAccounterHomeViewService.class);
			((ServiceDefTarget) homeViewService)
					.setServiceEntryPoint(Accounter.HOME_SERVICE_ENTRY_POINT);
		}
		return homeViewService;
	}

	public static IAccounterReportServiceAsync createReportService() {
		if (reportService == null) {
			reportService = (IAccounterReportServiceAsync) GWT
					.create(IAccounterReportService.class);
			((ServiceDefTarget) reportService)
					.setServiceEntryPoint(Accounter.REPORT_SERVICE_ENTRY_POINT);
		}
		return reportService;
	}

	public static ClientFinanceDate getStartDate() {
		return startDate;
	}

	public ClientFinanceDate getEndDate() {
		return endDate;
	}

	public static String getAppName() {
		return Accounter.constants().accounter();
	}

	public static AccounterMessages messages() {
		if (messages == null) {
			messages = (AccounterMessages) GWT.create(AccounterMessages.class);
		}
		return messages;
	}

	public static AccounterConstants constants() {
		if (constants == null) {
			constants = (AccounterConstants) GWT
					.create(AccounterConstants.class);
		}
		return constants;
	}

	public static AccounterConstants getFinanceConstants() {
		if (constants == null) {
			constants = (AccounterConstants) GWT
					.create(AccounterConstants.class);
		}
		return constants;
	}

	public static FinanceImages getFinanceImages() {
		if (financeImages == null) {
			financeImages = (FinanceImages) GWT.create(FinanceImages.class);
		}
		return financeImages;
	}

	public static FinanceMenuImages getFinanceMenuImages() {
		if (financeMenuImages == null) {
			financeMenuImages = (FinanceMenuImages) GWT
					.create(FinanceMenuImages.class);
		}
		return financeMenuImages;
	}

	public static ThemeImages getThemeImages() {
		if (themeImages == null) {
			themeImages = (ThemeImages) GWT.create(ThemeImages.class);
		}
		return themeImages;
	}

	@Override
	public void onModuleLoad() {
		eventBus = new SimpleEventBus();
		placeController = new PlaceController(eventBus);
		getCompany("");
	}

	public String getUserDisplayName() {
		return Accounter.getCompany().getDisplayName();
	}

	public String getCompanyName() {
		return Accounter.getCompany().getName();
	}

	public static boolean isLoggedInFromDomain() {
		// TODO Auto-generated method stub
		return false;
	}

	private static CustomDialog expireDialog;

	public enum AccounterType {
		ERROR, WARNING, WARNINGWITHCANCEL, INFORMATION;
	}

	private native void removeLoadingImage() /*-{
		var parent = $wnd.document.getElementById('loadingWrapper');
		parent.style.visibility = 'hidden';
	}-*/;

	/**
	 * 
	 * @param mesg
	 *            Default value:"Warning"
	 * @param mesgeType
	 *            Default value:"Warning"
	 * @param dialogType
	 *            Default OK
	 */
	public static void showError(String msg) {
		new AccounterDialog(msg, AccounterType.ERROR);
	}

	public static void showWarning(String mesg, AccounterType typeOfMesg) {

		new AccounterDialog(mesg, typeOfMesg).show();

	}

	public static void showWarning(String msg, AccounterType typeOfMesg,
			ErrorDialogHandler handler) {

		new AccounterDialog(msg, typeOfMesg, handler).show();
	}

	public static void showInformation(String msg) {

		new AccounterDialog(msg, AccounterType.INFORMATION).show();
	}

	private static EventBus eventBus;

	public static void showMessage(String message) {
		if (expireDialog != null) {
			expireDialog.removeFromParent();
		}
		expireDialog = new CustomDialog();
		expireDialog.setText(Accounter.constants().sessionExpired());
		VerticalPanel vPanel = new VerticalPanel();
		HTML data = new HTML("<p>" + message + "</p");
		data.getElement().getStyle().setMargin(10, Unit.PX);
		data.getElement().getStyle().setFontSize(14, Unit.PX);
		vPanel.add(data);
		Button loginBtn = new Button(constants().logIn());
		loginBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Window.Location.assign("/site/login");
			}
		});
		vPanel.add(loginBtn);
		loginBtn.setEnabled(true);
		loginBtn.getElement().getParentElement().addClassName("expiredButton");
		expireDialog.add(vPanel);
		expireDialog.center();
	}

	public static EventBus getEventBus() {
		return eventBus;
	}

	public static <D extends IAccounterCore> void inviteUser(
			final ISaveCallback source, final D coreObj) {
		final AccounterAsyncCallback<Long> transactionCallBack = new AccounterAsyncCallback<Long>() {

			public void onException(AccounterException caught) {
				source.saveFailed(caught);
				caught.printStackTrace();
				// TODO handle other kind of errors
			}

			public void onResultSuccess(Long result) {
				coreObj.setID(result);
				company.processUpdateOrCreateObject(coreObj);
				source.saveSuccess(coreObj);
			}
		};
		if (coreObj.getID() == 0) {
			Accounter.createCRUDService().inviteUser((IAccounterCore) coreObj,
					Accounter.getUser(), transactionCallBack);
		} else {
			Accounter.createCRUDService().updateUser((IAccounterCore) coreObj,
					transactionCallBack);
		}
		// } else {
		// Accounter.createCRUDService().updateUser((IAccounterCore) coreObj,
		// transactionCallBack);
		// }
	}

	public static <D extends IAccounterCore> void createOrUpdate(
			final ISaveCallback source, final D coreObj) {
		final AccounterAsyncCallback<Long> transactionCallBack = new AccounterAsyncCallback<Long>() {

			public void onException(AccounterException caught) {
				source.saveFailed(caught);
				caught.printStackTrace();
				// TODO handle other kind of errors
			}

			public void onResultSuccess(Long result) {

				coreObj.setID(result);
				company.processUpdateOrCreateObject(coreObj);
				source.saveSuccess(coreObj);
			}

		};
		if (coreObj.getID() == 0) {
			Accounter.createCRUDService().create((IAccounterCore) coreObj,
					transactionCallBack);
		} else {
			Accounter.createCRUDService().update((IAccounterCore) coreObj,
					transactionCallBack);
		}
	}

	public static <D extends IAccounterCore> void deleteUser(
			final IDeleteCallback source, final D data) {
		AccounterAsyncCallback<Boolean> transactionCallBack = new AccounterAsyncCallback<Boolean>() {

			public void onException(AccounterException exception) {
				source.deleteFailed(exception);
			}

			public void onResultSuccess(Boolean result) {
				getCompany().processDeleteObject(data);
				source.deleteSuccess(result);
			}

		};
		Accounter.createCRUDService().deleteUser(data,
				Accounter.getUser().getEmail(), transactionCallBack);
	}

	public static <D extends IAccounterCore> void deleteObject(
			final IDeleteCallback source, final D data) {
		AccounterAsyncCallback<Boolean> transactionCallBack = new AccounterAsyncCallback<Boolean>() {

			public void onException(AccounterException exception) {
				source.deleteFailed(exception);
			}

			public void onResultSuccess(Boolean result) {
				getCompany().processDeleteObject(data);
				source.deleteSuccess(result);
			}

		};
		Accounter.createCRUDService().delete(data.getObjectType(),
				data.getID(), transactionCallBack);
	}

	public static void voidTransaction(final ISaveCallback source,
			final AccounterCoreType coreType, final long transactionsID) {

		// currentrequestedWidget = widget;
		AccounterAsyncCallback<Boolean> callback = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onException(AccounterException exception) {
				getCompany().processCommand(exception);
			}

			@Override
			public void onResultSuccess(Boolean result) {

				if (result) {
					AccounterCommand cmd = new AccounterCommand();
					cmd.setCommand(AccounterCommand.UPDATION_SUCCESS);
					cmd.setData(null);
					cmd.setID(transactionsID);
					cmd.setObjectType(coreType);
					getCompany().processUpdateOrCreateObject(cmd);
					// FIXME We may need to pass the actual object or we need
					// the interface to know that we will not give any object
					source.saveSuccess(cmd);
				}

			}
		};
		Accounter.createCRUDService().voidTransaction(coreType, transactionsID,
				callback);
	}

	public static void updateCompany(final ISaveCallback callback,
			final ClientCompany clientCompany) {

		AccounterAsyncCallback<Long> transactionCallBack = new AccounterAsyncCallback<Long>() {

			public void onException(AccounterException caught) {

				if (caught instanceof AccounterException) {
					callback.saveFailed(caught);
				}
			}

			public void onResultSuccess(Long result) {

				if (result != null) {
					getCompany().processUpdateOrCreateObject(clientCompany);
				}
				callback.saveSuccess(clientCompany);
			}

		};
		Accounter.createCRUDService().updateCompany(clientCompany,
				transactionCallBack);

	}

	public static PlaceController placeController() {
		return placeController;
	}
}
