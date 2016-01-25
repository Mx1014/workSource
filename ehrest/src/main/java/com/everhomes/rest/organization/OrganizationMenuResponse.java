// @formatter:off
package com.everhomes.rest.organization;



import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>OrganizationMenu：机构菜单</li>
 * </ul>
 */
public class OrganizationMenuResponse {
	
	
	private OrganizationDTO OrganizationMenu;


	public OrganizationDTO getOrganizationMenu() {
		return OrganizationMenu;
	}

	public void setOrganizationMenu(OrganizationDTO organizationMenu) {
		OrganizationMenu = organizationMenu;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
