package com.everhomes.techpark.punch;

import java.sql.Date;
import java.sql.Time;
import java.util.Calendar;
import java.util.List;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.techpark.punch.DateStatus;
import com.everhomes.rest.techpark.punch.PunchDayLogDTO;
import com.everhomes.rest.techpark.punch.UserPunchStatusCount;
 

public interface PunchProvider {

	public List<PunchLog> listPunchLogsByDate(Long userId, Long companyId, String queryDate,byte clockCode);

	public PunchTimeRule getPunchTimeRuleByCompanyId(String ownerType ,Long companyId);

	public List<Date> listPunchLogsBwteenTwoDay(Long userId, Long companyId, String beginDate, String endDate);

	public void createPunchLog(PunchLog punchLog);

	public List<PunchGeopoint> listPunchGeopointsByCompanyId(Long companyId);


	public void createPunchTimeRule(PunchTimeRule punchRule);
	public void updatePunchTimeRule(PunchTimeRule punchRule);
	public void deletePunchTimeRule(PunchTimeRule punchRule);
	public void deletePunchTimeRuleById(Long id);
	public PunchTimeRule findPunchTimeRuleById(Long id);
	public PunchTimeRule findPunchTimeRuleByCompanyId(Long companyId);
	
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

	List<PunchTimeRule> queryPunchTimeRules(String ownerType, Long ownerId, String name);

	PunchTimeRule getPunchTimeRuleById(Long id);

	public List<PunchRule> findPunchRules(String ownerType, Long ownerId,Long timeRuleId,Long locationRuleId,Long wifiRuleId,Long workdayRuleId   );

	public List<PunchTimeRule> queryPunchTimeRuleList(String ownerType, Long ownerId, CrossShardListingLocator locator, int i);

	List<PunchLocationRule> queryPunchLocationRules(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback);

	Long createPunchLocationRule(PunchLocationRule obj);

	void updatePunchLocationRule(PunchLocationRule obj);

	void deletePunchLocationRule(PunchLocationRule obj);

	PunchLocationRule getPunchLocationRuleById(Long id);

	void updatePunchWifi(PunchWifi obj);

	void deletePunchWifi(PunchWifi obj);

	PunchWifi getPunchWifiById(Long id);

	List<PunchWifi> queryPunchWifis(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback);

	Long createPunchWifiRule(PunchWifiRule obj);

	void updatePunchWifiRule(PunchWifiRule obj);

	void deletePunchWifiRule(PunchWifiRule obj);

	PunchWifiRule getPunchWifiRuleById(Long id);

	List<PunchWifiRule> queryPunchWifiRules(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback);

	Long createPunchHoliday(PunchHoliday obj);

	void updatePunchHoliday(PunchHoliday obj);

	void deletePunchHoliday(PunchHoliday obj);

	PunchHoliday getPunchHolidayById(Long id);

	List<PunchHoliday> queryPunchHolidays(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback);

	Long createPunchWorkdayRule(PunchWorkdayRule obj);

	void updatePunchWorkdayRule(PunchWorkdayRule obj);

	void deletePunchWorkdayRule(PunchWorkdayRule obj);

	PunchWorkdayRule getPunchWorkdayRuleById(Long id);

	List<PunchWorkdayRule> queryPunchWorkdayRules(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback);

	Long createPunchRuleOwnerMap(PunchRuleOwnerMap obj);

	void updatePunchRuleOwnerMap(PunchRuleOwnerMap obj);

	void deletePunchRuleOwnerMap(PunchRuleOwnerMap obj);

	PunchRuleOwnerMap getPunchRuleOwnerMapByOwnerAndTarget(String ownerType , Long ownerId,String targetType , Long targetId);

	List<PunchRuleOwnerMap> queryPunchRuleOwnerMaps(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback);

	Long createPunchWifi(PunchWifi obj);

	void deletePunchTimeRuleByOwnerAndId(String ownerType, Long ownerId, Long id);

	void deletePunchRuleOwnerMapByOwnerAndId(String ownerType, Long ownerId, Long id);

	void deletePunchWorkdayRuleByOwnerAndId(String ownerType, Long ownerId, Long id);

	void deletePunchHolidayByOwnerAndId(String ownerType, Long ownerId, Long id);

	void deletePunchWifiRuleByOwnerAndId(String ownerType, Long ownerId, Long id);
 

