package com.everhomes.techpark.punch;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.organization.OrganizationMemberDetails;
import com.everhomes.rest.general_approval.GeneralApprovalAttribute;
import com.everhomes.rest.techpark.punch.DateStatus;
import com.everhomes.rest.techpark.punch.ExtDTO;
import com.everhomes.rest.techpark.punch.PunchDayLogDTO;
import com.everhomes.rest.techpark.punch.PunchExceptionRequestStatisticsItemDTO;
import com.everhomes.rest.techpark.punch.PunchExceptionRequestStatisticsItemType;
import com.everhomes.rest.techpark.punch.PunchStatusStatisticsItemType;
import com.everhomes.rest.techpark.punch.PunchType;
import com.everhomes.rest.techpark.punch.UserPunchStatusCount;
import com.everhomes.server.schema.tables.pojos.EhPunchNotifications;
import com.everhomes.techpark.punch.recordmapper.DailyPunchStatusStatisticsHistoryRecordMapper;
import com.everhomes.techpark.punch.recordmapper.DailyPunchStatusStatisticsTodayRecordMapper;
import com.everhomes.techpark.punch.recordmapper.DailyStatisticsByDepartmentBaseRecordMapper;
import com.everhomes.techpark.punch.recordmapper.MonthlyPunchStatusStatisticsRecordMapper;
import com.everhomes.techpark.punch.recordmapper.MonthlyStatisticsByDepartmentRecordMapper;
import com.everhomes.techpark.punch.recordmapper.MonthlyStatisticsByMemberRecordMapper;
import com.everhomes.techpark.punch.recordmapper.PunchExceptionRequestStatisticsRecordMapper;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;


public interface PunchProvider {

	public List<PunchLog> listPunchLogsByDate(Long userId, Long companyId, String queryDate,byte clockCode);

	public PunchTimeRule getPunchTimeRuleByCompanyId(String ownerType ,Long companyId);

	public List<Date> listPunchLogsBwteenTwoDay(Long userId, Long companyId, String beginDate, String endDate);

	public void createPunchLog(PunchLog punchLog);

	void deletePunchLog(PunchLog punchLog);

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

	public PunchDayLog getDayPunchLogByDateAndDetailId(Long detailId, Long companyId, String punchDate);

	int deletePunchDayLogByDateAndDetailId(Long detailId, Long companyId, String punchDate);

	public PunchDayLog getDayPunchLogByDateAndUserId(Long userId, Long companyId, String punchDate);
	
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

	List<PunchDayLog> listPunchDayLogsIncludeEndDay(Long detailId, Long companyId, String startDay, String endDay);

	public List<PunchExceptionRequest> listExceptionNotViewRequests(
			Long userId, Long companyId, String startDay, String endDay);

	public void viewDateFlags(Long userId, Long companyId, String format);

	public List<UserPunchStatusCount> listUserStatusPunch(Long companyId, String startDay,String endDay);
	public List<UserPunchStatusCount> listUserApprovalStatusPunch(Long companyId, String startDay,String endDay);
 
	public List<PunchDayLogDTO> listPunchDayLogs(Long companyId, String startDay, String endDay);

	List<PunchTimeRule> queryPunchTimeRules(String ownerType, Long ownerId,String targetType , Long targetId, String name);

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

	PunchStatistic getPunchStatisticById(Long id);

	void deletePunchStatistic(PunchStatistic obj);

	void updatePunchStatistic(PunchStatistic obj);

	Long createPunchStatistic(PunchStatistic obj);

	List<PunchStatistic> queryPunchStatistics(String ownerType, Long ownerId, List<String> months, Byte exceptionStatus,
											  List<Long> detailIds, Integer pageOffset, Integer pageSize);

	public void deletePunchStatisticByUser(String ownerType, List<Long> ownerId, String punchMonth, Long detailId);

	public PunchRuleOwnerMap getPunchRuleOwnerMapById(Long id);

