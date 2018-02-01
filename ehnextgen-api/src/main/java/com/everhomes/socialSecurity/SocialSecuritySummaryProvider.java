// @formatter:off
package com.everhomes.socialSecurity;

import com.everhomes.listing.CrossShardListingLocator;

import java.util.List;

public interface SocialSecuritySummaryProvider {

	void createSocialSecuritySummary(SocialSecuritySummary socialSecuritySummary);

	void updateSocialSecuritySummary(SocialSecuritySummary socialSecuritySummary);

	SocialSecuritySummary findSocialSecuritySummaryById(Long id);

	List<SocialSecuritySummary> listSocialSecuritySummary();

	void deleteSocialSecuritySummary(Long ownerId, String paymentMonth);

	List<SocialSecuritySummary> listSocialSecuritySummary(Long ownerId, String paymentMonth, CrossShardListingLocator locator, int i);
}