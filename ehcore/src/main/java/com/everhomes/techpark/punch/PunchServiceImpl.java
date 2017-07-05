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
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import com.everhomes.scheduler.RunningFlag;
import org.apache.lucene.spatial.geohash.GeoHashUtils;
import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.web.multipart.MultipartFile;

import com.everhomes.approval.ApprovalCategory;
import com.everhomes.approval.ApprovalCategoryProvider;
import com.everhomes.approval.ApprovalDayActualTimeProvider;
import com.everhomes.approval.ApprovalRangeStatistic;
import com.everhomes.approval.ApprovalRangeStatisticProvider;
import com.everhomes.approval.ApprovalRequestDefaultHandler;
import com.everhomes.approval.ApprovalRequestProvider;
import com.everhomes.approval.ApprovalRule;
import com.everhomes.approval.ApprovalRuleProvider;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.enterprise.EnterpriseContactProvider;
import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowCaseProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.locale.LocaleString;
import com.everhomes.locale.LocaleStringProvider;
import com.everhomes.messaging.MessagingService;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationMemberLog;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.approval.ApprovalType;
import com.everhomes.rest.flow.FlowUserType;
import com.everhomes.rest.messaging.MessageBodyType;
import com.everhomes.rest.messaging.MessageChannel;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.messaging.MessagingConstants;
import com.everhomes.rest.organization.ListOrganizationContactCommand;
import com.everhomes.rest.organization.ListOrganizationMemberCommandResponse;
import com.everhomes.rest.organization.OperationType;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.organization.OrganizationGroupType;
import com.everhomes.rest.organization.OrganizationMemberDTO;
import com.everhomes.rest.organization.OrganizationMemberStatus;
import com.everhomes.rest.organization.OrganizationMemberTargetType;
import com.everhomes.rest.techpark.punch.AddPunchExceptionRequestCommand;
import com.everhomes.rest.techpark.punch.AddPunchRuleCommand;
import com.everhomes.rest.techpark.punch.ApprovalPunchExceptionCommand;
import com.everhomes.rest.techpark.punch.ApprovalStatus;
import com.everhomes.rest.techpark.punch.CheckPunchAdminCommand;
import com.everhomes.rest.techpark.punch.CheckPunchAdminResponse;
import com.everhomes.rest.techpark.punch.ClockCode;
import com.everhomes.rest.techpark.punch.DateStatus;
import com.everhomes.rest.techpark.punch.DeletePunchRuleCommand;
import com.everhomes.rest.techpark.punch.ExceptionProcessStatus;
import com.everhomes.rest.techpark.punch.ExceptionStatus;
import com.everhomes.rest.techpark.punch.ExtDTO;
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
import com.everhomes.rest.techpark.punch.PunchUserStatus;
import com.everhomes.rest.techpark.punch.UpdatePunchRuleCommand;
import com.everhomes.rest.techpark.punch.ViewFlags;
import com.everhomes.rest.techpark.punch.admin.AddPunchPointCommand;
import com.everhomes.rest.techpark.punch.admin.AddPunchTimeRuleCommand;
import com.everhomes.rest.techpark.punch.admin.AddPunchWiFiCommand;
import com.everhomes.rest.techpark.punch.admin.DeleteCommonCommand;
import com.everhomes.rest.techpark.punch.admin.DeletePunchRuleMapCommand;
import com.everhomes.rest.techpark.punch.admin.GetTargetPunchAllRuleCommand;
import com.everhomes.rest.techpark.punch.admin.GetTargetPunchAllRuleResponse;
import com.everhomes.rest.techpark.punch.admin.ListPunchDetailsCommand;
import com.everhomes.rest.techpark.punch.admin.ListPunchDetailsResponse;
import com.everhomes.rest.techpark.punch.admin.ListPunchMonthLogsCommand;
import com.everhomes.rest.techpark.punch.admin.ListPunchMonthLogsResponse;
import com.everhomes.rest.techpark.punch.admin.ListPunchPointsCommand;
import com.everhomes.rest.techpark.punch.admin.ListPunchPointsResponse;
import com.everhomes.rest.techpark.punch.admin.ListPunchRuleMapsCommand;
import com.everhomes.rest.techpark.punch.admin.ListPunchRuleMapsResponse;
import com.everhomes.rest.techpark.punch.admin.ListPunchRulesCommonCommand;
import com.everhomes.rest.techpark.punch.admin.ListPunchRulesResponse;
import com.everhomes.rest.techpark.punch.admin.ListPunchSchedulingMonthCommand;
import com.everhomes.rest.techpark.punch.admin.ListPunchSchedulingMonthResponse;
import com.everhomes.rest.techpark.punch.admin.ListPunchWiFiRuleListResponse;
import com.everhomes.rest.techpark.punch.admin.ListPunchWiFisResponse;
import com.everhomes.rest.techpark.punch.admin.ListPunchWorkdayRuleListResponse;
import com.everhomes.rest.techpark.punch.admin.PunchDayDetailDTO;
import com.everhomes.rest.techpark.punch.admin.PunchLocationRuleDTO;
import com.everhomes.rest.techpark.punch.admin.PunchSchedulingDTO;
import com.everhomes.rest.techpark.punch.admin.PunchWiFiDTO;
import com.everhomes.rest.techpark.punch.admin.PunchWiFiRuleDTO;
import com.everhomes.rest.techpark.punch.admin.PunchWorkdayRuleDTO;
import com.everhomes.rest.techpark.punch.admin.QryPunchLocationRuleListResponse;
import com.everhomes.rest.techpark.punch.admin.UpdatePunchPointCommand;
import com.everhomes.rest.techpark.punch.admin.UpdatePunchSchedulingMonthCommand;
import com.everhomes.rest.techpark.punch.admin.UpdatePunchTimeRuleCommand;
import com.everhomes.rest.techpark.punch.admin.UpdateTargetPunchAllRuleCommand;
import com.everhomes.rest.techpark.punch.admin.UserMonthLogsDTO;
import com.everhomes.rest.techpark.punch.admin.listPunchTimeRuleListResponse;
import com.everhomes.rest.ui.user.ContactSignUpStatus;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.rest.user.MessageChannelType;
import com.everhomes.scheduler.ScheduleProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.user.admin.SystemUserPrivilegeMgr;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import com.everhomes.util.WebTokenGenerator;
import com.everhomes.util.excel.RowResult;
import com.everhomes.util.excel.handler.PropMrgOwnerHandler;

@Service
public class PunchServiceImpl implements PunchService {
	final String downloadDir ="download/";
 
	private MessagingService messagingService;
	private static final Logger LOGGER = LoggerFactory
			.getLogger(PunchServiceImpl.class);
	@Autowired
	private FlowCaseProvider flowCaseProvider;
    private static ThreadLocal<SimpleDateFormat> dateSF = new ThreadLocal<SimpleDateFormat>(){
    	protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    }; 
    private static ThreadLocal<SimpleDateFormat> timeSF = new ThreadLocal<SimpleDateFormat>(){
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("HH:mm:ss");
        }
    }; 
    private static ThreadLocal<SimpleDateFormat> monthSF = new ThreadLocal<SimpleDateFormat>(){
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyyMM");
        }
    }; 
    private static ThreadLocal<SimpleDateFormat> datetimeSF = new ThreadLocal<SimpleDateFormat>(){
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };
    private static ThreadLocal<List<PunchTimeRule>> targetTimeRules = new ThreadLocal<List<PunchTimeRule>>() ;

	@Autowired
	private ScheduleProvider scheduleProvider;
	
	@Autowired
	private PunchProvider punchProvider;
	@Autowired
	private PunchSchedulingProvider punchSchedulingProvider;
	@Autowired
	private UserProvider userProvider;
	@Autowired
	private ApprovalRangeStatisticProvider approvalRangeStatisticProvider;
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
	private LocaleStringProvider localeStringProvider;
    
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
	@Override
	public String statusToString (Byte status){
		if(null == status){
			return "";
		}
		LocaleString localeString = localeStringProvider.find( PunchConstants.PUNCH_STATUS_SCOPE, status.toString(),
				UserContext.current().getUser().getLocale());
		if(null == localeString)
			return "";
		return localeString.getText();
//		if(status.equals(ApprovalStatus.FORGOT.getCode()))
//			return "忘打卡";
//		if(status.equals(ApprovalStatus.HALFABSENCE.getCode()))
//			return "半天事假";
//		if(status.equals(ApprovalStatus.HALFEXCHANGE.getCode()))
//			return "半天调休";
//		if(status.equals(ApprovalStatus.HALFOUTWORK.getCode()))
//			return "半天公出";
//		if(status.equals(ApprovalStatus.HALFSICK.getCode()))
//			return "半天病假";
//		if(status.equals(ApprovalStatus.OVERTIME.getCode()))
//			return "加班";
//		if(status.equals(ApprovalStatus.ABSENCE.getCode()))
//			return "事假";
//		if(status.equals(ApprovalStatus.EXCHANGE.getCode()))
//			return "调休";
//		if(status.equals(ApprovalStatus.OUTWORK.getCode()))
//			return "公出";
//		if(status.equals(ApprovalStatus.SICK.getCode()))
//			return "病假";
//		if(status.equals(ApprovalStatus.BELATE.getCode()))
//			return "迟到";
//		if(status.equals(ApprovalStatus.BLANDLE.getCode()))
//			return "迟到且早退";
//		if(status.equals(ApprovalStatus.UNPUNCH.getCode()))
//			return "缺勤";
//		if(status.equals(ApprovalStatus.LEAVEEARLY.getCode()))
//			return "早退";
//		if(status.equals(ApprovalStatus.NORMAL.getCode()))
//			return "正常";
//		return "";
		
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
			start.setTime(dateSF.get().parse(cmd.getQueryYear() + "-01-01"));
			if (end.before(start)) {
				throw RuntimeErrorException.errorWith(
						PunchServiceErrorCode.SCOPE,
						PunchServiceErrorCode.ERROR_QUERY_YEAR_ERROR,
						"query Year is later than now ,please check again ");
			}
			if (start.get(Calendar.YEAR) != end.get(Calendar.YEAR)) {
				end.setTime(dateSF.get().parse(cmd.getQueryYear() + "-01-01"));
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
		// userId, cmd.getEnterpriseId(), dateSF.get().format(start.getTime()),
		// dateSF.get().format(end.getTime()));

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

//			Date date = start.getTime();
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
				companyId, dateSF.get().format(logDay.getTime()));
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
//		Date now = new Date();
		if(punchDayLog.getPunchTimesPerDay().equals(PunchTimesPerDay.TWICE.getCode())){
			pdl.setPunchStatus(punchDayLog.getStatus());
			 
			pdl.setExceptionStatus(punchDayLog.getStatus().equals(
						PunchStatus.NORMAL.getCode())||punchDayLog.getStatus().equals(
								PunchStatus.OVERTIME.getCode())  ? ExceptionStatus.NORMAL
						.getCode() : ExceptionStatus.EXCEPTION.getCode());
			 
			PunchExceptionApproval exceptionApproval = punchProvider
					.getPunchExceptionApprovalByDate(userId, companyId,
							dateSF.get().format(logDay.getTime()));
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
							dateSF.get().format(logDay.getTime()));
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
		// LOGGER.debug("************* "+dateSF.get().format(logDay.getTime())+"refreshPunchDayLog time : ["+(endrefreshPunchDayLogTimeLong-beginFunctionTimeLong)
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


	private PunchDayLog refreshPunchDayLog(Long userId, Long companyId1,
			Calendar logDay) throws ParseException {
		Long companyId = getTopEnterpriseId(companyId1);
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
		newPunchDayLog.setPunchDate(java.sql.Date.valueOf(dateSF.get().format(logDay
				.getTime())));
		newPunchDayLog.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
				.getTime()));
		newPunchDayLog.setPunchTimesPerDay(pdl.getPunchTimesPerDay());
		newPunchDayLog.setStatus(pdl.getPunchStatus());
		newPunchDayLog.setMorningStatus(pdl.getMorningPunchStatus());
		newPunchDayLog.setAfternoonStatus(pdl.getAfternoonPunchStatus());
		newPunchDayLog.setViewFlag(ViewFlags.NOTVIEW.getCode());
		newPunchDayLog.setExceptionStatus(pdl.getExceptionStatus());
		newPunchDayLog.setDeviceChangeFlag(getDeviceChangeFlag(userId,java.sql.Date.valueOf(dateSF.get().format(logDay
				.getTime())),companyId));
		PunchDayLog punchDayLog = punchProvider.getDayPunchLogByDate(userId,
				companyId, dateSF.get().format(logDay.getTime()));
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
 
	private Byte getDeviceChangeFlag(Long userId, java.sql.Date punchDate,
			Long companyId) {
		// TODO Auto-generated method stub
		Calendar cal = Calendar.getInstance();
		cal.setTime(punchDate);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		Integer  count = this.punchProvider.countPunchLogDevice(userId,companyId,new java.sql.Date(cal.getTime().getTime()),punchDate);
		if(count >1)
			return NormalFlag.YES.getCode();
		return NormalFlag.NO.getCode(); 	
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
	 * @param userId
	 *            ： 打卡用户;
	 * @param companyId
	 *            :打卡规则
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
				companyId, dateSF.get().format(logDay.getTime()));
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
				companyId, dateSF.get().format(logDay.getTime()),
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
				|| dateSF.get().format(now).equals(dateSF.get().format(logDay.getTime()))) {
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
				dateSF.get().format(logDay.getTime()));
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
						dateSF.get().format(logDay.getTime()));
		if (exceptionRequests.size() > 0) {
			for (PunchExceptionRequest exceptionRequest : exceptionRequests) {
				//是否有请求的flag
				if(exceptionRequest.getApprovalStatus() != null ){
					pdl.setRequestFlag(NormalFlag.YES.getCode());
					FlowCase flowCase = flowCaseProvider.findFlowCaseByReferId(exceptionRequest.getRequestId(), ApprovalRequestDefaultHandler.REFER_TYPE ,PunchConstants.PUNCH_MODULE_ID);
					if(null != flowCase)
						pdl.setRequestToken(ApprovalRequestDefaultHandler.processFlowURL(flowCase.getId(), FlowUserType.APPLIER.getCode(), flowCase.getModuleId()));
					else
						pdl.setRequestToken(WebTokenGenerator.getInstance().toWebToken(exceptionRequest.getRequestId()));
				}
				if(exceptionRequest.getMorningApprovalStatus() != null ){
					pdl.setMorningRequestFlag(NormalFlag.YES.getCode());
					FlowCase flowCase = flowCaseProvider.findFlowCaseByReferId(exceptionRequest.getRequestId(), ApprovalRequestDefaultHandler.REFER_TYPE ,PunchConstants.PUNCH_MODULE_ID);
					if(null != flowCase)
						pdl.setMorningRequestToken(ApprovalRequestDefaultHandler.processFlowURL(flowCase.getId(), FlowUserType.APPLIER.getCode(), flowCase.getModuleId()));
					else
						pdl.setMorningRequestToken(WebTokenGenerator.getInstance().toWebToken(exceptionRequest.getRequestId()));
				}
				if(exceptionRequest.getAfternoonApprovalStatus() != null ){
					pdl.setAfternoonRequestFlag(NormalFlag.YES.getCode());
					FlowCase flowCase = flowCaseProvider.findFlowCaseByReferId(exceptionRequest.getRequestId(), ApprovalRequestDefaultHandler.REFER_TYPE ,PunchConstants.PUNCH_MODULE_ID);
					if(null != flowCase)
						pdl.setAfternoonRequestToken(ApprovalRequestDefaultHandler.processFlowURL(flowCase.getId(), FlowUserType.APPLIER.getCode(), flowCase.getModuleId()));
					else
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
	 * @param companyId 永远为总公司id
	 * 
	 * */
	private PunchLogsDay calculateDayLog(Long userId, Long companyId,
			Calendar logDay, PunchLogsDay pdl, PunchDayLog punchDayLog) throws ParseException {
		 
		List<PunchLog> punchLogs = punchProvider.listPunchLogsByDate(userId,
				companyId, dateSF.get().format(logDay.getTime()),
				ClockCode.SUCESS.getCode());
		if(null != punchLogs){
			for (PunchLog log : punchLogs){
				pdl.getPunchLogs().add(ConvertHelper.convert(log,PunchLogDTO.class ));
			}
		}
//		Date now = new Date();
		PunchRule pr = this.getPunchRule(PunchOwnerType.ORGANIZATION.getCode(),companyId, userId );
		if(null == pr)
			throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
					PunchServiceErrorCode.ERROR_ENTERPRISE_DIDNOT_SETTING,
					"have no punch rule");

		PunchTimeRule punchTimeRule = getPunchTimeRuleByRuleIdAndDate(pr.getId(),logDay.getTime());
		//没有规则就是没有排班,就是非工作日
		if (null == punchTimeRule){
			pdl.setPunchTimesPerDay(PunchTimesPerDay.TWICE.getCode());
			pdl.setPunchStatus(PunchStatus.NORMAL.getCode());
			pdl.setMorningPunchStatus(PunchStatus.NORMAL.getCode());
			pdl.setAfternoonPunchStatus(PunchStatus.NORMAL.getCode());
			pdl.setExceptionStatus(ExceptionStatus.NORMAL.getCode()); 
			return pdl;
		}
		pdl.setPunchTimesPerDay(punchTimeRule.getPunchTimesPerDay());
		//对于已离职和未入职的判断 
		List<OrganizationMember> organizationMembers = organizationService.listOrganizationMemberByOrganizationPathAndUserId("/"+companyId,userId );
		if(organizationMembers == null || organizationMembers.size() == 0){
			//找不到就是已离职
			pdl.setPunchTimesPerDay(PunchTimesPerDay.TWICE.getCode());
			punchDayLog.setStatus(ApprovalStatus.RESIGNED.getCode());
			pdl.setMorningPunchStatus(PunchStatus.RESIGNED.getCode());
			pdl.setAfternoonPunchStatus(PunchStatus.RESIGNED.getCode());
			pdl.setPunchStatus(ApprovalStatus.RESIGNED.getCode());
			return pdl;
		}else{
			//查找是否未入职 --通过log的时间
			List<Long> orgIds = new ArrayList<>();
			for(OrganizationMember member : organizationMembers){
				orgIds.add(member.getOrganizationId());
			}
			List<OrganizationMemberLog> memberLogs = organizationProvider.listOrganizationMemberLogs(userId,orgIds, OperationType.JOIN.getCode()) ;
			if (null != memberLogs ){
				if(memberLogs.get(0).getOperateTime().after(logDay.getTime())){

					pdl.setPunchTimesPerDay(PunchTimesPerDay.TWICE.getCode());
					pdl.setMorningPunchStatus(PunchStatus.NONENTRY.getCode());
					pdl.setAfternoonPunchStatus(PunchStatus.NONENTRY.getCode());
					punchDayLog.setStatus(ApprovalStatus.NONENTRY.getCode());
					pdl.setPunchStatus(ApprovalStatus.NONENTRY.getCode());
					return pdl;
				}
			}
			
		}
		// 如果零次打卡记录
		if (null == punchLogs || punchLogs.size() == 0) {
//			if (!isWorkDay(logDay.getTime(),pr)|| dateSF.get().format(now).equals(dateSF.get().format(logDay.getTime()))) {
//				// 如果非工作日或者当天，不增pdl直接下一天
//				return null;
//			} 
			pdl.setPunchStatus(PunchStatus.UNPUNCH.getCode());
			pdl.setMorningPunchStatus(PunchStatus.UNPUNCH.getCode());
			pdl.setAfternoonPunchStatus(PunchStatus.UNPUNCH.getCode());
			pdl.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode()); 
			makeExceptionForDayList(userId, companyId, logDay, pdl);
			return pdl;
		}

		//非工作日按照两次计算工作时长
		if(!isWorkDay(logDay.getTime(),pr) || PunchTimesPerDay.TWICE.getCode().equals(punchTimeRule.getPunchTimesPerDay())){
			if (punchLogs.size() == 1) {
				// 如果只有一次打卡
				punchDayLog.setArriveTime(getDAOTime(punchLogs.get(0).getPunchTime().getTime()));
				PunchLogDTO arriveLogDTO = new PunchLogDTO();
				arriveLogDTO.setPunchTime(punchLogs.get(0).getPunchTime().getTime());
				pdl.getPunchLogs().add(arriveLogDTO);
				PunchLogDTO noPunchLogDTO2 = new PunchLogDTO();
				pdl.getPunchLogs().add(noPunchLogDTO2);
//				if (!isWorkDay(logDay.getTime(),pr)){
//					// 如果非工作日 NORMAL
//					pdl.setPunchStatus(PunchStatus.NORMAL.getCode()); 
//					pdl.setMorningPunchStatus(PunchStatus.NORMAL.getCode());
//					pdl.setAfternoonPunchStatus(PunchStatus.NORMAL.getCode());
//					pdl.setExceptionStatus(ExceptionStatus.NORMAL.getCode());
//					return pdl;
//				}
//				if (dateSF.get().format(now).equals(
//								dateSF.get().format(logDay.getTime()))) {
//					//今天的话,exception
//					pdl.setPunchStatus(PunchStatus.FORGOT.getCode());
//					pdl.setExceptionStatus(ExceptionStatus.NORMAL.getCode());
//				 
//					return pdl;
//				}
//				//工作日忘打卡
//				pdl.setPunchStatus(PunchStatus.FORGOT.getCode());
//				pdl.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
//				makeExceptionForDayList(userId, companyId, logDay, pdl);
//				return pdl;
			}
			
			Calendar startMinTime = Calendar.getInstance();
			Calendar startMaxTime = Calendar.getInstance();
			Calendar workTime = Calendar.getInstance();
			startMinTime.setTimeInMillis(punchLogs.get(0).getPunchDate().getTime()+ punchTimeRule.getStartEarlyTimeLong());

			startMaxTime.setTimeInMillis(punchLogs.get(0).getPunchDate().getTime()+ punchTimeRule.getStartLateTimeLong());

			workTime.setTimeInMillis(punchLogs.get(0).getPunchDate().getTime() + punchTimeRule.getWorkTimeLong());
			List<Calendar> punchMinAndMaxTime = getMinAndMaxTimeFromPunchlogs(punchLogs);
			Calendar arriveCalendar = punchMinAndMaxTime.get(0);
			Calendar leaveCalendar = punchMinAndMaxTime.get(1);
//			Time leaveCalendarTime = Time.valueOf(timeSF.get().format(leaveCalendar.getTime()));
//			Time arriveCalendarTime = Time.valueOf(timeSF.get().format(arriveCalendar.getTime()));
			Calendar AfternoonArriveCalendar = Calendar.getInstance();
			AfternoonArriveCalendar.setTimeInMillis((logDay.getTimeInMillis()+
					(punchTimeRule.getAfternoonArriveTimeLong()==null?convertTimeToGMTMillisecond(punchTimeRule.getAfternoonArriveTime()):punchTimeRule.getAfternoonArriveTimeLong())));
			Calendar NoonLeaveTimeCalendar = Calendar.getInstance();
			NoonLeaveTimeCalendar.setTimeInMillis((logDay.getTimeInMillis()+
					(punchTimeRule.getNoonLeaveTimeLong()==null?convertTimeToGMTMillisecond(punchTimeRule.getNoonLeaveTime()):punchTimeRule.getNoonLeaveTimeLong())));
			 
			long realWorkTime = 0L;  
			if(leaveCalendar.after(AfternoonArriveCalendar)&&arriveCalendar.before(NoonLeaveTimeCalendar)){ 
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
				pdl.setPunchStatus(PunchStatus.NORMAL.getCode());
				pdl.setMorningPunchStatus(PunchStatus.NORMAL.getCode());
				pdl.setAfternoonPunchStatus(PunchStatus.NORMAL.getCode());
				pdl.setExceptionStatus(ExceptionStatus.NORMAL.getCode()); 
				return pdl;
			}
		}
		else if(PunchTimesPerDay.FORTH.getCode().equals(punchTimeRule.getPunchTimesPerDay())){
			Calendar noonAnchor = Calendar.getInstance();
			//中间分界点是中午下班和下午上班中间时间点
			noonAnchor.setTime(getDAOTime(punchLogs.get(0).getPunchDate().getTime()+
					(punchTimeRule.getNoonLeaveTimeLong()+punchTimeRule.getAfternoonArriveTimeLong())/2));
			
//			//计算计算日中午十三点
//			noonAnchor.setTime(logDay.getTime());
//			noonAnchor.set(Calendar.HOUR_OF_DAY, 13);
//			noonAnchor.set(Calendar.MINUTE, 0);
//			noonAnchor.set(Calendar.SECOND, 0);
//			noonAnchor.set(Calendar.MILLISECOND, 0);
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
 
//			// 如果上午1次打卡记录
//			if ( morningLogs.size() == 1) {
//				punchDayLog.setArriveTime(getDAOTime(morningLogs.get(0).getPunchTime().getTime()));
//				punchDayLog.setWorkTime(convertTime(0L));
//				pdl.setMorningPunchStatus(PunchStatus.FORGOT.getCode());
//				pdl.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode()); 
//			}
//			// 如果下午1次打卡记录
//			if ( afternoonLogs.size() == 1) {
//				punchDayLog.setAfternoonArriveTime(getDAOTime(afternoonLogs.get(0).getPunchTime().getTime()));
//				punchDayLog.setWorkTime(convertTime(0L));
//				pdl.setAfternoonPunchStatus(PunchStatus.FORGOT.getCode());
//				pdl.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode()); 	
//			}
			Calendar startMinTime = Calendar.getInstance();
			Calendar startMaxTime = Calendar.getInstance();
			Calendar noonLeaveTime = Calendar.getInstance();
			Calendar afternoonArriveTime = Calendar.getInstance();
			Calendar workTime = Calendar.getInstance();
			Calendar arriveCalendar = Calendar.getInstance();
			startMinTime.setTimeInMillis(punchLogs.get(0).getPunchDate().getTime() + punchTimeRule.getStartEarlyTimeLong());
			startMaxTime.setTimeInMillis(punchLogs.get(0).getPunchDate().getTime() + punchTimeRule.getStartLateTimeLong());
					 
			noonLeaveTime.setTimeInMillis(punchLogs.get(0).getPunchDate().getTime() + punchTimeRule.getNoonLeaveTimeLong());
			 
			afternoonArriveTime.setTimeInMillis(punchLogs.get(0).getPunchDate().getTime() + punchTimeRule.getAfternoonArriveTimeLong());
 
			workTime.setTimeInMillis(punchLogs.get(0).getPunchDate().getTime() + punchTimeRule.getWorkTimeLong());
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
				pdl.setMorningPunchStatus(PunchStatus.NORMAL.getCode());
				pdl.setAfternoonPunchStatus(PunchStatus.NORMAL.getCode());
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
						dateSF.get().format(logDay.getTime()));
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
						dateSF.get().format(logDay.getTime()));
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
		String punchTime = datetimeSF.get().format(new Date());
		return createPunchLog(cmd,punchTime);
		
	}

	private PunchClockResponse createPunchLog(PunchClockCommand cmd, String punchTime) {
		//
		byte punchCode;
		try{
			punchCode= verifyPunchClock(cmd).getCode();
		}catch(Exception e){	
			//有报错就表示不成功
			PunchLog punchLog = ConvertHelper.convert(cmd, PunchLog.class);
			punchLog.setPunchStatus(ClockCode.FAIL.getCode());
			punchProvider.createPunchLog(punchLog);
			throw e;
		} 
		return createPunchLog(cmd, punchTime,  punchCode);
	}
	private PunchClockResponse createPunchLog(PunchClockCommand cmd, String punchTime,
			byte punchCode) {

		checkCompanyIdIsNull(cmd.getEnterpriseId());
		cmd.setEnterpriseId(getTopEnterpriseId(cmd.getEnterpriseId()));
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
		PunchLog punchLog = ConvertHelper.convert(cmd, PunchLog.class);
		punchLog.setUserId(userId);
		
		punchLog.setPunchTime(Timestamp.valueOf(punchTime));

		Calendar punCalendar = Calendar.getInstance();
		try {
			punCalendar.setTime(datetimeSF.get().parse(punchTime));
		} catch (ParseException e) {
            LOGGER.error("parse punchTime error punch Time = " + punchTime,e);
		}
		PunchRule pr = getPunchRule(PunchOwnerType.ORGANIZATION.getCode(), cmd.getEnterpriseId(), userId);
		if (null == pr  )
			throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
 					PunchServiceErrorCode.ERROR_ENTERPRISE_DIDNOT_SETTING,
 				"公司没有设置打卡规则");
		

		//把当天的时分秒转换成Long型
		Long punchTimeLong = punCalendar.get(Calendar.HOUR_OF_DAY)*3600*1000L; //hour
		punchTimeLong += punCalendar.get(Calendar.MINUTE)*60*1000L; //min
		punchTimeLong += punCalendar.get(Calendar.SECOND)*1000L;//second
		
