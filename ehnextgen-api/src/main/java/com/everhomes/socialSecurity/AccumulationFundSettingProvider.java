// @formatter:off
package com.everhomes.socialSecurity;

import java.util.List;

public interface AccumulationFundSettingProvider {

	void createAccumulationFundSetting(AccumulationFundSetting accumulationFundSetting);

	void updateAccumulationFundSetting(AccumulationFundSetting accumulationFundSetting);

	AccumulationFundSetting findAccumulationFundSettingById(Long id);

	List<AccumulationFundSetting> listAccumulationFundSetting();

}