	public List<PunchDayLog> listPunchDayLogs(List<Long> userIds, Long ownerId, List<Long> detailIds, List<Long> dptIds, String startDay, String endDay,
			Byte arriveTimeCompareFlag, Time arriveTime, Byte leaveTimeCompareFlag, Time leaveTime, Byte workTimeCompareFlag,
			Time workTime, Byte exceptionStatus,Integer pageOffset,Integer pageSize);

	public PunchExceptionRequest findPunchExceptionRequest(Long userId, Long ownerId, Long punchDate,
														   Integer intervalTimeNo);

	public PunchExceptionRequest findPunchExceptionRequestByRequestId(Long enterpriseId, Long userId, Long requestId);

	public void deletePunchExceptionRequest(PunchExceptionRequest punchExceptionRequest);

	public PunchExceptionApproval findPunchExceptionApproval(Long creatorUid, Long ownerId, Date date);

	public PunchDayLog findPunchDayLog(Long userId, Long enterpriseId, Date punchDate);


	List<PunchRuleOwnerMap> queryPunchRuleOwnerMapList(String ownerType, Long ownerId, String targetType, Long targetId, List<Long> userIds, CrossShardListingLocator locator, int i);
	List<PunchRuleOwnerMap> queryPunchRuleOwnerMaps(String ownerType,
			Long ownerId, String listType);

	public Integer countPunchLogDevice(Long userId, Long companyId,
			java.sql.Date beginDate , java.sql.Date endDate);

	PunchRuleOwnerMap getPunchRuleOwnerMapByTarget(String targetType, Long targetId);

	List<PunchTimeRule> queryPunchTimeRuleList(String ownerType, Long ownerId, String targetType,
			Long targetId, CrossShardListingLocator locator, int pageSize);

	List<PunchTimeRule> queryPunchTimeRuleList(Long startTimeLong, Long endTimeLong);

	void deletePunchTimeRulesByOwnerAndTarget(String ownerType, Long ownerId,
			String targetType, Long targetId);

	public void createPunchTimeInterval(PunchTimeInterval ptInterval);

	public void createPunchSpecialDay(PunchSpecialDay psd);

	public void deletePunchGeopointsByOwnerId(Long id);

	public void deletePunchWifisByOwnerId(Long id);

	public PunchRule getPunchruleByPunchOrgId(Long id);

	public void deletePunchTimeRuleByPunchOrgId(Long id);

	public void deletePunchSpecialDaysByPunchOrgId(Long id);

	public void deletePunchTimeIntervalByPunchRuleId(Long id);

	public List<PunchTimeRule> listActivePunchTimeRuleByOwner(String ownerType, Long ownerId, Byte status);
	
	public List<PunchTimeRule> listActivePunchTimeRuleByOwnerAndStatusList(String ownerType, Long ownerId, List<Byte> statusList);

	public List<PunchTimeInterval> listPunchTimeIntervalByTimeRuleId(Long timeRuleId);

	public List<PunchGeopoint> listPunchGeopointsByOwner(String ownerType, Long ownerId);

	public List<PunchWifi> listPunchWifsByOwner(String ownerType, Long ownerId);

	public List<PunchSpecialDay> listPunchSpecailDaysByOrgId(Long punchOrganizationId);

	public PunchSpecialDay findSpecialDayByDateAndOrgId(Long punchOrganizationId,
			java.util.Date date);

	public PunchHoliday findHolidayByDate(java.sql.Date punchDate);


	List<PunchTimeRule> listPunchTimeRulesBySplitTime(long beginTime, long endTime);

	void deletePunchTimeRuleByRuleId(Long id);

	Integer approveAbnormalPunch(Long userId, Date punchDate, Integer punchIntervalNo, Byte punchType);

	List<PunchExceptionRequest> listPunchExceptionRequestBetweenBeginAndEndTime(Long userId, Long enterpriseId, Timestamp dayStart, Timestamp dayEnd);

	List<PunchExceptionRequestStatisticsItemDTO> countPunchExceptionRequestGroupByMonth(Long userId, Long enterpriseId, String punchMonth);

	List<PunchExceptionRequest> listpunchexceptionRequestByDate(Long userId, Long enterpriseId, Date punchDate);

