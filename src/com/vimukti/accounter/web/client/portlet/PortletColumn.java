package com.vimukti.accounter.web.client.portlet;

import java.util.ArrayList;
import java.util.List;

import com.allen_sauer.gwt.dnd.client.drop.VerticalPanelDropController;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.ui.Portlet;

public class PortletColumn extends VerticalPanel {

	private int index;
	private VerticalPanelDropController dropController;
	private List<Portlet> portlets = new ArrayList<Portlet>();

	public PortletColumn(int columnIndex) {
		this.setSpacing(5);
		this.index = columnIndex;
		this.addStyleName("portletColumn");
		this.dropController = new VerticalPanelDropController(this);
	}

	public VerticalPanelDropController getDropController() {
		return dropController;
	}

	public void setDropController(VerticalPanelDropController dropController) {
		this.dropController = dropController;
	}

	public void refreshWidgets() {
		for (Portlet portlet : portlets) {
			portlet.refreshWidget();
		}
	}

	public void addPortlet(Portlet portlet) {
		this.portlets.add(portlet);
		this.add(portlet);
	}

	public List<Portlet> getPortlets() {
		update();
		return portlets;
	}

	/**
	 * 
	 */
	private void update() {
		this.portlets.clear();
		for (Widget child : getChildren()) {
			if (child instanceof Portlet) {
				Portlet portlet = (Portlet) child;
				// Update the index
				portlet.getConfiguration().column = this.index;
				this.portlets.add(portlet);
			}
		}
	}
}