//				punchProvider.getPunchTimeRuleById(pr.getTimeRuleId());
		PunchTimeRule ptr = getPunchTimeRuleByRuleIdAndDate(pr.getId(),punCalendar.getTime());
		 
		Calendar yesterday = Calendar.getInstance(); 
		yesterday.setTime(punCalendar.getTime());
		yesterday.add(Calendar.DATE, -1);
		PunchTimeRule yesterdayPtr = getPunchTimeRuleByRuleIdAndDate(pr.getId(),yesterday.getTime());
		//默认分界点是次日5点,如果timerule有设置就用设置的
		Long splitTime = 86400000+5*3600*1000L;
		Long yesterdaySplitTime = 86400000+ 5*3600*1000L;
		
		
		if(null != ptr && null != ptr.getDaySplitTimeLong())
			splitTime = ptr.getDaySplitTimeLong();
		else if(null != ptr && null != ptr.getDaySplitTime())
			splitTime = convertTimeToGMTMillisecond(ptr.getDaySplitTime());
		

		if(null != yesterdayPtr && null != yesterdayPtr.getDaySplitTimeLong())
			yesterdaySplitTime = yesterdayPtr.getDaySplitTimeLong();
		else if(null != yesterdayPtr && null != yesterdayPtr.getDaySplitTime())
			yesterdaySplitTime = convertTimeToGMTMillisecond(yesterdayPtr.getDaySplitTime());
        //TODO: 用日期来处理
		if ( punchTimeLong+86400000 < yesterdaySplitTime) {
			//取前一天的ptr,如果周期分界点>打卡时间+86400000 则算前一天
			punCalendar.setTime(yesterday.getTime());
		}
		else if (punchTimeLong.compareTo(splitTime)>0) {
			//取今天的ptr,如果周期分界点<打卡时间 则算后一天
			punCalendar.add(Calendar.DATE, 1);
		}
		//其它算当天
		
		punchLog.setPunchDate(java.sql.Date.valueOf(dateSF.get().format(punCalendar
				.getTime())));
		response.setPunchCode(punchCode);
		punchLog.setPunchStatus(punchCode);
		punchProvider.createPunchLog(punchLog);
//		//刷新这一天的数据
//		this.coordinationProvider.getNamedLock(CoordinationLocks.CREATE_PUNCH_LOG.getCode()).enter(()-> {
		    this.dbProvider.execute((status) -> {
		    	try {
					refreshPunchDayLog(userId, cmd.getEnterpriseId(), punCalendar);
				} catch (Exception e) {
					LOGGER.error(e.toString(),e);
					throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
							PunchServiceErrorCode.ERROR_PUNCH_ADD_DAYLOG,
							"Something wrong with refresh PunchDayLog");

				}
		        return null;
		    });
//		    return null;
//		});
		response.setPunchTime(punchTime);

		return response;
	}
	private ClockCode verifyPunchClock(PunchClockCommand cmd) {
		//获取打卡规则
//		ClockCode code = ClockCode.SUCESS;
		Long userId = UserContext.current().getUser().getId(); 
		PunchRule pr = getPunchRule(PunchOwnerType.ORGANIZATION.getCode(), cmd.getEnterpriseId(), userId);
		if (null == pr  )
			throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
 					PunchServiceErrorCode.ERROR_ENTERPRISE_DIDNOT_SETTING,
 				"公司没有设置打卡规则");
		//是否有wifi打卡,如果是判断wifi是否符合
		
		List<PunchWifi> wifis = this.punchProvider.listPunchWifisByRuleId(PunchOwnerType.ORGANIZATION.getCode(), cmd.getEnterpriseId(), pr.getWifiRuleId()) ;
		if(null != wifis && null != cmd.getWifiMac()){
			for(PunchWifi wifi : wifis){
				if(null != wifi.getMacAddress() && wifi.getMacAddress().toLowerCase().equals(cmd.getWifiMac().toLowerCase()))
					return ClockCode.SUCESS;
			}
			
		}
		
		//参数有地址规则看地址范围是否正确,不正确则报错
		List<PunchGeopoint> punchGeopoints = punchProvider
				.listPunchGeopointsByRuleId(PunchOwnerType.ORGANIZATION.getCode(), cmd.getEnterpriseId(),pr.getLocationRuleId());
		if(null == punchGeopoints || punchGeopoints.size() == 0){
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
		 
		for (PunchGeopoint punchGeopoint : punchGeopoints) {
			if (calculateDistance(cmd.getLongitude(), cmd.getLatitude(),
					punchGeopoint.getLongitude(), punchGeopoint.getLatitude()) <= punchGeopoint
					.getDistance()) {
				// 如果找到了一个就跳出
				return ClockCode.SUCESS; 
			} 
		 
		}
		if(null == wifis || wifis.size() == 0)
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
//	
//	private void createPunchGeopoints(Long userId, List<PunchGeoPointDTO> punchGeoPoints,
//		String ownerType,	Long ownerId) { 
//		for (PunchGeoPointDTO punchGeopointDTO : punchGeoPoints) {
//			PunchGeopoint punchGeopoint = ConvertHelper.convert(punchGeopointDTO, PunchGeopoint.class);
//			punchGeopoint.setOwnerType(ownerType);
//			punchGeopoint.setOwnerId(ownerId); 
//			punchGeopoint.setCreatorUid(userId);
//			punchGeopoint.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//			punchGeopoint.setGeohash(GeoHashUtils.encode(
//					punchGeopoint.getLatitude(), punchGeopoint.getLongitude()));
//			punchProvider.createPunchGeopoint(punchGeopoint);
//		}
//	}

	private void convertTime(PunchTimeRule punchRule, Long startEarlyTime,
			Long startLastTime, Long endEarlyTime) {
		Time startEarly = convertTime(startEarlyTime);
		punchRule.setStartEarlyTime(startEarly);
		punchRule.setStartLateTime(convertTime(startLastTime));
		Time endEarly = convertTime(endEarlyTime);
		Long workTime = endEarly.getTime() - startEarly.getTime();

		punchRule.setWorkTimeLong(workTime);
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

//	private Time convertTime(String TimeStr) {
//		if (!StringUtils.isEmpty(TimeStr)) {
//			return Time.valueOf(TimeStr);
//		}
//		return null;
//	}
	private Time convertTime(Long TimeLong) {
		if (null != TimeLong) {
			//从8点开始计算
			return new Time(TimeLong-MILLISECONDGMT);
		}
		return null;
	}
	@Override
	public Long convertTimeToGMTMillisecond(Time time) {
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

//	private String calculateEndTime(String dateFormat, String startEarlyTime,
//			String workTime) {
//		DateFormat format = new SimpleDateFormat(dateFormat);
//		format.setTimeZone(TimeZone.getTimeZone("GMT"));// 设置
//														// DateFormat的时间区域为GMT
//
//		long endTime = 0;
//		try {
//			endTime = format.parse(startEarlyTime).getTime()
//					+ format.parse(workTime).getTime();
//		} catch (ParseException e) {
//			LOGGER.error("the time format is error.", e);
//		}
//		return getGMTtimeString("HH:mm;ss", endTime);
//	}

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
	private Integer countWorkDayCount(Calendar startCalendar, Calendar endCalendar ,PunchStatistic statistic) {
//		Integer workDayCount = 0;
//		Calendar calendar = Calendar.getInstance();
//		calendar.setTime(startCalendar.getTime());
//		while (true) {
//			if (isWorkDay(calendar.getTime(),pr)) {
//				workDayCount++;
//			}
//			calendar.add(Calendar.DAY_OF_MONTH, 1);
//			if (calendar.after(endCalendar)) {
//				return workDayCount;
//			}
//		} 
		
		PunchRuleOwnerMap ruleMap = getPunchRuleMap(statistic.getOwnerType(), statistic.getOwnerId(), statistic.getUserId());
		if(null == ruleMap)
			return 0;
		List<PunchScheduling> punchSchedulings = punchSchedulingProvider.queryPunchSchedulings(null, Integer.MAX_VALUE,new ListingQueryBuilderCallback()  {
			@Override
			public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
					SelectQuery<? extends Record> query) { 
//				query.addConditions(Tables.EH_PUNCH_SCHEDULINGS.RULE_DATE.gt(new java.sql.Date( startCalendar.getTime()));
				query.addConditions(Tables.EH_PUNCH_SCHEDULINGS.RULE_DATE.greaterOrEqual(new java.sql.Date(startCalendar.getTime().getTime())));
				query.addConditions(Tables.EH_PUNCH_SCHEDULINGS.RULE_DATE.lt(new java.sql.Date( endCalendar.getTime().getTime())));
				query.addConditions(Tables.EH_PUNCH_SCHEDULINGS.TIME_RULE_ID.isNotNull());
				query.addConditions(Tables.EH_PUNCH_SCHEDULINGS.OWNER_TYPE.eq(ruleMap.getOwnerType()));
				query.addConditions(Tables.EH_PUNCH_SCHEDULINGS.OWNER_ID.eq(ruleMap.getOwnerId())); 
				query.addConditions(Tables.EH_PUNCH_SCHEDULINGS.TARGET_TYPE.eq(ruleMap.getTargetType())); 
				query.addConditions(Tables.EH_PUNCH_SCHEDULINGS.TARGET_ID.eq(ruleMap.getTargetId())); 
				query.addOrderBy(Tables.EH_PUNCH_SCHEDULINGS.RULE_DATE.asc());
				return null;
			}
		});
		if(null == punchSchedulings)
			return 0;
		return punchSchedulings.size();
	}

	@Override
	public boolean isWorkDay(Date date1,PunchRule punchRule) {
//		if (date1 == null)
//			return false;
////		SimpleDateFormat dateSF = new SimpleDateFormat("yyyy-MM-dd");
//		int openWeekInt = 111110;
//		if(null != punchRule.getWorkdayRuleId()){
//			// 如果属于周末调班 返回工作日
//			List<PunchHoliday> workDates = this.punchProvider.queryPunchHolidaysByStatus(punchRule.getOwnerType(),punchRule.getOwnerId(),
//					punchRule.getWorkdayRuleId(),DateStatus.WORKDAY.getCode());
//			if (null != workDates) {
//				for (PunchHoliday workDate : workDates) {
//					if (dateSF.get().format(date1).equals(dateSF.get().format(workDate.getRuleDate())))
//						return true;
//				}
//			}
//			// 如果属于工作日休假 返回非工作日
//			List<PunchHoliday> weekenDates = this.punchProvider.queryPunchHolidaysByStatus(punchRule.getOwnerType(),punchRule.getOwnerId(),
//					punchRule.getWorkdayRuleId(),DateStatus.HOLIDAY.getCode());
//			if (null != weekenDates) {
//				for (PunchHoliday weekenDate : weekenDates) {
//					if (dateSF.get().format(date1).equals(dateSF.get().format(weekenDate.getRuleDate())))
//						return false;
//				}
//			}
//			PunchWorkdayRule workdayRule = this.punchProvider.getPunchWorkdayRuleById(punchRule.getWorkdayRuleId());
//			openWeekInt = Integer.valueOf(workdayRule.getWorkWeekDates());
//		}
//		Calendar calendar = Calendar.getInstance();
//		calendar.setTime(date1);
//		 // 获取日期周几
////		Locale.setDefault(Locale.US);
//		Integer weekDay = calendar.get(Calendar.DAY_OF_WEEK)-1;
//		//将七位0111110这样的代码转换成一个存储星期几的list
//		
//		List<Integer> workDays=new ArrayList<Integer>();
//        for(int i=0;i<7;i++){
//        		workDays.add(i);
//        		if(openWeekInt%10 == 1)
//        	openWeekInt = openWeekInt/10;
//        }
//        //如果这个DAY_OF_WEEK 在 工作日list中,则返回真
//        if(workDays.contains(weekDay))
//        	return true;

		PunchTimeRule punchTimeRule = getPunchTimeRuleByRuleIdAndDate(punchRule.getId(),date1);
		if(null != punchTimeRule)
			return true;
        return false;

	}

	private boolean isWorkTime(Time time, PunchTimeRule punchTimeRule) {
		if (time == null || punchTimeRule == null) {
			return false;
		}
		time = Time.valueOf(timeSF.get().format(time));
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
	public boolean isWorkTime(Time time, PunchRule punchRule,Date date) {
		return isWorkTime(time, getPunchTimeRuleByRuleIdAndDate(punchRule.getId(), date)) ;
	} 
	
	@Override
	public boolean isSameDay(Date date1, Date date2) {
		return dateSF.get().format(date1).equals(dateSF.get().format(date2));
	}
	
	@Override
	public boolean isRestTime(Date fromTime, Date endTime, PunchRule punchRule) {
		return isSameDay(fromTime, endTime)
				&& timeSF.get().format(fromTime).equals(timeSF.get().format(getPunchTimeRuleByRuleIdAndDate(punchRule.getId(), fromTime).getNoonLeaveTime()))
				&& timeSF.get().format(endTime).equals(timeSF.get().format(getPunchTimeRuleByRuleIdAndDate(punchRule.getId(), endTime).getAfternoonArriveTime()));
	
	}
	
//	@Override
//	public PunchTimeRule getPunchTimeRule(PunchRule punchRule) {
//		if (punchRule != null && punchRule.getTimeRuleId() != null) {
//			return punchProvider.findPunchTimeRuleById(punchRule.getTimeRuleId());
//		}
//		return null;
//	}

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
							dto.getEnterpriseId(), dateSF.get().format(logDay.getTime()));
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
		List<Organization> departments = organizationProvider.listOrganizationByGroupTypes(organization.getPath() + "%", groupTypes);
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
//		Calendar start = Calendar.getInstance();
		// 月初
		Calendar monthStart = Calendar.getInstance(); 
		monthStart.add(Calendar.DAY_OF_MONTH, -30);
		// 前一天
		Calendar end = Calendar.getInstance();
		end.add(Calendar.DAY_OF_MONTH, -1);
		// 找出异常的记录
		List<PunchDayLog> PunchDayLogs = punchProvider
				.listPunchDayExceptionLogs(userId, cmd.getEnterpriseId(),
						dateSF.get().format(monthStart.getTime()),
						dateSF.get().format(end.getTime()));
		if (PunchDayLogs.size() > 0)
			response.setExceptionCode(ExceptionStatus.EXCEPTION.getCode());
		List<PunchExceptionRequest> exceptionRequests = punchProvider
				.listExceptionNotViewRequests(userId, cmd.getEnterpriseId(),
						dateSF.get().format(monthStart.getTime()),
						dateSF.get().format(end.getTime()));
		if (exceptionRequests.size() > 0)
			response.setExceptionCode(ExceptionStatus.EXCEPTION.getCode());
		return response;
	}

	@Override
	public PunchLogsDay getDayPunchLogs(GetDayPunchLogsCommand cmd) {
		Long userId = UserContext.current().getUser().getId();
		
		checkCompanyIdIsNull(cmd.getEnterpirseId());

		cmd.setEnterpirseId(getTopEnterpriseId(cmd.getEnterpirseId()));
		Calendar logDay = Calendar.getInstance();

		try {

			logDay.setTime(dateSF.get().parse(cmd.getQueryDate()));
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
				dateSF.get().format(logDay.getTime()));
		
		return pdl;
	}
	@Override
	public PunchTimeRule getPunchTimeRuleByRuleIdAndDate(Long ruleId,Date date){
		PunchScheduling punchScheduling = this.punchSchedulingProvider.getPunchSchedulingByRuleDateAndTarget(ruleId, date);
		if(null == punchScheduling || punchScheduling.getPunchRuleId() == null )
			return null ;
		return this.punchProvider.getPunchTimeRuleById(punchScheduling.getTimeRuleId());
	}
	private void addPunchStatistics(OrganizationMemberDTO member ,Long orgId,Calendar startCalendar,Calendar endCalendar){
		
		PunchRule pr = this.getPunchRule(PunchOwnerType.ORGANIZATION.getCode(),orgId, member.getTargetId());
		if(null == pr)
			throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
					PunchServiceErrorCode.ERROR_ENTERPRISE_DIDNOT_SETTING,
					"have no punch rule");
//		PunchTimeRule timeRule = getPunchTimeRuleByRuleIdAndDate(pr.getId(), startCalendar.getTime());
//		PunchScheduling punchScheduling = this.punchSchedulingProvider.getPunchSchedulingByRuleDateAndTarget(member.getTargetId(), startCalendar.getTime());
		PunchStatistic statistic = new PunchStatistic();
		try { 

			//对于已离职和未入职的判断
			statistic.setUserStatus(PunchUserStatus.NORMAL.getCode());
			if(!member.getStatus().equals(OrganizationMemberStatus.ACTIVE.getCode())){
				//找不到就是已离职
				statistic.setUserStatus(PunchUserStatus.RESIGNED.getCode());
			}else{
				//查找是否未入职 --通过log的时间
				List<Long> orgIds = new ArrayList<>();
				orgIds.add(member.getOrganizationId()); 
				List<OrganizationMemberLog> memberLogs = organizationProvider.listOrganizationMemberLogs(member.getTargetId(),orgIds, OperationType.JOIN.getCode()) ;
				if (null != memberLogs ){
					if(memberLogs.get(0).getOperateTime().after( endCalendar.getTime())){

						statistic.setUserStatus(PunchUserStatus.NONENTRY.getCode());
					}
				}
				
			}
	 
			statistic.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
					.getTime()));
			statistic.setPunchMonth(monthSF.get().format(startCalendar.getTime()));
			statistic.setOwnerType(PunchOwnerType.ORGANIZATION.getCode());
	        Long ownerId = getTopEnterpriseId(member.getOrganizationId());
			statistic.setOwnerId(ownerId);
			statistic.setUserId(member.getTargetId());
			Integer workDayCount = countWorkDayCount(startCalendar,endCalendar, statistic );
			
			statistic.setUserName(member.getContactName());
			OrganizationDTO dept = this.findUserDepartment(member.getTargetId(), member.getOrganizationId());   
			statistic.setDeptId(dept.getId());
			statistic.setDeptName(dept.getName());
			statistic.setWorkDayCount(workDayCount);

	 		List<PunchDayLog> dayLogList = this.punchProvider.listPunchDayLogsExcludeEndDay(member.getTargetId(), ownerId, dateSF.get().format(startCalendar.getTime()),
							dateSF.get().format(endCalendar.getTime()) );
			List<PunchStatisticsDTO> list = new ArrayList<PunchStatisticsDTO>();
			for(PunchDayLog dayLog : dayLogList){
				statistic.setPunchTimesPerDay(dayLog.getPunchTimesPerDay());
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
//					dto.setApprovalStatus((byte) 0);
				}
				
			}
