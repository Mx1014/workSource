package com.everhomes.techpark.punch;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.spatial.geohash.GeoHashUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.enterprise.EnterpriseContact;
import com.everhomes.enterprise.EnterpriseContactProvider;
import com.everhomes.enterprise.EnterpriseContactService;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.organization.ListOrganizationContactCommand;
import com.everhomes.rest.organization.ListOrganizationMemberCommandResponse;
import com.everhomes.rest.organization.OrganizationGroupType;
import com.everhomes.rest.organization.OrganizationMemberDTO;
import com.everhomes.rest.organization.OrganizationMemberTargetType;
import com.everhomes.rest.techpark.company.ContactType;
import com.everhomes.rest.techpark.punch.AddPunchExceptionRequestCommand;
import com.everhomes.rest.techpark.punch.AddPunchRuleCommand;
import com.everhomes.rest.techpark.punch.ApprovalPunchExceptionCommand;
import com.everhomes.rest.techpark.punch.ApprovalStatus;
import com.everhomes.rest.techpark.punch.ClockCode;
import com.everhomes.rest.techpark.punch.DateStatus;
import com.everhomes.rest.techpark.punch.DeletePunchRuleCommand;
import com.everhomes.rest.techpark.punch.ExceptionProcessStatus;
import com.everhomes.rest.techpark.punch.ExceptionStatus;
import com.everhomes.rest.techpark.punch.ExportPunchStatisticsCommand;
import com.everhomes.rest.techpark.punch.GetDayPunchLogsCommand;
import com.everhomes.rest.techpark.punch.GetPunchNewExceptionCommand;
import com.everhomes.rest.techpark.punch.GetPunchNewExceptionCommandResponse;
import com.everhomes.rest.techpark.punch.GetPunchRuleCommand;
import com.everhomes.rest.techpark.punch.GetPunchRuleCommandResponse;
import com.everhomes.rest.techpark.punch.ListMonthPunchLogsCommand;
import com.everhomes.rest.techpark.punch.ListMonthPunchLogsCommandResponse;
import com.everhomes.rest.techpark.punch.ListPunchCountCommand;
import com.everhomes.rest.techpark.punch.ListPunchCountCommandResponse;
import com.everhomes.rest.techpark.punch.ListPunchExceptionApprovalCommand;
import com.everhomes.rest.techpark.punch.ListPunchExceptionRequestCommand;
import com.everhomes.rest.techpark.punch.ListPunchExceptionRequestCommandResponse;
import com.everhomes.rest.techpark.punch.ListPunchStatisticsCommand;
import com.everhomes.rest.techpark.punch.ListPunchStatisticsCommandResponse;
import com.everhomes.rest.techpark.punch.ListYearPunchLogsCommand;
import com.everhomes.rest.techpark.punch.ListYearPunchLogsCommandResponse;
import com.everhomes.rest.techpark.punch.PunchClockCommand;
import com.everhomes.rest.techpark.punch.PunchClockResponse;
import com.everhomes.rest.techpark.punch.PunchCountDTO;
import com.everhomes.rest.techpark.punch.PunchDayLogDTO;
import com.everhomes.rest.techpark.punch.PunchExceptionDTO;
import com.everhomes.rest.techpark.punch.PunchExceptionRequestDTO;
import com.everhomes.rest.techpark.punch.PunchGeoPointDTO;
import com.everhomes.rest.techpark.punch.PunchLogDTO;
import com.everhomes.rest.techpark.punch.PunchLogsDay;
import com.everhomes.rest.techpark.punch.PunchLogsMonthList;
import com.everhomes.rest.techpark.punch.PunchRquestType;
import com.everhomes.rest.techpark.punch.PunchRuleDTO;
import com.everhomes.rest.techpark.punch.PunchServiceErrorCode;
import com.everhomes.rest.techpark.punch.PunchStatisticsDTO;
import com.everhomes.rest.techpark.punch.PunchStatus;
import com.everhomes.rest.techpark.punch.PunchTimesPerDay;
import com.everhomes.rest.techpark.punch.UpdatePunchRuleCommand;
import com.everhomes.rest.techpark.punch.ViewFlags;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;

@Service
public class PunchServiceImpl implements PunchService {
	final String downloadDir ="\\download\\";
	private static final Logger LOGGER = LoggerFactory
			.getLogger(PunchServiceImpl.class);

	SimpleDateFormat timeSF = new SimpleDateFormat("HH:mm:ss");
	SimpleDateFormat dateSF = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat datetimeSF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Autowired
	private PunchProvider punchProvider;

	@Autowired
	private EnterpriseContactProvider enterpriseContactProvider;
	@Autowired
	private DbProvider dbProvider;
	
	@Autowired
	private ConfigurationProvider configurationProvider;
	
	@Autowired
	private OrganizationService organizationService;
	
	@Autowired
	private OrganizationProvider organizationProvider;

	

    
    @Autowired
    private CoordinationProvider coordinationProvider;
    
    
	private void checkCompanyIdIsNull(Long companyId) {
		if (null == companyId || companyId.equals(0L)) {
			LOGGER.error("Invalid company Id parameter in the command");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid company Id parameter in the command");
		}

	}
	/**
	 * <ul>审批后的状态
	 *<li>HALFOUTWORK(13):  半天公出</li>
	 *<li>HALFEXCHANGE(12):  半天调休</li>
	 *<li>HALFABSENCE(11):  半天病假</li>
	 *<li>HALFSICK(10):  半天事假</li>
	 *<li>OVERTIME(9):  加班</li>
	 *<li>OUTWORK(8):  公出</li>
	 * <li>EXCHANGE(7): 调休</li>
	 * <li>SICK(6): 病假</li>
	 * <li>ABSENCE(5): 事假</li>
	 * <li>BLANDLE(4): 迟到且早退</li>
	 * <li>UNPUNCH(3): 缺勤</li>
	 * <li>LEAVEEARLY(2): 早退</li>
	 * <li>BELATE(1): 迟到</li>
	 * <li>NORMAL(0): 正常</li>
	 * </ul>
	 */

	public String statusToString (Byte status){
		if(null == status){
			return "";
		}
		if(status.equals(ApprovalStatus.HALFABSENCE.getCode()))
			return "半天事假";
		if(status.equals(ApprovalStatus.HALFEXCHANGE.getCode()))
			return "半天调休";
		if(status.equals(ApprovalStatus.HALFOUTWORK.getCode()))
			return "半天公出";
		if(status.equals(ApprovalStatus.HALFSICK.getCode()))
			return "半天病假";
		if(status.equals(ApprovalStatus.OVERTIME.getCode()))
			return "加班";
		if(status.equals(ApprovalStatus.ABSENCE.getCode()))
			return "事假";
		if(status.equals(ApprovalStatus.EXCHANGE.getCode()))
			return "调休";
		if(status.equals(ApprovalStatus.OUTWORK.getCode()))
			return "公出";
		if(status.equals(ApprovalStatus.SICK.getCode()))
			return "病假";
		if(status.equals(ApprovalStatus.BELATE.getCode()))
			return "迟到";
		if(status.equals(ApprovalStatus.BLANDLE.getCode()))
			return "迟到且早退";
		if(status.equals(ApprovalStatus.UNPUNCH.getCode()))
			return "缺勤";
		if(status.equals(ApprovalStatus.LEAVEEARLY.getCode()))
			return "早退";
		if(status.equals(ApprovalStatus.NORMAL.getCode()))
			return "正常";
		return "";
		
	} 
	
	@Override
	public ListYearPunchLogsCommandResponse getlistPunchLogs(
			ListYearPunchLogsCommand cmd) {

		checkCompanyIdIsNull(cmd.getEnterpriseId());
		if (cmd.getQueryYear() == null || cmd.getQueryYear().isEmpty())
			throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid queryYear parameter in the command");

		ListYearPunchLogsCommandResponse pyl = new ListYearPunchLogsCommandResponse();
		pyl.setPunchYear(cmd.getQueryYear());
		pyl.setPunchLogsMonthList(new ArrayList<PunchLogsMonthList>());
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		LOGGER.debug("*****************debug : begin getlistPunchLogs line 87");
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
		// userId, cmd.getEnterpriseId(), dateSF.format(start.getTime()),
		// dateSF.format(end.getTime()));

		pyl = getlistPunchLogsBetweenTwoCalendar(pyl, cmd.getEnterpriseId(),
				start, end);
		return pyl;
	}

	@Override
	public ListYearPunchLogsCommandResponse getlistPunchLogsBetweenTwoCalendar(
			ListYearPunchLogsCommandResponse pyl, long CompanyId,
			Calendar start, Calendar end) {
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
				pml.setPunchLogsDayListInfos(new ArrayList<PunchLogsDay>());
				pyl.getPunchLogsMonthList().add(pml);
			} else if (!pml.getPunchMonth().equals(
					String.valueOf(start.get(Calendar.MONTH) + 1))) {
				// 如果pml的月份和start不一样，建立新的pml
				pml = new PunchLogsMonthList();
				pml.setPunchMonth(String.valueOf(start.get(Calendar.MONTH) + 1));
				pml.setPunchLogsDayListInfos(new ArrayList<PunchLogsDay>());
				pyl.getPunchLogsMonthList().add(pml);
			}

			try {
				PunchLogsDay pdl = makePunchLogsDayStatus(userId,
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

	private PunchLogsDay makePunchLogsDayStatus(Long userId,
			Long companyId, Calendar logDay) throws ParseException {
		PunchLogsDay pdl = new PunchLogsDay();
		pdl.setPunchDay(String.valueOf(logDay.get(Calendar.DAY_OF_MONTH)));
		// Long beginFunctionTimeLong = DateHelper.currentGMTTime().getTime();
		PunchDayLog punchDayLog = punchProvider.getDayPunchLogByDate(userId,
				companyId, dateSF.format(logDay.getTime()));
		if (null == punchDayLog) {
			// 插入数据
			punchDayLog = refreshPunchDayLog(userId, companyId, logDay);
			if (null == punchDayLog) {
				// 验证后为空
				return null;
			}
		}
		// Long endrefreshPunchDayLogTimeLong =
		// DateHelper.currentGMTTime().getTime();
		Date now = new Date();
		if(punchDayLog.getPunchTimesPerDay().equals(PunchTimesPerDay.TWICE.getCode())){
			pdl.setPunchStatus(punchDayLog.getStatus());
			 
			pdl.setExceptionStatus(punchDayLog.getStatus().equals(
						PunchStatus.NORMAL.getCode())||punchDayLog.getStatus().equals(
								PunchStatus.OVERTIME.getCode())  ? ExceptionStatus.NORMAL
						.getCode() : ExceptionStatus.EXCEPTION.getCode());
			 
			PunchExceptionApproval exceptionApproval = punchProvider
					.getPunchExceptionApprovalByDate(userId, companyId,
							dateSF.format(logDay.getTime()));
			if (null != exceptionApproval) {
				pdl.setApprovalStatus(exceptionApproval.getApprovalStatus());
				 
					// 如果有申报审批结果，并且审批结果和实际打卡结果有一个是正常的话，异常结果为正常 别的为异常
				pdl.setExceptionStatus(caculateExceptionCode(exceptionApproval.getApprovalStatus()) );
			 
			}
		}
		else if(punchDayLog.getPunchTimesPerDay().equals(PunchTimesPerDay.FORTH.getCode())) {
			pdl.setMorningPunchStatus(punchDayLog.getMorningStatus());
			pdl.setAfternoonPunchStatus(punchDayLog.getAfternoonStatus());
			pdl.setExceptionStatus((punchDayLog.getMorningStatus().equals(
						PunchStatus.NORMAL.getCode())||punchDayLog.getMorningStatus().equals(
								PunchStatus.OVERTIME.getCode())) &&(punchDayLog.getAfternoonStatus().equals(
										PunchStatus.NORMAL.getCode())||punchDayLog.getAfternoonStatus().equals(
												PunchStatus.OVERTIME.getCode()))? ExceptionStatus.NORMAL
						.getCode() : ExceptionStatus.EXCEPTION.getCode() );
			PunchExceptionApproval exceptionApproval = punchProvider.getPunchExceptionApprovalByDate(userId, companyId,
							dateSF.format(logDay.getTime()));
			if (null != exceptionApproval) {
				pdl.setMorningApprovalStatus(exceptionApproval.getMorningApprovalStatus());
				pdl.setAfternoonApprovalStatus(exceptionApproval.getAfternoonApprovalStatus());
				if (caculateExceptionCode(pdl.getAfternoonApprovalStatus()).equals(ExceptionStatus.NORMAL.getCode())
						&&caculateExceptionCode(pdl.getMorningApprovalStatus()).equals(ExceptionStatus.NORMAL.getCode())) {
				 
					pdl.setExceptionStatus(ExceptionStatus.NORMAL.getCode());
				}
			}
		 
		}
		// Long endFunctionTimeLong = DateHelper.currentGMTTime().getTime();
		// LOGGER.debug("************* "+dateSF.format(logDay.getTime())+"refreshPunchDayLog time : ["+(endrefreshPunchDayLogTimeLong-beginFunctionTimeLong)
		// +"]  makePunchLogsDayStatus time : ["+(endFunctionTimeLong-beginFunctionTimeLong)
		// +"] ");
		return pdl;
	}
	
	private Byte caculateExceptionCode(Byte status) {
		if(status == null) {
			return ExceptionStatus.NORMAL.getCode();
		}
		if(status.equals( ApprovalStatus.NORMAL.getCode())||status.equals( ApprovalStatus.OVERTIME.getCode())
				||status.equals( ApprovalStatus.EXCHANGE.getCode())||status.equals( ApprovalStatus.OVERTIME.getCode())
				||status.equals( ApprovalStatus.NORMAL.getCode())||status.equals( ApprovalStatus.OUTWORK.getCode())
				||status.equals( ApprovalStatus.SICK.getCode())||status.equals( ApprovalStatus.ABSENCE.getCode())
				||status.equals( ApprovalStatus.HALFSICK.getCode())||status.equals( ApprovalStatus.HALFABSENCE.getCode())
				||status.equals( ApprovalStatus.HALFEXCHANGE.getCode())||status.equals( ApprovalStatus.HALFOUTWORK.getCode()))
			return ExceptionStatus.NORMAL.getCode();
		
		return ExceptionStatus.EXCEPTION.getCode();
	}


	private PunchDayLog refreshPunchDayLog(Long userId, Long companyId,
			Calendar logDay) throws ParseException {
		PunchLogsDay pdl = new PunchLogsDay();
		pdl.setPunchDay(String.valueOf(logDay.get(Calendar.DAY_OF_MONTH)));
		pdl.setPunchLogs(new ArrayList<PunchLogDTO>());
		PunchDayLog newPunchDayLog = new PunchDayLog();
		
		pdl = caculateDayLog(userId, companyId, logDay, pdl,newPunchDayLog);
		if (null == pdl) {
			return null;
		}
		
		newPunchDayLog.setUserId(userId);
		newPunchDayLog.setEnterpriseId(companyId);
		newPunchDayLog.setCreatorUid(userId);
		newPunchDayLog.setPunchDate(java.sql.Date.valueOf(dateSF.format(logDay
				.getTime())));
		newPunchDayLog.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
				.getTime()));
		newPunchDayLog.setPunchTimesPerDay(pdl.getPunchTimesPerDay());
		newPunchDayLog.setStatus(pdl.getPunchStatus());
		newPunchDayLog.setMorningStatus(pdl.getMorningPunchStatus());
		newPunchDayLog.setAfternoonStatus(pdl.getAfternoonPunchStatus());
		newPunchDayLog.setViewFlag(ViewFlags.NOTVIEW.getCode());
		PunchDayLog punchDayLog = punchProvider.getDayPunchLogByDate(userId,
				companyId, dateSF.format(logDay.getTime()));
		if (null == punchDayLog) {
			// 数据库没有计算好的数据 
			punchProvider.createPunchDayLog(newPunchDayLog);

		} else {
			// 数据库有计算好的数据
			newPunchDayLog.setId(punchDayLog.getId());
			punchProvider.updatePunchDayLog(newPunchDayLog);
		}
		return newPunchDayLog;
	}

