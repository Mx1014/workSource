// @formatter:off
package com.everhomes.rest.ui.privilege;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>entrancePrivilege: 入口标识，{@link com.everhomes.rest.ui.privilege.EntrancePrivilege}</li>
 * </ul>
 */
public class GetEntranceByPrivilegeResponse {
	
    private String entrancePrivilege;

    public String getEntrancePrivilege() {
		return entrancePrivilege;
	}

	public void setEntrancePrivilege(String entrancePrivilege) {
		this.entrancePrivilege = entrancePrivilege;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