//			if(statistic.getPunchTimesPerDay().equals(PunchTimesPerDay.TWICE.getCode())){
//				processTwicePunchListCount(list, statistic);
//			}
//			else{
//				processForthPunchListCount(list, statistic);
//			}
			processPunchListCount(list, statistic);

	        List<Organization> organizations = organizationProvider.listOrganizationByGroupTypes("/"+ownerId + "%", null);
	        List<Long> orgIds = new ArrayList();
	        if(null!=organizations)
	            for(Organization org1 : organizations)
	                orgIds.add(org1.getId());
	        this.punchProvider.deletePunchStatisticByUser(statistic.getOwnerType(),orgIds,statistic.getPunchMonth(),statistic.getUserId());

		} catch (Exception e) {
			LOGGER.error("#####refresh month log error!! userid:["+member.getTargetId()
					+"] organization id :["+orgId+"] ",e); 
		}finally{
			this.punchProvider.createPunchStatistic(statistic);
		}

	}
	private void processPunchListCount(List<PunchStatisticsDTO> list,
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
			if(punchDayLogDTO.getPunchTimesPerDay().equals(PunchTimesPerDay.FORTH.getCode())) {
				countForthStatistic(punchDayLogDTO,statistic);
			}else if (punchDayLogDTO.getPunchTimesPerDay().equals(PunchTimesPerDay.TWICE.getCode())) {
				countTwiceStatistic(punchDayLogDTO, statistic);
			}

		}
			//应上班天数－缺勤天数－事假天数-病假天数-调休天数-公出天数-调休天数
		statistic.setWorkCount(statistic.getWorkDayCount() - statistic.getUnpunchCount() - statistic.getAbsenceCount()
				- statistic.getSickCount() - statistic.getOutworkCount() - statistic.getExchangeCount());

		}

	private void countTwiceStatistic(PunchStatisticsDTO punchDayLogDTO, PunchStatistic statistic) {
		if (punchDayLogDTO.getApprovalStatus() != null) {
			if (ApprovalStatus.UNPUNCH.getCode() == punchDayLogDTO.getApprovalStatus()) {
				statistic.setUnpunchCount(statistic.getUnpunchCount() + 1);
				statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
			} else if (ApprovalStatus.SICK.getCode() == punchDayLogDTO.getApprovalStatus()) {
				statistic.setSickCount(statistic.getSickCount() + 1);
				statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
			} else if (ApprovalStatus.HALFSICK.getCode() == punchDayLogDTO.getApprovalStatus()) {
				statistic.setSickCount(statistic.getSickCount() + 0.5);
				statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
			} else if (ApprovalStatus.OUTWORK.getCode() == punchDayLogDTO.getApprovalStatus()) {
				statistic.setOutworkCount(statistic.getOutworkCount() + 1);
				statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
			} else if (ApprovalStatus.HALFOUTWORK.getCode() == punchDayLogDTO.getApprovalStatus()) {
				statistic.setOutworkCount(statistic.getOutworkCount() + 0.5);
				statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
			} else if (ApprovalStatus.LEAVEEARLY.getCode() == punchDayLogDTO.getApprovalStatus()) {
				statistic.setLeaveEarlyCount(statistic.getLeaveEarlyCount() + 1);
				statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
			} else if (ApprovalStatus.EXCHANGE.getCode() == punchDayLogDTO.getApprovalStatus()) {
				statistic.setExchangeCount(statistic.getExchangeCount() + 1);
				statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
			} else if (ApprovalStatus.HALFEXCHANGE.getCode() == punchDayLogDTO.getApprovalStatus()) {
				statistic.setExchangeCount(statistic.getExchangeCount() + 0.5);
				statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
			} else if (ApprovalStatus.BLANDLE.getCode() == punchDayLogDTO.getApprovalStatus()) {
				statistic.setBlandleCount(statistic.getBlandleCount() + 1);
				statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
			} else if (ApprovalStatus.BELATE.getCode() == punchDayLogDTO.getApprovalStatus()) {
				statistic.setBelateCount(statistic.getBelateCount() + 1);
				statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
			} else if (ApprovalStatus.ABSENCE.getCode() == punchDayLogDTO.getApprovalStatus()) {
				statistic.setAbsenceCount(statistic.getAbsenceCount() + 1);
				statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
			} else if (ApprovalStatus.HALFABSENCE.getCode() == punchDayLogDTO.getApprovalStatus()) {
				statistic.setAbsenceCount(statistic.getAbsenceCount() + 0.5);
				statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
			} else if (ApprovalStatus.OVERTIME.getCode() == punchDayLogDTO.getApprovalStatus()) {
				statistic.setOverTimeSum(statistic.getOverTimeSum() + punchDayLogDTO.getWorkTime());
			}
		} else {
			if (ApprovalStatus.UNPUNCH.getCode() == punchDayLogDTO.getStatus()) {
				statistic.setUnpunchCount(statistic.getUnpunchCount() + 1);
				statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
			} else if (ApprovalStatus.SICK.getCode() == punchDayLogDTO.getStatus()) {
				statistic.setSickCount(statistic.getSickCount() + 1);
				statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
			} else if (ApprovalStatus.HALFSICK.getCode() == punchDayLogDTO.getStatus()) {
				statistic.setSickCount(statistic.getSickCount() + 0.5);
				statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
			} else if (ApprovalStatus.OUTWORK.getCode() == punchDayLogDTO.getStatus()) {
				statistic.setOutworkCount(statistic.getOutworkCount() + 1);
				statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
			} else if (ApprovalStatus.HALFOUTWORK.getCode() == punchDayLogDTO.getStatus()) {
				statistic.setOutworkCount(statistic.getOutworkCount() + 0.5);
				statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
			} else if (ApprovalStatus.LEAVEEARLY.getCode() == punchDayLogDTO.getStatus()) {
				statistic.setLeaveEarlyCount(statistic.getLeaveEarlyCount() + 1);
				statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
			} else if (ApprovalStatus.EXCHANGE.getCode() == punchDayLogDTO.getStatus()) {
				statistic.setExchangeCount(statistic.getExchangeCount() + 1);
				statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
			} else if (ApprovalStatus.HALFEXCHANGE.getCode() == punchDayLogDTO.getStatus()) {
				statistic.setExchangeCount(statistic.getExchangeCount() + 0.5);
				statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
			} else if (ApprovalStatus.BLANDLE.getCode() == punchDayLogDTO.getStatus()) {
				statistic.setBlandleCount(statistic.getBlandleCount() + 1);
				statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
			} else if (ApprovalStatus.BELATE.getCode() == punchDayLogDTO.getStatus()) {
				statistic.setBelateCount(statistic.getBelateCount() + 1);
				statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
			} else if (ApprovalStatus.ABSENCE.getCode() == punchDayLogDTO.getStatus()) {
				statistic.setAbsenceCount(statistic.getAbsenceCount() + 1);
				statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
			} else if (ApprovalStatus.HALFABSENCE.getCode() == punchDayLogDTO.getStatus()) {
				statistic.setAbsenceCount(statistic.getAbsenceCount() + 0.5);
				statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
			} else if (ApprovalStatus.OVERTIME.getCode() == punchDayLogDTO.getStatus()) {
				statistic.setOverTimeSum(statistic.getOverTimeSum() + punchDayLogDTO.getWorkTime());
			}
		}
	}

	private void countForthStatistic(PunchStatisticsDTO punchDayLogDTO, PunchStatistic statistic) {
		if (punchDayLogDTO.getMorningApprovalStatus() != null) {
            //审批后的状态
			//上午
			if (ApprovalStatus.UNPUNCH.getCode() == punchDayLogDTO.getMorningApprovalStatus()) {
				statistic.setUnpunchCount(statistic.getUnpunchCount() + 0.5);
				statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
			} else if (ApprovalStatus.SICK.getCode() == punchDayLogDTO.getMorningApprovalStatus()) {
				statistic.setSickCount(statistic.getSickCount() + 0.5);
				statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
			} else if (ApprovalStatus.HALFSICK.getCode() == punchDayLogDTO.getMorningApprovalStatus()) {
				statistic.setSickCount(statistic.getSickCount() + 0.5);
				statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
			} else if (ApprovalStatus.OUTWORK.getCode() == punchDayLogDTO.getMorningApprovalStatus()) {
				statistic.setOutworkCount(statistic.getOutworkCount() + 0.5);
				statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
			} else if (ApprovalStatus.HALFOUTWORK.getCode() == punchDayLogDTO.getMorningApprovalStatus()) {
				statistic.setOutworkCount(statistic.getOutworkCount() + 0.5);
				statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
			} else if (ApprovalStatus.LEAVEEARLY.getCode() == punchDayLogDTO.getMorningApprovalStatus()) {
				statistic.setLeaveEarlyCount(statistic.getLeaveEarlyCount() + 1);
				statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
			} else if (ApprovalStatus.EXCHANGE.getCode() == punchDayLogDTO.getMorningApprovalStatus()) {
				statistic.setExchangeCount(statistic.getExchangeCount() + 0.5);
				statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
			} else if (ApprovalStatus.HALFEXCHANGE.getCode() == punchDayLogDTO.getMorningApprovalStatus()) {
				statistic.setExchangeCount(statistic.getExchangeCount() + 0.5);
				statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
			} else if (ApprovalStatus.BLANDLE.getCode() == punchDayLogDTO.getMorningApprovalStatus()) {
				statistic.setBlandleCount(statistic.getBlandleCount() + 1);
				statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
			} else if (ApprovalStatus.BELATE.getCode() == punchDayLogDTO.getMorningApprovalStatus()) {
				statistic.setBelateCount(statistic.getBelateCount() + 1);
				statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
			} else if (ApprovalStatus.ABSENCE.getCode() == punchDayLogDTO.getMorningApprovalStatus()) {
				statistic.setAbsenceCount(statistic.getAbsenceCount() + 0.5);
				statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
			} else if (ApprovalStatus.HALFABSENCE.getCode() == punchDayLogDTO.getMorningApprovalStatus()) {
				statistic.setAbsenceCount(statistic.getAbsenceCount() + 0.5);
				statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
			}
			//下午

			if (ApprovalStatus.UNPUNCH.getCode() == punchDayLogDTO.getAfternoonApprovalStatus()) {
				statistic.setUnpunchCount(statistic.getUnpunchCount() + 0.5);
				statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
			} else if (ApprovalStatus.SICK.getCode() == punchDayLogDTO.getAfternoonApprovalStatus()) {
				statistic.setSickCount(statistic.getSickCount() + 0.5);
				statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
			} else if (ApprovalStatus.HALFSICK.getCode() == punchDayLogDTO.getAfternoonApprovalStatus()) {
				statistic.setSickCount(statistic.getSickCount() + 0.5);
				statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
			} else if (ApprovalStatus.OUTWORK.getCode() == punchDayLogDTO.getAfternoonApprovalStatus()) {
				statistic.setOutworkCount(statistic.getOutworkCount() + 0.5);
				statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
			} else if (ApprovalStatus.HALFOUTWORK.getCode() == punchDayLogDTO.getAfternoonApprovalStatus()) {
				statistic.setOutworkCount(statistic.getOutworkCount() + 0.5);
				statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
			} else if (ApprovalStatus.LEAVEEARLY.getCode() == punchDayLogDTO.getAfternoonApprovalStatus()) {
				statistic.setLeaveEarlyCount(statistic.getLeaveEarlyCount() + 1);
				statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
			} else if (ApprovalStatus.EXCHANGE.getCode() == punchDayLogDTO.getAfternoonApprovalStatus()) {
				statistic.setExchangeCount(statistic.getExchangeCount() + 0.5);
				statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
			} else if (ApprovalStatus.HALFEXCHANGE.getCode() == punchDayLogDTO.getAfternoonApprovalStatus()) {
				statistic.setExchangeCount(statistic.getExchangeCount() + 0.5);
				statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
			} else if (ApprovalStatus.BLANDLE.getCode() == punchDayLogDTO.getAfternoonApprovalStatus()) {
				statistic.setBlandleCount(statistic.getBlandleCount() + 1);
				statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
			} else if (ApprovalStatus.BELATE.getCode() == punchDayLogDTO.getAfternoonApprovalStatus()) {
				statistic.setBelateCount(statistic.getBelateCount() + 1);
				statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
			} else if (ApprovalStatus.ABSENCE.getCode() == punchDayLogDTO.getAfternoonApprovalStatus()) {
				statistic.setAbsenceCount(statistic.getAbsenceCount() + 0.5);
				statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
			} else if (ApprovalStatus.HALFABSENCE.getCode() == punchDayLogDTO.getAfternoonApprovalStatus()) {
				statistic.setAbsenceCount(statistic.getAbsenceCount() + 0.5);
				statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
			}
			//加班和正常
			if (ApprovalStatus.OVERTIME.getCode() == punchDayLogDTO.getAfternoonApprovalStatus() || ApprovalStatus.OVERTIME.getCode() == punchDayLogDTO.getMorningApprovalStatus()) {
				statistic.setOverTimeSum(statistic.getOverTimeSum() + ((punchDayLogDTO.getWorkTime() == null) ? 0L : punchDayLogDTO.getWorkTime()));
			} else if (ApprovalStatus.NORMAL.getCode() == punchDayLogDTO.getAfternoonApprovalStatus() && ApprovalStatus.NORMAL.getCode() == punchDayLogDTO.getMorningApprovalStatus()) {
				statistic.setWorkCount(statistic.getWorkCount() + 1);
			}
		} else {
            //未审批的状态
			//上午
			if (ApprovalStatus.UNPUNCH.getCode() == punchDayLogDTO.getMorningStatus()) {
				statistic.setUnpunchCount(statistic.getUnpunchCount() + 0.5);
				statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
			}  else if (ApprovalStatus.LEAVEEARLY.getCode() == punchDayLogDTO.getMorningStatus()) {
				statistic.setLeaveEarlyCount(statistic.getLeaveEarlyCount() + 1);
				statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
			} else if (ApprovalStatus.BLANDLE.getCode() == punchDayLogDTO.getMorningStatus()) {
				statistic.setBlandleCount(statistic.getBlandleCount() + 1);
				statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
			} else if (ApprovalStatus.BELATE.getCode() == punchDayLogDTO.getMorningStatus()) {
				statistic.setBelateCount(statistic.getBelateCount() + 1);
				statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
			} else if (ApprovalStatus.ABSENCE.getCode() == punchDayLogDTO.getMorningStatus()) {
				statistic.setAbsenceCount(statistic.getAbsenceCount() + 0.5);
				statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
			} else if (ApprovalStatus.HALFABSENCE.getCode() == punchDayLogDTO.getMorningStatus()) {
				statistic.setAbsenceCount(statistic.getAbsenceCount() + 0.5);
				statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
			}
			//下午

			if (ApprovalStatus.UNPUNCH.getCode() == punchDayLogDTO.getAfternoonStatus()) {
				statistic.setUnpunchCount(statistic.getUnpunchCount() + 0.5);
				statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
			} else if (ApprovalStatus.LEAVEEARLY.getCode() == punchDayLogDTO.getAfternoonStatus()) {
				statistic.setLeaveEarlyCount(statistic.getLeaveEarlyCount() + 1);
				statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
			}  else if (ApprovalStatus.BLANDLE.getCode() == punchDayLogDTO.getAfternoonStatus()) {
				statistic.setBlandleCount(statistic.getBlandleCount() + 1);
				statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
			} else if (ApprovalStatus.BELATE.getCode() == punchDayLogDTO.getAfternoonStatus()) {
				statistic.setBelateCount(statistic.getBelateCount() + 1);
				statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
			} else if (ApprovalStatus.ABSENCE.getCode() == punchDayLogDTO.getAfternoonStatus()) {
				statistic.setAbsenceCount(statistic.getAbsenceCount() + 0.5);
				statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
			} else if (ApprovalStatus.HALFABSENCE.getCode() == punchDayLogDTO.getAfternoonStatus()) {
				statistic.setAbsenceCount(statistic.getAbsenceCount() + 0.5);
				statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
			}
			//加班和正常
			if (ApprovalStatus.OVERTIME.getCode() == punchDayLogDTO.getAfternoonStatus() || ApprovalStatus.OVERTIME.getCode() == punchDayLogDTO.getMorningStatus()) {
				statistic.setOverTimeSum(statistic.getOverTimeSum() +
						((punchDayLogDTO.getWorkTime() == null) ? 0L : punchDayLogDTO.getWorkTime()));
			} else if (ApprovalStatus.NORMAL.getCode() == punchDayLogDTO.getAfternoonStatus() && ApprovalStatus.NORMAL.getCode() == punchDayLogDTO.getMorningStatus()) {
				statistic.setWorkCount(statistic.getWorkCount() + 1);
			}

		}
	}

