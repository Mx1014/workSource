package com.everhomes.techpark.punch;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
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
import java.util.HashSet;
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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;

import com.everhomes.approval.ApprovalCategory;
import com.everhomes.approval.ApprovalCategoryProvider;
import com.everhomes.approval.ApprovalDayActualTime;
import com.everhomes.approval.ApprovalDayActualTimeProvider;
import com.everhomes.approval.ApprovalRequestProvider;
import com.everhomes.approval.ApprovalRule;
import com.everhomes.approval.ApprovalRuleProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.enterprise.EnterpriseContactProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.approval.ApprovalOwnerType;
import com.everhomes.rest.approval.ApprovalType;
import com.everhomes.rest.organization.ListOrganizationContactCommand;
import com.everhomes.rest.organization.ListOrganizationMemberCommandResponse;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.organization.OrganizationGroupType;
import com.everhomes.rest.organization.OrganizationMemberDTO;
import com.everhomes.rest.organization.OrganizationMemberTargetType;
import com.everhomes.rest.techpark.punch.AbsenceTimeDTO;
import com.everhomes.rest.techpark.punch.AddPunchExceptionRequestCommand;
import com.everhomes.rest.techpark.punch.AddPunchRuleCommand;
import com.everhomes.rest.techpark.punch.ApprovalPunchExceptionCommand;
import com.everhomes.rest.techpark.punch.ApprovalStatus;
import com.everhomes.rest.techpark.punch.ClockCode;
import com.everhomes.rest.techpark.punch.DateStatus;
import com.everhomes.rest.techpark.punch.DeletePunchRuleCommand;
import com.everhomes.rest.techpark.punch.ExceptionProcessStatus;
import com.everhomes.rest.techpark.punch.ExceptionStatus;
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
import com.everhomes.rest.techpark.punch.NormalFlag;
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
import com.everhomes.rest.techpark.punch.PunchOwnerType;
import com.everhomes.rest.techpark.punch.PunchRquestType;
import com.everhomes.rest.techpark.punch.PunchRuleDTO;
import com.everhomes.rest.techpark.punch.PunchRuleMapDTO;
import com.everhomes.rest.techpark.punch.PunchServiceErrorCode;
import com.everhomes.rest.techpark.punch.PunchStatisticsDTO;
import com.everhomes.rest.techpark.punch.PunchStatus;
import com.everhomes.rest.techpark.punch.PunchTimeRuleDTO;
import com.everhomes.rest.techpark.punch.PunchTimesPerDay;
import com.everhomes.rest.techpark.punch.UpdatePunchRuleCommand;
import com.everhomes.rest.techpark.punch.ViewFlags;
import com.everhomes.rest.techpark.punch.admin.AddPunchTimeRuleCommand;
import com.everhomes.rest.techpark.punch.admin.DeleteCommonCommand;
import com.everhomes.rest.techpark.punch.admin.DeletePunchRuleMapCommand;
import com.everhomes.rest.techpark.punch.admin.ListPunchDetailsCommand;
import com.everhomes.rest.techpark.punch.admin.ListPunchDetailsResponse;
import com.everhomes.rest.techpark.punch.admin.ListPunchMonthLogsCommand;
import com.everhomes.rest.techpark.punch.admin.ListPunchMonthLogsResponse;
import com.everhomes.rest.techpark.punch.admin.ListPunchRuleMapsCommand;
import com.everhomes.rest.techpark.punch.admin.ListPunchRuleMapsResponse;
import com.everhomes.rest.techpark.punch.admin.ListPunchRulesCommonCommand;
import com.everhomes.rest.techpark.punch.admin.ListPunchRulesResponse;
import com.everhomes.rest.techpark.punch.admin.ListPunchWiFiRuleListResponse;
import com.everhomes.rest.techpark.punch.admin.ListPunchWorkdayRuleListResponse;
import com.everhomes.rest.techpark.punch.admin.PunchDayDetailDTO;
import com.everhomes.rest.techpark.punch.admin.PunchLocationRuleDTO;
import com.everhomes.rest.techpark.punch.admin.PunchWiFiDTO;
import com.everhomes.rest.techpark.punch.admin.PunchWiFiRuleDTO;
import com.everhomes.rest.techpark.punch.admin.PunchWorkdayRuleDTO;
import com.everhomes.rest.techpark.punch.admin.QryPunchLocationRuleListResponse;
import com.everhomes.rest.techpark.punch.admin.UpdatePunchTimeRuleCommand;
import com.everhomes.rest.techpark.punch.admin.UserMonthLogsDTO;
import com.everhomes.rest.techpark.punch.admin.listPunchTimeRuleListResponse;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.ListUtils;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.WebTokenGenerator;
import com.mysql.fabric.xmlrpc.base.Array;

@Service
public class PunchServiceImpl implements PunchService {
	final String downloadDir ="\\download\\";
	private static final Logger LOGGER = LoggerFactory
			.getLogger(PunchServiceImpl.class);

	SimpleDateFormat timeSF = new SimpleDateFormat("HH:mm:ss");
	SimpleDateFormat dateSF = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat monthSF = new SimpleDateFormat("yyyyMM");
	SimpleDateFormat datetimeSF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Autowired
	private PunchProvider punchProvider;
	
	@Autowired
	private UserProvider userProvider;

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
	private ApprovalRuleProvider approvalRuleProvider;
	
	@Autowired
	private ApprovalRequestProvider approvalRequestProvider;
	
	@Autowired
	private ApprovalDayActualTimeProvider approvalDayActualTimeProvider;
	
	@Autowired
	private ApprovalCategoryProvider approvalCategoryProvider;

    
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
					pdl.setPunchStatusNew(pdl.getPunchStatus());
					pdl.setMorningPunchStatusNew(pdl.getMorningPunchStatus());
					pdl.setAfternoonPunchStatusNew(pdl.getAfternoonPunchStatus());
					if(pdl.getPunchStatus() != null && ApprovalStatus.FORGOT.equals(ApprovalStatus.fromCode(pdl.getPunchStatus())))
						pdl.setPunchStatus(ApprovalStatus.UNPUNCH.getCode());
					if(pdl.getMorningPunchStatus() != null && ApprovalStatus.FORGOT.equals(ApprovalStatus.fromCode(pdl.getMorningPunchStatus())))
						pdl.setMorningPunchStatus(ApprovalStatus.UNPUNCH.getCode());
					if(pdl.getAfternoonPunchStatus() != null && ApprovalStatus.FORGOT.equals(ApprovalStatus.fromCode(pdl.getAfternoonPunchStatus())))
						pdl.setAfternoonPunchStatus(ApprovalStatus.UNPUNCH.getCode());
					pdl.setApprovalStatusNew(pdl.getApprovalStatus());
					pdl.setMorningApprovalStatusNew(pdl.getMorningApprovalStatus());
					pdl.setAfternoonApprovalStatusNew(pdl.getAfternoonApprovalStatus());
					if(pdl.getApprovalStatus() != null && ApprovalStatus.FORGOT.equals(ApprovalStatus.fromCode(pdl.getApprovalStatus())))
						pdl.setApprovalStatus(ApprovalStatus.UNPUNCH.getCode());
					if(pdl.getMorningApprovalStatus() != null && ApprovalStatus.FORGOT.equals(ApprovalStatus.fromCode(pdl.getMorningApprovalStatus())))
						pdl.setMorningApprovalStatus(ApprovalStatus.UNPUNCH.getCode());
					if(pdl.getAfternoonApprovalStatus() != null && ApprovalStatus.FORGOT.equals(ApprovalStatus.fromCode(pdl.getAfternoonApprovalStatus())))
						pdl.setAfternoonApprovalStatus(ApprovalStatus.UNPUNCH.getCode());
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
/**
 * 从数据库取某天某人的打卡记录,如果为空则计算.
 * 
 * 并取审批状态
 * 
 * */
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
		pdl.setWorkTime(convertTimeToGMTMillisecond(punchDayLog.getWorkTime()));
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
				pdl.setExceptionStatus(calculateExceptionCode(exceptionApproval.getApprovalStatus()) );
			 
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
				Byte morningStatus = pdl.getMorningPunchStatus();
				if(null!=exceptionApproval.getMorningApprovalStatus())
					morningStatus = exceptionApproval.getMorningApprovalStatus();
				Byte afternoonStatus = pdl.getAfternoonPunchStatus();
				if(null!=exceptionApproval.getAfternoonApprovalStatus())
					afternoonStatus = exceptionApproval.getAfternoonApprovalStatus();
				if (calculateExceptionCode(afternoonStatus).equals(ExceptionStatus.NORMAL.getCode())
						&&calculateExceptionCode(morningStatus).equals(ExceptionStatus.NORMAL.getCode())) {
				 
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
	
	private Byte calculateExceptionCode(Byte status) {
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
		
		pdl = calculateDayLog(userId, companyId, logDay, pdl,newPunchDayLog);
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
		newPunchDayLog.setExceptionStatus(pdl.getExceptionStatus());
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
		if (null == arriveTime)
			return null;
		if (arriveTime.equals(0L)) {
			return java.sql.Time.valueOf(getGMTtimeString("HH:mm:ss",
					arriveTime));
		} else {
			return new java.sql.Time(arriveTime);
		}

	}

	/***
	 * @param logDay
	 *            : 计算的打卡日期
	 * @return PunchLogsDayList：计算好的当日打卡状态
	 * */
	@Override
	public PunchLogsDay makePunchLogsDayListInfo(Long userId,
			Long companyId, Calendar logDay) {
		Date now = new Date();
		
		PunchRule pr = this.getPunchRule(PunchOwnerType.ORGANIZATION.getCode(), companyId, userId);
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
						"ERROR IN REFRESHPUNCHDAYLOG  ");
			}
			if (null == punchDayLog) {
				// 验证后为空
				return new PunchLogsDay();
			}
		} 
		PunchLogsDay pdl = ConvertHelper.convert(punchDayLog, PunchLogsDay.class) ;
		if(punchDayLog.getArriveTime()!=null)
			pdl.setArriveTime(punchDayLog.getArriveTime().getTime());
		if(punchDayLog.getLeaveTime()!=null)
			pdl.setLeaveTime(punchDayLog.getLeaveTime().getTime());
		if(punchDayLog.getAfternoonArriveTime()!=null)
			pdl.setAfternoonArriveTime(punchDayLog.getAfternoonArriveTime().getTime());
		if(punchDayLog.getNoonLeaveTime()!=null)
			pdl.setNoonLeaveTime(punchDayLog.getNoonLeaveTime().getTime());
		

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
		//通过打卡记录计算状态
		// 如果是非工作日和当天，则异常为normal
		if (!isWorkDay(logDay.getTime(),pr)
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
				//上午为加班或者普通 且 下午为加班或者普通 则
				pdl.setExceptionStatus((punchDayLog.getMorningStatus().equals(
						PunchStatus.NORMAL.getCode())||punchDayLog.getMorningStatus().equals(
								PunchStatus.OVERTIME.getCode()))&&(punchDayLog.getAfternoonStatus().equals(
										PunchStatus.NORMAL.getCode())||punchDayLog.getAfternoonStatus().equals(
												PunchStatus.OVERTIME.getCode())) ? ExceptionStatus.NORMAL
						.getCode() : ExceptionStatus.EXCEPTION.getCode());
				 
				
			}
		}
		//通过审批计算状态
		PunchExceptionApproval exceptionApproval = punchProvider.getPunchExceptionApprovalByDate(userId, companyId,
				dateSF.format(logDay.getTime()));
		if (null != exceptionApproval) {
			pdl.setMorningApprovalStatus(exceptionApproval.getMorningApprovalStatus());
			pdl.setAfternoonApprovalStatus(exceptionApproval.getAfternoonApprovalStatus());
			if (calculateExceptionCode(pdl.getAfternoonApprovalStatus()).equals(ExceptionStatus.NORMAL.getCode())
					&&calculateExceptionCode(pdl.getMorningApprovalStatus()).equals(ExceptionStatus.NORMAL.getCode())) {
			 
				pdl.setExceptionStatus(ExceptionStatus.NORMAL.getCode());
			}
		}
		List<PunchExceptionRequest> exceptionRequests = punchProvider
				.listExceptionRequestsByDate(userId, companyId,
						dateSF.format(logDay.getTime()));
		if (exceptionRequests.size() > 0) {
			for (PunchExceptionRequest exceptionRequest : exceptionRequests) {
				//是否有请求的flag
				if(exceptionRequest.getApprovalStatus() != null ){
					pdl.setRequestFlag(NormalFlag.YES.getCode());
					pdl.setRequestToken(WebTokenGenerator.getInstance().toWebToken(exceptionRequest.getRequestId()));
				}
				if(exceptionRequest.getMorningApprovalStatus() != null ){
					pdl.setMorningRequestFlag(NormalFlag.YES.getCode());
					pdl.setMorningRequestToken(WebTokenGenerator.getInstance().toWebToken(exceptionRequest.getRequestId()));
				}
				if(exceptionRequest.getAfternoonApprovalStatus() != null ){
					pdl.setAfternoonRequestFlag(NormalFlag.YES.getCode());
					pdl.setAfternoonRequestToken(WebTokenGenerator.getInstance().toWebToken(exceptionRequest.getRequestId()));
				}
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
	private PunchLogsDay calculateDayLog(Long userId, Long companyId,
			Calendar logDay, PunchLogsDay pdl, PunchDayLog punchDayLog) throws ParseException {
		List<PunchLog> punchLogs = punchProvider.listPunchLogsByDate(userId,
				companyId, dateSF.format(logDay.getTime()),
				ClockCode.SUCESS.getCode());
		Date now = new Date();
		PunchRule pr = this.getPunchRule(PunchOwnerType.ORGANIZATION.getCode(),companyId, userId );
		if(null == pr)
			throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
					PunchServiceErrorCode.ERROR_ENTERPRISE_DIDNOT_SETTING,
					"have no punch rule");
		PunchTimeRule punchTimeRule = this.punchProvider.findPunchTimeRuleById(pr.getTimeRuleId());
		 
		
		if (null == punchTimeRule)
			throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"公司还没有打卡规则 ");
		pdl.setPunchTimesPerDay(punchTimeRule.getPunchTimesPerDay());
		// 如果零次打卡记录
		if (null == punchLogs || punchLogs.size() == 0) {
			if (!isWorkDay(logDay.getTime(),pr)|| dateSF.format(now).equals(dateSF.format(logDay.getTime()))) {
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
		//非工作日按照两次计算工作时长
		if(!isWorkDay(logDay.getTime(),pr) || PunchTimesPerDay.TWICE.getCode().equals(punchTimeRule.getPunchTimesPerDay())){
			if (punchLogs.size() == 1) {
				punchDayLog.setArriveTime(getDAOTime(punchLogs.get(0).getPunchTime().getTime()));
				// 如果只有一次打卡:工作日忘打卡,休息日正常
//				PunchLogDTO arriveLogDTO = new PunchLogDTO();
//				arriveLogDTO.setPunchTime(punchLogs.get(0).getPunchTime().getTime());
//				pdl.getPunchLogs().add(arriveLogDTO);
//				PunchLogDTO noPunchLogDTO2 = new PunchLogDTO();
//				pdl.getPunchLogs().add(noPunchLogDTO2);
				if (!isWorkDay(logDay.getTime(),pr)){
					// 如果非工作日 NORMAL
					pdl.setPunchStatus(PunchStatus.NORMAL.getCode()); 
					pdl.setMorningPunchStatus(PunchStatus.NORMAL.getCode());
					pdl.setAfternoonPunchStatus(PunchStatus.NORMAL.getCode());
					pdl.setExceptionStatus(ExceptionStatus.NORMAL.getCode());
					return pdl;
				}
				if (dateSF.format(now).equals(
								dateSF.format(logDay.getTime()))) {
					//今天的话,exception
					pdl.setPunchStatus(PunchStatus.FORGOT.getCode());
					pdl.setExceptionStatus(ExceptionStatus.NORMAL.getCode());
				 
					return pdl;
				}
				pdl.setPunchStatus(PunchStatus.FORGOT.getCode());
				pdl.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
				makeExceptionForDayList(userId, companyId, logDay, pdl);
				return pdl;
			}
			
			Calendar startMinTime = Calendar.getInstance();
			Calendar startMaxTime = Calendar.getInstance();
			Calendar workTime = Calendar.getInstance();
			startMinTime.setTime(datetimeSF.parse(dateSF.format(punchLogs.get(0).getPunchDate())+ " "
					+ timeSF.format(punchTimeRule.getStartEarlyTime())));
	
			startMaxTime.setTime(datetimeSF.parse(dateSF.format(punchLogs.get(0).getPunchDate())+ " "
					+ timeSF.format(punchTimeRule.getStartLateTime())));
	
			workTime.setTime(datetimeSF.parse(dateSF.format(punchLogs.get(0)
					.getPunchDate()) + " " + timeSF.format(punchTimeRule.getWorkTime())));
			List<Calendar> punchMinAndMaxTime = getMinAndMaxTimeFromPunchlogs(punchLogs);
			Calendar arriveCalendar = punchMinAndMaxTime.get(0);
			Calendar leaveCalendar = punchMinAndMaxTime.get(1);
			Time leaveCalendarTime = Time.valueOf(timeSF.format(leaveCalendar.getTime()));
			Time arriveCalendarTime = Time.valueOf(timeSF.format(arriveCalendar.getTime()));
			Time AfternoonArriveTimeTime = Time.valueOf(timeSF.format(punchTimeRule.getAfternoonArriveTime()));
			Time NoonLeaveTimeTime = Time.valueOf(timeSF.format(punchTimeRule.getNoonLeaveTime()));
			long realWorkTime = 0L;
			if(leaveCalendarTime.after(AfternoonArriveTimeTime)&&arriveCalendarTime.before(NoonLeaveTimeTime)){
				realWorkTime =leaveCalendar.getTimeInMillis() - arriveCalendar.getTimeInMillis()
					-punchTimeRule.getAfternoonArriveTime().getTime() +punchTimeRule.getNoonLeaveTime().getTime();
			}else {
				realWorkTime =leaveCalendar.getTimeInMillis() - arriveCalendar.getTimeInMillis();
						
			}
			punchDayLog.setArriveTime(getDAOTime(arriveCalendar.getTimeInMillis()));
			punchDayLog.setLeaveTime(getDAOTime(leaveCalendar.getTimeInMillis() )); 
			punchDayLog.setWorkTime( convertTime(realWorkTime) );
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
			if (!isWorkDay(logDay.getTime(),pr)){
				pdl.setPunchStatus(PunchStatus.OVERTIME.getCode());
				pdl.setMorningPunchStatus(PunchStatus.OVERTIME.getCode());
				pdl.setAfternoonPunchStatus(PunchStatus.OVERTIME.getCode());
				pdl.setExceptionStatus(ExceptionStatus.NORMAL.getCode()); 
				return pdl;
			}
		}
		else if(PunchTimesPerDay.FORTH.getCode().equals(punchTimeRule.getPunchTimesPerDay())){
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
				punchDayLog.setWorkTime(convertTime(0L)); 
				pdl.setMorningPunchStatus(PunchStatus.UNPUNCH.getCode());
				pdl.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode()); 
			}
			// 如果下午零次打卡记录
			if (null == afternoonLogs || afternoonLogs.size() == 0) {
				punchDayLog.setWorkTime(convertTime(0L));
				pdl.setAfternoonPunchStatus(PunchStatus.UNPUNCH.getCode());
				pdl.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode()); 	
			}
 
			// 如果上午1次打卡记录
			if ( morningLogs.size() == 1) {
				punchDayLog.setArriveTime(getDAOTime(morningLogs.get(0).getPunchTime().getTime()));
				punchDayLog.setWorkTime(convertTime(0L));
				pdl.setMorningPunchStatus(PunchStatus.FORGOT.getCode());
				pdl.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode()); 
			}
			// 如果下午1次打卡记录
			if ( afternoonLogs.size() == 1) {
				punchDayLog.setAfternoonArriveTime(getDAOTime(afternoonLogs.get(0).getPunchTime().getTime()));
				punchDayLog.setWorkTime(convertTime(0L));
				pdl.setAfternoonPunchStatus(PunchStatus.FORGOT.getCode());
				pdl.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode()); 	
			}
			Calendar startMinTime = Calendar.getInstance();
			Calendar startMaxTime = Calendar.getInstance();
			Calendar noonLeaveTime = Calendar.getInstance();
			Calendar afternoonArriveTime = Calendar.getInstance();
			Calendar workTime = Calendar.getInstance();
			Calendar arriveCalendar = Calendar.getInstance();
			startMinTime.setTime(datetimeSF.parse(dateSF.format(punchLogs.get(0).getPunchDate())+ " "
					+ timeSF.format(punchTimeRule.getStartEarlyTime())));
			startMaxTime.setTime(datetimeSF.parse(dateSF.format(punchLogs.get(0).getPunchDate())+ " "
					+ timeSF.format(punchTimeRule.getStartLateTime())));
			noonLeaveTime.setTime(datetimeSF.parse(dateSF.format(punchLogs.get(0).getPunchDate())+ " "
					+ timeSF.format(punchTimeRule.getNoonLeaveTime())));
			afternoonArriveTime.setTime(datetimeSF.parse(dateSF.format(punchLogs.get(0).getPunchDate())+ " "
					+ timeSF.format(punchTimeRule.getAfternoonArriveTime())));
			workTime.setTime(datetimeSF.parse(dateSF.format(punchLogs.get(0)
					.getPunchDate()) + " " + timeSF.format(punchTimeRule.getWorkTime())));
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
				if(!pdl.getMorningPunchStatus().equals(PunchStatus.NORMAL.getCode())){
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
				
			}
			punchDayLog.setWorkTime(convertTime(realWorkTime));
			// 如果是当日，则设置打卡考勤为正常并返回
			if (!isWorkDay(logDay.getTime(),pr)){
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
		//  
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
					OrganizationMember member = this.organizationProvider.findOrganizationMemberByOrgIdAndUId(exceptionRequest.getOperatorUid(), companyId);

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
//		if (cmd.getLatitude() == null || cmd.getLatitude().equals(0))
//			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
//					ErrorCodes.ERROR_INVALID_PARAMETER,
//					"Invalid Latitude parameter in the command");
//		if (cmd.getLongitude() == null || cmd.getLongitude().equals(0))
//			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
//					ErrorCodes.ERROR_INVALID_PARAMETER,
//					"Invalid Longitude parameter in the command");
		PunchClockResponse response = new PunchClockResponse();
		Long userId = UserContext.current().getUser().getId(); 
		// new Date()为获取当前系统时间为打卡时间
		String punchTime = datetimeSF.format(new Date());
		PunchLog punchLog = ConvertHelper.convert(cmd, PunchLog.class);
		punchLog.setUserId(userId);
		
		punchLog.setPunchTime(Timestamp.valueOf(punchTime));

		Calendar punCalendar = Calendar.getInstance();
		try {
			punCalendar.setTime(datetimeSF.parse(punchTime));
		} catch (ParseException e) {

			e.printStackTrace();
		}
		PunchRule pr = getPunchRule(PunchOwnerType.ORGANIZATION.getCode(), cmd.getEnterpriseId(), userId);
		if (null == pr  )
			throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
 					PunchServiceErrorCode.ERROR_ENTERPRISE_DIDNOT_SETTING,
 				"公司没有设置打卡规则");
		PunchTimeRule ptr = punchProvider.getPunchTimeRuleById(pr.getTimeRuleId());
		if (null == ptr  )
			throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
 					PunchServiceErrorCode.ERROR_ENTERPRISE_DIDNOT_SETTING,
 				"公司没有设置打卡时间规则");
		//把当天的时分秒转换成Long型
		Long punchTimeLong = punCalendar.get(Calendar.HOUR_OF_DAY)*3600*1000L; //hour
		punchTimeLong += punCalendar.get(Calendar.MINUTE)*60*1000L; //min
		punchTimeLong += punCalendar.get(Calendar.SECOND)*1000L;//second
		//默认分界点是5点,如果timerule有设置就用设置的
		Long splitTime = 5*3600*1000L;
		if(null != ptr.getDaySplitTime())
			splitTime = convertTimeToGMTMillisecond(ptr.getDaySplitTime());
		if (punchTimeLong.compareTo(splitTime)<0) {
			// 如果打卡时间小于设定的分界点，就用昨天
			punCalendar.add(Calendar.DATE, -1);
		}
		punchLog.setPunchDate(java.sql.Date.valueOf(dateSF.format(punCalendar
				.getTime())));
		
		try{
			response.setPunchCode(verifyPunchClock(cmd).getCode());
			punchLog.setPunchStatus(response.getPunchCode());
		}catch(Exception e){
			//有报错就表示不成功
			punchLog.setPunchStatus(ClockCode.FAIL.getCode());
			throw e;
		}finally{
			//无论如何保留打卡记录
			punchProvider.createPunchLog(punchLog);
		}
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
		response.setPunchTime(punchTime);

		return response;
	}

	private ClockCode verifyPunchClock(PunchClockCommand cmd) {
		//获取打卡规则
		ClockCode code = ClockCode.SUCESS;
		Long userId = UserContext.current().getUser().getId(); 
		PunchRule pr = getPunchRule(PunchOwnerType.ORGANIZATION.getCode(), cmd.getEnterpriseId(), userId);
		if (null == pr  )
			throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
 					PunchServiceErrorCode.ERROR_ENTERPRISE_DIDNOT_SETTING,
 				"公司没有设置打卡规则");
		//是否有wifi打卡,如果是判断wifi是否符合
		if(pr.getWifiRuleId() != null && cmd.getWifiMac() != null){
			List<PunchWifi> wifis = this.punchProvider.listPunchWifisByRuleId(PunchOwnerType.ORGANIZATION.getCode(), cmd.getEnterpriseId(), pr.getWifiRuleId()) ;
			if(null != wifis){
				for(PunchWifi wifi : wifis){
					if(null != wifi.getMacAddress() && wifi.getMacAddress().toLowerCase().equals(cmd.getWifiMac().toLowerCase()))
						return ClockCode.SUCESS;
				}
				
			}
		}
		if(null == pr.getLocationRuleId()){
			//wifi不符合看是否有地址规则,没有地址规则直接报错
			if(null == cmd.getWifiMac())
				throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
	 					PunchServiceErrorCode.ERROR_WIFI_NULL,"wifi mac address is null");
			else
				throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
	 					PunchServiceErrorCode.ERROR_WIFI_WRONG,"wifi mac address is wrong");
		}
		//有地址规则查看参数是否有地址规则,无则报错
		if(null == cmd.getLatitude() ||null == cmd.getLongitude() )
			throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
 					PunchServiceErrorCode.ERROR_GEOPOINT_NULL,"user location is null");
		//参数有地址规则看地址范围是否正确,不正确则报错
		List<PunchGeopoint> punchGeopoints = punchProvider
				.listPunchGeopointsByRuleId(PunchOwnerType.ORGANIZATION.getCode(), cmd.getEnterpriseId(),pr.getLocationRuleId());
		if (null == punchGeopoints || punchGeopoints.size() == 0)
			throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
 					PunchServiceErrorCode.ERROR_ENTERPRISE_DIDNOT_SETTING,
 				"公司没有设置打卡规则");
		for (PunchGeopoint punchGeopoint : punchGeopoints) {
			if (calculateDistance(cmd.getLongitude(), cmd.getLatitude(),
					punchGeopoint.getLongitude(), punchGeopoint.getLatitude()) <= punchGeopoint
					.getDistance()) {
				// 如果找到了一个就跳出
				return ClockCode.SUCESS; 
			} 
		 
		}
		if(null == pr.getWifiRuleId())
			throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
 					PunchServiceErrorCode.ERROR_USER_NOT_IN_PUNCHAREA,"not in punch area");
		else
			throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
					PunchServiceErrorCode.ERROR_NOT_IN_AREA_AND_WIFI,"not in punch area and not in wifi");
 
	}

	/*** return 两个坐标之间的距离 单位 米 */
	private double calculateDistance(double longitude1, double latitude1,
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
		PunchTimeRule punchRule = punchProvider.findPunchTimeRuleByCompanyId(cmd
				.getEnterpriseId()); 
		if(punchRule == null) {
			punchRule = ConvertHelper.convert(cmd, PunchTimeRule.class);
 			punchRule.setAfternoonArriveTime(convertTime(cmd.getAfternoonArriveTime()));
			punchRule.setPunchTimesPerDay(cmd.getPunchTimesPerDay());
			punchRule.setNoonLeaveTime(convertTime(cmd.getNoonLeaveTime())); 
			convertTime(punchRule, cmd.getStartEarlyTime(), cmd.getStartLateTime(), cmd.getEndEarlyTime());
			punchRule.setCreatorUid(userId);
			punchRule.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
					.getTime()));
			punchProvider.createPunchTimeRule(punchRule);
