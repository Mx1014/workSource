// @formatter:off
package com.everhomes.socialSecurity;

import java.util.List;

public interface SocialSecuritySettingProvider {

	void createSocialSecuritySetting(SocialSecuritySetting socialSecuritySetting);

	void updateSocialSecuritySetting(SocialSecuritySetting socialSecuritySetting);

	SocialSecuritySetting findSocialSecuritySettingById(Long id);

	List<SocialSecuritySetting> listSocialSecuritySetting();

}