//	private void processTwicePunchListCount(List<PunchStatisticsDTO> list, PunchStatistic statistic) {
//		statistic.setWorkCount(0.0);
//		statistic.setUnpunchCount(0.0);
//		statistic.setSickCount(0.0);
//		statistic.setOutworkCount(0.0);
//		statistic.setLeaveEarlyCount(0);
//		statistic.setExchangeCount(0.0);
//		statistic.setBlandleCount(0);
//		statistic.setBelateCount(0);
//		statistic.setAbsenceCount(0.0);
//		statistic.setOverTimeSum(0L);
//		statistic.setExceptionStatus(ExceptionStatus.NORMAL.getCode());
//		for (PunchStatisticsDTO punchDayLogDTO : list) {
//			if (punchDayLogDTO.getApprovalStatus() != null) {
//				if(ApprovalStatus.UNPUNCH.getCode() == punchDayLogDTO.getApprovalStatus()){
//					statistic.setUnpunchCount(statistic.getUnpunchCount()+1);
//					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
//				}else if(ApprovalStatus.SICK.getCode() == punchDayLogDTO.getApprovalStatus()){
//					statistic.setSickCount(statistic.getSickCount()+1);
//					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
//				}else if(ApprovalStatus.HALFSICK.getCode() == punchDayLogDTO.getApprovalStatus()){
//					statistic.setSickCount(statistic.getSickCount()+0.5);
//					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
//				}else if(ApprovalStatus.OUTWORK.getCode() == punchDayLogDTO.getApprovalStatus()){
//					statistic.setOutworkCount(statistic.getOutworkCount()+1);
//					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
//				}else if(ApprovalStatus.HALFOUTWORK.getCode() == punchDayLogDTO.getApprovalStatus()){
//					statistic.setOutworkCount(statistic.getOutworkCount()+0.5);
//					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
//				}else if(ApprovalStatus.LEAVEEARLY.getCode() == punchDayLogDTO.getApprovalStatus()){
//					statistic.setLeaveEarlyCount(statistic.getLeaveEarlyCount()+1);
//					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
//				}else if(ApprovalStatus.EXCHANGE.getCode() == punchDayLogDTO.getApprovalStatus()){
//					statistic.setExchangeCount(statistic.getExchangeCount()+1);
//					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
//				}else if(ApprovalStatus.HALFEXCHANGE.getCode() == punchDayLogDTO.getApprovalStatus()){
//					statistic.setExchangeCount(statistic.getExchangeCount()+0.5);
//					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
//				}else if(ApprovalStatus.BLANDLE.getCode() == punchDayLogDTO.getApprovalStatus()){
//					statistic.setBlandleCount(statistic.getBlandleCount()+1);
//					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
//				}else if(ApprovalStatus.BELATE.getCode() == punchDayLogDTO.getApprovalStatus()){
//					statistic.setBelateCount(statistic.getBelateCount()+1);
//					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
//				}else if(ApprovalStatus.ABSENCE.getCode() == punchDayLogDTO.getApprovalStatus()){
//					statistic.setAbsenceCount(statistic.getAbsenceCount()+1);
//					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
//				}else if(ApprovalStatus.HALFABSENCE.getCode() == punchDayLogDTO.getApprovalStatus()){
//					statistic.setAbsenceCount(statistic.getAbsenceCount()+0.5);
//					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
//				}else if(ApprovalStatus.OVERTIME.getCode() == punchDayLogDTO.getApprovalStatus()){
//					statistic.setOverTimeSum(statistic.getOverTimeSum()+ punchDayLogDTO.getWorkTime());
//				}
//			} else {
//				if(ApprovalStatus.UNPUNCH.getCode() == punchDayLogDTO.getStatus()){
//					statistic.setUnpunchCount(statistic.getUnpunchCount()+1);
//					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
//				}else if(ApprovalStatus.SICK.getCode() == punchDayLogDTO.getStatus()){
//					statistic.setSickCount(statistic.getSickCount()+1);
//					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
//				}else if(ApprovalStatus.HALFSICK.getCode() == punchDayLogDTO.getStatus()){
//					statistic.setSickCount(statistic.getSickCount()+0.5);
//					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
//				}else if(ApprovalStatus.OUTWORK.getCode() == punchDayLogDTO.getStatus()){
//					statistic.setOutworkCount(statistic.getOutworkCount()+1);
//					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
//				}else if(ApprovalStatus.HALFOUTWORK.getCode() == punchDayLogDTO.getStatus()){
//					statistic.setOutworkCount(statistic.getOutworkCount()+0.5);
//					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
//				}else if(ApprovalStatus.LEAVEEARLY.getCode() == punchDayLogDTO.getStatus()){
//					statistic.setLeaveEarlyCount(statistic.getLeaveEarlyCount()+1);
//					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
//				}else if(ApprovalStatus.EXCHANGE.getCode() == punchDayLogDTO.getStatus()){
//					statistic.setExchangeCount(statistic.getExchangeCount()+1);
//					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
//				}else if(ApprovalStatus.HALFEXCHANGE.getCode() == punchDayLogDTO.getStatus()){
//					statistic.setExchangeCount(statistic.getExchangeCount()+0.5);
//					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
//				}else if(ApprovalStatus.BLANDLE.getCode() == punchDayLogDTO.getStatus()){
//					statistic.setBlandleCount(statistic.getBlandleCount()+1);
//					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
//				}else if(ApprovalStatus.BELATE.getCode() == punchDayLogDTO.getStatus()){
//					statistic.setBelateCount(statistic.getBelateCount()+1);
//					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
//				}else if(ApprovalStatus.ABSENCE.getCode() == punchDayLogDTO.getStatus()){
//					statistic.setAbsenceCount(statistic.getAbsenceCount()+1);
//					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
//				}else if(ApprovalStatus.HALFABSENCE.getCode() == punchDayLogDTO.getStatus()){
//					statistic.setAbsenceCount(statistic.getAbsenceCount()+0.5);
//					statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
//				}else if(ApprovalStatus.OVERTIME.getCode() == punchDayLogDTO.getStatus()){
//					statistic.setOverTimeSum(statistic.getOverTimeSum()+ punchDayLogDTO.getWorkTime());
//				}
//			}
//		}
//		//应上班天数－缺勤天数－事假天数-病假天数-调休天数-公出天数-调休天数
//		statistic.setWorkCount(statistic.getWorkDayCount()-statistic.getUnpunchCount()-statistic.getAbsenceCount()
//				-statistic.getSickCount()-statistic.getOutworkCount()-statistic.getExchangeCount());
//	}

//	// 创建每个user的打卡map信息
//	private Map<Long, List<PunchStatisticsDTO>> buildUserPunchCountMap(
//			List<PunchStatisticsDTO> punchDayLogDTOList) {
//		Map<Long, List<PunchStatisticsDTO>> map = new HashMap<Long, List<PunchStatisticsDTO>>();
//		if (punchDayLogDTOList != null && punchDayLogDTOList.size() > 0) {
//			for (PunchStatisticsDTO punchDayLogDTO : punchDayLogDTOList) {
//				Long userId = punchDayLogDTO.getUserId();
//				if (map.containsKey(userId)) {
//					List<PunchStatisticsDTO> list = map.get(userId);
//					list.add(punchDayLogDTO);
//				} else {
//					List<PunchStatisticsDTO> list = new ArrayList<PunchStatisticsDTO>();
//					list.add(punchDayLogDTO);
//					map.put(userId, list);
//				}
//			}
//		}
//		return map;
//	}

//	// 计算user的每个打卡状态的总数
//	private Integer processListCount(List<PunchDayLogDTO> list, Byte status) {
//		Integer count = 0;
//		for (PunchDayLogDTO punchDayLogDTO : list) {
//			if (punchDayLogDTO.getApprovalStatus() != null) {
//				if (status == punchDayLogDTO.getApprovalStatus()) {
//					count++;
//				}
//			} else {
//				if (status == punchDayLogDTO.getStatus()) {
//					count++;
//				}
//			}
//		}
//		return count;
//	}

//	// 计算user的每个打卡状态的总数
//	private Long processListTimeSum (List<PunchDayLogDTO> list, Byte status) {
//		Long timeSum = 0L;
//		for (PunchDayLogDTO punchDayLogDTO : list) {
//			if (punchDayLogDTO.getApprovalStatus() != null) {
//				if (status == punchDayLogDTO.getApprovalStatus()) {
//					timeSum += punchDayLogDTO.getWorkTime();
//				}
//			} else {
//				if (status == punchDayLogDTO.getStatus()) {
//					timeSum += punchDayLogDTO.getWorkTime();
//				}
//			}
//		}
//		return timeSum;
//	}

	@Override
	public ListMonthPunchLogsCommandResponse listMonthPunchLogs(
			ListMonthPunchLogsCommand cmd) {

		checkCompanyIdIsNull(cmd.getEnterpriseId());

		cmd.setEnterpriseId(getTopEnterpriseId(cmd.getEnterpriseId()));
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
//		List<Organization> departments = organizationProvider.listOrganizationByGroupTypes(organization.getPath() + "%", groupTypes);
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
//            String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();

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
		cmd.setOwnerId(getTopEnterpriseId(cmd.getOwnerId()));
		List<PunchTimeRule> punchTimeRules = punchProvider.queryPunchTimeRules(cmd.getOwnerType(), cmd.getOwnerId(),cmd.getTargetType(),cmd.getTargetId(), cmd.getName()) ; 
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
			//计算work time 在下面方法中
			convertTime(punchTimeRule, cmd.getStartEarlyTime(), cmd.getStartLateTime(), cmd.getEndEarlyTime());

			punchTimeRule.setStartEarlyTimeLong(cmd.getStartEarlyTime());
			punchTimeRule.setAfternoonArriveTimeLong(cmd.getAfternoonArriveTime());
			punchTimeRule.setNoonLeaveTimeLong(cmd.getNoonLeaveTime());
			punchTimeRule.setStartLateTimeLong(cmd.getStartLateTime());
			punchTimeRule.setDaySplitTimeLong(cmd.getDaySplitTime());
 
			punchTimeRule.setCreatorUid(userId);
			punchTimeRule.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
					.getTime()));
			punchProvider.createPunchTimeRule(punchTimeRule);
			 
		}
	}
	PunchTimeRule convertPunchTimeRule(PunchTimeRuleDTO dto ){
		PunchTimeRule punchTimeRule = ConvertHelper.convert(dto, PunchTimeRule.class);
		punchTimeRule.setAfternoonArriveTime(convertTime(dto.getAfternoonArriveTime()));
		punchTimeRule.setPunchTimesPerDay(dto.getPunchTimesPerDay());
		punchTimeRule.setNoonLeaveTime(convertTime(dto.getNoonLeaveTime())); 
		punchTimeRule.setDaySplitTime(convertTime(dto.getDaySplitTime()));
		convertTime(punchTimeRule, dto.getStartEarlyTime(), dto.getStartLateTime(), dto.getEndEarlyTime());
		punchTimeRule.setCreatorUid( UserContext.current().getUser().getId());
		punchTimeRule.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
				.getTime()));
		return punchTimeRule;
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
		cmd.setOwnerId(getTopEnterpriseId(cmd.getOwnerId()));
		if (null == cmd.getPunchTimesPerDay() ) {
			LOGGER.error("Invalid PunchTimesPerDay parameter in the command");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid PunchTimesPerDay parameter in the command");
		}

		List<PunchTimeRule> punchTimeRules = punchProvider.queryPunchTimeRules(cmd.getOwnerType(), cmd.getOwnerId(),cmd.getTargetType(),cmd.getTargetId(),  cmd.getName()) ; 
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

			punchTimeRule.setStartEarlyTimeLong(cmd.getStartEarlyTime());
			punchTimeRule.setAfternoonArriveTimeLong(cmd.getAfternoonArriveTime());
			punchTimeRule.setNoonLeaveTimeLong(cmd.getNoonLeaveTime());
			punchTimeRule.setStartLateTimeLong(cmd.getStartLateTime());
			punchTimeRule.setDaySplitTimeLong(cmd.getDaySplitTime());
			punchTimeRule.setDescription(cmd.getDescription());
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
		cmd.setOwnerId(getTopEnterpriseId(cmd.getOwnerId()));
		listPunchTimeRuleListResponse response =new listPunchTimeRuleListResponse();
		if (cmd.getPageAnchor() == null)
			cmd.setPageAnchor(0L);
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());
		 
		List<PunchTimeRule> results = this.punchProvider.queryPunchTimeRuleList(cmd.getOwnerType(),cmd.getOwnerId(),cmd.getTargetType(),cmd.getTargetId(), locator, pageSize + 1 );
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
			PunchTimeRuleDTO dto = this.processTimeRuleDTO(other);
			response.getTimeRules().add(dto);
		});
		return response;
	}
	PunchTimeRuleDTO processTimeRuleDTO(PunchTimeRule other){
		PunchTimeRuleDTO dto = ConvertHelper.convert(other, PunchTimeRuleDTO.class); 
		dto.setAfternoonArriveTime(null!=other.getAfternoonArriveTimeLong()?other.getAfternoonArriveTimeLong():convertTimeToGMTMillisecond(other.getAfternoonArriveTime()));
		dto.setNoonLeaveTime(null!=other.getNoonLeaveTimeLong()?other.getNoonLeaveTimeLong():convertTimeToGMTMillisecond(other.getNoonLeaveTime()));
		dto.setStartEarlyTime(null!=other.getStartEarlyTimeLong()?other.getStartEarlyTimeLong():convertTimeToGMTMillisecond(other.getStartEarlyTime()));
		dto.setStartLateTime(null!=other.getStartLateTimeLong()?other.getStartLateTimeLong():convertTimeToGMTMillisecond(other.getStartLateTime()));
		
		dto.setEndEarlyTime(dto.getStartEarlyTime() + (null!=other.getWorkTimeLong()?other.getWorkTimeLong():convertTimeToGMTMillisecond(other.getWorkTime())));
		dto.setDaySplitTime(null!=other.getDaySplitTimeLong()?other.getDaySplitTimeLong():convertTimeToGMTMillisecond(other.getDaySplitTime()));
		return dto;
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
			PunchLocationRuleDTO dto = this.processLocationRuleDTO(other);
			response.getLocationRules().add(dto);
		});
		return response;
	}
	PunchLocationRuleDTO processLocationRuleDTO(PunchLocationRule other){
		PunchLocationRuleDTO dto = ConvertHelper.convert(other, PunchLocationRuleDTO.class); 
		List<PunchGeopoint> geos = this.punchProvider.listPunchGeopointsByRuleId(other.getOwnerType(), other.getOwnerId(), other.getId()) ;
		if(null != geos){
			dto.setPunchGeoPoints(new ArrayList<PunchGeoPointDTO>());
			for(PunchGeopoint geo : geos){
				PunchGeoPointDTO geoDTO = ConvertHelper.convert(geo, PunchGeoPointDTO.class);
				dto.getPunchGeoPoints().add(geoDTO);
			} 
		}
		return dto;
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
			PunchWiFiRuleDTO dto = processWifiRuleDTO(other);
			response.getWifiRules().add(dto);
		});
		return response;
	}
	PunchWiFiRuleDTO processWifiRuleDTO(PunchWifiRule other){
		PunchWiFiRuleDTO dto = ConvertHelper.convert(other, PunchWiFiRuleDTO.class); 
		
		List<PunchWifi> wifis = this.punchProvider.listPunchWifisByRuleId(other.getOwnerType(), other.getOwnerId(), other.getId()) ;
		if(null != wifis){
			dto.setWifis(new ArrayList<PunchWiFiDTO>()); 
			for(PunchWifi geo : wifis){
				PunchWiFiDTO wifiDTO = ConvertHelper.convert(geo, PunchWiFiDTO.class);
				dto.getWifis().add(wifiDTO);
			} 
		}
		return dto;
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
//		if(null == cmd.getPunchRuleId()){
//			LOGGER.error("Invalid PunchRuleId parameter in the command");
//			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,
//					"Invalid PunchRuleId parameter in the command");
//		}
//			
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
		 
		List<PunchRuleOwnerMap> results = this.punchProvider.queryPunchRuleOwnerMapList(cmd.getOwnerType(),cmd.getOwnerId(), cmd.getTargetType(),
				cmd.getTargetId(),locator, pageSize + 1 );
		if (null == results)
			return response;
		Long nextPageAnchor = null;
		if (results != null && results.size() > pageSize) {
			results.remove(results.size() - 1);
			nextPageAnchor = results.get(results.size() - 1).getId();
		}
		response.setNextPageAnchor(nextPageAnchor);
		response.setPunchRuleMaps(new ArrayList<PunchRuleMapDTO>()); 
		 
		for(PunchRuleOwnerMap other:results)  {
			PunchRuleMapDTO dto = ConvertHelper.convert(other, PunchRuleMapDTO.class);
//			PunchRule pr = this.punchProvider.getPunchRuleById(other.getPunchRuleId());
//			if(pr == null)
//				throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
//						PunchServiceErrorCode.ERROR_ENTERPRISE_DIDNOT_SETTING,
//						"have no punch rule");
//			dto.setPunchRuleName(pr.getName());
			if(PunchOwnerType.User.getCode().equals(other.getTargetType())){
				OrganizationMember member = this.organizationProvider.findOrganizationMemberByOrgIdAndUId(other.getTargetId(), other.getOwnerId());
				if(null== member)
					continue;
				dto.setTargetName(member.getContactName());
				OrganizationDTO dept =  this.findUserDepartment(other.getTargetId(), other.getOwnerId());
				if(null != dept)
					dto.setTargetDept(dept.getName());
				
			}
//			if (other.getReviewRuleId() != null) {
//				ApprovalRule approvalRule = approvalRuleProvider.findApprovalRuleById(other.getReviewRuleId());
//				if (approvalRule != null) {
//					dto.setReviewRuleName(approvalRule.getName());
//				}
//			}
			response.getPunchRuleMaps().add(dto);
		} 
		return response;
	}
	@Override
	public Long getTopEnterpriseId(Long organizationId){
		Organization organization = organizationProvider.findOrganizationById(organizationId);
		if(organization.getParentId() == null )
			return organizationId;
		else{
			return Long.valueOf(organization.getPath().split("/")[1]);
		}
		
		
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
			List<String> groupTypes = new ArrayList<String>();
			groupTypes.add(OrganizationGroupType.DEPARTMENT.getCode());
			groupTypes.add(OrganizationGroupType.ENTERPRISE.getCode());
			return this.organizationService.getMemberTopDepartment(groupTypes,
					userIdentifier.getIdentifierToken(), organizationId);
			}
		 

	}
	/** 向上递归找规则*/
	private PunchRuleOwnerMap getPunchRule( Long ownerId , Organization dept,int loopMax){
		if(--loopMax <0 || null == dept)
			throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
					PunchServiceErrorCode.ERROR_ENTERPRISE_DIDNOT_SETTING,
					"have no punch rule");
		PunchRuleOwnerMap ruleMap = this.punchProvider.getPunchRuleOwnerMapByTarget( 
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
		PunchRuleOwnerMap map = getPunchRuleMap(  ownerType,   ownerId,  userId);
		if(null == map || map.getPunchRuleId() == null)
			return null;
		return this.punchProvider.getPunchRuleById(map.getPunchRuleId());
		
	}

	/**找到用户的打卡总规则*/ 
	public PunchRuleOwnerMap getPunchRuleMap(String ownerType, Long ownerId,Long userId){
		//如果有个人规则就返回个人规则
		PunchRuleOwnerMap map = this.punchProvider.getPunchRuleOwnerMapByOwnerAndTarget(ownerType, ownerId, PunchOwnerType.User.getCode(), userId);
		if (null == map || map.getPunchRuleId() == null){
			//没有个人规则,向上递归找部门规则
			if(!ownerType.equals(PunchOwnerType.ORGANIZATION.getCode()))
				return null;
			//加循环限制
			int loopMax = 10;
			OrganizationDTO deptDTO = findUserDepartment(userId, ownerId);
			Organization dept =  ConvertHelper.convert(deptDTO, Organization.class);
			map = getPunchRule(null ,dept,loopMax);
		}
		return map;
		
	}
	//ownerType为组织，ownerId为组织id
	@Override
	public ApprovalRule getApprovalRule(String ownerType, Long ownerId,Long userId){
		//如果有个人规则就返回个人规则
		PunchRuleOwnerMap map = this.punchProvider.getPunchRuleOwnerMapByOwnerAndTarget(ownerType, ownerId, PunchOwnerType.User.getCode(), userId);
		if (null == map || map.getReviewRuleId() == null ){
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
//		cmd.setOwnerId(getTopEnterpriseId(cmd.getOwnerId()));
		if (null == cmd.getOwnerId() ||null == cmd.getOwnerType()) {
			LOGGER.error("Invalid owner type or  Id parameter in the command");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid owner type or  Id parameter in the command");
		}
		ListPunchCountCommandResponse response = new ListPunchCountCommandResponse();
		List<PunchCountDTO> punchCountDTOList = new ArrayList<PunchCountDTO>();

		Organization org = this.checkOrganization(cmd.getOwnerId());
		
		List<Long> userIds = listDptUserIds(org,cmd.getOwnerId(), cmd.getUserName(),cmd.getIncludeSubDpt());
		if (null == userIds)
			return response;
		//分页查询
		if (cmd.getPageAnchor() == null)
			cmd.setPageAnchor(0L);
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());
//		Long organizationId = org.getDirectlyEnterpriseId();
//		if(organizationId.equals(0L))
//			organizationId = org.getId();
        Long organizationId = getTopEnterpriseId(cmd.getOwnerId());
		List<PunchStatistic> results = this.punchProvider.queryPunchStatistics(cmd.getOwnerType(),
				organizationId,cmd.getMonth(),
				cmd.getExceptionStatus(),userIds, locator, pageSize + 1 );
		response.setExtColumns(new ArrayList<>());
		List<ApprovalCategory> categories = approvalCategoryProvider.listApprovalCategory(UserContext.getCurrentNamespaceId(), cmd.getOwnerType(),  organizationId, ApprovalType.ABSENCE.getCode());
		if(null != categories){
			for(ApprovalCategory category : categories){
				response.getExtColumns().add(category.getCategoryName());
			}
		}
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

            punchCountDTOList.add(dto);
//			if(statistic.getOverTimeSum().equals(0L)){
//				dto.setOverTimeSum(0.0);
//			}
//			else{
//				BigDecimal b = new BigDecimal(statistic.getOverTimeSum()/3600000.0);
//				dto.setOverTimeSum(b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
//			}
			
			dto.setOverTimeSum(approvalRequestProvider.countHourLengthByUserAndMonth(statistic.getUserId(),statistic.getOwnerType(),statistic.getOwnerId(),statistic.getPunchMonth()));
			List<ApprovalRangeStatistic> abscentStats = approvalRangeStatisticProvider.queryApprovalRangeStatistics(null, Integer.MAX_VALUE,new ListingQueryBuilderCallback()  {
				@Override
				public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
						SelectQuery<? extends Record> query) {  
					query.addConditions(Tables.EH_APPROVAL_RANGE_STATISTICS.PUNCH_MONTH.eq(cmd.getMonth()));  
					query.addConditions(Tables.EH_APPROVAL_RANGE_STATISTICS.USER_ID.eq(statistic.getUserId()));
					query.addConditions(Tables.EH_APPROVAL_RANGE_STATISTICS.OWNER_ID.eq(statistic.getOwnerId()));
					query.addConditions(Tables.EH_APPROVAL_RANGE_STATISTICS.OWNER_TYPE.eq(statistic.getOwnerType()));
					
					return null;
				}
			});
			dto.setExts(new ArrayList<ExtDTO>());
			if(null != categories){
				for(ApprovalCategory category : categories){
					ExtDTO extDTO = new ExtDTO();
					dto.getExts().add(extDTO);
					extDTO.setName(category.getCategoryName());
					if(null != abscentStats && abscentStats.size()>0){ 
						for(ApprovalRangeStatistic abstat : abscentStats){
							if(abstat.getCategoryId().equals(category.getId())){ 
								StringBuffer timeCountBuffer = new StringBuffer();
								String[] range = abstat.getActualResult().split("\\.");
								if(!range[0].equals("0")){
									timeCountBuffer.append(range[0]);
									timeCountBuffer.append("天");
								}
								if(!range[1].equals("0")){
									timeCountBuffer.append(range[1]);
									timeCountBuffer.append("小时");
								}
								if(!range[2].equals("0")){
									timeCountBuffer.append(range[2]);
									timeCountBuffer.append("分钟");
								}
								extDTO.setTimeCount(timeCountBuffer.toString());
								break;
							}
						}
					}
				}
			}
			absenceUserIdList.add(statistic.getUserId());
		}
		response.setNextPageAnchor(nextPageAnchor);
		response.setPunchCountList(punchCountDTOList);
		
		//把请假的天数加在这里，add by tt, 20160921
