// @formatter:off
package com.everhomes.rest.organization;



import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>OrganizationMenu：机构菜单</li>
 * </ul>
 */
public class OrganizationMenuResponse {
	
	
	private OrganizationDTO organizationMenu;

	public OrganizationDTO getOrganizationMenu() {
		return organizationMenu;
	}

	public void setOrganizationMenu(OrganizationDTO organizationMenu) {
		this.organizationMenu = organizationMenu;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
