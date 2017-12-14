// @formatter:off
package com.everhomes.rest.socialSecurity;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>socialSecurityDepartmentSummarys: 社保缴费列表 参考{@link com.everhomes.rest.socialSecurity.SocialSecurityDepartmentSummaryDTO}</li>
 * </ul>
 */
public class ListSocialSecurityDepartmentSummarysResponse {

	@ItemType(SocialSecurityDepartmentSummaryDTO.class)
	private List<SocialSecurityDepartmentSummaryDTO> socialSecurityDepartmentSummarys;

	public ListSocialSecurityDepartmentSummarysResponse() {

	}

	public ListSocialSecurityDepartmentSummarysResponse(List<SocialSecurityDepartmentSummaryDTO> socialSecurityDepartmentSummarys) {
		super();
		this.socialSecurityDepartmentSummarys = socialSecurityDepartmentSummarys;
	}

	public List<SocialSecurityDepartmentSummaryDTO> getSocialSecurityDepartmentSummarys() {
		return socialSecurityDepartmentSummarys;
	}

	public void setSocialSecurityDepartmentSummarys(List<SocialSecurityDepartmentSummaryDTO> socialSecurityDepartmentSummarys) {
		this.socialSecurityDepartmentSummarys = socialSecurityDepartmentSummarys;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