//		Map<Long, List<AbsenceTimeDTO>> userAbsenceTimeMap = getUserAbsenceTimes(cmd.getMonth(), cmd.getOwnerType(), cmd.getOwnerId(), absenceUserIdList);
//		punchCountDTOList.forEach(p->{
			
//			List<AbsenceTimeDTO> list = userAbsenceTimeMap.get(p.getUserId());
//			if (ListUtils.isEmpty(list)) {
//				try {
//					list = getDefaultAbsenceStatistics(organizationService.getTopOrganizationId(cmd.getOwnerId()), new java.sql.Date(monthSF.get().parse(cmd.getMonth()).getTime()));
//				} catch (Exception e) {
//					throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,
//							"parse month error");
//				}
//			}
//			p.setAbsenceTimeList(list);
			
//		});
		
		return response;
	}

	private List<Long> listDptUserIds(Organization org , Long ownerId,String userName, Byte includeSubDpt) {
		//找到所有子部门 下面的用户
		 
		List<String> groupTypeList = new ArrayList<String>();
		groupTypeList.add(OrganizationGroupType.ENTERPRISE.getCode());
		groupTypeList.add(OrganizationGroupType.DEPARTMENT.getCode());
		List<OrganizationMember> organizationMembers = null;
		if(null == includeSubDpt || includeSubDpt.equals(NormalFlag.YES.getCode())){ 
			
			List<Organization> orgs = organizationProvider.listOrganizationByGroupTypes(org.getPath()+"%", groupTypeList);

			LOGGER.debug(" organizationProvider.listOrganizationByGroupTypes("+org.getPath()+"% , "+StringHelper.toJsonString(groupTypeList)+"); " );
			LOGGER.debug("orgs  : "+StringHelper.toJsonString(orgs));
			List<Long> orgIds = new ArrayList<Long>();
			orgIds.add(org.getId());
			for (Organization o : orgs){
				orgIds.add(o.getId());
			}
			CrossShardListingLocator locator = new CrossShardListingLocator();
			organizationMembers = this.organizationProvider.listOrganizationPersonnels(userName, orgIds,
					OrganizationMemberStatus.ACTIVE.getCode(), ContactSignUpStatus.SIGNEDUP.getCode(), locator, Integer.MAX_VALUE-1);

			LOGGER.debug("members  : "+ organizationMembers.size());
			}
		else{
			org.setStatus(OrganizationMemberStatus.ACTIVE.getCode());
			organizationMembers = this.organizationProvider.listOrganizationPersonnels(userName, org, ContactSignUpStatus.SIGNEDUP.getCode(),
					null,null, Integer.MAX_VALUE-1);
			}
		if(null == organizationMembers)
			return null;
		List<Long> userIds = new ArrayList<Long>();
		for(OrganizationMember member : organizationMembers){
			if (member.getTargetType() != null && member.getTargetType().equals(OrganizationMemberTargetType.USER.getCode()))
				userIds.add(member.getTargetId());
		}

		LOGGER.debug("userIds  : "+StringHelper.toJsonString(userIds));
		return userIds;
}
//	private Map<Long, List<AbsenceTimeDTO>> getUserAbsenceTimes(String month, String ownerType, Long ownerId, List<Long> absenceUserIdList) {
//		Long userId = UserContext.current().getUser().getId();
//		Long organizationId = organizationService.getTopOrganizationId(ownerId);
//		try {
//			java.sql.Date fromDate = new java.sql.Date(monthSF.get().parse(month).getTime());
//			java.sql.Date toDate = new java.sql.Date(getNextMonth(month).getTime());
//			
//			List<ApprovalDayActualTime> approvalDayActualTimeList = approvalDayActualTimeProvider.listApprovalDayActualTimeByUserIds(fromDate, toDate, ApprovalOwnerType.ORGANIZATION.getCode(), organizationId, ApprovalType.ABSENCE.getCode(), absenceUserIdList);
//			if (ListUtils.isEmpty(approvalDayActualTimeList)) {
//				return new HashMap<Long, List<AbsenceTimeDTO>>();
//			}
//			//需要把 针对同一天既有请假申请，又有忘打卡申请，已最后提交的申请为依据 排除掉
//			Map<Long, Map<Long, List<ApprovalDayActualTime>>> map = approvalDayActualTimeList.stream().map(a->{
//				if (approvalRequestProvider.checkExcludeAbsenceRequest(userId, a.getOwnerId(), a.getTimeDate())) {
//					return null;
//				}
//				return a;
//			}).filter(a->a!=null).collect(Collectors.groupingBy(ApprovalDayActualTime::getUserId, Collectors.groupingBy(ApprovalDayActualTime::getCategoryId)));
//			
//			Map<Long, List<AbsenceTimeDTO>> resultMap = new HashMap<>();
//			List<ApprovalCategory> approvalCategoryList = approvalCategoryProvider.listApprovalCategoryForStatistics(UserContext.getCurrentNamespaceId(), ApprovalOwnerType.ORGANIZATION.getCode(), organizationId, ApprovalType.ABSENCE.getCode(), fromDate);
//			//key1 userId, key2 categoryId
//			map.forEach((key1, value1)->{
//				List<AbsenceTimeDTO> absenceTimeList = approvalCategoryList.stream().map(a->{
//					List<ApprovalDayActualTime> value2 = value1.get(a.getId());
//					AbsenceTimeDTO absenceTimeDTO = new AbsenceTimeDTO();
//					absenceTimeDTO.setCategoryId(a.getId());
//					absenceTimeDTO.setCategoryName(a.getCategoryName());
//					if (ListUtils.isEmpty(value2)) {
//						absenceTimeDTO.setActualResult("0.0.0");
//					}else {
//						absenceTimeDTO.setActualResult("");
//						value2.forEach(v->{
//							absenceTimeDTO.setActualResult(calculateTimeTotal(absenceTimeDTO.getActualResult(), v.getActualResult()));
//						});
//					}
//					return absenceTimeDTO;
//				}).collect(Collectors.toList());
//				
//				resultMap.put(key1, absenceTimeList);
//			});
//			
//			return resultMap;
//		} catch (ParseException e) {
//			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,
//					"parse month error");
//		}
//	}
//
//	private List<AbsenceTimeDTO> getDefaultAbsenceStatistics(Long organizationId, java.sql.Date fromDate){
//		List<ApprovalCategory> approvalCategoryList = approvalCategoryProvider.listApprovalCategoryForStatistics(UserContext.getCurrentNamespaceId(), ApprovalOwnerType.ORGANIZATION.getCode(), organizationId, ApprovalType.ABSENCE.getCode(), fromDate);
//		return approvalCategoryList.stream().map(a->{
//			AbsenceTimeDTO absenceTimeDTO = new AbsenceTimeDTO();
//			absenceTimeDTO.setCategoryId(a.getId());
//			absenceTimeDTO.setCategoryName(a.getCategoryName());
//			absenceTimeDTO.setActualResult("0.0.0");
//			return absenceTimeDTO;
//		}).collect(Collectors.toList());
//	}
	
//	private String calculateTimeTotal(String timeTotal, String actualResult) {
//		//表中按1.25.33这样存储，每一位分别代表天、小时、分钟，统计时需要每个位分别相加，且小时满24不用进一，分钟满60需要进一，如果某一位是0也必须存储，也就是说结果中必须包含两个小数点
//		if (StringUtils.isBlank(timeTotal)) {
//			return actualResult;
//		}
//		
//		String[] times = timeTotal.split("\\.");
//		String[] actuals = actualResult.split("\\.");
//		
//		int days = Integer.parseInt(times[0]) + Integer.parseInt(actuals[0]);
//		int hours = Integer.parseInt(times[1]) + Integer.parseInt(actuals[1]);
//		int minutes = Integer.parseInt(times[2]) + Integer.parseInt(actuals[2]);
//		
//		hours = hours + minutes / 60;
//		minutes = minutes % 60;
//		
//		return days + "." + hours + "." + minutes;
//	}
	
