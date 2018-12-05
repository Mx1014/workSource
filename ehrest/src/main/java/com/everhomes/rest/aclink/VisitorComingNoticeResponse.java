// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.discover.ItemType;

/**
 * <ul>访客来访提示
 * <li>notice：访客来访提示{@link AclinkFormValuesDTO}</li>
 * </ul>
 *
 */
public class VisitorComingNoticeResponse {

	
	@ItemType(AclinkFormValuesDTO.class)
	private AclinkFormValuesDTO notice;

	public AclinkFormValuesDTO getNotice() {
		return notice;
	}

	public void setNotice(AclinkFormValuesDTO phone) {
		this.notice = phone;
	}
}
