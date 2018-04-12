// @formatter:off
package com.everhomes.socialSecurity;

import com.everhomes.listing.CrossShardListingLocator;

import java.util.List;

public interface SocialSecurityInoutReportProvider {

	void createSocialSecurityInoutReport(SocialSecurityInoutReport socialSecurityInoutReport);

	void updateSocialSecurityInoutReport(SocialSecurityInoutReport socialSecurityInoutReport);

	SocialSecurityInoutReport findSocialSecurityInoutReportById(Long id);

	List<SocialSecurityInoutReport> listSocialSecurityInoutReport();

	void deleteSocialSecurityInoutReportByMonth(Long ownerId, String month, Byte isFIled);

	List<SocialSecurityInoutReport> listFiledSocialSecurityInoutReport(Long ownerId, String paymentMonth, CrossShardListingLocator locator, int i);

	List<SocialSecurityInoutReport> listSocialSecurityInoutReport(Long ownerId, String payMonth, Byte isFiled);
}