//	private String getCategoryName(Long categoryId){
//		ApprovalCategory category = approvalCategoryProvider.findApprovalCategoryById(categoryId);
//		if (category != null) {
//			return category.getCategoryName();
//		}
//		return "";
//	}
//	
//	private Date getNextMonth(String month) {
//		try {
//			Date date = monthSF.get().parse(month);
//			Calendar calendar = Calendar.getInstance();
//			calendar.setTime(date);
//			calendar.add(Calendar.MONTH, 1);
//			return calendar.getTime();
//		} catch (ParseException e) {
//			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,
//					"parse month error");
//		}
//	}
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

			List<Long> userIds = listDptUserIds(org,cmd.getOwnerId(), cmd.getUserName(),cmd.getIncludeSubDpt());
			if (null == userIds)
				return response;
			//分页查询 由于用到多条件排序,所以使用pageOffset方式分页
			 
			String startDay=null;
			if(null!=cmd.getStartDay())
				startDay =  dateSF.get().format(new Date(cmd.getStartDay()));
			String endDay=null;
			if(null!=cmd.getEndDay())
				endDay =  dateSF.get().format(new Date(cmd.getEndDay()));
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
    		return timeSF.get().format(convertTime(timeLong));
    }
	private void setNewPunchDetailsBookRow(Sheet sheet, PunchDayDetailDTO dto) {

		Row row = sheet.createRow(sheet.getLastRowNum()+1);
		int i = -1; 
		row.createCell(++i).setCellValue(dto.getUserName());
		row.createCell(++i).setCellValue(dto.getDeptName());
		row.createCell(++i).setCellValue(dateSF.get().format(new Date(dto.getPunchDate()))); 
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
		long daylogUseTime = 0L;

		ListPunchMonthLogsResponse response = new ListPunchMonthLogsResponse();
		PunchOwnerType ownerType = PunchOwnerType.fromCode(cmd.getOwnerType());
		if(PunchOwnerType.ORGANIZATION.equals(ownerType)){
			//找到所有子部门 下面的用户
//			Organization org = this.checkOrganization(cmd.getOwnerId());
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
				monthBegin.setTime(monthSF.get().parse(cmd.getPunchMonth()));
				//月初
				monthBegin.set(Calendar.DAY_OF_MONTH, 1);
				Calendar monthEnd = Calendar.getInstance();
				if(!monthSF.get().format(monthEnd.getTime()).equals(cmd.getPunchMonth())){
					monthEnd.setTime(monthSF.get().parse(cmd.getPunchMonth()));
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
					long beginDayLogTime = DateHelper.currentGMTTime().getTime();
					List<PunchDayLog> punchDayLogs = punchProvider.listPunchDayLogs(member.getTargetId(),member.getOrganizationId(), dateSF.get().format(monthBegin.getTime()),
							dateSF.get().format(monthEnd.getTime()) );
					long endDayLogTime = DateHelper.currentGMTTime().getTime();
					daylogUseTime+=endDayLogTime-beginDayLogTime;
					if (null == punchDayLogs || punchDayLogs.isEmpty())
						continue;
					userMonthLogsDTO.setPunchLogsDayList(new ArrayList<PunchLogsDay>());
					ExceptionStatus exceptionStatus = ExceptionStatus.NORMAL;
					userMonthLogsDTO.setUserStatus(PunchUserStatus.NORMAL.getCode());
					for(PunchDayLog dayLog : punchDayLogs){
						if(null != dayLog.getStatus()){
							if(dayLog.getStatus().equals(PunchStatus.NONENTRY.getCode()) )
								userMonthLogsDTO.setUserStatus(PunchUserStatus.NONENTRY.getCode());
							if(dayLog.getStatus().equals(PunchStatus.RESIGNED.getCode()) )
								userMonthLogsDTO.setUserStatus(PunchUserStatus.RESIGNED.getCode());
						}
						PunchLogsDay pdl = ConvertHelper.convert(dayLog, PunchLogsDay.class);
						Calendar logDay = Calendar.getInstance();
						logDay.setTime(dayLog.getPunchDate());
						pdl.setPunchDay(String.valueOf(logDay.get(Calendar.DAY_OF_MONTH)));
						pdl.setWorkTime(convertTimeToGMTMillisecond(dayLog.getWorkTime()));
						pdl.setPunchStatus(dayLog.getStatus());
						pdl.setAfternoonPunchStatus(dayLog.getAfternoonStatus());
						pdl.setMorningPunchStatus(dayLog.getMorningStatus());
						//TODO: 对于请假
						pdl.setStatuString(statusToString(dayLog.getStatus()));
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
			LOGGER.debug("daylog use  :"+daylogUseTime +" ms");
		}
		return response;
	}

	/**
	 * 刷新某公司某一段时间的所有打卡day logs
	 *
	 * */
	@Override
	public void refreshPunchDayLogs(ListPunchDetailsCommand cmd){

		Long companyId = getTopEnterpriseId(cmd.getOwnerId());
		
		Organization org = this.checkOrganization(cmd.getOwnerId());
		List<Long> userIds = listDptUserIds(org,cmd.getOwnerId(), cmd.getUserName(),(byte) 1);
		for(Long userId : userIds){ 
			UserIdentifier userIdentifier = this.userProvider.findClaimedIdentifierByOwnerAndType(userId, IdentifierType.MOBILE.getCode()) ;
			if(null == userIdentifier){
				continue;
			}
			Calendar start = Calendar.getInstance();
			Calendar end = Calendar.getInstance();
			start.setTimeInMillis(cmd.getStartDay());
			end.setTimeInMillis(cmd.getEndDay());
			while (start.before(end)) {
				try { 


					PunchDayLog punchDayLog = punchProvider.getDayPunchLogByDate(userId,
									companyId, dateSF.get().format(start.getTime()));
					if (null == punchDayLog) {
						// 数据库没有计算好的数据 
						PunchLogsDay pdl = new PunchLogsDay();
						pdl.setPunchDay(String.valueOf(start.get(Calendar.DAY_OF_MONTH)));
						pdl.setPunchLogs(new ArrayList<PunchLogDTO>());
						
						
						PunchDayLog newPunchDayLog = new PunchDayLog();
						pdl = calculateDayLog(userId, companyId, start, pdl,newPunchDayLog);
						if (null == pdl) {
							start.add(Calendar.DAY_OF_MONTH, 1);
							continue  ;
						} 
						newPunchDayLog.setUserId(userId);
						newPunchDayLog.setEnterpriseId(companyId);
						newPunchDayLog.setCreatorUid(userId);
						newPunchDayLog.setPunchDate(java.sql.Date.valueOf(dateSF.get().format(start
								.getTime())));
						newPunchDayLog.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
								.getTime()));
						newPunchDayLog.setPunchTimesPerDay(pdl.getPunchTimesPerDay());
						newPunchDayLog.setStatus(pdl.getPunchStatus());
						newPunchDayLog.setMorningStatus(pdl.getMorningPunchStatus());
						newPunchDayLog.setAfternoonStatus(pdl.getAfternoonPunchStatus());
						newPunchDayLog.setViewFlag(ViewFlags.NOTVIEW.getCode());
						newPunchDayLog.setExceptionStatus(pdl.getExceptionStatus());
						newPunchDayLog.setDeviceChangeFlag(getDeviceChangeFlag(userId,java.sql.Date.valueOf(dateSF.get().format(start
								.getTime())),companyId));
						punchProvider.createPunchDayLog(newPunchDayLog);
			
					}
				} catch (Exception e) {
					LOGGER.error("refresh day log wrong  userId["+userId+"],  day"+start.getTime(),e);
				}
	
				start.add(Calendar.DAY_OF_MONTH, 1);
			}
		}
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
		Long ownerId =getTopEnterpriseId(cmd.getOwnerId());
		if(PunchOwnerType.ORGANIZATION.equals(ownerType)){
			//找到所有子部门 下面的用户
			Organization org = this.checkOrganization(cmd.getOwnerId());

			List<Long> userIds = listDptUserIds(org,cmd.getOwnerId(), cmd.getUserName(),cmd.getIncludeSubDpt());
			if (null == userIds)
				return response;
			//分页查询 由于用到多条件排序,所以使用pageOffset方式分页
			Integer pageOffset = 0; 
			if (cmd.getPageAnchor() != null)
				pageOffset = cmd.getPageAnchor().intValue();
			int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
			String startDay=null;
			if(null!=cmd.getStartDay())
				startDay =  dateSF.get().format(new Date(cmd.getStartDay()));
			String endDay=null;
			if(null!=cmd.getEndDay())
				endDay =  dateSF.get().format(new Date(cmd.getEndDay()));
			Long organizationId = org.getDirectlyEnterpriseId();
			if(organizationId.equals(0L))
				organizationId = org.getId();
			List<PunchDayLog> results = punchProvider.listPunchDayLogs(userIds,
					ownerId,startDay,endDay , 
					cmd.getArriveTimeCompareFlag(),convertTime(cmd.getArriveTime()), cmd.getLeaveTimeCompareFlag(),
					convertTime(cmd.getLeaveTime()), cmd.getWorkTimeCompareFlag(),
					convertTime(cmd.getWorkTime()),cmd.getExceptionStatus(), pageOffset, pageSize+1 );
			
			if (null == results)
				return response;
			if(results.size() == pageSize+1){
				results.remove(pageSize);
				Long nextPageAnchor = Long.valueOf(pageOffset+pageSize);
				response.setNextPageAnchor(nextPageAnchor);
			}
			
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
			dto.setStatuString(statusToString(r.getStatus()));
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
			// modify by wh 2017年6月22日 现在都是挂总公司下,用户未必都能通过这种方式查到
//			OrganizationMember member = organizationProvider.findOrganizationMemberByOrgIdAndUId(dto.getUserId(), r.getEnterpriseId() );

			List<OrganizationMember> organizationMembers = organizationService.listOrganizationMemberByOrganizationPathAndUserId("/"+r.getEnterpriseId(),dto.getUserId() );
			if (null != organizationMembers && organizationMembers.size() >0) {
				dto.setUserName(organizationMembers.get(0).getContactName());
				OrganizationDTO dept = this.findUserDepartment(dto.getUserId(), organizationMembers.get(0).getOrganizationId());
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
		 

		List<Long> userIds = listDptUserIds(org,cmd.getOwnerId(), cmd.getUserName(),cmd.getIncludeSubDpt());
		if (null == userIds)
			return response;

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
			monthBegin.setTime(monthSF.get().parse(month));
			//月初
			monthBegin.set(Calendar.DAY_OF_MONTH, 1);
			if(!monthSF.get().format(monthEnd.getTime()).equals(month)){
				monthEnd.setTime(monthSF.get().parse(month));
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

	private void sendMessageToUser(Long userId, String content) {
		if(null == userId)
			return;
		MessageDTO messageDto = new MessageDTO();
		messageDto.setAppId(AppConstants.APPID_MESSAGING);
		messageDto.setSenderUid(User.SYSTEM_USER_LOGIN.getUserId());
		messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), userId.toString()));
		messageDto
				.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), Long.toString(User.SYSTEM_USER_LOGIN.getUserId())));
		messageDto.setBodyType(MessageBodyType.TEXT.getCode());
		messageDto.setBody(content);
		messageDto.setMetaAppId(AppConstants.APPID_MESSAGING);
		LOGGER.debug("punch push to user ["+userId+"]\n messageDTO : ++++ \n " + messageDto);
		// 发消息 +推送
		messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, MessageChannelType.USER.getCode(),
				userId.toString(), messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
	}
	/**
	 * 每15分轮询提醒打卡(整点的10,25,40,55分)
	 * 1.查询所有现在开始到15分钟后的最晚上班时间的timerule
	 * 2.通过timerule找今天的schedule然后找到部门用户推送
	 * 3.查询所有现在+86400000(一天)开始到15分钟后的最晚上班时间的timerule
	 * 4.通过timerule找昨天的schedule然后找到部门用户推送
	 * */
	@Scheduled(cron = "1 0/15 * * * ?") 
	public void scheduledSendPushToUsers(){
		 
		if(RunningFlag.fromCode(scheduleProvider.getRunningFlag()) == RunningFlag.TRUE){
			Date runDate = DateHelper.currentGMTTime();
			long runDateLong = convertTimeToGMTMillisecond(new Time(runDate.getTime()));
			Calendar anchorCalendar = Calendar.getInstance();
			anchorCalendar.setTime(runDate);
			List<Long> sendPunsUserList = new ArrayList<>();
			
			//今天的
			findPunsUser( runDateLong,anchorCalendar,sendPunsUserList);
			
			//昨天的
			runDateLong = runDateLong + 86400000L;
			anchorCalendar.add(Calendar.DAY_OF_MONTH, -1);
			findPunsUser( runDateLong,anchorCalendar,sendPunsUserList);
	
			//推送消息 
			LocaleString scheduleLocaleString = localeStringProvider.find( PunchConstants.PUNCH_PUSH_SCOPE, PunchConstants.PUNCH_REMINDER,"zh_CN"); 
			if(null == scheduleLocaleString ){
				return;
			}
			for(Long userId : sendPunsUserList){
				sendMessageToUser(userId, scheduleLocaleString.getText());
			}
		}
	}
	/** 
	 * @param runDateLong : 打卡结束时间的时间点 
	 * @param anchorCalendar : 日期锚点
	 * @param sendPunsUserList : 推送的用户list
	 * 
	 * */
	private void findPunsUser( long runDateLong, Calendar anchorCalendar,
			List<Long> sendPunsUserList) {
		// TODO Auto-generated method stub
		List<PunchTimeRule> timeRules = punchProvider.queryPunchTimeRuleList(runDateLong,runDateLong+15*60*1000L);
		List<Long> timeRuleIds = new ArrayList<>();
		timeRules.stream().forEach(r ->{
			timeRuleIds.add(r.getId());
		});
		List<PunchScheduling> punchSchedulings = punchSchedulingProvider.queryPunchSchedulings(null, Integer.MAX_VALUE,new ListingQueryBuilderCallback()  {
			@Override
			public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
					SelectQuery<? extends Record> query) {  
				query.addConditions(Tables.EH_PUNCH_SCHEDULINGS.RULE_DATE.eq(new java.sql.Date( anchorCalendar.getTimeInMillis() ) )); 
				query.addConditions(Tables.EH_PUNCH_SCHEDULINGS.TIME_RULE_ID.in( timeRuleIds ));  
				return null;
			}
		});
		if(null != punchSchedulings && punchSchedulings.size()>0){
			for(PunchScheduling scheduling : punchSchedulings){
				if(scheduling.getTargetType().equals(PunchOwnerType.User.getCode())){
					//对于对个人排班的
					//如果没有打卡 添加到推送列表
					if(!checkUserPunch(scheduling.getRuleDate(),scheduling.getTargetId(),scheduling.getOwnerId()))
						sendPunsUserList.add(scheduling.getTargetId());
				}else{
					//对于按机构排班的
					List<String> groupTypeList = new ArrayList<String>();
					groupTypeList.add(OrganizationGroupType.ENTERPRISE.getCode());
					groupTypeList.add(OrganizationGroupType.DEPARTMENT.getCode());
					List<OrganizationMemberDTO> organizationMembers = organizationService.listAllChildOrganizationPersonnel
							(scheduling.getTargetId(), groupTypeList, null) ;
					if(null != organizationMembers && organizationMembers.size() >0){
						for (OrganizationMemberDTO memberDTO : organizationMembers){
							//找到有效用户
							if(memberDTO.getTargetType().equals(OrganizationMemberTargetType.USER.getCode()) && null != memberDTO.getTargetId() ){
								//找到用户的rule 
								PunchRule punchRule = getPunchRule(scheduling.getOwnerType(), scheduling.getOwnerId(), memberDTO.getTargetId());
								//保证用户的rule 和schedule一致(也就是他使用的确实是这个排班而不是用别部门或者单独设置的排班)
								if(null != punchRule && punchRule.getId().equals(scheduling.getPunchRuleId()) &&
										//如果没有打卡 添加到推送列表
										!checkUserPunch(scheduling.getRuleDate(),memberDTO.getTargetId(),scheduling.getOwnerId())) 
									sendPunsUserList.add(memberDTO.getTargetId());
							}
						}
					}
				}
			}
		}
	}
	private boolean checkUserPunch(java.sql.Date ruleDate, Long userId,Long companyId) {
		// TODO Auto-generated method stub
		List<PunchLog> punchLogs = punchProvider.listPunchLogsByDate(userId,
				companyId, dateSF.get().format(ruleDate),
				ClockCode.SUCESS.getCode());
		if(null == punchLogs || punchLogs.size() ==0)
			return false;
		return true;
	}
	/**
	 * 每15分轮询刷打卡记录
	 * 1.刷punchdate为前一天的
	 * 2.找timerule里分界点(分界点只会是0,15,30,45)在这一个15分钟内的(当前时间点取整-15分钟,当前时间点取整]
	 * 3.找到规则映射的公司/部门/个人,然后精确到个人.刷前一天的记录.
	 * */
	@Scheduled(cron = "1 0/15 * * * ?")
	@Override
	public void dayRefreshLogScheduled() {
		if(RunningFlag.fromCode(scheduleProvider.getRunningFlag()) == RunningFlag.TRUE){
			coordinationProvider.getNamedLock(CoordinationLocks.PUNCH_DAY_SCHEDULE.getCode()).tryEnter(() -> {
		        Date runDate = DateHelper.currentGMTTime();
		        dayRefreshLogScheduled(runDate);
			});
		}
    }
    @Override
    public void testDayRefreshLogs(Long runDate){
        dayRefreshLogScheduled(new Date(runDate));
    }
    public void dayRefreshLogScheduled(Date runDate){
		//刷新前一天的
		Calendar punCalendar = Calendar.getInstance();
        punCalendar.setTime(runDate);
		punCalendar.add(Calendar.DATE, -1);
		//取所有设置了rule的公司 

		Calendar endCalendar = Calendar.getInstance();
		endCalendar.setTime(runDate); 
		endCalendar.set(Calendar.SECOND, 0);
		endCalendar.set(Calendar.MILLISECOND, 0);
		//取昨天和今天的排班
		List<PunchScheduling> punchSchedulings = punchSchedulingProvider.queryPunchSchedulings(null, Integer.MAX_VALUE,new ListingQueryBuilderCallback()  {
			@Override
			public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
					SelectQuery<? extends Record> query) { 
//					query.addConditions(Tables.EH_PUNCH_SCHEDULINGS.RULE_DATE.gt(new java.sql.Date( startCalendar.getTime()));
				query.addConditions(Tables.EH_PUNCH_SCHEDULINGS.RULE_DATE.between(new java.sql.Date(punCalendar.getTime().getTime()),
						new java.sql.Date( endCalendar.getTime().getTime())));
//					query.addConditions(Tables.EH_PUNCH_SCHEDULINGS.RULE_DATE.equal(new java.sql.Date( endCalendar.getTime().getTime())));
				query.addOrderBy(Tables.EH_PUNCH_SCHEDULINGS.RULE_DATE.asc());
				return null;
			}
		});
		
		//1.计算targetType 为organization的所有用户,并且userid不存在于map表的targetId内的
//			List<Long> orgIds = this.punchProvider.queryPunchOrganizationsFromRules();
		for(PunchScheduling punchScheduling : punchSchedulings){
			//在15分钟+1分钟延迟内的进行计算, 不在此范围的continue
			long endLong = runDate.getTime();
			long beginLong = runDate.getTime() - 15*60*1000L;
			if(null == punchScheduling.getTimeRuleId())
				continue;
			PunchTimeRule ptr = punchProvider.getPunchTimeRuleById(punchScheduling.getTimeRuleId());
			long punchDateLong = punchScheduling.getRuleDate().getTime()+ptr.getDaySplitTimeLong(); 
			if(punchDateLong > endLong || punchDateLong < beginLong)
				continue;
			//设置起止时间分别是排班的月初和当天
			punCalendar.setTime(runDate); 
			punCalendar.set(Calendar.HOUR_OF_DAY, 0);
			punCalendar.set(Calendar.SECOND, 0);
			punCalendar.set(Calendar.MILLISECOND, 0);
			Calendar startCalendar = Calendar.getInstance();
			startCalendar.setTime(punchScheduling.getRuleDate());
			startCalendar.set(Calendar.HOUR_OF_DAY, 0);
			startCalendar.set(Calendar.DAY_OF_MONTH, 1);
			startCalendar.set(Calendar.MINUTE, 0);
			startCalendar.set(Calendar.SECOND, 0);
			startCalendar.set(Calendar.MILLISECOND, 0);
			if(punchScheduling.getTargetType().equals(PunchOwnerType.ORGANIZATION.getCode())){
				List<PunchRuleOwnerMap> ownerMaps = punchProvider.queryPunchRuleOwnerMaps(null, Integer.MAX_VALUE,new ListingQueryBuilderCallback()  {
					@Override
					public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
							SelectQuery<? extends Record> query) {  
						query.addConditions(Tables.EH_PUNCH_RULE_OWNER_MAP.OWNER_TYPE.equal(punchScheduling.getOwnerType()));
						query.addConditions(Tables.EH_PUNCH_RULE_OWNER_MAP.OWNER_ID.equal(punchScheduling.getOwnerId())); 
						query.addConditions(Tables.EH_PUNCH_RULE_OWNER_MAP.TARGET_TYPE.equal(PunchOwnerType.User.getCode())); 
						return null;
					}
				});
				List<Long> userIdList = new ArrayList<Long>();
				if(null != ownerMaps)
					for(PunchRuleOwnerMap ownerMap : ownerMaps){
						userIdList.add(ownerMap.getTargetId());
					}
				Long orgId = punchScheduling.getTargetId();
				//拿到
				List<String> groupTypeList = new ArrayList<String>();
				groupTypeList.add(OrganizationGroupType.ENTERPRISE.getCode());
				groupTypeList.add(OrganizationGroupType.DEPARTMENT.getCode());
				List<OrganizationMemberDTO> organizationMembers = organizationService.listAllChildOrganizationPersonnel
						(orgId, groupTypeList, null) ;
				//循环刷所有员工
				for(OrganizationMemberDTO member : organizationMembers){
//					if(member.getTargetType().equals(OrganizationMemberTargetType.USER.getCode()) && null != member.getTargetId() && !userIdList.contains(member.getTargetId())){
//						refreshDayLogAndMonthStat(member, orgId, punCalendar,startCalendar);
//					}
					//如果这个人通过ownerid 取出来的规则,和排班表规则一样可以判定他用的是排班表规则,于是进行刷新
					try{
						PunchRule punchRule = getPunchRule(punchScheduling.getOwnerType(), punchScheduling.getOwnerId(), member.getTargetId());
						if(null != punchRule && punchRule.getId().equals(punchScheduling.getPunchRuleId()))
							refreshDayLogAndMonthStat(member, orgId, punCalendar,startCalendar);
					}catch(Exception e ){
						LOGGER.error("get punch rule failed ", e);
					}
				}
			}
			else{
				//2.计算targetType 为user的
				OrganizationDTO dept = findUserDepartment(punchScheduling.getTargetId(), punchScheduling.getOwnerId());
				OrganizationMember member = organizationProvider.findOrganizationMemberByOrgIdAndUId(punchScheduling.getTargetId(), dept.getId());
				refreshDayLogAndMonthStat(ConvertHelper.convert(member, OrganizationMemberDTO.class), dept.getId(), punCalendar, startCalendar);
			}
		
		}
