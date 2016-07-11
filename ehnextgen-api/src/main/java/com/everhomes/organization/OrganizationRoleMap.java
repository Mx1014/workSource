// @formatter:off
package com.everhomes.organization;

import com.everhomes.server.schema.tables.pojos.EhOrganizationRoleMap;
import com.everhomes.util.StringHelper;

public class OrganizationRoleMap extends EhOrganizationRoleMap {

	private String roleName;
	
	
	
    public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 8217625138287158696L;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
    
}
