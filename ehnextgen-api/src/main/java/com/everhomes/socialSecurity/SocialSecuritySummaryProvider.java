// @formatter:off
package com.everhomes.socialSecurity;

import java.util.List;

public interface SocialSecuritySummaryProvider {

	void createSocialSecuritySummary(SocialSecuritySummary socialSecuritySummary);

	void updateSocialSecuritySummary(SocialSecuritySummary socialSecuritySummary);

	SocialSecuritySummary findSocialSecuritySummaryById(Long id);

	List<SocialSecuritySummary> listSocialSecuritySummary();

}