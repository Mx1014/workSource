package com.everhomes.repeat;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.rest.repeat.RepeatExpressionDTO;
import com.everhomes.rest.repeat.TimeRangeDTO;

public interface RepeatService {
	
	List<TimeRangeDTO> analyzeTimeRange(String timeRange);
	Timestamp getEndTimeByAnalyzeDuration(Timestamp startTime, String duration);
	
	void createRepeatSettings(RepeatSettings repeat);
	void deleteRepeatSettingsById(Long id);
	RepeatSettings findRepeatSettingById(Long id);

	List<RepeatExpressionDTO> analyzeExpression(String expression);
	
	List<RepeatExpressionDTO> test();
	
	boolean isRepeatSettingActive(Long repeatSettingId);
}
