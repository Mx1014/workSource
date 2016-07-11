package com.everhomes.repeat;

public interface RepeatProvider {

	void createRepeatSettings(RepeatSettings repeat);
	void deleteRepeatSettingsById(Long id);
	RepeatSettings findRepeatSettingById(Long id);

}