//		});
	}
	private void refreshDayLogAndMonthStat(OrganizationMemberDTO member, Long orgId,
			Calendar punCalendar, Calendar startCalendar) { 
		try {
			LOGGER.debug("refresh day log stat "+ member.toString());
			//刷新 daylog
			this.refreshPunchDayLog(member.getTargetId(), orgId, punCalendar);
			//刷月报 
			
			
		} catch (Exception e) {
			LOGGER.error("#####refresh day log error!! userid:["+member.getTargetId()
					+"] organization id :["+orgId+"] ",e); 
		}
		addPunchStatistics(member, orgId, startCalendar, punCalendar);
	}
	@Override
	public void deletePunchRuleMap(DeletePunchRuleMapCommand cmd) {
		if (null == cmd.getOwnerId() ||null == cmd.getOwnerType()) {
			LOGGER.error("Invalid owner type or  Id parameter in the command");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid owner type or  Id parameter in the command");
		} 
		cmd.setOwnerId(getTopEnterpriseId(cmd.getOwnerId()));
		if (null == cmd.getId() ) {
			LOGGER.error("Invalid   Id parameter in the command");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid   Id parameter in the command");
		}
		PunchRuleOwnerMap obj = this.punchProvider.getPunchRuleOwnerMapById(cmd.getId());
		if(obj.getOwnerId().equals(cmd.getOwnerId())&&obj.getOwnerType().equals(cmd.getOwnerType())){
//			if(null != cmd.getTargetId() && null != cmd.getTargetType()){
//				if(cmd.getTargetId().equals(obj.getTargetId())&& cmd.getTargetType().equals(obj.getTargetType())){
					this.punchProvider.deletePunchRuleOwnerMap(obj);
					this.punchSchedulingProvider.deletePunchSchedulingByOwnerAndTarget(obj.getOwnerType(),obj.getOwnerId(),obj.getTargetType(),obj.getTargetId());
					this.punchProvider.deletePunchTimeRulesByOwnerAndTarget(obj.getOwnerType(),obj.getOwnerId(),obj.getTargetType(),obj.getTargetId());
//				}
//				else{
// 
//					LOGGER.error("Invalid target type or  Id parameter in the command");
//					throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,
//							"Invalid   target type or  Id parameter in the command");
//				 
//				}
//			}
//			else
//				this.punchProvider.deletePunchRuleOwnerMap(obj);
		}
		else{

			LOGGER.error("owenr id and type not the same with database db=[" +
					obj.getOwnerType()+obj.getOwnerId()+"],cmd=["
					+cmd.getOwnerType()+ cmd.getOwnerId()+"]");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,
					"owenr id and type not the same with database");
		}
	

	}
	/**
	 * 把一个target的所有规则取出来组装到resp里返回 
	 * */
	@Override
	public GetTargetPunchAllRuleResponse getTargetPunchAllRule(GetTargetPunchAllRuleCommand cmd) {
		return null; 
//
//		if (null == cmd.getOwnerId() ||null == cmd.getOwnerType()) {
//			LOGGER.error("Invalid owner type or  Id parameter in the command");
//			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,
//					"Invalid owner type or  Id parameter in the command");
//		}
//		if (null == cmd.getTargetId() ||null == cmd.getTargetType()) {
//			LOGGER.error("Invalid owner type or  Id parameter in the command");
//			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,
//					"Invalid target type or  Id parameter in the command");
//		}
//		GetTargetPunchAllRuleResponse resp = new GetTargetPunchAllRuleResponse();
//		PunchRuleOwnerMap map = this.punchProvider.getPunchRuleOwnerMapByOwnerAndTarget(cmd.getOwnerType(), cmd.getOwnerId(),
//				cmd.getTargetType(), cmd.getTargetId());
//		if(null == map || map.getPunchRuleId() == null ){
//			return null;
//		}
//		PunchRule pr = this.punchProvider.getPunchRuleById(map.getPunchRuleId());
//		if(null == pr)
//			return null;
//		PunchTimeRule ptr = getPunchTimeRuleByRuleIdAndDate(pr.getId(),)
//		if(null != ptr)
//			resp.setTimeRule(processTimeRuleDTO(ptr));
//		if(null != pr.getLocationRuleId()){
//			PunchLocationRule plr = this.punchProvider.getPunchLocationRuleById(pr.getLocationRuleId());
//			if(null != plr)
//				resp.setLocationRule(this.processLocationRuleDTO(plr));
//		}
//
//		if(null != pr.getWifiRuleId()){
//			PunchWifiRule pwr = this.punchProvider.getPunchWifiRuleById(pr.getWifiRuleId());
//			if(null != pwr)
//				resp.setWifiRule(this.processWifiRuleDTO(pwr));
//		}
//
//		if(null != pr.getWorkdayRuleId()){
//			PunchWorkdayRule pwkr = this.punchProvider.getPunchWorkdayRuleById(pr.getWorkdayRuleId());
//			if(null != pwkr)
//				resp.setWorkdayRule(this.ConvertPunchWiFiRule2DTO(pwkr));
//		}
//		return resp;
	}
	/**
	 * 给一个target设置规则
	 * */
	@Override
	public void updateTargetPunchAllRule(UpdateTargetPunchAllRuleCommand cmd) {

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
		// TODO 删除之前的规则
		coordinationProvider.getNamedLock(
				CoordinationLocks.UPDATE_APPROVAL_TARGET_RULE.getCode() + cmd.getTargetId()).enter(
				() -> {
			//建立所有规则
			PunchRule pr = new PunchRule();
			pr.setOwnerId(cmd.getOwnerId());
			pr.setOwnerType(cmd.getOwnerType());
			pr.setName(cmd.getTargetId()+"rule");
			pr.setCreatorUid(UserContext.current().getUser().getId());
			pr.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
					.getTime()));
			//time rule
			if(null != cmd.getTimeRule()){
				PunchTimeRule ptr = convertPunchTimeRule(cmd.getTimeRule());
				ptr.setOwnerId(cmd.getOwnerId());
				ptr.setOwnerType(cmd.getOwnerType());
				this.punchProvider.createPunchTimeRule(ptr);
				pr.setTimeRuleId(ptr.getId());
			}
			//location rule 
			if(null != cmd.getLocationRule()){
				PunchLocationRule obj = ConvertHelper.convert( cmd.getLocationRule(), PunchLocationRule.class);
				obj.setOwnerId(cmd.getOwnerId());
				obj.setOwnerType(cmd.getOwnerType());
				obj.setName(cmd.getTargetId()+"rule");
				obj.setCreatorUid(UserContext.current().getUser().getId());
				obj.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
						.getTime()));
				this.punchProvider.createPunchLocationRule(obj); 
				if(null !=  cmd.getLocationRule().getPunchGeoPoints()){ 
					for (PunchGeoPointDTO punchGeopointDTO :  cmd.getLocationRule().getPunchGeoPoints()) {
						PunchGeopoint punchGeopoint = ConvertHelper.convert(punchGeopointDTO, PunchGeopoint.class);
						punchGeopoint.setOwnerType(cmd.getOwnerType());
						punchGeopoint.setOwnerId(cmd.getOwnerId()); 
						punchGeopoint.setLocationRuleId(obj.getId());
						punchGeopoint.setCreatorUid(UserContext.current().getUser().getId());
						punchGeopoint.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
						punchGeopoint.setGeohash(GeoHashUtils.encode(
								punchGeopoint.getLatitude(), punchGeopoint.getLongitude()));
						punchProvider.createPunchGeopoint(punchGeopoint);
					}
				}
				pr.setLocationRuleId(obj.getId());
			}
			//wifi rule 
			if(null != cmd.getWifiRule()){
				PunchWifiRule obj = ConvertHelper.convert(cmd, PunchWifiRule.class);
				obj.setOwnerId(cmd.getOwnerId());
				obj.setOwnerType(cmd.getOwnerType());
				obj.setName(cmd.getTargetId()+"rule");
				obj.setCreatorUid(UserContext.current().getUser().getId());
				obj.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
						.getTime()));
				this.punchProvider.createPunchWifiRule(obj); 
				if(null != cmd.getWifiRule().getWifis()) {
					for (PunchWiFiDTO dto : cmd.getWifiRule().getWifis()) {
						PunchWifi punchWifi = ConvertHelper.convert(dto, PunchWifi.class);
						punchWifi.setOwnerType(cmd.getOwnerType());
						punchWifi.setOwnerId(cmd.getOwnerId());  
						punchWifi.setCreatorUid(UserContext.current().getUser().getId());
						punchWifi.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
						punchWifi.setWifiRuleId(obj.getId());
						punchProvider.createPunchWifi(punchWifi);
					}
				}
				pr.setWifiRuleId(obj.getId());
			}
			//workday rule 
			if(null != cmd.getWorkdayRule()){
				PunchWorkdayRule obj = converDTO2WorkdayRule( cmd.getWorkdayRule());
				obj.setOwnerId(cmd.getOwnerId());
				obj.setOwnerType(cmd.getOwnerType());
				obj.setName(cmd.getTargetId()+"rule");
				this.punchProvider.createPunchWorkdayRule(obj); 
				if(null !=  cmd.getWorkdayRule().getWorkdays()) {
					for (Long date :  cmd.getWorkdayRule().getWorkdays()) {
						PunchHoliday holiday = new PunchHoliday();
						holiday.setOwnerType(cmd.getOwnerType());
						holiday.setOwnerId(cmd.getOwnerId());  
						holiday.setCreatorUid(UserContext.current().getUser().getId());
						holiday.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
						holiday.setWorkdayRuleId(obj.getId());
						holiday.setRuleDate(new java.sql.Date(date));
						holiday.setStatus(DateStatus.WORKDAY.getCode());
						this.punchProvider.createPunchHoliday(holiday);
								
					}
				}
				if(null !=  cmd.getWorkdayRule().getHolidays()) {
					for (Long date :  cmd.getWorkdayRule().getHolidays()) {
						PunchHoliday holiday = new PunchHoliday();
						holiday.setOwnerType(cmd.getOwnerType());
						holiday.setOwnerId(cmd.getOwnerId());  
						holiday.setCreatorUid(UserContext.current().getUser().getId());
						holiday.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
						holiday.setWorkdayRuleId(obj.getId());
						holiday.setRuleDate(new java.sql.Date(date));
						holiday.setStatus(DateStatus.HOLIDAY.getCode());
						this.punchProvider.createPunchHoliday(holiday);
					}
				}
				pr.setWorkdayRuleId(obj.getId());
			}
			
	
			PunchRuleOwnerMap map = this.punchProvider.getPunchRuleOwnerMapByOwnerAndTarget(cmd.getOwnerType(), cmd.getOwnerId(),
					cmd.getTargetType(), cmd.getTargetId());
	
			if(null == map ){
				map = ConvertHelper.convert(cmd, PunchRuleOwnerMap.class);
				map.setCreatorUid(UserContext.current().getUser().getId());
				map.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
						.getTime()));
				this.punchProvider.createPunchRule(pr);
				map.setPunchRuleId(pr.getId());
				this.punchProvider.createPunchRuleOwnerMap(map);
			}else{
				if(null == map.getPunchRuleId())
					this.punchProvider.createPunchRule(pr);
				else{
					PunchRule oldPr = this.punchProvider.getPunchRuleById(map.getPunchRuleId());
					if(null == oldPr)
						this.punchProvider.createPunchRule(pr);
					else{
						pr.setId(oldPr.getId());
						this.punchProvider.updatePunchRule(pr);
					}
				}
				map.setCreatorUid(UserContext.current().getUser().getId());
				map.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
						.getTime()));
				map.setPunchRuleId(pr.getId());
				this.punchProvider.updatePunchRuleOwnerMap(map);
			}
			 return null;
		});
		 
	}
	@Override
	public void deleteTargetPunchAllRule(GetTargetPunchAllRuleCommand cmd) { 
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

		if(cmd.getTargetType().equals(cmd.getOwnerType())&&cmd.getTargetId().equals(cmd.getOwnerId())){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"不能删除公司规则");
		}
		PunchRuleOwnerMap map = this.punchProvider.getPunchRuleOwnerMapByOwnerAndTarget(cmd.getOwnerType(), cmd.getOwnerId(),
				cmd.getTargetType(), cmd.getTargetId());

		if(null != map){
			// 删除规则
			deletePunchRules(cmd.getOwnerType(), cmd.getOwnerId(),map.getPunchRuleId());
			map.setPunchRuleId(null);
			map.setCreatorUid(UserContext.current().getUser().getId());
			map.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
					.getTime())); 
			this.punchProvider.updatePunchRuleOwnerMap(map);
		}
	}
	private void deletePunchRules(String ownerType, Long ownerId, Long punchRuleId) {
		 
		if(null == punchRuleId)
			return;
		PunchRule pr = this.punchProvider.getPunchRuleById(punchRuleId);
		if(null == pr)
			return;
		this.punchProvider.deletePunchTimeRuleByOwnerAndId(ownerType , ownerId, pr.getTimeRuleId()); 
		this.punchProvider.deletePunchLocationRuleByOwnerAndId(ownerType, ownerId, pr.getLocationRuleId());  
		this.punchProvider.deletePunchGeopointsByRuleId( pr.getLocationRuleId());
		this.punchProvider.deletePunchWifiRuleByOwnerAndId(ownerType, ownerId, pr.getWifiRuleId());  
		this.punchProvider.deletePunchWifisByRuleId(pr.getWifiRuleId()); 
		this.punchProvider.deletePunchWorkdayRuleByOwnerAndId(ownerType, ownerId, pr.getWorkdayRuleId());  
		this.punchProvider.deletePunchHolidayByRuleId(pr.getWorkdayRuleId()); 
		this.punchProvider.deletePunchRule(pr);
	}
	public PunchRuleOwnerMap getOrCreateTargetRuleMap(String ownerType,Long ownerId,String targetType,Long targetId){
		if (null == ownerId ||null == ownerType) {
			LOGGER.error("Invalid owner type or  Id parameter in the command");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid owner type or  Id parameter in the command");
		}
		if (null == targetType ||null == targetId) {
			LOGGER.error("Invalid owner type or  Id parameter in the command");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid target type or  Id parameter in the command");
		}
		//z注释map和rule 的关系
		PunchRuleOwnerMap map = this.punchProvider.getPunchRuleOwnerMapByOwnerAndTarget(ownerType, ownerId,
				targetType, targetId);
		if(map == null){
			map = new PunchRuleOwnerMap();
			map.setOwnerType(ownerType);
			map.setOwnerId(ownerId);
			map.setTargetId(targetId);
			map.setTargetType(targetType);
			map.setCreatorUid(UserContext.current().getUser().getId());
			map.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			PunchRule pr = new PunchRule();
			pr.setOwnerId(ownerId);
			pr.setOwnerType(ownerType);
			pr.setName(targetId+"rule");
			pr.setCreatorUid(UserContext.current().getUser().getId());
			pr.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			this.punchProvider.createPunchRule(pr);
			map.setPunchRuleId(pr.getId());
			this.punchProvider.createPunchRuleOwnerMap(map);
		}else if(null ==map.getPunchRuleId()){
			PunchRule pr = new PunchRule();
			pr.setOwnerId(ownerId);
			pr.setOwnerType(ownerType);
			pr.setName(targetId+"rule");
			pr.setCreatorUid(UserContext.current().getUser().getId());
			pr.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
					.getTime()));
			this.punchProvider.createPunchRule(pr);
			map.setPunchRuleId(pr.getId());
			this.punchProvider.updatePunchRuleOwnerMap(map);
			
		}
		return map;
	}
	@Override
	public void updatePunchPoint(UpdatePunchPointCommand cmd) { 
		PunchGeopoint punchGeopoint = punchProvider.findPunchGeopointById(cmd.getId());
		punchGeopoint.setDescription(cmd.getDescription());
		punchGeopoint.setLatitude(cmd.getLatitude());
		punchGeopoint.setLongitude(cmd.getLongitude());
		punchGeopoint.setDistance(cmd.getDistance());
		punchGeopoint.setGeohash(GeoHashUtils.encode(
				punchGeopoint.getLatitude(), punchGeopoint.getLongitude()));
		punchProvider.updatePunchGeopoint(punchGeopoint);
		
		
	}
	@Override
	public void addPunchPoint(AddPunchPointCommand cmd) { 
		cmd.setOwnerId(getTopEnterpriseId(cmd.getOwnerId()));
		PunchRuleOwnerMap map = getOrCreateTargetRuleMap(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getTargetType(), cmd.getTargetId());
		PunchRule pr = punchProvider.getPunchRuleById(map.getPunchRuleId());
		PunchLocationRule plr = null;
		if(pr.getLocationRuleId()==null){
			plr = new PunchLocationRule();
			plr.setOwnerType(cmd.getOwnerType());
			plr.setOwnerId(cmd.getOwnerId());
			pr.setCreatorUid(UserContext.current().getUser().getId());
			pr.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
					.getTime()));
			plr.setName("punch location rule "+cmd.getTargetId());
			punchProvider.createPunchLocationRule(plr);
			pr.setLocationRuleId(plr.getId());
			punchProvider.updatePunchRule(pr);
		}
		else{
			plr = punchProvider.getPunchLocationRuleById(pr.getLocationRuleId());
		}
		PunchGeopoint punchGeopoint = ConvertHelper.convert(cmd, PunchGeopoint.class);
		punchGeopoint.setOwnerType(cmd.getOwnerType());
		punchGeopoint.setOwnerId(cmd.getOwnerId()); 
		punchGeopoint.setLocationRuleId(plr.getId());
		punchGeopoint.setCreatorUid(UserContext.current().getUser().getId());
		punchGeopoint.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		punchGeopoint.setGeohash(GeoHashUtils.encode(
				punchGeopoint.getLatitude(), punchGeopoint.getLongitude()));
		punchProvider.createPunchGeopoint(punchGeopoint);
		
	}
	@Override
	public ListPunchPointsResponse listPunchPoints(ListPunchPointsCommand cmd) { 
		cmd.setOwnerId(getTopEnterpriseId(cmd.getOwnerId()));
		ListPunchPointsResponse response = new ListPunchPointsResponse();
		PunchRuleOwnerMap map =  punchProvider.getPunchRuleOwnerMapByOwnerAndTarget(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getTargetType(), cmd.getTargetId());
		if(null == map)
			return response;
		PunchRule pr = punchProvider.getPunchRuleById(map.getPunchRuleId());
		if(null == pr)
			return response;
		PunchLocationRule plr = null;
		if(pr.getLocationRuleId()==null){
//			plr = new PunchLocationRule();
//			plr.setOwnerType(cmd.getOwnerType());
//			plr.setOwnerId(cmd.getOwnerId());
//			plr.setCreatorUid(UserContext.current().getUser().getId());
//			plr.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
//					.getTime()));
//			plr.setName("punch location rule "+cmd.getTargetId());
//			punchProvider.createPunchLocationRule(plr);
//			pr.setLocationRuleId(plr.getId());
//			punchProvider.updatePunchRule(pr);
			return response ;
		}
		else{
			plr = punchProvider.getPunchLocationRuleById(pr.getLocationRuleId());
		}
		
		List<PunchGeopoint> geos = this.punchProvider.listPunchGeopointsByRuleId(cmd.getOwnerType(), cmd.getOwnerId(), plr.getId()) ;
		if(null != geos){
			response.setPoints(new ArrayList<PunchGeoPointDTO>());
			for(PunchGeopoint geo : geos){
				PunchGeoPointDTO geoDTO = ConvertHelper.convert(geo, PunchGeoPointDTO.class);
				response.getPoints().add(geoDTO);
			} 
		}
		return response;
	}
	@Override
	public void addPunchWiFi(AddPunchWiFiCommand cmd) { 
		cmd.setOwnerId(getTopEnterpriseId(cmd.getOwnerId()));
		PunchRuleOwnerMap map = getOrCreateTargetRuleMap(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getTargetType(), cmd.getTargetId());
		PunchRule pr = punchProvider.getPunchRuleById(map.getPunchRuleId());
		PunchWifiRule pwr = null;
		if(pr.getWifiRuleId()==null){
			pwr = new PunchWifiRule();
			pwr.setOwnerType(cmd.getOwnerType());
			pwr.setOwnerId(cmd.getOwnerId());
			pwr.setCreatorUid(UserContext.current().getUser().getId());
			pwr.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
					.getTime()));
			pwr.setName("punch location rule "+cmd.getTargetId());
			punchProvider.createPunchWifiRule(pwr);
			pr.setWifiRuleId(pwr.getId());
			punchProvider.updatePunchRule(pr);
		}
		else{
			pwr = punchProvider.getPunchWifiRuleById(pr.getWifiRuleId());
		}
		PunchWifi punchWifi = ConvertHelper.convert(cmd, PunchWifi.class);
		punchWifi.setOwnerType(cmd.getOwnerType());
		punchWifi.setOwnerId(cmd.getOwnerId());  
		pr.setCreatorUid(UserContext.current().getUser().getId());
		pr.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
				.getTime()));
		punchWifi.setWifiRuleId(pwr.getId());
		punchProvider.createPunchWifi(punchWifi);
	}

	@Override
	public ListPunchWiFisResponse listPunchWiFis(ListPunchRulesCommonCommand cmd) {
		cmd.setOwnerId(getTopEnterpriseId(cmd.getOwnerId())); 
		ListPunchWiFisResponse response = new ListPunchWiFisResponse();
		PunchRuleOwnerMap map =  punchProvider.getPunchRuleOwnerMapByOwnerAndTarget(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getTargetType(), cmd.getTargetId());
		if(null == map)
			return response;
		PunchRule pr = punchProvider.getPunchRuleById(map.getPunchRuleId());
		if(null == pr)
			return response;
		PunchWifiRule pwr = null;
		if(pr.getWifiRuleId()==null){
//			pwr = new PunchWifiRule();
//			pwr.setOwnerType(cmd.getOwnerType());
//			pwr.setOwnerId(cmd.getOwnerId());
//			pwr.setCreatorUid(UserContext.current().getUser().getId());
//			pwr.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
//					.getTime()));
//			pwr.setName("punch location rule "+cmd.getTargetId());
//			punchProvider.createPunchWifiRule(pwr);
//			pr.setWifiRuleId(pwr.getId());
//			punchProvider.updatePunchRule(pr);
			return response;
		}
		else{
			pwr = punchProvider.getPunchWifiRuleById(pr.getWifiRuleId());
		}
		
		List<PunchWifi> wifis = this.punchProvider.listPunchWifisByRuleId(cmd.getOwnerType(), cmd.getOwnerId(), pwr.getId()) ;
		if(null != wifis){
			response.setWifis(new ArrayList<PunchWiFiDTO>());
			for(PunchWifi wifi : wifis){
				PunchWiFiDTO geoDTO = ConvertHelper.convert(wifi, PunchWiFiDTO.class);
				response.getWifis().add(geoDTO);
			} 
		}
		return response;
	}
	
	
	@Override
	public void updatePunchWiFi(PunchWiFiDTO cmd) { 
		PunchWifi punchWifi = punchProvider.getPunchWifiById(cmd.getId());
		punchWifi.setSsid(cmd.getSsid());
		punchWifi.setMacAddress(cmd.getMacAddress());
		punchProvider.updatePunchWifi(punchWifi);
		
	}
	@Override
	public void deletePunchWiFi(PunchWiFiDTO cmd) { 
		PunchWifi punchWifi = punchProvider.getPunchWifiById(cmd.getId());
		punchProvider.deletePunchWifi(punchWifi);
	} 
	@Override
	public ListPunchSchedulingMonthResponse listPunchScheduling(ListPunchSchedulingMonthCommand cmd) {
		cmd.setOwnerId(getTopEnterpriseId(cmd.getOwnerId()));
		PunchRuleOwnerMap map = getUsefulRuleMap(cmd.getOwnerType(),cmd.getOwnerId(),cmd.getTargetType(),cmd.getTargetId());
		Calendar startCalendar = Calendar.getInstance();
		Calendar endCalendar = Calendar.getInstance();
		startCalendar.setTime(new Date(cmd.getQueryTime())); 
		startCalendar.set(Calendar.DAY_OF_MONTH, 1);
		startCalendar.set(Calendar.MINUTE, 0);
		startCalendar.set(Calendar.SECOND, 0);
		startCalendar.set(Calendar.MILLISECOND, 0);
		endCalendar.setTime(startCalendar.getTime());
		endCalendar.add(Calendar.MONTH, 1);
		List<PunchScheduling> punchSchedulings = punchSchedulingProvider.queryPunchSchedulings(null, Integer.MAX_VALUE,
                new ListingQueryBuilderCallback()  {
			//TODO 联表查询
			@Override
			public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
					SelectQuery<? extends Record> query) {
				query.addConditions(Tables.EH_PUNCH_SCHEDULINGS.OWNER_TYPE.eq(map.getOwnerType()));
				query.addConditions(Tables.EH_PUNCH_SCHEDULINGS.OWNER_ID.eq(map.getOwnerId()));
				query.addConditions(Tables.EH_PUNCH_SCHEDULINGS.TARGET_ID.eq(map.getTargetId()));
				query.addConditions(Tables.EH_PUNCH_SCHEDULINGS.TARGET_TYPE.eq(map.getTargetType()));
				
				query.addConditions(Tables.EH_PUNCH_SCHEDULINGS.RULE_DATE.greaterOrEqual(new java.sql.Date( startCalendar.getTime().getTime())));
				query.addConditions(Tables.EH_PUNCH_SCHEDULINGS.RULE_DATE.lt(new java.sql.Date( endCalendar.getTime().getTime())));
				query.addOrderBy(Tables.EH_PUNCH_SCHEDULINGS.RULE_DATE.asc());
				return null;
			}
		});
		ListPunchSchedulingMonthResponse  response = new ListPunchSchedulingMonthResponse(); 
		response.setSchedulings(new ArrayList<PunchSchedulingDTO>());
		for(;startCalendar.before(endCalendar);startCalendar.add(Calendar.DAY_OF_MONTH,1)){
			PunchSchedulingDTO dto = ConvertHelper.convert(cmd, PunchSchedulingDTO.class);
			dto.setRuleDate(startCalendar.getTimeInMillis());
			String dateString = dateSF.get().format(startCalendar.getTime());
			for(PunchScheduling punchScheduling : punchSchedulings){
				if(dateSF.get().format(punchScheduling.getRuleDate()).equals(dateString)){
					PunchTimeRule timeRule = punchProvider.findPunchTimeRuleById(punchScheduling.getTimeRuleId());
					if(null != timeRule){
						dto.setTimeRuleId(timeRule.getId());
						dto.setTimeRuleName(timeRule.getName());
						dto.setTimeRuleDescription(timeRule.getDescription());
					}else{ 
						//TODO : 减少IO 做成MAP
						LocaleString scheduleLocaleString = localeStringProvider.find( PunchConstants.PUNCH_DEFAULT_SCOPE, PunchConstants.PUNCH_TIME_RULE_NAME,"zh_CN");   
						dto.setTimeRuleName( scheduleLocaleString==null?"":scheduleLocaleString.getText());
					}
				}
			}
			response.getSchedulings().add(dto);
		}
		ListPunchRulesCommonCommand cmd2 = ConvertHelper.convert(cmd, ListPunchRulesCommonCommand.class);
		cmd2.setPageSize(Integer.MAX_VALUE);
		 
		response.setTimeRules(listPunchTimeRuleList(cmd2).getTimeRules());
		return response;
	}
	private PunchRuleOwnerMap getUsefulRuleMap(String ownerType, Long ownerId, String targetType,
			Long targetId) {
		// TODO Auto-generated method stub
		PunchRuleOwnerMap map = null;
		if(targetType.equals(PunchOwnerType.User.getCode())){
			//如果有个人规则就返回个人规则
			map = this.punchProvider.getPunchRuleOwnerMapByOwnerAndTarget(ownerType, ownerId, targetType, targetId);
			if (null != map ) 
				return map;
			//如果没有就按照部门来找规则
			OrganizationDTO deptDTO = findUserDepartment(targetId, ownerId);
			targetId=deptDTO.getId();
		}
		
		int loopMax = 10;
		Organization dept =  organizationProvider.findOrganizationById(targetId);
		map = getPunchRule(null ,dept,loopMax);
		return map;
	}

	@Override
	public HttpServletResponse exportPunchScheduling(ListPunchSchedulingMonthCommand cmd,
			HttpServletResponse response) {
		cmd.setOwnerId(getTopEnterpriseId(cmd.getOwnerId()));
		targetTimeRules.set(punchProvider.queryPunchTimeRules(cmd.getOwnerType(), cmd.getOwnerId(),cmd.getTargetType(),cmd.getTargetId(),  null));
		if(null == targetTimeRules.get())
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,
					"have no time rule");
		// TODO Auto-generated method stub

		if (null == cmd.getOwnerId() ||null == cmd.getOwnerType()) {
			LOGGER.error("Invalid owner type or  Id parameter in the command");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid owner type or  Id parameter in the command");
		}
