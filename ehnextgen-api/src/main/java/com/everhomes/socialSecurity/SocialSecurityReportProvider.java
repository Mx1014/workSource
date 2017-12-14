// @formatter:off
package com.everhomes.socialSecurity;

import java.util.List;

public interface SocialSecurityReportProvider {

	void createSocialSecurityReport(SocialSecurityReport socialSecurityReport);

	void updateSocialSecurityReport(SocialSecurityReport socialSecurityReport);

	SocialSecurityReport findSocialSecurityReportById(Long id);

	List<SocialSecurityReport> listSocialSecurityReport();

}