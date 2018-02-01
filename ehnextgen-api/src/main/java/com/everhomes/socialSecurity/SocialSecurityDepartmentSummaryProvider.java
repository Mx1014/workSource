// @formatter:off
package com.everhomes.socialSecurity;

import com.everhomes.listing.CrossShardListingLocator;

import java.util.List;

public interface SocialSecurityDepartmentSummaryProvider {

	void createSocialSecurityDepartmentSummary(SocialSecurityDepartmentSummary socialSecurityDepartmentSummary);

	void updateSocialSecurityDepartmentSummary(SocialSecurityDepartmentSummary socialSecurityDepartmentSummary);

	SocialSecurityDepartmentSummary findSocialSecurityDepartmentSummaryById(Long id);

	List<SocialSecurityDepartmentSummary> listSocialSecurityDepartmentSummary();

	void deleteSocialSecurityDptReports(Long ownerId, String month);

	List<SocialSecurityDepartmentSummary> listSocialSecurityDepartmentSummary(Long ownerId, String paymentMonth, CrossShardListingLocator locator, int i);
}