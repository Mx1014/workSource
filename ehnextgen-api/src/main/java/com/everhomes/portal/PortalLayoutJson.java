// @formatter:off
package com.everhomes.portal;

import com.everhomes.util.StringHelper;

import java.util.List;

public class PortalLayoutJson {

	private String layoutName;

	private String location;

	private List<PortalItemGroupJson> groups;

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getLayoutName() {
		return layoutName;
	}

	public void setLayoutName(String layoutName) {
		this.layoutName = layoutName;
	}

	public List<PortalItemGroupJson> getGroups() {
		return groups;
	}

	public void setGroups(List<PortalItemGroupJson> groups) {
		this.groups = groups;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}