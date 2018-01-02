// @formatter:off
package com.everhomes.socialSecurity;

import com.everhomes.listing.CrossShardListingLocator;

import java.util.List;

public interface SocialSecurityPaymentLogProvider {

	void createSocialSecurityPaymentLog(SocialSecurityPaymentLog socialSecurityPaymentLog);

	void updateSocialSecurityPaymentLog(SocialSecurityPaymentLog socialSecurityPaymentLog);

	SocialSecurityPaymentLog findSocialSecurityPaymentLogById(Long id);

	List<SocialSecurityPaymentLog> listSocialSecurityPaymentLog();

	void deleteMonthLog(Long ownerId, String paymentMonth);

	SocialSecurityPaymentLog findAnyOneSocialSecurityPaymentLog(Long ownerId, String paymentMonth);
}