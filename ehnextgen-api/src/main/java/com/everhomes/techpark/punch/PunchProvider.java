package com.everhomes.techpark.punch;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import com.everhomes.rest.techpark.punch.DateStatus;
import com.everhomes.rest.techpark.punch.PunchDayLogDTO;
import com.everhomes.rest.techpark.punch.UserPunchStatusCount;
 

public interface PunchProvider {

	public List<PunchLog> listPunchLogsByDate(Long userId, Long companyId, String queryDate,byte clockCode);

	public PunchRule getPunchRuleByCompanyId(Long companyId);

	public List<Date> listPunchLogsBwteenTwoDay(Long userId, Long companyId, String beginDate, String endDate);

	public void createPunchLog(PunchLog punchLog);

	public List<PunchGeopoint> listPunchGeopointsByCompanyId(Long companyId);


	public void createPunchRule(PunchRule punchRule);
	public void updatePunchRule(PunchRule punchRule);
	public void deletePunchRule(PunchRule punchRule);
	public void deletePunchRuleById(Long id);
	public PunchRule findPunchRuleById(Long id);
	public PunchRule findPunchRuleByCompanyId(Long companyId);
	
	public void createPunchGeopoint(PunchGeopoint punchGeopoint);
	public void updatePunchGeopoint(PunchGeopoint punchGeopoint);
	public void deletePunchGeopoint(PunchGeopoint punchGeopoint);
	public void deletePunchGeopointById(Long id);
	public PunchGeopoint findPunchGeopointById(Long id);

	void createPunchWorkday(PunchWorkday punchWorkday);

	List<PunchWorkday> listWorkdays(DateStatus dateStatus);

	public void createPunchExceptionRequest(
			PunchExceptionRequest punchExceptionRequest);

	public List<PunchExceptionRequest> listExceptionRequestsByDate(Long userId,
			Long companyId, String logDay);

	public PunchExceptionApproval getPunchExceptionApprovalByDate(Long userId,
			Long companyId, String logDay);
	
	public Integer countExceptionRequests(Long userId, List<Long> userIds, Long companyId, String startDay,String endDay,
			Byte status, Byte processCode,Byte requestType);
	public List<PunchExceptionRequest> listExceptionRequests(Long userId,List<Long> userIds, Long companyId, String startDay,String endDay,
			Byte status, Byte processCode,Integer pageOffset,Integer pageSize,Byte requestType);

	public int countExceptionRequests(List<Long> userIds, Long companyId,
			String startDay, String endDay, Byte status,
			Byte processCode, Byte requestType);

	public List<PunchExceptionRequest> listExceptionRequests(List<Long> userIds,
			Long companyId, String startDay, String endDay,
			Byte status, Byte processCode, Integer pageOffset,
			int pageSize,  Byte requestType);

	public PunchExceptionApproval getExceptionApproval(Long userId, Long companyId,
			java.sql.Date punchDate);

	public void createPunchExceptionApproval(
			com.everhomes.techpark.punch.PunchExceptionApproval punchExceptionApproval);

	public void updatePunchExceptionApproval(
			PunchExceptionApproval punchExceptionApproval);

	public void deletePunchExceptionApproval(Long id);

	public void updatePunchExceptionRequest(PunchExceptionRequest punchExceptionRequest);

	public PunchDayLog getDayPunchLogByDate(Long userId, Long companyId,
			String format);

	public void createPunchDayLog(PunchDayLog punchDayLog);

	public void updatePunchDayLog(PunchDayLog punchDayLog);
	
	public int countPunchDayLogs(List<Long> userIds, Long companyId,
			String startDay, String endDay, Byte startTimeCompareFlag,
			String startTime, Byte endTimeCompareFlag, String endTime,
			Byte workTimeCompareFlag, String workTime, Byte status);

	public List<PunchDayLog> listPunchDayLogs(List<Long> userIds, Long companyId,
			String startDay, String endDay, Byte status,
			Byte arriveTimeCompareFlag, String arriveTime,
			Byte leaveTimeCompareFlag, String leaveTime,
			Byte workTimeCompareFlag, String workTime, Integer pageOffset,
			Integer pageSize);

	public List<PunchDayLog> listPunchDayExceptionLogs(Long userId,
			Long companyId, String startDay, String endDay);

	public List<PunchExceptionRequest> listExceptionNotViewRequests(
			Long userId, Long companyId, String startDay, String endDay);

	public void viewDateFlags(Long userId, Long companyId, String format);

	public List<UserPunchStatusCount> listUserStatusPunch(Long companyId, String startDay,String endDay);
	public List<UserPunchStatusCount> listUserApprovalStatusPunch(Long companyId, String startDay,String endDay);
 
	public List<PunchDayLogDTO> listPunchDayLogs(Long companyId, String startDay, String endDay);
 
}
