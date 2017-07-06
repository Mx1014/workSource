package com.everhomes.menu;

import java.util.ArrayList;
import java.util.List;

import com.everhomes.acl.WebMenu;
import com.everhomes.acl.WebMenuPrivilegeProvider;
import com.everhomes.rest.menu.WebMenuType;

public class MenuBuilder {
	private WebMenuItem curr;
	private MenuBuilder parent;
	private WebMenuPrivilegeProvider webMenuProvider;
	private List<WebMenu> allMenus;
	
	public MenuBuilder(WebMenuPrivilegeProvider webMenuProvider, WebMenuItem curr, MenuBuilder parent) {
		if(curr == null) {
			curr = new WebMenuItem();
			curr.getItem().setId(webMenuProvider.nextId());
			curr.getItem().setParentId(0l);
			curr.getItem().setPath(String.format("/%d", curr.getItem().getId()));
			curr.getItem().setLeafFlag((byte)1);
			curr.getItem().setType(WebMenuType.ZUOLIN.getCode());
			
			curr.getItem().setDataType("datatype-test");
			curr.getItem().setIconUrl("url-test");
			curr.getItem().setModuleId(0l);
			curr.getItem().setSortNum(curr.getItem().getId().intValue());
			
			allMenus = new ArrayList<WebMenu>();
			allMenus.add(curr.getItem());
		}
		this.curr = curr;
		this.parent = parent;
		this.webMenuProvider = webMenuProvider;
	}
	
	public WebMenuItem Curr() {
		return this.curr;
	}
	
	public MenuBuilder Child() {
		WebMenuItem item = new WebMenuItem();
		item.getItem().setId(webMenuProvider.nextId());
		item.getItem().setParentId(this.curr.getItem().getId());
		item.getItem().setPath(String.format("%s/%d", this.curr.getItem().getPath(), item.getItem().getId()));
		item.getItem().setLeafFlag((byte)1);
		item.getItem().setType(WebMenuType.ZUOLIN.getCode());
		item.getItem().setDataType("datatype-test");
		item.getItem().setIconUrl("url-test");
		item.getItem().setSortNum(this.curr.getItem().getSortNum() + this.allMenus.size() + 1);
		
		this.curr.getItem().setLeafFlag((byte)0);
		this.curr.getChilds().add(item.getItem());
		this.allMenus.add(item.getItem());
		
		MenuBuilder nb = new MenuBuilder(webMenuProvider, item, this);
		nb.setWebMenus(this.allMenus);
		return nb;
	}
	
	public MenuBuilder Parent() {
		return this.parent;
	}
	
	public MenuBuilder setId(Long id) {
		this.curr.getItem().setId(id);
		return this;
	}
	public MenuBuilder setName(String name) {
		this.curr.getItem().setName(name);
		return this;
	}
	public MenuBuilder setIconUrl(String iconUrl) {
		this.curr.getItem().setIconUrl(iconUrl);
		return this;
	}
	public MenuBuilder setDataType(String dataType) {
		this.curr.getItem().setDataType(dataType);
		return this;
	}
	public MenuBuilder setLeafFlag(Byte leafFlag) {
		this.curr.getItem().setLeafFlag(leafFlag);
		return this;
	}
	public MenuBuilder setParentId(Long parentId) {
		this.curr.getItem().setParentId(parentId);
		return this;
	}
	public MenuBuilder setModuleId(Long moduleId) {
		this.curr.getItem().setModuleId(moduleId);
		return this;
	}
	public MenuBuilder setConditionType(String conditionType) {
		this.curr.getItem().setConditionType(conditionType);
		return this;
	}
	public MenuBuilder setCategory(String category) {
		this.curr.getItem().setCategory(category);
		return this;
	}
	public List<WebMenu> getWebMenus() {
		List<WebMenu> menus = new ArrayList<WebMenu>();
		for(WebMenu m : this.allMenus) {
			if(m.getName() != null && !m.getName().isEmpty()) {
				menus.add(m);
			}
		}
		return menus;
	}
	public void setWebMenus(List<WebMenu> menus) {
		this.allMenus = menus;
	}
}
