package com.everhomes.repeat;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import com.everhomes.rest.repeat.RepeatSettingStatus;
import com.everhomes.rest.repeat.RepeatSettingsDTO;
import com.everhomes.rest.repeat.TimeRangeDTO;
import com.everhomes.user.UserContext;
import com.everhomes.util.DateHelper;
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
		else if(RepeatDurationUnit.WEEK.getCode().equals(unit)) {
			calendar.add(Calendar.DAY_OF_WEEK, period);
		}
		else if(RepeatDurationUnit.MONTH.getCode().equals(unit)) {
			calendar.add(Calendar.MONTH, period);
		}
		else if(RepeatDurationUnit.YEAR.getCode().equals(unit)) {
			calendar.add(Calendar.YEAR, period);
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

	@Override
	public boolean isRepeatSettingActive(Long repeatSettingId) {

		Timestamp now = new Timestamp(DateHelper.currentGMTTime().getTime());
		RepeatSettings repeat = findRepeatSettingById(repeatSettingId);
		LOGGER.info("isRepeatSettingActive: repeatSetting = " + repeat);
		if(repeat.getStatus() == RepeatSettingStatus.ACTIVE.getCode()) {
			if(repeat.getForeverFlag() == 1) {
				Date date = timestampToDate(repeat.getCreateTime());
				List<Integer> differences = getDateDifference(now, new Timestamp(date.getTime()),
						repeat, repeat.getRepeatType());
				LOGGER.info("isRepeatSettingActive: differences = " + differences + "; date = " + date);
				for(Integer difference : differences) {
					if(difference % repeat.getRepeatInterval() == 0 && difference >= 0) {
						return true;
					}
				}
			} else if(repeat.getForeverFlag() == 0) {
				if(repeat.getRepeatInterval() == null || repeat.getRepeatInterval() == 0 || repeat.getStartDate() == null) {
					return false;
				}
				if(repeat.getRepeatCount() == 0) {
					if(repeat.getEndDate() == null) {
						return false;
					} else {
						Timestamp expiredDate = addPeriod(new Timestamp(repeat.getEndDate().getTime()), 1, "d");
						if(expiredDate.after(now)) {
							List<Integer> differences = getDateDifference(now, new Timestamp(repeat.getStartDate().getTime()),
									repeat, repeat.getRepeatType());
							LOGGER.info("isRepeatSettingActive: differences = " + differences + "; startDate = " + repeat.getStartDate());
							for(Integer difference : differences) {
								if(difference % repeat.getRepeatInterval() == 0  && difference >= 0) {
									return true;
								}
							}
						}
					}
					
				} else {
					if(repeat.getRepeatType() == 0) {
						return false;
					} else {
						Timestamp expiredDate = addPeriod(new Timestamp(repeat.getEndDate().getTime()), 1, "d");
						Timestamp repeatEndDate = addPeriod(new Timestamp(repeat.getStartDate().getTime()), 
								repeat.getRepeatCount()*repeat.getRepeatInterval()+1, getRepeatType(repeat.getRepeatType()));
						Timestamp endDate = getEarlyTime(expiredDate, repeatEndDate);
						if(endDate.after(now)) {
							List<Integer> differences = getDateDifference(now, new Timestamp(repeat.getStartDate().getTime()),
									repeat, repeat.getRepeatType());
							LOGGER.info("isRepeatSettingActive: differences = " + differences + "; startDate = " + repeat.getStartDate());
							for(Integer difference : differences) {
								if(difference % repeat.getRepeatInterval() == 0 && difference >= 0) {
									return true;
								}
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	private Timestamp getEarlyTime(Timestamp time1, Timestamp time2) {
		
		Timestamp early = time1;
		if(early.after(time2)) {
			early = time2;
		}
		return early;
	}
	
	private List<Integer> getDateDifference(Timestamp now, Timestamp compareValue, RepeatSettings repeat, int field) {
		Calendar c = Calendar.getInstance();
		c.setTime(now);
		int yearNow = c.get(Calendar.YEAR);
		int monthNow = c.get(Calendar.MONTH);
		int dayWeekNow = c.get(Calendar.DAY_OF_WEEK);
		int dayNow = c.get(Calendar.DATE);
		
		c.setTime(compareValue);
		int yearCompare = c.get(Calendar.YEAR);
		int monthCompare = c.get(Calendar.MONTH);
		
		LOGGER.info("getDateDifference: yearNow = " + yearNow + "; monthNow = " + monthNow + "; dayWeekNow = "
				+ dayWeekNow + "; dayNow = " + dayNow + "; yearCompare = " + yearCompare + "; monthCompare = " + monthCompare);
		List<Integer> results = new ArrayList<Integer>();
		
		if(repeat != null && repeat.getExpression() != null) {
			List<RepeatExpressionDTO> expressionDto =  analyzeExpression(repeat.getExpression());
			int result = -1;
			
			if(field == 1) {
				if(repeat.getEveryWorkdayFlag() != null && repeat.getEveryWorkdayFlag() == 1
						&& (dayWeekNow == Calendar.SUNDAY || dayWeekNow == Calendar.SATURDAY)) {
					result = -1;
				} else {
					result = (int)((now.getTime() - compareValue.getTime())/86400000);
				}

				results.add(result);
			} else if(expressionDto != null && expressionDto.size() > 0) {
				for(RepeatExpressionDTO exp : expressionDto) {
					
					if(field == 0) {
						if(yearNow == exp.getYear() && monthNow == exp.getMonth() && dayNow == exp.getDay()) {
							result = 0;
						}
					}

					if(field == 2) {
						if(dayWeekNow == exp.getDay()) {
							result = (int)((now.getTime() - compareValue.getTime())/604800000);
						} else {
							result = -1;
						}
					}
					
					if(field == 3) {
						if(dayNow == exp.getDay()) {
							if(yearNow == yearCompare) {
								result = monthNow - monthCompare;
							} else {
								result = 12*(yearNow - yearCompare) + monthNow - monthCompare;
							}
							
						} else {
							result = -1;
						}
					}

					if(field == 4) {
						if(dayNow == exp.getDay() && monthNow == exp.getMonth()) {
							result = yearNow - yearCompare;
						} else {
							result = -1;
						}
					}

					results.add(result);
				}
			}

		}
		
		return results;
	}
	
	private String getRepeatType(Byte repeatType) {
		
		String repeat = "";
		
		switch(repeatType) {
		case 1:
			repeat = "d";
			break; 
		case 2:
			repeat = "W";
			break;
		case 3:
			repeat = "M";
			break;
		case 4:
			repeat = "Y";
			break;
		}
		
		return repeat;
	}
	
	private Date timestampToDate(Timestamp time) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String str = sdf.format(time);
		Date date = new Date();
		try {
			date = sdf.parse(str);
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return date;
	}

	@Override
	public String getExecutionFrequency(RepeatSettingsDTO rs) {
		List<RepeatExpressionDTO> expressionDto =  analyzeExpression(rs.getExpression());
		List<TimeRangeDTO> timeRanges = analyzeTimeRange(rs.getTimeRanges());
		
		StringBuilder sb = new StringBuilder();
		switch(rs.getRepeatType()) {
		case 0:
			sb.append("按次");
		case 1:
			sb.append("每" + rs.getRepeatInterval() + "天");
			if(timeRanges != null) {
				sb.append(timeRanges.size() + "次");
			}
			break; 
		case 2:
			sb.append("每" + rs.getRepeatInterval() + "周");
			if(expressionDto != null) {
				sb.append(expressionDto.size() * timeRanges.size() + "次");
			}
			break;
		case 3:
			sb.append("每" + rs.getRepeatInterval() + "月");
			if(expressionDto != null) {
				sb.append(expressionDto.size() * timeRanges.size() + "次");
			}
			break;
		case 4:
			sb.append("每" + rs.getRepeatInterval() + "年");
			if(expressionDto != null) {
				sb.append(expressionDto.size() * timeRanges.size() + "次");
			}
			break;
		}
		
		return sb.toString();
	}

	@Override
	public String getExecuteStartTime(RepeatSettingsDTO rs) {
		List<RepeatExpressionDTO> expressionDto =  analyzeExpression(rs.getExpression());
		List<TimeRangeDTO> timeRanges = analyzeTimeRange(rs.getTimeRanges());
		
		StringBuilder sb = new StringBuilder();
		switch(rs.getRepeatType()) {
		case 0:
			if(expressionDto != null) {
				for(RepeatExpressionDTO dto : expressionDto) {
					if(sb.length() != 0)
						sb.append(", ");
					
					sb.append(dto.getYear() + "年" + (dto.getMonth()+1) + "月" + dto.getDay() + "日");
				}
			}
			
		case 1:
			if(timeRanges != null) {
				for(TimeRangeDTO dto : timeRanges) {
					if(sb.length() != 0)
						sb.append(", ");
					
					sb.append(dto.getStartTime());
				}
			}
			break; 
		case 2:
			if(expressionDto != null) {
				for(RepeatExpressionDTO dto : expressionDto) {
					if(sb.length() != 0)
						sb.append(", ");
					
					if(dto.getDay() == 1) {
						sb.append("周日");
					} else {
						sb.append("周" + (dto.getDay()-1));
					}
					
				}
			}
			break;
		case 3:
			if(expressionDto != null) {
				for(RepeatExpressionDTO dto : expressionDto) {
					if(sb.length() != 0)
						sb.append(", ");
					
					sb.append("每月" + dto.getDay() + "号");
				}
			}
			break;
		case 4:
			if(expressionDto != null) {
				for(RepeatExpressionDTO dto : expressionDto) {
					if(sb.length() != 0)
						sb.append(", ");
					
					sb.append("每年" + (dto.getMonth()+1) + "月" + dto.getDay() + "日");
				}
			}
			break;
		}
		
		return sb.toString();
	}

	@Override
	public String getlimitTime(RepeatSettingsDTO rs) {
		List<TimeRangeDTO> timeRanges = analyzeTimeRange(rs.getTimeRanges());
		StringBuilder sb = new StringBuilder();
		for(TimeRangeDTO dto : timeRanges) {
			if(sb.length() != 0)
				sb.append(", ");
			
			String duration = dto.getDuration();
			String gap = duration.substring(0, duration.length()-1);
			sb.append(gap);
			String unit = duration.substring(duration.length()-1, duration.length());
			
			if(RepeatDurationUnit.MINUTE.getCode().equals(unit)) {
				sb.append("分钟");
			}
			else if(RepeatDurationUnit.HOUR.getCode().equals(unit)) {
				sb.append("小时");
			}
			else if(RepeatDurationUnit.DAY.getCode().equals(unit)) {
				sb.append("天");
			}
			else if(RepeatDurationUnit.WEEK.getCode().equals(unit)) {
				sb.append("周");
			}
			else if(RepeatDurationUnit.MONTH.getCode().equals(unit)) {
				sb.append("月");
			}
			else if(RepeatDurationUnit.YEAR.getCode().equals(unit)) {
				sb.append("年");
			}
		}
		return sb.toString();
	}
	
}
