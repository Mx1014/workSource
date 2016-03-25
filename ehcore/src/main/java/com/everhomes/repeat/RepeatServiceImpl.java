package com.everhomes.repeat;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.locale.LocaleStringService;
import com.everhomes.rest.repeat.ExpressionDTO;
import com.everhomes.rest.repeat.RangeDTO;
import com.everhomes.rest.repeat.RepeatDurationUnit;
import com.everhomes.rest.repeat.RepeatExpressionDTO;
import com.everhomes.rest.repeat.RepeatServiceErrorCode;
import com.everhomes.rest.repeat.TimeRangeDTO;
import com.everhomes.user.UserContext;
import com.everhomes.util.RuntimeErrorException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Component
public class RepeatServiceImpl implements RepeatService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RepeatServiceImpl.class);
	
	@Autowired
	private RepeatProvider repeatProvider;
	
	@Autowired
	private LocaleStringService localeStringService;

	@Override
	public List<TimeRangeDTO> analyzeTimeRange(String timeRange) {

		Gson gson = new Gson();
		RangeDTO ranges = gson.fromJson(timeRange, new TypeToken<RangeDTO>() {}.getType());
		List<TimeRangeDTO> timeRanges = ranges.getRanges();
		return timeRanges;
	}

	@Override
	public Timestamp getEndTimeByAnalyzeDuration(Timestamp startTime, String duration) {
		
		String dura = duration.substring(0, duration.length()-1);
		String unit = duration.substring(duration.length()-1, duration.length());
		return addPeriod(startTime, Integer.valueOf(dura), unit);
	}

	private Timestamp addPeriod(Timestamp startTime, int period, String unit) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startTime);
		if(RepeatDurationUnit.MINUTE.getCode().equals(unit)) {
			calendar.add(Calendar.MINUTE, period);
		}
		else if(RepeatDurationUnit.HOUR.getCode().equals(unit)) {
			calendar.add(Calendar.HOUR, period);
		}
		else if(RepeatDurationUnit.DAY.getCode().equals(unit)) {
			calendar.add(Calendar.DATE, period);
		}
		else if(RepeatDurationUnit.MONTH.getCode().equals(unit)) {
			calendar.add(Calendar.MONTH, period);
		}
		
		Timestamp time = new Timestamp(calendar.getTimeInMillis());
		
		return time;
	}

	@Override
	public void createRepeatSettings(RepeatSettings repeat) {
		if(repeat != null) {
			repeatProvider.createRepeatSettings(repeat);
		} else {
			if(LOGGER.isInfoEnabled()) {
				 LOGGER.info("The repeat is null, repeat=" + repeat);
			 }
		}
		
	}

	@Override
	public void deleteRepeatSettingsById(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public RepeatSettings findRepeatSettingById(Long id) {
		RepeatSettings repeat = repeatProvider.findRepeatSettingById(id);
		if(repeat == null) {
			LOGGER.error("the repeat which id="+id+" don't exist!");
			throw RuntimeErrorException
					.errorWith(
							RepeatServiceErrorCode.SCOPE,
							RepeatServiceErrorCode.ERROR_REPEAT_SETTING_NOT_EXIST,
							localeStringService.getLocalizedString(
									String.valueOf(RepeatServiceErrorCode.SCOPE),
									String.valueOf(RepeatServiceErrorCode.ERROR_REPEAT_SETTING_NOT_EXIST),
									UserContext.current().getUser().getLocale(),
									"the repeat don't exist!"));
		}
		return repeat;
	}

	@Override
	public List<RepeatExpressionDTO> analyzeExpression(String expression) {
		
		Gson gson = new Gson();
		ExpressionDTO repeat = gson.fromJson(expression, new TypeToken<ExpressionDTO>() {}.getType());
		List<RepeatExpressionDTO> expressions = repeat.getExpression();
		return expressions;
	}

	@Override
	public List<RepeatExpressionDTO> test() {
		RepeatSettings repeat = findRepeatSettingById(4L);
		List<RepeatExpressionDTO> ex = analyzeExpression(repeat.getExpression());
		return ex;
	}
	
}
