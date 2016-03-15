package com.everhomes.repeat;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.rest.quality.TimeRangeDTO;
import com.everhomes.rest.repeat.RepeatDurationUnit;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Component
public class RepeatServiceImpl implements RepeatService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RepeatServiceImpl.class);
	
	@Autowired
	private RepeatProvider repeatProvider;

	@Override
	public List<TimeRangeDTO> analyzeTimeRange(String timeRange) {

		Gson gson = new Gson();
		List<TimeRangeDTO> ranges = gson.fromJson(timeRange, new TypeToken<List<TimeRangeDTO>>() {}.getType());
		return ranges;
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
}
