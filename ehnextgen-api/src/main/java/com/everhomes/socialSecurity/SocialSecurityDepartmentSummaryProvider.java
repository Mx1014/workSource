// @formatter:off
package com.everhomes.socialSecurity;

import java.util.List;

public interface SocialSecurityDepartmentSummaryProvider {

	void createSocialSecurityDepartmentSummary(SocialSecurityDepartmentSummary socialSecurityDepartmentSummary);

	void updateSocialSecurityDepartmentSummary(SocialSecurityDepartmentSummary socialSecurityDepartmentSummary);

	SocialSecurityDepartmentSummary findSocialSecurityDepartmentSummaryById(Long id);

	List<SocialSecurityDepartmentSummary> listSocialSecurityDepartmentSummary();

}