	private Time getDAOTime(Long arriveTime) {
		if (arriveTime.equals(0L)) {
			return java.sql.Time.valueOf(getGMTtimeString("HH:mm:ss",
					arriveTime));
		} else {
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
	@Override
	public PunchLogsDay makePunchLogsDayListInfo(Long userId,
			Long companyId, Calendar logDay) {
		Date now = new Date();
		

		PunchDayLog punchDayLog = punchProvider.getDayPunchLogByDate(userId,
				companyId, dateSF.format(logDay.getTime()));
		if (null == punchDayLog) {
			// 插入数据
			try {
				punchDayLog = refreshPunchDayLog(userId, companyId, logDay);
			} catch (ParseException e) {
				throw RuntimeErrorException.errorWith(
						PunchServiceErrorCode.SCOPE,
						PunchServiceErrorCode.ERROR_PUNCH_REFRESH_DAYLOG,
						"ERROR IN REFRESHPUNCHDAYLOG  LINE 353");
			}
			if (null == punchDayLog) {
				// 验证后为空
				return null;
			}
		} 
		PunchLogsDay pdl = ConvertHelper.convert(punchDayLog, PunchLogsDay.class) ;
		pdl.setPunchStatus(punchDayLog.getStatus());
		pdl.setMorningPunchStatus(punchDayLog.getMorningStatus());
		pdl.setAfternoonPunchStatus(punchDayLog.getAfternoonStatus()); 
		pdl.setPunchDay(String.valueOf(logDay.get(Calendar.DAY_OF_MONTH)));
		pdl.setPunchLogs(new ArrayList<PunchLogDTO>());
		List<PunchLog> punchLogs = punchProvider.listPunchLogsByDate(userId,
				companyId, dateSF.format(logDay.getTime()),
				ClockCode.SUCESS.getCode());
		for (PunchLog log : punchLogs){
			PunchLogDTO dto = new PunchLogDTO();
			dto.setPunchTime(log.getPunchTime().getTime());
			pdl.getPunchLogs().add(dto); 
		}
		pdl.setPunchStatus(punchDayLog.getStatus());
		// 如果是非工作日和当天，则异常为normal
		if (!isWorkDay(logDay.getTime())
				|| dateSF.format(now).equals(dateSF.format(logDay.getTime()))) {
			pdl.setExceptionStatus(ExceptionStatus.NORMAL.getCode());
		} else {
			if(pdl.getPunchTimesPerDay().equals(PunchTimesPerDay.TWICE.getCode())){
			pdl.setExceptionStatus(punchDayLog.getStatus().equals(
					PunchStatus.NORMAL.getCode())||punchDayLog.getStatus().equals(
							PunchStatus.OVERTIME.getCode()) ? ExceptionStatus.NORMAL
					.getCode() : ExceptionStatus.EXCEPTION.getCode());
			}
			else if(pdl.getPunchTimesPerDay().equals(PunchTimesPerDay.FORTH.getCode())){
				pdl.setExceptionStatus(punchDayLog.getMorningStatus().equals(
						PunchStatus.NORMAL.getCode())||punchDayLog.getMorningStatus().equals(
								PunchStatus.OVERTIME.getCode())||punchDayLog.getAfternoonStatus().equals(
										PunchStatus.NORMAL.getCode())||punchDayLog.getAfternoonStatus().equals(
												PunchStatus.OVERTIME.getCode()) ? ExceptionStatus.NORMAL
						.getCode() : ExceptionStatus.EXCEPTION.getCode());
				 
				
			}
		}

		List<PunchExceptionRequest> exceptionRequests = punchProvider
				.listExceptionRequestsByDate(userId, companyId,
						dateSF.format(logDay.getTime()));
		if (exceptionRequests.size() > 0) {
			for (PunchExceptionRequest exceptionRequest : exceptionRequests) {

				PunchExceptionDTO punchExceptionDTO = ConvertHelper.convert(exceptionRequest , PunchExceptionDTO.class);

				punchExceptionDTO.setRequestType(exceptionRequest
						.getRequestType());
				punchExceptionDTO.setCreateTime(exceptionRequest
						.getCreateTime().getTime());
				if (exceptionRequest.getRequestType().equals(
						PunchRquestType.REQUEST.getCode())) {
					// 对于申请
					punchExceptionDTO.setExceptionComment(exceptionRequest
							.getDescription());
					OrganizationMember member = organizationProvider.findOrganizationMemberByOrgIdAndUId(exceptionRequest.getUserId(), companyId);

					if (null == member) {
						punchExceptionDTO.setName("无此人");
					} else {
						punchExceptionDTO.setName(member.getContactName());
					}
				} else {
					// 审批
					punchExceptionDTO.setExceptionComment(exceptionRequest
							.getProcessDetails());
					OrganizationMember member = organizationProvider.findOrganizationMemberByOrgIdAndUId(exceptionRequest.getOperatorUid(), companyId);

					if (null == member) {
						punchExceptionDTO.setName("无此人");
					} else {
						punchExceptionDTO.setName(member.getContactName());
					}
				}
				if (null == pdl.getPunchExceptionDTOs()) {
					pdl.setPunchExceptionDTOs(new ArrayList<PunchExceptionDTO>());
				}
				pdl.getPunchExceptionDTOs().add(punchExceptionDTO);
			}
		}
		return pdl;
	}

	/***
	 * 计算每一天的打卡状态，返回值PDL
	 * @param punchDayLog 
	 * */
	private PunchLogsDay caculateDayLog(Long userId, Long companyId,
			Calendar logDay, PunchLogsDay pdl, PunchDayLog punchDayLog) throws ParseException {
		List<PunchLog> punchLogs = punchProvider.listPunchLogsByDate(userId,
				companyId, dateSF.format(logDay.getTime()),
				ClockCode.SUCESS.getCode());
		Date now = new Date();
		
		PunchRule punchRule = punchProvider.getPunchRuleByCompanyId(companyId);
		
		if (null == punchRule)
			throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"公司还没有打卡规则 ");
		pdl.setPunchTimesPerDay(punchRule.getPunchTimesPerDay());
		// 如果零次打卡记录
		if (null == punchLogs || punchLogs.size() == 0) {
			if (!isWorkDay(logDay.getTime())|| dateSF.format(now).equals(dateSF.format(logDay.getTime()))) {
				// 如果非工作日或者当天，不增pdl直接下一天
				return null;
			}
			pdl.setPunchStatus(PunchStatus.UNPUNCH.getCode());
			pdl.setMorningPunchStatus(PunchStatus.UNPUNCH.getCode());
			pdl.setAfternoonPunchStatus(PunchStatus.UNPUNCH.getCode());
			pdl.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode()); 
			makeExceptionForDayList(userId, companyId, logDay, pdl);
			return pdl;
		}

		for (PunchLog log : punchLogs){
			pdl.getPunchLogs().add(ConvertHelper.convert(log,PunchLogDTO.class ));
		}
		if(PunchTimesPerDay.TWICE.getCode().equals(punchRule.getPunchTimesPerDay())){
//			if (punchLogs.size() == 1) {
//				// 如果只有一次打卡，就把离开设置为未打卡,当天设置为旷工
//				PunchLogDTO arriveLogDTO = new PunchLogDTO();
//				arriveLogDTO.setPunchTime(punchLogs.get(0).getPunchTime().getTime());
//				pdl.getPunchLogs().add(arriveLogDTO);
//				PunchLogDTO noPunchLogDTO2 = new PunchLogDTO();
//				pdl.getPunchLogs().add(noPunchLogDTO2);
//				if (!isWorkDay(logDay.getTime())){
//					pdl.setPunchStatus(PunchStatus.OVERTIME.getCode());
//					pdl.setExceptionStatus(ExceptionStatus.NORMAL.getCode());
//					// 如果非工作日 normal
//					return pdl;
//				}
//				if (dateSF.format(now).equals(
//								dateSF.format(logDay.getTime()))) {
//					pdl.setPunchStatus(PunchStatus.UNPUNCH.getCode());
//					pdl.setExceptionStatus(ExceptionStatus.NORMAL.getCode());
//					// 如果非工作日 normal
//					return pdl;
//				}
//				pdl.setPunchStatus(PunchStatus.UNPUNCH.getCode());
//				pdl.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
//				makeExceptionForDayList(userId, companyId, logDay, pdl);
//				return pdl;
//			}
			
			Calendar startMinTime = Calendar.getInstance();
			Calendar startMaxTime = Calendar.getInstance();
			Calendar workTime = Calendar.getInstance();
			startMinTime.setTime(datetimeSF.parse(dateSF.format(punchLogs.get(0).getPunchDate())+ " "
					+ timeSF.format(punchRule.getStartEarlyTime())));
	
			startMaxTime.setTime(datetimeSF.parse(dateSF.format(punchLogs.get(0).getPunchDate())+ " "
					+ timeSF.format(punchRule.getStartLateTime())));
	
			workTime.setTime(datetimeSF.parse(dateSF.format(punchLogs.get(0)
					.getPunchDate()) + " " + timeSF.format(punchRule.getWorkTime())));
			List<Calendar> punchMinAndMaxTime = getMinAndMaxTimeFromPunchlogs(punchLogs);
			Calendar arriveCalendar = punchMinAndMaxTime.get(0);
			Calendar leaveCalendar = punchMinAndMaxTime.get(1);
			Time leaveCalendarTime = Time.valueOf(timeSF.format(leaveCalendar.getTime()));
			Time arriveCalendarTime = Time.valueOf(timeSF.format(arriveCalendar.getTime()));
			Time AfternoonArriveTimeTime = Time.valueOf(timeSF.format(punchRule.getAfternoonArriveTime()));
			Time NoonLeaveTimeTime = Time.valueOf(timeSF.format(punchRule.getNoonLeaveTime()));
			long realWorkTime = 0L;
			if(leaveCalendarTime.after(AfternoonArriveTimeTime)&&arriveCalendarTime.before(NoonLeaveTimeTime)){
				realWorkTime =leaveCalendar.getTimeInMillis() - arriveCalendar.getTimeInMillis()
					-punchRule.getAfternoonArriveTime().getTime() +punchRule.getNoonLeaveTime().getTime();
			}else {
				realWorkTime =leaveCalendar.getTimeInMillis() - arriveCalendar.getTimeInMillis();
						
			}
			punchDayLog.setArriveTime(getDAOTime(arriveCalendar.getTimeInMillis()));
			punchDayLog.setLeaveTime(getDAOTime(leaveCalendar.getTimeInMillis() )); 
			punchDayLog.setWorkTime(convertTime(realWorkTime));
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
						if (pdl.getPunchStatus().equals(PunchStatus.NORMAL.getCode())) {
							pdl.setPunchStatus(PunchStatus.LEAVEEARLY.getCode());
							pdl.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
						} else {
							pdl.setPunchStatus(PunchStatus.BLANDLE.getCode());
							pdl.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
						}
					}
				} else {
					if (leaveCalendar.after(startMinTime)) {
	
					} else {
						if (pdl.getPunchStatus().equals(PunchStatus.NORMAL.getCode())) {
							pdl.setPunchStatus(PunchStatus.LEAVEEARLY.getCode());
							pdl.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
						} else {
							pdl.setPunchStatus(PunchStatus.BLANDLE.getCode());
							pdl.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
						}
					}
				}

			
			}
			// 如果是当日，则设置打卡考勤为正常并返回
			if (!isWorkDay(logDay.getTime())){
				pdl.setPunchStatus(PunchStatus.OVERTIME.getCode());
				pdl.setExceptionStatus(ExceptionStatus.NORMAL.getCode()); 
				return pdl;
			}
		}
		else if(PunchTimesPerDay.FORTH.getCode().equals(punchRule.getPunchTimesPerDay())){
			 //计算计算日中午十三点
			Calendar noonAnchor = Calendar.getInstance();
			noonAnchor.setTime(logDay.getTime());
			noonAnchor.set(Calendar.HOUR_OF_DAY, 13);
			noonAnchor.set(Calendar.MINUTE, 0);
			noonAnchor.set(Calendar.SECOND, 0);
			noonAnchor.set(Calendar.MILLISECOND, 0);
			//将十三点之前的作为一个morningList，十三点之后的作为一个afternoonList
			List<PunchLog> morningLogs = new ArrayList<PunchLog>();
			List<PunchLog> afternoonLogs = new ArrayList<PunchLog>();
			for(PunchLog punchLog : punchLogs){
				if(punchLog.getPunchTime().before(noonAnchor.getTime())){
					morningLogs.add(punchLog);
				}
				else{
					afternoonLogs.add(punchLog);
				}
			}
			//分别计算morning和afternoon的状态
			// 如果上午零次打卡记录
			if (null == morningLogs || morningLogs.size() == 0) {
				pdl.setMorningPunchStatus(PunchStatus.UNPUNCH.getCode());
				pdl.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode()); 
			}
			// 如果上午零次打卡记录
			if (null == afternoonLogs || afternoonLogs.size() == 0) {
				pdl.setAfternoonPunchStatus(PunchStatus.UNPUNCH.getCode());
				pdl.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode()); 	
			}
			Calendar startMinTime = Calendar.getInstance();
			Calendar startMaxTime = Calendar.getInstance();
			Calendar noonLeaveTime = Calendar.getInstance();
			Calendar afternoonArriveTime = Calendar.getInstance();
			Calendar workTime = Calendar.getInstance();
			Calendar arriveCalendar = Calendar.getInstance();
			startMinTime.setTime(datetimeSF.parse(dateSF.format(punchLogs.get(0).getPunchDate())+ " "
					+ timeSF.format(punchRule.getStartEarlyTime())));
			startMaxTime.setTime(datetimeSF.parse(dateSF.format(punchLogs.get(0).getPunchDate())+ " "
					+ timeSF.format(punchRule.getStartLateTime())));
			noonLeaveTime.setTime(datetimeSF.parse(dateSF.format(punchLogs.get(0).getPunchDate())+ " "
					+ timeSF.format(punchRule.getNoonLeaveTime())));
			afternoonArriveTime.setTime(datetimeSF.parse(dateSF.format(punchLogs.get(0).getPunchDate())+ " "
					+ timeSF.format(punchRule.getAfternoonArriveTime())));
			workTime.setTime(datetimeSF.parse(dateSF.format(punchLogs.get(0)
					.getPunchDate()) + " " + timeSF.format(punchRule.getWorkTime())));
			long realWorkTime = 0L;
			if(null == pdl.getMorningPunchStatus()){
				
				
				List<Calendar> punchMinAndMaxTime = getMinAndMaxTimeFromPunchlogs(morningLogs);
				arriveCalendar = punchMinAndMaxTime.get(0); 
				realWorkTime = punchMinAndMaxTime.get(1).getTimeInMillis()- punchMinAndMaxTime.get(0).getTimeInMillis();
				punchDayLog.setArriveTime(getDAOTime(arriveCalendar.getTimeInMillis()));
				punchDayLog.setNoonLeaveTime( getDAOTime(punchMinAndMaxTime.get(1).getTimeInMillis()));
				 
				// 打卡状态设置为正常或者迟到
				if (punchMinAndMaxTime.get(0).before(startMaxTime)) {
					if (punchMinAndMaxTime.get(1).after(noonLeaveTime)) {
						pdl.setMorningPunchStatus(PunchStatus.NORMAL.getCode());
						pdl.setExceptionStatus(ExceptionStatus.NORMAL.getCode());
					}
					else{
						pdl.setMorningPunchStatus(PunchStatus.LEAVEEARLY.getCode());
						pdl.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
					}
				} else {
					if (punchMinAndMaxTime.get(1).after(noonLeaveTime)) {
						pdl.setMorningPunchStatus(PunchStatus.BELATE.getCode());
						pdl.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
					}
					else{
						pdl.setMorningPunchStatus(PunchStatus.BLANDLE.getCode());
						pdl.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
					}
				}
			}
			// 下午
			startMinTime.add(Calendar.HOUR, workTime.get(Calendar.HOUR));
			startMinTime.add(Calendar.MINUTE, workTime.get(Calendar.MINUTE));
			startMinTime.add(Calendar.SECOND, workTime.get(Calendar.SECOND));
			startMaxTime.add(Calendar.HOUR, workTime.get(Calendar.HOUR));
			startMaxTime.add(Calendar.MINUTE, workTime.get(Calendar.MINUTE));
			startMaxTime.add(Calendar.SECOND, workTime.get(Calendar.SECOND));
			arriveCalendar.add(Calendar.HOUR, workTime.get(Calendar.HOUR));
			arriveCalendar.add(Calendar.MINUTE, workTime.get(Calendar.MINUTE));
			arriveCalendar.add(Calendar.SECOND, workTime.get(Calendar.SECOND));
			Calendar afternoonLeaveTime = null;
			if(null == pdl.getAfternoonPunchStatus()){
				if(pdl.getMorningPunchStatus().equals(PunchStatus.UNPUNCH.getCode())){
					afternoonLeaveTime = startMinTime;
				}else {
					 if(startMinTime.after(arriveCalendar)){
						 afternoonLeaveTime = startMinTime;}
					 else if (startMaxTime.after(arriveCalendar)){
						 afternoonLeaveTime = arriveCalendar;}
					 else {
						 afternoonLeaveTime = startMaxTime;
					}
						 
				}
				List<Calendar>  punchMinAndMaxTime = getMinAndMaxTimeFromPunchlogs(afternoonLogs);
				if (punchMinAndMaxTime.get(0).before(afternoonArriveTime)) {
					if (punchMinAndMaxTime.get(1).after(afternoonLeaveTime)) {
						pdl.setAfternoonPunchStatus(PunchStatus.NORMAL.getCode());
						pdl.setExceptionStatus(ExceptionStatus.NORMAL.getCode());
					}
					else{
						pdl.setAfternoonPunchStatus(PunchStatus.LEAVEEARLY.getCode());
						pdl.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
					}
				} else {
					if (punchMinAndMaxTime.get(1).after(afternoonLeaveTime)) {
						pdl.setAfternoonPunchStatus(PunchStatus.BELATE.getCode());
						pdl.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
					}
					else{
						pdl.setAfternoonPunchStatus(PunchStatus.BLANDLE.getCode());
						pdl.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
					}
				}
				realWorkTime = realWorkTime + punchMinAndMaxTime.get(1).getTimeInMillis()- punchMinAndMaxTime.get(0).getTimeInMillis();
				punchDayLog.setAfternoonArriveTime(getDAOTime(punchMinAndMaxTime.get(0).getTimeInMillis()));
				punchDayLog.setLeaveTime(getDAOTime(punchMinAndMaxTime.get(1).getTimeInMillis()));
				punchDayLog.setWorkTime(convertTime(realWorkTime));
			}
			// 如果是当日，则设置打卡考勤为正常并返回
			if (!isWorkDay(logDay.getTime())){
				pdl.setMorningPunchStatus(PunchStatus.OVERTIME.getCode());
				pdl.setAfternoonPunchStatus(PunchStatus.OVERTIME.getCode());
				pdl.setExceptionStatus(ExceptionStatus.NORMAL.getCode()); 
				return pdl;
			}
							
						
		}
		makeExceptionForDayList(userId, companyId, logDay, pdl);
		return pdl;
	}
	/***
	 * 组装异常申报和回复的记录
	 * 更改异常标志
	 * */
	private void makeExceptionForDayList(Long userId, Long companyId,
			Calendar logDay, PunchLogsDay pdl) {
		// TODO： 四次打卡和加班
		PunchExceptionApproval exceptionApproval = punchProvider
				.getPunchExceptionApprovalByDate(userId, companyId,
						dateSF.format(logDay.getTime()));
		if (null != exceptionApproval) {
			if (pdl.getPunchTimesPerDay().equals(PunchTimesPerDay.TWICE.getCode())){
				pdl.setApprovalStatus(exceptionApproval.getApprovalStatus());
				if (pdl.getApprovalStatus().equals(ApprovalStatus.NORMAL.getCode())
						|| pdl.getPunchStatus().equals(PunchStatus.NORMAL.getCode())
						|| pdl.getPunchStatus().equals(PunchStatus.OVERTIME.getCode())) {
					// 如果有申报审批结果，并且审批结果和实际打卡结果有一个是正常的话，异常结果为正常 别的为异常
					pdl.setExceptionStatus(ExceptionStatus.NORMAL.getCode());
				} else {
					pdl.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
				}		
			}
			else if(pdl.getPunchTimesPerDay().equals(PunchTimesPerDay.FORTH.getCode())) {
				pdl.setMorningApprovalStatus(exceptionApproval.getMorningApprovalStatus());	
				pdl.setAfternoonApprovalStatus(exceptionApproval.getAfternoonApprovalStatus());
				if(pdl.getMorningApprovalStatus().equals(ApprovalStatus.NORMAL.getCode())&&
						pdl.getAfternoonApprovalStatus().equals(ApprovalStatus.NORMAL.getCode())){
					pdl.setExceptionStatus(ExceptionStatus.NORMAL.getCode());
				}else {
					pdl.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
				}
			}
		} 
		
		List<PunchExceptionRequest> exceptionRequests = punchProvider
				.listExceptionRequestsByDate(userId, companyId,
						dateSF.format(logDay.getTime()));
		if (exceptionRequests.size() > 0) {
			for (PunchExceptionRequest exceptionRequest : exceptionRequests) {
 

				PunchExceptionDTO punchExceptionDTO = ConvertHelper.convert(exceptionRequest , PunchExceptionDTO.class);
				punchExceptionDTO.setRequestType(exceptionRequest
						.getRequestType());
				punchExceptionDTO.setCreateTime(exceptionRequest
						.getCreateTime().getTime());
				if (exceptionRequest.getRequestType().equals(
						PunchRquestType.REQUEST.getCode())) {
					// 对于申请
					
					OrganizationMember member = organizationProvider.findOrganizationMemberByOrgIdAndUId(exceptionRequest.getUserId(), companyId);

					if (null == member) {
						punchExceptionDTO.setName("无此人");
					} else {
						punchExceptionDTO.setName(member.getContactName());
					}
				} else {
					// 审批
					punchExceptionDTO.setExceptionComment(exceptionRequest
							.getProcessDetails());
					OrganizationMember member = organizationProvider.findOrganizationMemberByOrgIdAndUId(exceptionRequest.getOperatorUid(), companyId);

					if (null == member) {
						punchExceptionDTO.setName("无此人");
					} else {
						punchExceptionDTO.setName(member.getContactName());
					}
				}
				if (null == pdl.getPunchExceptionDTOs()) {
					pdl.setPunchExceptionDTOs(new ArrayList<PunchExceptionDTO>());
				}
				pdl.getPunchExceptionDTOs().add(punchExceptionDTO);
			}
		}
	}

 
 

	private List<Calendar> getMinAndMaxTimeFromPunchlogs(List<PunchLog> punchLogs) {
		List<Calendar> result = new ArrayList<Calendar>();
		Calendar logCalendar = Calendar.getInstance();
		Calendar maxCalendar = Calendar.getInstance();
		Calendar minCalendar = Calendar.getInstance();
		maxCalendar.setTime((Date) punchLogs.get(0).getPunchTime().clone());
		minCalendar.setTime((Date) punchLogs.get(0).getPunchTime().clone());
		if (punchLogs.size() != 1) {

			for (PunchLog punchlog : punchLogs) {
				logCalendar.setTime((Date) punchlog.getPunchTime().clone());
				if (logCalendar.before(minCalendar))
					minCalendar.setTime(logCalendar.getTime());
				if (logCalendar.after(maxCalendar))
					maxCalendar.setTime(logCalendar.getTime());

			}
		}
		result.add(minCalendar);
		result.add(maxCalendar);
		return result;
	}

	@Override
	public PunchClockResponse createPunchLog(PunchClockCommand cmd) {

		checkCompanyIdIsNull(cmd.getEnterpriseId());
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
		// new Date()为获取当前系统时间为打卡时间
		String punchTime = datetimeSF.format(new Date());
		PunchLog punchLog = ConvertHelper.convert(cmd, PunchLog.class);
		punchLog.setUserId(userId);
		punchLog.setPunchTime(Timestamp.valueOf(punchTime));
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
		//刷新这一天的数据
		this.coordinationProvider.getNamedLock(CoordinationLocks.CREATE_PUNCH_LOG.getCode()).enter(()-> {
		    this.dbProvider.execute((status) -> {
		    	try {
					refreshPunchDayLog(userId, cmd.getEnterpriseId(), punCalendar);
				} catch (Exception e) {
					LOGGER.error(e.toString());
					throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
							PunchServiceErrorCode.ERROR_PUNCH_ADD_DAYLOG,
							"Something wrong with refresh PunchDayLog");

				}
		        return null;
		    });
		    return null;
		});
		request.setPunchTime(punchTime);

		return request;
	}

	private ClockCode verifyCompanyGoePoints(PunchClockCommand cmd) {
		ClockCode code = ClockCode.NOTINAREA;
		List<PunchGeopoint> punchGeopoints = punchProvider
				.listPunchGeopointsByCompanyId(cmd.getEnterpriseId());
		if (punchGeopoints.size() == 0)
			throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
 					PunchServiceErrorCode.ERROR_ENTERPRISE_DIDNOT_SETTING,
 				"公司没有设置打卡规则");
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
	public void createPunchRule(AddPunchRuleCommand cmd) {
		Long userId = UserContext.current().getUser().getId();
		checkCompanyIdIsNull(cmd.getEnterpriseId());
		PunchRule punchRule = punchProvider.findPunchRuleByCompanyId(cmd
				.getEnterpriseId()); 
		if(punchRule == null) {
			punchRule = ConvertHelper.convert(cmd, PunchRule.class);
 			punchRule.setAfternoonArriveTime(convertTime(cmd.getAfternoonArriveTime()));
			punchRule.setPunchTimesPerDay(cmd.getPunchTimesPerDay());
			punchRule.setNoonLeaveTime(convertTime(cmd.getNoonLeaveTime())); 
			convertTime(punchRule, cmd.getStartEarlyTime(), cmd.getStartLateTime(), cmd.getEndEarlyTime());
			punchRule.setCreatorUid(userId);
			punchRule.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
					.getTime()));
			punchProvider.createPunchRule(punchRule);
			createPunchGeopoints(userId, cmd.getPunchGeoPoints(),cmd.getEnterpriseId());
		}

	}
	@Override
	public void updatePunchRule(UpdatePunchRuleCommand cmd) {
		Long userId = UserContext.current().getUser().getId();
		checkCompanyIdIsNull(cmd.getEnterpriseId());
		PunchRule punchRule = punchProvider.findPunchRuleByCompanyId(cmd
				.getEnterpriseId()); 
		if (punchRule != null) {
			convertTime(punchRule, cmd.getStartEarlyTime(), cmd.getStartLateTime(), cmd.getEndEarlyTime());
			punchRule.setAfternoonArriveTime(convertTime(cmd.getAfternoonArriveTime()));
			punchRule.setPunchTimesPerDay(cmd.getPunchTimesPerDay());
			punchRule.setNoonLeaveTime(convertTime(cmd.getNoonLeaveTime()));
			punchRule.setOperatorUid(userId);
			punchRule.setOperateTime(new Timestamp(DateHelper.currentGMTTime()
					.getTime()));
			punchProvider.updatePunchRule(punchRule);
			if (null!=cmd.getPunchGeoPoints()) {
				List<PunchGeopoint> geopoints = punchProvider
						.listPunchGeopointsByCompanyId(cmd.getEnterpriseId());
				if (geopoints != null && geopoints.size() > 0) {
					for (PunchGeopoint punchGeopoint : geopoints) {
						punchProvider.deletePunchGeopoint(punchGeopoint);
					}
				}
				createPunchGeopoints(userId, cmd.getPunchGeoPoints(),
						cmd.getEnterpriseId());
			}
		}
	}
	
	private void createPunchGeopoints(Long userId, List<PunchGeoPointDTO> punchGeoPoints,
			Long enterpriseId) { 
		for (PunchGeoPointDTO punchGeopointDTO : punchGeoPoints) {
			PunchGeopoint punchGeopoint = ConvertHelper.convert(punchGeopointDTO, PunchGeopoint.class);
			punchGeopoint.setEnterpriseId(enterpriseId);
			punchGeopoint.setCreatorUid(userId);
			punchGeopoint.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			punchGeopoint.setGeohash(GeoHashUtils.encode(
					punchGeopoint.getLatitude(), punchGeopoint.getLongitude()));
			punchProvider.createPunchGeopoint(punchGeopoint);
		}
	}

	private void convertTime(PunchRule punchRule, Long startEarlyTime,
			Long startLastTime, Long endEarlyTime) {
		Time startEarly = convertTime(startEarlyTime);
		punchRule.setStartEarlyTime(startEarly);
		punchRule.setStartLateTime(convertTime(startLastTime));
		Time endEarly = convertTime(endEarlyTime);
		Long workTime = endEarly.getTime() - startEarly.getTime();
		// long hours = (workTime/(1000* 60 * 60));
		// long minutes = (workTime-hours*(1000* 60 * 60))/(1000* 60);
//		String workTimeStr = getGMTtimeString("HH:mm:ss", workTime);
		punchRule.setWorkTime(convertTime(workTime));
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
	private Time convertTime(Long TimeLong) {
		if (null != TimeLong) {
			//从8点开始计算
			return new Time(TimeLong-MILLISECONDGMT);
		}
		return null;
	}
	 
//	private Long convertTimeToGMTMillisecond(Time time) {
//		if (null != time) {
//			//从8点开始计算
//			return time.getTime()+MILLISECONDGMT;
//		}
//		return null;
//	}
	
    private final Long MILLISECONDGMT=8*3600*1000L;
	@Override
	public GetPunchRuleCommandResponse getPunchRuleByCompanyId(
			GetPunchRuleCommand cmd) {
		GetPunchRuleCommandResponse response = new GetPunchRuleCommandResponse();
		PunchRuleDTO dto = null;
		checkCompanyIdIsNull(cmd.getEnterpriseId());
		PunchRule punchRule = punchProvider.findPunchRuleByCompanyId(cmd
				.getEnterpriseId());
		if (punchRule != null) {
			dto = ConvertHelper.convert(punchRule, PunchRuleDTO.class);
			dto.setAfternoonArriveTime(punchRule.getAfternoonArriveTime().getTime());
			dto.setNoonLeaveTime(punchRule.getNoonLeaveTime().getTime());
			dto.setStartEarlyTime(punchRule.getStartEarlyTime().getTime());
			dto.setStartLateTime(punchRule.getStartLateTime().getTime());
			dto.setEndEarlyTime(punchRule.getStartEarlyTime().getTime() + punchRule.getWorkTime().getTime() +MILLISECONDGMT);
			List<PunchGeopoint> geopoints = punchProvider
					.listPunchGeopointsByCompanyId(cmd.getEnterpriseId());
			dto.setPunchGeoPoints(new ArrayList<PunchGeoPointDTO>());
			for (PunchGeopoint point : geopoints){
				dto.getPunchGeoPoints().add(ConvertHelper.convert(point,PunchGeoPointDTO.class));
			}
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
	public void deletePunchRule(DeletePunchRuleCommand cmd) {
		PunchRule punchRule = punchProvider.findPunchRuleByCompanyId(cmd
				.getEnterpriseId());
		if (punchRule != null) {
			punchProvider.deletePunchRule(punchRule);
			List<PunchGeopoint> geopoints = punchProvider
					.listPunchGeopointsByCompanyId(cmd.getEnterpriseId());
			if (geopoints != null && geopoints.size() > 0) {
				for (PunchGeopoint punchGeopoint : geopoints) {
					punchProvider.deletePunchGeopoint(punchGeopoint);
				}
			}
		}
	}

	// 如果查询时间为空，重置时间范围。默认为上个月。
	private void processQueryCommandDay(ListPunchCountCommand cmd) {
		Calendar startCalendar = Calendar.getInstance();
		Calendar endCalendar = Calendar.getInstance();
		
		String startDay = cmd.getStartDay();
		String endDay = cmd.getEndDay();
		if (StringUtils.isEmpty(startDay) && StringUtils.isEmpty(endDay)) {
			startCalendar.setTime(new Date());
			startCalendar.add(Calendar.MONTH, -1);
			startCalendar.set(Calendar.DAY_OF_MONTH, 1);
			cmd.setStartDay(String.format("%tF", startCalendar.getTime()));

			endCalendar.setTime(new Date());
			endCalendar.set(Calendar.DAY_OF_MONTH, 1);
			cmd.setEndDay(String.format("%tF", endCalendar.getTime()));
		}
	}

	// 计算两个日期间工作日天数，不包含结束时间
	private Integer countWorkDayCount(String startDay, String endDay) {
		Integer workDayCount = 0;
		try {
			Calendar startCalendar = Calendar.getInstance();
			Calendar endCalendar = Calendar.getInstance();
			if (!StringUtils.isEmpty(startDay) && !StringUtils.isEmpty(endDay)) {
				startCalendar.setTime(dateSF.parse(startDay));
				endCalendar.setTime(dateSF.parse(endDay));
			} else {
				startCalendar.setTime(new Date());
				startCalendar.add(Calendar.MONTH, -1);
				startCalendar.set(Calendar.DAY_OF_MONTH, 1);

				endCalendar.setTime(new Date());
				endCalendar.set(Calendar.DAY_OF_MONTH, 1);
			}
			while (true) {
				if (isWorkDay(startCalendar.getTime())) {
					workDayCount++;
				}
				startCalendar.add(Calendar.DAY_OF_MONTH, 1);
				if (startCalendar.after(endCalendar)) {
					return workDayCount;
				}
			}
		} catch (ParseException e) {
			LOGGER.error("the time format is error.", e);
		}
		return workDayCount;
	}

	public boolean isWorkDay(Date date1) {
		if (date1 == null)
			return false;
		SimpleDateFormat dateSF = new SimpleDateFormat("yyyy-MM-dd");
		// 如果属于周末调班 返回工作日
		List<Date> workDates = getSpecialDay(DateStatus.WEEKENDWORK);
		if (null != workDates) {
			for (Date workDate : workDates) {
				if (dateSF.format(date1).equals(dateSF.format(workDate)))
					return true;
			}
		}
		// 如果属于工作日休假 返回非工作日
		List<Date> weekenDates = getSpecialDay(DateStatus.HOLIDAY);
		if (null != weekenDates) {
			for (Date weekenDate : weekenDates) {
				if (dateSF.format(date1).equals(dateSF.format(weekenDate)))
					return false;
			}
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
	public void createPunchExceptionRequest(AddPunchExceptionRequestCommand cmd) {
		Long userId = UserContext.current().getUser().getId();
		checkCompanyIdIsNull(cmd.getEnterpriseId());
		if (cmd.getRequestDescription() == null
				|| cmd.getRequestDescription().equals(0))
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid description parameter in the command");
//		PunchRule rule = this.punchProvider.getPunchRuleByCompanyId(cmd.getEnterpriseId());
		PunchExceptionRequest punchExceptionRequest = new PunchExceptionRequest();
		punchExceptionRequest.setEnterpriseId(cmd.getEnterpriseId());
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
		punchExceptionRequest.setViewFlag(ViewFlags.NOTVIEW.getCode()); 
		punchProvider.createPunchExceptionRequest(punchExceptionRequest);

	}

	@Override
	public ListPunchExceptionRequestCommandResponse listExceptionRequests(
			ListPunchExceptionRequestCommand cmd) {
		checkCompanyIdIsNull(cmd.getEnterpriseId());
		ListPunchExceptionRequestCommandResponse response = new ListPunchExceptionRequestCommandResponse();
		cmd.setPageOffset(cmd.getPageOffset() == null ? 1 : cmd.getPageOffset());
		List<EnterpriseContact> enterpriseContacts = enterpriseContactProvider
				.queryEnterpriseContactByKeyword(cmd.getKeyword());
		List<Long> userIds = new ArrayList<Long>();
		for (EnterpriseContact enterpriseContact : enterpriseContacts) {
			userIds.add(enterpriseContact.getUserId());
		}
		int totalCount = punchProvider.countExceptionRequests(userIds,
				cmd.getEnterpriseId(), cmd.getStartDay(), cmd.getEndDay(),
				cmd.getExceptionStatus(), cmd.getProcessCode(),
				PunchRquestType.REQUEST.getCode());
		if (totalCount == 0)
			return response;

		int pageSize = PaginationConfigHelper.getPageSize(
				configurationProvider, cmd.getPageSize());
		int pageCount = getPageCount(totalCount, pageSize);

		List<PunchExceptionRequest> result = punchProvider
				.listExceptionRequests(userIds, cmd.getEnterpriseId(),
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
					dto.setPunchDate(r.getPunchDate().getTime());
					logDay.setTime(r.getPunchDate());

					PunchDayLog punchDayLog = punchProvider.getDayPunchLogByDate(dto.getUserId(),
							dto.getEnterpriseId(), dateSF.format(logDay.getTime()));
					if (null == punchDayLog) {
						// 插入数据
						try {
							punchDayLog = refreshPunchDayLog(dto.getUserId(),
									dto.getEnterpriseId(),logDay);
						} catch (ParseException e) {
							throw RuntimeErrorException.errorWith(
									PunchServiceErrorCode.SCOPE,
									PunchServiceErrorCode.ERROR_PUNCH_REFRESH_DAYLOG,
									"ERROR IN REFRESHPUNCHDAYLOG  LINE 353");
						}
						if (null == punchDayLog) {
							// 验证后为空
							throw RuntimeErrorException.errorWith(
									ErrorCodes.SCOPE_GENERAL,
									ErrorCodes.ERROR_INVALID_PARAMETER,
									"Invalid description parameter in the command");
						}
					} 
					dto.setPunchStatus(punchDayLog.getStatus());
					dto.setMorningPunchStatus(punchDayLog.getMorningStatus());
					dto.setAfternoonPunchStatus(punchDayLog.getAfternoonStatus());
					if(null!=punchDayLog.getNoonLeaveTime() )
						dto.setNoonLeaveTime(punchDayLog.getNoonLeaveTime().getTime());
					if(null!=punchDayLog.getAfternoonArriveTime() )
						dto.setAfternoonArriveTime(punchDayLog.getAfternoonArriveTime().getTime());
					if(null!=punchDayLog.getArriveTime() )
						dto.setArriveTime(punchDayLog.getArriveTime().getTime());
					if(null!=punchDayLog.getLeaveTime() )
						dto.setLeaveTime(punchDayLog.getLeaveTime().getTime());
					if(null!=punchDayLog.getWorkTime() )
						dto.setWorkTime(punchDayLog.getWorkTime().getTime()); 
					dto.setPunchTimesPerDay(punchDayLog.getPunchTimesPerDay());
					OrganizationMember member = organizationProvider.findOrganizationMemberByOrgIdAndUId(dto.getUserId(), cmd.getEnterpriseId());

					if (null == member) {
					} else {
						dto.setUserName(member.getContactName());
						dto.setUserPhoneNumber(member.getContactToken());
					}
					
					if (null != dto.getOperatorUid()
							&& 0 != dto.getOperatorUid()) {

						member = organizationProvider.findOrganizationMemberByOrgIdAndUId(dto.getOperatorUid(), cmd.getEnterpriseId());

						if (null == member) {
							dto.setOperatorName("无此人");
						} else {
							dto.setOperatorName(member.getContactName());
						}
					}
					
					PunchExceptionApproval  approval = punchProvider.getExceptionApproval(r.getUserId(), cmd.getEnterpriseId(),r.getPunchDate());
					if(null!= approval){
						dto.setMorningApprovalStatus(approval.getMorningApprovalStatus());
						dto.setAfternoonApprovalStatus(approval.getAfternoonApprovalStatus());
					}  
					
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
		checkCompanyIdIsNull(cmd.getEnterpriseId());
		ListPunchExceptionRequestCommandResponse response = new ListPunchExceptionRequestCommandResponse();
		cmd.setPageOffset(cmd.getPageOffset() == null ? 1 : cmd.getPageOffset());
		int totalCount = punchProvider.countExceptionRequests(cmd.getUserId(),
				null, cmd.getEnterpriseId(), cmd.getPunchDate(),
				cmd.getPunchDate(), null, null,
				PunchRquestType.APPROVAL.getCode());
		if (totalCount == 0)
			return response;
		int pageSize = PaginationConfigHelper.getPageSize(
				configurationProvider, cmd.getPageSize());
		int pageCount = getPageCount(totalCount, pageSize);

		List<PunchExceptionRequest> result = punchProvider
				.listExceptionRequests(cmd.getUserId(), null,
						cmd.getEnterpriseId(), cmd.getPunchDate(),
						cmd.getPunchDate(), null, null, cmd.getPageOffset(),
						pageSize, PunchRquestType.APPROVAL.getCode());
		response.setExceptionRequestList(result
				.stream()
				.map(r -> {
					PunchExceptionRequestDTO dto = ConvertHelper.convert(r,
							PunchExceptionRequestDTO.class);
					OrganizationMember member = organizationProvider.findOrganizationMemberByOrgIdAndUId(dto.getUserId(), cmd.getEnterpriseId());
					PunchExceptionApproval  approval = punchProvider.getExceptionApproval(cmd.getUserId(), cmd.getEnterpriseId(), java.sql.Date.valueOf(cmd.getPunchDate()));
					if(null != member){
						dto.setUserName(member.getContactName());
						dto.setUserPhoneNumber(member.getContactToken());
					}
					if (null != dto.getOperatorUid()
							&& 0 != dto.getOperatorUid()) {

						member = organizationProvider.findOrganizationMemberByOrgIdAndUId(dto.getOperatorUid(), cmd.getEnterpriseId());
						if (null == member) {
							dto.setOperatorName("无此人");
						} else {
							dto.setOperatorName(member.getContactName());
						}
					}
					
					if(null== approval){
						return dto;
					}  
					dto.setPunchTimesPerDay(approval.getPunchTimesPerDay());
					dto.setPunchDate(approval.getPunchDate().getTime());
					
					 
					
					
					return dto;
				}).collect(Collectors.toList()));

		response.setNextPageOffset(cmd.getPageOffset() == pageCount ? null
				: cmd.getPageOffset() + 1);
		return response;
	}

	@Override
	public void punchExceptionApproval(ApprovalPunchExceptionCommand cmd) {
		if (null == cmd.getUserId() || cmd.getUserId().equals(0L)) {
			LOGGER.error("Invalid user Id parameter in the command");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid company Id parameter in the command");
		}
		
		if(cmd.getMorningApprovalStatus() == null && cmd.getAfternoonApprovalStatus() == null && cmd.getApprovalStatus() == null ) {
			LOGGER.error("morningApprovalStatus、afternoonApprovalStatus、approvalStatus cannot be null at the same time!");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"morningApprovalStatus、afternoonApprovalStatus、approvalStatus cannot be null at the same time!");
		}
		checkCompanyIdIsNull(cmd.getEnterpriseId());
		// 插入一条eh_punch_exception_requests 记录
		PunchExceptionRequest punchExceptionRequest = ConvertHelper.convert(cmd,PunchExceptionRequest.class );
		punchExceptionRequest.setEnterpriseId(cmd.getEnterpriseId());
		punchExceptionRequest
				.setRequestType(PunchRquestType.APPROVAL.getCode());  
		punchExceptionRequest.setDescription(cmd.getProcessDetails());
		punchExceptionRequest.setUserId(cmd.getUserId());
		punchExceptionRequest.setCreatorUid(cmd.getCreatorUid());
		punchExceptionRequest.setCreateTime(new Timestamp(DateHelper
				.currentGMTTime().getTime()));
		punchExceptionRequest.setOperatorUid(cmd.getOperatorUid());
		punchExceptionRequest.setOperateTime(new Timestamp(DateHelper
				.currentGMTTime().getTime()));
		punchExceptionRequest.setPunchDate(java.sql.Date.valueOf(cmd
				.getPunchDate())); 
		punchExceptionRequest.setViewFlag(ViewFlags.NOTVIEW.getCode());
		punchProvider.createPunchExceptionRequest(punchExceptionRequest);
		// 查eh_punch_exception_approvals有无数据：无数据，结果是同意则插入 /有数据 如果结果是同意
		// 则修改，结果是驳回则删除
		PunchExceptionApproval punchExceptionApproval = new PunchExceptionApproval();
		punchExceptionApproval.setEnterpriseId(cmd.getEnterpriseId());
		punchExceptionApproval.setApprovalStatus(cmd.getApprovalStatus());
		punchExceptionApproval.setMorningApprovalStatus(cmd.getMorningApprovalStatus());
		punchExceptionApproval.setAfternoonApprovalStatus(cmd.getAfternoonApprovalStatus());
		punchExceptionApproval.setUserId(cmd.getUserId());
		punchExceptionApproval.setCreatorUid(cmd.getCreatorUid());
		punchExceptionApproval.setCreateTime(new Timestamp(DateHelper
				.currentGMTTime().getTime()));
		punchExceptionApproval.setOperatorUid(cmd.getOperatorUid());
		punchExceptionApproval.setOperateTime(new Timestamp(DateHelper
				.currentGMTTime().getTime()));
		punchExceptionApproval.setPunchDate(java.sql.Date.valueOf(cmd
				.getPunchDate()));
		punchExceptionApproval.setViewFlag(ViewFlags.NOTVIEW.getCode());
		punchExceptionApproval.setPunchTimesPerDay(cmd.getPunchTimesPerDay());
		PunchExceptionApproval oldpunchExceptionApproval = punchProvider
				.getExceptionApproval(cmd.getUserId(), cmd.getEnterpriseId(),
						java.sql.Date.valueOf(cmd.getPunchDate()));
		if (null == oldpunchExceptionApproval) {
			if (cmd.getStatus().equals(ExceptionProcessStatus.ACTIVE.getCode())) {
				punchProvider.createPunchExceptionApproval(punchExceptionApproval);
			}
		} else {
			punchExceptionApproval.setId(oldpunchExceptionApproval.getId());
			if (cmd.getStatus().equals(ExceptionProcessStatus.ACTIVE.getCode())) {
				punchProvider.updatePunchExceptionApproval(punchExceptionApproval);
			} else {
				punchProvider.deletePunchExceptionApproval(punchExceptionApproval.getId());
			}
		}
		// 更新eh_punch_exception_requests当天当人的申请记录
		List<PunchExceptionRequest> results = punchProvider
				.listExceptionRequests(cmd.getUserId(), null,
						cmd.getEnterpriseId(), cmd.getPunchDate(),
						cmd.getPunchDate(), null, null, 1, 999999,
						PunchRquestType.REQUEST.getCode());
		for (PunchExceptionRequest result : results) {

			result.setApprovalStatus(cmd.getApprovalStatus());
			result.setMorningApprovalStatus(cmd.getMorningApprovalStatus());
			result.setAfternoonApprovalStatus(cmd.getAfternoonApprovalStatus());
			result.setProcessDetails(cmd.getProcessDetails());
			result.setUserId(cmd.getUserId());
			result.setOperatorUid(cmd.getOperatorUid());
			result.setOperateTime(new Timestamp(DateHelper.currentGMTTime()
					.getTime()));
			result.setStatus(cmd.getStatus());
			punchProvider.updatePunchExceptionRequest(result);
		}
	}

	@Override
	public ListPunchStatisticsCommandResponse listPunchStatistics(
			ListPunchStatisticsCommand cmd) {
		checkCompanyIdIsNull(cmd.getEnterpriseId());
		ListPunchStatisticsCommandResponse response = new ListPunchStatisticsCommandResponse();
		cmd.setPageOffset(cmd.getPageOffset() == null ? 1 : cmd.getPageOffset());
		ListOrganizationContactCommand orgCmd = new ListOrganizationContactCommand();
		orgCmd.setOrganizationId(cmd.getEnterpriseId());
		if(null != cmd.getEnterpriseGroupId()){
			orgCmd.setOrganizationId(cmd.getEnterpriseGroupId());
		}
		orgCmd.setKeywords(cmd.getKeyword());
		orgCmd.setPageSize(100000);
		ListOrganizationMemberCommandResponse resp =  organizationService.listOrganizationPersonnels(orgCmd, false);
		List<OrganizationMemberDTO> members = resp.getMembers();
		List<Long> userIds = new ArrayList<Long>();
		if(null != members){
			for (OrganizationMemberDTO member : members) {
				if(member.getTargetType().equals(OrganizationMemberTargetType.USER.getCode())){
					userIds.add(member.getTargetId());
				}
			}
		}
		
		int totalCount = punchProvider.countPunchDayLogs(userIds,
				cmd.getEnterpriseId(), cmd.getStartDay(), cmd.getEndDay(),
				cmd.getArriveTimeCompareFlag(), cmd.getArriveTime(),
				cmd.getLeaveTimeCompareFlag(), cmd.getLeaveTime(),
				cmd.getWorkTimeCompareFlag(), cmd.getWorkTime(),
				cmd.getStatus());
		if (totalCount == 0)
			return response;

		Integer pageSize = PaginationConfigHelper.getPageSize(
				configurationProvider, cmd.getPageSize());
		int pageCount = getPageCount(totalCount, pageSize);
		Organization organization = organizationProvider.findOrganizationById(cmd.getEnterpriseId());
		List<String> groupTypes = new ArrayList<String>();
		groupTypes.add(OrganizationGroupType.DEPARTMENT.getCode());
		List<Organization> departments = organizationProvider.listOrganizationByGroupTypes(organization.getPath() + "/%", groupTypes);
		Map<Long, Organization> deptMap = this.convertDeptListToMap(departments);
		List<PunchDayLog> result = punchProvider.listPunchDayLogs(userIds,
				cmd.getEnterpriseId(), cmd.getStartDay(), cmd.getEndDay(),
				cmd.getStatus(), cmd.getArriveTimeCompareFlag(),
				cmd.getArriveTime(), cmd.getLeaveTimeCompareFlag(),
				cmd.getLeaveTime(), cmd.getWorkTimeCompareFlag(),
				cmd.getWorkTime(), cmd.getPageOffset(), pageSize);
		response.setPunchList(result
				.stream()
				.map(r -> {
					PunchStatisticsDTO dto = ConvertHelper.convert(r,
							PunchStatisticsDTO.class);
					processPunchStatisticsDTOTime(dto, r);
					if (dto != null) {
						OrganizationMember member = organizationProvider.findOrganizationMemberByOrgIdAndUId(dto.getUserId(), cmd.getEnterpriseId());
						if (null != member) {
							Organization department = deptMap.get(member.getGroupId());
							if(null != department){
								dto.setUserEnterpriseGroup(department.getName());
							}
							
							dto.setUserName(member.getContactName());
							dto.setUserPhoneNumber(member.getContactToken());
							// dto.setUserDepartment(enterpriseContact.get);
							PunchExceptionApproval approval = punchProvider
									.getExceptionApproval(dto.getUserId(),
											dto.getEnterpriseId(),
											new java.sql.Date(dto.getPunchDate()));
							if (approval != null) {
								dto.setApprovalStatus(approval
										.getApprovalStatus());
								dto.setMorningApprovalStatus(approval.getMorningApprovalStatus());
								dto.setAfternoonApprovalStatus(approval.getAfternoonApprovalStatus());
								OrganizationMember operator = organizationProvider.findOrganizationMemberByOrgIdAndUId(approval.getOperatorUid(), cmd.getEnterpriseId());
								if(null != operator )
									dto.setOperatorName(operator.getContactName());
							} else {
								//do nothing
//								dto.setApprovalStatus((byte) 0);
							}
						}
					}
					return dto;
				}).collect(Collectors.toList()));

		response.setNextPageOffset(cmd.getPageOffset() == pageCount ? null
				: cmd.getPageOffset() + 1);
		return response;

	}

	private Map<Long, Organization> convertDeptListToMap(List<Organization> depts){
		Map<Long, Organization> map = new HashMap<Long, Organization>();
		if(null == depts){
			return map;
		}
		for (Organization dept : depts) {
			map.put(dept.getId(), dept);
		}
		return map;
	}
	
	private void processPunchStatisticsDTOTime(PunchStatisticsDTO dto,
			PunchDayLog r) {
		if(null!= r.getArriveTime())
			dto.setArriveTime(   r.getArriveTime().getTime());

		if(null!= r.getLeaveTime())
			dto.setLeaveTime( r.getLeaveTime().getTime());

		if(null!= r.getWorkTime())
			dto.setWorkTime(  r.getWorkTime().getTime());

		if(null!= r.getNoonLeaveTime())
			dto.setNoonLeaveTime(  r.getNoonLeaveTime().getTime());

		if(null!= r.getAfternoonArriveTime())
			dto.setAfternoonArriveTime(  r.getAfternoonArriveTime().getTime());
		if(null!= r.getPunchDate())
			dto.setPunchDate(r.getPunchDate().getTime());
		dto.setPunchTimesPerDay(r.getPunchTimesPerDay());
	}

	@Override
	public GetPunchNewExceptionCommandResponse getPunchNewException(
			GetPunchNewExceptionCommand cmd) {
		checkCompanyIdIsNull(cmd.getEnterpriseId());
		Long userId = UserContext.current().getUser().getId();
		GetPunchNewExceptionCommandResponse response = new GetPunchNewExceptionCommandResponse();
		response.setExceptionCode(ExceptionStatus.NORMAL.getCode());
		// TODO：从本月初，或者第一次打卡开始
		Calendar start = Calendar.getInstance();
		// 月初
		Calendar monthStart = Calendar.getInstance(); 
		monthStart.add(Calendar.DAY_OF_MONTH, -30);
		// 前一天
		Calendar end = Calendar.getInstance();
		end.add(Calendar.DAY_OF_MONTH, -1);
		// 找出异常的记录
		List<PunchDayLog> PunchDayLogs = punchProvider
				.listPunchDayExceptionLogs(userId, cmd.getEnterpriseId(),
						dateSF.format(monthStart.getTime()),
						dateSF.format(end.getTime()));
		if (PunchDayLogs.size() > 0)
			response.setExceptionCode(ExceptionStatus.EXCEPTION.getCode());
		List<PunchExceptionRequest> exceptionRequests = punchProvider
				.listExceptionNotViewRequests(userId, cmd.getEnterpriseId(),
						dateSF.format(monthStart.getTime()),
						dateSF.format(end.getTime()));
		if (exceptionRequests.size() > 0)
			response.setExceptionCode(ExceptionStatus.EXCEPTION.getCode());
		return response;
	}

	@Override
	public PunchLogsDay getDayPunchLogs(GetDayPunchLogsCommand cmd) {
		Long userId = UserContext.current().getUser().getId();
		checkCompanyIdIsNull(cmd.getEnterpirseId());
		Calendar logDay = Calendar.getInstance();

		try {

			logDay.setTime(dateSF.parse(cmd.getQueryDate()));
			Calendar today = Calendar.getInstance();
			// 对于今天以后的查询，都返回为null
			if (logDay.after(today))
				return null;
		} catch (Exception e) {
			throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid queryDate parameter in the command");
		}
		PunchLogsDay pdl = makePunchLogsDayListInfo(userId,
				cmd.getEnterpirseId(), logDay);
		punchProvider.viewDateFlags(userId, cmd.getEnterpirseId(),
				dateSF.format(logDay.getTime()));
		return pdl;
	}

	@Override
	public ListPunchCountCommandResponse listPunchCount(
			ListPunchCountCommand cmd) {
		processQueryCommandDay(cmd);
		ListPunchCountCommandResponse response = new ListPunchCountCommandResponse();
		List<PunchCountDTO> punchCountDTOList = new ArrayList<PunchCountDTO>();
		Integer workDayCount = countWorkDayCount(cmd.getStartDay(),
				cmd.getEndDay());
		ListPunchStatisticsCommand paramCommand =  ConvertHelper.convert(cmd, ListPunchStatisticsCommand.class );
		paramCommand.setPageSize(Integer.MAX_VALUE);
		List<PunchStatisticsDTO>punchDayLogDTOList =  this.listPunchStatistics(paramCommand).getPunchList();
		Map<Long, List<PunchStatisticsDTO>> map = buildUserPunchCountMap(punchDayLogDTOList);
		PunchRule rule = this.punchProvider.getPunchRuleByCompanyId(cmd.getEnterpriseId());
		Set<Long> userIds = map.keySet();
		for (Long userId : userIds) {
			List<PunchStatisticsDTO> list = map.get(userId);
			
			PunchCountDTO dto = new PunchCountDTO();
			dto.setPunchTimesPerDay(rule.getPunchTimesPerDay());
			dto.setUserId(userId);
			
			if (null == list || list.size() == 0)
				continue;
			dto.setUserName(list.get(0).getUserName());
			dto.setToken(list.get(0).getUserPhoneNumber());
			dto.setWorkDayCount(workDayCount);
			
			dto.setUserEnterpriseGroup(list.get(0).getUserEnterpriseGroup());
			if(dto.getPunchTimesPerDay().equals(PunchTimesPerDay.TWICE.getCode())){
				processTwicePunchListCount(list, dto);
			}
			else{
				processForthPunchListCount(list, dto);
			}
			 
			
			punchCountDTOList.add(dto);
		}
		response.setPunchCountList(punchCountDTOList);
		return response;
	}

	private void processForthPunchListCount(List<PunchStatisticsDTO> list,
			PunchCountDTO dto) { 
		dto.setWorkCount(0);
		dto.setUnPunchCount(0.0);
		dto.setSickCount(0.0);
		dto.setOutworkCount(0.0);
		dto.setLeaveEarlyCount(0);
		dto.setExchangeCount(0.0);
		dto.setBlandleCount(0);
		dto.setBelateCount(0);
		dto.setAbsenceCount(0.0); 
		dto.setOverTimeSum(0L); 
		for (PunchStatisticsDTO punchDayLogDTO : list) {
			if (punchDayLogDTO.getMorningApprovalStatus() != null) {
				//上午
				if(ApprovalStatus.UNPUNCH.getCode() == punchDayLogDTO.getMorningApprovalStatus()){
					dto.setUnPunchCount(dto.getUnPunchCount()+0.5);
				}else if(ApprovalStatus.SICK.getCode() == punchDayLogDTO.getMorningApprovalStatus()){
					dto.setSickCount(dto.getSickCount()+0.5);
				}else if(ApprovalStatus.HALFSICK.getCode() == punchDayLogDTO.getMorningApprovalStatus()){
					dto.setSickCount(dto.getSickCount()+0.5);
				}else if(ApprovalStatus.OUTWORK.getCode() == punchDayLogDTO.getMorningApprovalStatus()){
					dto.setOutworkCount(dto.getOutworkCount()+0.5);
				}else if(ApprovalStatus.HALFOUTWORK.getCode() == punchDayLogDTO.getMorningApprovalStatus()){
					dto.setOutworkCount(dto.getOutworkCount()+0.5);
				}else if(ApprovalStatus.LEAVEEARLY.getCode() == punchDayLogDTO.getMorningApprovalStatus()){
					dto.setLeaveEarlyCount(dto.getLeaveEarlyCount()+1);
				}else if(ApprovalStatus.EXCHANGE.getCode() == punchDayLogDTO.getMorningApprovalStatus()){
					dto.setExchangeCount(dto.getExchangeCount()+0.5);
				}else if(ApprovalStatus.HALFEXCHANGE.getCode() == punchDayLogDTO.getMorningApprovalStatus()){
					dto.setExchangeCount(dto.getExchangeCount()+0.5);
				}else if(ApprovalStatus.BLANDLE.getCode() == punchDayLogDTO.getMorningApprovalStatus()){
					dto.setBlandleCount(dto.getBlandleCount()+1);
				}else if(ApprovalStatus.BELATE.getCode() == punchDayLogDTO.getMorningApprovalStatus()){
					dto.setBelateCount(dto.getBelateCount()+1);
				}else if(ApprovalStatus.ABSENCE.getCode() == punchDayLogDTO.getMorningApprovalStatus()){
					dto.setAbsenceCount(dto.getAbsenceCount()+0.5);
				}else if(ApprovalStatus.HALFABSENCE.getCode() == punchDayLogDTO.getMorningApprovalStatus()){
					dto.setAbsenceCount(dto.getAbsenceCount()+0.5);
				}
				//下午
				
				if(ApprovalStatus.UNPUNCH.getCode() == punchDayLogDTO.getAfternoonApprovalStatus()){
					dto.setUnPunchCount(dto.getUnPunchCount()+0.5);
				}else if(ApprovalStatus.SICK.getCode() == punchDayLogDTO.getAfternoonApprovalStatus()){
					dto.setSickCount(dto.getSickCount()+0.5);
				}else if(ApprovalStatus.HALFSICK.getCode() == punchDayLogDTO.getAfternoonApprovalStatus()){
					dto.setSickCount(dto.getSickCount()+0.5);
				}else if(ApprovalStatus.OUTWORK.getCode() == punchDayLogDTO.getAfternoonApprovalStatus()){
					dto.setOutworkCount(dto.getOutworkCount()+0.5);
				}else if(ApprovalStatus.HALFOUTWORK.getCode() == punchDayLogDTO.getAfternoonApprovalStatus()){
					dto.setOutworkCount(dto.getOutworkCount()+0.5);
				}else if(ApprovalStatus.LEAVEEARLY.getCode() == punchDayLogDTO.getAfternoonApprovalStatus()){
					dto.setLeaveEarlyCount(dto.getLeaveEarlyCount()+1);
				}else if(ApprovalStatus.EXCHANGE.getCode() == punchDayLogDTO.getAfternoonApprovalStatus()){
					dto.setExchangeCount(dto.getExchangeCount()+0.5);
				}else if(ApprovalStatus.HALFEXCHANGE.getCode() == punchDayLogDTO.getAfternoonApprovalStatus()){
					dto.setExchangeCount(dto.getExchangeCount()+0.5);
				}else if(ApprovalStatus.BLANDLE.getCode() == punchDayLogDTO.getAfternoonApprovalStatus()){
					dto.setBlandleCount(dto.getBlandleCount()+1);
				}else if(ApprovalStatus.BELATE.getCode() == punchDayLogDTO.getAfternoonApprovalStatus()){
					dto.setBelateCount(dto.getBelateCount()+1);
				}else if(ApprovalStatus.ABSENCE.getCode() == punchDayLogDTO.getAfternoonApprovalStatus()){
					dto.setAbsenceCount(dto.getAbsenceCount()+0.5);
				}else if(ApprovalStatus.HALFABSENCE.getCode() == punchDayLogDTO.getAfternoonApprovalStatus()){
					dto.setAbsenceCount(dto.getAbsenceCount()+0.5);
				}
				//加班和正常
				if(ApprovalStatus.OVERTIME.getCode() == punchDayLogDTO.getAfternoonApprovalStatus()||ApprovalStatus.OVERTIME.getCode() == punchDayLogDTO.getMorningApprovalStatus()){
					dto.setOverTimeSum(dto.getOverTimeSum()+ punchDayLogDTO.getWorkTime());
				}else  if(ApprovalStatus.NORMAL.getCode() == punchDayLogDTO.getAfternoonApprovalStatus()&&ApprovalStatus.NORMAL.getCode() == punchDayLogDTO.getMorningApprovalStatus()) {
					dto.setWorkCount(dto.getWorkCount()+1);
				}
			} else {
				//上午
				if(ApprovalStatus.UNPUNCH.getCode() == punchDayLogDTO.getMorningStatus()){
					dto.setUnPunchCount(dto.getUnPunchCount()+0.5);
				}else if(ApprovalStatus.SICK.getCode() == punchDayLogDTO.getMorningStatus()){
					dto.setSickCount(dto.getSickCount()+0.5);
				}else if(ApprovalStatus.HALFSICK.getCode() == punchDayLogDTO.getMorningStatus()){
					dto.setSickCount(dto.getSickCount()+0.5);
				}else if(ApprovalStatus.OUTWORK.getCode() == punchDayLogDTO.getMorningStatus()){
					dto.setOutworkCount(dto.getOutworkCount()+0.5);
				}else if(ApprovalStatus.HALFOUTWORK.getCode() == punchDayLogDTO.getMorningStatus()){
					dto.setOutworkCount(dto.getOutworkCount()+0.5);
				}else if(ApprovalStatus.LEAVEEARLY.getCode() == punchDayLogDTO.getMorningStatus()){
					dto.setLeaveEarlyCount(dto.getLeaveEarlyCount()+1);
				}else if(ApprovalStatus.EXCHANGE.getCode() == punchDayLogDTO.getMorningStatus()){
					dto.setExchangeCount(dto.getExchangeCount()+0.5);
				}else if(ApprovalStatus.HALFEXCHANGE.getCode() == punchDayLogDTO.getMorningStatus()){
					dto.setExchangeCount(dto.getExchangeCount()+0.5);
				}else if(ApprovalStatus.BLANDLE.getCode() == punchDayLogDTO.getMorningStatus()){
					dto.setBlandleCount(dto.getBlandleCount()+1);
				}else if(ApprovalStatus.BELATE.getCode() == punchDayLogDTO.getMorningStatus()){
					dto.setBelateCount(dto.getBelateCount()+1);
				}else if(ApprovalStatus.ABSENCE.getCode() == punchDayLogDTO.getMorningStatus()){
					dto.setAbsenceCount(dto.getAbsenceCount()+0.5);
				}else if(ApprovalStatus.HALFABSENCE.getCode() == punchDayLogDTO.getMorningStatus()){
					dto.setAbsenceCount(dto.getAbsenceCount()+0.5);
				}
				//下午
				
				if(ApprovalStatus.UNPUNCH.getCode() == punchDayLogDTO.getAfternoonStatus()){
					dto.setUnPunchCount(dto.getUnPunchCount()+0.5);
				}else if(ApprovalStatus.SICK.getCode() == punchDayLogDTO.getAfternoonStatus()){
					dto.setSickCount(dto.getSickCount()+0.5);
				}else if(ApprovalStatus.HALFSICK.getCode() == punchDayLogDTO.getAfternoonStatus()){
					dto.setSickCount(dto.getSickCount()+0.5);
				}else if(ApprovalStatus.OUTWORK.getCode() == punchDayLogDTO.getAfternoonStatus()){
					dto.setOutworkCount(dto.getOutworkCount()+0.5);
				}else if(ApprovalStatus.HALFOUTWORK.getCode() == punchDayLogDTO.getAfternoonStatus()){
					dto.setOutworkCount(dto.getOutworkCount()+0.5);
				}else if(ApprovalStatus.LEAVEEARLY.getCode() == punchDayLogDTO.getAfternoonStatus()){
					dto.setLeaveEarlyCount(dto.getLeaveEarlyCount()+1);
				}else if(ApprovalStatus.EXCHANGE.getCode() == punchDayLogDTO.getAfternoonStatus()){
					dto.setExchangeCount(dto.getExchangeCount()+0.5);
				}else if(ApprovalStatus.HALFEXCHANGE.getCode() == punchDayLogDTO.getAfternoonStatus()){
					dto.setExchangeCount(dto.getExchangeCount()+0.5);
				}else if(ApprovalStatus.BLANDLE.getCode() == punchDayLogDTO.getAfternoonStatus()){
					dto.setBlandleCount(dto.getBlandleCount()+1);
				}else if(ApprovalStatus.BELATE.getCode() == punchDayLogDTO.getAfternoonStatus()){
					dto.setBelateCount(dto.getBelateCount()+1);
				}else if(ApprovalStatus.ABSENCE.getCode() == punchDayLogDTO.getAfternoonStatus()){
					dto.setAbsenceCount(dto.getAbsenceCount()+0.5);
				}else if(ApprovalStatus.HALFABSENCE.getCode() == punchDayLogDTO.getAfternoonStatus()){
					dto.setAbsenceCount(dto.getAbsenceCount()+0.5);
				}
				//加班和正常
				if(ApprovalStatus.OVERTIME.getCode() == punchDayLogDTO.getAfternoonStatus()||ApprovalStatus.OVERTIME.getCode() == punchDayLogDTO.getMorningStatus()){
					dto.setOverTimeSum(dto.getOverTimeSum()+ punchDayLogDTO.getWorkTime());
				}else  if(ApprovalStatus.NORMAL.getCode() == punchDayLogDTO.getAfternoonStatus()&&ApprovalStatus.NORMAL.getCode() == punchDayLogDTO.getMorningStatus()) {
					dto.setWorkCount(dto.getWorkCount()+1);
				} 
			}
		}
	}
	private void processTwicePunchListCount(List<PunchStatisticsDTO> list, PunchCountDTO dto) {
		dto.setWorkCount(0);
		dto.setUnPunchCount(0.0);
		dto.setSickCount(0.0);
		dto.setOutworkCount(0.0);
		dto.setLeaveEarlyCount(0);
		dto.setExchangeCount(0.0);
		dto.setBlandleCount(0);
		dto.setBelateCount(0);
		dto.setAbsenceCount(0.0); 
		dto.setOverTimeSum(0L); 
		for (PunchStatisticsDTO punchDayLogDTO : list) {
			if (punchDayLogDTO.getApprovalStatus() != null) {
				if (ApprovalStatus.NORMAL.getCode() == punchDayLogDTO.getApprovalStatus()) {
					dto.setWorkCount(dto.getWorkCount()+1);
				}else if(ApprovalStatus.UNPUNCH.getCode() == punchDayLogDTO.getApprovalStatus()){
					dto.setUnPunchCount(dto.getUnPunchCount()+1);
				}else if(ApprovalStatus.SICK.getCode() == punchDayLogDTO.getApprovalStatus()){
					dto.setSickCount(dto.getSickCount()+1);
				}else if(ApprovalStatus.HALFSICK.getCode() == punchDayLogDTO.getApprovalStatus()){
					dto.setSickCount(dto.getSickCount()+0.5);
				}else if(ApprovalStatus.OUTWORK.getCode() == punchDayLogDTO.getApprovalStatus()){
					dto.setOutworkCount(dto.getOutworkCount()+1);
				}else if(ApprovalStatus.HALFOUTWORK.getCode() == punchDayLogDTO.getApprovalStatus()){
					dto.setOutworkCount(dto.getOutworkCount()+0.5);
				}else if(ApprovalStatus.LEAVEEARLY.getCode() == punchDayLogDTO.getApprovalStatus()){
					dto.setLeaveEarlyCount(dto.getLeaveEarlyCount()+1);
				}else if(ApprovalStatus.EXCHANGE.getCode() == punchDayLogDTO.getApprovalStatus()){
					dto.setExchangeCount(dto.getExchangeCount()+1);
				}else if(ApprovalStatus.HALFEXCHANGE.getCode() == punchDayLogDTO.getApprovalStatus()){
					dto.setExchangeCount(dto.getExchangeCount()+0.5);
				}else if(ApprovalStatus.BLANDLE.getCode() == punchDayLogDTO.getApprovalStatus()){
					dto.setBlandleCount(dto.getBlandleCount()+1);
				}else if(ApprovalStatus.BELATE.getCode() == punchDayLogDTO.getApprovalStatus()){
					dto.setBelateCount(dto.getBelateCount()+1);
				}else if(ApprovalStatus.ABSENCE.getCode() == punchDayLogDTO.getApprovalStatus()){
					dto.setAbsenceCount(dto.getAbsenceCount()+1);
				}else if(ApprovalStatus.HALFABSENCE.getCode() == punchDayLogDTO.getApprovalStatus()){
					dto.setAbsenceCount(dto.getAbsenceCount()+0.5);
				}else if(ApprovalStatus.OVERTIME.getCode() == punchDayLogDTO.getApprovalStatus()){
					dto.setOverTimeSum(dto.getOverTimeSum()+ punchDayLogDTO.getWorkTime());
				} 
			} else {
				if (ApprovalStatus.NORMAL.getCode() == punchDayLogDTO.getStatus()) {
					dto.setWorkCount(dto.getWorkCount()+1);
				}else if(ApprovalStatus.UNPUNCH.getCode() == punchDayLogDTO.getStatus()){
					dto.setUnPunchCount(dto.getUnPunchCount()+1);
				}else if(ApprovalStatus.SICK.getCode() == punchDayLogDTO.getStatus()){
					dto.setSickCount(dto.getSickCount()+1);
				}else if(ApprovalStatus.HALFSICK.getCode() == punchDayLogDTO.getStatus()){
					dto.setSickCount(dto.getSickCount()+0.5);
				}else if(ApprovalStatus.OUTWORK.getCode() == punchDayLogDTO.getStatus()){
					dto.setOutworkCount(dto.getOutworkCount()+1);
				}else if(ApprovalStatus.HALFOUTWORK.getCode() == punchDayLogDTO.getStatus()){
					dto.setOutworkCount(dto.getOutworkCount()+0.5);
				}else if(ApprovalStatus.LEAVEEARLY.getCode() == punchDayLogDTO.getStatus()){
					dto.setLeaveEarlyCount(dto.getLeaveEarlyCount()+1);
				}else if(ApprovalStatus.EXCHANGE.getCode() == punchDayLogDTO.getStatus()){
					dto.setExchangeCount(dto.getExchangeCount()+1);
				}else if(ApprovalStatus.HALFEXCHANGE.getCode() == punchDayLogDTO.getStatus()){
					dto.setExchangeCount(dto.getExchangeCount()+0.5);
				}else if(ApprovalStatus.BLANDLE.getCode() == punchDayLogDTO.getStatus()){
					dto.setBlandleCount(dto.getBlandleCount()+1);
				}else if(ApprovalStatus.BELATE.getCode() == punchDayLogDTO.getStatus()){
					dto.setBelateCount(dto.getBelateCount()+1);
				}else if(ApprovalStatus.ABSENCE.getCode() == punchDayLogDTO.getStatus()){
					dto.setAbsenceCount(dto.getAbsenceCount()+1);
				}else if(ApprovalStatus.HALFABSENCE.getCode() == punchDayLogDTO.getStatus()){
					dto.setAbsenceCount(dto.getAbsenceCount()+0.5);
				}else if(ApprovalStatus.OVERTIME.getCode() == punchDayLogDTO.getStatus()){
					dto.setOverTimeSum(dto.getOverTimeSum()+ punchDayLogDTO.getWorkTime());
				} 
			}
		}
	}
	 
	// 创建每个user的打卡map信息
	private Map<Long, List<PunchStatisticsDTO>> buildUserPunchCountMap(
			List<PunchStatisticsDTO> punchDayLogDTOList) {
		Map<Long, List<PunchStatisticsDTO>> map = new HashMap<Long, List<PunchStatisticsDTO>>();
		if (punchDayLogDTOList != null && punchDayLogDTOList.size() > 0) {
			for (PunchStatisticsDTO punchDayLogDTO : punchDayLogDTOList) {
				Long userId = punchDayLogDTO.getUserId();
				if (map.containsKey(userId)) {
					List<PunchStatisticsDTO> list = map.get(userId);
					list.add(punchDayLogDTO);
				} else {
					List<PunchStatisticsDTO> list = new ArrayList<PunchStatisticsDTO>();
					list.add(punchDayLogDTO);
					map.put(userId, list);
				}
			}
		}
		return map;
	}

	// 计算user的每个打卡状态的总数
	private Integer processListCount(List<PunchDayLogDTO> list, Byte status) {
		Integer count = 0;
		for (PunchDayLogDTO punchDayLogDTO : list) {
			if (punchDayLogDTO.getApprovalStatus() != null) {
				if (status == punchDayLogDTO.getApprovalStatus()) {
					count++;
				}
			} else {
				if (status == punchDayLogDTO.getStatus()) {
					count++;
				}
			}
		}
		return count;
	}

	// 计算user的每个打卡状态的总数
	private Long processListTimeSum (List<PunchDayLogDTO> list, Byte status) {
		Long timeSum = 0L;
		for (PunchDayLogDTO punchDayLogDTO : list) {
			if (punchDayLogDTO.getApprovalStatus() != null) {
				if (status == punchDayLogDTO.getApprovalStatus()) {
					timeSum += punchDayLogDTO.getWorkTime();
				}
			} else {
				if (status == punchDayLogDTO.getStatus()) {
					timeSum += punchDayLogDTO.getWorkTime();
				}
			}
		}
		return timeSum;
	}

	@Override
	public ListMonthPunchLogsCommandResponse listMonthPunchLogs(
			ListMonthPunchLogsCommand cmd) {

		checkCompanyIdIsNull(cmd.getEnterpriseId());
		SimpleDateFormat yearSF = new SimpleDateFormat("yyyy");

		ListMonthPunchLogsCommandResponse response = new ListMonthPunchLogsCommandResponse();
		response.setPunchYear(yearSF.format(new java.sql.Date(cmd
				.getQueryTime())));
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		// start 设置为本月月初
		start.set(Calendar.DAY_OF_MONTH,
				start.getActualMinimum(Calendar.DAY_OF_MONTH));
		start.set(Calendar.HOUR_OF_DAY, 0);
		start.set(Calendar.SECOND, 0);
		start.set(Calendar.MILLISECOND, 0);
		start.set(Calendar.MINUTE, 0);
		if (start.getTime().after(new java.sql.Date(cmd.getQueryTime()))) {
			// 如果查询日在月初之前 则设置为查询月的月初和月末
			start.setTime(new java.sql.Date(cmd.getQueryTime()));
			end.setTime(new java.sql.Date(cmd.getQueryTime()));
			start.set(Calendar.DAY_OF_MONTH,
					start.getActualMinimum(Calendar.DAY_OF_MONTH));
			end.add(Calendar.MONTH, 1);
			end.set(Calendar.DAY_OF_MONTH,
					end.getActualMinimum(Calendar.DAY_OF_MONTH));
		}

		ListYearPunchLogsCommandResponse pyl = new ListYearPunchLogsCommandResponse();
		pyl.setPunchLogsMonthList(new ArrayList<PunchLogsMonthList>());
		pyl = getlistPunchLogsBetweenTwoCalendar(pyl, cmd.getEnterpriseId(),
				start, end);
		response.setPunchLogsMonthList(pyl.getPunchLogsMonthList());
		return response;
	}
	
	private void createPunchStatisticsBookSheetHead(Sheet sheet, Byte punchTimes ){
		Row row = sheet.createRow(sheet.getLastRowNum());
		int i =-1 ;
		if(punchTimes.equals(PunchTimesPerDay.TWICE.getCode())){
			row.createCell(++i).setCellValue("姓名");
			row.createCell(++i).setCellValue("部门");
			row.createCell(++i).setCellValue("打卡日期");
			row.createCell(++i).setCellValue("工作时长");
			row.createCell(++i).setCellValue("上班打卡");
			row.createCell(++i).setCellValue("下班打卡");
			row.createCell(++i).setCellValue("打卡考勤");
			row.createCell(++i).setCellValue("审批考勤");
			row.createCell(++i).setCellValue("审批人");
		}else if(punchTimes.equals(PunchTimesPerDay.FORTH.getCode())){
			row.createCell(++i).setCellValue("姓名");
			row.createCell(++i).setCellValue("部门");
			row.createCell(++i).setCellValue("打卡日期");
			row.createCell(++i).setCellValue("工作时长");
			row.createCell(++i).setCellValue("上班打卡");
			row.createCell(++i).setCellValue("中午下班打卡");
			row.createCell(++i).setCellValue("下午上班打卡");
			row.createCell(++i).setCellValue("下班打卡");
			row.createCell(++i).setCellValue("上午打卡考勤");
			row.createCell(++i).setCellValue("下午打卡考勤");
			row.createCell(++i).setCellValue("上午审批考勤");
			row.createCell(++i).setCellValue("下午审批考勤");
			row.createCell(++i).setCellValue("审批人");
		}
	}
	
	public void setNewPunchStatisticsBookRow(Sheet sheet ,PunchStatisticsDTO dto){
		Row row = sheet.createRow(sheet.getLastRowNum()+1);
		int i = -1;
		if(dto.getPunchTimesPerDay().equals(PunchTimesPerDay.TWICE.getCode())){
			row.createCell(++i).setCellValue(dto.getUserName());
			row.createCell(++i).setCellValue(dto.getUserDepartment());
			row.createCell(++i).setCellValue(dateSF.format(dto.getPunchDate()));
			if(null!=dto.getWorkTime())
				row.createCell(++i).setCellValue(timeSF.format(new java.sql.Time(dto.getWorkTime())));
			else 
				row.createCell(++i).setCellValue("");
			if(null!=dto.getArriveTime())
				row.createCell(++i).setCellValue(timeSF.format(new java.sql.Time(dto.getArriveTime())));
			else 
				row.createCell(++i).setCellValue("");
			if(null!=dto.getLeaveTime())
				row.createCell(++i).setCellValue(timeSF.format(new java.sql.Time(dto.getLeaveTime())));
			else 
				row.createCell(++i).setCellValue("");
			row.createCell(++i).setCellValue(statusToString(dto.getStatus()));
			if(dto.getOperatorName() != null ){
				row.createCell(++i).setCellValue(statusToString(dto.getApprovalStatus()));
				row.createCell(++i).setCellValue(dto.getOperatorName());
				}
		}else if(dto.getPunchTimesPerDay().equals(PunchTimesPerDay.FORTH.getCode())){
			row.createCell(++i).setCellValue(dto.getUserName());
			row.createCell(++i).setCellValue(dto.getUserDepartment());
			row.createCell(++i).setCellValue(dateSF.format(dto.getPunchDate()));
			if(null!=dto.getWorkTime())
				row.createCell(++i).setCellValue(timeSF.format(new java.sql.Time(dto.getWorkTime())));
			else 
				row.createCell(++i).setCellValue("");
			if(null!=dto.getArriveTime())
				row.createCell(++i).setCellValue(timeSF.format(new java.sql.Time(dto.getArriveTime())));
			else 
				row.createCell(++i).setCellValue("");
			if(null!=dto.getNoonLeaveTime())
				row.createCell(++i).setCellValue(timeSF.format(new java.sql.Time(dto.getNoonLeaveTime())));
			else 
				row.createCell(++i).setCellValue("");
			if(null!=dto.getAfternoonArriveTime())
				row.createCell(++i).setCellValue(timeSF.format(new java.sql.Time(dto.getAfternoonArriveTime())));
			else 
				row.createCell(++i).setCellValue("");
			if(null!=dto.getLeaveTime())
				row.createCell(++i).setCellValue(timeSF.format(new java.sql.Time(dto.getLeaveTime())));
			else 
				row.createCell(++i).setCellValue("");
			row.createCell(++i).setCellValue(statusToString(dto.getMorningStatus()));
			row.createCell(++i).setCellValue(statusToString(dto.getAfternoonStatus()));
			if(dto.getOperatorName() != null ){
				row.createCell(++i).setCellValue(statusToString(dto.getMorningApprovalStatus()));
				row.createCell(++i).setCellValue(statusToString(dto.getAfternoonApprovalStatus()));
				row.createCell(++i).setCellValue(dto.getOperatorName());
			}
		}
	}
	
	public void createPunchStatisticsBook(String path,List<PunchStatisticsDTO> dtos) {
		if (null == dtos || dtos.size() == 0)
			return;
		Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet("punchStatistics");
		
		this.createPunchStatisticsBookSheetHead(sheet,dtos.get(0).getPunchTimesPerDay());
		for (PunchStatisticsDTO dto : dtos )
			this.setNewPunchStatisticsBookRow(sheet, dto);
		try {
			
			FileOutputStream out = new FileOutputStream(path);
			wb.write(out);
			wb.close();
			out.close();
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
					PunchServiceErrorCode.ERROR_PUNCH_ADD_DAYLOG,
					e.getLocalizedMessage());
		}
	}

	@Override
	public HttpServletResponse exportPunchStatistics(
			ExportPunchStatisticsCommand cmd ,HttpServletResponse response 
			) { 
		
		checkCompanyIdIsNull(cmd.getEnterpriseId());
		
		List<OrganizationMember> members = organizationProvider.listOrganizationMembersByOrgId(cmd.getEnterpriseId());
		
		List<Long> userIds = new ArrayList<Long>();
		for (OrganizationMember member : members) {
			if(OrganizationMemberTargetType.fromCode(member.getTargetType()) == OrganizationMemberTargetType.USER){
				userIds.add(member.getTargetId());
			}
		
		}

		
		List<PunchDayLog> result = punchProvider.listPunchDayLogs(userIds,
				cmd.getEnterpriseId(), cmd.getStartDay(), cmd.getEndDay(),
				cmd.getStatus(), cmd.getArriveTimeCompareFlag(),
				cmd.getArriveTime(), cmd.getLeaveTimeCompareFlag(),
				cmd.getLeaveTime(), cmd.getWorkTimeCompareFlag(),
				cmd.getWorkTime(), null, Integer.MAX_VALUE);
		if (null == result || result.size() ==0 )
			return null;
		
		Organization organization = organizationProvider.findOrganizationById(cmd.getEnterpriseId());
		List<String> groupTypes = new ArrayList<String>();
		groupTypes.add(OrganizationGroupType.DEPARTMENT.getCode());
		List<Organization> departments = organizationProvider.listOrganizationByGroupTypes(organization.getPath() + "/%", groupTypes);
		Map<Long, Organization> deptMap = this.convertDeptListToMap(departments);
		
		List<PunchStatisticsDTO> dtos = result
				.stream()
				.map(r -> {
					PunchStatisticsDTO dto = ConvertHelper.convert(r,
							PunchStatisticsDTO.class);
					processPunchStatisticsDTOTime(dto, r);
					if (dto != null) {
						OrganizationMember member = organizationProvider.findOrganizationMemberByOrgIdAndUId(dto.getUserId(), cmd.getEnterpriseId());
						if (null != member) {

							dto.setUserName(member.getContactName());
							dto.setUserPhoneNumber(member.getContactToken());
							Organization department = deptMap.get(member.getGroupId());
							if(null != department){
								dto.setUserDepartment(department.getName());
							}
							PunchExceptionApproval approval = punchProvider
									.getExceptionApproval(dto.getUserId(),
											dto.getEnterpriseId(),
											new java.sql.Date(dto.getPunchDate()));
							if (approval != null) {
								dto.setApprovalStatus(approval
										.getApprovalStatus());
								dto.setMorningApprovalStatus(approval.getMorningApprovalStatus());
								dto.setAfternoonApprovalStatus(approval.getAfternoonApprovalStatus());
								OrganizationMember operaor = organizationProvider.findOrganizationMemberByOrgIdAndUId(approval.getOperatorUid(), cmd.getEnterpriseId());
								dto.setOperatorName(operaor.getContactName());
							} else {
								dto.setApprovalStatus((byte) 0);
							}
						}
					}
					return dto;
				}).collect(Collectors.toList());
		
		URL rootPath = PunchServiceImpl.class.getResource("/");
		String filePath =rootPath.getPath() + this.downloadDir ;
		File file = new File(filePath);
		if(!file.exists())
			file.mkdirs();
		filePath = filePath + "PunchStatistics"+System.currentTimeMillis()+".xlsx";
		//新建了一个文件
		this.createPunchStatisticsBook(filePath, dtos);
		
		return download(filePath,response);
	}
	public HttpServletResponse download(String path, HttpServletResponse response) {
        try {
            // path是指欲下载的文件的路径。
            File file = new File(path);
            // 取得文件名。
            String filename = file.getName();
            // 取得文件的后缀名。
            String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();

            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(path));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
            
            // 读取完成删除文件
            if (file.isFile() && file.exists()) {  
                file.delete();  
            } 
        } catch (IOException ex) { 
 			LOGGER.error(ex.getMessage());
 			throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
 					PunchServiceErrorCode.ERROR_PUNCH_ADD_DAYLOG,
 					ex.getLocalizedMessage());
     		 
        }
        return response;
    }
	
}