//		List<PunchDayDetailDTO> dtos = new ArrayList<PunchDayDetailDTO>();
//		PunchOwnerType ownerType = PunchOwnerType.fromCode(cmd.getOwnerType());
		 
		
		URL rootPath = PunchServiceImpl.class.getResource("/");
		String filePath =rootPath.getPath() + this.downloadDir ;
		File file = new File(filePath);
		if(!file.exists())
			file.mkdirs();

		LocaleString scheduleLocaleString = localeStringProvider.find( PunchConstants.PUNCH_EXCEL_SCOPE, PunchConstants.EXCEL_SCHEDULE,
				UserContext.current().getUser().getLocale());
		filePath = filePath +monthSF.get().format(new Date(cmd.getQueryTime()))+ (scheduleLocaleString==null?"排班表":scheduleLocaleString.getText())+".xlsx";
		LOGGER.debug("filePath = "+ filePath);
		//新建了一个文件
		
		createPunchSchedulingsBook(filePath,listPunchScheduling(cmd));
		
		return download(filePath,response);
		 
	}
	
	 private void createPunchSchedulingsBook(String filePath,
			ListPunchSchedulingMonthResponse listPunchScheduling) {
		// TODO Auto-generated method stub
			if (null == listPunchScheduling ||  listPunchScheduling.getSchedulings().size() == 0)
				return;
			Workbook wb = new XSSFWorkbook();
			Sheet sheet = wb.createSheet("sheet1");
			
			this.createPunchSchedulingsBookSheetHead(sheet );
			ArrayList<String> textlist = new ArrayList<String>();
			for(PunchTimeRule rule : targetTimeRules.get()){
				textlist.add(rule.getName());
			}
			textlist.add("");
			//设置格式
			String[] a = {};
			
			
			for (PunchSchedulingDTO statistic : listPunchScheduling.getSchedulings() )
				setNewPunchSchedulingsBookRow(sheet, statistic,textlist);
//			setHSSFValidation(sheet,textlist.toArray(a), 2, 32, 1, 1);
			try {
				
				FileOutputStream out = new FileOutputStream(filePath);
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
	private void createPunchSchedulingsBookSheetHead(Sheet sheet) { 
		Row row = sheet.createRow(sheet.getLastRowNum());
		int i =-1 ;
 
		LocaleString scheduleLocaleString = localeStringProvider.find( PunchConstants.PUNCH_EXCEL_SCOPE, PunchConstants.EXCEL_SCHEDULE, UserContext.current().getUser().getLocale());
 
//		scheduleLocaleString==null?"":scheduleLocaleString.getText()
//		
//		row = sheet.createRow(sheet.getLastRowNum()+1); 
 

		LocaleString ruleLocaleString = localeStringProvider.find( PunchConstants.PUNCH_EXCEL_SCOPE, PunchConstants.EXCEL_RULE , 
				UserContext.current().getUser().getLocale());
		row.createCell(++i).setCellValue(scheduleLocaleString==null?"":scheduleLocaleString.getText());
		row.createCell(++i).setCellValue(ruleLocaleString==null?"":ruleLocaleString.getText());
	}
	private void setNewPunchSchedulingsBookRow(Sheet sheet, PunchSchedulingDTO dto, ArrayList<String> textlist) { 
		Row row = sheet.createRow(sheet.getLastRowNum()+1);
		int i = -1; 
		row.createCell(++i).setCellValue(dateSF.get().format(new Date(dto.getRuleDate())));
//		Label subLabel = new Label(t, td, "");  
//        WritableCellFeatures wcf = new WritableCellFeatures();  
//        List angerlist = new ArrayList();  
//        angerlist.add("电话");//可从数据库中取出  
//        angerlist.add("手机");  
//        angerlist.add("呼机");  
//        wcf.setDataValidationList(angerlist);  
//        subLabel.setCellFeatures(wcf);  
//        ws.addCell(subLabel);  
		row.createCell(++i).setCellValue(null == dto.getTimeRuleId()?"":getTimeRuleById(dto.getTimeRuleId()).getName());
	}
	/** 
     * 设置某些列的值只能输入预制的数据,显示下拉框. 
     *  
     * @param sheet 
     *            要设置的sheet. 
     * @param textlist 
     *            下拉框显示的内容 
     * @param firstRow 
     *            开始行 
     * @param endRow 
     *            结束行 
     * @param firstCol 
     *            开始列 
     * @param endCol 
     *            结束列 
     * @return 设置好的sheet. 
     */  
    public static Sheet setHSSFValidation(Sheet sheet,  
            String[] textlist, int firstRow, int endRow, int firstCol,  
            int endCol) {  
//        // 加载下拉列表内容  
//    	XSSFDataValidationConstraint constraint = new XSSFDataValidationConstraint(textlist);  
//        // 设置数据有效性加载在哪个单元格上,四个参数分别是：起始行、终止行、起始列、终止列  
//        CellRangeAddressList regions = new CellRangeAddressList(firstRow,  
//                endRow, firstCol, endCol);  
//        // 数据有效性对象    
//        CTDataValidation newDataValidation = CTDataValidation.Factory.newInstance();
//        newDataValidation.setType(STDataValidationType.LIST);
//        newDataValidation.setFormula1(constraint.getFormula1());
//        XSSFDataValidation data_validation_list = new XSSFDataValidation(  constraint,
//                regions, newDataValidation );  
//        sheet.addValidationData(data_validation_list);  
//        return sheet;  
        // 构造constraint对象
        DVConstraint constraint = DVConstraint
                .createCustomFormulaConstraint("BB1");
        // 四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList regions = new CellRangeAddressList(firstRow,
                endRow, firstCol, endCol);
        // 数据有效性对象
        HSSFDataValidation data_validation_view = new HSSFDataValidation(
                regions, constraint);
        data_validation_view.createPromptBox("title", "content");
        sheet.addValidationData(data_validation_view);
        return sheet;
    }  
  
    

	private PunchTimeRule getTimeRuleById(Long id){
		for(PunchTimeRule rule : targetTimeRules.get()){
			if(rule.getId().equals(id))
				return rule;
		}
		return null;
	}	
	private PunchTimeRule getTimeRuleByName(String name ){
		for(PunchTimeRule rule : targetTimeRules.get()){
			if(rule.getName().equals(name))
				return rule;
		}
		return null;
	}


	private void storeFile(MultipartFile file, String filePath1)throws Exception{
		OutputStream out = new FileOutputStream(new File(filePath1));
		InputStream in = file.getInputStream();
		byte [] buffer = new byte [1024];
		while(in.read(buffer) != -1){
			out.write(buffer);
		}
		out.close();
		in.close();
	}
	
	@Override
	public void importPunchScheduling(ListPunchRulesCommonCommand cmd ,MultipartFile[] files) {
		// TODO Auto-generated method stub
		cmd.setOwnerId(getTopEnterpriseId(cmd.getOwnerId()));
		targetTimeRules.set(punchProvider.queryPunchTimeRules(cmd.getOwnerType(), cmd.getOwnerId(),cmd.getTargetType(),cmd.getTargetId(),  null));
		ArrayList resultList = new ArrayList();
		String rootPath = System.getProperty("user.dir");
		String filePath = rootPath + File.separator+UUID.randomUUID().toString() + ".xlsx";
		LOGGER.error("importOrganization-filePath="+filePath);
		//将原文件暂存在服务器中
		try {
			this.storeFile(files[0],filePath);
		} catch (Exception e) {
			LOGGER.error("importOrganization-store file fail.message="+e.getMessage());
		}
		try {
			File file = new File(filePath);
			if(!file.exists())
				LOGGER.error("executeImportOrganization-file is not exist.filePath="+filePath);
			InputStream in = new FileInputStream(file);
			resultList = PropMrgOwnerHandler.processorExcel(in);
		} catch (IOException e) {
			LOGGER.error("executeImportOrganization-parse file fail.message="+e.getMessage());
		} /*finally {
			File file = new File(filePath);
			if(file.exists())
				file.delete();
		}*/

		PunchRuleOwnerMap map = getOrCreateTargetRuleMap(cmd.getOwnerType(), cmd.getOwnerId(), 
				cmd.getTargetType(), cmd.getTargetId());
		PunchRule pr = punchProvider.getPunchRuleById(map.getPunchRuleId());
		List<PunchScheduling> list =  convertToPunchSchedulings(resultList);
		for(PunchScheduling scheduling : list){
			scheduling.setOwnerType(cmd.getOwnerType());
			scheduling.setOwnerId(cmd.getOwnerId());
			scheduling.setTargetId(cmd.getTargetId());
			scheduling.setTargetType(cmd.getTargetType()); 
			scheduling.setPunchRuleId(pr.getId());
			scheduling.setCreatorUid(UserContext.current().getUser().getId());
			scheduling.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
					.getTime()));
			punchSchedulingProvider.deletePunchScheduling(scheduling);
			punchSchedulingProvider.createPunchScheduling(scheduling);
		}
	}
	@Override
	public void importPunchLogs( MultipartFile[] files) {
		// TODO Auto-generated method stub
		 
		ArrayList resultList = new ArrayList();
		String rootPath = System.getProperty("user.dir");
		String filePath = rootPath + File.separator+UUID.randomUUID().toString() + ".xlsx";
		LOGGER.error("importOrganization-filePath="+filePath);
		//将原文件暂存在服务器中
		try {
			this.storeFile(files[0],filePath);
		} catch (Exception e) {
			LOGGER.error("importOrganization-store file fail.message="+e.getMessage());
		}
		try {
			File file = new File(filePath);
			if(!file.exists())
				LOGGER.error("executeImportOrganization-file is not exist.filePath="+filePath);
			InputStream in = new FileInputStream(file);
			resultList = PropMrgOwnerHandler.processorExcel(in);
		} catch (IOException e) {
			LOGGER.error("executeImportOrganization-parse file fail.message="+e.getMessage());
		} /*finally {
			File file = new File(filePath);
			if(file.exists())
				file.delete();
		}*/
 
		for(int rowIndex=1;rowIndex<resultList.size();rowIndex++){
			RowResult r = (RowResult)resultList.get(rowIndex);	
			if(r.getA() == null || r.getA().trim().equals("")){
				LOGGER.error("have row is empty.rowIndex="+(rowIndex+1));
				break;
			}
			PunchClockCommand cmd = new PunchClockCommand();
			cmd.setEnterpriseId(Long.valueOf(r.getA()));
			try {
				createPunchLog(cmd,datetimeSF.get().format(new SimpleDateFormat("yyyy/MM/dd  HH:mm:ss").parse(r.getB())),ClockCode.SUCESS.getCode());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				LOGGER.error("row time format wrong.rowIndex="+(rowIndex+1));
				break;
			}
			  
		}
	}
	private List<PunchScheduling> convertToPunchSchedulings(ArrayList list ) {
	 
		List<PunchScheduling> result = new ArrayList<PunchScheduling>();
		for(int rowIndex=1;rowIndex<list.size();rowIndex++){
			RowResult r = (RowResult)list.get(rowIndex);	
			if(r.getA() == null || r.getA().trim().equals("")){
				LOGGER.error("have row is empty.rowIndex="+(rowIndex+1));
				break;
			}
			PunchScheduling ps = new PunchScheduling();
			try {
				ps.setRuleDate(new java.sql.Date( dateSF.get().parse(r.getA()).getTime()));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				LOGGER.error("excel A column date format wrong",e);
				throw  RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
						"excel is wrong format.");
			} 
			PunchTimeRule timeRule = getTimeRuleByName(r.getB());
			if(null != timeRule)
				ps.setTimeRuleId(timeRule.getId());
//			ps.(this.setAreaName(r.getB()));
//			ps.setOrgName(this.getOrgName(r.getC()));
//			ps.setOrgType(this.getOrgType(r.getD()));
//			ps.setTokens(this.getTokens(r.getE()));
//			ps.setAddressName(this.getAddressName(r.getF()));
//			ps.setLongitude(this.getLongitude(r.getG()));
//			ps.setLatitude(this.getLatitude(r.getH()));
//			ps.setCommunityNames(this.getCommunityNames(r.getI()));
			result.add(ps);
		}
		return result;
	}
	
	@Override
	public void updatePunchRuleMap(PunchRuleMapDTO cmd) {
		  
		PunchRuleOwnerMap old = this.punchProvider.getPunchRuleOwnerMapById(cmd.getId()); 
		if( null == old ){
			//如果没有就新建
			throw  RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"can not find this rule");
		}else{
			//有就更新
			old.setTargetId(cmd.getTargetId());
			old.setTargetType(cmd.getTargetType()); 
			this.punchProvider.updatePunchRuleOwnerMap(old);
		}
	}
	@Override
	public void deletePunchPoint(DeleteCommonCommand cmd) {
		PunchGeopoint punchGeopoint = punchProvider.findPunchGeopointById(cmd.getId());
		punchProvider.deletePunchGeopoint(punchGeopoint);
		
	}
	@Override
	public void updatePunchSchedulings(UpdatePunchSchedulingMonthCommand cmd) { 
		if(null == cmd.getSchedulings() ||cmd.getSchedulings().size() == 0 )
			return ;
		PunchRuleOwnerMap map = getOrCreateTargetRuleMap(cmd.getSchedulings().get(0).getOwnerType(), cmd.getSchedulings().get(0).getOwnerId(), 
				cmd.getSchedulings().get(0).getTargetType(), cmd.getSchedulings().get(0).getTargetId());
		PunchRule pr = punchProvider.getPunchRuleById(map.getPunchRuleId());
		if(null == pr)
			return  ;
		for(PunchSchedulingDTO dto : cmd.getSchedulings()){
			
			PunchScheduling punchScheduling = ConvertHelper.convert(dto, PunchScheduling.class);
			punchScheduling.setPunchRuleId(pr.getId());
			punchScheduling.setRuleDate(new java.sql.Date(dto.getRuleDate()));
			punchScheduling.setCreatorUid(UserContext.current().getUser().getId());
			punchScheduling.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
					.getTime()));
			punchSchedulingProvider.deletePunchScheduling(punchScheduling);
			punchSchedulingProvider.createPunchScheduling(punchScheduling);
			
		}
		
	}

	@Override
	public CheckPunchAdminResponse checkPunchAdmin(CheckPunchAdminCommand cmd) {

		checkCompanyIdIsNull(cmd.getOrganizationId());
		cmd.setOrganizationId(getTopEnterpriseId(cmd.getOrganizationId()));
		SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
		CheckPunchAdminResponse response = new CheckPunchAdminResponse();
		response.setIsAdminFlag(NormalFlag.NO.getCode());
		try{
			if(resolver.checkSuperAdmin(UserContext.current().getUser().getId(), cmd.getOrganizationId()) 
					|| resolver.checkOrganizationAdmin(UserContext.current().getUser().getId(), cmd.getOrganizationId()))
				response.setIsAdminFlag(NormalFlag.YES.getCode());
		}
		catch(Exception e){
			LOGGER.error("there is a error when check org admin ",e);
		}
		return response;
	}
}