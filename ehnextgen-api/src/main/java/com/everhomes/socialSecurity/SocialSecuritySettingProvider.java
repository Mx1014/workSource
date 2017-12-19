// @formatter:off
package com.everhomes.socialSecurity;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.rest.socialSecurity.SocialSecurityPaymentDTO;
import com.everhomes.rest.socialSecurity.SsorAfPay;

import java.util.List;

public interface SocialSecuritySettingProvider {

	void createSocialSecuritySetting(SocialSecuritySetting socialSecuritySetting);

	void updateSocialSecuritySetting(SocialSecuritySetting socialSecuritySetting);

	SocialSecuritySetting findSocialSecuritySettingById(Long id);

	List<SocialSecurityPaymentDTO> listSocialSecuritySetting(Long socialSecurityCityId, Long accumulationFundCityId, Long deptId, String keywords, SsorAfPay payFlag, List<Long> detailIds, CrossShardListingLocator locator);

	List<SocialSecuritySetting> listSocialSecuritySetting(Long socialSecurityCityId, Long accumulationFundCityId, Long deptId, String keywords, SsorAfPay payFlag, List<Long> detailIds);

	List<SocialSecuritySetting> listSocialSecuritySetting();
}