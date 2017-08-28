package com.everhomes.techpark.punch;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.bigcollection.Accessor;
import com.everhomes.bigcollection.BigCollectionProvider;
import com.everhomes.bus.*;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.rentalv2.RentalNotificationTemplateCode;
import com.everhomes.rest.print.PrintErrorCode;
import com.everhomes.rest.techpark.punch.*;
import com.everhomes.util.*;
import com.google.zxing.WriterException;
import org.apache.lucene.spatial.geohash.GeoHashUtils;
import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.elasticsearch.client.transport.NoNodeAvailableException;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.async.DeferredResult;
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
import com.everhomes.organization.OrganizationMemberDetails;
import com.everhomes.organization.OrganizationMemberLog;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.RestResponse;
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
import com.everhomes.rest.organization.OrganizationMemberDetailDTO;
import com.everhomes.rest.organization.OrganizationMemberStatus;
import com.everhomes.rest.organization.OrganizationMemberTargetType;
import com.everhomes.rest.techpark.punch.admin.AddPunchGroupCommand;
import com.everhomes.rest.techpark.punch.admin.AddPunchPointCommand;
import com.everhomes.rest.techpark.punch.admin.AddPunchTimeRuleCommand;
import com.everhomes.rest.techpark.punch.admin.AddPunchWiFiCommand;
import com.everhomes.rest.techpark.punch.admin.DeleteCommonCommand;
import com.everhomes.rest.techpark.punch.admin.DeletePunchRuleMapCommand;
import com.everhomes.rest.techpark.punch.admin.GetPunchGroupCommand;
import com.everhomes.rest.techpark.punch.admin.GetTargetPunchAllRuleCommand;
import com.everhomes.rest.techpark.punch.admin.GetTargetPunchAllRuleResponse;
import com.everhomes.rest.techpark.punch.admin.ListPunchDetailsCommand;
import com.everhomes.rest.techpark.punch.admin.ListPunchDetailsResponse;
import com.everhomes.rest.techpark.punch.admin.ListPunchGroupsCommand;
import com.everhomes.rest.techpark.punch.admin.ListPunchGroupsResponse;
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
import com.everhomes.rest.techpark.punch.admin.PunchGroupDTO;
import com.everhomes.rest.techpark.punch.admin.PunchLocationRuleDTO;
import com.everhomes.rest.techpark.punch.admin.PunchSchedulingDTO;
import com.everhomes.rest.techpark.punch.admin.PunchSchedulingEmployeeDTO;
import com.everhomes.rest.techpark.punch.admin.PunchSpecialDayDTO;
import com.everhomes.rest.techpark.punch.admin.PunchTargetType;
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
import com.everhomes.rest.uniongroup.GetUniongroupConfiguresCommand;
import com.everhomes.rest.uniongroup.SaveUniongroupConfiguresCommand;
import com.everhomes.rest.uniongroup.UniongroupConfiguresDTO;
import com.everhomes.rest.uniongroup.UniongroupTarget;
import com.everhomes.rest.uniongroup.UniongroupType;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.rest.user.MessageChannelType;
import com.everhomes.scheduler.RunningFlag;
import com.everhomes.scheduler.ScheduleProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.uniongroup.UniongroupConfigureProvider;
import com.everhomes.uniongroup.UniongroupMemberDetail;
import com.everhomes.uniongroup.UniongroupService;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.user.admin.SystemUserPrivilegeMgr;
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
	private UniongroupConfigureProvider uniongroupConfigureProvider;
	@Autowired
    private UniongroupService uniongroupService;
	
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

	@Autowired
	private LocaleTemplateService localeTemplateService;

	@Autowired
	private BigCollectionProvider bigCollectionProvider;

    @Autowired
    private LocalBusOneshotSubscriberBuilder localBusSubscriberBuilder;

    @Autowired
    private LocalBus localBus;

    @Autowired
    private BusBridgeProvider busBridgeProvider;


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
								ApprovalStatus.OVERTIME.getCode())  ? ExceptionStatus.NORMAL
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
								ApprovalStatus.OVERTIME.getCode())) &&(punchDayLog.getAfternoonStatus().equals(
										PunchStatus.NORMAL.getCode())||punchDayLog.getAfternoonStatus().equals(
                    ApprovalStatus.OVERTIME.getCode()))? ExceptionStatus.NORMAL
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

	/**
	 * 刷新取某人某日的打卡日状态 
	 * */
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
		newPunchDayLog.setStatusList(pdl.getStatusList());
		newPunchDayLog.setPunchCount(pdl.getPunchCount());
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
		if (!isWorkDay(logDay.getTime(),pr,userId)
				|| dateSF.get().format(now).equals(dateSF.get().format(logDay.getTime()))) {
			pdl.setExceptionStatus(ExceptionStatus.NORMAL.getCode());
		} else {
			if(pdl.getPunchTimesPerDay().equals(PunchTimesPerDay.TWICE.getCode())){
			pdl.setExceptionStatus(punchDayLog.getStatus().equals(
					PunchStatus.NORMAL.getCode())||punchDayLog.getStatus().equals(
                    ApprovalStatus.OVERTIME.getCode()) ? ExceptionStatus.NORMAL
					.getCode() : ExceptionStatus.EXCEPTION.getCode());
			}
			else if(pdl.getPunchTimesPerDay().equals(PunchTimesPerDay.FORTH.getCode())){
				//上午为加班或者普通 且 下午为加班或者普通 则
				pdl.setExceptionStatus((punchDayLog.getMorningStatus().equals(
						PunchStatus.NORMAL.getCode())||punchDayLog.getMorningStatus().equals(
                        ApprovalStatus.OVERTIME.getCode()))&&(punchDayLog.getAfternoonStatus().equals(
										PunchStatus.NORMAL.getCode())||punchDayLog.getAfternoonStatus().equals(
                        ApprovalStatus.OVERTIME.getCode())) ? ExceptionStatus.NORMAL
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
				Organization organization = organizationProvider.findOrganizationById(companyId);
				if (exceptionRequest.getRequestType().equals(
						PunchRquestType.REQUEST.getCode())) {
					// 对于申请
					punchExceptionDTO.setExceptionComment(exceptionRequest
							.getDescription());
					OrganizationMember member = findOrganizationMemberByOrgIdAndUId(exceptionRequest.getUserId(), organization.getPath());
					if (null == member ) {
						punchExceptionDTO.setName("无此人");
					} else {
						punchExceptionDTO.setName(member.getContactName());
					}
				} else {
					// 审批
					punchExceptionDTO.setExceptionComment(exceptionRequest
							.getProcessDetails());
					OrganizationMember member = findOrganizationMemberByOrgIdAndUId(exceptionRequest.getUserId(), organization.getPath());
					if (null == member ) {
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
        pdl = calculateDayLogByeverypunch(userId, companyId, logDay, pdl, punchDayLog);
        //对statuslist进行统一处理,从每次打卡状态->每个班状态
        String[] statusArrary = pdl.getStatusList().split(PunchConstants.STATUS_SEPARATOR);
        String statusList = "";
        if (statusArrary != null && statusArrary.length > 1) {
            for(int i = 0;i<statusArrary.length/2;i++){
                String status = "";
                if (statusArrary[2 * i].equals(String.valueOf(PunchStatus.UNPUNCH.getCode()))) {
                    status = statusArrary[2 * i];
                } else if (statusArrary[2 * i + 1].equals(String.valueOf(PunchStatus.UNPUNCH.getCode()))) {
                    status = String.valueOf(PunchStatus.FORGOT.getCode());
                } else if (statusArrary[2 * i + 1].equals(String.valueOf(PunchStatus.LEAVEEARLY.getCode()))) {
                    if (statusArrary[2 * i].equals(String.valueOf(PunchStatus.BELATE.getCode()))) {
                        status = String.valueOf(PunchStatus.BLANDLE.getCode());
                    } else if (statusArrary[2 * i].equals(String.valueOf(PunchStatus.NORMAL.getCode()))) {
                        status = String.valueOf(PunchStatus.BELATE.getCode());
                    }
                } else if (statusArrary[2 * i + 1].equals(String.valueOf(PunchStatus.NORMAL.getCode()))) {
                    status = statusArrary[2 * i];
                }
                if(i == 0){
                    statusList = status;
                }else{
                    statusList = statusList + PunchConstants.STATUS_SEPARATOR + status;
                }

            }
        }
        pdl.setStatusList(statusList);
        return  pdl;
    }
    private PunchLogsDay calculateDayLogByeverypunch(Long userId, Long companyId,
                Calendar logDay, PunchLogsDay pdl, PunchDayLog punchDayLog) throws ParseException {
            List<PunchLog> punchLogs = punchProvider.listPunchLogsByDate(userId,
				companyId, dateSF.get().format(logDay.getTime()),
				ClockCode.SUCESS.getCode());
		if(null != punchLogs){
			pdl.setPunchCount(punchLogs.size());
			for (PunchLog log : punchLogs){
				pdl.getPunchLogs().add(ConvertHelper.convert(log,PunchLogDTO.class ));
			}
		}else{
			pdl.setPunchCount(0);
		}
		PunchRule pr = this.getPunchRule(PunchOwnerType.ORGANIZATION.getCode(),companyId, userId );
		if(null == pr)
			throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
					PunchServiceErrorCode.ERROR_ENTERPRISE_DIDNOT_SETTING,
					"have no punch rule"); 
		//获取当天的排班
		PunchTimeRule punchTimeRule = getPunchTimeRuleByRuleIdAndDate(pr,logDay.getTime(),userId);
		//没有规则就是没有排班,就是非工作日
		if (null == punchTimeRule){
			pdl.setStatusList(PunchStatus.NOTWORKDAY.getCode()+"");
			pdl.setPunchTimesPerDay(PunchTimesPerDay.TWICE.getCode());
			pdl.setPunchStatus(PunchStatus.NORMAL.getCode());
			pdl.setMorningPunchStatus(PunchStatus.NORMAL.getCode());
			pdl.setAfternoonPunchStatus(PunchStatus.NORMAL.getCode());
			pdl.setExceptionStatus(ExceptionStatus.NORMAL.getCode()); 
			return pdl;
		}
		pdl.setTimeRuleId(punchTimeRule.getId());
		pdl.setTimeRuleName(punchTimeRule.getName());
		pdl.setPunchOrganizationId(pr.getPunchOrganizationId());
		pdl.setRuleType(punchTimeRule.getRuleType());
		pdl.setPunchTimesPerDay(punchTimeRule.getPunchTimesPerDay());
		//对于已离职和未入职的判断 
		List<OrganizationMember> organizationMembers = organizationService.listOrganizationMemberByOrganizationPathAndUserId("/"+companyId,userId );
		if(organizationMembers == null || organizationMembers.size() == 0){
			//找不到就是已离职
			pdl.setStatusList(PunchStatus.RESIGNED.getCode()+"");
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
					pdl.setStatusList(PunchStatus.NONENTRY.getCode()+"");
					pdl.setPunchTimesPerDay(PunchTimesPerDay.TWICE.getCode());
					pdl.setMorningPunchStatus(PunchStatus.NONENTRY.getCode());
					pdl.setAfternoonPunchStatus(PunchStatus.NONENTRY.getCode());
					punchDayLog.setStatus(ApprovalStatus.NONENTRY.getCode());
					pdl.setPunchStatus(ApprovalStatus.NONENTRY.getCode());
					return pdl;
				}
			}
		}
		
		// 如果有排班 而且 零次打卡记录
		if (null == punchLogs || punchLogs.size() == 0) {
//			if (!isWorkDay(logDay.getTime(),pr)|| dateSF.get().format(now).equals(dateSF.get().format(logDay.getTime()))) {
//				// 如果非工作日或者当天，不增pdl直接下一天
//				return null;
//			} 
			for(int i = 1; i<= (int) punchTimeRule.getPunchTimesPerDay();i++){
				if(null == pdl.getStatusList()){
					pdl.setStatusList(PunchStatus.UNPUNCH.getCode()+"");
				}else{
					pdl.setStatusList(pdl.getStatusList()+PunchConstants.STATUS_SEPARATOR+PunchStatus.UNPUNCH.getCode());
				}
			} 
			pdl.setPunchStatus(PunchStatus.UNPUNCH.getCode());
			pdl.setMorningPunchStatus(PunchStatus.UNPUNCH.getCode());
			pdl.setAfternoonPunchStatus(PunchStatus.UNPUNCH.getCode());
			pdl.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode()); 
			makeExceptionForDayList(userId, companyId, logDay, pdl);
			return pdl;
		}
		
		List<PunchLog> efficientLogs = new ArrayList<>();
		//2次打卡的计算
		if(PunchTimesPerDay.TWICE.getCode().equals(punchTimeRule.getPunchTimesPerDay())){
			PunchLog onDutyLog = findPunchLog(punchLogs, PunchType.ON_DUTY.getCode(), 1);
			if(onDutyLog == null){
				onDutyLog = new PunchLog();
				onDutyLog.setPunchStatus(PunchStatus.UNPUNCH.getCode());
			}
			PunchLog offDutyLog = findPunchLog(punchLogs, PunchType.OFF_DUTY.getCode(), 1);
			if(offDutyLog == null){
				offDutyLog = new PunchLog();
				offDutyLog.setPunchStatus(PunchStatus.UNPUNCH.getCode());
			}
			efficientLogs.add(onDutyLog);
			efficientLogs.add(offDutyLog);  
//			/**上班时间*/
			Calendar arriveCalendar = Calendar.getInstance();
			PunchLog punchLog = findPunchLog(punchLogs, PunchType.ON_DUTY.getCode(),1);
			Calendar leaveCalendar = Calendar.getInstance();
			
			if(null == punchLog){
                pdl.setStatusList(PunchStatus.UNPUNCH.getCode()+"");
                pdl.setPunchStatus(PunchStatus.UNPUNCH.getCode());
				pdl.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode()); 
				makeExceptionForDayList(userId, companyId, logDay, pdl);
				return pdl;
			}else{
				arriveCalendar.setTime(punchLog.getPunchTime());
				/**下班时间*/
				punchLog = findPunchLog(punchLogs, PunchType.OFF_DUTY.getCode(),1);
				if(null == punchLog){
					pdl.setStatusList(PunchStatus.FORGOT.getCode()+"");
					pdl.setPunchStatus(PunchStatus.FORGOT.getCode()); 
					pdl.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode()); 
					makeExceptionForDayList(userId, companyId, logDay, pdl);
					return pdl;
				}else{
				leaveCalendar.setTime(punchLog.getPunchTime());
				}
			}
//			 
//			
			/**最早上班时间*/
			Calendar startMinTime = Calendar.getInstance();
			/**最晚上班时间*/
			Calendar startMaxTime = Calendar.getInstance();
//			Calendar workTime = Calendar.getInstance();
			startMinTime.setTimeInMillis(punchLogs.get(0).getPunchDate().getTime()+ punchTimeRule.getStartEarlyTimeLong()); 
			startMaxTime.setTimeInMillis(punchLogs.get(0).getPunchDate().getTime()+ punchTimeRule.getStartLateTimeLong());
//			workTime.setTimeInMillis(punchLogs.get(0).getPunchDate().getTime() + punchTimeRule.getWorkTimeLong()); 
			Calendar AfternoonArriveCalendar = Calendar.getInstance();
			AfternoonArriveCalendar.setTimeInMillis((punchLogs.get(0).getPunchDate().getTime()+
					(punchTimeRule.getAfternoonArriveTimeLong()==null?convertTimeToGMTMillisecond(punchTimeRule.getAfternoonArriveTime()):punchTimeRule.getAfternoonArriveTimeLong())));
			Calendar NoonLeaveTimeCalendar = Calendar.getInstance();
			NoonLeaveTimeCalendar.setTimeInMillis((punchLogs.get(0).getPunchDate().getTime()+
					(punchTimeRule.getNoonLeaveTimeLong()==null?convertTimeToGMTMillisecond(punchTimeRule.getNoonLeaveTime()):punchTimeRule.getNoonLeaveTimeLong())));
			 
			long realWorkTime = 0L;  
			if(leaveCalendar.after(AfternoonArriveCalendar)&&arriveCalendar.before(NoonLeaveTimeCalendar)){ 
				realWorkTime = leaveCalendar.getTimeInMillis() - arriveCalendar.getTimeInMillis()
					- punchTimeRule.getAfternoonArriveTime().getTime() + punchTimeRule.getNoonLeaveTime().getTime();
			}else {
				realWorkTime = leaveCalendar.getTimeInMillis() - arriveCalendar.getTimeInMillis();
						
			}
			punchDayLog.setWorkTime( convertTime(realWorkTime) );
		}
		else if(PunchTimesPerDay.FORTH.getCode().equals(punchTimeRule.getPunchTimesPerDay())){ 
			Long workTimeLong = 0L;
			PunchLog onDutyLog = findPunchLog(punchLogs, PunchType.ON_DUTY.getCode(), 1);
			if(onDutyLog == null){
				onDutyLog = new PunchLog(); 
				onDutyLog.setPunchStatus(PunchStatus.UNPUNCH.getCode());
			}
			efficientLogs.add(onDutyLog);
			PunchLog offDutyLog = findPunchLog(punchLogs, PunchType.OFF_DUTY.getCode(), 1);
			if(offDutyLog == null){
				offDutyLog = new PunchLog();
				offDutyLog.setPunchStatus(PunchStatus.UNPUNCH.getCode());
			}
			efficientLogs.add(offDutyLog);
			if(onDutyLog.getPunchTime() != null && offDutyLog.getPunchTime()!=null){
				workTimeLong += offDutyLog.getPunchTime().getTime() - onDutyLog.getPunchTime().getTime();
			}
				
			onDutyLog = findPunchLog(punchLogs, PunchType.ON_DUTY.getCode(), 2);
			if(onDutyLog == null){
				onDutyLog = new PunchLog();
				onDutyLog.setPunchStatus(PunchStatus.UNPUNCH.getCode());
			}
			efficientLogs.add(onDutyLog);
			offDutyLog = findPunchLog(punchLogs, PunchType.OFF_DUTY.getCode(), 2);
			if(offDutyLog == null){
				offDutyLog = new PunchLog();
				offDutyLog.setPunchStatus(PunchStatus.UNPUNCH.getCode());
			}
			efficientLogs.add(offDutyLog);

			if(onDutyLog.getPunchTime() != null && offDutyLog.getPunchTime()!=null){
				workTimeLong += offDutyLog.getPunchTime().getTime() - onDutyLog.getPunchTime().getTime();
			}
			
			punchDayLog.setWorkTime(convertTime(workTimeLong));
		}else{
			//多次打卡 
			List<PunchTimeInterval> intervals = punchProvider.listPunchTimeIntervalByTimeRuleId(punchTimeRule.getId());
			if(null == intervals )
				throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
	 					PunchServiceErrorCode.ERROR_ENTERPRISE_DIDNOT_SETTING,
	 				"公司没有设置打卡时间段");
