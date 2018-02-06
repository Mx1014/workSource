// @formatter:off
package com.everhomes.socialSecurity;

import java.util.List;

public interface SocialSecurityGroupProvider {

	void createSocialSecurityGroup(SocialSecurityGroup socialSecurityGroup);

	void updateSocialSecurityGroup(SocialSecurityGroup socialSecurityGroup);

	SocialSecurityGroup findSocialSecurityGroupById(Long id);

	List<SocialSecurityGroup> listSocialSecurityGroup();

}