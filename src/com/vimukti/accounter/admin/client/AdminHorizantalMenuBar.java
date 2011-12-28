package com.vimukti.accounter.admin.client;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.theme.ThemesUtil;
import com.vimukti.accounter.web.client.ui.CustomMenuBar;
import com.vimukti.accounter.web.client.ui.core.AccounterDOM;

public class AdminHorizantalMenuBar extends HorizontalPanel {

	AccounterMessages messages=Global.get().messages();
	public AdminHorizantalMenuBar() {

		MenuBar menuBar = getAdminMenuBar();
		add(menuBar);
		setStyleName("MENU_BAR_BG");
		AccounterDOM.addStyleToparent(menuBar.getElement(), messages.menuBarParent());

	}

	private MenuBar getAdminMenuBar() {
		MenuBar menuBar = new MenuBar();
		MenuItem menuitem = menuBar.addItem(AdminHomePage.getAdminConstants()
				.users(), getUsersMenu());
		ThemesUtil.insertImageChildToMenuItem(menuBar, menuitem);

		ThemesUtil.insertImageChildToMenuItem(menuBar, menuitem);

		return menuBar;
	}

	private CustomMenuBar getUsersMenu() {

		CustomMenuBar usersMenuBar = getSubMenu();
		usersMenuBar.addItem(AdminHomePage.getNewAdminUserAction());
		usersMenuBar.addItem(AdminHomePage.getAdminUserListAction());
		return usersMenuBar;
	}

	private CustomMenuBar getSubMenu() {
		CustomMenuBar subMenu = new CustomMenuBar();

		return subMenu;
	}
}