	Integer countExceptionRequests(Long userId, Long ownerId, String punchMonth, List<Byte> statusList);

	List<ExtDTO> listAskForLeaveExtDTOs(Long userId, String ownerType, Long ownerId, String punchMonth, Map<Long, String> approvalCategoryIdNames);

	List<PunchRule> listPunchRulesByStatus(List<Byte> statusList);

	List<PunchRule> listPunchRulesByOwnerAndRuleType(String ownerType, Long ownerId, byte code);

	PunchExceptionRequest findPunchExceptionRequest(Long userId, Long enterpriseId, Date punchDate, Integer punchIntervalNo, Byte punchType);

	PunchLog findPunchLog(Long organizationId, Long applyUserId, Date punchDate, Byte punchType, Integer punchIntervalNo);

	void updatePunchLog(PunchLog onDutyLog);

	List<PunchLog> listPunchLogs(Long ownerId, List<Long> userIds, Long startDay, Long endDay);

	PunchExceptionRequest findPunchExceptionRequest(Long userId, Long enterpriseId, Date punchDate, Byte status);

	Map<Long, Integer> countAbonormalExceptionRequestGroupByPunchDate(Long organizationId, Long userId, String month, List<Byte> statusList);

	List<PunchExceptionRequest> listpunchexceptionRequestByDate(Long userId, Long enterpriseId,
			Date startDate, Date endDate);
 
	List<PunchLog> listPunchLogs(Long userId, Long companyId, String startDay, String endDay);

	public List<Long> listPunchLogEnterprise(String startDayString, String endDayString);

	public List<Long> listPunchLogUserId(Long enterpriseId, String startDayString, String endDayString);

	Integer countpunchStatistic(String punchMonth, Long ownerId);
 
	List<PunchLog> listPunchLogsForOpenApi(Long ownerId, List<Long> userIds, Long startDay,
			Long endDay);
 
	void deletePunchLogs(Long ownerId, Date monthBegin, Date monthEnd);

	void filePunchLogs(Long ownerId, Date monthBegin, Date monthEnd, PunchMonthReport report);

	public void deletePunchDayLogs(Long ownerId, Date monthBegin, Date monthEnd);

	public void filePunchDayLogs(Long ownerId, Date monthBegin, Date monthEnd, PunchMonthReport report1);

	public void deletePunchDayLogs(Long ownerId, String punchMonth);

	public void filePunchDayLogs(Long ownerId, String punchMonth, PunchMonthReport report1);

	public PunchLog findLastPunchLog(Long userId, Long enterpriseId, Timestamp punchTime);

	public List<PunchLog> listPunchLogs(List<Long> userIds, Long ownerId, String startDay,
			String endDay, Byte exceptionStatus, Integer pageOffset, Integer pageSize);

	PunchOvertimeRule getPunchOvertimeRuleById(Long id);

	Long createPunchOvertimeRule(PunchOvertimeRule punchOvertimeRule);

	Long updatePunchOvertimeRule(PunchOvertimeRule punchOvertimeRule);

	List<PunchOvertimeRule> findPunchOvertimeRulesByPunchRuleId(Long punchRuleId, Byte status);

	void deletePunchOvertimeRulesByPunchRuleId(Long punchRuleId);

	List<PunchDayLog> listPunchDayLogsByItemTypeAndDeptIds(Long orgId, List<Long> deptIds,
			String startDay, String endDay, PunchStatusStatisticsItemType itemType, Integer pageOffset, Integer pageSize);

	List<PunchStatistic> listPunchSatisticsByItemTypeAndDeptIds(Long organizationId,
			List<Long> deptIds, String queryByMonth, PunchStatusStatisticsItemType itemType,
			Integer pageOffset, int pageSize);

	List<OrganizationMemberDetails> listExceptionMembersByDate(Long organizationId, Long departmentId, Date startDate, Date endDate, GeneralApprovalAttribute approvalAttribute, Integer pageOffset, int pageSize);
	MonthlyStatisticsByMemberRecordMapper monthlyStatisticsByMember(Long organizationId, String punchMonth, Long detailId);