	public List<PunchLocationRule> queryPunchLocationRulesByName(String ownerType, Long ownerId, String name);

	 
	void deletePunchGeopointsByRuleId(  Long ruleId);

	public void deletePunchLocationRuleByOwnerAndId(String ownerType, Long ownerId, Long id);

	public List<PunchLocationRule> queryPunchLocationRuleList(String ownerType, Long ownerId, CrossShardListingLocator locator, int i);

 
	List<PunchGeopoint> listPunchGeopointsByRuleId(String ownerType, Long ownerId, Long ruleId);

	public List<PunchWifiRule> queryPunchWiFiRulesByName(String ownerType, Long ownerId, String name);

	public void deletePunchWifisByRuleId(Long id);

	public List<PunchWifiRule> queryPunchWifiRuleList(String ownerType, Long ownerId, CrossShardListingLocator locator, int i);

	public List<PunchWifi> listPunchWifisByRuleId(String ownerType, Long ownerId, Long id);

	public List<PunchWorkdayRule> queryPunchWorkdayRulesByName(String ownerType, Long ownerId, String name);

	public void deletePunchHolidayByRuleId(Long id);

	public List<PunchWorkdayRule> queryPunchWorkdayRuleList(String ownerType, Long ownerId, CrossShardListingLocator locator, int i);

	public List<PunchHoliday> listPunchHolidaysByRuleId(String ownerType, Long ownerId, Long id);

	public List<PunchRule> queryPunchRulesByName(String ownerType, Long ownerId, String name);

	List<PunchRule> queryPunchRules(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback);

	PunchRule getPunchRuleById(Long id);

	void deletePunchRule(PunchRule obj);

	void updatePunchRule(PunchRule obj);

	Long createPunchRule(PunchRule obj);

	public List<PunchRuleOwnerMap> queryPunchRuleOwnerMapsByRuleId(String ownerType, Long ownerId, Long id);

	public List<PunchRule> queryPunchRuleList(String ownerType, Long ownerId, CrossShardListingLocator locator, int i);
 
	List<PunchRuleOwnerMap> queryPunchRuleOwnerMapList(String ownerType, Long ownerId, String targetType, Long targetId,
			CrossShardListingLocator locator, int i);

	public List<PunchHoliday> queryPunchHolidaysByStatus(String ownerType, Long ownerId, Long workdayRuleId, byte code);

	public List<Long> queryPunchOrganizationsFromRules();

	List<PunchDayLog> listPunchDayLogs(Long userId, Long companyId, String startDay, String endDay);

	List<PunchStatistic> queryPunchStatistics(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback);

	PunchStatistic getPunchStatisticById(Long id);

	void deletePunchStatistic(PunchStatistic obj);

	void updatePunchStatistic(PunchStatistic obj);

	Long createPunchStatistic(PunchStatistic obj);

	public List<PunchStatistic> queryPunchStatistics(String ownerType, Long ownerId, String month, Byte exceptionStatus,
			List<Long> userIds, CrossShardListingLocator locator, int i);
  
	public void deletePunchStatisticByUser(String ownerType, Long ownerId, String punchMonth, Long userId);

	public PunchRuleOwnerMap getPunchRuleOwnerMapById(Long id);

	public List<PunchDayLog> listPunchDayLogs(List<Long> userIds, Long ownerId, String startDay, String endDay,
			Byte arriveTimeCompareFlag, Time arriveTime, Byte leaveTimeCompareFlag, Time leaveTime, Byte workTimeCompareFlag,
			Time workTime, Byte exceptionStatus,Integer pageOffset,Integer pageSize);

	public PunchExceptionRequest findPunchExceptionRequest(Long userId, Long ownerId, Long punchDate,
			Byte exceptionRequestType);

	public PunchExceptionRequest findPunchExceptionRequestByRequestId(Long ownerId, Long creatorUid, Long id);

	public void deletePunchExceptionRequest(PunchExceptionRequest punchExceptionRequest);

	public PunchExceptionApproval findPunchExceptionApproval(Long creatorUid, Long ownerId, Date date);

	public PunchDayLog findPunchDayLog(Long userId, Long enterpriseId, Date punchDate);


	List<PunchRuleOwnerMap> queryPunchRuleOwnerMapList(String ownerType, Long ownerId, String targetType, Long targetId, List<Long> userIds, CrossShardListingLocator locator, int i);
}