//			createPunchGeopoints(userId, cmd.getPunchGeoPoints(),cmd.getEnterpriseId());
		}

	}
	@Override
	public void updatePunchRule(UpdatePunchRuleCommand cmd) {
//		Long userId = UserContext.current().getUser().getId();
//		checkCompanyIdIsNull(cmd.getEnterpriseId());
//		PunchTimeRule punchRule = punchProvider.findPunchTimeRuleByCompanyId(cmd
//				.getEnterpriseId()); 
//		if (punchRule != null) {
//			convertTime(punchRule, cmd.getStartEarlyTime(), cmd.getStartLateTime(), cmd.getEndEarlyTime());
//			punchRule.setAfternoonArriveTime(convertTime(cmd.getAfternoonArriveTime()));
//			punchRule.setPunchTimesPerDay(cmd.getPunchTimesPerDay());
//			punchRule.setNoonLeaveTime(convertTime(cmd.getNoonLeaveTime()));
//			punchRule.setOperatorUid(userId);
//			punchRule.setOperateTime(new Timestamp(DateHelper.currentGMTTime()
//					.getTime()));
//			punchProvider.updatePunchTimeRule(punchRule);
//			if (null!=cmd.getPunchGeoPoints()) {
//				List<PunchGeopoint> geopoints = punchProvider
//						.listPunchGeopointsByCompanyId(cmd.getEnterpriseId());
//				if (geopoints != null && geopoints.size() > 0) {
//					for (PunchGeopoint punchGeopoint : geopoints) {
//						punchProvider.deletePunchGeopoint(punchGeopoint);
//					}
//				}
////				createPunchGeopoints(userId, cmd.getPunchGeoPoints(),
////						cmd.getEnterpriseId());
//			}
//		}
	}
	
	private void createPunchGeopoints(Long userId, List<PunchGeoPointDTO> punchGeoPoints,
		String ownerType,	Long ownerId) { 
		for (PunchGeoPointDTO punchGeopointDTO : punchGeoPoints) {
			PunchGeopoint punchGeopoint = ConvertHelper.convert(punchGeopointDTO, PunchGeopoint.class);
			punchGeopoint.setOwnerType(ownerType);
			punchGeopoint.setOwnerId(ownerId); 
			punchGeopoint.setCreatorUid(userId);
			punchGeopoint.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			punchGeopoint.setGeohash(GeoHashUtils.encode(
					punchGeopoint.getLatitude(), punchGeopoint.getLongitude()));
			punchProvider.createPunchGeopoint(punchGeopoint);
		}
	}

	private void convertTime(PunchTimeRule punchRule, Long startEarlyTime,
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
	 
	private Long convertTimeToGMTMillisecond(Time time) {
		if (null != time) {
			//从8点开始计算
			return time.getTime()+MILLISECONDGMT;
		}
		return null;
	}
	
    private final Long MILLISECONDGMT=8*3600*1000L;
	@Override
	public GetPunchRuleCommandResponse getPunchRuleByCompanyId(
			GetPunchRuleCommand cmd) {
		GetPunchRuleCommandResponse response = new GetPunchRuleCommandResponse();
//		PunchRuleDTO dto = null;
//		checkCompanyIdIsNull(cmd.getEnterpriseId());
//		PunchTimeRule punchRule = punchProvider.findPunchRuleByCompanyId(cmd
//				.getEnterpriseId());
//		if (punchRule != null) {
//			dto = ConvertHelper.convert(punchRule, PunchRuleDTO.class);
//			dto.setAfternoonArriveTime(punchRule.getAfternoonArriveTime().getTime());
//			dto.setNoonLeaveTime(punchRule.getNoonLeaveTime().getTime());
//			dto.setStartEarlyTime(punchRule.getStartEarlyTime().getTime());
//			dto.setStartLateTime(punchRule.getStartLateTime().getTime());
//			dto.setEndEarlyTime(punchRule.getStartEarlyTime().getTime() + punchRule.getWorkTime().getTime() +MILLISECONDGMT);
//			List<PunchGeopoint> geopoints = punchProvider
//					.listPunchGeopointsByCompanyId(cmd.getEnterpriseId());
//			dto.setPunchGeoPoints(new ArrayList<PunchGeoPointDTO>());
//			for (PunchGeopoint point : geopoints){
//				dto.getPunchGeoPoints().add(ConvertHelper.convert(point,PunchGeoPointDTO.class));
//			}
//		}
//		response.setPunchRuleDTO(dto);
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
		PunchTimeRule punchRule = punchProvider.findPunchTimeRuleByCompanyId(cmd
				.getEnterpriseId());
		if (punchRule != null) {
			punchProvider.deletePunchTimeRule(punchRule);
			List<PunchGeopoint> geopoints = punchProvider
					.listPunchGeopointsByCompanyId(cmd.getEnterpriseId());
			if (geopoints != null && geopoints.size() > 0) {
				for (PunchGeopoint punchGeopoint : geopoints) {
					punchProvider.deletePunchGeopoint(punchGeopoint);
				}
			}
		}
	}

//	// 如果查询时间为空，重置时间范围。默认为上个月。
//	private void processQueryCommandDay(ListPunchCountCommand cmd) {
//		Calendar startCalendar = Calendar.getInstance();
//		Calendar endCalendar = Calendar.getInstance();
//		
//		Long startDay = cmd.getStartDay();
//		Long endDay = cmd.getEndDay();
//		if (startDay == null && endDay == null ) {
//			startCalendar.setTime(new Date());
//			startCalendar.add(Calendar.MONTH, -1);
//			startCalendar.set(Calendar.DAY_OF_MONTH, 1);
//			cmd.setStartDay( startCalendar.getTime().getTime());
//
//			endCalendar.setTime(new Date());
//			endCalendar.set(Calendar.DAY_OF_MONTH, 1);
//			cmd.setEndDay(  endCalendar.getTime().getTime());
//		}
//	}

	// 计算两个日期间工作日天数，不包含结束时间
	private Integer countWorkDayCount(Calendar startCalendar, Calendar endCalendar ,PunchRule pr) {
		Integer workDayCount = 0;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startCalendar.getTime());
		while (true) {
			if (isWorkDay(calendar.getTime(),pr)) {
				workDayCount++;
			}
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			if (calendar.after(endCalendar)) {
				return workDayCount;
			}
		} 
	}

	@Override
	public boolean isWorkDay(Date date1,PunchRule punchRule) {
		if (date1 == null)
			return false;
		SimpleDateFormat dateSF = new SimpleDateFormat("yyyy-MM-dd");
		int openWeekInt = 111110;
		if(null != punchRule.getWorkdayRuleId()){
			// 如果属于周末调班 返回工作日
			List<PunchHoliday> workDates = this.punchProvider.queryPunchHolidaysByStatus(punchRule.getOwnerType(),punchRule.getOwnerId(),
					punchRule.getWorkdayRuleId(),DateStatus.WORKDAY.getCode());
			if (null != workDates) {
				for (PunchHoliday workDate : workDates) {
					if (dateSF.format(date1).equals(dateSF.format(workDate.getRuleDate())))
						return true;
				}
			}
			// 如果属于工作日休假 返回非工作日
			List<PunchHoliday> weekenDates = this.punchProvider.queryPunchHolidaysByStatus(punchRule.getOwnerType(),punchRule.getOwnerId(),
					punchRule.getWorkdayRuleId(),DateStatus.HOLIDAY.getCode());
			if (null != weekenDates) {
				for (PunchHoliday weekenDate : weekenDates) {
					if (dateSF.format(date1).equals(dateSF.format(weekenDate.getRuleDate())))
						return false;
				}
			}
			PunchWorkdayRule workdayRule = this.punchProvider.getPunchWorkdayRuleById(punchRule.getWorkdayRuleId());
			openWeekInt = Integer.valueOf(workdayRule.getWorkWeekDates());
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date1);
		 // 获取日期周几
//		Locale.setDefault(Locale.US);
		Integer weekDay = calendar.get(Calendar.DAY_OF_WEEK)-1;
		//将七位0111110这样的代码转换成一个存储星期几的list
		
		List<Integer> workDays=new ArrayList<Integer>();
        for(int i=0;i<7;i++){
        	if(openWeekInt%10 == 1)
        		workDays.add(i);
        	openWeekInt = openWeekInt/10;
        }
        //如果这个DAY_OF_WEEK 在 工作日list中,则返回真
        if(workDays.contains(weekDay))
        	return true;
        
        return false;

	}

	private boolean isWorkTime(Time time, PunchTimeRule punchTimeRule) {
		if (time == null || punchTimeRule == null) {
			return false;
		}
		time = Time.valueOf(timeSF.format(time));
		//时间在最早上班时间到最晚下班时间之间且不在午休时间范围内
		if (!time.before(punchTimeRule.getStartEarlyTime()) && !time.after(getEndTime(punchTimeRule.getStartLateTime(), punchTimeRule.getWorkTime()))
				&& !(time.after(punchTimeRule.getNoonLeaveTime()) && time.before(punchTimeRule.getAfternoonArriveTime()))) {
			return true;
		}
		return false;
	}
	
	@Override
	public Time getEndTime(Time startTime, Time workTime){
		return new Time(convertTimeToGMTMillisecond(startTime) + convertTimeToGMTMillisecond(workTime) - MILLISECONDGMT);
	}
	
	@Override
	public boolean isWorkTime(Time time, PunchRule punchRule) {
		return isWorkTime(time, getPunchTimeRule(punchRule));
	}
	
	private boolean isRestTime(Date fromTime, Date endTime, PunchTimeRule punchTimeRule){
		return isSameDay(fromTime, endTime)
				&& timeSF.format(fromTime).equals(timeSF.format(punchTimeRule.getNoonLeaveTime()))
				&& timeSF.format(endTime).equals(timeSF.format(punchTimeRule.getAfternoonArriveTime()));
	}
	
	@Override
	public boolean isSameDay(Date date1, Date date2) {
		return dateSF.format(date1).equals(dateSF.format(date2));
	}
	
	@Override
	public boolean isRestTime(Date fromTime, Date endTime, PunchRule punchRule) {
		return isRestTime(fromTime, endTime, getPunchTimeRule(punchRule));
	}
	
	@Override
	public PunchTimeRule getPunchTimeRule(PunchRule punchRule) {
		if (punchRule != null && punchRule.getTimeRuleId() != null) {
			return punchProvider.findPunchTimeRuleById(punchRule.getTimeRuleId());
		}
		return null;
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

		List<String> groupTypeList = new ArrayList<String>();
		groupTypeList.add(OrganizationGroupType.ENTERPRISE.getCode());
		groupTypeList.add(OrganizationGroupType.DEPARTMENT.getCode());
		List<OrganizationMemberDTO> organizationMembers = this.organizationService.listAllChildOrganizationPersonnel
				(cmd.getEnterpriseId(), groupTypeList, cmd.getKeyword()) ;
		if(null == organizationMembers)
			return response;
		List<Long> userIds = new ArrayList<Long>();
		for(OrganizationMemberDTO member : organizationMembers){
			userIds.add(member.getTargetId());
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

		if(cmd.getStatus() == null){
			LOGGER.error("Invalid Status parameter in the command");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid Status   parameter in the command");
			
		}
		if(cmd.getStatus().equals(ExceptionProcessStatus.ACTIVE.getCode()) 
				&& cmd.getMorningApprovalStatus() == null && cmd.getAfternoonApprovalStatus() == null && cmd.getApprovalStatus() == null ) {
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

	/**
	 * 打卡1.0的统计,已经不用了
	 * */
	@Override
	public ListPunchStatisticsCommandResponse listPunchStatistics(
			ListPunchStatisticsCommand cmd) {
		checkCompanyIdIsNull(cmd.getEnterpriseId());
		ListPunchStatisticsCommandResponse response = new ListPunchStatisticsCommandResponse();
		cmd.setPageOffset(cmd.getPageOffset() == null ? 1 : cmd.getPageOffset());
		ListOrganizationContactCommand orgCmd = new ListOrganizationContactCommand();
		orgCmd.setOrganizationId(cmd.getEnterpriseId());
//		if(null != cmd.getEnterpriseGroupId()){
//			orgCmd.setOrganizationId(cmd.getEnterpriseGroupId());
//		}
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
			dto.setArriveTime(  convertTimeToGMTMillisecond(r.getArriveTime())  );

		if(null!= r.getLeaveTime())
			dto.setLeaveTime( convertTimeToGMTMillisecond(r.getLeaveTime()));

		if(null!= r.getWorkTime())
			dto.setWorkTime( convertTimeToGMTMillisecond( r.getWorkTime()));

		if(null!= r.getNoonLeaveTime())
			dto.setNoonLeaveTime(  convertTimeToGMTMillisecond(r.getNoonLeaveTime()));

		if(null!= r.getAfternoonArriveTime())
			dto.setAfternoonArriveTime(  convertTimeToGMTMillisecond(r.getAfternoonArriveTime()));
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
		pdl.setPunchStatusNew(pdl.getPunchStatus());
		pdl.setMorningPunchStatusNew(pdl.getMorningPunchStatus());
		pdl.setAfternoonPunchStatusNew(pdl.getAfternoonPunchStatus());
		if(pdl.getPunchStatus() != null && ApprovalStatus.FORGOT.equals(ApprovalStatus.fromCode(pdl.getPunchStatus())))
			pdl.setPunchStatus(ApprovalStatus.UNPUNCH.getCode());
		if(pdl.getMorningPunchStatus() != null && ApprovalStatus.FORGOT.equals(ApprovalStatus.fromCode(pdl.getMorningPunchStatus())))
			pdl.setMorningPunchStatus(ApprovalStatus.UNPUNCH.getCode());
		if(pdl.getAfternoonPunchStatus() != null && ApprovalStatus.FORGOT.equals(ApprovalStatus.fromCode(pdl.getAfternoonPunchStatus())))
			pdl.setAfternoonPunchStatus(ApprovalStatus.UNPUNCH.getCode());
		pdl.setApprovalStatusNew(pdl.getApprovalStatus());
		pdl.setMorningApprovalStatusNew(pdl.getMorningApprovalStatus());
		pdl.setAfternoonApprovalStatusNew(pdl.getAfternoonApprovalStatus());
		if(pdl.getApprovalStatus() != null && ApprovalStatus.FORGOT.equals(ApprovalStatus.fromCode(pdl.getApprovalStatus())))
			pdl.setApprovalStatus(ApprovalStatus.UNPUNCH.getCode());
		if(pdl.getMorningApprovalStatus() != null && ApprovalStatus.FORGOT.equals(ApprovalStatus.fromCode(pdl.getMorningApprovalStatus())))
			pdl.setMorningApprovalStatus(ApprovalStatus.UNPUNCH.getCode());
		if(pdl.getAfternoonApprovalStatus() != null && ApprovalStatus.FORGOT.equals(ApprovalStatus.fromCode(pdl.getAfternoonApprovalStatus())))
			pdl.setAfternoonApprovalStatus(ApprovalStatus.UNPUNCH.getCode());
		punchProvider.viewDateFlags(userId, cmd.getEnterpirseId(),
				dateSF.format(logDay.getTime()));
		
		return pdl;
	}

	private void addPunchStatistics(OrganizationMemberDTO member ,Long orgId,Calendar startCalendar,Calendar endCalendar){
		
		PunchRule pr = this.getPunchRule(PunchOwnerType.ORGANIZATION.getCode(),orgId, member.getTargetId());
		if(null == pr)
			throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
					PunchServiceErrorCode.ERROR_ENTERPRISE_DIDNOT_SETTING,
					"have no punch rule");
		PunchTimeRule timeRule =  this.punchProvider.findPunchTimeRuleById(pr.getTimeRuleId());
		 
		PunchStatistic statistic = new PunchStatistic();
		statistic.setPunchMonth(monthSF.format(startCalendar.getTime()));
		statistic.setOwnerType(PunchOwnerType.ORGANIZATION.getCode());
		statistic.setOwnerId(orgId);
		statistic.setPunchTimesPerDay(timeRule.getPunchTimesPerDay());
		statistic.setUserId(member.getTargetId());
		Integer workDayCount = countWorkDayCount(startCalendar,endCalendar, pr);
		
		statistic.setUserName(member.getContactName());
		OrganizationDTO dept = this.findUserDepartment(member.getTargetId(), member.getOrganizationId());   
		statistic.setDeptId(dept.getId());
		statistic.setDeptName(dept.getName());
		statistic.setWorkDayCount(workDayCount);
		List<PunchDayLog> dayLogList = this.punchProvider.listPunchDayLogs(member.getTargetId(), orgId, dateSF.format(startCalendar.getTime()),
						dateSF.format(endCalendar.getTime()) );
		List<PunchStatisticsDTO> list = new ArrayList<PunchStatisticsDTO>();
		for(PunchDayLog dayLog : dayLogList){ 
			PunchStatisticsDTO dto = ConvertHelper.convert(dayLog,
					PunchStatisticsDTO.class);
			list.add(dto);
			processPunchStatisticsDTOTime(dto, dayLog);
			PunchExceptionApproval approval = punchProvider.getExceptionApproval(dto.getUserId(),
							dto.getEnterpriseId(),new java.sql.Date(dto.getPunchDate()));
			if (approval != null) {
				dto.setApprovalStatus(approval
						.getApprovalStatus());
				dto.setMorningApprovalStatus(approval.getMorningApprovalStatus());
				dto.setAfternoonApprovalStatus(approval.getAfternoonApprovalStatus()); 
				
			} else {
				//do nothing
//				dto.setApprovalStatus((byte) 0);
			}
			
		}
		if(statistic.getPunchTimesPerDay().equals(PunchTimesPerDay.TWICE.getCode())){
			processTwicePunchListCount(list, statistic);
		}
		else{
			processForthPunchListCount(list, statistic);
		} 
		this.punchProvider.deletePunchStatisticByUser(statistic.getOwnerType(),statistic.getOwnerId(),statistic.getPunchMonth(),statistic.getUserId());
		this.punchProvider.createPunchStatistic(statistic);
		
	}
	private void processForthPunchListCount(List<PunchStatisticsDTO> list,
			PunchStatistic statistic) { 
		statistic.setWorkCount(0.0);
		statistic.setUnpunchCount(0.0);
		statistic.setSickCount(0.0);
		statistic.setOutworkCount(0.0);
		statistic.setLeaveEarlyCount(0);
		statistic.setExchangeCount(0.0);
		statistic.setBlandleCount(0);
		statistic.setBelateCount(0);
		statistic.setAbsenceCount(0.0); 
		statistic.setOverTimeSum(0L); 
		statistic.setExceptionStatus(ExceptionStatus.NORMAL.getCode());
		for (PunchStatisticsDTO punchDayLogDTO : list) {
			if (punchDayLogDTO.getMorningApprovalStatus() != null) {
				//上午
				if(ApprovalStatus.UNPUNCH.getCode() == punchDayLogDTO.getMorningApprovalStatus()){
					statistic.setUnpunchCount(statistic.getUnpunchCount()+0.5);
					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
				}else if(ApprovalStatus.SICK.getCode() == punchDayLogDTO.getMorningApprovalStatus()){
					statistic.setSickCount(statistic.getSickCount()+0.5);
					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
				}else if(ApprovalStatus.HALFSICK.getCode() == punchDayLogDTO.getMorningApprovalStatus()){
					statistic.setSickCount(statistic.getSickCount()+0.5);
					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
				}else if(ApprovalStatus.OUTWORK.getCode() == punchDayLogDTO.getMorningApprovalStatus()){
					statistic.setOutworkCount(statistic.getOutworkCount()+0.5);
					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
				}else if(ApprovalStatus.HALFOUTWORK.getCode() == punchDayLogDTO.getMorningApprovalStatus()){
					statistic.setOutworkCount(statistic.getOutworkCount()+0.5);
					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
				}else if(ApprovalStatus.LEAVEEARLY.getCode() == punchDayLogDTO.getMorningApprovalStatus()){
					statistic.setLeaveEarlyCount(statistic.getLeaveEarlyCount()+1);
					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
				}else if(ApprovalStatus.EXCHANGE.getCode() == punchDayLogDTO.getMorningApprovalStatus()){
					statistic.setExchangeCount(statistic.getExchangeCount()+0.5);
					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
				}else if(ApprovalStatus.HALFEXCHANGE.getCode() == punchDayLogDTO.getMorningApprovalStatus()){
					statistic.setExchangeCount(statistic.getExchangeCount()+0.5);
					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
				}else if(ApprovalStatus.BLANDLE.getCode() == punchDayLogDTO.getMorningApprovalStatus()){
					statistic.setBlandleCount(statistic.getBlandleCount()+1);
					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
				}else if(ApprovalStatus.BELATE.getCode() == punchDayLogDTO.getMorningApprovalStatus()){
					statistic.setBelateCount(statistic.getBelateCount()+1);
					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
				}else if(ApprovalStatus.ABSENCE.getCode() == punchDayLogDTO.getMorningApprovalStatus()){
					statistic.setAbsenceCount(statistic.getAbsenceCount()+0.5);
					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
				}else if(ApprovalStatus.HALFABSENCE.getCode() == punchDayLogDTO.getMorningApprovalStatus()){
					statistic.setAbsenceCount(statistic.getAbsenceCount()+0.5);
					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
				}
				//下午
				
				if(ApprovalStatus.UNPUNCH.getCode() == punchDayLogDTO.getAfternoonApprovalStatus()){
					statistic.setUnpunchCount(statistic.getUnpunchCount()+0.5);
					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
				}else if(ApprovalStatus.SICK.getCode() == punchDayLogDTO.getAfternoonApprovalStatus()){
					statistic.setSickCount(statistic.getSickCount()+0.5);
					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
				}else if(ApprovalStatus.HALFSICK.getCode() == punchDayLogDTO.getAfternoonApprovalStatus()){
					statistic.setSickCount(statistic.getSickCount()+0.5);
					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
				}else if(ApprovalStatus.OUTWORK.getCode() == punchDayLogDTO.getAfternoonApprovalStatus()){
					statistic.setOutworkCount(statistic.getOutworkCount()+0.5);
					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
				}else if(ApprovalStatus.HALFOUTWORK.getCode() == punchDayLogDTO.getAfternoonApprovalStatus()){
					statistic.setOutworkCount(statistic.getOutworkCount()+0.5);
					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
				}else if(ApprovalStatus.LEAVEEARLY.getCode() == punchDayLogDTO.getAfternoonApprovalStatus()){
					statistic.setLeaveEarlyCount(statistic.getLeaveEarlyCount()+1);
					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
				}else if(ApprovalStatus.EXCHANGE.getCode() == punchDayLogDTO.getAfternoonApprovalStatus()){
					statistic.setExchangeCount(statistic.getExchangeCount()+0.5);
					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
				}else if(ApprovalStatus.HALFEXCHANGE.getCode() == punchDayLogDTO.getAfternoonApprovalStatus()){
					statistic.setExchangeCount(statistic.getExchangeCount()+0.5);
					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
				}else if(ApprovalStatus.BLANDLE.getCode() == punchDayLogDTO.getAfternoonApprovalStatus()){
					statistic.setBlandleCount(statistic.getBlandleCount()+1);
					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
				}else if(ApprovalStatus.BELATE.getCode() == punchDayLogDTO.getAfternoonApprovalStatus()){
					statistic.setBelateCount(statistic.getBelateCount()+1);
					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
				}else if(ApprovalStatus.ABSENCE.getCode() == punchDayLogDTO.getAfternoonApprovalStatus()){
					statistic.setAbsenceCount(statistic.getAbsenceCount()+0.5);
					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
				}else if(ApprovalStatus.HALFABSENCE.getCode() == punchDayLogDTO.getAfternoonApprovalStatus()){
					statistic.setAbsenceCount(statistic.getAbsenceCount()+0.5);
					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
				}
				//加班和正常
				if(ApprovalStatus.OVERTIME.getCode() == punchDayLogDTO.getAfternoonApprovalStatus()||ApprovalStatus.OVERTIME.getCode() == punchDayLogDTO.getMorningApprovalStatus()){
					statistic.setOverTimeSum(statistic.getOverTimeSum()+((punchDayLogDTO.getWorkTime()==null)?0L:punchDayLogDTO.getWorkTime()));
				}else  if(ApprovalStatus.NORMAL.getCode() == punchDayLogDTO.getAfternoonApprovalStatus()&&ApprovalStatus.NORMAL.getCode() == punchDayLogDTO.getMorningApprovalStatus()) {
					statistic.setWorkCount(statistic.getWorkCount()+1);
				}
			} else {
				//上午
				if(ApprovalStatus.UNPUNCH.getCode() == punchDayLogDTO.getMorningStatus()){
					statistic.setUnpunchCount(statistic.getUnpunchCount()+0.5);
					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
				}else if(ApprovalStatus.SICK.getCode() == punchDayLogDTO.getMorningStatus()){
					statistic.setSickCount(statistic.getSickCount()+0.5);
					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
				}else if(ApprovalStatus.HALFSICK.getCode() == punchDayLogDTO.getMorningStatus()){
					statistic.setSickCount(statistic.getSickCount()+0.5);
					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
				}else if(ApprovalStatus.OUTWORK.getCode() == punchDayLogDTO.getMorningStatus()){
					statistic.setOutworkCount(statistic.getOutworkCount()+0.5);
					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
				}else if(ApprovalStatus.HALFOUTWORK.getCode() == punchDayLogDTO.getMorningStatus()){
					statistic.setOutworkCount(statistic.getOutworkCount()+0.5);
					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
				}else if(ApprovalStatus.LEAVEEARLY.getCode() == punchDayLogDTO.getMorningStatus()){
					statistic.setLeaveEarlyCount(statistic.getLeaveEarlyCount()+1);
					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
				}else if(ApprovalStatus.EXCHANGE.getCode() == punchDayLogDTO.getMorningStatus()){
					statistic.setExchangeCount(statistic.getExchangeCount()+0.5);
					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
				}else if(ApprovalStatus.HALFEXCHANGE.getCode() == punchDayLogDTO.getMorningStatus()){
					statistic.setExchangeCount(statistic.getExchangeCount()+0.5);
					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
				}else if(ApprovalStatus.BLANDLE.getCode() == punchDayLogDTO.getMorningStatus()){
					statistic.setBlandleCount(statistic.getBlandleCount()+1);
					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
				}else if(ApprovalStatus.BELATE.getCode() == punchDayLogDTO.getMorningStatus()){
					statistic.setBelateCount(statistic.getBelateCount()+1);
					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
				}else if(ApprovalStatus.ABSENCE.getCode() == punchDayLogDTO.getMorningStatus()){
					statistic.setAbsenceCount(statistic.getAbsenceCount()+0.5);
					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
				}else if(ApprovalStatus.HALFABSENCE.getCode() == punchDayLogDTO.getMorningStatus()){
					statistic.setAbsenceCount(statistic.getAbsenceCount()+0.5);
					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
				}
				//下午
				
				if(ApprovalStatus.UNPUNCH.getCode() == punchDayLogDTO.getAfternoonStatus()){
					statistic.setUnpunchCount(statistic.getUnpunchCount()+0.5);
					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
				}else if(ApprovalStatus.SICK.getCode() == punchDayLogDTO.getAfternoonStatus()){
					statistic.setSickCount(statistic.getSickCount()+0.5);
					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
				}else if(ApprovalStatus.HALFSICK.getCode() == punchDayLogDTO.getAfternoonStatus()){
					statistic.setSickCount(statistic.getSickCount()+0.5);
					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
				}else if(ApprovalStatus.OUTWORK.getCode() == punchDayLogDTO.getAfternoonStatus()){
					statistic.setOutworkCount(statistic.getOutworkCount()+0.5);
					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
				}else if(ApprovalStatus.HALFOUTWORK.getCode() == punchDayLogDTO.getAfternoonStatus()){
					statistic.setOutworkCount(statistic.getOutworkCount()+0.5);
					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
				}else if(ApprovalStatus.LEAVEEARLY.getCode() == punchDayLogDTO.getAfternoonStatus()){
					statistic.setLeaveEarlyCount(statistic.getLeaveEarlyCount()+1);
					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
				}else if(ApprovalStatus.EXCHANGE.getCode() == punchDayLogDTO.getAfternoonStatus()){
					statistic.setExchangeCount(statistic.getExchangeCount()+0.5);
					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
				}else if(ApprovalStatus.HALFEXCHANGE.getCode() == punchDayLogDTO.getAfternoonStatus()){
					statistic.setExchangeCount(statistic.getExchangeCount()+0.5);
					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
				}else if(ApprovalStatus.BLANDLE.getCode() == punchDayLogDTO.getAfternoonStatus()){
					statistic.setBlandleCount(statistic.getBlandleCount()+1);
					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
				}else if(ApprovalStatus.BELATE.getCode() == punchDayLogDTO.getAfternoonStatus()){
					statistic.setBelateCount(statistic.getBelateCount()+1);
					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
				}else if(ApprovalStatus.ABSENCE.getCode() == punchDayLogDTO.getAfternoonStatus()){
					statistic.setAbsenceCount(statistic.getAbsenceCount()+0.5);
					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
				}else if(ApprovalStatus.HALFABSENCE.getCode() == punchDayLogDTO.getAfternoonStatus()){
					statistic.setAbsenceCount(statistic.getAbsenceCount()+0.5);
					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
				}
				//加班和正常
				if(ApprovalStatus.OVERTIME.getCode() == punchDayLogDTO.getAfternoonStatus()||ApprovalStatus.OVERTIME.getCode() == punchDayLogDTO.getMorningStatus()){
					statistic.setOverTimeSum(statistic.getOverTimeSum()+
							((punchDayLogDTO.getWorkTime()==null)?0L:punchDayLogDTO.getWorkTime()));
				}else  if(ApprovalStatus.NORMAL.getCode() == punchDayLogDTO.getAfternoonStatus()&&ApprovalStatus.NORMAL.getCode() == punchDayLogDTO.getMorningStatus()) {
					statistic.setWorkCount(statistic.getWorkCount()+1);
				} 

				//应上班天数－缺勤天数－事假天数-病假天数-调休天数-公出天数-调休天数
				statistic.setWorkCount(statistic.getWorkDayCount()-statistic.getUnpunchCount()-statistic.getAbsenceCount()
						-statistic.getSickCount()-statistic.getOutworkCount()-statistic.getExchangeCount());
			}
		}
	}
	private void processTwicePunchListCount(List<PunchStatisticsDTO> list, PunchStatistic statistic) {
		statistic.setWorkCount(0.0);
		statistic.setUnpunchCount(0.0);
		statistic.setSickCount(0.0);
		statistic.setOutworkCount(0.0);
		statistic.setLeaveEarlyCount(0);
		statistic.setExchangeCount(0.0);
		statistic.setBlandleCount(0);
		statistic.setBelateCount(0);
		statistic.setAbsenceCount(0.0); 
		statistic.setOverTimeSum(0L); 
		statistic.setExceptionStatus(ExceptionStatus.NORMAL.getCode());
		for (PunchStatisticsDTO punchDayLogDTO : list) {
			if (punchDayLogDTO.getApprovalStatus() != null) {
				if(ApprovalStatus.UNPUNCH.getCode() == punchDayLogDTO.getApprovalStatus()){
					statistic.setUnpunchCount(statistic.getUnpunchCount()+1);
					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
				}else if(ApprovalStatus.SICK.getCode() == punchDayLogDTO.getApprovalStatus()){
					statistic.setSickCount(statistic.getSickCount()+1);
					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
				}else if(ApprovalStatus.HALFSICK.getCode() == punchDayLogDTO.getApprovalStatus()){
					statistic.setSickCount(statistic.getSickCount()+0.5);
					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
				}else if(ApprovalStatus.OUTWORK.getCode() == punchDayLogDTO.getApprovalStatus()){
					statistic.setOutworkCount(statistic.getOutworkCount()+1);
					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
				}else if(ApprovalStatus.HALFOUTWORK.getCode() == punchDayLogDTO.getApprovalStatus()){
					statistic.setOutworkCount(statistic.getOutworkCount()+0.5);
					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
				}else if(ApprovalStatus.LEAVEEARLY.getCode() == punchDayLogDTO.getApprovalStatus()){
					statistic.setLeaveEarlyCount(statistic.getLeaveEarlyCount()+1);
					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
				}else if(ApprovalStatus.EXCHANGE.getCode() == punchDayLogDTO.getApprovalStatus()){
					statistic.setExchangeCount(statistic.getExchangeCount()+1);
					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
				}else if(ApprovalStatus.HALFEXCHANGE.getCode() == punchDayLogDTO.getApprovalStatus()){
					statistic.setExchangeCount(statistic.getExchangeCount()+0.5);
					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
				}else if(ApprovalStatus.BLANDLE.getCode() == punchDayLogDTO.getApprovalStatus()){
					statistic.setBlandleCount(statistic.getBlandleCount()+1);
					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
				}else if(ApprovalStatus.BELATE.getCode() == punchDayLogDTO.getApprovalStatus()){
					statistic.setBelateCount(statistic.getBelateCount()+1);
					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
				}else if(ApprovalStatus.ABSENCE.getCode() == punchDayLogDTO.getApprovalStatus()){
					statistic.setAbsenceCount(statistic.getAbsenceCount()+1);
					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
				}else if(ApprovalStatus.HALFABSENCE.getCode() == punchDayLogDTO.getApprovalStatus()){
					statistic.setAbsenceCount(statistic.getAbsenceCount()+0.5);
					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
				}else if(ApprovalStatus.OVERTIME.getCode() == punchDayLogDTO.getApprovalStatus()){
					statistic.setOverTimeSum(statistic.getOverTimeSum()+ punchDayLogDTO.getWorkTime());
				} 
			} else {
				if(ApprovalStatus.UNPUNCH.getCode() == punchDayLogDTO.getStatus()){
					statistic.setUnpunchCount(statistic.getUnpunchCount()+1);
					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
				}else if(ApprovalStatus.SICK.getCode() == punchDayLogDTO.getStatus()){
					statistic.setSickCount(statistic.getSickCount()+1);
					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
				}else if(ApprovalStatus.HALFSICK.getCode() == punchDayLogDTO.getStatus()){
					statistic.setSickCount(statistic.getSickCount()+0.5);
					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
				}else if(ApprovalStatus.OUTWORK.getCode() == punchDayLogDTO.getStatus()){
					statistic.setOutworkCount(statistic.getOutworkCount()+1);
					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
				}else if(ApprovalStatus.HALFOUTWORK.getCode() == punchDayLogDTO.getStatus()){
					statistic.setOutworkCount(statistic.getOutworkCount()+0.5);
					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
				}else if(ApprovalStatus.LEAVEEARLY.getCode() == punchDayLogDTO.getStatus()){
					statistic.setLeaveEarlyCount(statistic.getLeaveEarlyCount()+1);
					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
				}else if(ApprovalStatus.EXCHANGE.getCode() == punchDayLogDTO.getStatus()){
					statistic.setExchangeCount(statistic.getExchangeCount()+1);
					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
				}else if(ApprovalStatus.HALFEXCHANGE.getCode() == punchDayLogDTO.getStatus()){
					statistic.setExchangeCount(statistic.getExchangeCount()+0.5);
					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
				}else if(ApprovalStatus.BLANDLE.getCode() == punchDayLogDTO.getStatus()){
					statistic.setBlandleCount(statistic.getBlandleCount()+1);
					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
				}else if(ApprovalStatus.BELATE.getCode() == punchDayLogDTO.getStatus()){
					statistic.setBelateCount(statistic.getBelateCount()+1);
					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
				}else if(ApprovalStatus.ABSENCE.getCode() == punchDayLogDTO.getStatus()){
					statistic.setAbsenceCount(statistic.getAbsenceCount()+1);
					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
				}else if(ApprovalStatus.HALFABSENCE.getCode() == punchDayLogDTO.getStatus()){
					statistic.setAbsenceCount(statistic.getAbsenceCount()+0.5);
					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
				}else if(ApprovalStatus.OVERTIME.getCode() == punchDayLogDTO.getStatus()){
					statistic.setOverTimeSum(statistic.getOverTimeSum()+ punchDayLogDTO.getWorkTime());
				} 
			}
		}
		//应上班天数－缺勤天数－事假天数-病假天数-调休天数-公出天数-调休天数
		statistic.setWorkCount(statistic.getWorkDayCount()-statistic.getUnpunchCount()-statistic.getAbsenceCount()
				-statistic.getSickCount()-statistic.getOutworkCount()-statistic.getExchangeCount());
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
	
	private void createPunchStatisticsBookSheetHead(Sheet sheet ){
		Row row = sheet.createRow(sheet.getLastRowNum());
		int i =-1 ;
		 
		row.createCell(++i).setCellValue("姓名");
		row.createCell(++i).setCellValue("部门");
		row.createCell(++i).setCellValue("应上班天数");
		row.createCell(++i).setCellValue("实际上班天数");
		row.createCell(++i).setCellValue("缺勤天数");
		row.createCell(++i).setCellValue("迟到次数");
		row.createCell(++i).setCellValue("早退次数");
		row.createCell(++i).setCellValue("迟到且早退次数");
		row.createCell(++i).setCellValue("事假天数");
		row.createCell(++i).setCellValue("病假天数");
		row.createCell(++i).setCellValue("调休天数");
		row.createCell(++i).setCellValue("公出天数");
		row.createCell(++i).setCellValue("加班小时数"); 
	   
	}
	
	public void setNewPunchStatisticsBookRow(Sheet sheet ,PunchStatistic statistic){
		Row row = sheet.createRow(sheet.getLastRowNum()+1);
		int i = -1; 
		row.createCell(++i).setCellValue(statistic.getUserName());
		row.createCell(++i).setCellValue(statistic.getDeptName());
		row.createCell(++i).setCellValue(statistic.getWorkDayCount());
		row.createCell(++i).setCellValue(statistic.getWorkCount());
		row.createCell(++i).setCellValue(statistic.getUnpunchCount());
		row.createCell(++i).setCellValue(statistic.getBelateCount());
		row.createCell(++i).setCellValue(statistic.getLeaveEarlyCount());
		row.createCell(++i).setCellValue(statistic.getBlandleCount()); 
		row.createCell(++i).setCellValue(statistic.getAbsenceCount()); 
		row.createCell(++i).setCellValue(statistic.getSickCount()); 
		row.createCell(++i).setCellValue(statistic.getExchangeCount()); 
		row.createCell(++i).setCellValue(statistic.getOutworkCount()); 
		if(statistic.getOverTimeSum().equals(0L)){
			row.createCell(++i).setCellValue(0);
		}
		else{
			BigDecimal b = new BigDecimal(statistic.getOverTimeSum()/3600000.0); 
			row.createCell(++i).setCellValue(b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());  
		}
			 
	}
	
	public void createPunchStatisticsBook(String path,List<PunchStatistic> results) {
		if (null == results || results.size() == 0)
			return;
		Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet("punchStatistics");
		
		this.createPunchStatisticsBookSheetHead(sheet );
		for (PunchStatistic statistic : results )
			this.setNewPunchStatisticsBookRow(sheet, statistic);
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

//	@Override
//	public HttpServletResponse exportPunchStatistics(
//			ListPunchDetailsCommand cmd ,HttpServletResponse response 
//			) { 
//		return null;
//		checkCompanyIdIsNull(cmd.getOwnerId());
//		
//		List<OrganizationMember> members = organizationProvider.listOrganizationMembersByOrgId(cmd.getEnterpriseId());
//		
//		List<Long> userIds = new ArrayList<Long>();
//		for (OrganizationMember member : members) {
//			if(OrganizationMemberTargetType.fromCode(member.getTargetType()) == OrganizationMemberTargetType.USER){
//				userIds.add(member.getTargetId());
//			}
//		
//		}
//
//		
//		List<PunchDayLog> result = punchProvider.listPunchDayLogs(userIds,
//				cmd.getOwnerId(), cmd.getStartDay(), cmd.getEndDay(),
//				cmd.getExceptionStatus(), cmd.getArriveTimeCompareFlag(),
//				cmd.getArriveTime(), cmd.getLeaveTimeCompareFlag(),
//				cmd.getLeaveTime(), cmd.getWorkTimeCompareFlag(),
//				cmd.getWorkTime(), null, Integer.MAX_VALUE);
//		if (null == result || result.size() ==0 )
//			return null;
//		
//		Organization organization = organizationProvider.findOrganizationById(cmd.getEnterpriseId());
//		List<String> groupTypes = new ArrayList<String>();
//		groupTypes.add(OrganizationGroupType.DEPARTMENT.getCode());
//		List<Organization> departments = organizationProvider.listOrganizationByGroupTypes(organization.getPath() + "/%", groupTypes);
//		Map<Long, Organization> deptMap = this.convertDeptListToMap(departments);
//		
//		List<PunchStatisticsDTO> dtos = result
//				.stream()
//				.map(r -> {
//					PunchStatisticsDTO dto = ConvertHelper.convert(r,
//							PunchStatisticsDTO.class);
//					processPunchStatisticsDTOTime(dto, r);
//					if (dto != null) {
//						OrganizationMember member = organizationProvider.findOrganizationMemberByOrgIdAndUId(dto.getUserId(), cmd.getOwnerId());
//						if (null != member) {
//
//							dto.setUserName(member.getContactName());
//							dto.setUserPhoneNumber(member.getContactToken());
//							Organization department = deptMap.get(member.getGroupId());
//							if(null != department){
//								dto.setUserDepartment(department.getName());
//							}
//							PunchExceptionApproval approval = punchProvider
//									.getExceptionApproval(dto.getUserId(),
//											dto.getEnterpriseId(),
//											new java.sql.Date(dto.getPunchDate()));
//							if (approval != null) {
//								dto.setApprovalStatus(approval
//										.getApprovalStatus());
//								dto.setMorningApprovalStatus(approval.getMorningApprovalStatus());
//								dto.setAfternoonApprovalStatus(approval.getAfternoonApprovalStatus());
//								OrganizationMember operaor = organizationProvider.findOrganizationMemberByOrgIdAndUId(approval.getOperatorUid(), cmd.getEnterpriseId());
//								dto.setOperatorName(operaor.getContactName());
//							} else {
//								dto.setApprovalStatus((byte) 0);
//							}
//						}
//					}
//					return dto;
//				}).collect(Collectors.toList());
//		
//		URL rootPath = PunchServiceImpl.class.getResource("/");
//		String filePath =rootPath.getPath() + this.downloadDir ;
//		File file = new File(filePath);
//		if(!file.exists())
//			file.mkdirs();
//		filePath = filePath + "PunchStatistics"+System.currentTimeMillis()+".xlsx";
//		//新建了一个文件
//		this.createPunchStatisticsBook(filePath, dtos);
//		
//		return download(filePath,response);
//	}
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
	@Override
	public void addPunchTimeRule(AddPunchTimeRuleCommand cmd) {
		Long userId = UserContext.current().getUser().getId();
		if (null == cmd.getOwnerId() ||null == cmd.getOwnerType()) {
			LOGGER.error("Invalid owner type or  Id parameter in the command");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid owner type or  Id parameter in the command");
		}
		if (null == cmd.getPunchTimesPerDay() ) {
			LOGGER.error("Invalid PunchTimesPerDay parameter in the command");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid PunchTimesPerDay parameter in the command");
		}
		List<PunchTimeRule> punchTimeRules = punchProvider.queryPunchTimeRules(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getName()) ; 
		if(null!=punchTimeRules ){
			LOGGER.error("Invalid name parameter in the command");
			throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,PunchServiceErrorCode.ERROR_NAME_REPEAT,
					"name repeat");
		}
		else{
			PunchTimeRule punchTimeRule = ConvertHelper.convert(cmd, PunchTimeRule.class);
 			punchTimeRule.setAfternoonArriveTime(convertTime(cmd.getAfternoonArriveTime()));
			punchTimeRule.setPunchTimesPerDay(cmd.getPunchTimesPerDay());
			punchTimeRule.setNoonLeaveTime(convertTime(cmd.getNoonLeaveTime())); 
			punchTimeRule.setDaySplitTime(convertTime(cmd.getDaySplitTime()));
			convertTime(punchTimeRule, cmd.getStartEarlyTime(), cmd.getStartLateTime(), cmd.getEndEarlyTime());
			punchTimeRule.setCreatorUid(userId);
			punchTimeRule.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
					.getTime()));
			punchProvider.createPunchTimeRule(punchTimeRule);
			 
		}
	}
	@Override
	public void updatePunchTimeRule(UpdatePunchTimeRuleCommand cmd) {
		Long userId = UserContext.current().getUser().getId();
		if (null == cmd.getId()  ) {
			LOGGER.error("Invalid   Id parameter in the command");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid  Id parameter in the command");
		}
		if (null == cmd.getOwnerId() ||null == cmd.getOwnerType()) {
			LOGGER.error("Invalid owner type or  Id parameter in the command");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid owner type or  Id parameter in the command");
		}
		if (null == cmd.getPunchTimesPerDay() ) {
			LOGGER.error("Invalid PunchTimesPerDay parameter in the command");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid PunchTimesPerDay parameter in the command");
		}

		List<PunchTimeRule> punchTimeRules = punchProvider.queryPunchTimeRules(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getName()) ; 
		if(null!=punchTimeRules && (punchTimeRules.size()>1 || !punchTimeRules.get(0).getId().equals(cmd.getId())) ){
			//有两个同名rules(正常业务不可能) 或者 同名rule的id不等于修改的id 则重名错误
			LOGGER.error("Invalid name parameter in the command");
			throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,PunchServiceErrorCode.ERROR_NAME_REPEAT,
					"name repeat");
			
		}
		PunchTimeRule punchTimeRule = punchProvider.getPunchTimeRuleById(cmd.getId()) ; 
		if(null ==punchTimeRule ){
			LOGGER.error("Invalid   Id parameter in the command");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid  Id parameter in the command:can not found rule");
		}
		else{
			punchTimeRule.setName(cmd.getName()); 
 			punchTimeRule.setAfternoonArriveTime(convertTime(cmd.getAfternoonArriveTime()));
			punchTimeRule.setPunchTimesPerDay(cmd.getPunchTimesPerDay());
			punchTimeRule.setNoonLeaveTime(convertTime(cmd.getNoonLeaveTime())); 
			punchTimeRule.setDaySplitTime(convertTime(cmd.getDaySplitTime()));
			convertTime(punchTimeRule, cmd.getStartEarlyTime(), cmd.getStartLateTime(), cmd.getEndEarlyTime());
			punchTimeRule.setOperatorUid(userId);
			punchTimeRule.setOperateTime(new Timestamp(DateHelper.currentGMTTime()
					.getTime()));
			punchProvider.updatePunchTimeRule(punchTimeRule);
			 
		}
	}
	@Override
	public void deletePunchTimeRule(DeleteCommonCommand cmd) { 
		List<PunchRule> punchRules = this.punchProvider.findPunchRules(null, null, cmd.getId(), null, null, null);
		if(null == punchRules){
			//验证是否有这个权限删除这个owner的rule
			
			//根据id和ownerid删除一条rule
			this.punchProvider.deletePunchTimeRuleByOwnerAndId(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getId()); 
		}
		else{
			//被使用的不删,报错
			throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
 					PunchServiceErrorCode.ERROR_RULE_USING,
 					"delete rule is using ");
			}
	}
	@Override
	public listPunchTimeRuleListResponse listPunchTimeRuleList(ListPunchRulesCommonCommand cmd) {
		listPunchTimeRuleListResponse response =new listPunchTimeRuleListResponse();
		if (cmd.getPageAnchor() == null)
			cmd.setPageAnchor(0L);
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());
		 
		List<PunchTimeRule> results = this.punchProvider.queryPunchTimeRuleList(cmd.getOwnerType(),cmd.getOwnerId(), locator, pageSize + 1 );
		if (null == results)
			return response;
		Long nextPageAnchor = null;
		if (results != null && results.size() > pageSize) {
			results.remove(results.size() - 1);
			nextPageAnchor = results.get(results.size() - 1).getId();
		}
		response.setNextPageAnchor(nextPageAnchor);
		response.setTimeRules(new ArrayList<PunchTimeRuleDTO>());
		results.forEach((other) -> {
			PunchTimeRuleDTO dto = ConvertHelper.convert(other, PunchTimeRuleDTO.class); 
			dto.setAfternoonArriveTime(convertTimeToGMTMillisecond(other.getAfternoonArriveTime()));
			dto.setNoonLeaveTime(convertTimeToGMTMillisecond(other.getNoonLeaveTime()));
			dto.setStartEarlyTime(convertTimeToGMTMillisecond(other.getStartEarlyTime()));
			dto.setStartLateTime(convertTimeToGMTMillisecond(other.getStartLateTime()));
			dto.setEndEarlyTime(convertTimeToGMTMillisecond(other.getStartEarlyTime()) + convertTimeToGMTMillisecond(other.getWorkTime()));
			dto.setDaySplitTime(convertTimeToGMTMillisecond(other.getDaySplitTime()));
			response.getTimeRules().add(dto);
		});
		return response;
	}
	@Override
	public void addPunchLocationRule(PunchLocationRuleDTO cmd) {
		 
		Long userId = UserContext.current().getUser().getId();
		if (null == cmd.getOwnerId() ||null == cmd.getOwnerType()) {
			LOGGER.error("Invalid owner type or  Id parameter in the command");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid owner type or  Id parameter in the command");
		}
		if (null == cmd.getName() ) {
			LOGGER.error("Invalid name parameter in the command");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid name parameter in the command");
		}
		List<PunchLocationRule> punchLocationRules = punchProvider.queryPunchLocationRulesByName(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getName()) ; 
		if(null!=punchLocationRules ){
			LOGGER.error("Invalid name parameter in the command");
			throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,PunchServiceErrorCode.ERROR_NAME_REPEAT,
					"name repeat");
			
		}
		else{
			this.dbProvider.execute((TransactionStatus status) -> {
				PunchLocationRule obj = ConvertHelper.convert(cmd, PunchLocationRule.class);
				obj.setCreatorUid(userId);
				obj.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
						.getTime()));
				this.punchProvider.createPunchLocationRule(obj); 
				if(null == cmd.getPunchGeoPoints())
					return null;
				for (PunchGeoPointDTO punchGeopointDTO : cmd.getPunchGeoPoints()) {
					PunchGeopoint punchGeopoint = ConvertHelper.convert(punchGeopointDTO, PunchGeopoint.class);
					punchGeopoint.setOwnerType(cmd.getOwnerType());
					punchGeopoint.setOwnerId(cmd.getOwnerId()); 
					punchGeopoint.setLocationRuleId(obj.getId());
					punchGeopoint.setCreatorUid(userId);
					punchGeopoint.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
					punchGeopoint.setGeohash(GeoHashUtils.encode(
							punchGeopoint.getLatitude(), punchGeopoint.getLongitude()));
					punchProvider.createPunchGeopoint(punchGeopoint);
				}
				return null;
			});
		}
	}
	@Override
	public void updatePunchLocationRule(PunchLocationRuleDTO cmd) {
		Long userId = UserContext.current().getUser().getId();
		if (null == cmd.getId()  ) {
			LOGGER.error("Invalid   Id parameter in the command");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid  Id parameter in the command");
		}
		if (null == cmd.getOwnerId() ||null == cmd.getOwnerType()) {
			LOGGER.error("Invalid owner type or  Id parameter in the command");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid owner type or  Id parameter in the command");
		}
		if (null == cmd.getName() ) {
			LOGGER.error("Invalid name parameter in the command");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid name parameter in the command");
		}

		List<PunchLocationRule> punchLocationRules = punchProvider.queryPunchLocationRulesByName(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getName()) ;  
		if(null!=punchLocationRules && (punchLocationRules.size()>1 || !punchLocationRules.get(0).getId().equals(cmd.getId())) ){
			//有两个同名rules(正常业务不可能) 或者 同名rule的id不等于修改的id 则重名错误
			LOGGER.error("Invalid name parameter in the command");
			throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,PunchServiceErrorCode.ERROR_NAME_REPEAT,
					"name repeat");
			
		}
		PunchLocationRule obj = this.punchProvider.getPunchLocationRuleById(cmd.getId());
		if(null == obj ){
			LOGGER.error("Invalid   Id parameter in the command");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid  Id parameter in the command:can not found rule");
		}else {
			this.dbProvider.execute((TransactionStatus status) -> {
				obj.setDescription(cmd.getDescription());
				obj.setName(cmd.getName());
				obj.setOwnerType(cmd.getOwnerType());
				obj.setOwnerId(cmd.getOwnerId()); 
				this.punchProvider.updatePunchLocationRule(obj); 
				this.punchProvider.deletePunchGeopointsByRuleId( cmd.getId());
				if(null == cmd.getPunchGeoPoints())
					return null;
				for (PunchGeoPointDTO punchGeopointDTO : cmd.getPunchGeoPoints()) {
					PunchGeopoint punchGeopoint = ConvertHelper.convert(punchGeopointDTO, PunchGeopoint.class);
					punchGeopoint.setOwnerType(cmd.getOwnerType());
					punchGeopoint.setOwnerId(cmd.getOwnerId()); 
					punchGeopoint.setLocationRuleId(obj.getId());
					punchGeopoint.setCreatorUid(userId);
					punchGeopoint.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
					punchGeopoint.setGeohash(GeoHashUtils.encode(
							punchGeopoint.getLatitude(), punchGeopoint.getLongitude()));
					punchProvider.createPunchGeopoint(punchGeopoint);
				}
				return null;
			});
		}
	}
	@Override
	public void deletePunchLocationRule(DeleteCommonCommand cmd) {
		List<PunchRule> punchRules = this.punchProvider.findPunchRules(null, null, null, cmd.getId(), null, null);
		if(null == punchRules){
			//验证是否有这个权限删除这个owner的rule
			
			

			this.dbProvider.execute((TransactionStatus status) -> {
				//根据id和ownerid删除一条rule
				this.punchProvider.deletePunchLocationRuleByOwnerAndId(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getId());  
				this.punchProvider.deletePunchGeopointsByRuleId( cmd.getId());;
				 
				return null;
			});
		}
		else{
			//被使用的不删,报错
			throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
 					PunchServiceErrorCode.ERROR_RULE_USING,
 					"delete rule is using ");
			}
	}
	@Override
	public QryPunchLocationRuleListResponse listPunchLocationRules(ListPunchRulesCommonCommand cmd) { 
		QryPunchLocationRuleListResponse response = new QryPunchLocationRuleListResponse();
		if (cmd.getPageAnchor() == null)
			cmd.setPageAnchor(0L);
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());
		 
		List<PunchLocationRule> results = this.punchProvider.queryPunchLocationRuleList(cmd.getOwnerType(),cmd.getOwnerId(), locator, pageSize + 1 );
		if (null == results)
			return response;
		Long nextPageAnchor = null;
		if (results != null && results.size() > pageSize) {
			results.remove(results.size() - 1);
			nextPageAnchor = results.get(results.size() - 1).getId();
		}
		response.setNextPageAnchor(nextPageAnchor);
		response.setLocationRules(new ArrayList<PunchLocationRuleDTO>());
		results.forEach((other) -> {
			PunchLocationRuleDTO dto = ConvertHelper.convert(other, PunchLocationRuleDTO.class); 
			response.getLocationRules().add(dto);
			List<PunchGeopoint> geos = this.punchProvider.listPunchGeopointsByRuleId(other.getOwnerType(), other.getOwnerId(), other.getId()) ;
			if(null != geos){
				dto.setPunchGeoPoints(new ArrayList<PunchGeoPointDTO>());
				for(PunchGeopoint geo : geos){
					PunchGeoPointDTO geoDTO = ConvertHelper.convert(geo, PunchGeoPointDTO.class);
					dto.getPunchGeoPoints().add(geoDTO);
				} 
			}
		});
		return response;
	}
	@Override
	public void addPunchWiFiRule(PunchWiFiRuleDTO cmd) {

		Long userId = UserContext.current().getUser().getId();
		if (null == cmd.getOwnerId() ||null == cmd.getOwnerType()) {
			LOGGER.error("Invalid owner type or  Id parameter in the command");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid owner type or  Id parameter in the command");
		}
		if (null == cmd.getName() ) {
			LOGGER.error("Invalid name parameter in the command");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid name parameter in the command");
		}
		List<PunchWifiRule> punchWiFiRules = punchProvider.queryPunchWiFiRulesByName(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getName()) ; 
		if(null!=punchWiFiRules ){
			LOGGER.error("Invalid name parameter in the command");
			throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,PunchServiceErrorCode.ERROR_NAME_REPEAT,
					"name repeat");
			
		}
		else{
			this.dbProvider.execute((TransactionStatus status) -> {
				PunchWifiRule obj = ConvertHelper.convert(cmd, PunchWifiRule.class);
				obj.setCreatorUid(userId);
				obj.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
						.getTime()));
				this.punchProvider.createPunchWifiRule(obj); 
				if(null == cmd.getWifis())
					return null;
				for (PunchWiFiDTO dto : cmd.getWifis()) {
					PunchWifi punchWifi = ConvertHelper.convert(dto, PunchWifi.class);
					punchWifi.setOwnerType(cmd.getOwnerType());
					punchWifi.setOwnerId(cmd.getOwnerId());  
					punchWifi.setCreatorUid(userId);
					punchWifi.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
					punchWifi.setWifiRuleId(obj.getId());
					punchProvider.createPunchWifi(punchWifi);
				}
				return null;
			});
		}
	}
	@Override
	public void updatePunchWiFiRule(PunchWiFiRuleDTO cmd) {
		Long userId = UserContext.current().getUser().getId();
		if (null == cmd.getId()  ) {
			LOGGER.error("Invalid   Id parameter in the command");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid  Id parameter in the command");
		}
		if (null == cmd.getOwnerId() ||null == cmd.getOwnerType()) {
			LOGGER.error("Invalid owner type or  Id parameter in the command");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid owner type or  Id parameter in the command");
		}
		if (null == cmd.getName() ) {
			LOGGER.error("Invalid name parameter in the command");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid name parameter in the command");
		}
		List<PunchWifiRule> punchWiFiRules = punchProvider.queryPunchWiFiRulesByName(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getName()) ;  
		if(null!=punchWiFiRules && (punchWiFiRules.size()>1 || !punchWiFiRules.get(0).getId().equals(cmd.getId())) ){
			//有两个同名rules(正常业务不可能) 或者 同名rule的id不等于修改的id 则重名错误
			LOGGER.error("Invalid name parameter in the command");
			throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,PunchServiceErrorCode.ERROR_NAME_REPEAT,
					"name repeat");
			
		}
		PunchWifiRule obj = this.punchProvider.getPunchWifiRuleById(cmd.getId());
		if(null == obj ){
			LOGGER.error("Invalid   Id parameter in the command");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid  Id parameter in the command:can not found rule");
		}else {
			this.dbProvider.execute((TransactionStatus status) -> {
				obj.setDescription(cmd.getDescription());
				obj.setName(cmd.getName());
				obj.setOwnerType(cmd.getOwnerType());
				obj.setOwnerId(cmd.getOwnerId()); 
				this.punchProvider.updatePunchWifiRule(obj); 
				this.punchProvider.deletePunchWifisByRuleId( cmd.getId());
				if(null == cmd.getWifis())
					return null;
				for (PunchWiFiDTO dto : cmd.getWifis()) {
					PunchWifi punchWifi = ConvertHelper.convert(dto, PunchWifi.class);
					punchWifi.setOwnerType(cmd.getOwnerType());
					punchWifi.setOwnerId(cmd.getOwnerId());  
					punchWifi.setCreatorUid(userId);
					punchWifi.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
					punchWifi.setWifiRuleId(obj.getId());
					punchProvider.createPunchWifi(punchWifi);
				}
				return null;
			});
		}
	}
	@Override
	public void deletePunchWiFiRule(DeleteCommonCommand cmd) { 
		List<PunchRule> punchRules = this.punchProvider.findPunchRules(null, null, null, null, cmd.getId(), null);
		if(null == punchRules){
			//验证是否有这个权限删除这个owner的rule
			
			

			this.dbProvider.execute((TransactionStatus status) -> {
				//根据id和ownerid删除一条rule
				this.punchProvider.deletePunchWifiRuleByOwnerAndId(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getId());  
				this.punchProvider.deletePunchWifisByRuleId( cmd.getId());;
				 
				return null;
			});
		}
		else{
			//被使用的不删,报错
			throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
 					PunchServiceErrorCode.ERROR_RULE_USING,
 					"delete rule is using ");
			}

	}
	@Override
	public ListPunchWiFiRuleListResponse listPunchWiFiRule(ListPunchRulesCommonCommand cmd) {
		ListPunchWiFiRuleListResponse response = new ListPunchWiFiRuleListResponse();
		if (cmd.getPageAnchor() == null)
			cmd.setPageAnchor(0L);
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());
		 
		List<PunchWifiRule> results = this.punchProvider.queryPunchWifiRuleList(cmd.getOwnerType(),cmd.getOwnerId(), locator, pageSize + 1 );
		if (null == results)
			return response;
		Long nextPageAnchor = null;
		if (results != null && results.size() > pageSize) {
			results.remove(results.size() - 1);
			nextPageAnchor = results.get(results.size() - 1).getId();
		}
		response.setNextPageAnchor(nextPageAnchor);
		response.setWifiRules(new ArrayList<PunchWiFiRuleDTO>()); 
		results.forEach((other) -> {
			PunchWiFiRuleDTO dto = ConvertHelper.convert(other, PunchWiFiRuleDTO.class); 
			response.getWifiRules().add(dto);
			List<PunchWifi> wifis = this.punchProvider.listPunchWifisByRuleId(other.getOwnerType(), other.getOwnerId(), other.getId()) ;
			if(null != wifis){
				dto.setWifis(new ArrayList<PunchWiFiDTO>()); 
				for(PunchWifi geo : wifis){
					PunchWiFiDTO wifiDTO = ConvertHelper.convert(geo, PunchWiFiDTO.class);
					dto.getWifis().add(wifiDTO);
				} 
			}
		});
		return response;
	}
	@Override
	public void addPunchWorkdayRule(PunchWorkdayRuleDTO cmd) {

		Long userId = UserContext.current().getUser().getId();
		if (null == cmd.getOwnerId() ||null == cmd.getOwnerType()) {
			LOGGER.error("Invalid owner type or  Id parameter in the command");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid owner type or  Id parameter in the command");
		}
		if (null == cmd.getName() ) {
			LOGGER.error("Invalid name parameter in the command");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid name parameter in the command");
		}
		List<PunchWorkdayRule> punchWorkdayRules = punchProvider.queryPunchWorkdayRulesByName(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getName()) ; 
		if(null!=punchWorkdayRules ){
			LOGGER.error("Invalid name parameter in the command");
			throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,PunchServiceErrorCode.ERROR_NAME_REPEAT,
					"name repeat");
			
		}
		else{
			this.dbProvider.execute((TransactionStatus status) -> {
				PunchWorkdayRule obj = converDTO2WorkdayRule(cmd);
				
				this.punchProvider.createPunchWorkdayRule(obj); 
				if(null != cmd.getWorkdays()) {
					for (Long date : cmd.getWorkdays()) {
						PunchHoliday holiday = new PunchHoliday();
						holiday.setOwnerType(cmd.getOwnerType());
						holiday.setOwnerId(cmd.getOwnerId());  
						holiday.setCreatorUid(userId);
						holiday.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
						holiday.setWorkdayRuleId(obj.getId());
						holiday.setRuleDate(new java.sql.Date(date));
						holiday.setStatus(DateStatus.WORKDAY.getCode());
						this.punchProvider.createPunchHoliday(holiday);
								
					}
				}
				if(null != cmd.getHolidays()) {
					for (Long date : cmd.getHolidays()) {
						PunchHoliday holiday = new PunchHoliday();
						holiday.setOwnerType(cmd.getOwnerType());
						holiday.setOwnerId(cmd.getOwnerId());  
						holiday.setCreatorUid(userId);
						holiday.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
						holiday.setWorkdayRuleId(obj.getId());
						holiday.setRuleDate(new java.sql.Date(date));
						holiday.setStatus(DateStatus.HOLIDAY.getCode());
						this.punchProvider.createPunchHoliday(holiday);
					}
				}
				return null;
			});
		}
	}
	private PunchWorkdayRule converDTO2WorkdayRule(PunchWorkdayRuleDTO dto) {
		PunchWorkdayRule obj = ConvertHelper.convert(dto, PunchWorkdayRule.class);
		if(null==dto.getWorkWeekDates()){
			obj.setWorkWeekDates("0000000");
		}else{
			int openWorkdayInt=0;
			for(Integer weekdayInteger : dto.getWorkWeekDates())
				openWorkdayInt+=Math.pow(10,weekdayInteger);
			String openWorkday=String.valueOf(openWorkdayInt);
			for( ;openWorkday.length()<7; ){
				openWorkday ="0"+openWorkday;
			}
			obj.setWorkWeekDates(openWorkday);
		}
		obj.setCreatorUid(UserContext.current().getUser().getId());
		obj.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
				.getTime()));
		
		return obj;
	}
	@Override
	public void updatePunchWorkdayRule(PunchWorkdayRuleDTO cmd) {
		Long userId = UserContext.current().getUser().getId();
		if (null == cmd.getId()  ) {
			LOGGER.error("Invalid   Id parameter in the command");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid  Id parameter in the command");
		}
		if (null == cmd.getOwnerId() ||null == cmd.getOwnerType()) {
			LOGGER.error("Invalid owner type or  Id parameter in the command");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid owner type or  Id parameter in the command");
		}
		if (null == cmd.getName() ) {
			LOGGER.error("Invalid name parameter in the command");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid name parameter in the command");
		}

		List<PunchWorkdayRule> punchWorkdayRules = punchProvider.queryPunchWorkdayRulesByName(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getName()) ; 
		if(null!=punchWorkdayRules && (punchWorkdayRules.size()>1 || !punchWorkdayRules.get(0).getId().equals(cmd.getId())) ){
			//有两个同名rules(正常业务不可能) 或者 同名rule的id不等于修改的id 则重名错误
			LOGGER.error("Invalid name parameter in the command");
			throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,PunchServiceErrorCode.ERROR_NAME_REPEAT,
					"name repeat");
			
		}
		
		PunchWorkdayRule old = this.punchProvider.getPunchWorkdayRuleById(cmd.getId());
		if(null == old ){
			LOGGER.error("Invalid   Id parameter in the command");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid  Id parameter in the command:can not found rule");
		}else {
			this.dbProvider.execute((TransactionStatus status) -> {
				PunchWorkdayRule obj = converDTO2WorkdayRule(cmd); 
				obj.setCreateTime(old.getCreateTime());
				obj.setCreatorUid(old.getCreatorUid()); 
				this.punchProvider.updatePunchWorkdayRule(obj); 
				this.punchProvider.deletePunchHolidayByRuleId(cmd.getId());
				if(null != cmd.getWorkdays()) {
					for (Long date : cmd.getWorkdays()) {
						PunchHoliday holiday = new PunchHoliday();
						holiday.setOwnerType(cmd.getOwnerType());
						holiday.setOwnerId(cmd.getOwnerId());  
						holiday.setCreatorUid(userId);
						holiday.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
						holiday.setWorkdayRuleId(obj.getId());
						holiday.setRuleDate(new java.sql.Date(date));
						holiday.setStatus(DateStatus.WORKDAY.getCode());
						this.punchProvider.createPunchHoliday(holiday);
								
					}
				}
				if(null != cmd.getHolidays()) {
					for (Long date : cmd.getHolidays()) {
						PunchHoliday holiday = new PunchHoliday();
						holiday.setOwnerType(cmd.getOwnerType());
						holiday.setOwnerId(cmd.getOwnerId());  
						holiday.setCreatorUid(userId);
						holiday.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
						holiday.setWorkdayRuleId(obj.getId());
						holiday.setRuleDate(new java.sql.Date(date));
						holiday.setStatus(DateStatus.HOLIDAY.getCode());
						this.punchProvider.createPunchHoliday(holiday);
					}
				}
				return null;
			});
		}
	}
	@Override
	public void deletePunchWorkdayRule(DeleteCommonCommand cmd) {
		List<PunchRule> punchRules = this.punchProvider.findPunchRules(null, null, null, null, null, cmd.getId());
		if(null == punchRules){
			//验证是否有这个权限删除这个owner的rule
			
			

			this.dbProvider.execute((TransactionStatus status) -> {
				//根据id和ownerid删除一条rule
				this.punchProvider.deletePunchWorkdayRuleByOwnerAndId(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getId());  
				this.punchProvider.deletePunchHolidayByRuleId(cmd.getId()); 
				 
				return null;
			});
		}
		else{
			//被使用的不删,报错
			throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
 					PunchServiceErrorCode.ERROR_RULE_USING,
 					"delete rule is using ");
			}

	}
	@Override
	public ListPunchWorkdayRuleListResponse listPunchWorkdayRule(ListPunchRulesCommonCommand cmd) {
		ListPunchWorkdayRuleListResponse response = new ListPunchWorkdayRuleListResponse();
		if (cmd.getPageAnchor() == null)
			cmd.setPageAnchor(0L);
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());
		 
		List<PunchWorkdayRule> results = this.punchProvider.queryPunchWorkdayRuleList(cmd.getOwnerType(),cmd.getOwnerId(), locator, pageSize + 1 );
		if (null == results)
			return response;
		Long nextPageAnchor = null;
		if (results != null && results.size() > pageSize) {
			results.remove(results.size() - 1);
			nextPageAnchor = results.get(results.size() - 1).getId();
		}
		response.setNextPageAnchor(nextPageAnchor);
		response.setWorkdayRules(new ArrayList<PunchWorkdayRuleDTO>()); 
		results.forEach((other) -> {
			PunchWorkdayRuleDTO dto =ConvertPunchWiFiRule2DTO(other);
			response.getWorkdayRules().add(dto);
		});
		return response;
	}
	
	private PunchWorkdayRuleDTO ConvertPunchWiFiRule2DTO(PunchWorkdayRule other) {
		PunchWorkdayRuleDTO dto  = ConvertHelper.convert(other, PunchWorkdayRuleDTO.class); 
		dto.setWorkWeekDates(new ArrayList<Integer>());
		int openWeekInt = Integer.valueOf(other.getWorkWeekDates());
        for(int i=0;i<7;i++){
        	if(openWeekInt%10 == 1)
        		dto.getWorkWeekDates().add(i);
        	openWeekInt = openWeekInt/10;
        }
        
		List<PunchHoliday> holidays = this.punchProvider.listPunchHolidaysByRuleId(other.getOwnerType(), other.getOwnerId(), other.getId()) ;
		if(null != holidays){
			dto.setWorkdays(new ArrayList<Long>()); 
			dto.setHolidays(new ArrayList<Long>()); 
			for(PunchHoliday day : holidays){
				if(DateStatus.HOLIDAY.equals(DateStatus.fromCode(day.getStatus())))
					dto.getHolidays().add(day.getRuleDate().getTime());
				if(DateStatus.WORKDAY.equals(DateStatus.fromCode(day.getStatus())))
					dto.getWorkdays().add(day.getRuleDate().getTime());
			} 
		}
		return dto;
	}
	@Override
	public void addPunchRule(PunchRuleDTO cmd) {

		if (null == cmd.getTimeRuleId()) {
			LOGGER.error("Invalid TimeRuleId parameter in the command");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid TimeRuleId parameter in the command");
		}
		if (null == cmd.getWorkdayRuleId()) {
			LOGGER.error("Invalid  WorkdayRuleId parameter in the command");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid  WorkdayRuleId parameter in the command");
		}
		if (null == cmd.getWifiRuleId() && null == cmd.getLocationRuleId()) {
			LOGGER.error("wifi and location can not be both null ");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,
					"wifi and location can not be both null ");
		}
		if (null == cmd.getOwnerId() ||null == cmd.getOwnerType()) {
			LOGGER.error("Invalid owner type or  Id parameter in the command");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid owner type or  Id parameter in the command");
		}
		if (null == cmd.getName() ) {
			LOGGER.error("Invalid name parameter in the command");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid name parameter in the command");
		}
		List<PunchRule> punchRules = punchProvider.queryPunchRulesByName(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getName()) ; 
		if(null!=punchRules ){
			LOGGER.error("Invalid name parameter in the command");
			throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,PunchServiceErrorCode.ERROR_NAME_REPEAT,
					"name repeat");
			
		}
		else{
			PunchRule obj = ConvertHelper.convert(cmd, PunchRule.class);
			obj.setCreatorUid(UserContext.current().getUser().getId());
			obj.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
					.getTime()));
			
			this.punchProvider.createPunchRule(obj);
			
		}
	}
	@Override
	public void updatePunchRule(PunchRuleDTO cmd) {
		Long userId = UserContext.current().getUser().getId();
		if (null == cmd.getId()  ) {
			LOGGER.error("Invalid   Id parameter in the command");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid  Id parameter in the command");
		}
		if (null == cmd.getOwnerId() ||null == cmd.getOwnerType()) {
			LOGGER.error("Invalid owner type or  Id parameter in the command");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid owner type or  Id parameter in the command");
		}
		if (null == cmd.getName() ) {
			LOGGER.error("Invalid name parameter in the command");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid name parameter in the command");
		}
		List<PunchRule> punchRules = punchProvider.queryPunchRulesByName(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getName()) ; 
		if(null!=punchRules && (punchRules.size()>1 || !punchRules.get(0).getId().equals(cmd.getId())) ){
			//有两个同名rules(正常业务不可能) 或者 同名rule的id不等于修改的id 则重名错误
			LOGGER.error("Invalid name parameter in the command");
			throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,PunchServiceErrorCode.ERROR_NAME_REPEAT,
					"name repeat");
			
		}
		
		PunchRule old = this.punchProvider.getPunchRuleById(cmd.getId());
		if(null == old ){
			LOGGER.error("Invalid   Id parameter in the command");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid  Id parameter in the command:can not found rule");
		}else {
			PunchRule obj = ConvertHelper.convert(cmd, PunchRule.class);
			obj.setCreateTime(old.getCreateTime());
			obj.setCreatorUid(old.getCreatorUid());
			obj.setOperatorUid(userId);
			obj.setOperateTime(new Timestamp(DateHelper.currentGMTTime()
					.getTime()));
			this.punchProvider.updatePunchRule(obj);
		}
	}
	@Override
	public void deletePunchRule(DeleteCommonCommand cmd) {
		List<PunchRuleOwnerMap> punchRuleMaps = this.punchProvider.queryPunchRuleOwnerMapsByRuleId(cmd.getOwnerType(),cmd.getOwnerId(),cmd.getId());
		if(null == punchRuleMaps){
			//验证是否有这个权限删除这个owner的rule
			
			

			this.dbProvider.execute((TransactionStatus status) -> {
				//根据id和ownerid删除一条rule
				PunchRule obj = this.punchProvider.getPunchRuleById(cmd.getId());
				if(obj.getOwnerId().equals(cmd.getOwnerId())&&obj.getOwnerType().equals(cmd.getOwnerType()))
					this.punchProvider.deletePunchRule(obj); 
				 
				return null;
			});
		}
		else{
			//被使用的不删,报错
			throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
 					PunchServiceErrorCode.ERROR_RULE_USING,
 					"delete rule is using ");
			}
	}
	@Override
	public ListPunchRulesResponse listPunchRules(ListPunchRulesCommonCommand cmd) {
		ListPunchRulesResponse response = new ListPunchRulesResponse();
		if (cmd.getPageAnchor() == null)
			cmd.setPageAnchor(0L);
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());
		 
		List<PunchRule> results = this.punchProvider.queryPunchRuleList(cmd.getOwnerType(),cmd.getOwnerId(), locator, pageSize + 1 );
		if (null == results)
			return response;
		Long nextPageAnchor = null;
		if (results != null && results.size() > pageSize) {
			results.remove(results.size() - 1);
			nextPageAnchor = results.get(results.size() - 1).getId();
		}
		response.setNextPageAnchor(nextPageAnchor);
		response.setPunchRuleDTOs (new ArrayList<PunchRuleDTO>()); 
		results.forEach((other) -> {
			PunchRuleDTO dto = ConvertHelper.convert(other, PunchRuleDTO.class);
			PunchTimeRule timeRule = this.punchProvider.getPunchTimeRuleById(other.getTimeRuleId());
			if(null != timeRule)
				dto.setTimeRuleName(timeRule.getName());
			PunchLocationRule locationRule = this.punchProvider.getPunchLocationRuleById(other.getLocationRuleId());
			if(null != locationRule)
				dto.setLocationRuleName(locationRule.getName());
			PunchWifiRule wifiRule = this.punchProvider.getPunchWifiRuleById(other.getWifiRuleId());
			if(null != wifiRule)
				dto.setWifiRuleName(wifiRule.getName());
			PunchWorkdayRule workdayRule = this.punchProvider.getPunchWorkdayRuleById(other.getWorkdayRuleId());
			if(null != workdayRule)
				dto.setWorkdayRuleName(workdayRule.getName());
			response.getPunchRuleDTOs().add(dto);
		});
		return response;
	}
	@Override
	public void addPunchRuleMap(PunchRuleMapDTO cmd) {

		if (null == cmd.getOwnerId() ||null == cmd.getOwnerType()) {
			LOGGER.error("Invalid owner type or  Id parameter in the command");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid owner type or  Id parameter in the command");
		}
		if (null == cmd.getTargetId() ||null == cmd.getTargetType()) {
			LOGGER.error("Invalid owner type or  Id parameter in the command");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid target type or  Id parameter in the command");
		}
		if(null == cmd.getPunchRuleId()){
			LOGGER.error("Invalid PunchRuleId parameter in the command");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid PunchRuleId parameter in the command");
		}
			
		PunchRuleOwnerMap old = this.punchProvider.getPunchRuleOwnerMapByOwnerAndTarget(cmd.getOwnerType(), cmd.getOwnerId(),  cmd.getTargetType(), cmd.getTargetId());
		PunchRuleOwnerMap obj = ConvertHelper.convert(cmd, PunchRuleOwnerMap.class);
		if( null == old ){
			//如果没有就新建
			obj.setCreatorUid(UserContext.current().getUser().getId());
			obj.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
					.getTime()));
			this.punchProvider.createPunchRuleOwnerMap(obj);
		}else{
			//有就更新
			obj.setId(old.getId());
			obj.setCreatorUid(old.getCreatorUid());
			obj.setCreateTime(old.getCreateTime());
			this.punchProvider.updatePunchRuleOwnerMap(obj);
		}
	}
	@Override
	public ListPunchRuleMapsResponse listPunchRuleMaps(ListPunchRuleMapsCommand cmd) {
		ListPunchRuleMapsResponse response = new ListPunchRuleMapsResponse();
		if (cmd.getPageAnchor() == null)
			cmd.setPageAnchor(0L);
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());
		//找到所有子部门 下面的用户
		List<Long> userIds = null;
		if (cmd.getOrganizationId() != null) {

			List<String> groupTypeList = new ArrayList<String>();
			groupTypeList.add(OrganizationGroupType.ENTERPRISE.getCode());
			groupTypeList.add(OrganizationGroupType.DEPARTMENT.getCode());
			List<OrganizationMemberDTO> organizationMembers = this.organizationService.listAllChildOrganizationPersonnel
					(cmd.getOrganizationId(), groupTypeList, null) ;
			if(null == organizationMembers)
				return response;
			userIds = new ArrayList<Long>();
			for(OrganizationMemberDTO member : organizationMembers){
				if (member.getTargetType() != null && member.getTargetType().equals(OrganizationMemberTargetType.USER.getCode()))
					userIds.add(member.getTargetId());
			}
		}
		List<PunchRuleOwnerMap> results = this.punchProvider.queryPunchRuleOwnerMapList(cmd.getOwnerType(),cmd.getOwnerId(), cmd.getTargetType(),
				cmd.getTargetId(),userIds,locator, pageSize + 1 );
		if (null == results)
			return response;
		Long nextPageAnchor = null;
		if (results != null && results.size() > pageSize) {
			results.remove(results.size() - 1);
			nextPageAnchor = results.get(results.size() - 1).getId();
		}
		response.setNextPageAnchor(nextPageAnchor);
		response.setPunchRuleMaps(new ArrayList<PunchRuleMapDTO>()); 
		results.forEach((other) -> {
			PunchRuleMapDTO dto = ConvertHelper.convert(other, PunchRuleMapDTO.class);
			PunchRule pr = this.punchProvider.getPunchRuleById(other.getPunchRuleId());
			if(pr == null)
				throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
						PunchServiceErrorCode.ERROR_ENTERPRISE_DIDNOT_SETTING,
						"have no punch rule");
			dto.setPunchRuleName(pr.getName());
			if(PunchOwnerType.User.getCode().equals(other.getTargetType())){
				OrganizationMember member = this.organizationProvider.findOrganizationMemberByOrgIdAndUId(other.getTargetId(), other.getOwnerId());
				dto.setTargetName(member.getContactName());
				OrganizationDTO dept =  this.findUserDepartment(other.getTargetId(), other.getOwnerId());
				if(null != dept)
					dto.setTargetDept(dept.getName());
				
			}
			if (other.getReviewRuleId() != null) {
				ApprovalRule approvalRule = approvalRuleProvider.findApprovalRuleById(other.getReviewRuleId());
				if (approvalRule != null) {
					dto.setReviewRuleName(approvalRule.getName());
				}
			}
			response.getPunchRuleMaps().add(dto);
		});
		return response;
	}
	/**找到用户的部门-多部门取最上级第一个*/
	private OrganizationDTO findUserDepartment(Long userId, Long organizationId){
//		// 多部门找顶级部门
//		List<OrganizationMember> organizationMembers = this.organizationProvider.findOrganizationMembersByOrgIdAndUId( userId,  organizationId);
//		Organization result = null;
//		if(organizationMembers == null || organizationMembers.isEmpty())
//			return null;
//		for(OrganizationMember member : organizationMembers){
//			//groupid == 0 话直接返回总公司
//			if(null == member.getGroupId() || member.getGroupId().equals(0L))
//				return this.organizationProvider.findOrganizationById(member.getOrganizationId());
//			if(result == null)
//				result = this.organizationProvider.findOrganizationById(member.getGroupId());
//			else{
//				//多部门找顶级部门
//				Organization org = this.organizationProvider.findOrganizationById(member.getGroupId());
//				if(org.getLevel()<result.getLevel()){
//					//取最顶层的  
//					result = org;
//				}
//			}
//		}
		UserIdentifier userIdentifier = this.userProvider.findClaimedIdentifierByOwnerAndType(userId, IdentifierType.MOBILE.getCode()) ;
		if(null == userIdentifier){
			LOGGER.debug("userIdentifier is null...userId = " +userId);
			return null;
		}else{
			return this.organizationService.getMemberTopDepartment(OrganizationGroupType.DEPARTMENT,
					userIdentifier.getIdentifierToken(), organizationId);
			}
		 

	}
	/** 向上递归找规则*/
	private PunchRuleOwnerMap getPunchRule( Long ownerId , Organization dept,int loopMax){
		if(--loopMax <0 || null == dept)
			throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
					PunchServiceErrorCode.ERROR_ENTERPRISE_DIDNOT_SETTING,
					"have no punch rule");
		PunchRuleOwnerMap ruleMap = this.punchProvider.getPunchRuleOwnerMapByOwnerAndTarget(PunchOwnerType.ORGANIZATION.getCode(), ownerId,
				PunchOwnerType.ORGANIZATION.getCode(), dept.getId());
		if(null != ruleMap)
			return ruleMap;
		else{
			Organization parent = this.organizationProvider.findOrganizationById(dept.getParentId());
			if (null == parent)
				throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
						PunchServiceErrorCode.ERROR_ENTERPRISE_DIDNOT_SETTING,
						"have no punch rule owner id = '"+ownerId+"'");
			else 
				return getPunchRule(ownerId, parent,loopMax);
		}
	}
	/**找到用户的打卡总规则*/
	@Override
	public PunchRule getPunchRule(String ownerType, Long ownerId,Long userId){
		//如果有个人规则就返回个人规则
		PunchRuleOwnerMap map = this.punchProvider.getPunchRuleOwnerMapByOwnerAndTarget(ownerType, ownerId, PunchOwnerType.User.getCode(), userId);
		if (null == map){
			//没有个人规则,向上递归找部门规则
			if(!ownerType.equals(PunchOwnerType.ORGANIZATION.getCode()))
				return null;
			//加循环限制
			int loopMax = 10;
			OrganizationDTO deptDTO = findUserDepartment(userId, ownerId);
			Organization dept =  ConvertHelper.convert(deptDTO, Organization.class);
			map = getPunchRule(ownerId ,dept,loopMax);
		}
		return this.punchProvider.getPunchRuleById(map.getPunchRuleId());
		
	}
	
	//ownerType为组织，ownerId为组织id
	@Override
	public ApprovalRule getApprovalRule(String ownerType, Long ownerId,Long userId){
		//如果有个人规则就返回个人规则
		PunchRuleOwnerMap map = this.punchProvider.getPunchRuleOwnerMapByOwnerAndTarget(ownerType, ownerId, PunchOwnerType.User.getCode(), userId);
		if (null == map){
			//没有个人规则,向上递归找部门规则
			if(!ownerType.equals(PunchOwnerType.ORGANIZATION.getCode()))
				return null;
			//加循环限制
			int loopMax = 10;
			OrganizationDTO deptDTO = findUserDepartment(userId, ownerId);
			Organization dept =  ConvertHelper.convert(deptDTO, Organization.class);
			map = getPunchRule(ownerId ,dept,loopMax);
		}
		return approvalRuleProvider.findApprovalRuleById(map.getReviewRuleId());
	}
	
