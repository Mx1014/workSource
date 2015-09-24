package com.everhomes.techpark.punch;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.spatial.geohash.GeoHashUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.naming.NameMapper;
import com.everhomes.organization.pm.pay.GsonUtil;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.EhPunchRules;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Service
public class PunchServiceImpl implements PunchService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(PunchServiceImpl.class);

	@Autowired
	PunchProvider punchProvider;
 
	private void checkCompanyIdIsNull(Long companyId) {
		if (companyId == null || companyId.equals(0L)) {
			LOGGER.error("Invalid company Id parameter in the command");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid company Id parameter in the command");
		}

	}

	@Override
	public PunchLogsYearListResponse getlistPunchLogs(ListPunchLogsCommand cmd) {
		
		checkCompanyIdIsNull(cmd.getCompanyId());
		if (cmd.getQueryYear() == null || cmd.getQueryYear().isEmpty())
			throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid queryYear parameter in the command");

		
		PunchLogsYearListResponse pyl = new PunchLogsYearListResponse();
		pyl.setPunchYear(cmd.getQueryYear());
		pyl.setPunchLogsMonthList(new ArrayList<PunchLogsMonthList>());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		try {
			// 从年初开始，如果是查询今年，到今天截止，如果不是查询今年，则到该年年末
			// 如果要修改，只需要修改范围即可
			start.setTime(format.parse(cmd.getQueryYear() + "-01-01"));
			if (end.before(start)) {
				throw RuntimeErrorException.errorWith(
						PunchServiceErrorCode.SCOPE,
						PunchServiceErrorCode.ERROR_QUERY_YEAR_ERROR,
						"query Year is later than now ,please check again ");
			}
			if (start.get(Calendar.YEAR) != end.get(Calendar.YEAR)) {
				end.setTime(format.parse(cmd.getQueryYear() + "-01-01"));
				end.add(Calendar.YEAR, 1);
			}
		} catch (ParseException e) {
			throw RuntimeErrorException
					.errorWith(PunchServiceErrorCode.SCOPE,
							PunchServiceErrorCode.ERROR_QUERY_YEAR_ERROR,
							"there is something wrong with queryYear,please check again ");
		}
//		List<java.sql.Date> dateList = punchProvider.listPunchLogsBwteenTwoDay(
//				userId, cmd.getCompanyId(), dateSF.format(start.getTime()),
//				dateSF.format(end.getTime()));
		
		pyl = getlistPunchLogsBetweenTwoCalendar(pyl, cmd.getCompanyId(),start, end);
		return pyl;
	}
	@Override
	public PunchLogsYearListResponse getlistPunchLogsBetweenTwoCalendar(PunchLogsYearListResponse pyl,long CompanyId ,Calendar start,Calendar end){
		Long userId = UserContext.current().getUser().getId();
		SimpleDateFormat dateSF = new SimpleDateFormat("yyyy-MM-dd");
		PunchRule punchRule = punchProvider.getPunchRuleByCompanyId(CompanyId);
		if (null == punchRule)
			throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid company Id parameter in the command ");
		PunchLogsMonthList pml = null;
		while (start.before(end)){
			
			Date date = start.getTime();
//			start.setTime(date);
			//if not workday continue
			
			
			if (null == pml) {
				// 如果pml为空，即是循环第一次，建立新的pml
				pml = new PunchLogsMonthList();
				pml.setPunchMonth(String.valueOf(start.get(Calendar.MONTH) + 1));
				pml.setPunchLogsDayListInfos(new ArrayList<PunchLogsDayList>());
				pyl.getPunchLogsMonthList().add(pml);
			} else if (!pml.getPunchMonth().equals(
					String.valueOf(start.get(Calendar.MONTH) + 1))) {
				// 如果pml的月份和start不一样，建立新的pml
				pml = new PunchLogsMonthList();
				pml.setPunchMonth(String.valueOf(start.get(Calendar.MONTH) + 1));
				pml.setPunchLogsDayListInfos(new ArrayList<PunchLogsDayList>());
				pyl.getPunchLogsMonthList().add(pml);
			}
			PunchLogsDayList pdl = new PunchLogsDayList();
			pdl.setPunchDay(String.valueOf(start.get(Calendar.DAY_OF_MONTH)));
			pdl.setPunchLogs(new ArrayList<PunchLogDTO>());
			
			if(!isWorkDay(date)){ 
				//如果非工作日，不增pdl直接下一天
				start.add(Calendar.DAY_OF_MONTH, 1);
				continue;  
			}
			List<PunchLog> punchLogs = punchProvider.listPunchLogsByDate(
					userId, CompanyId, dateSF.format(start.getTime()) ,ClockCode.SUCESS.getCode());

			
			if (null == punchLogs || punchLogs.size() == 0) {
				PunchLogDTO noPunchLogDTO1 = new PunchLogDTO();
				noPunchLogDTO1.setClockStatus(ClockStatus.ARRIVE.getCode());
				noPunchLogDTO1.setPunchStatus(PunchStatus.UNPUNCH.getCode());
				pdl.getPunchLogs().add(noPunchLogDTO1);
				PunchLogDTO noPunchLogDTO2 = new PunchLogDTO();
				noPunchLogDTO2.setClockStatus(ClockStatus.LEAVE.getCode());
				noPunchLogDTO2.setPunchStatus(PunchStatus.UNPUNCH.getCode());
				pdl.getPunchLogs().add(noPunchLogDTO2);
				pml.getPunchLogsDayList().add(pdl);
				start.add(Calendar.DAY_OF_MONTH, 1);
				continue;
			}
			try {
				pml.getPunchLogsDayList().add(
						makePunchLogsDayListInfo(punchLogs, punchRule, pdl));
			} catch (ParseException e) {

				throw RuntimeErrorException.errorWith(
						PunchServiceErrorCode.SCOPE,
						ErrorCodes.ERROR_INVALID_PARAMETER,
						"punch Rule has somthing wrong");
			}
			start.add(Calendar.DAY_OF_MONTH, 1);
		}
		return pyl;
	}
	private PunchLogsDayList makePunchLogsDayListInfo(List<PunchLog> punchLogs,
			PunchRule punchRule, PunchLogsDayList pdl) throws ParseException {

		SimpleDateFormat timeSF = new SimpleDateFormat("HH:mm:ss");
		SimpleDateFormat dateSF = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat datetimeSF = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		Calendar startMinTime = Calendar.getInstance();
		Calendar startMaxTime = Calendar.getInstance();
		Calendar workTime = Calendar.getInstance();
		startMinTime.setTime(datetimeSF.parse(dateSF.format(punchLogs.get(0)
				.getPunchDate())
				+ " "
				+ timeSF.format(punchRule.getStartEarlyTime())));

		startMaxTime.setTime(datetimeSF.parse(dateSF.format(punchLogs.get(0)
				.getPunchDate())
				+ " "
				+ timeSF.format(punchRule.getStartLateTime())));

		workTime.setTime(datetimeSF.parse(dateSF.format(punchLogs.get(0)
				.getPunchDate()) + " " + timeSF.format(punchRule.getWorkTime())));
		if (punchLogs.size() == 0) {
			// 未打卡
			pdl.setPunchStatus(PunchStatus.UNPUNCH.getCode());
		} else {
			PunchLogDTO arrivePunchLogDTO = new PunchLogDTO();
			PunchLogDTO leavePunchLogDTO = new PunchLogDTO();
			arrivePunchLogDTO.setPunchStatus(ClockStatus.ARRIVE.getCode());
			leavePunchLogDTO.setPunchStatus(ClockStatus.LEAVE.getCode());
			List<Calendar> punchMinAndMaxTime = getMinAndMaxTimeFromPunchlogs(punchLogs);
			Calendar arriveCalendar = punchMinAndMaxTime.get(0);
			Calendar leaveCalendar = punchMinAndMaxTime.get(1);
			arrivePunchLogDTO.setPunchTime(arriveCalendar.getTime().getTime());
			leavePunchLogDTO.setPunchTime(leaveCalendar.getTime().getTime());

			// 先设开始状态 只要早于最晚上班时间就可以
			if (punchMinAndMaxTime.get(0).before(startMaxTime)) {
				arrivePunchLogDTO.setPunchStatus(PunchStatus.NORMAL.getCode());
			} else {
				arrivePunchLogDTO.setPunchStatus(PunchStatus.BELATE.getCode());
				pdl.setPunchStatus(PunchStatus.BELATE.getCode());
			}
			// 再设离开状态 ：
			// 1.晚于最晚下班时间为正常，2.判断上班时间是否早于最早上班时间，如果是，则晚于最早下班时间为正常，如果不是，则晚于上班时间+工作时长
			startMinTime.add(Calendar.HOUR, workTime.get(Calendar.HOUR));
			startMinTime.add(Calendar.MINUTE, workTime.get(Calendar.MINUTE));
			startMinTime.add(Calendar.SECOND, workTime.get(Calendar.SECOND));
			startMaxTime.add(Calendar.HOUR, workTime.get(Calendar.HOUR));
			startMaxTime.add(Calendar.MINUTE, workTime.get(Calendar.MINUTE));
			startMaxTime.add(Calendar.SECOND, workTime.get(Calendar.SECOND));
			arriveCalendar.add(Calendar.HOUR, workTime.get(Calendar.HOUR));
			arriveCalendar.add(Calendar.MINUTE, workTime.get(Calendar.MINUTE));
			arriveCalendar.add(Calendar.SECOND, workTime.get(Calendar.SECOND));
			if (leaveCalendar.after(startMaxTime)) {
				leavePunchLogDTO.setPunchStatus(PunchStatus.NORMAL.getCode());
			} else {
				if (arriveCalendar.after(startMinTime)) {
					if (leaveCalendar.after(arriveCalendar)) {
						leavePunchLogDTO.setPunchStatus(PunchStatus.NORMAL
								.getCode());
					} else {
						leavePunchLogDTO.setPunchStatus(PunchStatus.LEAVEEARLY
								.getCode());
						pdl.setPunchStatus(PunchStatus.LEAVEEARLY.getCode());
					}
				} else {
					if (leaveCalendar.after(startMinTime)) {
						leavePunchLogDTO.setPunchStatus(PunchStatus.NORMAL
								.getCode());
					} else {
						leavePunchLogDTO.setPunchStatus(PunchStatus.LEAVEEARLY
								.getCode());
						pdl.setPunchStatus(PunchStatus.LEAVEEARLY.getCode());
					}
				}
			}
			pdl.getPunchLogs().add(leavePunchLogDTO);
			pdl.getPunchLogs().add(arrivePunchLogDTO);
		}

		return pdl;
	}

	private List<Calendar> getMinAndMaxTimeFromPunchlogs(
			List<PunchLog> punchLogs) {
		List<Calendar> result = new ArrayList<Calendar>();
		Calendar logCalendar = Calendar.getInstance();
		Calendar maxCalendar = Calendar.getInstance();
		Calendar minCalendar = Calendar.getInstance();
		maxCalendar.setTime(punchLogs.get(0).getPunchTime());
		minCalendar.setTime(punchLogs.get(0).getPunchTime());
		if (punchLogs.size() != 1) {

			for (PunchLog punchlog : punchLogs) {
				logCalendar.setTime(punchlog.getPunchTime());
				if (logCalendar.before(minCalendar))
					minCalendar = logCalendar;
				if (logCalendar.after(maxCalendar))
					maxCalendar = logCalendar;

			}
		}
		result.add(minCalendar);
		result.add(maxCalendar);
		return result;
	}

	@Override
	public PunchClockResponse createPunchLog(PunchClockCommand cmd) {

		checkCompanyIdIsNull(cmd.getCompanyId());
		if (cmd.getLatitude() == null || cmd.getLatitude().equals(0))
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid Latitude parameter in the command");
		if (cmd.getLongitude() == null || cmd.getLongitude().equals(0))
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid Longitude parameter in the command");
		PunchClockResponse request = new PunchClockResponse();
		Long userId = UserContext.current().getUser().getId();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		String punchTime = df.format(new Date());// new Date()为获取当前系统时间为打卡时间
		SimpleDateFormat datetimeSF = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat dateSF = new SimpleDateFormat("yyyy-MM-dd");
		PunchLog punchLog = new PunchLog();
		punchLog.setCompanyId(cmd.getCompanyId());
		punchLog.setUserId(userId);
		punchLog.setPunchTime(Timestamp.valueOf(punchTime));
		punchLog.setLatitude(cmd.getLatitude());
		punchLog.setLongitude(cmd.getLongitude());
		request.setPunchCode(verifyCompanyGoePoints(cmd).getCode());
		punchLog.setPunchStatus(request.getPunchCode());
		Calendar punCalendar = Calendar.getInstance();
		try {
			punCalendar.setTime(datetimeSF.parse(punchTime));
		} catch (ParseException e) {

			e.printStackTrace();
		}
		if (punCalendar.get(Calendar.HOUR_OF_DAY) < 5) {
			// 如果打卡时间小于5，用昨天
			punCalendar.add(Calendar.DATE, -1);
		}
		punchLog.setPunchDate(java.sql.Date.valueOf(dateSF.format(punCalendar
				.getTime())));
		punchProvider.createPunchLog(punchLog);
		request.setPunchTime(punchTime);

		return request;
	}

	private ClockCode verifyCompanyGoePoints(PunchClockCommand cmd) { 
		ClockCode code = ClockCode.NOTINAREA;
		List<PunchGeopoint> punchGeopoints = punchProvider
				.listPunchGeopointsByCompanyId(cmd.getCompanyId());
		if (punchGeopoints.size() == 0)
			throw RuntimeErrorException
					.errorWith(ErrorCodes.SCOPE_GENERAL,
							ErrorCodes.ERROR_INVALID_PARAMETER,
							"Invalid companyId parameter in the command:companyId has no geo point");
		for (PunchGeopoint punchGeopoint : punchGeopoints) {
			if (caculateDistance(cmd.getLongitude(), cmd.getLatitude(),
					punchGeopoint.getLongitude(), punchGeopoint.getLatitude()) <= punchGeopoint
					.getDistance()) {
				// 如果找到了一个就跳出
				code = ClockCode.SUCESS;
				break;
			}
		}

		return code;
	}

	/*** return 两个坐标之间的距离 单位 米 */
	private double caculateDistance(double longitude1, double latitude1,
			double longitude2, double latitude2) {
		double radianLat1 = angle2Radian(latitude1);
		double radianLat2 = angle2Radian(latitude2);
		double differenceLat = radianLat1 - radianLat2;
		double differenceLng = angle2Radian(longitude1)
				- angle2Radian(longitude2);

		double s = 2 * Math.asin(Math.sqrt(Math.pow(
				Math.sin(differenceLat / 2), 2)
				+ Math.cos(radianLat1)
				* Math.cos(radianLat2)
				* Math.pow(Math.sin(differenceLng / 2), 2)));
		s = s * 6378137.0D;
		s = Math.round(s * 10000) / 10000;

		return s;
	}

	private double angle2Radian(double angle) {
		return angle * Math.PI / 180.0;
	}

	@Override
	public void createPunchRule(CreatePunchRuleCommand cmd) {
		Long userId = UserContext.current().getUser().getId();
		checkCompanyIdIsNull(cmd.getCompanyId());
		PunchRule punchRule = punchProvider.findPunchRuleByCompanyId(cmd
				.getCompanyId());
		String startEarlyTime = cmd.getStartEarlyTime();
		String startLastTime = cmd.getStartLateTime();
		String endEarlyTime = cmd.getEndEarlyTime();

		if (punchRule == null) {
			punchRule = ConvertHelper.convert(cmd, PunchRule.class);
			 
			convertTime(punchRule, startEarlyTime, startLastTime, endEarlyTime);
			punchRule.setCreatorUid(userId);
			punchRule.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
					.getTime()));
			punchProvider.createPunchRule(punchRule);
			createPunchGeopoints(userId,cmd.getLocations(),cmd.getCompanyId());
			
		}

	}

	private void createPunchGeopoints(Long userId, String locations,Long compantId) {
		JSONObject jsonObject = (JSONObject) JSONValue.parse(locations);
		JSONArray locationValue = (JSONArray) jsonObject.get("locations");
		Gson  gson = new Gson();
		List<PunchGeopoint> geopoints = gson.fromJson(locationValue.toString(), new TypeToken<List<PunchGeopoint>>(){}.getType());
		for (PunchGeopoint punchGeopoint : geopoints) {
			punchGeopoint.setCompanyId(compantId);
			punchGeopoint.setCreatorUid(userId);
			punchGeopoint.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
					.getTime()));
			punchGeopoint.setGeohash(GeoHashUtils.encode(punchGeopoint.getLatitude(), punchGeopoint.getLongitude()));
			punchProvider.createPunchGeopoint(punchGeopoint);
		}
	}

	private void convertTime(PunchRule punchRule, String startEarlyTime,
			String startLastTime, String endEarlyTime) {
		Time startEarly = convertTime(startEarlyTime);
		punchRule.setStartEarlyTime(startEarly);
		punchRule.setStartLateTime(convertTime(startLastTime));
		Time endEarly = convertTime(endEarlyTime);
		Long workTime = endEarly.getTime() - startEarly.getTime();
//		long hours = (workTime/(1000* 60 * 60));
//		long minutes = (workTime-hours*(1000* 60 * 60))/(1000* 60);
		String workTimeStr = getGMTtimeString("HH:mm:ss", workTime);
		punchRule.setWorkTime(convertTime(workTimeStr));
	}
	
	public String getGMTtimeString(String dateFormat,long time){
		DateFormat format = new SimpleDateFormat(dateFormat);
		format.setTimeZone(TimeZone.getTimeZone("GMT"));//设置 DateFormat的时间区域为GMT
		Date date = new Date(time);
		return format.format(date);
	}
	
	private Time convertTime(String TimeStr) {
		if (!StringUtils.isEmpty(TimeStr)) {
			return Time.valueOf(TimeStr);
		}
		return null;
	}

	@Override
	public void updatePunchRule(UpdatePunchRuleCommand cmd) {
		Long userId = UserContext.current().getUser().getId();
		checkCompanyIdIsNull(cmd.getCompanyId());
		PunchRule punchRule = punchProvider.findPunchRuleByCompanyId(cmd
				.getCompanyId());
		String startEarlyTime = cmd.getStartEarlyTime();
		String startLastTime = cmd.getStartLateTime();
		String endEarlyTime = cmd.getEndEarlyTime();
		if (punchRule != null) {
			convertTime(punchRule, startEarlyTime, startLastTime, endEarlyTime);
			punchRule.setOperatorUid(userId);
			punchRule.setOperateTime(new
			 Timestamp(DateHelper.currentGMTTime().getTime()));
			punchProvider.updatePunchRule(punchRule);
			if(!StringUtils.isEmpty(cmd.getLocations())){
				List<PunchGeopoint> geopoints = punchProvider.listPunchGeopointsByCompanyId(cmd.getCompanyId());
				if(geopoints != null && geopoints.size() >0){
					for (PunchGeopoint punchGeopoint : geopoints) {
						punchProvider.deletePunchGeopoint(punchGeopoint);
					}
				}
				createPunchGeopoints(userId,cmd.getLocations(),cmd.getCompanyId());
			}
		}
	}

	@Override
	public PunchRuleResponse getPunchRuleByCompanyId(PunchCompanyIdCommand cmd) {
		PunchRuleResponse response = new PunchRuleResponse();
		PunchRuleDTO dto = null;
		checkCompanyIdIsNull(cmd.getCompanyId());
		PunchRule punchRule = punchProvider.findPunchRuleByCompanyId(cmd
				.getCompanyId());
		if (punchRule != null) {
			dto = ConvertHelper.convert(punchRule, PunchRuleDTO.class);
			dto.setStartEarlyTime(String.format("%tT", punchRule.getStartEarlyTime()));
			dto.setStartLateTime(String.format("%tT", punchRule.getStartLateTime()));
			dto.setEndEarlyTime(calculateEndTime("HH:mm:ss",String.format("%tT", punchRule.getStartEarlyTime()),String.format("%tT", punchRule.getWorkTime())));
			dto.setEndLateTime(calculateEndTime("HH:mm:ss",String.format("%tT", punchRule.getStartLateTime()),String.format("%tT", punchRule.getWorkTime())));
			List<PunchGeopoint> geopoints = punchProvider.listPunchGeopointsByCompanyId(cmd.getCompanyId());
			dto.setLocations(GsonUtil.toJson(geopoints));
		}
		response.setPunchRuleDTO(dto);
		return response;
	}

	private String calculateEndTime(String dateFormat,String startEarlyTime, String workTime) {
		DateFormat format = new SimpleDateFormat(dateFormat);
		format.setTimeZone(TimeZone.getTimeZone("GMT"));//设置 DateFormat的时间区域为GMT
		
		long endTime = 0;
		try {
			endTime = format.parse(startEarlyTime).getTime()+format.parse(workTime).getTime();
		} catch (ParseException e) {
			LOGGER.error("the time format is error.", e);
		}
		return getGMTtimeString("HH:mm;ss", endTime);
	}

	@Override
	public void deletePunchRule(PunchCompanyIdCommand cmd) {
		PunchRule punchRule = punchProvider.findPunchRuleByCompanyId(cmd.getCompanyId());
		if (punchRule != null) {
			punchProvider.deletePunchRule(punchRule);
			List<PunchGeopoint> geopoints = punchProvider.listPunchGeopointsByCompanyId(cmd.getCompanyId());
			if(geopoints != null && geopoints.size() >0){
				for (PunchGeopoint punchGeopoint : geopoints) {
					punchProvider.deletePunchGeopoint(punchGeopoint);
				}
			}
		}
	}
	
	
	public boolean isWorkDay(Date date1){
		if (date1 == null)
			return false;
		SimpleDateFormat dateSF = new SimpleDateFormat("yyyy-MM-dd");
		//如果属于周末调班 返回工作日
		for(Date workDate : getSpecialDay(DateStatus.WEEKENDWORK)){
			if (dateSF.format(date1).equals(dateSF.format(workDate)))
				return true;
		}
		//如果属于工作日休假 返回非工作日
		for(Date workDate : getSpecialDay(DateStatus.HOLIDAY)){
			if (dateSF.format(date1).equals(dateSF.format(workDate)))
				return false;
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date1);
		//一周第一天是否为星期天
		boolean isFirstSunday = (calendar.getFirstDayOfWeek() == Calendar.SUNDAY);
		//获取周几
		int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
		//若一周第一天为星期天，则-1
		if(isFirstSunday){
		  weekDay = weekDay - 1;
		  if(weekDay == 0){
		    weekDay = 7;
		  }
		}
		if( weekDay >=6){
			return false;
		}else {
			return true;
		}
		
	}
	
	public List<Date> getSpecialDay (DateStatus dateStatu){
		List<Date> result = new ArrayList() ;
		List<PunchWorkday> punchWorkdays = punchProvider.listWorkdays(dateStatu);
		if (punchWorkdays.size() ==0){
			return null ;
		}
		for (PunchWorkday punchWorkday : punchWorkdays){
			result.add(punchWorkday.getDateTag());
		}
		return result;
	}

	@Override
	public void createPunchExceptionRequest(PunchExceptionRequestCommand cmd) {
		// TODO Auto-generated method stub
		Long userId = UserContext.current().getUser().getId();
		checkCompanyIdIsNull(cmd.getCompanyId());
		if (cmd.getDescription() == null || cmd.getDescription().equals(0))
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid description parameter in the command");
		if (cmd.getRequestType() == null )
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid description parameter in the command");
		PunchExceptionRequest punchExceptionRequest = new PunchExceptionRequest();
		punchExceptionRequest.setCompanyId(cmd.getCompanyId());
		punchExceptionRequest.setUserId(userId);
		punchExceptionRequest.setCreatorUid(userId);
		punchExceptionRequest.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
				.getTime()));
		punchExceptionRequest.setDescription(cmd.getDescription());
		punchExceptionRequest.setRequestType(cmd.getRequestType());
		punchExceptionRequest.setPunchDate(new java.sql.Date(0).valueOf(cmd.getPunchDate()));
		punchProvider.createPunchExceptionRequest(punchExceptionRequest);
		
	} 
	
	
}
