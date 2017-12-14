// @formatter:off
package com.everhomes.socialSecurity;

import java.util.List;

public interface SocialSecurityInoutReportProvider {

	void createSocialSecurityInoutReport(SocialSecurityInoutReport socialSecurityInoutReport);

	void updateSocialSecurityInoutReport(SocialSecurityInoutReport socialSecurityInoutReport);

	SocialSecurityInoutReport findSocialSecurityInoutReportById(Long id);

	List<SocialSecurityInoutReport> listSocialSecurityInoutReport();

}