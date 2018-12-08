// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.discover.ItemType;

/**
 * <ul>查询服务热线
 * <li>phone：服务热线信息{@link AclinkFormValuesDTO}</li>
 * </ul>
 *
 */
public class FaceplusLoginResponse {

	
	@ItemType(AclinkFormValuesDTO.class)
	private AclinkFormValuesDTO phone;

	public AclinkFormValuesDTO getPhone() {
		return phone;
	}

	public void setPhone(AclinkFormValuesDTO phone) {
		this.phone = phone;
	}
}
