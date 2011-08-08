package com.vimukti.accounter.web.client.ui.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to keep track of what views we have opened last. We will keep the size
 * of this limited;
 * 
 * @author rajesh
 * 
 */
public class HistoryList {
	private int size;
	List<HistoryItem> list = new ArrayList<HistoryItem>();

	public HistoryList(int size) {
		this.size = size;
	}

	public HistoryList() {
		this(10);
	}

	public void add(HistoryItem val) {
		if (list.contains(val)) {
			list.remove(val);
		}
		list.add(val);
		if (list.size() > size) {
			list.remove(0);
		}
	}

	public static class HistoryItem {
		Action action;
		HistoryItem(ParentCanvas<?> view,Action action) {
			this.view = view;
			this.action=action;
		}

		ParentCanvas<?> view;
	}

	public HistoryItem getView(String token) {
		for (HistoryItem item : list) {
			if (item.action.getHistoryToken().equals(token)) {
				return item;
			}
		}
		return null;
	}

	/**
	 * Return previous view
	 * 
	 */

	public HistoryItem previous() {
		list.remove(list.size() - 1);
		if (list.size() > 0) {
			return list.remove(list.size() - 1);
		}else{
			return null;
		}

	}

	public void clear() {
		for (HistoryItem item : list) {
			item.view=null;
		}
	}
}
