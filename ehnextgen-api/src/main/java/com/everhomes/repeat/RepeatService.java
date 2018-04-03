package com.everhomes.repeat;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.rest.repeat.RepeatExpressionDTO;
import com.everhomes.rest.repeat.RepeatSettingsDTO;
import com.everhomes.rest.repeat.TimeRangeDTO;

public interface RepeatService {
	
	List<TimeRangeDTO> analyzeTimeRange(String timeRange);
	Timestamp getEndTimeByAnalyzeDuration(Timestamp startTime, String duration);
	
	void createRepeatSettings(RepeatSettings repeat);
	void updateRepeatSettings(RepeatSettings repeat);
	void deleteRepeatSettingsById(Long id);
	RepeatSettings findRepeatSettingById(Long id);

	List<RepeatExpressionDTO> analyzeExpression(String expression);
	
	List<RepeatExpressionDTO> test();

	boolean repeatSettingStillWork(Long repeatSettingId);
	boolean isRepeatSettingActive(Long repeatSettingId);
	
	String getExecutionFrequency(RepeatSettingsDTO rs);
	String getExecuteStartTime(RepeatSettingsDTO rs);
	String getlimitTime(RepeatSettingsDTO rs);
}
