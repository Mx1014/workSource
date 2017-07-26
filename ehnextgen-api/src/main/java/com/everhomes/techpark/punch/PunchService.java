package com.everhomes.techpark.punch;

import java.sql.Time;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.everhomes.organization.Organization;
import org.springframework.web.multipart.MultipartFile;

import com.everhomes.approval.ApprovalRule;
import com.everhomes.rest.techpark.punch.AddPunchExceptionRequestCommand;
import com.everhomes.rest.techpark.punch.AddPunchRuleCommand;
import com.everhomes.rest.techpark.punch.ApprovalPunchExceptionCommand;
import com.everhomes.rest.techpark.punch.CheckPunchAdminCommand;
import com.everhomes.rest.techpark.punch.CheckPunchAdminResponse;
import com.everhomes.rest.techpark.punch.DeletePunchRuleCommand;
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
import com.everhomes.rest.techpark.punch.PunchLogsDay;
import com.everhomes.rest.techpark.punch.PunchRuleDTO;
import com.everhomes.rest.techpark.punch.PunchRuleMapDTO;
import com.everhomes.rest.techpark.punch.UpdatePunchRuleCommand;
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
import com.everhomes.rest.techpark.punch.admin.PunchLocationRuleDTO;
import com.everhomes.rest.techpark.punch.admin.PunchWiFiDTO;
import com.everhomes.rest.techpark.punch.admin.PunchWiFiRuleDTO;
import com.everhomes.rest.techpark.punch.admin.PunchWorkdayRuleDTO;
import com.everhomes.rest.techpark.punch.admin.QryPunchLocationRuleListResponse;
import com.everhomes.rest.techpark.punch.admin.UpdatePunchPointCommand;
import com.everhomes.rest.techpark.punch.admin.UpdatePunchSchedulingMonthCommand;
import com.everhomes.rest.techpark.punch.admin.UpdatePunchTimeRuleCommand;
import com.everhomes.rest.techpark.punch.admin.UpdateTargetPunchAllRuleCommand;
import com.everhomes.rest.techpark.punch.admin.listPunchTimeRuleListResponse;

public interface PunchService {
	 
	public ListYearPunchLogsCommandResponse getlistPunchLogs(ListYearPunchLogsCommand cmd);

	public PunchClockResponse createPunchLog(PunchClockCommand cmd);
	
	public void createPunchRule(AddPunchRuleCommand cmd);
	public void updatePunchRule(UpdatePunchRuleCommand cmd);
	public void deletePunchRule(DeletePunchRuleCommand cmd);
	public GetPunchRuleCommandResponse getPunchRuleByCompanyId(GetPunchRuleCommand cmd);
 
	ListYearPunchLogsCommandResponse getlistPunchLogsBetweenTwoCalendar(
			ListYearPunchLogsCommandResponse pyl, long CompanyId, Calendar start,
			Calendar end);

	public void createPunchExceptionRequest(AddPunchExceptionRequestCommand cmd);
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

	boolean isWorkDay(Date date1, PunchRule punchRule);

//	PunchTimeRule getPunchTimeRule(PunchRule punchRule);

//	boolean isWorkTime(Time time, PunchRule punchRule);

	boolean isRestTime(Date fromTime, Date endTime, PunchRule punchRule);

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

	public void importPunchScheduling(ListPunchRulesCommonCommand cmd, MultipartFile[] files);

	public void updatePunchRuleMap(PunchRuleMapDTO cmd);

	public void deletePunchPoint(DeleteCommonCommand cmd);

	public void updatePunchSchedulings(UpdatePunchSchedulingMonthCommand cmd);

	public ListPunchWiFisResponse listPunchWiFis(ListPunchRulesCommonCommand cmd);

	PunchTimeRule getPunchTimeRuleByRuleIdAndDate(Long ruleId, Date date);

	boolean isWorkTime(Time time, PunchRule punchRule, Date date);

	Long convertTimeToGMTMillisecond(Time time);

	public CheckPunchAdminResponse checkPunchAdmin(CheckPunchAdminCommand cmd);

	Long getTopEnterpriseId(Long organizationId);

	void importPunchLogs(MultipartFile[] files);

	void refreshPunchDayLogs(ListPunchDetailsCommand cmd);

}
