// @formatter:off
package com.everhomes.rest.socialSecurity;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>socialSecuritySummarys: 社保缴费汇总列表 参考{@link com.everhomes.rest.socialSecurity.SocialSecuritySummaryDTO}</li>
 * </ul>
 */
public class ListSocialSecurityHistoryFilesResponse {

	@ItemType(SocialSecuritySummaryDTO.class)
	private List<SocialSecuritySummaryDTO> socialSecuritySummarys;

	public ListSocialSecurityHistoryFilesResponse() {

	}

	public ListSocialSecurityHistoryFilesResponse(List<SocialSecuritySummaryDTO> socialSecuritySummarys) {
		super();
		this.socialSecuritySummarys = socialSecuritySummarys;
	}

	public List<SocialSecuritySummaryDTO> getSocialSecuritySummarys() {
		return socialSecuritySummarys;
	}

	public void setSocialSecuritySummarys(List<SocialSecuritySummaryDTO> socialSecuritySummarys) {
		this.socialSecuritySummarys = socialSecuritySummarys;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
