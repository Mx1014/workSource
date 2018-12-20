package com.everhomes.techpark.punch;

import com.everhomes.approval.ApprovalRule;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationMemberDetails;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.approval.ApprovalCategoryTimeSelectType;
import com.everhomes.rest.openapi.GetOrgCheckInDataCommand;
import com.everhomes.rest.openapi.GetOrgCheckInDataResponse;
import com.everhomes.rest.techpark.punch.*;
import com.everhomes.rest.techpark.punch.admin.AddPunchGroupCommand;
import com.everhomes.rest.techpark.punch.admin.AddPunchPointCommand;
import com.everhomes.rest.techpark.punch.admin.AddPunchTimeRuleCommand;
import com.everhomes.rest.techpark.punch.admin.AddPunchWiFiCommand;
import com.everhomes.rest.techpark.punch.admin.DeleteCommonCommand;
import com.everhomes.rest.techpark.punch.admin.DeletePunchRuleMapCommand;
import com.everhomes.rest.techpark.punch.admin.GetPunchGroupCommand;
import com.everhomes.rest.techpark.punch.admin.GetPunchGroupsCountCommand;
import com.everhomes.rest.techpark.punch.admin.GetPunchGroupsCountResponse;
import com.everhomes.rest.techpark.punch.admin.GetTargetPunchAllRuleCommand;
import com.everhomes.rest.techpark.punch.admin.GetUserPunchRuleInfoCommand;
import com.everhomes.rest.techpark.punch.admin.GetUserPunchRuleInfoResponse;
import com.everhomes.rest.techpark.punch.admin.ListAllSimplePunchGroupsResponse;
import com.everhomes.rest.techpark.punch.admin.ListApprovalCategoriesResponse;
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
import com.everhomes.rest.techpark.punch.admin.PunchGroupDTO;
import com.everhomes.rest.techpark.punch.admin.PunchLocationRuleDTO;
import com.everhomes.rest.techpark.punch.admin.PunchSchedulingDTO;
import com.everhomes.rest.techpark.punch.admin.PunchSchedulingEmployeeDTO;
import com.everhomes.rest.techpark.punch.admin.PunchWiFiDTO;
import com.everhomes.rest.techpark.punch.admin.PunchWiFiRuleDTO;
import com.everhomes.rest.techpark.punch.admin.PunchWorkdayRuleDTO;
import com.everhomes.rest.techpark.punch.admin.QryPunchLocationRuleListResponse;
import com.everhomes.rest.techpark.punch.admin.UpdatePunchPointCommand;
import com.everhomes.rest.techpark.punch.admin.UpdatePunchSchedulingMonthCommand;
import com.everhomes.rest.techpark.punch.admin.UpdatePunchTimeRuleCommand;
import com.everhomes.rest.techpark.punch.admin.UpdateTargetPunchAllRuleCommand;
import com.everhomes.rest.techpark.punch.admin.listPunchTimeRuleListResponse;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.OutputStream;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public interface PunchService {
	public ListYearPunchLogsCommandResponse getlistPunchLogs(ListYearPunchLogsCommand cmd);

	public PunchClockResponse createPunchLog(PunchClockCommand cmd);

	ListYearPunchLogsCommandResponse getlistPunchLogsBetweenTwoCalendar(
			ListYearPunchLogsCommandResponse pyl, long CompanyId, Calendar start,
			Calendar end);

	public ListPunchExceptionRequestCommandResponse listExceptionRequests(ListPunchExceptionRequestCommand cmd);

	public ListPunchExceptionRequestCommandResponse listExceptionApprovals(ListPunchExceptionApprovalCommand cmd);

	public void punchExceptionApproval(ApprovalPunchExceptionCommand cmd);


	public ListPunchStatisticsCommandResponse listPunchStatistics(ListPunchStatisticsCommand cmd);

	public GetPunchNewExceptionCommandResponse getPunchNewException(GetPunchNewExceptionCommand cmd);

	PunchDayLog refreshPunchDayLog(OrganizationMemberDetails memberDetail, Calendar logDay) throws ParseException;

	public PunchLogsDay makePunchLogsDayListInfo(Long userId, Long companyId, Calendar logDay) throws ParseException;

	public PunchLogsDay getDayPunchLogs(GetDayPunchLogsCommand cmd);

	public ListPunchCountCommandResponse listPunchCount(ListPunchCountCommand cmd);

	public ListMonthPunchLogsCommandResponse listMonthPunchLogs(ListMonthPunchLogsCommand cmd);

	List<Long> listDptUserIds(Organization org, Long ownerId, String userName, Byte includeSubDpt);

	public HttpServletResponse exportPunchDetails(ListPunchDetailsCommand cmd, HttpServletResponse response);

	public void addPunchTimeRule(AddPunchTimeRuleCommand cmd);

	public void updatePunchTimeRule(UpdatePunchTimeRuleCommand cmd);

	public void deletePunchTimeRule(DeleteCommonCommand cmd);

	public listPunchTimeRuleListResponse listPunchTimeRuleList(ListPunchRulesCommonCommand cmd);

	public void addPunchLocationRule(PunchLocationRuleDTO cmd);

	public void updatePunchLocationRule(PunchLocationRuleDTO cmd);

	public void deletePunchLocationRule(DeleteCommonCommand cmd);

	public QryPunchLocationRuleListResponse listPunchLocationRules(ListPunchRulesCommonCommand cmd);

	public void addPunchWiFiRule(PunchWiFiRuleDTO cmd);

	public void updatePunchWiFiRule(PunchWiFiRuleDTO cmd);

	public void deletePunchWiFiRule(DeleteCommonCommand cmd);

	public ListPunchWiFiRuleListResponse listPunchWiFiRule(ListPunchRulesCommonCommand cmd);

	public void addPunchWorkdayRule(PunchWorkdayRuleDTO cmd);

	public void updatePunchWorkdayRule(PunchWorkdayRuleDTO cmd);

	public void deletePunchWorkdayRule(DeleteCommonCommand cmd);

	public ListPunchWorkdayRuleListResponse listPunchWorkdayRule(ListPunchRulesCommonCommand cmd);

	public void addPunchRule(PunchRuleDTO cmd);

	public void updatePunchRule(PunchRuleDTO cmd);

	public void deletePunchRule(DeleteCommonCommand cmd);

	public ListPunchRulesResponse listPunchRules(ListPunchRulesCommonCommand cmd);

	public void addPunchRuleMap(PunchRuleMapDTO cmd);

	public ListPunchRuleMapsResponse listPunchRuleMaps(ListPunchRuleMapsCommand cmd);

	public ListPunchMonthLogsResponse listPunchMonthLogs(ListPunchMonthLogsCommand cmd);

	void refreshPunchDayLog(Long userId, Long companyId, Long startDay, Long endDay);

	void refreshPunchDayLog(Long userId, Long companyId, Date startDay, Date endDay);

	public ListPunchDetailsResponse listPunchDetails(ListPunchDetailsCommand cmd);

	public HttpServletResponse exportPunchStatistics(ListPunchCountCommand cmd, HttpServletResponse response);

	@Scheduled(cron = "1 0 5 * * ?")
	void dayRefreshPunchGroupScheduled();

	@Deprecated
	void dayRefreshLogScheduled();

	void punchDayLogInitialize();

	void punchDayLogInitialize(PunchDayLogInitializeCommand cmd);

	void testDayRefreshLogs(Long runDate, Long orgId);

	public void deletePunchRuleMap(DeletePunchRuleMapCommand cmd);

	void refreshMonthDayLogs(String month);

	ApprovalRule getApprovalRule(String ownerType, Long ownerId, Long userId);

	PunchRule getPunchRule(String ownerType, Long ownerId, Long userId);

	PunchRule getPunchRule(OrganizationMemberDetails memberDetail);

	boolean isSameDay(Date date1, Date date2);

	Time getEndTime(Time startTime, Time workTime);

	public void updateTargetPunchAllRule(UpdateTargetPunchAllRuleCommand cmd);

	public void deleteTargetPunchAllRule(GetTargetPunchAllRuleCommand cmd);

	List<String> getTimeIntervalApprovalAttribute();

	String statusToString(Timestamp splitDate, Byte status);
 

	public void updatePunchPoint(UpdatePunchPointCommand cmd);

	public void addPunchPoint(AddPunchPointCommand cmd);

	public ListPunchPointsResponse listPunchPoints(ListPunchPointsCommand cmd);

	public void addPunchWiFi(AddPunchWiFiCommand cmd);

	public void updatePunchWiFi(PunchWiFiDTO cmd);

	public void deletePunchWiFi(PunchWiFiDTO cmd);

	public ListPunchSchedulingMonthResponse listPunchScheduling(ListPunchSchedulingMonthCommand cmd);
 

	public HttpServletResponse exportPunchScheduling(ListPunchSchedulingMonthCommand cmd,
			HttpServletResponse response);

	public PunchSchedulingDTO importPunchScheduling(MultipartFile[] files);

	public void updatePunchRuleMap(PunchRuleMapDTO cmd);

	public void deletePunchPoint(DeleteCommonCommand cmd);

	public void updatePunchSchedulings(UpdatePunchSchedulingMonthCommand cmd);

	public ListPunchWiFisResponse listPunchWiFis(ListPunchRulesCommonCommand cmd);


	java.sql.Date calculatePunchDate(Calendar punCalendar, Long enterpriseId, Long userId);

	Long convertTimeToGMTMillisecond(Time time);

	public CheckPunchAdminResponse checkPunchAdmin(CheckPunchAdminCommand cmd);

	OrganizationMember findOrganizationMemberByOrgIdAndUId(Long targetId, String path);

	Long getTopEnterpriseId(Long organizationId);

	void importPunchLogs(MultipartFile[] files);

	void refreshPunchDayLogs(ListPunchDetailsCommand cmd);

	//  punch 2.8 added by R 20170725
	ListPunchSupportiveAddressCommandResponse listPunchSupportiveAddress(ListPunchSupportiveAddressCommand cmd);

	public PunchGroupDTO addPunchGroup(AddPunchGroupCommand cmd);

	public ListPunchGroupsResponse listPunchGroups(ListPunchGroupsCommand cmd);

	ListApprovalCategoriesResponse listApprovalCategories(ListApprovalCategoriesCommand cmd, HttpServletRequest request);

	public PunchGroupDTO updatePunchGroup(PunchGroupDTO cmd);

	public void deletePunchGroup(DeleteCommonCommand cmd);

	public GetPunchDayStatusResponse getPunchDayStatus(GetPunchDayStatusCommand cmd);

	public PunchGroupDTO getPunchGroup(GetPunchGroupCommand cmd); 

	PunchTimeRule getPunchTimeRuleByRuleIdAndDate(PunchRule pr, Date date, Long userId);

	boolean isRestTime(Date fromTime, Date endTime, PunchRule punchRule, Long userId);

	boolean isWorkTime(Time time, PunchRule punchRule, Date date, Long userId);

	boolean isWorkDay(Date date1, PunchRule punchRule, Long userId);

	public ListPunchMonthStatusResponse listPunchMonthStatus(ListPunchMonthStatusCommand cmd);

	public String getPunchQRCode(GetPunchQRCodeCommand cmd,
			HttpServletResponse response);

	public void addPunchPoints(AddPunchPointsCommand cmd);
 
	public void addPunchWifis(AddPunchWifisCommand cmd);

	public DeferredResult<RestResponse> getPunchQRCodeResult(GetPunchQRCodeCommand cmd);

	OutputStream getPunchSchedulingOutputStream(Long queryTime,
									   List<PunchSchedulingEmployeeDTO> employees, List<PunchTimeRuleDTO> timeRules, Long taskId);

	HttpServletResponse exportPunchSchedulingTemplate(ListPunchSchedulingMonthCommand cmd, HttpServletResponse response);

	void invalidPunchQRCode(GetPunchQRCodeCommand cmd);

	public CheckAbnormalStatusResponse checkAbnormalStatus(CheckPunchAdminCommand cmd);

	GetPunchGroupsCountResponse getPunchGroupsCount(GetPunchGroupsCountCommand cmd);

	void punchGroupAddNewEmployee(Long groupId);

	ListPunchLogsResponse listPunchLogs(ListPunchLogsCommand cmd);

	void checkAppPrivilege(Long orgId, Long checkOrgId, Long privilege);

	OutputStream getPunchDetailsOutputStream(Long startDay, Long endDay, Byte exceptionStatus,
			String userName, String ownerType, Long ownerId, Long taskId, Long userId);

	OutputStream getPunchStatisticsOutputStream(Long startDay, Long endDay, Byte exceptionStatus,
												String userName, String ownerType, Long ownerId, List<Long> departmentIds, Long taskId, Long monthReportId);

	ArrayList processImportExcel2ArrayList(MultipartFile[] files);
	
	void addPunchLogShouldPunchTime(AddPunchLogShouldPunchTimeCommand cmd);

	void refreshMonthReport(String month);

	public ListPunchMonthReportsResponse listPunchMonthReports(ListPunchMonthReportsCommand cmd);


	public void updateMonthReport(UpdateMonthReportCommand cmd);


	public GetMonthReportProcessResponse getMonthReportProcess(GetMonthReportProcessCommand cmd);


	public void fileMonthReport(FileMonthReportCommand cmd);

	GetOrgCheckInDataResponse getOrgCheckInData(GetOrgCheckInDataCommand cmd);

	ListOrganizationPunchLogsResponse listOrganizationPunchLogs(ListOrganizationPunchLogsCommand cmd);

	Date parseDateTimeByTimeSelectType(Long organizationId, Long userId, String day, ApprovalCategoryTimeSelectType type);

	GetOvertimeInfoResponse getOvertimeInfo(GetOvertimeInfoCommand cmd);

	ListPunchStatusMembersResponse listMembersOfAPunchStatus(ListPunchStatusMembersCommand cmd);

	ListPunchMembersResponse listMembersOfDepartment(ListPunchMembersCommand cmd);

	ListPunchExceptionRequestMembersResponse listMembersOfAPunchExceptionRequest(ListPunchExceptionRequestMembersCommand cmd);

	PunchMonthlyStatisticsByMemberResponse monthlyStatisticsByMember(PunchMonthlyStatisticsByMemberCommand cmd);

	PunchMonthlyStatisticsByDepartmentResponse monthlyStatisticsByDepartment(PunchMonthlyStatisticsByDepartmentCommand cmd);

	PunchDailyStatisticsByDepartmentResponse dailyStatisticsByDepartment(PunchDailyStatisticsByDepartmentCommand cmd);

	ListPunchStatusItemDetailResponse listItemDetailsOfAPunchStatus(ListPunchStatusItemDetailCommand cmd);

	ListPunchExceptionRequestItemDetailResponse listItemDetailsOfAPunchExceptionRequest(ListPunchExceptionRequestItemDetailCommand cmd);

	GetUserPunchRuleInfoResponse getUserPunchRuleInfo(GetUserPunchRuleInfoCommand cmd);

	CheckUserStatisticPrivilegeResponse checkUserStatisticPrivilege(CheckUserStatisticPrivilegeCommand cmd);
	
	String processUserPunchRuleInfoUrl(Long ownerId, Long punchDate, HttpServletRequest request);
	
	GetUserPunchRuleInfoUrlResponse getUserPunchRuleInfoUrl(GetUserPunchRuleInfoUrlCommand cmd, HttpServletRequest request);

	String getAdjustRuleUrl(HttpServletRequest request);

	/**
	 * 用于上线时进行手动初始化操作
	 */
	void punchDayLogInitializeByMonth(String initMonth) throws ParseException;

	PunchClockResponse thirdPartPunchClock(ThirdPartPunchClockCommand cmd);

	GoOutPunchLogDTO goOutPunchClock(GoOutPunchClockCommand cmd);

	GoOutPunchLogDTO updateGoOutPunchLog(UpdateGoOutPunchLogCommand cmd);

	GoOutPunchLogDTO getGoOutPunchLog(GetGoOutPunchLogCommand cmd);

	ListAllSimplePunchGroupsResponse listAllSimplePunchGroups(ListPunchGroupsCommand cmd);
 
}
