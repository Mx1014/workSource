// @formatter:off
package com.everhomes.express;

import java.util.List;

public interface ExpressParamSettingProvider {

	void createExpressParamSetting(ExpressParamSetting expressParamSetting);

	void updateExpressParamSetting(ExpressParamSetting expressParamSetting);

	ExpressParamSetting findExpressParamSettingById(Long id);

	List<ExpressParamSetting> listExpressParamSetting();

	ExpressParamSetting getExpressParamSettingByOwner(int namespaceId, String ownerType, long ownerId);

}