	MonthlyStatisticsByDepartmentRecordMapper monthlyStatisticsByDepartment(Long organizationId, String statisticsMonth, List<Long> deptIds);

	DailyStatisticsByDepartmentBaseRecordMapper dailyStatisticsByDepartment(Long organizationId, Date statisticsDate, List<Long> deptIds, boolean isToday);

	DailyPunchStatusStatisticsTodayRecordMapper dailyPunchStatusMemberCountsTodayByDepartment(Long organizationId, Date statisticsDate, List<Long> deptIds);

	DailyPunchStatusStatisticsHistoryRecordMapper dailyPunchStatusMemberCountsHistoryByDepartment(Long organizationId, Date statisticsDate, List<Long> deptIds);

	MonthlyPunchStatusStatisticsRecordMapper monthlyPunchStatusMemberCountsByDepartment(Long organizationId, String statisticsMonth, List<Long> deptIds);

	PunchExceptionRequestStatisticsRecordMapper dailyPunchExceptionRequestMemberCountsByDepartment(Long organizationId, Date statisticsDate, List<Long> deptIds);

	PunchExceptionRequestStatisticsRecordMapper monthlyPunchExceptionRequestMemberCountsByDepartment(Long organizationId, String statisticsMonth, List<Long> deptIds);

	List<PunchDayLog> listPunchDayLogsByApprovalAttributeAndDeptIds(Long organizationId, List<Long> deptIds, Date queryDate, PunchExceptionRequestStatisticsItemType itemType, Integer pageOffset, int pageSize);

	List<PunchStatistic> listPunchSatisticsByExceptionItemTypeAndDeptIds(Long organizationId, List<Long> deptIds, String queryByMonth, PunchExceptionRequestStatisticsItemType itemType, Integer pageOffset, int pageSize);

	List<PunchDayLog> listPunchDayLogsByItemTypeAndUserId(Long organizationId, Long userId, Date startDay, Date endDay, PunchStatusStatisticsItemType itemType);

	List<PunchExceptionRequest> listExceptionRequestsByItemTypeAndDate(Long userId, Long organizationId, Date startDay, Date endDay, GeneralApprovalAttribute approvalAttribute);

	void setPunchTimeRuleStatus(Long prId, Byte targetStatus);

	void setPunchSchedulingsStatus(Long prId, Byte targetStatus, Date beginDate);

	Integer countDeviceChanges(java.sql.Date theFirstDate, java.sql.Date theLastDate, Long userId, Long ownerId);

	void batchCreatePunchNotifications(List<EhPunchNotifications> punchNotifications);

	int countPunchNotifications(Integer namespaceId, Long organizationId, Long punchRuleId, Date punchDate);

	List<PunchNotification> findPunchNotificationList(QueryPunchNotificationCondition condition);

	int invalidPunchNotificationList(QueryPunchNotificationCondition condition);

	PunchNotification findPunchNotification(Integer namespaceId, Long organizationId, Long detailId, Date punchDate, PunchType punchType, Integer punchIntervalNo);

	List<PunchNotification> findPunchNotificationList(Integer namespaceId, Long organizationId, Long userId, Date punchDate);

	void updatePunchNotification(PunchNotification punchNotification);

	int deleteAllPunchNotificationsBeforeDate(Date beforePunchDate);

	Integer countPunchSatisticsByItemTypeAndDeptIds(Long organizationId, List<Long> deptIds,
			String queryByMonth);

	Integer countPunchDayLogsByItemTypeAndDeptIds(Long organizationId, List<Long> deptIds,
			java.util.Date queryDate);

	void createPUnchGoOutLog(PunchGoOutLog log);

	PunchGoOutLog findPunchGoOutLogById(Long id);

	void updatePunchGoOutLog(PunchGoOutLog log);

	Byte processGoOutPunchFlag(Date punchDate, Long targetId);

	List<PunchGoOutLog> listPunchGoOutLogs(Long userId, Long enterpriseId,
			java.sql.Date pDate);
}