//			Long lateTimeLong = 0L;
			Long workTimeLong = 0L;
			for(int punchIntervalNo = 1;punchIntervalNo <= intervals.size();punchIntervalNo++){
				PunchLog onDutyLog = findPunchLog(punchLogs, PunchType.ON_DUTY.getCode(), punchIntervalNo);
				if(onDutyLog == null){
					onDutyLog = new PunchLog(); 
					onDutyLog.setPunchStatus(PunchStatus.UNPUNCH.getCode());
				}
				efficientLogs.add(onDutyLog);
				PunchLog offDutyLog = findPunchLog(punchLogs, PunchType.OFF_DUTY.getCode(), punchIntervalNo);
				if(offDutyLog == null){
					offDutyLog = new PunchLog();
					offDutyLog.setPunchStatus(PunchStatus.UNPUNCH.getCode());
				}
				efficientLogs.add(offDutyLog);
				
				if(onDutyLog.getPunchTime() != null && offDutyLog.getPunchTime()!=null){
					workTimeLong += offDutyLog.getPunchTime().getTime() - onDutyLog.getPunchTime().getTime();
				}


			}
			punchDayLog.setWorkTime(convertTime(workTimeLong));
		}
		for(PunchLog log : efficientLogs){
			if(null == pdl.getStatusList()){
				pdl.setStatusList(statusToString(log.getPunchStatus())+"");
			}else{
				pdl.setStatusList(pdl.getStatusList()+PunchConstants.STATUS_SEPARATOR+statusToString(log.getPunchStatus()));
			}
		}
		makeExceptionForDayList(userId, companyId, logDay, pdl);
		return pdl;
	}
	private void calculateArriveStatus(Calendar arriveCalendar, Calendar startTime, PunchLogsDay pdl) {
		if(arriveCalendar.before(startTime)){
			pdl.setPunchStatus(PunchStatus.NORMAL.getCode());
			pdl.setExceptionStatus(ExceptionStatus.NORMAL.getCode()); 
		}else{ 
			pdl.setPunchStatus(PunchStatus.BELATE.getCode());
			pdl.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
		}
	}
	private void calculateLeaveStatus(Calendar leaveCalendar, Calendar endTime, PunchLogsDay pdl) {
		if(leaveCalendar.before(endTime)){ 
			if (pdl.getPunchStatus().equals(PunchStatus.NORMAL.getCode())) {
				pdl.setPunchStatus(PunchStatus.LEAVEEARLY.getCode());
				pdl.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
			} else {
				pdl.setPunchStatus(PunchStatus.BLANDLE.getCode());
				pdl.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
			}
		} 
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
						|| pdl.getPunchStatus().equals(ApprovalStatus.OVERTIME.getCode())) {
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
				Organization organization = organizationProvider.findOrganizationById(companyId);
				if (exceptionRequest.getRequestType().equals(
						PunchRquestType.REQUEST.getCode())) {
					// 对于申请
					punchExceptionDTO.setExceptionComment(exceptionRequest
							.getDescription());
					OrganizationMember member = findOrganizationMemberByOrgIdAndUId(exceptionRequest.getUserId(), organization.getPath());
					if (null == member) {
						punchExceptionDTO.setName("无此人");
					} else {
						punchExceptionDTO.setName(member.getContactName());
					}
				} else {
					// 审批
					punchExceptionDTO.setExceptionComment(exceptionRequest
							.getProcessDetails());
					OrganizationMember member = findOrganizationMemberByOrgIdAndUId(exceptionRequest.getUserId(), organization.getPath());
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

 
 

//	private List<Calendar> getMinAndMaxTimeFromPunchlogs(List<PunchLog> punchLogs) {
//		List<Calendar> result = new ArrayList<Calendar>();
//		Calendar logCalendar = Calendar.getInstance();
//		Calendar maxCalendar = Calendar.getInstance();
//		Calendar minCalendar = Calendar.getInstance();
//		maxCalendar.setTime((Date) punchLogs.get(0).getPunchTime().clone());
//		minCalendar.setTime((Date) punchLogs.get(0).getPunchTime().clone());
//		if (punchLogs.size() != 1) {
//
//			for (PunchLog punchlog : punchLogs) {
//				logCalendar.setTime((Date) punchlog.getPunchTime().clone());
//				if (logCalendar.before(minCalendar))
//					minCalendar.setTime(logCalendar.getTime());
//				if (logCalendar.after(maxCalendar))
//					maxCalendar.setTime(logCalendar.getTime());
//
//			}
//		}
//		result.add(minCalendar);
//		result.add(maxCalendar);
//		return result;
//	}

	@Override
	public PunchClockResponse createPunchLog(PunchClockCommand cmd) {

		cmd.setEnterpriseId(getTopEnterpriseId(cmd.getEnterpriseId()));
		String punchTime = datetimeSF.get().format(new Date());
		return createPunchLog(cmd,punchTime);
		
	}

	private PunchClockResponse createPunchLog(PunchClockCommand cmd, String punchTime) {
		//
		byte punchCode;
		try{
			punchCode= verifyPunchClock(cmd).getCode();
			return createPunchLog(cmd, punchTime,  punchCode);
		}catch(Exception e){
			//有报错就表示不成功
			PunchLog punchLog = ConvertHelper.convert(cmd, PunchLog.class);
			punchLog.setPunchStatus(ClockCode.FAIL.getCode());
			punchProvider.createPunchLog(punchLog);
			throw e;
		}
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
		PunchLogDTO punchType = getPunchType(userId,cmd.getEnterpriseId(),punchLog.getPunchTime());
        response.setClockStatus(punchType.getClockStatus());
		punchLog.setRuleTime(punchType.getRuleTime());
		punchLog.setStatus(punchType.getClockStatus());
		//如果是下班之后打卡当做下班打卡
		if(punchType.getPunchType().equals(PunchType.FINISH.getCode())){
			punchType.setPunchType(PunchType.OFF_DUTY.getCode());
		}

		punchLog.setPunchIntervalNo(punchType.getPunchIntervalNo());
		if(null ==cmd.getPunchType()){ 
			punchLog.setPunchType(punchType.getPunchType()); 
		}else{
			switch(PunchType.fromCode(punchType.getPunchType())){
			case NOT_WORKDAY:
			case MEIPAIBAN:
				punchLog.setPunchStatus(ClockCode.FAIL.getCode());
				break;
			default:
				if(!punchLog.getPunchType().equals(punchType.getPunchType())){
					throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
		 					PunchServiceErrorCode.ERROR_PUNCH_TYPE,"重新获取上下班类型");
				}
				break;
			}
		}
		
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
		PunchTimeRule ptr = getPunchTimeRuleByRuleIdAndDate(pr,punCalendar.getTime(),userId);
		 
		Calendar yesterday = Calendar.getInstance(); 
		yesterday.setTime(punCalendar.getTime());
		yesterday.add(Calendar.DATE, -1);
		PunchTimeRule yesterdayPtr = getPunchTimeRuleByRuleIdAndDate(pr,yesterday.getTime(),userId);
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

		List<PunchGeopoint> punchGeopoints = punchProvider.listPunchGeopointsByOwner(PunchOwnerType.ORGANIZATION.getCode(),pr.getPunchOrganizationId());
		List<PunchWifi> wifis = punchProvider.listPunchWifsByOwner(PunchOwnerType.ORGANIZATION.getCode(),pr.getPunchOrganizationId());
		if(null != wifis && null != cmd.getWifiMac()){
			for(PunchWifi wifi : wifis){
				if(null != wifi.getMacAddress() && wifi.getMacAddress().toLowerCase().equals(cmd.getWifiMac().toLowerCase()))
					return ClockCode.SUCESS;
			}
			
		}
		
		//参数有地址规则看地址范围是否正确,不正确则报错 
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

//	@Override
//	public void createPunchRule(AddPunchRuleCommand cmd) {
//		Long userId = UserContext.current().getUser().getId();
//		checkCompanyIdIsNull(cmd.getEnterpriseId());
//		PunchTimeRule punchRule = punchProvider.findPunchTimeRuleByCompanyId(cmd
//				.getEnterpriseId()); 
//		if(punchRule == null) {
//			punchRule = ConvertHelper.convert(cmd, PunchTimeRule.class);
// 			punchRule.setAfternoonArriveTime(convertTime(cmd.getAfternoonArriveTime()));
//			punchRule.setPunchTimesPerDay(cmd.getPunchTimesPerDay());
//			punchRule.setNoonLeaveTime(convertTime(cmd.getNoonLeaveTime())); 
//			convertTime(punchRule, cmd.getStartEarlyTime(), cmd.getStartLateTime(), cmd.getEndEarlyTime());
//			punchRule.setCreatorUid(userId);
//			punchRule.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
//					.getTime()));
//			punchProvider.createPunchTimeRule(punchRule);
////			createPunchGeopoints(userId, cmd.getPunchGeoPoints(),cmd.getEnterpriseId());
//		}
//
//	}
//	@Override
//	public void updatePunchRule(UpdatePunchRuleCommand cmd) {
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
//	}
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
//	@Override
//	public GetPunchRuleCommandResponse getPunchRuleByCompanyId(
//			GetPunchRuleCommand cmd) {
//		GetPunchRuleCommandResponse response = new GetPunchRuleCommandResponse();
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
//		return response;
//	}

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

//	@Override
//	public void deletePunchRule(DeletePunchRuleCommand cmd) {
//		PunchTimeRule punchRule = punchProvider.findPunchTimeRuleByCompanyId(cmd
//				.getEnterpriseId());
//		if (punchRule != null) {
//			punchProvider.deletePunchTimeRule(punchRule);
//			List<PunchGeopoint> geopoints = punchProvider
//					.listPunchGeopointsByCompanyId(cmd.getEnterpriseId());
//			if (geopoints != null && geopoints.size() > 0) {
//				for (PunchGeopoint punchGeopoint : geopoints) {
//					punchProvider.deletePunchGeopoint(punchGeopoint);
//				}
//			}
//		}
//	}

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
		Calendar anchorCalendar = Calendar.getInstance();
		anchorCalendar.setTimeInMillis(startCalendar.getTimeInMillis());
		PunchRule  pr = getPunchRule(statistic.getOwnerType(), statistic.getOwnerId(), statistic.getUserId());
		if(null == pr)
			return 0;
		if(pr.getRuleType().equals(PunchRuleType.PAIBAN.getCode())){
			List<PunchScheduling> punchSchedulings = punchSchedulingProvider.queryPunchSchedulings(null, Integer.MAX_VALUE,new ListingQueryBuilderCallback()  {
				@Override
				public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
						SelectQuery<? extends Record> query) { 
	//				query.addConditions(Tables.EH_PUNCH_SCHEDULINGS.RULE_DATE.gt(new java.sql.Date( startCalendar.getTime()));
					query.addConditions(Tables.EH_PUNCH_SCHEDULINGS.RULE_DATE.greaterOrEqual(new java.sql.Date(startCalendar.getTime().getTime())));
					query.addConditions(Tables.EH_PUNCH_SCHEDULINGS.RULE_DATE.lessOrEqual(new java.sql.Date( endCalendar.getTime().getTime())));
					query.addConditions(Tables.EH_PUNCH_SCHEDULINGS.TIME_RULE_ID.isNotNull()); 
					query.addConditions(Tables.EH_PUNCH_SCHEDULINGS.TIME_RULE_ID.ne(0L)); 
					query.addConditions(Tables.EH_PUNCH_SCHEDULINGS.TARGET_ID.ne(statistic.getDetailId()));
					query.addConditions(Tables.EH_PUNCH_SCHEDULINGS.PUNCH_RULE_ID.eq(pr.getId()));
					
					query.addOrderBy(Tables.EH_PUNCH_SCHEDULINGS.RULE_DATE.asc());
					return null;
				}
			});
			if(null == punchSchedulings)
				return 0;
			return punchSchedulings.size();
		}else if(pr.getRuleType().equals(PunchRuleType.GUDING.getCode())){
			int workday=0;
			for(;!anchorCalendar.after(endCalendar);anchorCalendar.add(Calendar.DAY_OF_MONTH, 1)){
				PunchTimeRule timerule = getPunchTimeRuleByRuleIdAndDate(pr,anchorCalendar.getTime(), statistic.getUserId());
				if(null != timerule)
					workday++;
			}
			return workday;
		}
		
		return 0;
	}

	@Override
	public boolean isWorkDay(Date date1,PunchRule punchRule,Long userId) {
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

		PunchTimeRule punchTimeRule = getPunchTimeRuleByRuleIdAndDate(punchRule ,date1,userId);
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
	public boolean isWorkTime(Time time, PunchRule punchRule,Date date,Long userId) {
		return isWorkTime(time, getPunchTimeRuleByRuleIdAndDate(punchRule, date,userId)) ;
	} 
	
	@Override
	public boolean isSameDay(Date date1, Date date2) {
		return dateSF.get().format(date1).equals(dateSF.get().format(date2));
	}
	
	@Override
	public boolean isRestTime(Date fromTime, Date endTime, PunchRule punchRule,Long userId) {
		return isSameDay(fromTime, endTime)
				&& timeSF.get().format(fromTime).equals(timeSF.get().format(getPunchTimeRuleByRuleIdAndDate(punchRule, fromTime,userId).getNoonLeaveTime()))
				&& timeSF.get().format(endTime).equals(timeSF.get().format(getPunchTimeRuleByRuleIdAndDate(punchRule, endTime,userId).getAfternoonArriveTime()));
	
	}
	
//	@Override
//	public PunchTimeRule getPunchTimeRule(PunchRule punchRule) {
//		if (punchRule != null && punchRule.getTimeRuleId() != null) {
//			return punchProvider.findPunchTimeRuleById(punchRule.getTimeRuleId());
//		}
//		return null;
//	}

//	@Override
//	public void createPunchExceptionRequest(AddPunchExceptionRequestCommand cmd) {
//		Long userId = UserContext.current().getUser().getId();
//		checkCompanyIdIsNull(cmd.getEnterpriseId());
//		if (cmd.getRequestDescription() == null
//				|| cmd.getRequestDescription().equals(0))
//			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
//					ErrorCodes.ERROR_INVALID_PARAMETER,
//					"Invalid description parameter in the command");
////		PunchRule rule = this.punchProvider.getPunchRuleByCompanyId(cmd.getEnterpriseId());
//		PunchExceptionRequest punchExceptionRequest = new PunchExceptionRequest();
//		punchExceptionRequest.setEnterpriseId(cmd.getEnterpriseId());
//		punchExceptionRequest.setRequestType(PunchRquestType.REQUEST.getCode());
//		punchExceptionRequest.setUserId(userId);
//		punchExceptionRequest.setCreatorUid(userId);
//		punchExceptionRequest.setCreateTime(new Timestamp(DateHelper
//				.currentGMTTime().getTime()));
//		punchExceptionRequest.setDescription(cmd.getRequestDescription());
//		punchExceptionRequest.setPunchDate(java.sql.Date.valueOf(cmd
//				.getPunchDate()));
//		punchExceptionRequest.setStatus(ExceptionProcessStatus.WAITFOR
//				.getCode());
//		punchExceptionRequest.setViewFlag(ViewFlags.NOTVIEW.getCode()); 
//		punchProvider.createPunchExceptionRequest(punchExceptionRequest);
//
//	}

	@Override
	public ListPunchExceptionRequestCommandResponse listExceptionRequests(
			ListPunchExceptionRequestCommand cmd) {
		checkCompanyIdIsNull(cmd.getEnterpriseId());
		Organization organization = organizationProvider.findOrganizationById(cmd.getEnterpriseId());
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
					OrganizationMember member = findOrganizationMemberByOrgIdAndUId(dto.getUserId(), organization.getPath());

					if (null == member) {
					} else {
						dto.setUserName(member.getContactName());
						dto.setUserPhoneNumber(member.getContactToken());
					}
					
					if (null != dto.getOperatorUid()
							&& 0 != dto.getOperatorUid()) {

						member = findOrganizationMemberByOrgIdAndUId(dto.getOperatorUid(), organization.getPath());

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
							dto.setOperatorName("");
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
	public PunchTimeRule getPunchTimeRuleByRuleIdAndDate(PunchRule pr,Date date,Long userId){
		Long id = getPunchTimeRuleIdByRuleIdAndDate(pr, date, userId);
		if(null != id && !id.equals(0L))
			return punchProvider.getPunchTimeRuleById(id);
		return null;
		
	}
	public Long getPunchTimeRuleIdByRuleIdAndDate(PunchRule pr,Date date,Long userId){
		//分为排班制和固定班
		if(pr.getRuleType().equals(PunchRuleType.GUDING.getCode())){
			//固定班次
			//看是否为特殊日期
			PunchSpecialDay specialDay = punchProvider.findSpecialDayByDateAndOrgId(pr.getPunchOrganizationId(),date);
			if(null != specialDay){
				if(specialDay.getStatus().equals(NormalFlag.YES.getCode())){
					return null;
				}else {
					return specialDay.getTimeRuleId();
				}
			}
            //如果为节假日则返回null  如果是节假调休日,用调休日期代替
            java.sql.Date punchDate = new java.sql.Date(date.getTime());
            punchDate = checkHoliday(pr,punchDate);
			//看是循环timerule找当天的timeRule
			List<PunchTimeRule> timeRules = punchProvider.listPunchTimeRuleByOwner(PunchOwnerType.ORGANIZATION.getCode(),pr.getPunchOrganizationId());
			if(null != timeRules)
				for(PunchTimeRule timeRule :  timeRules){
					Integer openWeek = Integer.parseInt(timeRule.getOpenWeekday(), 2);
					Integer weekDayInt = getWeekDayInt(punchDate);
					if(weekDayInt.equals(openWeek&weekDayInt)){
						return timeRule.getId();
					}
				}
			return null;	
		}else{
			//查询当天那个人的班次
            OrganizationMember member = organizationProvider.findOrganizationMemberByOrgIdAndUId(userId, pr.getOwnerId());
            PunchScheduling punchScheduling = this.punchSchedulingProvider.getPunchSchedulingByRuleDateAndTarget(pr.getPunchOrganizationId(),member.getDetailId(),date);
			if(null == punchScheduling || punchScheduling.getPunchRuleId() == null )
				return null ;
			return punchScheduling.getTimeRuleId();
		}
	}

    private java.sql.Date checkHoliday(PunchRule pr, java.sql.Date punchDate) {

        if(null != pr.getChinaHolidayFlag() && pr.getChinaHolidayFlag().equals(NormalFlag.YES.getCode())){
            PunchHoliday holiday = punchProvider.findHolidayByDate(punchDate);
            if(null != holiday){
                if(holiday.getStatus().equals(NormalFlag.YES.getCode())){
                    return null;
                }else {
                    punchDate = holiday.getExchangeFromDate();
                }
            }

        }
        return punchDate;
    }

    private Integer getWeekDayInt(java.sql.Date punchDate) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(punchDate);
		int CalendarWeekDay = calendar.get(Calendar.DAY_OF_WEEK);
		switch(CalendarWeekDay){
		case Calendar.SUNDAY:
			return PunchConstants.SUNDAY_INT;
		case Calendar.MONDAY:
			return PunchConstants.MONDAY_INT;
		case Calendar.TUESDAY:
			return PunchConstants.TUESDAY_INT;
		case Calendar.WEDNESDAY:
			return PunchConstants.WEDNESDAY_INT;
		case Calendar.THURSDAY:
			return PunchConstants.THURSDAY_INT;
		case Calendar.FRIDAY:
			return PunchConstants.FRIDAY_INT;
		case Calendar.SATURDAY:
			return PunchConstants.SATURDAY_INT;
		}
		return 0;
	}
	private void addPunchStatistics(OrganizationMemberDTO member ,Long orgId,Calendar startCalendar,Calendar endCalendar){
		
		PunchRule pr = this.getPunchRule(PunchOwnerType.ORGANIZATION.getCode(),orgId, member.getTargetId());
		if(null == pr)
			throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
					PunchServiceErrorCode.ERROR_ENTERPRISE_DIDNOT_SETTING,
					"have no punch rule");
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
            statistic.setDetailId(member.getDetailId());
            //应上班天数
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
			processPunchListCount(dayLogList, statistic);

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
	private void processPunchListCount(List<PunchDayLog> list,
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
		for (PunchDayLog pdl : list) {
            if(pdl.getStatusList()!=null){
                Byte isNormal = NormalFlag.YES.getCode();
                if (pdl.getStatusList().contains(PunchConstants.STATUS_SEPARATOR)) {
                    String[] status = pdl.getStatusList().split(PunchConstants.STATUS_SEPARATOR);
                    for (int i = 1; i <= status.length; i++) {

                        isNormal = countOneDayStatistic(status[i-1], statistic, isNormal,i,pdl.getTimeRuleId());
                    }
                }else{
                    if (pdl.getStatusList().equals(String.valueOf(PunchStatus.NOTWORKDAY.getCode()))) {
                        //不上班跳过
                        continue;
                    }else{
                        isNormal = countOneDayStatistic(pdl.getStatusList(),statistic,isNormal,1,pdl.getTimeRuleId());
                    }
                }
                if (NormalFlag.fromCode(isNormal).equals(NormalFlag.YES)) {
                    statistic.setWorkCount(statistic.getWorkCount() + 1);
                }
            }
		}

    }

    private Byte countOneDayStatistic(String status, PunchStatistic statistic, Byte isNormal,
                                      int punchTimeNo, Long timeRuleId) {
        if (status.equals(PunchStatus.UNPUNCH.getCode())) {
            statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
            isNormal = NormalFlag.NO.getCode();
            //缺勤计算小时
            PunchTimeRule r1 = punchProvider.findPunchTimeRuleById(timeRuleId);
            if(null != r1) {
                PunchTimeRuleDTO dto1 = convertPunchTimeRule2DTO(r1);
                PunchTimeIntervalDTO interval = dto1.getPunchTimeIntervals().get(punchTimeNo - 1);
                statistic.setUnpunchCount(statistic.getUnpunchCount()
                        +(interval.getLeaveTime()-interval.getArriveTime())/3600000.0);
            }
        }  else if (status.equals(PunchStatus.LEAVEEARLY.getCode())) {
            statistic.setLeaveEarlyCount(statistic.getLeaveEarlyCount() + 1);
            statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
            isNormal = NormalFlag.NO.getCode();
        }  else  if (status.equals(PunchStatus.BLANDLE.getCode())) {
            statistic.setBlandleCount(statistic.getBlandleCount() + 1);
            statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
            isNormal = NormalFlag.NO.getCode();
        } else  if (status.equals(PunchStatus.BELATE.getCode())) {
            statistic.setBelateCount(statistic.getBelateCount() + 1);
            statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
            isNormal = NormalFlag.NO.getCode();
        }
        return  isNormal;
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

		row.createCell(++i).setCellValue("时间");
		row.createCell(++i).setCellValue("姓名");
		row.createCell(++i).setCellValue("部门");
		row.createCell(++i).setCellValue("所属规则");
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
	
	public void setNewPunchStatisticsBookRow(Sheet sheet , PunchCountDTO statistic){
		Row row = sheet.createRow(sheet.getLastRowNum()+1);
		int i = -1; 
		row.createCell(++i).setCellValue(statistic.getPunchMonth());
		row.createCell(++i).setCellValue(statistic.getUserName());
		row.createCell(++i).setCellValue(statistic.getDeptName());
		row.createCell(++i).setCellValue(statistic.getPunchOrgName());
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
		if(statistic.getOverTimeSum()==null || statistic.getOverTimeSum().equals(0L)){
			row.createCell(++i).setCellValue(0);
		}
		else{
			BigDecimal b = new BigDecimal(statistic.getOverTimeSum()/3600000.0); 
			row.createCell(++i).setCellValue(b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());  
		}
			 
	}
	
	public Workbook createPunchStatisticsBook(List<PunchCountDTO> results) {
		if (null == results || results.size() == 0)
			return null;
		Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet("punchStatistics");
		
		this.createPunchStatisticsBookSheetHead(sheet );
		for (PunchCountDTO statistic : results )
			this.setNewPunchStatisticsBookRow(sheet, statistic);
		return wb;
//		try {
//
//			FileOutputStream out = new FileOutputStream(path);
//			wb.write(out);
//			wb.close();
//			out.close();
//		} catch (Exception e) {
//			LOGGER.error(e.getMessage());
//			throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
//					PunchServiceErrorCode.ERROR_PUNCH_ADD_DAYLOG,
//					e.getLocalizedMessage());
//		}
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
	public HttpServletResponse download(Workbook workbook, String fileName, HttpServletResponse response) {
        try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			workbook.write(out);
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes()));
			OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/msexcel");
			toClient.write(out.toByteArray());
			toClient.flush();
			toClient.close();

//            // 读取完成删除文件
//            if (file.isFile() && file.exists()) {
//                file.delete();
//            }
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
//		punchTimeRule.setPunchTimesPerDay(dto.getPunchTimesPerDay());
		punchTimeRule.setNoonLeaveTime(convertTime(dto.getNoonLeaveTime())); 
		punchTimeRule.setDaySplitTime(convertTime(dto.getDaySplitTime()));
//		convertTime(punchTimeRule, dto.getStartEarlyTime(), dto.getStartLateTime(), dto.getEndEarlyTime());
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
//		dto.setStartEarlyTime(null!=other.getStartEarlyTimeLong()?other.getStartEarlyTimeLong():convertTimeToGMTMillisecond(other.getStartEarlyTime()));
//		dto.setStartLateTime(null!=other.getStartLateTimeLong()?other.getStartLateTimeLong():convertTimeToGMTMillisecond(other.getStartLateTime()));
//		
//		dto.setEndEarlyTime(dto.getStartEarlyTime() + (null!=other.getWorkTimeLong()?other.getWorkTimeLong():convertTimeToGMTMillisecond(other.getWorkTime())));
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
		 
		Organization organization = checkOrganization(cmd.getOwnerId());
		for(PunchRuleOwnerMap other:results)  {
			PunchRuleMapDTO dto = ConvertHelper.convert(other, PunchRuleMapDTO.class);
//			PunchRule pr = this.punchProvider.getPunchRuleById(other.getPunchRuleId());
//			if(pr == null)
//				throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
//						PunchServiceErrorCode.ERROR_ENTERPRISE_DIDNOT_SETTING,
//						"have no punch rule");
//			dto.setPunchRuleName(pr.getName());
			if(PunchOwnerType.User.getCode().equals(other.getTargetType())){
				OrganizationMember member = findOrganizationMemberByOrgIdAndUId(other.getTargetId(), organization.getPath()); 
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
	private OrganizationMember findOrganizationMemberByOrgIdAndUId(Long targetId, String path) {
		List<OrganizationMember> members = this.organizationProvider.listOrganizationMemberByPath(null, path, null, null, new CrossShardListingLocator(), 1000000);
		if(null== members || members.size() == 0)
			return null;
		for(OrganizationMember member : members){
			if(member.getTargetId().equals(targetId))
				return member;
		}
		return null;
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
	/**找到用户的打卡规则*/
	@Override
	public PunchRule getPunchRule(String ownerType, Long ownerId,Long userId){
		Organization punchOrg = findPunchGroupByUser(userId, ownerId);
		if (null == punchOrg)
			return null;
		PunchRule pr = punchProvider.getPunchruleByPunchOrgId(punchOrg.getId());
		return pr;
		
	}

	/**
	 * 找到user 的考勤组
	 * */
	public Organization findPunchGroupByUser(Long userId,Long organizationId){
		UniongroupMemberDetail detail = findUserMemberDetail(userId, organizationId);
		if(null == detail)
			return null;
		Organization organization = organizationProvider.findOrganizationById(detail.getGroupId());
		return organization; 
	}
	
	public UniongroupMemberDetail findUserMemberDetail(Long userId,Long organizationId){
		List<OrganizationMember> orgMembers = organizationProvider.findOrganizationMembersByOrgIdAndUId(userId, organizationId);
		if(null == orgMembers || orgMembers.size() == 0 )
			return null; 
		UniongroupMemberDetail detail = this.uniongroupConfigureProvider.findUniongroupMemberDetailByDetailId(orgMembers.get(0).getNamespaceId(),
				orgMembers.get(0).getDetailId(), UniongroupType.PUNCHGROUP.getCode());
		return detail;
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
		int pageSize =  getPageSize(configurationProvider, cmd.getPageSize());
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());
//		Long organizationId = org.getDirectlyEnterpriseId();
//		if(organizationId.equals(0L))
//			organizationId = org.getId();
        Long organizationId = getTopEnterpriseId(cmd.getOwnerId());
        
        //2017-8-2 加入了时间区间
        List<String> months = new ArrayList<>();
        if(null != cmd.getStartDay()){
    		if (cmd.getStartDay() > cmd.getEndDay()) {
    			LOGGER.error("Invalid  parameter in the command : startDay > end day");
    			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,
    					"Invalid  parameter in the command : startDay > end day");
    		}
    		Calendar start = Calendar.getInstance();
    		Calendar end = Calendar.getInstance();
    		start.setTime(new Date(cmd.getStartDay()));
    		end.setTime(new Date(cmd.getEndDay()));
    		while(start.before(end)){
    			months.add(monthSF.get().format(start.getTime()));
    			start.add(Calendar.MONTH, 1);
    		}
        }else{
        	months.add(cmd.getMonth());
        }
		List<PunchStatistic> results = this.punchProvider.queryPunchStatistics(cmd.getOwnerType(),
				organizationId,months,
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
	@Override
	public List<Long> listDptUserIds(Organization org , Long ownerId,String userName, Byte includeSubDpt) {
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
//		List<PunchDayDetailDTO> dtos = new ArrayList<PunchDayDetailDTO>();
//		PunchOwnerType ownerType = PunchOwnerType.fromCode(cmd.getOwnerType());
//		if(PunchOwnerType.ORGANIZATION.equals(ownerType)){
//			//找到所有子部门 下面的用户
//			Organization org = this.checkOrganization(cmd.getOwnerId());
//
//			List<Long> userIds = listDptUserIds(org,cmd.getOwnerId(), cmd.getUserName(),cmd.getIncludeSubDpt());
//			if (null == userIds)
//				return response;
//			//分页查询 由于用到多条件排序,所以使用pageOffset方式分页
//			 
//			String startDay=null;
//			if(null!=cmd.getStartDay())
//				startDay =  dateSF.get().format(new Date(cmd.getStartDay()));
//			String endDay=null;
//			if(null!=cmd.getEndDay())
//				endDay =  dateSF.get().format(new Date(cmd.getEndDay()));
//			Long organizationId = org.getDirectlyEnterpriseId();
//			if(organizationId.equals(0L))
//				organizationId = org.getId();
//			List<PunchDayLog> results = punchProvider.listPunchDayLogs(userIds,
//					organizationId,startDay,endDay , 
//					cmd.getArriveTimeCompareFlag(),convertTime(cmd.getArriveTime()), cmd.getLeaveTimeCompareFlag(),
//					convertTime(cmd.getLeaveTime()), cmd.getWorkTimeCompareFlag(),
//					convertTime(cmd.getWorkTime()),cmd.getExceptionStatus(), null, null);
//			
//			if (null == results)
//				return null;
//			 
//			for(PunchDayLog r : results){
//				PunchDayDetailDTO dto =convertToPunchDayDetailDTO(r);
//				dtos.add(dto);
//			}
//			
//		}
		cmd.setPageSize(Integer.MAX_VALUE-1);
		ListPunchDetailsResponse resp = listPunchDetails(cmd);

		String filePath = "PunchDetails" + System.currentTimeMillis() + ".xlsx";
		//新建了一个文件

		Workbook wb = createPunchDetailsBook(resp.getPunchDayDetails());
		
		return download(wb,filePath,response);
	}

    private Workbook createPunchDetailsBook(List<PunchDayDetailDTO> dtos) {
    	if (null == dtos || dtos.size() == 0)
			return new XSSFWorkbook();
		Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet("punchDetails");
		
		this.createPunchDetailsBookSheetHead(sheet );
		for (PunchDayDetailDTO dto : dtos )
			this.setNewPunchDetailsBookRow(sheet, dto);
		return wb;
	}
    private String convertTimeLongToString(Long timeLong){
    	if(null == timeLong)
    		return "";
    	else{
//            if(timeLong < 48*3600*1000L){
//                Long time = dateSF.get().parse("2010-1-1").getTime();
//                return timeSF.get().format(time+timeLong);
//            }
//
    		return timeSF.get().format(convertTime(timeLong));
        }
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
						pdl.setStatuString( processStatus(dayLog.getStatusList()));
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
				
				coordinationProvider.getNamedLock(CoordinationLocks.PUNCH_DAY_SCHEDULE.getCode()+start.getTimeInMillis()).tryEnter(() -> {
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
//								start.add(Calendar.DAY_OF_MONTH, 1);
//								continue  ;
								return;
							}
                            newPunchDayLog.setStatusList(pdl.getStatusList());
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
				});				
	
				start.add(Calendar.DAY_OF_MONTH, 1);
			}
		}
	}
	public static int getPageSize(ConfigurationProvider configProvider, Integer requestedPageSize) {
		if(requestedPageSize == null) {
			return configProvider.getIntValue("pagination.default.size", AppConstants.PAGINATION_DEFAULT_SIZE);
		}
//
//		int maxSize = configProvider.getIntValue("pagination.max.size", AppConstants.PAGINATION_MAX_SIZE);
//		if(requestedPageSize.intValue() > maxSize)
//			return maxSize;

		return requestedPageSize.intValue();
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
		//分页查询 由于用到多条件排序,所以使用pageOffset方式分页 
		Integer pageOffset = 0; 
		if (cmd.getPageAnchor() != null)
			pageOffset = cmd.getPageAnchor().intValue();
		int pageSize = getPageSize(configurationProvider, cmd.getPageSize());
		List<Long> userIds = new ArrayList<>();
//		Long organizationId = null;
		if(cmd.getUserId() != null){
			userIds.add(cmd.getUserId());
		}
		else if(PunchOwnerType.ORGANIZATION.equals(ownerType)){
			//找到所有子部门 下面的用户
			Organization org = this.checkOrganization(cmd.getOwnerId()); 
			userIds = listDptUserIds(org,cmd.getOwnerId(), cmd.getUserName(),cmd.getIncludeSubDpt());
			if (null == userIds)
				return response; 
//			organizationId = org.getDirectlyEnterpriseId();
//			if(organizationId.equals(0L))
//				organizationId = org.getId();
		}
		String startDay=null;
		if(null!=cmd.getStartDay())
			startDay =  dateSF.get().format(new Date(cmd.getStartDay()));
		String endDay=null;
		if(null!=cmd.getEndDay())
			endDay =  dateSF.get().format(new Date(cmd.getEndDay()));
		
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
		return response;
	}
	String processStatus(String statuList ){
		String result = "";
		if(statuList.contains(PunchConstants.STATUS_SEPARATOR)){
			String[] statulist = StringUtils.split(statuList, PunchConstants.STATUS_SEPARATOR);
			for(int i = 0 ;i<statulist.length;i++ ){
				if(i ==0){
					result = statusToString( Byte.valueOf(statulist[i]));
				}else{
					result = result + PunchConstants.STATUS_SEPARATOR + statusToString( Byte.valueOf(statulist[i]));
				}
			}
		}
		else{
			result = statusToString(Byte.valueOf(statuList));
		}
        return result;
    }
	public PunchDayDetailDTO convertToPunchDayDetailDTO(PunchDayLog r ){
		PunchDayDetailDTO dto =  ConvertHelper.convert(r,PunchDayDetailDTO.class);
		dto.setStatuString(processStatus(r.getStatusList()));
		Organization punchGroup = organizationProvider.findOrganizationById(r.getPunchOrganizationId());
		dto.setPunchOrgName(punchGroup.getName());
		
		
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
	 * 打卡2.0 的考勤统计-按月统计
	 * */
	@Override
	public HttpServletResponse exportPunchStatistics(ListPunchCountCommand cmd, HttpServletResponse response) {

		//找到所有子部门 下面的用户
		Organization org = this.checkOrganization(cmd.getOwnerId());
		cmd.setPageSize(Integer.MAX_VALUE-1);
		ListPunchCountCommandResponse resp = listPunchCount(cmd);
//		List<Long> userIds = listDptUserIds(org,cmd.getOwnerId(), cmd.getUserName(),cmd.getIncludeSubDpt());
//		if (null == userIds)
//			return response;
//
//		Long organizationId = org.getDirectlyEnterpriseId();
//		if(organizationId.equals(0L))
//			organizationId = org.getId();
//		List<PunchStatistic> results = this.punchProvider.queryPunchStatistics(cmd.getOwnerType(),organizationId,cmd.getMonth(),cmd.getExceptionStatus()
//				,userIds, null, Integer.MAX_VALUE);
//		if(null == results || results.isEmpty())
//			return response;
//		URL rootPath = PunchServiceImpl.class.getResource("/");
//		String filePath =rootPath.getPath() + this.downloadDir ;
//		File file = new File(filePath);
//		if(!file.exists())
//			file.mkdirs();
		String fileName =  "PunchStatistics"+System.currentTimeMillis()+".xlsx";
		//新建了一个文件

		Workbook wb = createPunchStatisticsBook(resp.getPunchCountList());
		
		return download(wb,fileName,response);
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
    private static int refreshGap = 15;
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
        Long timeLong = getTimeLong(punCalendar);
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DAY_OF_MONTH, -1);
        //找今天刷新的(当前时间点前15分钟 到当前时间点之间split的
        List<PunchTimeRule> timeRules = punchProvider.listPunchTimeRulesBySplitTime(timeLong - refreshGap * 60 * 1000,
                timeLong);
        if (null == timeRules) {
            timeRules = new ArrayList<>();
        }
        List<PunchTimeRule> timeRules2 = punchProvider.listPunchTimeRulesBySplitTime(timeLong - refreshGap * 60 * 1000
                + ONE_DAY_MS, timeLong + ONE_DAY_MS);
        if (null != timeRules2) {
            timeRules.addAll(timeRules2);
        }
        Set<PunchRule> punchRules = new HashSet<>();
        for (PunchTimeRule ptr : timeRules) {
            if(ptr.getPunchRuleId() ==null)
                continue;
            PunchRule pr = punchProvider.getPunchRuleById(ptr.getPunchRuleId());
            if (null == pr) {
                continue;
            }

            if (PunchRuleType.GUDING.equals(PunchRuleType.fromCode(pr.getRuleType()))) {
                //看昨天是否为特殊日期
                PunchSpecialDay specialDay = punchProvider.findSpecialDayByDateAndOrgId(pr.getPunchOrganizationId(),yesterday.getTime());
                if(null != specialDay){
                    if(specialDay.getStatus().equals(NormalFlag.YES.getCode())){
                        continue;
                    }else {
                        //特殊上班工作日,要处理它
                        punchRules.add(pr);
                    }
                }
                //如果为节假日则返回null  如果是节假调休日,用调休日期代替
                java.sql.Date punchDate = new java.sql.Date(yesterday.getTime().getTime());
                punchDate = checkHoliday(pr,punchDate);
                //看是循环timerule找当天的timeRule
                Integer openWeek = Integer.parseInt(ptr.getOpenWeekday(), 2);
                Integer weekDayInt = getWeekDayInt(punchDate);
                if(weekDayInt.equals(openWeek&weekDayInt)){
                    //当天上班
                    punchRules.add(pr);
                }
            }else{
                Calendar schedulingCalendar = Calendar.getInstance();
                schedulingCalendar = yesterday;
                if (ptr.getDaySplitTimeLong() < ONE_DAY_MS) {
                    schedulingCalendar = punCalendar;
                }
                final java.sql.Date queryDate = new java.sql.Date(schedulingCalendar.getTimeInMillis());
                List<PunchScheduling> punchSchedulings = punchSchedulingProvider.queryPunchSchedulings(null, Integer.MAX_VALUE,new ListingQueryBuilderCallback()  {
                    @Override
                    public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                                                                        SelectQuery<? extends Record> query) {
                        query.addConditions(Tables.EH_PUNCH_SCHEDULINGS.RULE_DATE.eq(queryDate));
                        query.addConditions(Tables.EH_PUNCH_SCHEDULINGS.TIME_RULE_ID.eq(ptr.getId()));
                        query.addOrderBy(Tables.EH_PUNCH_SCHEDULINGS.RULE_DATE.asc());
                        return null;
                    }
                });
                if (null != punchSchedulings) {
                    for(PunchScheduling punchScheduling : punchSchedulings){
                        OrganizationMemberDetails memberDetail = organizationProvider.findOrganizationMemberDetailsByDetailId(punchScheduling.getTargetId());
                        refreshDayLogAndMonthStat(memberDetail.getTargetId(), pr.getOwnerId(), schedulingCalendar);
                    }
                }
            }
        }
        for (PunchRule pr : punchRules) {
            refreshGroupDayLogAndMonthStat(pr, yesterday);
        }
    }
    /**刷固定排班*/
    private void refreshGroupDayLogAndMonthStat(PunchRule pr, Calendar yesterday) {
        List<UniongroupMemberDetail> members = uniongroupConfigureProvider.listUniongroupMemberDetail(pr.getPunchOrganizationId());
        for(UniongroupMemberDetail member : members) {
            if(member.getTargetType().equals("USER")){
                refreshDayLogAndMonthStat(member.getTargetId(),pr.getOwnerId(),yesterday);
            }
        }
    }

    private void refreshDayLogAndMonthStat(Long userId,Long ownerId ,Calendar punCalendar){
        if(userId == null || userId.equals(0L))
            return;
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(punCalendar.getTime());
        startCalendar.set(Calendar.HOUR_OF_DAY, 0);
        startCalendar.set(Calendar.DAY_OF_MONTH, 1);

        OrganizationDTO dept = findUserDepartment(userId, ownerId);
        OrganizationMember member = organizationProvider.findOrganizationMemberByOrgIdAndUId(userId, dept.getId());
        refreshDayLogAndMonthStat(ConvertHelper.convert(member, OrganizationMemberDTO.class), dept.getId(), punCalendar, startCalendar);

    }
    /**刷新punCalendar 当天日数据 和 start到pun 之间的月数据*/
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

			LOGGER.error("Invalid owner type or  Id parameter in the command");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid owner type or  Id parameter in the command");
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
//		PunchRuleOwnerMap map = getUsefulRuleMap(cmd.getOwnerType(),cmd.getOwnerId(),cmd.getTargetType(),cmd.getTargetId());
		Calendar startCalendar = Calendar.getInstance();
		Calendar endCalendar = Calendar.getInstance();
		startCalendar.setTime(new Date(cmd.getQueryTime())); 
		startCalendar.set(Calendar.DAY_OF_MONTH, 1);
		startCalendar.set(Calendar.MINUTE, 0);
		startCalendar.set(Calendar.SECOND, 0);
		startCalendar.set(Calendar.MILLISECOND, 0);
		endCalendar.setTime(startCalendar.getTime());
		endCalendar.add(Calendar.MONTH, 1);
//		List<PunchScheduling> punchSchedulings = punchSchedulingProvider.queryPunchSchedulings(null, Integer.MAX_VALUE,
//                new ListingQueryBuilderCallback()  {
//			//TODO 联表查询
//			@Override
//			public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
//					SelectQuery<? extends Record> query) {
//				query.addConditions(Tables.EH_PUNCH_SCHEDULINGS.OWNER_TYPE.eq(map.getOwnerType()));
//				query.addConditions(Tables.EH_PUNCH_SCHEDULINGS.OWNER_ID.eq(map.getOwnerId()));
//				query.addConditions(Tables.EH_PUNCH_SCHEDULINGS.TARGET_ID.eq(map.getTargetId()));
//				query.addConditions(Tables.EH_PUNCH_SCHEDULINGS.TARGET_TYPE.eq(map.getTargetType()));
//				
//				query.addConditions(Tables.EH_PUNCH_SCHEDULINGS.RULE_DATE.greaterOrEqual(new java.sql.Date( startCalendar.getTime().getTime())));
//				query.addConditions(Tables.EH_PUNCH_SCHEDULINGS.RULE_DATE.lt(new java.sql.Date( endCalendar.getTime().getTime())));
//				query.addOrderBy(Tables.EH_PUNCH_SCHEDULINGS.RULE_DATE.asc());
//				return null;
//			}
//		});
		ListPunchSchedulingMonthResponse  response = new ListPunchSchedulingMonthResponse(); 
//		response.setSchedulings(new ArrayList<PunchSchedulingDTO>());
//		for(;startCalendar.before(endCalendar);startCalendar.add(Calendar.DAY_OF_MONTH,1)){
//			PunchSchedulingDTO dto = ConvertHelper.convert(cmd, PunchSchedulingDTO.class);
////			dto.setRuleDate(startCalendar.getTimeInMillis());
//			String dateString = dateSF.get().format(startCalendar.getTime());
//			for(PunchScheduling punchScheduling : punchSchedulings){
//				if(dateSF.get().format(punchScheduling.getRuleDate()).equals(dateString)){
//					PunchTimeRule timeRule = punchProvider.findPunchTimeRuleById(punchScheduling.getTimeRuleId());
//					if(null != timeRule){
////						dto.setTimeRuleId(timeRule.getId());
////						dto.setTimeRuleName(timeRule.getName());
////						dto.setTimeRuleDescription(timeRule.getDescription());
//					}else{ 
//						//TODO : 减少IO 做成MAP
//						LocaleString scheduleLocaleString = localeStringProvider.find( PunchConstants.PUNCH_DEFAULT_SCOPE, PunchConstants.PUNCH_TIME_RULE_NAME,"zh_CN");   
////						dto.setTimeRuleName( scheduleLocaleString==null?"":scheduleLocaleString.getText());
//					}
//				}
//			}
//			response.getSchedulings().add(dto);
//		}
//		ListPunchRulesCommonCommand cmd2 = ConvertHelper.convert(cmd, ListPunchRulesCommonCommand.class);
//		cmd2.setPageSize(Integer.MAX_VALUE);
		 
		PunchRule pr = punchProvider.getPunchruleByPunchOrgId(cmd.getPunchOriganizationId());
		
		java.sql.Date startDate = new java.sql.Date( startCalendar.getTime().getTime());
		java.sql.Date endDate =new java.sql.Date( endCalendar.getTime().getTime());
		//		response.setTimeRules(listPunchTimeRuleList(cmd2).getTimeRules());
		List<PunchSchedulingDTO> schedulingDTOs =  processschedulings(pr, startDate, endDate);
		response.setSchedulings(schedulingDTOs);
		return response;
	}
//	private PunchRuleOwnerMap getUsefulRuleMap(String ownerType, Long ownerId, String targetType,
//			Long targetId) {
//		// TODO Auto-generated method stub
//		PunchRuleOwnerMap map = null;
//		if(targetType.equals(PunchOwnerType.User.getCode())){
//			//如果有个人规则就返回个人规则
//			map = this.punchProvider.getPunchRuleOwnerMapByOwnerAndTarget(ownerType, ownerId, targetType, targetId);
//			if (null != map ) 
//				return map;
//			//如果没有就按照部门来找规则
//			OrganizationDTO deptDTO = findUserDepartment(targetId, ownerId);
//			targetId=deptDTO.getId();
//		}
//		
//		int loopMax = 10;
//		Organization dept =  organizationProvider.findOrganizationById(targetId);
//		map = getPunchRule(null ,dept,loopMax);
//		return map;
//	}

    @Override
    public HttpServletResponse exportPunchScheduling(ListPunchSchedulingMonthCommand cmd,
                                                     HttpServletResponse response) {
//		cmd.setOwnerId(getTopEnterpriseId(cmd.getOwnerId()));
//		targetTimeRules.set(punchProvider.queryPunchTimeRules(cmd.getOwnerType(), cmd.getOwnerId(),cmd.getTargetType(),cmd.getTargetId(),  null));
//		if(null == targetTimeRules.get())
//			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,
//					"have no time rule");
//		if (null == cmd.getOwnerId() ||null == cmd.getOwnerType()) {
//			LOGGER.error("Invalid owner type or  Id parameter in the command");
//			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,
//					"Invalid owner type or  Id parameter in the command");
//		}
        LocaleString scheduleLocaleString = localeStringProvider.find( PunchConstants.PUNCH_EXCEL_SCOPE, PunchConstants.EXCEL_SCHEDULE,
                UserContext.current().getUser().getLocale());
        String filePath = monthSF.get().format(new Date(cmd.getQueryTime()))+ (scheduleLocaleString==null?"scheduling":scheduleLocaleString.getText())+".xlsx";
        //新建了一个文件

        Workbook wb = createPunchSchedulingsBook(cmd.getQueryTime(),cmd.getEmployees(),cmd.getTimeRules());

        return download(wb, filePath, response);

    }


    @Override
    public HttpServletResponse exportPunchSchedulingTemplate(ListPunchSchedulingMonthCommand cmd,
                                                     HttpServletResponse response) {
        LocaleString scheduleLocaleString = localeStringProvider.find( PunchConstants.PUNCH_EXCEL_SCOPE, PunchConstants.EXCEL_SCHEDULE,
                UserContext.current().getUser().getLocale());
        String filePath = monthSF.get().format(new Date(cmd.getQueryTime()))+ (scheduleLocaleString==null?"scheduling":scheduleLocaleString.getText())+".xlsx";
        //新建了一个文件

        Workbook wb = createPunchSchedulingTemplateBook(cmd.getQueryTime(),cmd.getTimeRules());

        return download(wb, filePath, response);

    }

    private XSSFWorkbook createPunchSchedulingTemplateBook(Long queryTime, List<PunchTimeRuleDTO> timeRules) {

        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("sheet1");
        String sheetName ="sheet1";
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 31));
        XSSFCellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontHeightInPoints((short) 20);
        font.setFontName("Courier New");

        style.setFont(font);

        XSSFCellStyle titleStyle = wb.createCellStyle();
        titleStyle.setFont(font);
        titleStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);

        int rowNum = 0;

        //  创建标题
        XSSFRow rowTitle = sheet.createRow(rowNum++);
        rowTitle.createCell(0).setCellValue(monthSF.get().format(new Date(queryTime)));
        rowTitle.setRowStyle(titleStyle);

        XSSFRow rowReminder = sheet.createRow(rowNum++);

        Map<String, String> map = new HashMap<String, String>();
        StringBuilder ruleSB = new StringBuilder();
        if(null!=timeRules){
            for(PunchTimeRuleDTO rule : timeRules){
                ruleSB.append(rule.getName());
                ruleSB.append("(");
                for (int i = 0; i < rule.getPunchTimeIntervals().size(); i++) {
                    if(i>0)
                        ruleSB.append(" ");
                    ruleSB.append(convertTimeLongToString(rule.getPunchTimeIntervals().get(i).getArriveTime()));
                    ruleSB.append("-");
                    ruleSB.append(convertTimeLongToString(rule.getPunchTimeIntervals().get(i).getLeaveTime()));
                }
                ruleSB.append(");");
            }
        }
        map.put("timeRules", ruleSB.toString());
        String result = localeTemplateService.getLocaleTemplateString(PunchConstants.PUNCH_EXCEL_SCOPE,
                PunchConstants.PUNCH_EXCEL_SCHEDULING_REMINDER, RentalNotificationTemplateCode.locale, map, "");

        rowReminder.createCell(0).setCellValue(result);
        rowReminder.setRowStyle(titleStyle);

        this.createPunchSchedulingsBookSheetHead(sheet,queryTime);
        return wb;
    }

    private Workbook createPunchSchedulingsBook(Long queryTime,
			List<PunchSchedulingEmployeeDTO> employees,List<PunchTimeRuleDTO> timeRules) {

        if (null == employees ||  employees.size() == 0)
            return null;
        XSSFWorkbook wb = createPunchSchedulingTemplateBook(queryTime,timeRules);
        XSSFSheet sheet = wb.getSheetAt(0);
        for (PunchSchedulingEmployeeDTO employee : employees )
            setNewPunchSchedulingsBookRow(sheet, employee );
        return wb;
	}
	private void createPunchSchedulingsBookSheetHead(Sheet sheet, Long queryTime) { 
		Row row = sheet.createRow(sheet.getLastRowNum()+1);
		int cellNum =0 ;
		SimpleDateFormat sf= new SimpleDateFormat("dd日 EEE");
		Calendar startCalendar = Calendar.getInstance();
		Calendar endCalendar = Calendar.getInstance();
		startCalendar.setTime(new Date(queryTime)); 
		startCalendar.set(Calendar.DAY_OF_MONTH, 1);
		startCalendar.set(Calendar.MINUTE, 0);
		startCalendar.set(Calendar.SECOND, 0);
		startCalendar.set(Calendar.MILLISECOND, 0);
		endCalendar.setTime(startCalendar.getTime());
		endCalendar.add(Calendar.MONTH, 1);
		for(;startCalendar.before(endCalendar);startCalendar.add(Calendar.DAY_OF_MONTH, 1)){
			row.createCell(++cellNum).setCellValue(sf.format(startCalendar.getTime()));
		}
	}
	private void setNewPunchSchedulingsBookRow(Sheet sheet, PunchSchedulingEmployeeDTO employee ) { 
		Row row = sheet.createRow(sheet.getLastRowNum()+1);
		int i = -1;  
		row.createCell(++i).setCellValue(employee.getContactName());
		if(null != employee.getDaySchedulings())
			for( String paiban : employee.getDaySchedulings()){
				row.createCell(++i).setCellValue(paiban);
			} 
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
	public PunchSchedulingDTO  importPunchScheduling(  MultipartFile[] files) {
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
		PunchSchedulingDTO result = convertToPunchSchedulings(resultList);
		return result;
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
	private PunchSchedulingDTO convertToPunchSchedulings(ArrayList list ) {
		PunchSchedulingDTO result = new PunchSchedulingDTO();
		result.setEmployees(new  ArrayList<PunchSchedulingEmployeeDTO>());
		RowResult title = (RowResult) list.get(0);
		String monthString = title.getCells().get("A");
		Calendar calendar = Calendar.getInstance();
		try {

			Date month = monthSF.get().parse(monthString);
			result.setMonth(month.getTime());

			calendar.setTime(month);
			calendar.set(Calendar.DAY_OF_MONTH,1);
			calendar.add(Calendar.MONTH,1);
			calendar.add(Calendar.DAY_OF_MONTH,-1);
			int days = calendar.get(Calendar.DAY_OF_MONTH);

			for(int rowIndex=3;rowIndex<list.size();rowIndex++){
				RowResult r = (RowResult)list.get(rowIndex);
				PunchSchedulingEmployeeDTO dto = new PunchSchedulingEmployeeDTO();
				dto.setContactName(r.getCells().get("A"));
				dto.setDaySchedulings(new ArrayList<>());
				for(int i = 1 ; i<=days;i++){
					String val = r.getCells().get(GetExcelLetter(i + 1));
					dto.getDaySchedulings().add(val);
				}
				result.getEmployees().add(dto);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}


    /******* 导入部分 *******/
    //  数字转换(1-A,2-B...)
    //  接下来会是很长的导入导出代码
    private static String GetExcelLetter(int n) {
        String s = "";
        while (n > 0) {
            int m = n % 26;
            if (m == 0)
                m = 26;
            s = (char) (m + 64) + s;
            n = (n - m) / 26;
        }
        return s;
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
//		if(null == cmd.getSchedulings() ||cmd.getSchedulings().size() == 0 )
//			return ;
//		PunchRuleOwnerMap map = getOrCreateTargetRuleMap(cmd.getSchedulings().get(0).getOwnerType(), cmd.getSchedulings().get(0).getOwnerId(), 
//				cmd.getSchedulings().get(0).getTargetType(), cmd.getSchedulings().get(0).getTargetId());
//		PunchRule pr = punchProvider.getPunchRuleById(map.getPunchRuleId());
//		if(null == pr)
//			return  ;
//		for(PunchSchedulingDTO dto : cmd.getSchedulings()){
//			
//			PunchScheduling punchScheduling = ConvertHelper.convert(dto, PunchScheduling.class);
//			punchScheduling.setPunchRuleId(pr.getId());
////			punchScheduling.setRuleDate(new java.sql.Date(dto.getRuleDate()));
//			punchScheduling.setCreatorUid(UserContext.current().getUser().getId());
//			punchScheduling.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
//					.getTime()));
//			punchSchedulingProvider.deletePunchScheduling(punchScheduling);
//			punchSchedulingProvider.createPunchScheduling(punchScheduling);
//			
//		}
		
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

	//  punch 2.8 added by R 20170725
	@Override
	public ListPunchSupportiveAddressCommandResponse listPunchSupportiveAddress(ListPunchSupportiveAddressCommand cmd){

        ListPunchSupportiveAddressCommandResponse response = new ListPunchSupportiveAddressCommandResponse();

	    Long userId = UserContext.current().getUser().getId();
        PunchRule pr = getPunchRule(PunchOwnerType.ORGANIZATION.getCode(), cmd.getEnterpriseId(), userId);
        if (null == pr  )
            throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
                    PunchServiceErrorCode.ERROR_ENTERPRISE_DIDNOT_SETTING,
                    "公司没有设置打卡规则");
        //  查询用户对应的wifi mac地址
        List<PunchWifi> wifis = this.punchProvider.listPunchWifisByRuleId(PunchOwnerType.ORGANIZATION.getCode(), cmd.getEnterpriseId(), pr.getWifiRuleId()) ;
        //  查询用户对应的经纬度信息
        List<PunchGeopoint> punchGeopoints = punchProvider
                .listPunchGeopointsByRuleId(PunchOwnerType.ORGANIZATION.getCode(), cmd.getEnterpriseId(),pr.getLocationRuleId());

        if(wifis != null){
            response.setWifis(wifis.stream().map(r ->{
                PunchWiFiDTO dto = ConvertHelper.convert(r,PunchWiFiDTO.class);
                return dto;
            }).collect(Collectors.toList()));
        }

        if(punchGeopoints != null){
            response.setGeoPoints(punchGeopoints.stream().map(r ->{
                PunchGeoPointDTO dto = ConvertHelper.convert(r,PunchGeoPointDTO.class);
                return dto;
            }).collect(Collectors.toList()));
        }

        return response;
    } 
	@Override
	public void deletePunchGroup(DeleteCommonCommand cmd) {
		this.dbProvider.execute((TransactionStatus status) -> {
			 Organization organization = this.organizationProvider.findOrganizationById(cmd.getId());
			 this.organizationProvider.deleteOrganization(organization);
			 //  组织架构删除薪酬组人员关联及配置
			 this.uniongroupService.deleteUniongroupConfigresByGroupId(cmd.getId(),cmd.getOwnerId());
			 this.uniongroupService.deleteUniongroupMemberDetailByGroupId(cmd.getId(),cmd.getOwnerId());
			 //删除考勤规则
			 punchProvider.deletePunchGeopointsByOwnerId(cmd.getId());
			 punchProvider.deletePunchWifisByOwnerId(cmd.getId());
			 PunchRule pr = punchProvider.getPunchruleByPunchOrgId(cmd.getId());
			 punchProvider.deletePunchTimeRuleByPunchOrgId(cmd.getId());
			 punchProvider.deletePunchSpecialDaysByPunchOrgId(cmd.getId());
			 punchProvider.deletePunchTimeIntervalByPunchRuleId(pr.getId());
			 punchSchedulingProvider.deletePunchSchedulingByPunchRuleId(pr.getId());
			 punchProvider.deletePunchRule(pr);
			 return null;
		});
         
		
	} 
	@Override
	public PunchGroupDTO addPunchGroup(AddPunchGroupCommand cmd) {
		// 
		this.dbProvider.execute((status) -> {
			if(cmd.getRuleType() == null)
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                        ErrorCodes.ERROR_INVALID_PARAMETER,
                        "Invalid rule type parameter in the command");
            cmd.setOwnerId(getTopEnterpriseId(cmd.getOwnerId()));
            //建立考勤组
			Organization punchOrg = this.organizationService.createUniongroupOrganization(cmd.getOwnerId(),cmd.getGroupName(),UniongroupType.PUNCHGROUP.getCode());
			//添加关联
			SaveUniongroupConfiguresCommand command = new SaveUniongroupConfiguresCommand();
	        command.setGroupId(punchOrg.getId());
	        command.setGroupType(UniongroupType.PUNCHGROUP.getCode());
	        command.setEnterpriseId(cmd.getOwnerId());
	        command.setTargets(cmd.getTargets());
            try {
                this.uniongroupService.saveUniongroupConfigures(command);
            }catch(NoNodeAvailableException e){
                LOGGER.error("NoNodeAvailableException",e);
            }
	        //打卡地点和wifi
	        saveGeopointsAndWifis(punchOrg.getId(),cmd.getPunchGeoPoints(),cmd.getWifis());
	
	        PunchRule pr = ConvertHelper.convert(cmd, PunchRule.class);
            pr.setChinaHolidayFlag(cmd.getChinaHolidayFlag());
	        pr.setOwnerType(PunchOwnerType.ORGANIZATION.getCode());
	        pr.setOwnerId(cmd.getOwnerId());  
	        pr.setChinaHolidayFlag(cmd.getChinaHolidayFlag());
	        pr.setName(cmd.getGroupName());
	        pr.setRuleType(cmd.getRuleType());
	        pr.setPunchOrganizationId( punchOrg.getId());  
	        punchProvider.createPunchRule(pr); 
	        //打卡时间
			savePunchTimeRule(ConvertHelper.convert(cmd, PunchGroupDTO.class),pr);
			return null;
		});
		return null;
	} 
	/**
	 * 根据orgId,先删除后添加
	 * */
	private void saveGeopointsAndWifis(Long orgId, List<PunchGeoPointDTO> punchGeoPoints,
			List<PunchWiFiDTO> wifis) {
		punchProvider.deletePunchGeopointsByOwnerId(orgId);
		punchProvider.deletePunchWifisByOwnerId(orgId);
		if(null != punchGeoPoints){
        	for(PunchGeoPointDTO point:punchGeoPoints){
        		PunchGeopoint punchGeopoint =convertDTO2GeoPoint(point);
        		punchGeopoint.setOwnerId(orgId);  
        		punchProvider.createPunchGeopoint(punchGeopoint);
        	}
        }
        if(null != wifis){
        	for(PunchWiFiDTO wifi: wifis){ 
        		PunchWifi punchWifi = convertDTO2Wifi(wifi); 
        		punchWifi.setOwnerId(orgId);  
        		punchProvider.createPunchWifi(punchWifi);
        	}
        }
	}
	/**
	 * 根据pr ,先删除再添加打卡时间,排班,特殊日期
	 * */
	private void savePunchTimeRule(PunchGroupDTO punchGroupDTO, PunchRule pr) {
		Long punchOrgId = pr.getPunchOrganizationId();
		punchProvider.deletePunchTimeRuleByPunchOrgId(punchOrgId);
		punchProvider.deletePunchSpecialDaysByPunchOrgId(punchOrgId);
		punchProvider.deletePunchTimeIntervalByPunchRuleId(pr.getId());
		punchSchedulingProvider.deletePunchSchedulingByPunchRuleId(pr.getId());
        List<PunchTimeRule> ptrs = new ArrayList<>();
        if(null != punchGroupDTO.getTimeRules()){
        	for(PunchTimeRuleDTO timeRule:punchGroupDTO.getTimeRules()){
        		if(timeRule.getPunchTimeIntervals() == null || timeRule.getPunchTimeIntervals().size() == 0)
        			continue;
        		PunchTimeRule ptr =ConvertHelper.convert(timeRule, PunchTimeRule.class);
                ptr.setOwnerType(PunchOwnerType.ORGANIZATION.getCode());
                ptr.setOwnerId(punchOrgId);  
        		ptr.setPunchTimesPerDay((byte) (timeRule.getPunchTimeIntervals().size()*2));
        		ptr.setPunchRuleId(pr.getId());
        		ptr.setPunchOrganizationId(punchOrgId);
        		ptr.setFlexTimeLong(timeRule.getFlexTime());
        		ptrs.add(ptr);
        		if(pr.getRuleType().equals(PunchRuleType.GUDING.getCode())){
        			//固定班次 默认第二天4点
        			ptr.setDaySplitTimeLong(28*3600*1000L);
        		}else{
        			//排班制 是下班时间+弹性时间+结束打卡时间
        			if(timeRule.getHommizationType().equals(PunchHommizationType.LATEARRIVE_LATELEAVE.getCode()))
	        			ptr.setDaySplitTimeLong(timeRule.getPunchTimeIntervals().get(timeRule.getPunchTimeIntervals().size()-1).getLeaveTime()
	        					+(timeRule.getFlexTime()==null?0:timeRule.getFlexTime())
	        					+timeRule.getEndPunchTime());
        			else {
        				ptr.setDaySplitTimeLong(timeRule.getPunchTimeIntervals().get(timeRule.getPunchTimeIntervals().size()-1).getLeaveTime()
	        					+timeRule.getEndPunchTime());
        			}
        		}
        		if(timeRule.getPunchTimeIntervals().size()==1){
        			ptr.setStartEarlyTimeLong(timeRule.getPunchTimeIntervals().get(0).getArriveTime());
        			ptr.setStartLateTimeLong(timeRule.getPunchTimeIntervals().get(0).getArriveTime()+(timeRule.getFlexTime()==null?0:timeRule.getFlexTime()));
        			ptr.setWorkTimeLong(timeRule.getPunchTimeIntervals().get(0).getLeaveTime() - timeRule.getPunchTimeIntervals().get(0).getArriveTime());
        			punchProvider.createPunchTimeRule(ptr);
        		}else if(timeRule.getPunchTimeIntervals().size()==2){
        			ptr.setStartEarlyTimeLong(timeRule.getPunchTimeIntervals().get(0).getArriveTime());
        			ptr.setStartLateTimeLong(timeRule.getPunchTimeIntervals().get(0).getArriveTime()+(timeRule.getFlexTime()==null?0:timeRule.getFlexTime()));
        			ptr.setNoonLeaveTimeLong(timeRule.getPunchTimeIntervals().get(0).getLeaveTime());
        			ptr.setAfternoonArriveTimeLong(timeRule.getPunchTimeIntervals().get(1).getArriveTime());
        			ptr.setWorkTimeLong(timeRule.getPunchTimeIntervals().get(1).getLeaveTime() - timeRule.getPunchTimeIntervals().get(0).getArriveTime());
        			punchProvider.createPunchTimeRule(ptr);
        		}else{
        			punchProvider.createPunchTimeRule(ptr);
        			for(PunchTimeIntervalDTO interval:timeRule.getPunchTimeIntervals()){
        				PunchTimeInterval ptInterval = ConvertHelper.convert(ptr, PunchTimeInterval.class);
        				ptInterval.setArriveTimeLong(interval.getArriveTime());
        				ptInterval.setLeaveTimeLong(interval.getLeaveTime());
        				ptInterval.setPunchRuleId(pr.getId());
        				ptInterval.setTimeRuleId(ptr.getId());
        				punchProvider.createPunchTimeInterval(ptInterval);
        				
        			}
        		} 
        	}
        }
        
        //特殊日期
        if(null != punchGroupDTO.getSpecialDays()){
        	for(PunchSpecialDayDTO specialDayDTO : punchGroupDTO.getSpecialDays()){
        		PunchSpecialDay psd =ConvertHelper.convert(specialDayDTO, PunchSpecialDay.class);
        		psd.setOwnerType(PunchOwnerType.ORGANIZATION.getCode());
        		psd.setOwnerId(punchGroupDTO.getOwnerId());  
        		psd.setPunchRuleId(pr.getId());
        		psd.setPunchOrganizationId(punchOrgId);  
        		psd.setRuleDate(new java.sql.Date(specialDayDTO.getRuleDate())); 
				if(null != specialDayDTO.getTimeRule()){
					PunchTimeRuleDTO timeRule = specialDayDTO.getTimeRule();
					PunchTimeRule ptr2 =ConvertHelper.convert(timeRule, PunchTimeRule.class); 
	        		ptr2.setPunchTimesPerDay((byte) (timeRule.getPunchTimeIntervals().size()*2)); 
	        		ptr2.setFlexTimeLong(timeRule.getFlexTime()); 
	        		if(timeRule.getPunchTimeIntervals().size()==1){
	        			ptr2.setStartEarlyTimeLong(timeRule.getPunchTimeIntervals().get(0).getArriveTime());
	        			ptr2.setStartLateTimeLong(timeRule.getPunchTimeIntervals().get(0).getArriveTime()+(timeRule.getFlexTime()==null?0:timeRule.getFlexTime()));
	        			ptr2.setWorkTimeLong(timeRule.getPunchTimeIntervals().get(0).getLeaveTime() - timeRule.getPunchTimeIntervals().get(0).getArriveTime());
	        			punchProvider.createPunchTimeRule(ptr2);
	        		}else if(timeRule.getPunchTimeIntervals().size()==2){
	        			ptr2.setStartEarlyTimeLong(timeRule.getPunchTimeIntervals().get(0).getArriveTime());
	        			ptr2.setStartLateTimeLong(timeRule.getPunchTimeIntervals().get(0).getArriveTime()+(timeRule.getFlexTime()==null?0:timeRule.getFlexTime()));
	        			ptr2.setNoonLeaveTimeLong(timeRule.getPunchTimeIntervals().get(0).getLeaveTime());
	        			ptr2.setAfternoonArriveTimeLong(timeRule.getPunchTimeIntervals().get(1).getArriveTime());
	        			ptr2.setWorkTimeLong(timeRule.getPunchTimeIntervals().get(1).getLeaveTime() - timeRule.getPunchTimeIntervals().get(0).getArriveTime());
	        			punchProvider.createPunchTimeRule(ptr2);
	        		}else{
	        			punchProvider.createPunchTimeRule(ptr2);
	        			for(PunchTimeIntervalDTO interval:timeRule.getPunchTimeIntervals()){
	        				PunchTimeInterval ptInterval = ConvertHelper.convert(ptr2, PunchTimeInterval.class);
	        				ptInterval.setArriveTimeLong(interval.getArriveTime());
	        				ptInterval.setLeaveTimeLong(interval.getLeaveTime());
	        				ptInterval.setPunchRuleId(pr.getId());
	        				ptInterval.setTimeRuleId(ptr2.getId());
	        				punchProvider.createPunchTimeInterval(ptInterval);
	        				
	        			}
	        		} 
	        		psd.setTimeRuleId(ptr2.getId());
				}
				punchProvider.createPunchSpecialDay(psd);
        	}
        }
        //排班
        if(punchGroupDTO.getRuleType().equals(PunchRuleType.PAIBAN.getCode()) && punchGroupDTO.getSchedulings() != null){
        	for(PunchSchedulingDTO monthScheduling : punchGroupDTO.getSchedulings()){
                for (PunchSchedulingEmployeeDTO r : monthScheduling.getEmployees()) {
                    saveEmployeeScheduling(r,monthScheduling.getMonth(),pr,ptrs);
        		}
        	}
        }
	}
	private void saveEmployeeScheduling(PunchSchedulingEmployeeDTO r, Long month, PunchRule pr, List<PunchTimeRule> ptrs) { 
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(month); 
		int i = 1; 
		for(String ruleName : r.getDaySchedulings() ){
			PunchTimeRule ptr =findPtrByName(ptrs,ruleName);
			calendar.set(Calendar.DAY_OF_MONTH, i);
			PunchScheduling ps = ConvertHelper.convert(pr, PunchScheduling.class);
			ps.setPunchRuleId(pr.getId());
			ps.setRuleDate(new java.sql.Date(calendar.getTimeInMillis()));
			if(null != ptr){
				ps.setTimeRuleId(ptr.getId());
			}else{
				ps.setTimeRuleId(0L);
			}
			ps.setTargetType(PunchTargetType.USER.getCode());
			ps.setTargetId(r.getUserId());
			punchSchedulingProvider.createPunchScheduling(ps);
			
			i++;
		}
	}
	private PunchTimeRule findPtrByName(List<PunchTimeRule> ptrs, String ruleName) {
		for(PunchTimeRule ptr : ptrs){
			if(ptr.getName().equals(ruleName))
				return ptr;
		}
		return null;
	}
	private PunchWifi convertDTO2Wifi(PunchWiFiDTO wifi) {
		PunchWifi punchWifi = ConvertHelper.convert(wifi, PunchWifi.class);
		punchWifi.setOwnerType(PunchOwnerType.ORGANIZATION.getCode());
		punchWifi.setCreatorUid(UserContext.current().getUser().getId());
		punchWifi.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
				.getTime())); 
		return punchWifi;
	}
	private PunchGeopoint convertDTO2GeoPoint(PunchGeoPointDTO point) {
		PunchGeopoint punchGeopoint = ConvertHelper.convert(point, PunchGeopoint.class);
 		punchGeopoint.setOwnerType(PunchOwnerType.ORGANIZATION.getCode());
 		punchGeopoint.setCreatorUid(UserContext.current().getUser().getId());
 		punchGeopoint.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
 		punchGeopoint.setGeohash(GeoHashUtils.encode(
 				punchGeopoint.getLatitude(), punchGeopoint.getLongitude()));
		return punchGeopoint;
	}

	@Override
	public PunchGroupDTO getPunchGroup(GetPunchGroupCommand cmd) {
		Organization org = organizationProvider.findOrganizationById(cmd.getId());
		return getPunchGroupDTOByOrg(org);
	}
	
	@Override
	public ListPunchGroupsResponse listPunchGroups(ListPunchGroupsCommand cmd) { 
		ListPunchGroupsResponse response = new ListPunchGroupsResponse();
        Organization org = organizationProvider.findOrganizationById(cmd.getOwnerId());
        Integer allOrganizationInteger = organizationProvider.countOrganizationMemberDetailsByOrgId(org.getNamespaceId(), cmd.getOwnerId());
		response.setAllEmployeeCount(allOrganizationInteger);
		// 未关联人数 
		List<OrganizationMemberDetails> details = uniongroupConfigureProvider.listDetailNotInUniongroup(org.getNamespaceId(), org.getId());
		if (null != details && details.size()>0)
			response.setUnjoinPunchGroupEmployees(details.stream().map(r ->{
				OrganizationMemberDetailDTO dto = ConvertHelper.convert(r, OrganizationMemberDetailDTO.class);
				Map<Long,String> departMap = this.organizationProvider.listOrganizationsOfDetail(org.getNamespaceId(),r.getId(),OrganizationGroupType.DEPARTMENT.getCode());
		        String department = ""; 
		        if(!StringUtils.isEmpty(departMap)){
		            for(Long k : departMap.keySet()){
		                department += (departMap.get(k) + ",");
		            }
		            department = department.substring(0,department.length()-1);
		        }
		        dto.setDepartment(department);
				return dto;
			}).collect(Collectors.toList()));

        //  获取所有批次
		if (cmd.getPageAnchor() == null)
			cmd.setPageAnchor(0L);
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());
		   
        List<Organization> organizations = this.organizationProvider.listOrganizationsByGroupType(UniongroupType.PUNCHGROUP.getCode(), cmd.getOwnerId(),locator,pageSize + 1 );
        
        if (null == organizations)
			return response;
		Long nextPageAnchor = null;
		if (organizations != null && organizations.size() > pageSize) {
			organizations.remove(organizations.size() - 1);
			nextPageAnchor = organizations.get(organizations.size() - 1).getId();
		}
		response.setNextPageAnchor(nextPageAnchor);
        List<PunchGroupDTO> punchGroups = organizations.stream().map(r -> {
        	PunchGroupDTO dto = getPunchGroupDTOByOrg(r);
            return dto;
        }).collect(Collectors.toList());
        response.setPunchGroups(punchGroups);
		return response;
	}
	private PunchGroupDTO getPunchGroupDTOByOrg(Organization r) {
		PunchRule pr = punchProvider.getPunchruleByPunchOrgId(r.getId());
		if(null == pr )
			return null;
		PunchGroupDTO dto = ConvertHelper.convert(pr, PunchGroupDTO.class);
		dto.setGroupName(r.getName());
		dto.setId(pr.getPunchOrganizationId());
		Integer totalCount = uniongroupConfigureProvider.countUnionGroupMemberDetailsByGroupId(r.getNamespaceId(),r.getId());
		dto.setEmployeeCount(totalCount);
		// 关联 人员和机构
		GetUniongroupConfiguresCommand cmd1 = new GetUniongroupConfiguresCommand();
		cmd1.setGroupId(r.getId());
		List<UniongroupConfiguresDTO> resp = uniongroupService.getConfiguresListByGroupId(cmd1);
		dto.setTargets(new ArrayList<UniongroupTarget>());
		if(null != resp){
			for (UniongroupConfiguresDTO obj : resp){
				UniongroupTarget target = new UniongroupTarget();
				target.setName(obj.getCurrentName());
				target.setId(obj.getCurrentId());
				target.setType(obj.getCurrentType());
				dto.getTargets().add(target);
			}
		}
		//打卡时间
		List<PunchTimeRule> timeRules = punchProvider.listPunchTimeRuleByOwner(PunchOwnerType.ORGANIZATION.getCode(),r.getId());
		if(null != timeRules && timeRules.size() > 0)
			dto.setTimeRules(timeRules.stream().map(r1 -> {
				PunchTimeRuleDTO dto1 = convertPunchTimeRule2DTO(r1) ;
	            return dto1;
	        }).collect(Collectors.toList()));
		
		//打卡地点和WiFi 
		dto.setPunchGeoPoints(new ArrayList<>());
		dto.setWifis(new ArrayList<>());
		List<PunchGeopoint> points = punchProvider.listPunchGeopointsByOwner(PunchOwnerType.ORGANIZATION.getCode(),r.getId());
		if(null != points)
			for(PunchGeopoint point:points){
				PunchGeoPointDTO dto1 = ConvertHelper.convert(point, PunchGeoPointDTO.class);
				dto.getPunchGeoPoints().add(dto1);
			}
		List<PunchWifi> wifis = punchProvider.listPunchWifsByOwner(PunchOwnerType.ORGANIZATION.getCode(),r.getId());
		if(null != wifis)
			for(PunchWifi wifi : wifis ){
				PunchWiFiDTO dto1 = ConvertHelper.convert(wifi, PunchWiFiDTO.class);
				dto.getWifis().add(dto1);
			}
		
		//排班和非排班的特殊处理
		if(pr.getRuleType().equals(PunchRuleType.PAIBAN.getCode())){
			Calendar start = Calendar.getInstance();
			start.set(Calendar.DAY_OF_MONTH, 1);
			Calendar end = Calendar.getInstance();
			end.add(Calendar.MONTH, 1);
			Integer linkedCount = punchSchedulingProvider.countSchedulingUser(pr.getId(),new java.sql.Date(start.getTimeInMillis()),new java.sql.Date(end.getTimeInMillis()));
			dto.setUnSchedulingCount(totalCount - linkedCount);
			//排班
			List<PunchSchedulingDTO> schedulings = processschedulings(pr,new java.sql.Date(start.getTimeInMillis()),new java.sql.Date(end.getTimeInMillis()));
			dto.setSchedulings(schedulings);
		}else{
	        //特殊日期
			List<PunchSpecialDay> specialDays = punchProvider.listPunchSpecailDaysByOrgId(pr.getPunchOrganizationId());
			if(null != specialDays ){
				dto.setSpecialDays(new ArrayList<>());
				for(PunchSpecialDay specialDay : specialDays){
					PunchSpecialDayDTO dto1 =ConvertHelper.convert(specialDay, PunchSpecialDayDTO.class);
					if(null != specialDay.getTimeRuleId()){
						PunchTimeRule timeRule = punchProvider.getPunchTimeRuleById(specialDay.getTimeRuleId());
						if(null != timeRule){
							PunchTimeRuleDTO timeRuleDTO = convertPunchTimeRule2DTO(timeRule);
							dto1.setTimeRule(timeRuleDTO);
						}
					}
					dto.getSpecialDays().add(dto1);
				}
			}
		} 
		return dto;
	}
	private List<PunchSchedulingDTO> processschedulings(PunchRule pr, java.sql.Date startDate,
			java.sql.Date endDate) { 
		List<PunchSchedulingDTO> result = new ArrayList<PunchSchedulingDTO>();
		PunchSchedulingDTO dto = new PunchSchedulingDTO();
		dto.setMonth(startDate.getTime());
		List<PunchScheduling> schedulings = punchSchedulingProvider.queryPunchSchedulings(null, Integer.MAX_VALUE,new ListingQueryBuilderCallback()  {
			@Override
			public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
					SelectQuery<? extends Record> query) { 
				query.addConditions(Tables.EH_PUNCH_SCHEDULINGS.RULE_DATE.greaterOrEqual(startDate));
				query.addConditions(Tables.EH_PUNCH_SCHEDULINGS.RULE_DATE.lt(endDate));
				query.addConditions(Tables.EH_PUNCH_SCHEDULINGS.TIME_RULE_ID.isNotNull());
				query.addConditions(Tables.EH_PUNCH_SCHEDULINGS.PUNCH_RULE_ID.eq(pr.getId())); 
				query.addConditions(Tables.EH_PUNCH_SCHEDULINGS.TARGET_TYPE.eq(PunchTargetType.USER.getCode()));
				query.addOrderBy(Tables.EH_PUNCH_SCHEDULINGS.TARGET_ID.asc());
				return null;
			}
		});
		if(null != schedulings){
			Map<Long, List<PunchScheduling>> scheMap = new HashMap<>();
			for(PunchScheduling sche : schedulings){
				if(scheMap.get(sche.getTargetId()) == null)
					scheMap.put(sche.getTargetId(), new ArrayList<PunchScheduling>()); 
				scheMap.get(sche.getTargetId()).add(sche);
			}
			List<PunchSchedulingEmployeeDTO> employeeDTOs = new ArrayList<PunchSchedulingEmployeeDTO>();
			//循环每个人
			for(Long detaliId : scheMap.keySet()){
				PunchSchedulingEmployeeDTO employeeDTO = new PunchSchedulingEmployeeDTO();
				employeeDTO.setDaySchedulings(new ArrayList<>());
				Organization organization = organizationProvider.findOrganizationById(pr.getOwnerId()); 
				employeeDTO.setUserId(detaliId);
                OrganizationMemberDetails memberDetail = organizationProvider.findOrganizationMemberDetailsByDetailId(detaliId);
                employeeDTO.setContactName(memberDetail.getContactName());
				
				//循环每个人这个月每一天
				Calendar start = Calendar.getInstance();
				start.setTime(startDate);
				Calendar end = Calendar.getInstance();
				end.setTime(endDate);
				for(;start.before(end);start.add(Calendar.DAY_OF_MONTH, 1)){
					PunchScheduling scheduling = findSchedlingByDate(scheMap.get(detaliId),new java.sql.Date(start.getTimeInMillis()));
					if(null != scheduling){
						if(scheduling.getTimeRuleId()==null || scheduling.getTimeRuleId().equals(0L)){
							employeeDTO.getDaySchedulings().add("休息");
						}
						else{
							PunchTimeRule ptr = punchProvider.findPunchTimeRuleById(scheduling.getTimeRuleId());
							if(null != ptr){
								employeeDTO.getDaySchedulings().add(ptr.getName()); 
							}
							else{
								employeeDTO.getDaySchedulings().add(""); 
							}
						}
					}
					else{
						employeeDTO.getDaySchedulings().add(""); 
					}
				}
				employeeDTOs.add(employeeDTO);
			}
			dto.setEmployees(employeeDTOs);
		}
		result.add(dto);
		return result;
	} 
	private PunchScheduling findSchedlingByDate(List<PunchScheduling> list, java.sql.Date date) {
		if(null != list)
			for(PunchScheduling sche : list){
				if(sche.getRuleDate().equals(date) && sche.getTimeRuleId()!=null)
					return sche;
			}
		return null;
	}
	private PunchTimeRuleDTO convertPunchTimeRule2DTO(PunchTimeRule r) { 
		PunchTimeRuleDTO dto = ConvertHelper.convert(r, PunchTimeRuleDTO.class);
		dto.setFlexTime(r.getFlexTimeLong());
		dto.setAfternoonArriveTime(r.getAfternoonArriveTimeLong());
		dto.setBeginPunchTime(r.getBeginPunchTime());
		dto.setEndPunchTime(r.getEndPunchTime());
		dto.setAfternoonArriveTime(r.getAfternoonArriveTimeLong());
		dto.setPunchTimeIntervals(new ArrayList<>());
		if(r.getPunchTimesPerDay().equals((byte)2)){
			PunchTimeIntervalDTO intervalDTO = new PunchTimeIntervalDTO();
			intervalDTO.setArriveTime(r.getStartEarlyTimeLong());
			intervalDTO.setLeaveTime(r.getStartEarlyTimeLong() +r.getWorkTimeLong());
			dto.getPunchTimeIntervals().add(intervalDTO);
		}else if(r.getPunchTimesPerDay().equals((byte)4)){
			PunchTimeIntervalDTO intervalDTO = new PunchTimeIntervalDTO();
			intervalDTO.setArriveTime(r.getStartEarlyTimeLong());
			intervalDTO.setLeaveTime(r.getNoonLeaveTimeLong());
			dto.getPunchTimeIntervals().add(intervalDTO);
			intervalDTO = new PunchTimeIntervalDTO();
			intervalDTO.setArriveTime(r.getAfternoonArriveTimeLong());
			intervalDTO.setLeaveTime(r.getStartEarlyTimeLong() +r.getWorkTimeLong());
			dto.getPunchTimeIntervals().add(intervalDTO);
		}
		else{
			List<PunchTimeInterval> intervals = punchProvider.listPunchTimeIntervalByTimeRuleId(r.getId());
			if(null != intervals)
				for(PunchTimeInterval interval : intervals){
					PunchTimeIntervalDTO intervalDTO = ConvertHelper.convert(interval, PunchTimeIntervalDTO.class); 
					intervalDTO.setArriveTime(interval.getArriveTimeLong());
					intervalDTO.setLeaveTime(interval.getLeaveTimeLong());
					dto.getPunchTimeIntervals().add(intervalDTO); 
				}
		}
		
		return dto;
	}
	@Override
	public PunchGroupDTO updatePunchGroup(PunchGroupDTO cmd) {
		//
		if(cmd.getRuleType() == null)

			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid rule type parameter in the command");
		//获取考勤组
		Organization punchOrg = this.organizationProvider.findOrganizationById(cmd.getId());
		punchOrg.setName(cmd.getGroupName());
		organizationProvider.updateOrganization(punchOrg);
		PunchRule pr = punchProvider.getPunchruleByPunchOrgId(cmd.getId());
		//添加关联
		SaveUniongroupConfiguresCommand command = new SaveUniongroupConfiguresCommand();
        command.setGroupId(punchOrg.getId());
        command.setGroupType(UniongroupType.PUNCHGROUP.getCode());
        command.setEnterpriseId(cmd.getOwnerId());
        command.setTargets(cmd.getTargets());
        try {
            this.uniongroupService.saveUniongroupConfigures(command);
        }catch(NoNodeAvailableException e){
            LOGGER.error("NoNodeAvailableException",e);
        }
        //打卡地点和wifi
        saveGeopointsAndWifis(punchOrg.getId(),cmd.getPunchGeoPoints(),cmd.getWifis());
        
        //打卡时间,特殊日期,排班等
        pr.setOwnerType(PunchOwnerType.ORGANIZATION.getCode());
        pr.setOwnerId(cmd.getOwnerId());  
        pr.setChinaHolidayFlag(cmd.getChinaHolidayFlag());
        pr.setName(cmd.getGroupName());
        pr.setRuleType(cmd.getRuleType());
        pr.setPunchOrganizationId( punchOrg.getId());
        punchProvider.updatePunchRule(pr); 
       
        savePunchTimeRule(cmd, pr);
		return null;
	}
	@Override
	public GetPunchDayStatusResponse getPunchDayStatus(GetPunchDayStatusCommand cmd) {
		// 
		cmd.setEnterpriseId(getTopEnterpriseId(cmd.getEnterpriseId()));
		Long userId = UserContext.current().getUser().getId();
		Date punchTime = new Date();
		PunchLogDTO punchLog = getPunchType(userId,cmd.getEnterpriseId(),punchTime);
		GetPunchDayStatusResponse response = ConvertHelper.convert(punchLog, GetPunchDayStatusResponse.class);
		response.setPunchLogs(new ArrayList<>());
		PunchRule pr = getPunchRule(PunchOwnerType.ORGANIZATION.getCode(), cmd.getEnterpriseId(), userId);
		if (null == pr  )
			throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
					PunchServiceErrorCode.ERROR_ENTERPRISE_DIDNOT_SETTING,
					"公司没有设置打卡规则");
		Long ptrId = getPunchTimeRuleIdByRuleIdAndDate(pr, punchTime, userId);
        PunchDayLog pdl = punchProvider.findPunchDayLog(userId, cmd.getEnterpriseId(), new java.sql.Date(cmd.getQueryTime()));
        if(null != pdl)
            response.setStatusList(pdl.getStatusList());
        List<PunchLog> punchLogs = punchProvider.listPunchLogsByDate(userId,cmd.getEnterpriseId(), dateSF.get().format(punchTime),
				ClockCode.SUCESS.getCode());
		if (null != ptrId) {
			PunchTimeRule ptr = punchProvider.getPunchTimeRuleById(ptrId);
			for(Integer punchIntervalNo= 1;punchIntervalNo <= ptr.getPunchTimesPerDay()/2;punchIntervalNo++) {
				PunchLogDTO dto = null;
				PunchLog pl = findPunchLog(punchLogs,PunchType.ON_DUTY.getCode(),punchIntervalNo);
				if(null == pl){
					dto = new PunchLogDTO();
					dto.setClockStatus(PunchStatus.UNPUNCH.getCode());
					dto.setPunchType(PunchType.ON_DUTY.getCode());
					dto.setPunchIntervalNo(punchIntervalNo);
					dto.setRuleTime(findRuleTime(ptr,dto.getPunchType(),punchIntervalNo));
				}else{
					dto = ConvertHelper.convert(pl, PunchLogDTO.class);
				}
				response.getPunchLogs().add(dto);

				dto = null;
				pl = findPunchLog(punchLogs,PunchType.OFF_DUTY.getCode(),punchIntervalNo);
				if(null == pl){
					dto = new PunchLogDTO();
					dto.setClockStatus(PunchStatus.UNPUNCH.getCode());
					dto.setPunchType(PunchType.OFF_DUTY.getCode());
					dto.setPunchIntervalNo(punchIntervalNo);
					dto.setRuleTime(findRuleTime(ptr,dto.getPunchType(),punchIntervalNo));
				}else{
					dto = ConvertHelper.convert(pl, PunchLogDTO.class);
				}
				response.getPunchLogs().add(dto);
			}
		}
		return response;
	}

	private long findRuleTime(PunchTimeRule ptr, Byte punchType, Integer punchIntervalNo) {
		if(ptr.getPunchTimesPerDay().intValue() == 2){
			if (punchType.equals(PunchType.ON_DUTY.getCode())) {
				return ptr.getStartEarlyTimeLong();
			}else{
				return ptr.getStartEarlyTimeLong() + ptr.getWorkTimeLong();
			}
		}else if (ptr.getPunchTimesPerDay().intValue() ==4){
			if(punchIntervalNo.equals(1)){
				if (punchType.equals(PunchType.ON_DUTY.getCode())) {
					return ptr.getStartEarlyTimeLong();
				}else{
					return ptr.getNoonLeaveTimeLong();
				}
			}else{
				if (punchType.equals(PunchType.ON_DUTY.getCode())) {
					return ptr.getAfternoonArriveTimeLong();
				}else{
					return ptr.getStartEarlyTimeLong() + ptr.getWorkTimeLong();
				}
			}
		}else {
			List<PunchTimeInterval> intervals = punchProvider.listPunchTimeIntervalByTimeRuleId(ptr.getId());
			if (null == intervals)
				throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
						PunchServiceErrorCode.ERROR_ENTERPRISE_DIDNOT_SETTING,
						"公司没有设置打卡时间段");
			if (punchType.equals(PunchType.ON_DUTY.getCode())) {
				return intervals.get(punchIntervalNo - 1).getArriveTimeLong();
			} else {
				return intervals.get(punchIntervalNo - 1).getLeaveTimeLong();
			}
		}
	}

	static final Long ONE_DAY_MS = 24*3600*1000L;
	/**
	 * 获取打卡状态: 
	 * list: 元素0 : 上班打卡/下班打卡/不用打卡 元素1:第几次排班的打卡
	 * */
	private PunchLogDTO getPunchType(Long userId, Long enterpriseId, Date punchTime) {
		PunchLogDTO result = new PunchLogDTO();
		// 获取打卡规则->timerule
		PunchRule pr = getPunchRule(PunchOwnerType.ORGANIZATION.getCode(), enterpriseId, userId);
		if (null == pr  )
			throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
 					PunchServiceErrorCode.ERROR_ENTERPRISE_DIDNOT_SETTING,
 				"公司没有设置打卡规则");
		Long ptrId = getPunchTimeRuleIdByRuleIdAndDate(pr, punchTime, userId);
		if(null == ptrId){
			if(pr.getRuleType().equals(PunchRuleType.GUDING.getCode())){
				result.setPunchType(PunchType.NOT_WORKDAY.getCode());
			}else{
			//没排班
				result.setPunchType(PunchType.MEIPAIBAN.getCode());
			}
			result.setPunchIntervalNo(0);
			return result;
		}
		Calendar punCalendar = Calendar.getInstance(); 
		punCalendar.setTime(punchTime); 
		//把当天的时分秒转换成Long型
		Long punchTimeLong = getTimeLong(punCalendar);
		List<PunchLog> punchLogs = punchProvider.listPunchLogsByDate(userId,enterpriseId, dateSF.get().format(punchTime),
				ClockCode.SUCESS.getCode());
		int punchIntevalNo = 1;
		PunchTimeRule ptr = punchProvider.getPunchTimeRuleById(ptrId);
		if(ptr.getPunchTimesPerDay().equals((byte)2)){
			//对于2次打卡:
			return calculate2timePunchStatus(ptr,punchTimeLong,punchLogs,result);
		}else if(ptr.getPunchTimesPerDay().equals((byte)4)){
			return calculate4timePunchStatus(ptr, punchTimeLong, punchLogs, result, punchIntevalNo);
		}else{
			//对于多次打卡
			return calculateMoretimePunchStatus(ptr, punchTimeLong, punchLogs, result, punchIntevalNo);
		}
	}

    private Long getTimeLong(Calendar punCalendar) {

        Long punchTimeLong = punCalendar.get(Calendar.HOUR_OF_DAY)*3600*1000L; //hour
        punchTimeLong += punCalendar.get(Calendar.MINUTE)*60*1000L; //min
        punchTimeLong += punCalendar.get(Calendar.SECOND)*1000L;//second
        return punchTimeLong;
    }

    private PunchLogDTO calculateMoretimePunchStatus(PunchTimeRule ptr, Long punchTimeLong,
												  List<PunchLog> punchLogs, PunchLogDTO result,Integer punchIntevalNo) {
		List<PunchTimeInterval> intervals = punchProvider.listPunchTimeIntervalByTimeRuleId(ptr.getId());
		if(null == intervals )
			throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
					PunchServiceErrorCode.ERROR_ENTERPRISE_DIDNOT_SETTING,
					"公司没有设置打卡时间段");
		//非打卡时段
		if((punchTimeLong < (intervals.get(0).getArriveTimeLong()-(timeIsNull(ptr.getBeginPunchTime(),ONE_DAY_MS)))) ||
				(punchTimeLong > (intervals.get(intervals.size()-1).getLeaveTimeLong()+timeIsNull(ptr.getEndPunchTime(), ONE_DAY_MS)))){
			result.setPunchType(PunchType.NOT_WORKTIME.getCode());
			result.setPunchIntervalNo(punchIntevalNo);
			return result ;
		}

		for(int i =0 ;i<intervals.size();i++){
			//循环每一次打卡
			punchIntevalNo=i+1;
			if(i == 0){
				//第一次: 如果时间在最早打卡后 下一次上班打卡之前为本次打卡
				if(punchTimeLong > (intervals.get(i).getArriveTimeLong()-(timeIsNull(ptr.getBeginPunchTime(),ONE_DAY_MS)))
						&& punchTimeLong < intervals.get(i+1).getArriveTimeLong()){
					PunchLog onDutyPunch = findPunchLog(punchLogs,PunchType.ON_DUTY.getCode(),punchIntevalNo);
					if(null == onDutyPunch){
						return processOndutyPunchLog(ptr,intervals,result,punchIntevalNo,punchTimeLong);
					}
					PunchLog offDutyPunch = findPunchLog(punchLogs,PunchType.OFF_DUTY.getCode(),punchIntevalNo);
					if(null == offDutyPunch){
						return processOffdutyPunchLog(ptr,intervals,result,punchIntevalNo,punchTimeLong);
					}else{
						continue;
					}
				}
			}else if (i == intervals.size()-1){
				//最后一次  如果打卡时间比本次下班时间+最晚下班时间早  则是本次打卡否则
				if(  punchTimeLong < intervals.get(i).getLeaveTimeLong()+timeIsNull(ptr.getEndPunchTime(),ONE_DAY_MS)){
					PunchLog onDutyPunch = findPunchLog(punchLogs,PunchType.ON_DUTY.getCode(),punchIntevalNo);
					if(null == onDutyPunch){
						return processOndutyPunchLog(ptr,intervals,result,punchIntevalNo,punchTimeLong);
					}
					PunchLog offDutyPunch = findPunchLog(punchLogs,PunchType.OFF_DUTY.getCode(),punchIntevalNo);
					if(null == offDutyPunch){
						return processOffdutyPunchLog(ptr,intervals,result,punchIntevalNo,punchTimeLong);
					}else{
						result = processOffdutyPunchLog(ptr,intervals,result,punchIntevalNo,punchTimeLong);
						result.setPunchType(PunchType.FINISH.getCode());
						return result ;
					}
				}
			}else{
				//中间的
				//如果时间在下一次上班打卡之前则是本次打卡
				//如果本次没有上班打卡,则是本次上班打卡.
				//本次有上班打卡,没有下班打卡,则是本次下班打卡,否则是下次打卡
				if(punchTimeLong < intervals.get(i+1).getArriveTimeLong()){
					PunchLog onDutyPunch = findPunchLog(punchLogs,PunchType.ON_DUTY.getCode(),punchIntevalNo);
					if(null == onDutyPunch){
						return processOndutyPunchLog(ptr,intervals,result,punchIntevalNo,punchTimeLong);
					}
					PunchLog offDutyPunch = findPunchLog(punchLogs,PunchType.OFF_DUTY.getCode(),punchIntevalNo);
					if(null == offDutyPunch){
						return processOffdutyPunchLog(ptr,intervals,result,punchIntevalNo,punchTimeLong);
					}else{
						continue;
					}
				}

			}
		}
		return result ;
	}

	private PunchLogDTO processOffdutyPunchLog(PunchTimeRule ptr, List<PunchTimeInterval> intervals, PunchLogDTO result, Integer punchIntevalNo, Long punchTimeLong) {
		result.setPunchType(PunchType.OFF_DUTY.getCode());
		result.setPunchIntervalNo(punchIntevalNo);
		result.setRuleTime(intervals.get(punchIntevalNo-1).getLeaveTimeLong());
		long leaveTime = result.getRuleTime();
		if(ptr.getHommizationType().equals(HommizationType.FLEX.getCode())){
			leaveTime = leaveTime + ptr.getFlexTimeLong();
		}
		if(punchTimeLong < leaveTime)
			result.setClockStatus(PunchStatus.LEAVEEARLY.getCode());
		else
			result.setClockStatus(PunchStatus.NORMAL.getCode());
		result.setPunchNormalTime(result.getRuleTime());
		if(punchIntevalNo.equals(intervals.size())){
			result.setExpiryTime(intervals.get(punchIntevalNo - 1).getLeaveTimeLong() + timeIsNull(ptr.getEndPunchTime(), ONE_DAY_MS));
		}else{
			result.setExpiryTime(intervals.get(0).getArriveTimeLong());
		}
		return result ;
	}

	private PunchLogDTO processOndutyPunchLog(PunchTimeRule ptr, List<PunchTimeInterval> intervals, PunchLogDTO result, Integer punchIntevalNo, Long punchTimeLong) {
		result.setPunchType(PunchType.ON_DUTY.getCode());
		result.setPunchIntervalNo(punchIntevalNo);
		result.setRuleTime(intervals.get(punchIntevalNo-1).getArriveTimeLong());
		long arriveTIme = result.getRuleTime();
		if(ptr.getHommizationType().equals(HommizationType.FLEX.getCode())){
			arriveTIme = arriveTIme + ptr.getFlexTimeLong();
		}
		if(punchTimeLong > arriveTIme)
			result.setClockStatus(PunchStatus.BELATE.getCode());
		else
			result.setClockStatus(PunchStatus.NORMAL.getCode());
		result.setPunchNormalTime(intervals.get(punchIntevalNo-1).getArriveTimeLong()-(timeIsNull(ptr.getBeginPunchTime(),ONE_DAY_MS)));
		if(punchIntevalNo.equals(intervals.size())){
			result.setExpiryTime(intervals.get(punchIntevalNo - 1).getLeaveTimeLong() + timeIsNull(ptr.getEndPunchTime(), ONE_DAY_MS));
		}else{
			result.setExpiryTime(intervals.get(0).getArriveTimeLong());
		}
		return result ;
	}

	private PunchLogDTO calculate4timePunchStatus(PunchTimeRule ptr, Long punchTimeLong,
			List<PunchLog> punchLogs, PunchLogDTO result,Integer punchIntevalNo) {
		//对于4次打卡:
		//如果在最早上班打卡之前,或者  最晚下班打卡之后. 那就是非打卡时间
		if((punchTimeLong < (ptr.getStartEarlyTimeLong()-(timeIsNull(ptr.getBeginPunchTime(),ONE_DAY_MS)))) ||
				(punchTimeLong > (ptr.getStartLateTimeLong()+ptr.getWorkTimeLong()+timeIsNull(ptr.getEndPunchTime(), ONE_DAY_MS)))){
			result.setPunchType(PunchType.NOT_WORKTIME.getCode());
			result.setPunchIntervalNo(punchIntevalNo); 
			return result ;
		}
		//如果在在最早上班打卡之后 下午上班之前
		// 则是第一段打卡,如果没有第一段上班打卡就是上班打卡,有上班打卡没有下班打卡就是下班打卡,
		if(punchTimeLong < ptr.getAfternoonArriveTimeLong()){
			PunchLog onDutyPunch = findPunchLog(punchLogs,PunchType.ON_DUTY.getCode(),punchIntevalNo);
			if(null == onDutyPunch){
				result.setPunchType(PunchType.ON_DUTY.getCode());
				result.setPunchIntervalNo(punchIntevalNo);
				result.setRuleTime(ptr.getStartEarlyTimeLong());
				if(punchTimeLong < ptr.getStartLateTimeLong())
					result.setClockStatus(PunchStatus.NORMAL.getCode());
				else
					result.setClockStatus(PunchStatus.BELATE.getCode());
				result.setExpiryTime(ptr.getAfternoonArriveTimeLong());
				result.setPunchNormalTime(ptr.getStartEarlyTimeLong());
				return result ;
			}
			PunchLog offDutyPunch = findPunchLog(punchLogs, PunchType.OFF_DUTY.getCode(), punchIntevalNo);
			if(null == offDutyPunch){
				result.setPunchType(PunchType.OFF_DUTY.getCode());
				result.setPunchIntervalNo(punchIntevalNo);
				result.setRuleTime(ptr.getNoonLeaveTimeLong());
				if(punchTimeLong < ptr.getNoonLeaveTimeLong())
					result.setClockStatus(PunchStatus.LEAVEEARLY.getCode());
				else
					result.setClockStatus(PunchStatus.NORMAL.getCode());

				result.setExpiryTime(ptr.getAfternoonArriveTimeLong());
				result.setPunchNormalTime(ptr.getNoonLeaveTimeLong());
				return result ;
			}else{

			}
		}
		// 否则则是第二段上班打卡
		punchIntevalNo = 2;
		//第二段打卡:
		// 如果有第二段上班打卡则是第二段上班打卡,如果有第二段上班打卡没有第二段下班打卡则是下班打卡,否则不打卡
		PunchLog onDutyPunch = findPunchLog(punchLogs,PunchType.ON_DUTY.getCode(),punchIntevalNo);
		if(null == onDutyPunch){
			result.setPunchType(PunchType.ON_DUTY.getCode());
			result.setPunchIntervalNo(punchIntevalNo);
			result.setRuleTime(ptr.getAfternoonArriveTimeLong());
			if(punchTimeLong < ptr.getAfternoonArriveTimeLong())
				result.setClockStatus(PunchStatus.NORMAL.getCode());
			else
				result.setClockStatus(PunchStatus.BELATE.getCode());

			result.setExpiryTime(ptr.getStartLateTimeLong() + ptr.getWorkTimeLong() + timeIsNull(ptr.getEndPunchTime(), ONE_DAY_MS));
			result.setPunchNormalTime(ptr.getAfternoonArriveTimeLong());
			return result ;
		}
		PunchLog offDutyPunch = findPunchLog(punchLogs,PunchType.OFF_DUTY.getCode(),punchIntevalNo);
		if(null == offDutyPunch){
			result.setPunchType(PunchType.OFF_DUTY.getCode());
			result.setPunchIntervalNo(punchIntevalNo);
		}else{
			result.setPunchType(PunchType.FINISH.getCode());
			result.setPunchIntervalNo(punchIntevalNo);
		}
		processLastOffDutyPunchLog(result, ptr, punchTimeLong, punchLogs);

		return result;
	}

	private PunchLogDTO calculate2timePunchStatus(PunchTimeRule ptr, Long punchTimeLong,
			List<PunchLog> punchLogs, PunchLogDTO result) {
		//如果时间在最早上班打卡之后,,如果有上班打卡 
		if(punchTimeLong > (ptr.getStartEarlyTimeLong()-(timeIsNull(ptr.getBeginPunchTime(),ONE_DAY_MS)))
				//在最晚下班打卡之前
				&& punchTimeLong < (ptr.getStartLateTimeLong()+ptr.getWorkTimeLong()+timeIsNull(ptr.getEndPunchTime(), ONE_DAY_MS))){
			PunchLog onDutyPunch = findPunchLog(punchLogs,PunchType.ON_DUTY.getCode(),1);
			// 没有上班打卡  则是上班打卡 
			if(null == onDutyPunch){
				result.setPunchType(PunchType.ON_DUTY.getCode());
				result.setPunchIntervalNo(1);  
				result.setRuleTime(ptr.getStartEarlyTimeLong());
				if(punchTimeLong < ptr.getStartLateTimeLong())
					result.setClockStatus(PunchStatus.NORMAL.getCode());
				else
					result.setClockStatus(PunchStatus.BELATE.getCode());
				result.setExpiryTime(ptr.getStartLateTimeLong() + ptr.getWorkTimeLong() + timeIsNull(ptr.getEndPunchTime(), ONE_DAY_MS));
				result.setPunchNormalTime(ptr.getStartLateTimeLong());
				return result ;
			}
			PunchLog offDutyPunch = findPunchLog(punchLogs,PunchType.OFF_DUTY.getCode(),1);
			//没有下班打卡则是下班打卡
			if(null == offDutyPunch){
				result.setPunchType(PunchType.OFF_DUTY.getCode());
				result.setPunchIntervalNo(1);
				processLastOffDutyPunchLog(result,ptr,punchTimeLong,punchLogs);
				return result ;
			}else{ 
				//否则就是已完成打卡-但是可以更新打卡
				result.setPunchType(PunchType.FINISH.getCode());
				result.setPunchIntervalNo(1); 
				return result ;
			}
		}else{
			//不在时间范围内无法打卡
			result.setPunchType(PunchType.NOT_WORKTIME.getCode());
			result.setPunchIntervalNo(1); 
			return result ; 
		}
	}

	private void processLastOffDutyPunchLog(PunchLogDTO result, PunchTimeRule ptr, Long punchTimeLong, List<PunchLog> punchLogs) {
		long leaveTime = getLeaveTime(ptr, findPunchLog(punchLogs, PunchType.ON_DUTY.getCode(), 1));
		result.setRuleTime(ptr.getStartEarlyTimeLong()+ptr.getWorkTimeLong());
		if(punchTimeLong < leaveTime){
			result.setClockStatus(PunchStatus.LEAVEEARLY.getCode());
		}else{
			result.setClockStatus(PunchStatus.NORMAL.getCode());
		}
		result.setExpiryTime(ptr.getStartLateTimeLong() + ptr.getWorkTimeLong() + timeIsNull(ptr.getEndPunchTime(), ONE_DAY_MS));
		result.setPunchNormalTime(leaveTime);
	}

	private long getLeaveTime (PunchTimeRule ptr,PunchLog onDutyPunch) {

		long leaveTime = ptr.getStartEarlyTimeLong()+ptr.getWorkTimeLong();
		if(ptr.getHommizationType().equals(HommizationType.LATEARRIVE.getCode())){

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(onDutyPunch.getPunchTime());
			//把当天的时分秒转换成Long型
			Long ondutyTime = calendar.get(Calendar.HOUR_OF_DAY)*3600*1000L; //hour
			ondutyTime += calendar.get(Calendar.MINUTE)*60*1000L; //min
			ondutyTime += calendar.get(Calendar.SECOND)*1000L;//second
			if(ondutyTime>ptr.getStartEarlyTimeLong()){
				if(ondutyTime>ptr.getStartLateTimeLong()){
					leaveTime = ptr.getStartEarlyTimeLong()+ptr.getWorkTimeLong();
				}else{
					leaveTime = ondutyTime+ptr.getWorkTimeLong();
				}
			}
		}
		return leaveTime;
	}
	//上班取最早,下班取最晚
	private PunchLog findPunchLog(List<PunchLog> punchLogs, Byte pucnhType, Integer punchIntevalNo) {
		if(null == punchLogs || punchLogs.size() == 0)
			return null;
		PunchLog result = null;
		for(PunchLog log : punchLogs){
			if(log.getPunchType().equals(pucnhType) && log.getPunchIntervalNo().equals(punchIntevalNo)){
				if(pucnhType.equals(PunchType.ON_DUTY.getCode())){
					if(result == null || log.getPunchTime().before(result.getPunchTime()))
						result = log;
				}else if(pucnhType.equals(PunchType.OFF_DUTY.getCode())){
					if(result == null || log.getPunchTime().after(result.getPunchTime()))
						result = log;
				}
			}
		}
		return result;
	}
	private Long timeIsNull(Long value, Long defaultValue) {
		 return value==null?defaultValue:value;
	}
	@Override
	public ListPunchMonthStatusResponse listPunchMonthStatus(ListPunchMonthStatusCommand cmd) {
		// TODO Auto-generated method sub
        cmd.setEnterpriseId(getTopEnterpriseId(cmd.getEnterpriseId()));
        ListPunchMonthStatusResponse response = new ListPunchMonthStatusResponse();
        response.setDayStatus(new ArrayList<>());
        response.setMonthDate(cmd.getQueryTime());
        Long userId = UserContext.current().getUser().getId();
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTimeInMillis(cmd.getQueryTime());
        startCalendar.set(Calendar.DAY_OF_MONTH,1);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(startCalendar.getTime());
        endCalendar.add(Calendar.MONTH,1);
        List<PunchDayLog> dayLogList = this.punchProvider.listPunchDayLogsExcludeEndDay(userId,
                cmd.getEnterpriseId(), dateSF.get().format(startCalendar.getTime()),
                dateSF.get().format(endCalendar.getTime()) );
        if (null != dayLogList) {
            for (PunchDayLog log : dayLogList) {
                MonthDayStatusDTO dto = ConvertHelper.convert(log, MonthDayStatusDTO.class);
                response.getDayStatus().add(dto);
                if (null == log.getStatusList()) {
                    dto.setExceptionStatus(log.getStatus().equals(PunchStatus.NORMAL.getCode())?
                            ExceptionStatus.NORMAL.getCode():ExceptionStatus.EXCEPTION.getCode());
                }else {
                    String[] status = log.getStatusList().split(PunchConstants.STATUS_SEPARATOR);
                    dto.setExceptionStatus(ExceptionStatus.NORMAL.getCode());
                    if(status ==null )
                        continue;
                    else{
                        for (String s1 : status) {
                            if (!s1.equals(String.valueOf(PunchStatus.NORMAL.getCode()))) {
                                dto.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
                            }
                        }
                    }
                }

            }
        }
        return response;
    }
	@Override
	public HttpServletResponse getPunchQRCode(GetPunchQRCodeCommand cmd,
			HttpServletResponse response) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("qrtype", String.valueOf(cmd.getCodeType()));
		map.put("token", cmd.getQrToken().trim());
		String result = localeTemplateService.getLocaleTemplateString(PunchConstants.PUNCH_TOOL_URI_SCOPE,
				PunchConstants.PUNCH_TOOL_URI_CODE, RentalNotificationTemplateCode.locale, map, "");
        String key = cmd.getQrToken().trim();
        ValueOperations<String, String> valueOperations = getValueOperations(key);
        int timeout = configurationProvider.getIntValue(PunchConstants.PUNCH_QRCODE_TIMEOUT, 15);
        TimeUnit unit = TimeUnit.MINUTES;;
        // 先放一个和key一样的值,表示这个人key有效
        valueOperations.set(key, key, timeout, unit);
		try{
			ByteArrayOutputStream out = QRCodeEncoder.createSimpleQrCode(Base64.getEncoder().encodeToString(result.getBytes()));
			return downloadPng(out,response);
		} catch (WriterException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return response;
	}
	public HttpServletResponse downloadPng(ByteArrayOutputStream out, HttpServletResponse response) {
		try {
			response.reset();
			// 设置response的Header
//			response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes()));
			OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("image/x-png");
			toClient.write(out.toByteArray());
			toClient.flush();
			toClient.close();

		} catch (IOException ex) {
			LOGGER.error(ex.getMessage());
			throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
					PunchServiceErrorCode.ERROR_PUNCH_ADD_DAYLOG,
					ex.getLocalizedMessage());

		}
		return response;
	}

	@Override
	public void addPunchPoints(AddPunchPointsCommand cmd) {
        RestResponse restResponse = new RestResponse(cmd.getPunchGeoPoints());
        busMessage(restResponse, cmd.getToken().trim());
    }

    private void busMessage(RestResponse restResponse, String key) {
        ValueOperations<String, String> valueOperations = getValueOperations(key);
        String value = valueOperations.get(key);
        if(null == value){
            //找不到key,说明过期了 报错
            throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
                    PunchServiceErrorCode.ERROR_PUNCH_TOKEN_TIMEOUT,
                    "token time out ");
        }else{
            submitLocalBus(PunchConstants.PUNCH_QRCODE_SUBJECT + key, restResponse);
        }
    }

    @Override
	public void addPunchWifis(AddPunchWifisCommand cmd) {
        RestResponse restResponse = new RestResponse(cmd.getWifis());
        busMessage(restResponse, cmd.getToken().trim());
	}
	@Override
	public DeferredResult<RestResponse> getPunchQRCodeResult(GetPunchQRCodeCommand cmd) {
		// TODO Auto-generated method stub
        DeferredResult<RestResponse> deferredResult = new DeferredResult<>();
        RestResponse response =  new RestResponse();
        String key = cmd.getQrToken().trim();
        ValueOperations<String, String> valueOperations = getValueOperations(key);
        String value = valueOperations.get(key);
        if(null == value){
            //找不到key,说明过期了 报错
            throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
                    PunchServiceErrorCode.ERROR_PUNCH_TOKEN_TIMEOUT,
                    "token time out ");
        }
        //如果value 和key 不一样,说明value被放入了前端给的数据,直接返回结果
        if(null!=value && value != key){
            deferredResult.setResult(JSONObject.parseObject(value,RestResponse.class));
            int timeout = configurationProvider.getIntValue(PunchConstants.PUNCH_QRCODE_TIMEOUT, 15);
            TimeUnit unit = TimeUnit.MINUTES;;
            // 先放一个和key一样的值,表示这个key有效
            valueOperations.set(key, key, timeout, unit);
            return deferredResult;
        }

        String subject = PunchConstants.PUNCH_QRCODE_SUBJECT;
        int scanTimeout = configurationProvider.getIntValue(PrintErrorCode.PRINT_LOGON_SCAN_TIMOUT, 10000);
        localBusSubscriberBuilder.build(subject + "." + key, new LocalBusOneshotSubscriber() {
            @Override
            public Action onLocalBusMessage(Object sender, String subject,
                                            Object restResponse, String path) {
                //这里要不要重新把timeou的时间刷一刷?
                deferredResult.setResult((RestResponse)restResponse);
                return null;
            }
            @Override
            public void onLocalBusListeningTimeout() {
                response.setResponseObject("timed out");
                response.setErrorCode(408);
                deferredResult.setResult(response);
            }

        }).setTimeout(scanTimeout).create();

        return deferredResult;
	}
    //给每台机器发通知
    private void submitLocalBus(String localKey,RestResponse restResponse){
        ExecutorUtil.submit(new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    LocalBusSubscriber localBusSubscriber = (LocalBusSubscriber) busBridgeProvider;
                    localBusSubscriber.onLocalBusMessage(null, localKey, JSONObject.toJSONString(restResponse), null);
                } catch (Exception e) {
                    LOGGER.error("submit LocalBusSubscriber {} got excetion", localKey , e);
                }
                localBus.publish(null, localKey, JSONObject.toJSONString(restResponse));
            }
        },"subscriberPrint"));
    }
	/**
	 * 获取key在redis操作的valueOperations
	 */
	private ValueOperations<String, String> getValueOperations(String key) {
		final StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
		Accessor acc = bigCollectionProvider.getMapAccessor(key, "");
		RedisTemplate redisTemplate = acc.getTemplate(stringRedisSerializer);
		ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

		return valueOperations;
	}


	/**
	 * 清除redis中key的缓存
	 */
	private void deleteValueOperations(String key) {
		final StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
		Accessor acc = bigCollectionProvider.getMapAccessor(key, "");
		RedisTemplate redisTemplate = acc.getTemplate(stringRedisSerializer);
		redisTemplate.delete(key);
	}

}