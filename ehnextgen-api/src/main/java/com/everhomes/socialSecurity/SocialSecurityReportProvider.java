// @formatter:off
package com.everhomes.socialSecurity;

import com.everhomes.listing.CrossShardListingLocator;

import java.util.List;

public interface SocialSecurityReportProvider {

	void createSocialSecurityReport(SocialSecurityReport socialSecurityReport);

	void updateSocialSecurityReport(SocialSecurityReport socialSecurityReport);

	SocialSecurityReport findSocialSecurityReportById(Long id);

	List<SocialSecurityReport> listSocialSecurityReport();

	void deleteSocialSecurityReports(Long ownerId, String payMonth, Byte isFiled);

	SocialSecurityDepartmentSummary calculateSocialSecurityDepartmentSummary(List<Long> detailIds, String month);

	SocialSecurityReport findSocialSecurityReportByDetailId(Long id, String month);

	List<SocialSecurityReport> listFiledSocialSecurityReport(Long ownerId, String paymentMonth, CrossShardListingLocator locator, int pageSize);

	List<SocialSecurityReport> listSocialSecurityReport(Long ownerId, String payMonth, Byte isFiled);
}