// @formatter:off
package com.everhomes.socialSecurity;

import java.util.List;

public interface SocialSecurityPaymentLogProvider {

	void createSocialSecurityPaymentLog(SocialSecurityPaymentLog socialSecurityPaymentLog);

	void updateSocialSecurityPaymentLog(SocialSecurityPaymentLog socialSecurityPaymentLog);

	SocialSecurityPaymentLog findSocialSecurityPaymentLogById(Long id);

	List<SocialSecurityPaymentLog> listSocialSecurityPaymentLog();

}