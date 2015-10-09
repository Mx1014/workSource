package com.everhomes.techpark.punch;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.spatial.geohash.GeoHashUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.organization.pm.pay.GsonUtil;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.techpark.company.GroupContact;
import com.everhomes.techpark.company.GroupContactProvider;
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

	SimpleDateFormat timeSF = new SimpleDateFormat("HH:mm:ss");
	SimpleDateFormat dateSF = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat datetimeSF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Autowired
	PunchProvider punchProvider;
	@Autowired
	GroupContactProvider groupContactProvider;

	@Autowired
	ConfigurationProvider configurationProvider;

	private void checkCompanyIdIsNull(Long companyId) {
		if (null == companyId || companyId.equals(0L)) {
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
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		try {
			// 从年初开始，如果是查询今年，到今天截止，如果不是查询今年，则到该年年末
			// 如果要修改，只需要修改范围即可
			start.setTime(dateSF.parse(cmd.getQueryYear() + "-01-01"));
			if (end.before(start)) {
				throw RuntimeErrorException.errorWith(
						PunchServiceErrorCode.SCOPE,
						PunchServiceErrorCode.ERROR_QUERY_YEAR_ERROR,
						"query Year is later than now ,please check again ");
			}
			if (start.get(Calendar.YEAR) != end.get(Calendar.YEAR)) {
				end.setTime(dateSF.parse(cmd.getQueryYear() + "-01-01"));
				end.add(Calendar.YEAR, 1);
			}
		} catch (ParseException e) {
			throw RuntimeErrorException
					.errorWith(PunchServiceErrorCode.SCOPE,
							PunchServiceErrorCode.ERROR_QUERY_YEAR_ERROR,
							"there is something wrong with queryYear,please check again ");
		}
		// List<java.sql.Date> dateList =
		// punchProvider.listPunchLogsBwteenTwoDay(
		// userId, cmd.getCompanyId(), dateSF.format(start.getTime()),
		// dateSF.format(end.getTime()));

		pyl = getlistPunchLogsBetweenTwoCalendar(pyl, cmd.getCompanyId(),
				start, end);
		return pyl;
	}

	@Override
	public PunchLogsYearListResponse getlistPunchLogsBetweenTwoCalendar(
			PunchLogsYearListResponse pyl, long CompanyId, Calendar start,
			Calendar end) {
		Long userId = UserContext.current().getUser().getId();
		PunchLogsMonthList pml = null;
		while (start.before(end)) {

			Date date = start.getTime();
			// start.setTime(date);
			// if not workday continue

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

			try {
				PunchLogsDayList pdl = makePunchLogsDayListInfo(userId,
						CompanyId, start);
				if (null != pdl) {
					pml.getPunchLogsDayList().add(pdl);
				}
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

	private void makeExceptionForDayList(Long userId, Long companyId,
			Calendar logDay, PunchLogsDayList pdl) {
		// 异常申报结果的返回
		SimpleDateFormat dateSF = new SimpleDateFormat("yyyy-MM-dd");
		PunchExceptionApproval exceptionApproval = punchProvider
				.getPunchExceptionApprovalByDate(userId, companyId,
						dateSF.format(logDay.getTime()));
		if (null != exceptionApproval) {
			pdl.setApprovalStatus(exceptionApproval.getApprovalStatus());
			if (pdl.getApprovalStatus().equals(ApprovalStatus.NORMAL.getCode())
					|| pdl.getPunchStatus()
							.equals(PunchStatus.NORMAL.getCode())) {
				// 如果有申报审批结果，并且审批结果和实际打卡结果有一个是正常的话，异常结果为正常 别的为异常
				pdl.setExceptionStatus(ExceptionStatus.NORMAL.getCode());
			} else {
				pdl.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
			}
		}
		List<PunchExceptionRequest> exceptionRequests = punchProvider
				.listExceptionRequestsByDate(userId, companyId,
						dateSF.format(logDay.getTime()));
		if (exceptionRequests.size() > 0) {
			for (PunchExceptionRequest exceptionRequest : exceptionRequests) {

				PunchExceptionDTO punchExceptionDTO = new PunchExceptionDTO();

				punchExceptionDTO.setRequestType(exceptionRequest
						.getRequestType());
				punchExceptionDTO.setCreateTime(exceptionRequest
						.getCreateTime().getTime());
				if (exceptionRequest.getRequestType().equals(
						PunchRquestType.REQUEST.getCode())) {
					// 对于申请
					punchExceptionDTO.setExceptionComment(exceptionRequest
							.getDescription());
					GroupContact groupContact = groupContactProvider
							.findGroupContactByUserId(exceptionRequest
									.getUserId());
					if (null == groupContact) {
						punchExceptionDTO.setName("无此人");
					} else {
						punchExceptionDTO
								.setName(groupContact.getContactName());
					}
				} else {
					// 审批
					punchExceptionDTO.setExceptionComment(exceptionRequest
							.getProcessDetails());
					GroupContact groupContact = groupContactProvider
							.findGroupContactByUserId(exceptionRequest
									.getOperatorUid());
					if (null == groupContact) {
						punchExceptionDTO.setName("无此人");
					} else {
						punchExceptionDTO
								.setName(groupContact.getContactName());
					}
					punchExceptionDTO.setProcessCode(exceptionRequest
							.getProcessCode());
				}
				if (null == pdl.getPunchExceptionDTOs()) {
					pdl.setPunchExceptionDTOs(new ArrayList<PunchExceptionDTO>());
				}
				pdl.getPunchExceptionDTOs().add(punchExceptionDTO);
			}
		}
	}

	private PunchDayLog refreshPunchDayLog(Long userId, Long companyId,
			Calendar logDay ) throws ParseException {
		PunchLogsDayList pdl = new PunchLogsDayList();
		pdl.setPunchDay(String.valueOf(logDay.get(Calendar.DAY_OF_MONTH)));
		pdl.setPunchLogs(new ArrayList<PunchLogDTO>());
		PunchDayLog punchDayLog = punchProvider.getDayPunchLogByDate(userId,
				companyId, dateSF.format(logDay.getTime()));
		pdl = caculateDayLog(userId, companyId, logDay, pdl);
		if (null == pdl ){
			return null;
		}
		if (null == punchDayLog) {
			// 数据库没有计算好的数据
			punchDayLog = new PunchDayLog();
			punchDayLog.setUserId(userId);
			punchDayLog.setCompanyId(companyId);
			punchDayLog.setCreatorUid(userId);
			punchDayLog.setPunchDate(java.sql.Date.valueOf(dateSF.format(logDay
					.getTime())));
			punchDayLog.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
					.getTime()));
			Long arriveTime = null;
			Long leaveTime = null;
			for (PunchLogDTO pDto : pdl.getPunchLogs()) {
				if (pDto.getClockStatus().equals(ClockStatus.LEAVE.getCode())) {
					leaveTime = pDto.getPunchTime();
				} else {
					arriveTime = pDto.getPunchTime();
				}
			}
			long workTime = leaveTime - arriveTime; 
			punchDayLog.setArriveTime(getDAOTime(arriveTime));
			punchDayLog.setLeaveTime(getDAOTime(leaveTime));
			punchDayLog.setWorkTime(java.sql.Time.valueOf(getGMTtimeString("HH:mm:ss", workTime))); 
			punchDayLog.setStatus(pdl.getPunchStatus());
			punchProvider.createPunchDayLog(punchDayLog);

		} else {
			// 数据库有计算好的数据
			punchDayLog.setUserId(userId);
			punchDayLog.setCompanyId(companyId);
			punchDayLog.setCreatorUid(userId);
			punchDayLog.setPunchDate(java.sql.Date.valueOf(dateSF.format(logDay
					.getTime())));
			punchDayLog.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
					.getTime()));
			Long arriveTime = null;
			Long leaveTime = null;
			for (PunchLogDTO pDto : pdl.getPunchLogs()) {
				if (pDto.getClockStatus().equals(ClockStatus.LEAVE.getCode())) {
					leaveTime = pDto.getPunchTime();
				} else {
					arriveTime = pDto.getPunchTime();
				}
			}
			Long workTime = leaveTime - arriveTime;
			punchDayLog.setArriveTime(getDAOTime(arriveTime));
			punchDayLog.setLeaveTime(getDAOTime(leaveTime));
			punchDayLog.setWorkTime(java.sql.Time.valueOf(getGMTtimeString("HH:mm:ss", workTime))); 
			punchDayLog.setStatus(pdl.getPunchStatus());
			punchProvider.updatePunchDayLog(punchDayLog);
		}
		return punchDayLog;
	}

	private Time getDAOTime(Long arriveTime) { 
		if(arriveTime.equals(0L)){
			return java.sql.Time.valueOf(getGMTtimeString("HH:mm:ss", arriveTime));
		}
		else {
			return new java.sql.Time(arriveTime);	
		}
		
	}

	/***
	 * @param punchLogs
	 *            ： 当天的全部打卡记录通过punchProvider.listPunchLogsByDate()方法得到;
	 * @param punchRule
	 *            :打卡规则
	 * @param logDay
	 *            : 计算的打卡日期
	 * @return PunchLogsDayList：计算好的当日打卡状态
	 * */
	private PunchLogsDayList makePunchLogsDayListInfo(Long userId,
			Long companyId, Calendar logDay) throws ParseException {
		Date now = new Date();
		PunchLogsDayList pdl = new PunchLogsDayList();
		pdl.setPunchDay(String.valueOf(logDay.get(Calendar.DAY_OF_MONTH)));
		pdl.setPunchLogs(new ArrayList<PunchLogDTO>());

		PunchDayLog punchDayLog = punchProvider.getDayPunchLogByDate(userId,
				companyId, dateSF.format(logDay.getTime()));
		if (null == punchDayLog) {
			// 插入数据
			punchDayLog = refreshPunchDayLog(userId, companyId, logDay );
			if(null ==punchDayLog){
				//验证后为空
				return null ;
			}
		}
		PunchLogDTO arriveLogDTO = new PunchLogDTO();
		arriveLogDTO.setClockStatus(ClockStatus.ARRIVE.getCode());
		arriveLogDTO.setPunchTime(punchDayLog.getArriveTime().getTime());
		pdl.getPunchLogs().add(arriveLogDTO);
		PunchLogDTO leaveLogDTO = new PunchLogDTO();
		leaveLogDTO.setClockStatus(ClockStatus.LEAVE.getCode());
		leaveLogDTO.setPunchTime(punchDayLog.getLeaveTime().getTime());
		pdl.getPunchLogs().add(leaveLogDTO);
		pdl.setPunchStatus(punchDayLog.getStatus());
		pdl.setExceptionStatus(punchDayLog.getStatus().equals(PunchStatus.NORMAL.getCode())?ExceptionStatus.NORMAL.getCode():ExceptionStatus.EXCEPTION.getCode());
		makeExceptionForDayList(userId, companyId, logDay, pdl);

		return pdl;
	}

	/***
	 * 计算每一天的打卡状态，返回值PDL 
	 * */
	private PunchLogsDayList caculateDayLog(Long userId, Long companyId,
			Calendar logDay, PunchLogsDayList pdl) throws ParseException {
		List<PunchLog> punchLogs = punchProvider.listPunchLogsByDate(userId,
				companyId, dateSF.format(logDay.getTime()),
				ClockCode.SUCESS.getCode());
		Date now = new Date();
		// 如果零次打卡记录
		if (null == punchLogs || punchLogs.size() == 0) {
			if (!isWorkDay(logDay.getTime())
					|| dateSF.format(now).equals(
							dateSF.format(logDay.getTime()))) {
				// 如果非工作日或者当天，不增pdl直接下一天
				return null;
			}
			PunchLogDTO noPunchLogDTO1 = new PunchLogDTO();
			noPunchLogDTO1.setClockStatus(ClockStatus.ARRIVE.getCode());
			pdl.getPunchLogs().add(noPunchLogDTO1);
			PunchLogDTO noPunchLogDTO2 = new PunchLogDTO();
			noPunchLogDTO2.setClockStatus(ClockStatus.LEAVE.getCode());
			pdl.setPunchStatus(PunchStatus.UNPUNCH.getCode());
			pdl.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
			pdl.getPunchLogs().add(noPunchLogDTO2);
			makeExceptionForDayList(userId, companyId, logDay, pdl);
			return pdl;
		}

		if (punchLogs.size() == 1) {
			// 如果只有一次打卡，就把离开设置为未打卡,当天设置为旷工
			PunchLogDTO arriveLogDTO = new PunchLogDTO();
			arriveLogDTO.setClockStatus(ClockStatus.ARRIVE.getCode());
			arriveLogDTO
					.setPunchTime(punchLogs.get(0).getPunchDate().getTime());
			pdl.getPunchLogs().add(arriveLogDTO);
			PunchLogDTO noPunchLogDTO2 = new PunchLogDTO();
			noPunchLogDTO2.setClockStatus(ClockStatus.LEAVE.getCode());
			pdl.getPunchLogs().add(noPunchLogDTO2);
			if (!isWorkDay(logDay.getTime())
					|| dateSF.format(now).equals(
							dateSF.format(logDay.getTime()))) {
				pdl.setPunchStatus(PunchStatus.NORMAL.getCode());
				pdl.setExceptionStatus(ExceptionStatus.NORMAL.getCode());
				// 如果非工作日 normal
				return pdl;
			}
			pdl.setPunchStatus(PunchStatus.UNPUNCH.getCode());
			pdl.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
			makeExceptionForDayList(userId, companyId, logDay, pdl);
			return pdl;
		}
		PunchRule punchRule = punchProvider.getPunchRuleByCompanyId(companyId);
		if (null == punchRule)
			throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid company Id parameter in the command ");
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

		PunchLogDTO arrivePunchLogDTO = new PunchLogDTO();
		PunchLogDTO leavePunchLogDTO = new PunchLogDTO();
		pdl.getPunchLogs().add(leavePunchLogDTO);
		pdl.getPunchLogs().add(arrivePunchLogDTO);
		arrivePunchLogDTO.setClockStatus(ClockStatus.ARRIVE.getCode());
		leavePunchLogDTO.setClockStatus(ClockStatus.LEAVE.getCode());
		List<Calendar> punchMinAndMaxTime = getMinAndMaxTimeFromPunchlogs(punchLogs);
		Calendar arriveCalendar = punchMinAndMaxTime.get(0);
		Calendar leaveCalendar = punchMinAndMaxTime.get(1);
		arrivePunchLogDTO.setPunchTime(arriveCalendar.getTime().getTime());
		leavePunchLogDTO.setPunchTime(leaveCalendar.getTime().getTime());

		if (!isWorkDay(logDay.getTime())
				|| dateSF.format(now).equals(dateSF.format(logDay.getTime()))) {
			// 如果非工作日 normal
			pdl.setPunchStatus(PunchStatus.NORMAL.getCode());
			pdl.setExceptionStatus(ExceptionStatus.NORMAL.getCode());

			return pdl;
		}
		// 打卡状态设置为正常或者迟到
		if (punchMinAndMaxTime.get(0).before(startMaxTime)) {
			pdl.setPunchStatus(PunchStatus.NORMAL.getCode());
			pdl.setExceptionStatus(ExceptionStatus.NORMAL.getCode());
		} else {
			pdl.setPunchStatus(PunchStatus.BELATE.getCode());
			pdl.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
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
			// 如果离开时间超过最晚工作时间，为正常
		} else {
			if (arriveCalendar.after(startMinTime)) {
				// 如果到达时间晚于最早工作时间，按照到达时间计算
				if (leaveCalendar.after(arriveCalendar)) {

				} else {
					if (pdl.getPunchStatus().equals(
							PunchStatus.NORMAL.getCode())) {
						pdl.setPunchStatus(PunchStatus.LEAVEEARLY.getCode());
						pdl.setExceptionStatus(ExceptionStatus.EXCEPTION
								.getCode());
					} else {
						pdl.setPunchStatus(PunchStatus.BLANDLE.getCode());
						pdl.setExceptionStatus(ExceptionStatus.EXCEPTION
								.getCode());
					}
				}
			} else {
				if (leaveCalendar.after(startMinTime)) {

				} else {
					if (pdl.getPunchStatus().equals(
							PunchStatus.NORMAL.getCode())) {
						pdl.setPunchStatus(PunchStatus.LEAVEEARLY.getCode());
						pdl.setExceptionStatus(ExceptionStatus.EXCEPTION
								.getCode());
					} else {
						pdl.setPunchStatus(PunchStatus.BLANDLE.getCode());
						pdl.setExceptionStatus(ExceptionStatus.EXCEPTION
								.getCode());
					}
				}
			}
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
		try {
			refreshPunchDayLog(userId, cmd.getCompanyId(), punCalendar);
		} catch (ParseException e) { 
			LOGGER.error(e.toString());

			throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
					PunchServiceErrorCode.ERROR_PUNCH_ADD_DAYLOG,
					"Something wrong with refresh PunchDayLog");
		}
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
			createPunchGeopoints(userId, cmd.getLocations(), cmd.getCompanyId());

		}

	}

	private void createPunchGeopoints(Long userId, String locations,
			Long compantId) {
		JSONObject jsonObject = (JSONObject) JSONValue.parse(locations);
		JSONArray locationValue = (JSONArray) jsonObject.get("locations");
		Gson gson = new Gson();
		List<PunchGeopoint> geopoints = gson.fromJson(locationValue.toString(),
				new TypeToken<List<PunchGeopoint>>() {
				}.getType());
		for (PunchGeopoint punchGeopoint : geopoints) {
			punchGeopoint.setCompanyId(compantId);
			punchGeopoint.setCreatorUid(userId);
			punchGeopoint.setCreateTime(new Timestamp(DateHelper
					.currentGMTTime().getTime()));
			punchGeopoint.setGeohash(GeoHashUtils.encode(
					punchGeopoint.getLatitude(), punchGeopoint.getLongitude()));
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
		// long hours = (workTime/(1000* 60 * 60));
		// long minutes = (workTime-hours*(1000* 60 * 60))/(1000* 60);
		String workTimeStr = getGMTtimeString("HH:mm:ss", workTime);
		punchRule.setWorkTime(convertTime(workTimeStr));
	}

	public String getGMTtimeString(String dateFormat, long time) {
		DateFormat format = new SimpleDateFormat(dateFormat);
		format.setTimeZone(TimeZone.getTimeZone("GMT"));// 设置
														// DateFormat的时间区域为GMT
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
			punchRule.setOperateTime(new Timestamp(DateHelper.currentGMTTime()
					.getTime()));
			punchProvider.updatePunchRule(punchRule);
			if (!StringUtils.isEmpty(cmd.getLocations())) {
				List<PunchGeopoint> geopoints = punchProvider
						.listPunchGeopointsByCompanyId(cmd.getCompanyId());
				if (geopoints != null && geopoints.size() > 0) {
					for (PunchGeopoint punchGeopoint : geopoints) {
						punchProvider.deletePunchGeopoint(punchGeopoint);
					}
				}
				createPunchGeopoints(userId, cmd.getLocations(),
						cmd.getCompanyId());
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
			dto.setStartEarlyTime(String.format("%tT",
					punchRule.getStartEarlyTime()));
			dto.setStartLateTime(String.format("%tT",
					punchRule.getStartLateTime()));
			dto.setEndEarlyTime(calculateEndTime("HH:mm:ss",
					String.format("%tT", punchRule.getStartEarlyTime()),
					String.format("%tT", punchRule.getWorkTime())));
			dto.setEndLateTime(calculateEndTime("HH:mm:ss",
					String.format("%tT", punchRule.getStartLateTime()),
					String.format("%tT", punchRule.getWorkTime())));
			List<PunchGeopoint> geopoints = punchProvider
					.listPunchGeopointsByCompanyId(cmd.getCompanyId());
			dto.setLocations(GsonUtil.toJson(geopoints));
		}
		response.setPunchRuleDTO(dto);
		return response;
	}

	private String calculateEndTime(String dateFormat, String startEarlyTime,
			String workTime) {
		DateFormat format = new SimpleDateFormat(dateFormat);
		format.setTimeZone(TimeZone.getTimeZone("GMT"));// 设置
														// DateFormat的时间区域为GMT

		long endTime = 0;
		try {
			endTime = format.parse(startEarlyTime).getTime()
					+ format.parse(workTime).getTime();
		} catch (ParseException e) {
			LOGGER.error("the time format is error.", e);
		}
		return getGMTtimeString("HH:mm;ss", endTime);
	}

	@Override
	public void deletePunchRule(PunchCompanyIdCommand cmd) {
		PunchRule punchRule = punchProvider.findPunchRuleByCompanyId(cmd
				.getCompanyId());
		if (punchRule != null) {
			punchProvider.deletePunchRule(punchRule);
			List<PunchGeopoint> geopoints = punchProvider
					.listPunchGeopointsByCompanyId(cmd.getCompanyId());
			if (geopoints != null && geopoints.size() > 0) {
				for (PunchGeopoint punchGeopoint : geopoints) {
					punchProvider.deletePunchGeopoint(punchGeopoint);
				}
			}
		}
	}

	public boolean isWorkDay(Date date1) {
		if (date1 == null)
			return false;
		SimpleDateFormat dateSF = new SimpleDateFormat("yyyy-MM-dd");
		// 如果属于周末调班 返回工作日
		for (Date workDate : getSpecialDay(DateStatus.WEEKENDWORK)) {
			if (dateSF.format(date1).equals(dateSF.format(workDate)))
				return true;
		}
		// 如果属于工作日休假 返回非工作日
		for (Date workDate : getSpecialDay(DateStatus.HOLIDAY)) {
			if (dateSF.format(date1).equals(dateSF.format(workDate)))
				return false;
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date1);
		// 一周第一天是否为星期天
		boolean isFirstSunday = (calendar.getFirstDayOfWeek() == Calendar.SUNDAY);
		// 获取周几
		int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
		// 若一周第一天为星期天，则-1
		if (isFirstSunday) {
			weekDay = weekDay - 1;
			if (weekDay == 0) {
				weekDay = 7;
			}
		}
		if (weekDay >= 6) {
			return false;
		} else {
			return true;
		}

	}

	public List<Date> getSpecialDay(DateStatus dateStatu) {
		List<Date> result = new ArrayList();
		List<PunchWorkday> punchWorkdays = punchProvider
				.listWorkdays(dateStatu);
		if (punchWorkdays.size() == 0) {
			return null;
		}
		for (PunchWorkday punchWorkday : punchWorkdays) {
			result.add(punchWorkday.getDateTag());
		}
		return result;
	}

	@Override
	public void createPunchExceptionRequest(PunchExceptionRequestCommand cmd) {
		Long userId = UserContext.current().getUser().getId();
		checkCompanyIdIsNull(cmd.getCompanyId());
		if (cmd.getRequestDescription() == null || cmd.getRequestDescription().equals(0))
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid description parameter in the command");

		PunchExceptionRequest punchExceptionRequest = new PunchExceptionRequest();
		punchExceptionRequest.setCompanyId(cmd.getCompanyId());
		punchExceptionRequest.setRequestType(PunchRquestType.REQUEST.getCode());
		punchExceptionRequest.setUserId(userId);
		punchExceptionRequest.setCreatorUid(userId);
		punchExceptionRequest.setCreateTime(new Timestamp(DateHelper
				.currentGMTTime().getTime()));
		punchExceptionRequest.setDescription(cmd.getRequestDescription());
		punchExceptionRequest.setPunchDate(java.sql.Date.valueOf(cmd
				.getPunchDate()));
		punchExceptionRequest.setStatus(ExceptionProcessStatus.WAITFOR
				.getCode());
		punchProvider.createPunchExceptionRequest(punchExceptionRequest);

	}

	@Override
	public ListPunchExceptionRequestCommandResponse listExceptionRequests(
			ListPunchExceptionRequestCommand cmd) {
		checkCompanyIdIsNull(cmd.getCompanyId());
		ListPunchExceptionRequestCommandResponse response = new ListPunchExceptionRequestCommandResponse();
		cmd.setPageOffset(cmd.getPageOffset() == null ? 1 : cmd.getPageOffset());
		int totalCount = punchProvider.countExceptionRequests(cmd.getKeyword(),
				cmd.getCompanyId(), cmd.getStartDay(), cmd.getEndDay(),
				cmd.getExceptionStatus(), cmd.getProcessCode(),
				PunchRquestType.REQUEST.getCode());
		if (totalCount == 0)
			return response;

		int pageSize = PaginationConfigHelper.getPageSize(
				configurationProvider, cmd.getPageSize());
		int pageCount = getPageCount(totalCount, pageSize);

		List<PunchExceptionRequest> result = punchProvider
				.listExceptionRequests(cmd.getKeyword(), cmd.getCompanyId(),
						cmd.getStartDay(), cmd.getEndDay(),
						cmd.getExceptionStatus(), cmd.getProcessCode(),
						cmd.getPageOffset(), pageSize,
						PunchRquestType.REQUEST.getCode());
		response.setExceptionRequestList(result
				.stream()
				.map(r -> {
					PunchExceptionRequestDTO dto = ConvertHelper.convert(r,
							PunchExceptionRequestDTO.class);
					Calendar logDay = Calendar.getInstance();
					logDay.setTime(dto.getPunchDate());
					PunchLogsDayList pdl = null;
					try {
						pdl = this.makePunchLogsDayListInfo(dto.getUserId(),
								dto.getCompanyId(), logDay);
					} catch (Exception e) {

						e.printStackTrace();
					}
					if (null == pdl)
						throw RuntimeErrorException.errorWith(
								ErrorCodes.SCOPE_GENERAL,
								ErrorCodes.ERROR_INVALID_PARAMETER,
								"Invalid description parameter in the command");
					dto.setPunchStatus(pdl.getPunchStatus());
					Long arriveTime = null;
					Long leaveTime = null;
					for (PunchLogDTO pDto : pdl.getPunchLogs()) {
						if (pDto.getClockStatus().equals(
								ClockStatus.LEAVE.getCode())) {
							leaveTime = pDto.getPunchTime();
						} else {
							arriveTime = pDto.getPunchTime();
						}
					}
					if (leaveTime.equals(0L) || arriveTime.equals(0L)) {
						if (arriveTime.equals(0L)) {

						} else {
							dto.setArriveTime(getGMTtimeString("HH:mm:ss",
									arriveTime));
						}

					} else {
						Long workTime = leaveTime - arriveTime;
						dto.setArriveTime(timeSF.format(new Date(arriveTime)));
						dto.setLeaveTime(timeSF.format(new Date(leaveTime)));
						dto.setWorkTime(getGMTtimeString("HH:mm:ss", workTime));
					}
					GroupContact groupContact = groupContactProvider
							.findGroupContactByUserId(dto.getUserId());
					dto.setUserName(groupContact.getContactName());
					dto.setToken(groupContact.getContactToken());
					return dto;
				}).collect(Collectors.toList()));

		response.setNextPageOffset(cmd.getPageOffset() == pageCount ? null
				: cmd.getPageOffset() + 1);
		return response;
	}

	private int getPageCount(int totalCount, int pageSize) {
		int pageCount = totalCount / pageSize;

		if (totalCount % pageSize != 0) {
			pageCount++;
		}
		return pageCount;
	}

	@Override
	public ListPunchExceptionRequestCommandResponse listExceptionApprovals(
			ListPunchExceptionApprovalCommand cmd) {
		if (null == cmd.getUserId() || cmd.getUserId().equals(0L)) {
			LOGGER.error("Invalid user Id parameter in the command");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid company Id parameter in the command");
		}
		checkCompanyIdIsNull(cmd.getCompanyId());
		ListPunchExceptionRequestCommandResponse response = new ListPunchExceptionRequestCommandResponse();
		cmd.setPageOffset(cmd.getPageOffset() == null ? 1 : cmd.getPageOffset());
		int totalCount = punchProvider.countExceptionRequests(cmd.getUserId(),
				null, cmd.getCompanyId(), cmd.getPunchDate(),
				cmd.getPunchDate(), null, null,
				PunchRquestType.APPROVAL.getCode());
		if (totalCount == 0)
			return response;
		int pageSize = PaginationConfigHelper.getPageSize(
				configurationProvider, cmd.getPageSize());
		int pageCount = getPageCount(totalCount, pageSize);

		List<PunchExceptionRequest> result = punchProvider
				.listExceptionRequests(cmd.getUserId(), null,
						cmd.getCompanyId(), cmd.getPunchDate(),
						cmd.getPunchDate(), null, null, cmd.getPageOffset(),
						pageSize, PunchRquestType.APPROVAL.getCode());
		response.setExceptionRequestList(result
				.stream()
				.map(r -> {
					PunchExceptionRequestDTO dto = ConvertHelper.convert(r,
							PunchExceptionRequestDTO.class);

					GroupContact groupContact = groupContactProvider
							.findGroupContactByUserId(dto.getUserId());
					dto.setUserName(groupContact.getContactName());
					dto.setToken(groupContact.getContactToken());
					return dto;
				}).collect(Collectors.toList()));

		response.setNextPageOffset(cmd.getPageOffset() == pageCount ? null
				: cmd.getPageOffset() + 1);
		return response;
	}

	@Override
	public void punchExceptionApproval(PunchExceptionApprovalCommand cmd) {
		if (null == cmd.getUserId() || cmd.getUserId().equals(0L)) {
			LOGGER.error("Invalid user Id parameter in the command");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid company Id parameter in the command");
		}
		checkCompanyIdIsNull(cmd.getCompanyId());
		// 插入一条eh_punch_exception_requests 记录
		PunchExceptionRequest punchExceptionRequest = new PunchExceptionRequest();
		punchExceptionRequest.setCompanyId(cmd.getCompanyId());
		punchExceptionRequest
				.setRequestType(PunchRquestType.APPROVAL.getCode());
		punchExceptionRequest.setProcessCode(cmd.getProcessCode());
		punchExceptionRequest.setProcessDetails(cmd.getProcessDetails());
		punchExceptionRequest.setUserId(cmd.getUserId());
		punchExceptionRequest.setCreatorUid(cmd.getCreatorUid());
		punchExceptionRequest.setCreateTime(new Timestamp(DateHelper
				.currentGMTTime().getTime()));
		punchExceptionRequest.setOperatorUid(cmd.getOperatorUid());
		punchExceptionRequest.setOperateTime(new Timestamp(DateHelper
				.currentGMTTime().getTime()));
		punchExceptionRequest.setPunchDate(java.sql.Date.valueOf(cmd
				.getPunchDate()));
		punchExceptionRequest.setStatus(cmd.getStatus());
		punchProvider.createPunchExceptionRequest(punchExceptionRequest);
		// 查eh_punch_exception_approvals有无数据：无数据，结果是同意则插入 /有数据 如果结果是同意
		// 则修改，结果是驳回则删除
		PunchExceptionApproval punchExceptionApproval = punchProvider
				.getExceptionApproval(cmd.getUserId(), cmd.getCompanyId(),
						java.sql.Date.valueOf(cmd.getPunchDate()));
		if (null == punchExceptionApproval) {
			if (cmd.getStatus().equals(ExceptionProcessStatus.ACTIVE.getCode())) {
				punchExceptionApproval = new PunchExceptionApproval();
				punchExceptionApproval.setCompanyId(cmd.getCompanyId());
				punchExceptionApproval.setApprovalStatus(cmd.getProcessCode());
				punchExceptionApproval.setUserId(cmd.getUserId());
				punchExceptionApproval.setCreatorUid(cmd.getCreatorUid());
				punchExceptionApproval.setCreateTime(new Timestamp(DateHelper
						.currentGMTTime().getTime()));
				punchExceptionApproval.setOperatorUid(cmd.getOperatorUid());
				punchExceptionApproval.setOperateTime(new Timestamp(DateHelper
						.currentGMTTime().getTime()));
				punchExceptionApproval.setPunchDate(java.sql.Date.valueOf(cmd
						.getPunchDate()));
				punchProvider
						.createPunchExceptionApproval(punchExceptionApproval);
			}
		} else {
			if (cmd.getStatus().equals(ExceptionProcessStatus.ACTIVE.getCode())) {
				punchExceptionApproval.setCompanyId(cmd.getCompanyId());
				punchExceptionApproval.setApprovalStatus(cmd.getProcessCode());
				punchExceptionApproval.setUserId(cmd.getUserId());
				punchExceptionApproval.setCreatorUid(cmd.getCreatorUid());
				punchExceptionApproval.setCreateTime(new Timestamp(DateHelper
						.currentGMTTime().getTime()));
				punchExceptionApproval.setOperatorUid(cmd.getOperatorUid());
				punchExceptionApproval.setOperateTime(new Timestamp(DateHelper
						.currentGMTTime().getTime()));
				punchExceptionApproval.setPunchDate(java.sql.Date.valueOf(cmd
						.getPunchDate()));
				punchProvider
						.updatePunchExceptionApproval(punchExceptionApproval);
			} else {
				punchProvider
						.deletePunchExceptionApproval(punchExceptionApproval
								.getId());
			}
		}
		// 更新eh_punch_exception_requests当天当人的申请记录
		List<PunchExceptionRequest> results = punchProvider
				.listExceptionRequests(cmd.getUserId(), null,
						cmd.getCompanyId(), cmd.getPunchDate(),
						cmd.getPunchDate(), null, null, 1, 999999,
						PunchRquestType.REQUEST.getCode());
		for (PunchExceptionRequest result : results) {

			result.setProcessCode(cmd.getProcessCode());
			result.setProcessDetails(cmd.getProcessDetails());
			result.setUserId(cmd.getUserId());
			result.setCreatorUid(cmd.getCreatorUid());
			result.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
					.getTime()));
			result.setOperatorUid(cmd.getOperatorUid());
			result.setOperateTime(new Timestamp(DateHelper.currentGMTTime()
					.getTime()));
			result.setStatus(cmd.getStatus());
			punchProvider.updatePunchExceptionRequest(result);
		}
	}
	
	@Override
	public ListPunchStatisticsCommandResponse listPunchStatistics(ListPunchStatisticsCommand cmd) {
		checkCompanyIdIsNull(cmd.getCompanyId());
		ListPunchStatisticsCommandResponse response = new ListPunchStatisticsCommandResponse();
		cmd.setPageOffset(cmd.getPageOffset() == null ? 1 : cmd.getPageOffset());
		int totalCount = punchProvider.countPunchDayLogs(cmd.getKeyword(),
				cmd.getCompanyId(), cmd.getStartDay(), cmd.getEndDay(),cmd.getStatus());
		if (totalCount == 0)
			return response;

		Integer pageSize = PaginationConfigHelper.getPageSize(
				configurationProvider, cmd.getPageSize());
		int pageCount = getPageCount(totalCount, pageSize);
		

		List<PunchDayLog> result = punchProvider
				.listPunchDayLogs(cmd.getKeyword(),
						cmd.getCompanyId(), cmd.getStartDay(), cmd.getEndDay(),cmd.getStatus(), cmd.getPageOffset(),
						pageSize);
		response.setPunchList(result
				.stream()
				.map(r -> {
					PunchStatisticsDTO dto = ConvertHelper.convert(r,
							PunchStatisticsDTO.class);
					if(dto != null){
						GroupContact groupContact = groupContactProvider
								.findGroupContactByUserId(dto.getUserId());
						dto.setUserName(groupContact.getContactName());
						dto.setToken(groupContact.getContactToken());
						
						PunchExceptionApproval approval = punchProvider.getExceptionApproval(dto.getUserId(), dto.getCompanyId(), dto.getPunchDate());
						dto.setApprovalStatus(approval!=null?approval.getApprovalStatus():0);
					}
					return dto;
				}).collect(Collectors.toList()));

		response.setNextPageOffset(cmd.getPageOffset() == pageCount ? null
				: cmd.getPageOffset() + 1);
		return response;

	}

	@Override
	public getPunchNewExceptionResponse getPunchNewException(getPunchNewExceptionCommand cmd) {
		checkCompanyIdIsNull(cmd.getCompanyId());
		Long userId = UserContext.current().getUser().getId();
		getPunchNewExceptionResponse response = new getPunchNewExceptionResponse();
		response.setExceptionCode(ExceptionStatus.NORMAL.getCode());
		//TODO：从本月初，或者第一次打卡开始
		Calendar start = Calendar.getInstance();
		//月初
		Calendar monthStart = Calendar.getInstance();
		monthStart.set(GregorianCalendar.DAY_OF_MONTH, 1); 
		//前一天
		Calendar end = Calendar.getInstance();
		end.add(Calendar.DAY_OF_MONTH, -1);
		//找出异常的记录
		List<PunchDayLog> PunchDayLogs = punchProvider.listPunchDayExceptionLogs(
						userId,cmd.getCompanyId(), dateSF.format(start.getTime()), dateSF.format(end.getTime())); 
		if(PunchDayLogs.size() >0)
			response.setExceptionCode(ExceptionStatus.EXCEPTION.getCode());
		return response;
	}
}