//	/**找到用户的打卡时间规则*/
//	private PunchTimeRule getTimeRule(String ownerType, Long ownerId ,Long  userId) {  
//		PunchRule pr = this.getPunchRule(ownerType, ownerId, userId);
//		if(null == pr)
//			throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
//					PunchServiceErrorCode.ERROR_ENTERPRISE_DIDNOT_SETTING,
//					"have no punch rule");
//		return this.punchProvider.findPunchTimeRuleById(pr.getTimeRuleId());
//	}

	/**
	 * 打卡2.0 的考勤统计月报
	 * */
	@Override
	public ListPunchCountCommandResponse listPunchCount(
			ListPunchCountCommand cmd) {
//		processQueryCommandDay(cmd);
		if (null == cmd.getOwnerId() ||null == cmd.getOwnerType()) {
			LOGGER.error("Invalid owner type or  Id parameter in the command");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid owner type or  Id parameter in the command");
		}
		ListPunchCountCommandResponse response = new ListPunchCountCommandResponse();
		List<PunchCountDTO> punchCountDTOList = new ArrayList<PunchCountDTO>();

		//找到所有子部门 下面的用户
		Organization org = this.checkOrganization(cmd.getOwnerId());
		List<String> groupTypeList = new ArrayList<String>();
		groupTypeList.add(OrganizationGroupType.ENTERPRISE.getCode());
		groupTypeList.add(OrganizationGroupType.DEPARTMENT.getCode());
		List<OrganizationMemberDTO> organizationMembers = this.organizationService.listAllChildOrganizationPersonnel
				(cmd.getOwnerId(), groupTypeList, cmd.getUserName()) ;
		if(null == organizationMembers)
			return response;
		List<Long> userIds = new ArrayList<Long>();
		for(OrganizationMemberDTO member : organizationMembers){
			if (member.getTargetType() != null && member.getTargetType().equals(OrganizationMemberTargetType.USER.getCode()))
				userIds.add(member.getTargetId());
		}
		//分页查询
		if (cmd.getPageAnchor() == null)
			cmd.setPageAnchor(0L);
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());
		Long organizationId = org.getDirectlyEnterpriseId();
		if(organizationId.equals(0L))
			organizationId = org.getId();
		List<PunchStatistic> results = this.punchProvider.queryPunchStatistics(cmd.getOwnerType(),
				organizationId,cmd.getMonth(),
				cmd.getExceptionStatus(),userIds, locator, pageSize + 1 );
		
		if (null == results)
			return response;
		Long nextPageAnchor = null;
		if (results != null && results.size() > pageSize) {
			results.remove(results.size() - 1);
			nextPageAnchor = results.get(results.size() - 1).getId();
		}
		
		List<Long> absenceUserIdList = new ArrayList<>();
		for(PunchStatistic statistic : results){
			PunchCountDTO dto =ConvertHelper.convert(statistic, PunchCountDTO.class);
			if(statistic.getOverTimeSum().equals(0L)){
				dto.setOverTimeSum(0.0);
			}
			else{
				BigDecimal b = new BigDecimal(statistic.getOverTimeSum()/3600000.0);
				dto.setOverTimeSum(b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
			}
			
			punchCountDTOList.add(dto);
			absenceUserIdList.add(statistic.getUserId());
		}
		response.setNextPageAnchor(nextPageAnchor);
		response.setPunchCountList(punchCountDTOList);
		
		//把请假的天数加在这里，add by tt, 20160921
		Map<Long, List<AbsenceTimeDTO>> userAbsenceTimeMap = getUserAbsenceTimes(cmd.getMonth(), cmd.getOwnerType(), cmd.getOwnerId(), absenceUserIdList);
		punchCountDTOList.forEach(p->{
			List<AbsenceTimeDTO> list = userAbsenceTimeMap.get(p.getUserId());
			if (ListUtils.isEmpty(list)) {
				try {
					list = getDefaultAbsenceStatistics(organizationService.getTopOrganizationId(cmd.getOwnerId()), new java.sql.Date(monthSF.parse(cmd.getMonth()).getTime()));
				} catch (Exception e) {
					throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,
							"parse month error");
				}
			}
			p.setAbsenceTimeList(list);
		});
		
		return response;
	}

	private Map<Long, List<AbsenceTimeDTO>> getUserAbsenceTimes(String month, String ownerType, Long ownerId, List<Long> absenceUserIdList) {
		Long userId = UserContext.current().getUser().getId();
		Long organizationId = organizationService.getTopOrganizationId(ownerId);
		try {
			java.sql.Date fromDate = new java.sql.Date(monthSF.parse(month).getTime());
			java.sql.Date toDate = new java.sql.Date(getNextMonth(month).getTime());
			
			List<ApprovalDayActualTime> approvalDayActualTimeList = approvalDayActualTimeProvider.listApprovalDayActualTimeByUserIds(fromDate, toDate, ApprovalOwnerType.ORGANIZATION.getCode(), organizationId, ApprovalType.ABSENCE.getCode(), absenceUserIdList);
			if (ListUtils.isEmpty(approvalDayActualTimeList)) {
				return new HashMap<Long, List<AbsenceTimeDTO>>();
			}
			//需要把 针对同一天既有请假申请，又有忘打卡申请，已最后提交的申请为依据 排除掉
			Map<Long, Map<Long, List<ApprovalDayActualTime>>> map = approvalDayActualTimeList.stream().map(a->{
				if (approvalRequestProvider.checkExcludeAbsenceRequest(userId, a.getOwnerId(), a.getTimeDate())) {
					return null;
				}
				return a;
			}).filter(a->a!=null).collect(Collectors.groupingBy(ApprovalDayActualTime::getUserId, Collectors.groupingBy(ApprovalDayActualTime::getCategoryId)));
			
			Map<Long, List<AbsenceTimeDTO>> resultMap = new HashMap<>();
			List<ApprovalCategory> approvalCategoryList = approvalCategoryProvider.listApprovalCategoryForStatistics(UserContext.getCurrentNamespaceId(), ApprovalOwnerType.ORGANIZATION.getCode(), organizationId, ApprovalType.ABSENCE.getCode(), fromDate);
			//key1 userId, key2 categoryId
			map.forEach((key1, value1)->{
				List<AbsenceTimeDTO> absenceTimeList = approvalCategoryList.stream().map(a->{
					List<ApprovalDayActualTime> value2 = value1.get(a.getId());
					AbsenceTimeDTO absenceTimeDTO = new AbsenceTimeDTO();
					absenceTimeDTO.setCategoryId(a.getId());
					absenceTimeDTO.setCategoryName(a.getCategoryName());
					if (ListUtils.isEmpty(value2)) {
						absenceTimeDTO.setActualResult("0.0.0");
					}else {
						absenceTimeDTO.setActualResult("");
						value2.forEach(v->{
							absenceTimeDTO.setActualResult(calculateTimeTotal(absenceTimeDTO.getActualResult(), v.getActualResult()));
						});
					}
					return absenceTimeDTO;
				}).collect(Collectors.toList());
				
				resultMap.put(key1, absenceTimeList);
			});
			
			return resultMap;
		} catch (ParseException e) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,
					"parse month error");
		}
	}

	private List<AbsenceTimeDTO> getDefaultAbsenceStatistics(Long organizationId, java.sql.Date fromDate){
		List<ApprovalCategory> approvalCategoryList = approvalCategoryProvider.listApprovalCategoryForStatistics(UserContext.getCurrentNamespaceId(), ApprovalOwnerType.ORGANIZATION.getCode(), organizationId, ApprovalType.ABSENCE.getCode(), fromDate);
		return approvalCategoryList.stream().map(a->{
			AbsenceTimeDTO absenceTimeDTO = new AbsenceTimeDTO();
			absenceTimeDTO.setCategoryId(a.getId());
			absenceTimeDTO.setCategoryName(a.getCategoryName());
			absenceTimeDTO.setActualResult("0.0.0");
			return absenceTimeDTO;
		}).collect(Collectors.toList());
	}
	
	private String calculateTimeTotal(String timeTotal, String actualResult) {
		//表中按1.25.33这样存储，每一位分别代表天、小时、分钟，统计时需要每个位分别相加，且小时满24不用进一，分钟满60需要进一，如果某一位是0也必须存储，也就是说结果中必须包含两个小数点
		if (StringUtils.isBlank(timeTotal)) {
			return actualResult;
		}
		
		String[] times = timeTotal.split("\\.");
		String[] actuals = actualResult.split("\\.");
		
		int days = Integer.parseInt(times[0]) + Integer.parseInt(actuals[0]);
		int hours = Integer.parseInt(times[1]) + Integer.parseInt(actuals[1]);
		int minutes = Integer.parseInt(times[2]) + Integer.parseInt(actuals[2]);
		
		hours = hours + minutes / 60;
		minutes = minutes % 60;
		
		return days + "." + hours + "." + minutes;
	}
	
	private String getCategoryName(Long categoryId){
		ApprovalCategory category = approvalCategoryProvider.findApprovalCategoryById(categoryId);
		if (category != null) {
			return category.getCategoryName();
		}
		return "";
	}
	
	private Date getNextMonth(String month) {
		try {
			Date date = monthSF.parse(month);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.MONTH, 1);
			return calendar.getTime();
		} catch (ParseException e) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,
					"parse month error");
		}
	}
	/**
	 * 打卡2.0 的考勤详情
	 * */
	@Override
	public HttpServletResponse exportPunchDetails(ListPunchDetailsCommand cmd, HttpServletResponse response) {
		// TODO Auto-generated method stub

		if (null == cmd.getOwnerId() ||null == cmd.getOwnerType()) {
			LOGGER.error("Invalid owner type or  Id parameter in the command");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid owner type or  Id parameter in the command");
		}
		List<PunchDayDetailDTO> dtos = new ArrayList<PunchDayDetailDTO>();
		PunchOwnerType ownerType = PunchOwnerType.fromCode(cmd.getOwnerType());
		if(PunchOwnerType.ORGANIZATION.equals(ownerType)){
			//找到所有子部门 下面的用户
			Organization org = this.checkOrganization(cmd.getOwnerId());
			List<String> groupTypeList = new ArrayList<String>();
			groupTypeList.add(OrganizationGroupType.ENTERPRISE.getCode());
			groupTypeList.add(OrganizationGroupType.DEPARTMENT.getCode());
			List<OrganizationMemberDTO> organizationMembers = this.organizationService.listAllChildOrganizationPersonnel
					(cmd.getOwnerId(), groupTypeList, cmd.getUserName()) ;
			if(null == organizationMembers)
				return null;
			List<Long> userIds = new ArrayList<Long>();
			for(OrganizationMemberDTO member : organizationMembers){
				userIds.add(member.getTargetId());
			}
			//分页查询 由于用到多条件排序,所以使用pageOffset方式分页
			 
			String startDay=null;
			if(null!=cmd.getStartDay())
				startDay =  dateSF.format(new Date(cmd.getStartDay()));
			String endDay=null;
			if(null!=cmd.getEndDay())
				endDay =  dateSF.format(new Date(cmd.getEndDay()));
			Long organizationId = org.getDirectlyEnterpriseId();
			if(organizationId.equals(0L))
				organizationId = org.getId();
			List<PunchDayLog> results = punchProvider.listPunchDayLogs(userIds,
					organizationId,startDay,endDay , 
					cmd.getArriveTimeCompareFlag(),convertTime(cmd.getArriveTime()), cmd.getLeaveTimeCompareFlag(),
					convertTime(cmd.getLeaveTime()), cmd.getWorkTimeCompareFlag(),
					convertTime(cmd.getWorkTime()),cmd.getExceptionStatus(), null, null);
			
			if (null == results)
				return null;
			 
			for(PunchDayLog r : results){
				PunchDayDetailDTO dto =convertToPunchDayDetailDTO(r);
				dtos.add(dto);
			}
			
		}
		
		URL rootPath = PunchServiceImpl.class.getResource("/");
		String filePath =rootPath.getPath() + this.downloadDir ;
		File file = new File(filePath);
		if(!file.exists())
			file.mkdirs();
		filePath = filePath + "PunchDetails"+System.currentTimeMillis()+".xlsx";
		//新建了一个文件
		
		this.createPunchDetailsBook(filePath,dtos);
		
		return download(filePath,response);
	}

    private void createPunchDetailsBook(String path, List<PunchDayDetailDTO> dtos) {
    	if (null == dtos || dtos.size() == 0)
			return;
		Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet("punchDetails");
		
		this.createPunchDetailsBookSheetHead(sheet );
		for (PunchDayDetailDTO dto : dtos )
			this.setNewPunchDetailsBookRow(sheet, dto);
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
    private String convertTimeLongToString(Long timeLong){
    	if(null == timeLong)
    		return "";
    	else 
    		return timeSF.format(convertTime(timeLong));
    }
	private void setNewPunchDetailsBookRow(Sheet sheet, PunchDayDetailDTO dto) {

		Row row = sheet.createRow(sheet.getLastRowNum()+1);
		int i = -1; 
		row.createCell(++i).setCellValue(dto.getUserName());
		row.createCell(++i).setCellValue(dto.getDeptName());
		row.createCell(++i).setCellValue(dateSF.format(new Date(dto.getPunchDate()))); 
		row.createCell(++i).setCellValue(convertTimeLongToString(dto.getWorkTime())); 
		PunchTimesPerDay timePerDay = PunchTimesPerDay.fromCode(dto.getPunchTimesPerDay());
		switch(timePerDay){
		case TWICE:
			row.createCell(++i).setCellValue(convertTimeLongToString(dto.getArriveTime())+"/"+convertTimeLongToString(dto.getLeaveTime())); 
			row.createCell(++i).setCellValue(statusToString(dto.getStatus())); 
			row.createCell(++i).setCellValue(dto.getApprovalStatus()==null?statusToString(dto.getStatus()):statusToString(dto.getApprovalStatus())); 
			break;
		case FORTH:
			row.createCell(++i).setCellValue(convertTimeLongToString(dto.getArriveTime())
					+"/"+convertTimeLongToString(dto.getNoonLeaveTime())
					+"/"+convertTimeLongToString(dto.getAfternoonArriveTime())
					+"/"+convertTimeLongToString(dto.getLeaveTime())); 
			row.createCell(++i).setCellValue(statusToString(dto.getMorningStatus())+"/"+statusToString(dto.getAfternoonStatus())); 
			row.createCell(++i).setCellValue(
					dto.getMorningApprovalStatus()==null?statusToString(dto.getMorningStatus()):statusToString(dto.getMorningApprovalStatus())
					+"/"+
					dto.getAfternoonApprovalStatus()==null?statusToString(dto.getAfternoonStatus()):statusToString(dto.getAfternoonApprovalStatus())); 
			break;
		default :
			++i;
			++i;
			++i;
			break;
		}
		ExceptionStatus exception = ExceptionStatus.fromCode(dto.getExceptionStatus());
		if(null != exception){
			switch(exception){
				case NORMAL:
					row.createCell(++i).setCellValue("正常");
					break;
				case EXCEPTION:
					row.createCell(++i).setCellValue("异常");
					break;
				default :
					++i;
					break;
			}
		}else{
			++i;
		}

	}
	private void createPunchDetailsBookSheetHead(Sheet sheet) {
		Row row = sheet.createRow(sheet.getLastRowNum());
		int i =-1 ;
		row.createCell(++i).setCellValue("姓名");
		row.createCell(++i).setCellValue("部门");
		row.createCell(++i).setCellValue("打卡日期");
		row.createCell(++i).setCellValue("工作时长");
		row.createCell(++i).setCellValue("打卡");
		row.createCell(++i).setCellValue("打卡状态");
		row.createCell(++i).setCellValue("考勤状态");
		row.createCell(++i).setCellValue("状态"); 
	}
	private Organization checkOrganization(Long orgId) {
		Organization org = organizationProvider.findOrganizationById(orgId);
		if(org == null){
			LOGGER.error("Unable to find the organization.organizationId=" + orgId);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Unable to find the organization.");
		}
		return org;
	}
	@Override
	public ListPunchMonthLogsResponse listPunchMonthLogs(ListPunchMonthLogsCommand cmd) {
		if (null == cmd.getOwnerId() ||null == cmd.getOwnerType()) {
			LOGGER.error("Invalid owner type or  Id parameter in the command");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid owner type or  Id parameter in the command");
		}
		
		ListPunchMonthLogsResponse response = new ListPunchMonthLogsResponse();
		PunchOwnerType ownerType = PunchOwnerType.fromCode(cmd.getOwnerType());
		if(PunchOwnerType.ORGANIZATION.equals(ownerType)){
			//找到所有子部门 下面的用户
			Organization org = this.checkOrganization(cmd.getOwnerId());
			List<String> groupTypeList = new ArrayList<String>();
			groupTypeList.add(OrganizationGroupType.ENTERPRISE.getCode());
			groupTypeList.add(OrganizationGroupType.DEPARTMENT.getCode());
			List<OrganizationMemberDTO> organizationMembers = this.organizationService.listAllChildOrganizationPersonnel
					(cmd.getOwnerId(), groupTypeList, cmd.getUserName()) ;
			if(null == organizationMembers)
				return response;  
			response.setUserLogs(new ArrayList<UserMonthLogsDTO>());
			//取查询月的第一天和最后一天
			Calendar monthBegin = Calendar.getInstance();
			try {
				monthBegin.setTime(monthSF.parse(cmd.getPunchMonth()));
				//月初
				monthBegin.set(Calendar.DAY_OF_MONTH, 1);
				Calendar monthEnd = Calendar.getInstance();
				if(!monthSF.format(monthEnd.getTime()).equals(cmd.getPunchMonth())){
					monthEnd.setTime(monthSF.parse(cmd.getPunchMonth()));
					//月末
					monthEnd.set(Calendar.DAY_OF_MONTH, monthEnd.getActualMaximum(Calendar.DAY_OF_MONTH));
				} 
				for(OrganizationMemberDTO member : organizationMembers){
					if(null == member.getTargetType())
						continue;
					UserMonthLogsDTO userMonthLogsDTO = new UserMonthLogsDTO(); 
					Organization dept = this.organizationProvider.findOrganizationById(member.getGroupId());
					if(null == dept)
						dept = this.organizationProvider.findOrganizationById(member.getOrganizationId());
					userMonthLogsDTO.setDeptName(dept.getName());
					userMonthLogsDTO.setUserName(member.getContactName());
					List<PunchDayLog> punchDayLogs = punchProvider.listPunchDayLogs(member.getTargetId(),member.getOrganizationId(), dateSF.format(monthBegin.getTime()),
							dateSF.format(monthEnd.getTime()) );
					if (null == punchDayLogs || punchDayLogs.isEmpty())
						continue;
					userMonthLogsDTO.setPunchLogsDayList(new ArrayList<PunchLogsDay>());
					ExceptionStatus exceptionStatus = ExceptionStatus.NORMAL;
					for(PunchDayLog dayLog : punchDayLogs){
						PunchLogsDay pdl = ConvertHelper.convert(dayLog, PunchLogsDay.class);
						Calendar logDay = Calendar.getInstance();
						logDay.setTime(dayLog.getPunchDate());
						pdl.setPunchDay(String.valueOf(logDay.get(Calendar.DAY_OF_MONTH)));
						pdl.setWorkTime(convertTimeToGMTMillisecond(dayLog.getWorkTime()));
						pdl.setPunchStatus(dayLog.getStatus());
						pdl.setAfternoonPunchStatus(dayLog.getAfternoonStatus());
						pdl.setMorningPunchStatus(dayLog.getMorningStatus());
						userMonthLogsDTO.getPunchLogsDayList().add(pdl);
						if (dayLog.getExceptionStatus() != null && ExceptionStatus.EXCEPTION.equals(ExceptionStatus.fromCode(dayLog.getExceptionStatus()))){
							exceptionStatus = ExceptionStatus.EXCEPTION;
						}
							
					}
					if (cmd.getExceptionStatus()!= null && !exceptionStatus.equals(ExceptionStatus.fromCode(cmd.getExceptionStatus()))) 
						continue;
					response.getUserLogs().add(userMonthLogsDTO);
				}

			} catch (ParseException e) {
				LOGGER.error("ParseException : punch month : "+cmd.getPunchMonth());

				throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
						ErrorCodes.ERROR_INVALID_PARAMETER,
						"ParseException : punch month INVALID :  "+cmd.getPunchMonth()); 
			}
		}
		return response;
	}

	/**
	 * 打卡2.0 的考勤详情
	 * */
	@Override
	public ListPunchDetailsResponse listPunchDetails(ListPunchDetailsCommand cmd) {
 
		if (null == cmd.getOwnerId() ||null == cmd.getOwnerType()) {
			LOGGER.error("Invalid owner type or  Id parameter in the command");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid owner type or  Id parameter in the command");
		}
		
		ListPunchDetailsResponse response = new ListPunchDetailsResponse();
		
		
		PunchOwnerType ownerType = PunchOwnerType.fromCode(cmd.getOwnerType());
		if(PunchOwnerType.ORGANIZATION.equals(ownerType)){
			//找到所有子部门 下面的用户
			Organization org = this.checkOrganization(cmd.getOwnerId());
			List<String> groupTypeList = new ArrayList<String>();
			groupTypeList.add(OrganizationGroupType.ENTERPRISE.getCode());
			groupTypeList.add(OrganizationGroupType.DEPARTMENT.getCode());
			List<OrganizationMemberDTO> organizationMembers = this.organizationService.listAllChildOrganizationPersonnel
					(cmd.getOwnerId(), groupTypeList, cmd.getUserName()) ;
			if(null == organizationMembers)
				return response;
			List<Long> userIds = new ArrayList<Long>();
			for(OrganizationMemberDTO member : organizationMembers){
				userIds.add(member.getTargetId());
			}
			//分页查询 由于用到多条件排序,所以使用pageOffset方式分页
			Integer pageOffset = 0; 
			if (cmd.getPageAnchor() != null)
				pageOffset = cmd.getPageAnchor().intValue();
			int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
			String startDay=null;
			if(null!=cmd.getStartDay())
				startDay =  dateSF.format(new Date(cmd.getStartDay()));
			String endDay=null;
			if(null!=cmd.getEndDay())
				endDay =  dateSF.format(new Date(cmd.getEndDay()));
			Long organizationId = org.getDirectlyEnterpriseId();
			if(organizationId.equals(0L))
				organizationId = org.getId();
			List<PunchDayLog> results = punchProvider.listPunchDayLogs(userIds,
					organizationId,startDay,endDay , 
					cmd.getArriveTimeCompareFlag(),convertTime(cmd.getArriveTime()), cmd.getLeaveTimeCompareFlag(),
					convertTime(cmd.getLeaveTime()), cmd.getWorkTimeCompareFlag(),
					convertTime(cmd.getWorkTime()),cmd.getExceptionStatus(), pageOffset, pageSize );
			
			if (null == results)
				return response;
			Long nextPageAnchor = Long.valueOf(pageOffset+pageSize);
			response.setNextPageAnchor(nextPageAnchor);
			
			response.setPunchDayDetails(new ArrayList<PunchDayDetailDTO>());
			for(PunchDayLog r : results){
				PunchDayDetailDTO dto =convertToPunchDayDetailDTO(r);
				if(null!= r.getArriveTime())
					dto.setArriveTime(  convertTimeToGMTMillisecond(r.getArriveTime())  );

				if(null!= r.getLeaveTime())
					dto.setLeaveTime( convertTimeToGMTMillisecond(r.getLeaveTime()));

				if(null!= r.getWorkTime())
					dto.setWorkTime( convertTimeToGMTMillisecond( r.getWorkTime()));

				if(null!= r.getNoonLeaveTime())
					dto.setNoonLeaveTime(  convertTimeToGMTMillisecond(r.getNoonLeaveTime()));

				if(null!= r.getAfternoonArriveTime())
					dto.setAfternoonArriveTime(  convertTimeToGMTMillisecond(r.getAfternoonArriveTime()));
				if(null!= r.getPunchDate())
					dto.setPunchDate(r.getPunchDate().getTime());
				response.getPunchDayDetails().add(dto);
			}
			
		}
		return response;
	}
	public PunchDayDetailDTO convertToPunchDayDetailDTO(PunchDayLog r ){
		PunchDayDetailDTO dto =  ConvertHelper.convert(r,PunchDayDetailDTO.class);
			
			if(null!= r.getArriveTime())
				dto.setArriveTime(  convertTimeToGMTMillisecond(r.getArriveTime())  );

			if(null!= r.getLeaveTime())
				dto.setLeaveTime( convertTimeToGMTMillisecond(r.getLeaveTime()));

			if(null!= r.getWorkTime())
				dto.setWorkTime( convertTimeToGMTMillisecond( r.getWorkTime()));

			if(null!= r.getNoonLeaveTime())
				dto.setNoonLeaveTime(  convertTimeToGMTMillisecond(r.getNoonLeaveTime()));

			if(null!= r.getAfternoonArriveTime())
				dto.setAfternoonArriveTime(  convertTimeToGMTMillisecond(r.getAfternoonArriveTime()));
			if(null!= r.getPunchDate())
				dto.setPunchDate(r.getPunchDate().getTime());
			dto.setPunchTimesPerDay(r.getPunchTimesPerDay());
			OrganizationMember member = organizationProvider.findOrganizationMemberByOrgIdAndUId(dto.getUserId(), r.getEnterpriseId() );
			if (null != member) {
				dto.setUserName(member.getContactName());
				OrganizationDTO dept = this.findUserDepartment(dto.getUserId(), member.getOrganizationId());
				if(null != dept){
					dto.setDeptName(dept.getName());
				}

				   
//				dto.setUserPhoneNumber(member.getContactToken());
				// dto.setUserDepartment(enterpriseContact.get);
				PunchExceptionApproval approval = punchProvider
						.getExceptionApproval(dto.getUserId(),
								 r.getEnterpriseId() ,
								new java.sql.Date(dto.getPunchDate()));
				if (approval != null) {
					dto.setApprovalStatus(approval
							.getApprovalStatus());
					dto.setMorningApprovalStatus(approval.getMorningApprovalStatus());
					dto.setAfternoonApprovalStatus(approval.getAfternoonApprovalStatus());
//					OrganizationMember operator = organizationProvider.findOrganizationMemberByOrgIdAndUId(approval.getOperatorUid(), cmd.getEnterpriseId());
//					if(null != operator )
//						dto.setOperatorName(operator.getContactName());
				} else {
					//do nothing
//					dto.setApprovalStatus((byte) 0);
				}
			}
			return dto;
	}

	/**
	 * 打卡2.0 的考勤统计
	 * */
	@Override
	public HttpServletResponse exportPunchStatistics(ListPunchCountCommand cmd, HttpServletResponse response) {

		//找到所有子部门 下面的用户
		Organization org = this.checkOrganization(cmd.getOwnerId());
		List<String> groupTypeList = new ArrayList<String>();
		groupTypeList.add(OrganizationGroupType.ENTERPRISE.getCode());
		groupTypeList.add(OrganizationGroupType.DEPARTMENT.getCode());
		List<OrganizationMemberDTO> organizationMembers = this.organizationService.listAllChildOrganizationPersonnel
				(cmd.getOwnerId(), groupTypeList, cmd.getUserName()) ;
		if(null == organizationMembers)
			return response;
		List<Long> userIds = new ArrayList<Long>();
		for(OrganizationMemberDTO member : organizationMembers){
			if (null != member.getTargetType() && member.getTargetType().equals(OrganizationMemberTargetType.USER.getCode()))
				userIds.add(member.getTargetId());
		}

		Long organizationId = org.getDirectlyEnterpriseId();
		if(organizationId.equals(0L))
			organizationId = org.getId();
		List<PunchStatistic> results = this.punchProvider.queryPunchStatistics(cmd.getOwnerType(),organizationId,cmd.getMonth(),cmd.getExceptionStatus()
				,userIds, null, Integer.MAX_VALUE);
		if(null == results || results.isEmpty())
			return response;
		URL rootPath = PunchServiceImpl.class.getResource("/");
		String filePath =rootPath.getPath() + this.downloadDir ;
		File file = new File(filePath);
		if(!file.exists())
			file.mkdirs();
		filePath = filePath + "PunchStatistics"+System.currentTimeMillis()+".xlsx";
		//新建了一个文件
		
		this.createPunchStatisticsBook(filePath,results);
		
		return download(filePath,response);
	}
	
	@Override
	public void refreshMonthDayLogs(String month){

		Calendar monthBegin = Calendar.getInstance();
		Calendar monthEnd = Calendar.getInstance();
		try {
			monthBegin.setTime(monthSF.parse(month));
			//月初
			monthBegin.set(Calendar.DAY_OF_MONTH, 1);
			if(!monthSF.format(monthEnd.getTime()).equals(month)){
				monthEnd.setTime(monthSF.parse(month));
				//月末
				monthEnd.set(Calendar.DAY_OF_MONTH, monthEnd.getActualMaximum(Calendar.DAY_OF_MONTH));
			}

			while (true) {

				List<Long> orgIds = this.punchProvider.queryPunchOrganizationsFromRules();
				for(Long orgId : orgIds){

					List<String> groupTypeList = new ArrayList<String>();
					groupTypeList.add(OrganizationGroupType.ENTERPRISE.getCode());
					groupTypeList.add(OrganizationGroupType.DEPARTMENT.getCode());
					List<OrganizationMemberDTO> members = this.organizationService.listAllChildOrganizationPersonnel
							(orgId, groupTypeList, null);
					//循环刷所有员工
					for(OrganizationMemberDTO member : members){
						if(member.getTargetType().equals(OrganizationMemberTargetType.USER.getCode()) && null != member.getTargetId()){
							try {
								//刷新 daylog
								this.refreshPunchDayLog(member.getTargetId(), orgId, monthBegin);
								  
							} catch (Exception e) {
								LOGGER.error("#####refresh day log error!! userid:["+member.getTargetId()
										+"] organization id :["+orgId+"] ");
								LOGGER.error(e.getLocalizedMessage());
								 
								e.printStackTrace();
							}
						}
					}
				}
				monthBegin.add(Calendar.DAY_OF_MONTH, 1);
				if (monthBegin.after(monthEnd)) {
					return  ;
				}
			} 
		} catch (ParseException e) {
			throw RuntimeErrorException
			.errorWith(PunchServiceErrorCode.SCOPE,
					PunchServiceErrorCode.ERROR_QUERY_YEAR_ERROR,
					"there is something wrong with queryYear,please check again ");
		}
		
	}
	/**
	 * 每天早上5点10分刷打卡记录
	 * */
	@Scheduled(cron = "0 10 5 * * ?")
	@Override
	public void dayRefreshLogScheduled(){
		//刷新前一天的
		Calendar punCalendar = Calendar.getInstance();
		punCalendar.add(Calendar.DATE, -1);
		//取所有设置了rule的公司
		Calendar startCalendar =Calendar.getInstance();

 
		
		List<Long> orgIds = this.punchProvider.queryPunchOrganizationsFromRules();
		for(Long orgId : orgIds){

			List<String> groupTypeList = new ArrayList<String>();
			groupTypeList.add(OrganizationGroupType.ENTERPRISE.getCode());
			groupTypeList.add(OrganizationGroupType.DEPARTMENT.getCode());
			List<OrganizationMemberDTO> organizationMembers = organizationService.listAllChildOrganizationPersonnel
					(orgId, groupTypeList, null) ;
			//循环刷所有员工
			for(OrganizationMemberDTO member : organizationMembers){
				if(member.getTargetType().equals(OrganizationMemberTargetType.USER.getCode()) && null != member.getTargetId()){
					try {
						//刷新 daylog
						this.refreshPunchDayLog(member.getTargetId(), orgId, punCalendar);
						//刷月报
						
						//从计算日的月初计算到计算日当天
						startCalendar.setTime(punCalendar.getTime());
						startCalendar.set(Calendar.DAY_OF_MONTH, 1);
						
						addPunchStatistics(member, orgId, startCalendar, punCalendar);
					} catch (Exception e) {
						LOGGER.error("#####refresh day log error!! userid:["+member.getTargetId()
								+"] organization id :["+orgId+"] ");
						LOGGER.error(e.getLocalizedMessage());
						 
						e.printStackTrace();
					}
				}
			}
		}
	}
	@Override
	public void deletePunchRuleMap(DeletePunchRuleMapCommand cmd) {
		if (null == cmd.getOwnerId() ||null == cmd.getOwnerType()) {
			LOGGER.error("Invalid owner type or  Id parameter in the command");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid owner type or  Id parameter in the command");
		} 
		if (null == cmd.getId() ) {
			LOGGER.error("Invalid   Id parameter in the command");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid   Id parameter in the command");
		}
		PunchRuleOwnerMap obj = this.punchProvider.getPunchRuleOwnerMapById(cmd.getId());
		if(obj.getOwnerId().equals(cmd.getOwnerId())&&obj.getOwnerType().equals(cmd.getOwnerType()))
			if(null != cmd.getTargetId() && null != cmd.getTargetType()){
				if(cmd.getTargetId().equals(obj.getTargetId())&& cmd.getTargetType().equals(obj.getTargetType()))
					this.punchProvider.deletePunchRuleOwnerMap(obj);
				else{
 
					LOGGER.error("Invalid target type or  Id parameter in the command");
					throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,
							"Invalid   target type or  Id parameter in the command");
				 
				}
			}
			else
				this.punchProvider.deletePunchRuleOwnerMap(obj);
		else{

			LOGGER.error("Invalid owner type or  Id parameter in the command");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid owner type or  Id parameter in the command");
		}
	

	}
	
}
