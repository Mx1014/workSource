// @formatter:off
package com.everhomes.express;

import java.util.List;

import com.everhomes.rest.express.ExpressOwner;

public interface ExpressParamSettingProvider {

	void createExpressParamSetting(ExpressParamSetting expressParamSetting);

	void updateExpressParamSetting(ExpressParamSetting expressParamSetting);

	ExpressParamSetting findExpressParamSettingById(Long id);

	List<ExpressParamSetting> listExpressParamSetting();

	ExpressParamSetting getExpressParamSettingByOwner(int namespaceId, String ownerType, long ownerId);

	void updateExpressBusinessNoteByOwner(ExpressOwner owner, String businessNote);
	
	void updateExpressBusinessNoteFlagByOwner(ExpressOwner owner, Byte businessNoteFlag);

	void updateExpressHotlineFlagByOwner(ExpressOwner owner, Byte hotlineFlag);

}