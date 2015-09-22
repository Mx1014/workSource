package com.everhomes.techpark.punch;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.apache.naming.java.javaURLContextFactory;
import org.apache.tools.ant.taskdefs.Java;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.rest.RestResponse;
import com.everhomes.user.UserContext;
import com.everhomes.util.RuntimeErrorException;

@Service
public class PunchServiceImpl implements PunchService {

	@Autowired
	PunchProvider punchProvider;

	@Override
	public PunchLogsYearListResponse getlistPunchLogs(ListPunchLogsCommand cmd) {
		Long userId = UserContext.current().getUser().getId();
       
		SimpleDateFormat dateSF = new SimpleDateFormat("yyyy-MM-dd");
		if (cmd.getCompanyId() == null)
			throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid company Id parameter in the command");
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

	private PunchLogsDayList makePunchLogsDayListInfo(
			List<PunchLog> punchLogs, PunchRule punchRule,
			PunchLogsDayList pdl) throws ParseException {

		SimpleDateFormat timeSF = new SimpleDateFormat("HH:mm:ss");
		SimpleDateFormat dateSF = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat datetimeSF = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		Calendar starttime = Calendar.getInstance();
		Calendar latetime = Calendar.getInstance();
		Calendar endtime = Calendar.getInstance();
		Calendar earlytime = Calendar.getInstance();

		// 先看弹性非弹性
		if (PunchCheckType.STANDARD == PunchCheckType.fromCode(punchRule
				.getCheckType())) {
			// 非弹性工作
			// 上班
			starttime.setTime(datetimeSF.parse(dateSF.format(punchLogs.get(0)
					.getPunchDate())
					+ " "
					+ timeSF.format(punchRule.getStartTime())));
			PunchLogDTO punchLogDTO = marcheLogsBeforeTime(punchLogs, starttime);
			if (null == punchLogDTO) {
				// 上班时间前没有记录
				latetime.setTime(punchRule.getLateArriveTime());
				starttime.add(Calendar.HOUR, latetime.get(Calendar.HOUR));
				starttime.add(Calendar.MINUTE, latetime.get(Calendar.MINUTE));
				starttime.add(Calendar.SECOND, latetime.get(Calendar.SECOND));
				punchLogDTO = marcheLogsBeforeTime(punchLogs, starttime);
				if (null == punchLogDTO) {
					// 上班时间+迟到时间前没有记录-未打卡
					punchLogDTO = new PunchLogDTO();
					punchLogDTO.setPunchStatus(PunchStatus.UNPUNCH.getCode());
					pdl.setPunchStatus(PunchStatus.UNPUNCH.getCode());
				} else {
					// 上班时间+迟到时间前有记录-迟到
					punchLogDTO.setPunchStatus(PunchStatus.BELATE.getCode());
					pdl.setPunchStatus(PunchStatus.BELATE.getCode());
				}
			} else {
				// 上班时间前有记录-正常
				punchLogDTO.setPunchStatus(PunchStatus.NORMAL.getCode());
				pdl.setPunchStatus(PunchStatus.NORMAL.getCode());
			}
			// 添加上班记录到日记录
			punchLogDTO.setClockStatus(ClockStatus.ARRIVE.getCode());
			pdl.getPunchLogs().add(punchLogDTO);
			// 下班
			endtime.setTime(datetimeSF.parse(dateSF.format(punchLogs.get(0)
					.getPunchDate())
					+ " "
					+ timeSF.format(punchRule.getEndTime())));
			punchLogDTO = marcheLogsAfterTime(punchLogs, endtime);
			if (null == punchLogDTO) {
				// 下班时间后没有记录
				earlytime.setTime(punchRule.getLateArriveTime());
				endtime.add(Calendar.HOUR, -earlytime.get(Calendar.HOUR));
				endtime.add(Calendar.MINUTE, -earlytime.get(Calendar.MINUTE));
				endtime.add(Calendar.SECOND, -earlytime.get(Calendar.SECOND));
				punchLogDTO = marcheLogsAfterTime(punchLogs, endtime);
				if (null == punchLogDTO) {
					// 下班时间-早退时间前没有记录-未打卡
					punchLogDTO = new PunchLogDTO();
					punchLogDTO.setPunchStatus(PunchStatus.UNPUNCH.getCode());
					pdl.setPunchStatus(PunchStatus.UNPUNCH.getCode());
				} else {
					// 下班时间-早退时间前有记录-早退
					punchLogDTO
							.setPunchStatus(PunchStatus.LEAVEEARLY.getCode());
					if (pdl.getPunchStatus() == PunchStatus.NORMAL.getCode())
						pdl.setPunchStatus(PunchStatus.LEAVEEARLY.getCode());
				}
			} else {
				// 下班时间前有记录-正常
				punchLogDTO.setPunchStatus(PunchStatus.NORMAL.getCode());
			}
			// 添加上班记录到日记录
			punchLogDTO.setClockStatus(ClockStatus.LEAVE.getCode());
			pdl.getPunchLogs().add(punchLogDTO);

		} else {
			// 弹性工作
			// 上班
			starttime.setTime(datetimeSF.parse(dateSF.format(punchLogs.get(0)
					.getPunchDate())
					+ " "
					+ timeSF.format(punchRule.getEndTime())));
			PunchLogDTO punchLogDTO = marcheLogsBeforeTime(punchLogs, starttime);
			if (null == punchLogDTO) {
				// 上班时间前没有记录
				latetime.setTime(punchRule.getLateArriveTime());
				starttime.add(Calendar.HOUR, latetime.get(Calendar.HOUR));
				starttime.add(Calendar.MINUTE, latetime.get(Calendar.MINUTE));
				starttime.add(Calendar.SECOND, latetime.get(Calendar.SECOND));
				punchLogDTO = marcheLogsBeforeTime(punchLogs, starttime);
				if (null == punchLogDTO) {
					// 上班时间+迟到时间前没有记录-未打卡
					punchLogDTO = new PunchLogDTO();
					punchLogDTO.setPunchStatus(PunchStatus.UNPUNCH.getCode());
					pdl.setPunchStatus(PunchStatus.UNPUNCH.getCode());
				} else {
					// 上班时间+迟到时间前有记录-迟到
					punchLogDTO.setPunchStatus(PunchStatus.BELATE.getCode());
					pdl.setPunchStatus(PunchStatus.BELATE.getCode());
				}
			} else {
				// 上班时间前有记录-正常
				punchLogDTO.setPunchStatus(PunchStatus.NORMAL.getCode());
				pdl.setPunchStatus(PunchStatus.NORMAL.getCode());
			}
			// 添加上班记录到日记录
			punchLogDTO.setClockStatus(ClockStatus.ARRIVE.getCode());
			pdl.getPunchLogs().add(punchLogDTO);
			// 下班
			if (pdl.getPunchStatus() == PunchStatus.NORMAL.getCode()) {
				// 如果上班的状态是正常 判断实际上班时间和最早上班时间的关系
				endtime.setTime(new Date(punchLogDTO.getPunchTime()));// 实际上班时间
				starttime.setTime(datetimeSF.parse(dateSF.format(punchLogs.get(
						0).getPunchDate())
						+ " " + timeSF.format(punchRule.getStartTime())));// 最早上班时间
				if (endtime.before(starttime)) {
					// 如果实际上班时间小于最早上班时间,则用最早上班时间计算
					endtime.setTime(starttime.getTime());
				}
				// 如果实际上班时间不小于最早上班时间，则用实际上班时间计算
			} else {
				// 如果上班的状态是迟到和未打卡，则下班时间是通过最晚上班时间计算
				endtime.setTime(datetimeSF.parse(dateSF.format(punchLogs.get(0)
						.getPunchDate())
						+ " "
						+ timeSF.format(punchRule.getEndTime())));
			}
			// 增加工作时间则为应该下班时间
			Calendar workCalendar = Calendar.getInstance();
			workCalendar.setTime(punchRule.getWorkTime());
			endtime.add(Calendar.HOUR, workCalendar.get(Calendar.HOUR));
			endtime.add(Calendar.MINUTE, workCalendar.get(Calendar.MINUTE));
			endtime.add(Calendar.SECOND, workCalendar.get(Calendar.SECOND));
			// 其余和非弹性一样
			punchLogDTO = marcheLogsAfterTime(punchLogs, endtime);
			if (null == punchLogDTO) {
				// 下班时间后没有记录 减少允许早退时间
				earlytime.setTime(punchRule.getLateArriveTime());
				endtime.add(Calendar.HOUR, -earlytime.get(Calendar.HOUR));
				endtime.add(Calendar.MINUTE, -earlytime.get(Calendar.MINUTE));
				endtime.add(Calendar.SECOND, -earlytime.get(Calendar.SECOND));
				punchLogDTO = marcheLogsAfterTime(punchLogs, endtime);
				if (null == punchLogDTO) {
					// 下班时间-早退时间前没有记录-未打卡
					punchLogDTO = new PunchLogDTO();
					punchLogDTO.setPunchStatus(PunchStatus.UNPUNCH.getCode());
					pdl.setPunchStatus(PunchStatus.UNPUNCH.getCode());
				} else {
					// 下班时间-早退时间前有记录-早退
					punchLogDTO
							.setPunchStatus(PunchStatus.LEAVEEARLY.getCode());
					if (pdl.getPunchStatus() == PunchStatus.NORMAL.getCode())
						pdl.setPunchStatus(PunchStatus.LEAVEEARLY.getCode());
				}
			} else {
				// 下班时间前有记录-正常
				punchLogDTO.setPunchStatus(PunchStatus.NORMAL.getCode());
			}
			// 添加上班记录到日记录
			punchLogDTO.setClockStatus(ClockStatus.LEAVE.getCode());
			pdl.getPunchLogs().add(punchLogDTO);
		}

		// 再看一天打几次卡
		if (punchTimes.fromCode(punchRule.getPunchTimes()) == punchTimes.FORTH) {
			// 一天四次卡
			// 中午下班时间到上班时间是否有打卡
			Calendar noonCalendar = Calendar.getInstance();
			Calendar afternoonCalendar = Calendar.getInstance();
			noonCalendar.setTime(datetimeSF.parse(dateSF.format(punchLogs
					.get(0).getPunchDate())
					+ " "
					+ timeSF.format(punchRule.getNoonEndTime())));
			afternoonCalendar.setTime(datetimeSF.parse(dateSF.format(punchLogs
					.get(0).getPunchDate())
					+ " "
					+ timeSF.format(punchRule.getAfternoonStartTime())));
			List<PunchLogDTO> PunchLogDTOs = marcheLogsBetweenTime(punchLogs,
					noonCalendar, afternoonCalendar);

			if (null == PunchLogDTOs || PunchLogDTOs.size() == 0) {
				// 一次打卡都没
				PunchLogDTO noonDTO = new PunchLogDTO();
				noonDTO.setClockStatus(ClockStatus.NOONLEAVE.getCode());
				noonDTO.setPunchStatus(PunchStatus.UNPUNCH.getCode());
				pdl.getPunchLogs().add(noonDTO);
				PunchLogDTO afternoonDTO = new PunchLogDTO();
				afternoonDTO.setClockStatus(ClockStatus.NOONLEAVE.getCode());
				afternoonDTO.setPunchStatus(PunchStatus.UNPUNCH.getCode());
				pdl.getPunchLogs().add(afternoonDTO);
				if (pdl.getPunchStatus() == PunchStatus.NORMAL.getCode())
					pdl.setPunchStatus(PunchStatus.UNPUNCH.getCode());
			} else if (PunchLogDTOs.size() == 1) {
				// 1次打卡
				pdl.getPunchLogs().addAll(PunchLogDTOs);
				PunchLogDTO afternoonDTO = new PunchLogDTO();
				afternoonDTO.setClockStatus(ClockStatus.NOONLEAVE.getCode());
				afternoonDTO.setPunchStatus(PunchStatus.UNPUNCH.getCode());
				pdl.getPunchLogs().add(afternoonDTO);
				if (pdl.getPunchStatus() == PunchStatus.NORMAL.getCode())
					pdl.setPunchStatus(PunchStatus.UNPUNCH.getCode());
			} else {
				// 2次打卡
				pdl.getPunchLogs().addAll(PunchLogDTOs);
			}
		}
		// 一天两次卡就走

		return pdl;
	}

	private List<PunchLogDTO> marcheLogsBetweenTime(List<PunchLog> punchLogs,
			Calendar beginCalendar, Calendar endCalendar) {
		List<PunchLogDTO> result = new ArrayList<PunchLogDTO>();
		Calendar logCalendar = Calendar.getInstance();
		Calendar maxCalendar = Calendar.getInstance();
		Calendar minCalendar = Calendar.getInstance();
		for (PunchLog punchlog : punchLogs) {
			logCalendar.setTime(punchlog.getPunchTime());
			// 打卡记录在时间之前
			if (logCalendar.after(beginCalendar)
					&& logCalendar.before(endCalendar)) {
				if (result.size() == 0) {
					result.add(new PunchLogDTO());
					minCalendar = logCalendar;
				} else if (result.size() == 1) {
					result.add(new PunchLogDTO());
					if (minCalendar.before(logCalendar))
						maxCalendar = logCalendar;
					else {
						maxCalendar = minCalendar;
						minCalendar = logCalendar;
					}
				} else {
					if (logCalendar.before(minCalendar))
						minCalendar = logCalendar;
					if (logCalendar.after(maxCalendar))
						maxCalendar = logCalendar;
				}
			}
		}
		if (result.size() == 0) {
			result = null;
		} else if (result.size() == 1) {
			result.get(0).setPunchTime(minCalendar.getTime().getTime());
			result.get(0).setClockStatus(ClockStatus.NOONLEAVE.getCode());
			result.get(0).setPunchStatus(PunchStatus.NORMAL.getCode());
		} else {

			result.get(0).setPunchTime(minCalendar.getTime().getTime());
			result.get(0).setClockStatus(ClockStatus.NOONLEAVE.getCode());
			result.get(0).setPunchStatus(PunchStatus.NORMAL.getCode());
			result.get(1).setPunchTime(maxCalendar.getTime().getTime());
			result.get(1).setClockStatus(ClockStatus.AFTERNOONARRIVE.getCode());
			result.get(1).setPunchStatus(PunchStatus.NORMAL.getCode());
		}
		return result;
	}

	private PunchLogDTO marcheLogsAfterTime(List<PunchLog> punchLogs,
			Calendar compareCalendar) {
		PunchLogDTO result = null;
		Calendar logCalendar = Calendar.getInstance();
		Calendar maxCalendar = Calendar.getInstance();
		for (PunchLog punchlog : punchLogs) {
			logCalendar.setTime(punchlog.getPunchTime());
			// 打卡记录在时间之前
			if (logCalendar.after(compareCalendar)) {
				if (result == null) {
					result = new PunchLogDTO();
					result.setPunchTime(logCalendar.getTime().getTime());
					maxCalendar = logCalendar;
				} else if (logCalendar.after(maxCalendar)) {
					result.setPunchTime(logCalendar.getTime().getTime());
					maxCalendar = logCalendar;
				}
			}
		}
		return result;
	}

	private PunchLogDTO marcheLogsBeforeTime(List<PunchLog> punchLogs,
			Calendar compareCalendar) {
		PunchLogDTO result = null;
		Calendar logCalendar = Calendar.getInstance();
		Calendar minCalendar = Calendar.getInstance();
		for (PunchLog punchlog : punchLogs) {
			logCalendar.setTime(punchlog.getPunchTime());
			// 打卡记录在时间之前
			if (logCalendar.before(compareCalendar)) {
				if (result == null) {
					result = new PunchLogDTO();
					result.setPunchTime(logCalendar.getTime().getTime());
					minCalendar = logCalendar;
				} else if (logCalendar.before(minCalendar)) {
					result.setPunchTime(logCalendar.getTime().getTime());
					minCalendar = logCalendar;
				}
			}
		}
		return result;
	}

	@Override
	public String createPunchLog(PunchClockCommand cmd) {

		 if(cmd.getCompanyId() == null || cmd.getCompanyId().equals(0L) )
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER, 
	                    "Invalid companyId parameter in the command");
		 if(cmd.getLatitude() == null || cmd.getLatitude().equals(0))
	            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
	                    "Invalid Latitude parameter in the command");
		 if(cmd.getLongitude() == null || cmd.getLongitude().equals(0) )
	            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
	                    "Invalid Longitude parameter in the command");
		
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
		return punchTime;
	}
	/***return 两个坐标之间的距离 单位 米*/
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

}
