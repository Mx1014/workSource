package com.everhomes.menu;

import java.util.ArrayList;
import java.util.List;

import com.everhomes.acl.WebMenu;

public class WebMenuItem {
	private WebMenu item;
	private List<WebMenu> childs;
	
	public WebMenuItem() {
		childs = new ArrayList<WebMenu>();
		this.item = new WebMenu();
	}

	public WebMenu getItem() {
		return item;
	}

	public List<WebMenu> getChilds() {
		return childs;
	}

}
