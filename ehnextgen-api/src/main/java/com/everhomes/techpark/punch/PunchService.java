package com.everhomes.techpark.punch;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.everhomes.rest.organization.pm.CommunityPropFamilyMemberCommand;
import com.everhomes.rest.techpark.punch.AddPunchExceptionRequestCommand;
import com.everhomes.rest.techpark.punch.AddPunchRuleCommand;
import com.everhomes.rest.techpark.punch.ApprovalPunchExceptionCommand;
import com.everhomes.rest.techpark.punch.DeletePunchRuleCommand;
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
import com.everhomes.rest.techpark.punch.PunchLogsDay;
import com.everhomes.rest.techpark.punch.UpdatePunchRuleCommand;

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

	public HttpServletResponse exportPunchStatistics(
			ExportPunchStatisticsCommand cmd,
			 
			  HttpServletResponse response );
}
