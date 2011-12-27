package com.vimukti.accounter.web.client.ui.widgets;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.TextDecoration;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.ui.Accounter;

/**
 * 
 * @author P.Praneeth
 * 
 */

public abstract class BizDecorPanel extends FlexTable {
	private Label TC;
	private AutoFillWidget MC;
	private Image closeImage;
	private Image configImage;
	private Label gotoLabel;

	public BizDecorPanel(String title, String gotoString) {
		setDecorator(title, gotoString);
		this.setStyleName("biz-decor-panel");
		if (title != null) {
			setPanelTitle(title);
		} else {
			this.addStyleName("no-title");
		}

		if (gotoString != null) {
			setGoToAction(title, gotoString);
		}
		setTitleActions();
	}

	public BizDecorPanel(String title, String gotoString, String width) {
		this(title, gotoString);
		TC.getElement().getParentElement().setAttribute("width", width);
	}

	private void setTitleActions() {
		closeImage.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				onClose();
			}
		});

		configImage.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				onConfigure();
			}
		});

	}

	/**
	 * Initiates the layout
	 */
	private void setDecorator(String title, String gotoString) {
		setFirstRow();
		setSecondRow();
		setThirdRow();
		setStyles(title);
	}

	protected void setPortletTitle(String title) {
		TC.setText(title);
	}

	/**
	 * Creates the first row of Panel
	 */
	private void setFirstRow() {
		Label TL = new Label();
		TC = new Label();
		closeImage = new Image(Accounter.getFinanceImages().portletClose());
		configImage = new Image(Accounter.getFinanceImages().portletSettings());
		gotoLabel = new HTML();

		FlexTable titleTable = new FlexTable();
		titleTable.setWidget(0, 0, TC);
		titleTable.setWidget(0, 1, gotoLabel);

		if (canConfigure()) {
			titleTable.setWidget(0, 2, configImage);
			configImage.getElement().getParentElement()
					.addClassName("portlet_config_button");
		}
		if (canClose()) {
			titleTable.setWidget(0, 3, closeImage);
			closeImage.getElement().getParentElement()
					.addClassName("portlet_close_button");

		}
		Label TR = new Label();
		this.setWidget(0, 0, TL);
		this.setWidget(0, 1, titleTable);
		this.setWidget(0, 2, TR);
	}

	private void setGoToAction(String title, String gotoString) {
		gotoLabel.setText(gotoString);
		gotoLabel.addStyleName("goToLink");
		gotoLabel.setTitle(Accounter.messages().clickThisObjToOpen(
				Accounter.messages().link(),
				Accounter.messages().allTransactionDetails(title)));
		gotoLabel.getElement().getStyle()
				.setTextDecoration(getTitleDecoration());
		gotoLabel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				titleClicked();
			}
		});
	}

	/**
	 * Creates the second row of Panel
	 */
	private void setSecondRow() {
		MC = new AutoFillWidget();
		AutoFillWidget c = new AutoFillWidget();
		c.add(MC);

		MC.setStyleName("decor-left");
		this.setWidget(1, 0, c);
		this.getFlexCellFormatter().setColSpan(1, 0, 3);
	}

	/**
	 * Creates the third row of Panel
	 */
	private void setThirdRow() {
		Label BL = new Label();
		Label BC = new Label();
		Label BR = new Label();
		this.setWidget(2, 0, BL);
		this.setWidget(2, 1, BC);
		this.setWidget(2, 2, BR);
	}

	/**
	 * Sets the required styles to cells
	 */
	private void setStyles(String title) {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (i == 1 && j != 0)
					break;
				this.getCellFormatter().setStyleName(i, j, "td" + i + j);
				this.getWidget(i, j).addStyleName("decor" + i + j);
			}
		}
	}

	/**
	 * Sets the panel title.
	 * 
	 * @param title
	 */
	private void setPanelTitle(String title) {
		TC.setText(title);
		TC.addStyleName("panel-title");
	}

	public Cursor getTitleCursor() {
		return Cursor.AUTO;
	}

	public TextDecoration getTitleDecoration() {
		return TextDecoration.UNDERLINE;
	}

	public void titleClicked() {

	}

	/**
	 * Adds the widget to the centre cell
	 * 
	 * @param widget
	 */
	public void setWidget(Widget widget) {
		MC.add(widget);
		widget.addStyleName("main-w");
	}

	/**
	 * Returns the added widget
	 * 
	 * @return
	 */
	public Widget getWidget() {
		return super.getWidget(1, 1);
	}

	@Override
	public void setHeight(String height) {
		if (height.contains("%")) {
			super.setHeight(height);
			MC.setHeight(height);
		} else {
			super.setHeight("");
			int h = Integer.parseInt(height.replace("px", ""));
			if (h > 0) {
				MC.setHeight(h + "px");
			}

		}
	}

	@Override
	public void setWidth(String width) {
		if (width.contains("%")) {
			super.setWidth(width);
		} else {
			super.setWidth("");
			int w = Integer.parseInt(width.replace("px", ""));
			if (w > 0) {
				MC.setWidth(w + "px");
			}

		}
	}

	/**
	 * Behaves same as setWidget()
	 */
	@Override
	public void add(Widget widget) {
		MC.add(widget);
		widget.addStyleName("main-w-finance");
		widget.setWidth("");
	}

	public void doAnimate(final Boolean isMinimizing) {
		if (isMinimizing) {
			MC.setVisible(false);
		} else {
			MC.setVisible(true);
		}
	}

	protected abstract boolean canClose();

	protected abstract boolean canConfigure();

	protected abstract void onClose();

	protected abstract void onConfigure();

	public Label getHeader() {
		return TC;
	}

	public void setHeader(Label tC) {
		TC = tC;
	}

}
