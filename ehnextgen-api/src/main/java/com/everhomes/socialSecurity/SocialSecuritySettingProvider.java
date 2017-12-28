// @formatter:off
package com.everhomes.socialSecurity;

import com.everhomes.rest.socialSecurity.AccumOrSocail;
import com.everhomes.rest.socialSecurity.SocialSecurityItemDTO;
import com.everhomes.rest.socialSecurity.SsorAfPay;
import com.everhomes.server.schema.tables.pojos.EhSocialSecuritySettings;

import java.util.List;

public interface SocialSecuritySettingProvider {

	void createSocialSecuritySetting(SocialSecuritySetting socialSecuritySetting);

	void updateSocialSecuritySetting(SocialSecuritySetting socialSecuritySetting);

	SocialSecuritySetting findSocialSecuritySettingById(Long id);

//	List<SocialSecurityPaymentDTO> listSocialSecuritySetting(Long deptId, String keywords, SsorAfPay payFlag, List<Long> detailIds, CrossShardListingLocator locator);

	List<SocialSecuritySetting> listSocialSecuritySetting(Long socialSecurityCityId, Long accumulationFundCityId, Long deptId, String keywords, SsorAfPay payFlag, List<Long> detailIds);

	List<SocialSecuritySetting> listSocialSecuritySetting();

	int setUserCityAndHTByAccumOrSocial(Long detailId, Byte accumOrSocial, Long cityId, String householdType);

	SocialSecuritySetting findSocialSecuritySettingByDetailIdAndItem(Long detailId, SocialSecurityItemDTO itemDTO, Byte accumOrSocial);

	List<SocialSecuritySetting> listSocialSecuritySetting(Long detailId);

	List<SocialSecuritySetting> listSocialSecuritySetting(List<Long> detailIds);

	List<Long> listDetailsByCityId(List<Long> detailIds, Long socialSecurityCityId, byte code);

	SocialSecuritySetting findSocialSecuritySettingByDetailIdAndAOS(Long detailId, AccumOrSocail socail);

	void batchCreateSocialSecuritySetting(List<EhSocialSecuritySettings> settings);
}