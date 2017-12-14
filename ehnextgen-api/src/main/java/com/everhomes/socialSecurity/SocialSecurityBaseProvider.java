// @formatter:off
package com.everhomes.socialSecurity;

import java.util.List;

public interface SocialSecurityBaseProvider {

	void createSocialSecurityBase(SocialSecurityBase socialSecurityBase);

	void updateSocialSecurityBase(SocialSecurityBase socialSecurityBase);

	SocialSecurityBase findSocialSecurityBaseById(Long id);

	List<SocialSecurityBase> listSocialSecurityBase();

}