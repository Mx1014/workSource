// @formatter:off
package com.everhomes.rest.socialSecurity;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>socialSecurityInoutReports: 社保缴费列表 参考{@link com.everhomes.rest.socialSecurity.SocialSecurityInoutReportDTO}</li>
 * </ul>
 */
public class ListSocialSecurityInoutReportsResponse {

	@ItemType(SocialSecurityInoutReportDTO.class)
	private List<SocialSecurityInoutReportDTO> socialSecurityInoutReports;

	public ListSocialSecurityInoutReportsResponse() {

	}

	public ListSocialSecurityInoutReportsResponse(List<SocialSecurityInoutReportDTO> socialSecurityInoutReports) {
		super();
		this.socialSecurityInoutReports = socialSecurityInoutReports;
	}

	public List<SocialSecurityInoutReportDTO> getSocialSecurityInoutReports() {
		return socialSecurityInoutReports;
	}

	public void setSocialSecurityInoutReports(List<SocialSecurityInoutReportDTO> socialSecurityInoutReports) {
		this.socialSecurityInoutReports = socialSecurityInoutReports;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
