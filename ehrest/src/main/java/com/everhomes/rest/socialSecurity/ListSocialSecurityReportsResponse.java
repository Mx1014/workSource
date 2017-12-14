// @formatter:off
package com.everhomes.rest.socialSecurity;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>socialSecurityReports: 社保缴费列表 参考{@link com.everhomes.rest.socialSecurity.SocialSecurityReportDTO}</li>
 * </ul>
 */
public class ListSocialSecurityReportsResponse {

	@ItemType(SocialSecurityReportDTO.class)
	private List<SocialSecurityReportDTO> socialSecurityReports;

	public ListSocialSecurityReportsResponse() {

	}

	public ListSocialSecurityReportsResponse(List<SocialSecurityReportDTO> socialSecurityReports) {
		super();
		this.socialSecurityReports = socialSecurityReports;
	}

	public List<SocialSecurityReportDTO> getSocialSecurityReports() {
		return socialSecurityReports;
	}

	public void setSocialSecurityReports(List<SocialSecurityReportDTO> socialSecurityReports) {
		this.socialSecurityReports = socialSecurityReports;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
