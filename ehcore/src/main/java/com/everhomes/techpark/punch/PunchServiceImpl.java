package com.everhomes.techpark.punch;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.EhPunchRules;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;

@Service
public class PunchServiceImpl implements PunchService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(PunchServiceImpl.class);

	@Autowired
	PunchProvider punchProvider;

	@Autowired
	SequenceProvider sequenceProvider;

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
		Long userId = UserContext.current().getUser().getId();

		SimpleDateFormat dateSF = new SimpleDateFormat("yyyy-MM-dd");
		checkCompanyIdIsNull(cmd.getCompanyId());
		if (cmd.getQueryYear() == null || cmd.getQueryYear().isEmpty())
			throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid queryYear parameter in the command");

		PunchRule punchRule = punchProvider.getPunchRuleByCompanyId(cmd
				.getCompanyId());
		if (null == punchRule)
			throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid company Id parameter in the command ");
		PunchLogsYearListResponse pyl = new PunchLogsYearListResponse();
		pyl.setPunchYear(cmd.getQueryYear());
		PunchLogsMonthList pml = null;
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
		List<java.sql.Date> dateList = punchProvider.listPunchLogsBwteenTwoDay(
				userId, cmd.getCompanyId(), dateSF.format(start.getTime()),
				dateSF.format(end.getTime()));

		for (java.sql.Date date : dateList) {
			start.setTime(date);
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
			List<PunchLog> punchLogs = punchProvider.listPunchLogsByDate(
					userId, cmd.getCompanyId(), dateSF.format(start.getTime()));
			if (null == punchLogs || punchLogs.size() == 0) {
				continue;
			}
			PunchLogsDayList pdl = new PunchLogsDayList();
			pdl.setPunchDay(String.valueOf(start.get(Calendar.DAY_OF_MONTH)));
			pdl.setPunchLogs(new ArrayList<PunchLogDTO>());
			try {
				pml.getPunchLogsDayList().add(
						makePunchLogsDayListInfo(punchLogs, punchRule, pdl));
			} catch (ParseException e) {

				throw RuntimeErrorException.errorWith(
						PunchServiceErrorCode.SCOPE,
						ErrorCodes.ERROR_INVALID_PARAMETER,
						"punch Rule has somthing wrong");
			}
			// start.add(Calendar.DAY_OF_MONTH, 1);
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
		request.setCode(verifyCompanyGoePoints(cmd));
		punchLog.setPunchStatus(request.getCode().getCode());
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
		// TODO Auto-generated method stub
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
			long id = sequenceProvider.getNextSequence(NameMapper
					.getSequenceDomainFromTablePojo(EhPunchRules.class));
			punchRule.setId(id);
			convertTime(punchRule, startEarlyTime, startLastTime, endEarlyTime);
			punchRule.setCreatorUid(userId);
			punchRule.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
					.getTime()));
			punchProvider.createPunchRule(punchRule);
		}

	}

	private void convertTime(PunchRule punchRule, String startEarlyTime,
			String startLastTime, String endEarlyTime) {
		Time startEarly = convertTime(startEarlyTime);
		punchRule.setStartEarlyTime(startEarly);
		punchRule.setStartLateTime(convertTime(startLastTime));
		Time endEarly = convertTime(endEarlyTime);
		Long workTime = endEarly.getTime() - startEarly.getTime();
		punchRule.setWorkTime(convertTime(String.format("%tT", DateHelper
				.getDateDisplayString(TimeZone.getDefault(), workTime,
						"HH:mm:ss"))));
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
			PunchRule newPunchRule = ConvertHelper
					.convert(cmd, PunchRule.class);
			newPunchRule.setCreatorUid(punchRule.getCreatorUid());
			newPunchRule.setCreateTime(punchRule.getCreateTime());
			convertTime(punchRule, startEarlyTime, startLastTime, endEarlyTime);
			// punchRule.setUpdateUid(userId);
			// punchRule.setUpdateTime(new
			// Timestamp(DateHelper.currentGMTTime().getTime()));
			punchProvider.updatePunchRule(punchRule);
		}
	}

	@Override
	public PunchRuleResponse getPunchRuleByCompanyId(PunchClockCommand cmd) {
		PunchRuleResponse response = new PunchRuleResponse();
		PunchRuleDTO dto = null;
		checkCompanyIdIsNull(cmd.getCompanyId());
		PunchRule punchRule = punchProvider.findPunchRuleByCompanyId(cmd
				.getCompanyId());
		if (punchRule != null) {
			dto = ConvertHelper.convert(punchRule, PunchRuleDTO.class);
		}
		response.setPunchRuleDTO(dto);
		return response;
	}

	@Override
	public void deletePunchRule(DeletePunchCommand cmd) {
		PunchRule punchRule = punchProvider.findPunchRuleById(cmd.getId());
		if (punchRule != null) {
			punchProvider.deletePunchRule(punchRule);
		}
	}

}
