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
import com.everhomes.util.RuntimeErrorException;

@Service
public class PunchServiceImpl implements PunchService {

	@Autowired
	PunchProvider punchProvider;

	@Override
	public PunchLogsYearListResponse getlistPunchLogs(Long userId,
			ListPunchLogsCommand cmd) {
		SimpleDateFormat dateSF = new SimpleDateFormat("yyyy-MM-dd");
		if (cmd.getCompanyId() == null)
			throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid company Id parameter in the command");
		if (cmd.getQueryYear() == null || cmd.getQueryYear().isEmpty())
			throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid queryYear parameter in the command");

		PunchRules punchRule = punchProvider.getPunchRuleByCompanyId(cmd
				.getCompanyId());
		if (null == punchRule)
			throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid company Id parameter in the command ");
		PunchLogsYearListResponse pyl = new PunchLogsYearListResponse();
		pyl.setPunch_year(cmd.getQueryYear());
		PunchLogsMonthListResponse pml = null;
		pyl.setPunchLogsMonthListResponses(new ArrayList<PunchLogsMonthListResponse>());
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
		List<java.sql.Date> dateList = punchProvider.listPunchLogsBwteenTwoDay(userId,cmd.getCompanyId(),dateSF.format(start.getTime()), dateSF.format(end.getTime()));
		
		for(java.sql.Date date : dateList) {
			start.setTime(date);
			if (null == pml) {
				// 如果pml为空，即是循环第一次，建立新的pml
				pml = new PunchLogsMonthListResponse();
				pml.setPunch_month(String.valueOf(start.get(Calendar.MONTH) + 1));
				pml.setPunchLogsDayListInfos(new ArrayList<PunchLogsDayListInfo>());
				pyl.getPunchLogsMonthListResponses().add(pml);
			} else if (!pml.getPunch_month().equals(
					String.valueOf(start.get(Calendar.MONTH)+1))) {
				// 如果pml的月份和start不一样，建立新的pml
				pml = new PunchLogsMonthListResponse();
				pml.setPunch_month(String.valueOf(start.get(Calendar.MONTH)+ 1));
				pml.setPunchLogsDayListInfos(new ArrayList<PunchLogsDayListInfo>());
				pyl.getPunchLogsMonthListResponses().add(pml);
			}
			List<PunchLogs> punchLogs = punchProvider
					.listPunchLogsByDate(userId,cmd.getCompanyId(),dateSF.format(start.getTime()));
			if (null == punchLogs|| punchLogs.size()==0) {
				continue;
			}
			PunchLogsDayListInfo pdl = new PunchLogsDayListInfo();
			pdl.setPunch_day(String.valueOf(start.get(Calendar.DAY_OF_MONTH)));
			pdl.setPunchLogs(new ArrayList<PunchLogDTO>());
			try{
			pml.getPunchLogsDayListInfos().add(
					makePunchLogsDayListInfo(punchLogs, punchRule, pdl));}
			catch(ParseException e){
				 
					throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
							ErrorCodes.ERROR_INVALID_PARAMETER,
							"punch Rule has somthing wrong");
			} 
//			start.add(Calendar.DAY_OF_MONTH, 1);
		}
		return pyl;
	}

	private PunchLogsDayListInfo makePunchLogsDayListInfo(
			List<PunchLogs> punchLogs, PunchRules punchRule,
			PunchLogsDayListInfo pdl) throws ParseException {

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
					punchLogDTO.setPunchStatus(PunchStatus.UNPUNCH);
					pdl.setPunchStatus(PunchStatus.UNPUNCH);
				} else {
					// 上班时间+迟到时间前有记录-迟到
					punchLogDTO.setPunchStatus(PunchStatus.BELATE);
					pdl.setPunchStatus(PunchStatus.BELATE);
				}
			} else {
				// 上班时间前有记录-正常
				punchLogDTO.setPunchStatus(PunchStatus.NORMAL);
				pdl.setPunchStatus(PunchStatus.NORMAL);
			}
			// 添加上班记录到日记录
			punchLogDTO.setClockStatus(ClockStatus.ARRIVE);
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
					punchLogDTO.setPunchStatus(PunchStatus.UNPUNCH);
					pdl.setPunchStatus(PunchStatus.UNPUNCH);
				} else {
					// 下班时间-早退时间前有记录-早退
					punchLogDTO.setPunchStatus(PunchStatus.LEAVEEARLY);
					if (pdl.getPunchStatus() == PunchStatus.NORMAL)
						pdl.setPunchStatus(PunchStatus.LEAVEEARLY);
				}
			} else {
				// 下班时间前有记录-正常
				punchLogDTO.setPunchStatus(PunchStatus.NORMAL);
			}
			// 添加上班记录到日记录
			punchLogDTO.setClockStatus(ClockStatus.LEAVE);
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
					punchLogDTO.setPunchStatus(PunchStatus.UNPUNCH);
					pdl.setPunchStatus(PunchStatus.UNPUNCH);
				} else {
					// 上班时间+迟到时间前有记录-迟到
					punchLogDTO.setPunchStatus(PunchStatus.BELATE);
					pdl.setPunchStatus(PunchStatus.BELATE);
				}
			} else {
				// 上班时间前有记录-正常
				punchLogDTO.setPunchStatus(PunchStatus.NORMAL);
				pdl.setPunchStatus(PunchStatus.NORMAL);
			}
			// 添加上班记录到日记录
			punchLogDTO.setClockStatus(ClockStatus.ARRIVE);
			pdl.getPunchLogs().add(punchLogDTO);
			// 下班
			if (pdl.getPunchStatus() == PunchStatus.NORMAL) {
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
					punchLogDTO.setPunchStatus(PunchStatus.UNPUNCH);
					pdl.setPunchStatus(PunchStatus.UNPUNCH);
				} else {
					// 下班时间-早退时间前有记录-早退
					punchLogDTO.setPunchStatus(PunchStatus.LEAVEEARLY);
					if (pdl.getPunchStatus() == PunchStatus.NORMAL)
						pdl.setPunchStatus(PunchStatus.LEAVEEARLY);
				}
			} else {
				// 下班时间前有记录-正常
				punchLogDTO.setPunchStatus(PunchStatus.NORMAL);
			}
			// 添加上班记录到日记录
			punchLogDTO.setClockStatus(ClockStatus.LEAVE);
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
				noonDTO.setClockStatus(ClockStatus.NOONLEAVE);
				noonDTO.setPunchStatus(PunchStatus.UNPUNCH);
				pdl.getPunchLogs().add(noonDTO);
				PunchLogDTO afternoonDTO = new PunchLogDTO();
				afternoonDTO.setClockStatus(ClockStatus.NOONLEAVE);
				afternoonDTO.setPunchStatus(PunchStatus.UNPUNCH);
				pdl.getPunchLogs().add(afternoonDTO);
				if (pdl.getPunchStatus() == PunchStatus.NORMAL)
					pdl.setPunchStatus(PunchStatus.UNPUNCH);
			} else if (PunchLogDTOs.size() == 1) {
				// 1次打卡
				pdl.getPunchLogs().addAll(PunchLogDTOs);
				PunchLogDTO afternoonDTO = new PunchLogDTO();
				afternoonDTO.setClockStatus(ClockStatus.NOONLEAVE);
				afternoonDTO.setPunchStatus(PunchStatus.UNPUNCH);
				pdl.getPunchLogs().add(afternoonDTO);
				if (pdl.getPunchStatus() == PunchStatus.NORMAL)
					pdl.setPunchStatus(PunchStatus.UNPUNCH);
			} else {
				// 2次打卡
				pdl.getPunchLogs().addAll(PunchLogDTOs);
			}
		}
		// 一天两次卡就走

		return pdl;
	}

	private List<PunchLogDTO> marcheLogsBetweenTime(List<PunchLogs> punchLogs,
			Calendar beginCalendar, Calendar endCalendar) { 
		List<PunchLogDTO> result = new ArrayList<PunchLogDTO>();
		Calendar logCalendar = Calendar.getInstance();
		Calendar maxCalendar = Calendar.getInstance();
		Calendar minCalendar = Calendar.getInstance();
		for (PunchLogs punchlog : punchLogs) {
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
			result.get(0).setClockStatus(ClockStatus.NOONLEAVE);
			result.get(0).setPunchStatus(PunchStatus.NORMAL);
		} else {

			result.get(0).setPunchTime(minCalendar.getTime().getTime());
			result.get(0).setClockStatus(ClockStatus.NOONLEAVE);
			result.get(0).setPunchStatus(PunchStatus.NORMAL);
			result.get(1).setPunchTime(maxCalendar.getTime().getTime());
			result.get(1).setClockStatus(ClockStatus.AFTERNOONARRIVE);
			result.get(1).setPunchStatus(PunchStatus.NORMAL);
		}
		return result;
	}

	private PunchLogDTO marcheLogsAfterTime(List<PunchLogs> punchLogs,
			Calendar compareCalendar) {
		PunchLogDTO result = null;
		Calendar logCalendar = Calendar.getInstance();
		Calendar maxCalendar = Calendar.getInstance();
		for (PunchLogs punchlog : punchLogs) {
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

	private PunchLogDTO marcheLogsBeforeTime(List<PunchLogs> punchLogs,
			Calendar compareCalendar) {
		PunchLogDTO result = null;
		Calendar logCalendar = Calendar.getInstance();
		Calendar minCalendar = Calendar.getInstance();
		for (PunchLogs punchlog : punchLogs) {
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
	public void createPunchLog(Long userId, Long companyId, String punchTime) {
 
		SimpleDateFormat datetimeSF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat dateSF = new SimpleDateFormat("yyyy-MM-dd");
		PunchLogs punchLog = new PunchLogs();
		punchLog.setCompanyId(companyId);
		punchLog.setUserId(userId);
		punchLog.setPunchTime(Timestamp.valueOf(punchTime)); 
		Calendar punCalendar = Calendar.getInstance();
		try {
			punCalendar.setTime(datetimeSF.parse(punchTime));
		} catch (ParseException e) {
		 
			e.printStackTrace();
		}
		if(punCalendar.get(Calendar.HOUR_OF_DAY)<5){
			//如果打卡时间小于5，用昨天
			punCalendar.add(Calendar.DATE, -1);
		}
		punchLog.setPunchDate(  java.sql.Date.valueOf(dateSF.format(punCalendar.getTime())));
		punchProvider.createPunchLog(punchLog);
	}

}
