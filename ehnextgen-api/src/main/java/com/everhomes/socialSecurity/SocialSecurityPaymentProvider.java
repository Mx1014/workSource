// @formatter:off
package com.everhomes.socialSecurity;

import java.util.List;

public interface SocialSecurityPaymentProvider {

	void createSocialSecurityPayment(SocialSecurityPayment socialSecurityPayment);

	void updateSocialSecurityPayment(SocialSecurityPayment socialSecurityPayment);

	SocialSecurityPayment findSocialSecurityPaymentById(Long id);

	List<SocialSecurityPayment> listSocialSecurityPayment();

}