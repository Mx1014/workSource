package com.everhomes.techpark.punch;

import java.sql.Time;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.everhomes.organization.Organization;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.techpark.punch.*;

import com.everhomes.rest.techpark.punch.admin.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;

import com.everhomes.approval.ApprovalRule;

public interface PunchService {
	 
	public ListYearPunchLogsCommandResponse getlistPunchLogs(ListYearPunchLogsCommand cmd);

	public PunchClockResponse createPunchLog(PunchClockCommand cmd);
	
//	public void createPunchRule(AddPunchRuleCommand cmd);
//	public void updatePunchRule(UpdatePunchRuleCommand cmd);
//	public void deletePunchRule(DeletePunchRuleCommand cmd);
//	public GetPunchRuleCommandResponse getPunchRuleByCompanyId(GetPunchRuleCommand cmd);
 
	ListYearPunchLogsCommandResponse getlistPunchLogsBetweenTwoCalendar(
			ListYearPunchLogsCommandResponse pyl, long CompanyId, Calendar start,
			Calendar end);

//	public void createPunchExceptionRequest(AddPunchExceptionRequestCommand cmd);
	public ListPunchExceptionRequestCommandResponse listExceptionRequests(ListPunchExceptionRequestCommand cmd);
//	void approvePropFamilyMember(PunchExceptionRequest cmd);
//	void rejectPropFamilyMember(PunchExceptionRequest cmd);

	public ListPunchExceptionRequestCommandResponse listExceptionApprovals(
			ListPunchExceptionApprovalCommand cmd);

	public void punchExceptionApproval(ApprovalPunchExceptionCommand cmd);
 
	
	public ListPunchStatisticsCommandResponse listPunchStatistics(ListPunchStatisticsCommand cmd);

	public GetPunchNewExceptionCommandResponse getPunchNewException( GetPunchNewExceptionCommand cmd);

	public PunchLogsDay makePunchLogsDayListInfo(Long userId, Long companyId,
			Calendar logDay) throws ParseException;

	public PunchLogsDay getDayPunchLogs(GetDayPunchLogsCommand cmd);

	public ListPunchCountCommandResponse listPunchCount(ListPunchCountCommand cmd);

	public ListMonthPunchLogsCommandResponse listMonthPunchLogs(
			ListMonthPunchLogsCommand cmd);

	List<Long> listDptUserIds(Organization org, Long ownerId, String userName, Byte includeSubDpt);

	public HttpServletResponse exportPunchDetails(
			ListPunchDetailsCommand cmd,
			 
			  HttpServletResponse response );

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

	public ListPunchDetailsResponse listPunchDetails(ListPunchDetailsCommand cmd);

	public HttpServletResponse exportPunchStatistics(ListPunchCountCommand cmd, HttpServletResponse response);

	void dayRefreshLogScheduled();

	void testDayRefreshLogs(Long runDate);

	public void deletePunchRuleMap(DeletePunchRuleMapCommand cmd);

	void refreshMonthDayLogs(String month);

	ApprovalRule getApprovalRule(String ownerType, Long ownerId, Long userId);

	PunchRule getPunchRule(String ownerType, Long ownerId, Long userId);


//	PunchTimeRule getPunchTimeRule(PunchRule punchRule);

//	boolean isWorkTime(Time time, PunchRule punchRule);
 

	boolean isSameDay(Date date1, Date date2);

	Time getEndTime(Time startTime, Time workTime);

	public GetTargetPunchAllRuleResponse getTargetPunchAllRule(GetTargetPunchAllRuleCommand cmd);

	public void updateTargetPunchAllRule(UpdateTargetPunchAllRuleCommand cmd);

	public void deleteTargetPunchAllRule(GetTargetPunchAllRuleCommand cmd);

	String statusToString(Byte status);
 

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
 

	Long convertTimeToGMTMillisecond(Time time);

	public CheckPunchAdminResponse checkPunchAdmin(CheckPunchAdminCommand cmd);

	Long getTopEnterpriseId(Long organizationId);

	void importPunchLogs(MultipartFile[] files);

	void refreshPunchDayLogs(ListPunchDetailsCommand cmd);

	//  punch 2.8 added by R 20170725
	ListPunchSupportiveAddressCommandResponse listPunchSupportiveAddress(ListPunchSupportiveAddressCommand cmd);

	public PunchGroupDTO addPunchGroup(AddPunchGroupCommand cmd);

	public ListPunchGroupsResponse listPunchGroups(ListPunchGroupsCommand cmd);

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

	HttpServletResponse exportPunchSchedulingTemplate(ListPunchSchedulingMonthCommand cmd, HttpServletResponse response);

	void invalidPunchQRCode(GetPunchQRCodeCommand cmd);
}
