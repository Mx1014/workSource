// @formatter:off
package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>officialFlag: 是否为官方帖；参考{@link com.everhomes.rest.organization.OfficialFlag}</li>
 * </ul>
 */
public class CheckOfficalPrivilegeResponse {
	private Byte officialFlag;

	public Byte getOfficialFlag() {
		return officialFlag;
	}

	public void setOfficialFlag(Byte officialFlag) {
		this.officialFlag = officialFlag;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
