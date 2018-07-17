package com.everhomes.statistics.terminal;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


public interface StatTerminalProvider {
	
	void createTerminalDayStatistics(TerminalDayStatistics terminalDayStatistics);
	
	void createTerminalHourStatistics(TerminalHourStatistics terminalHourStatistics);
	
	void createTerminalAppVersionStatistics(List<TerminalAppVersionStatistics> terminalAppVersionStatistics);
	
	void deleteTerminalDayStatistics(Integer namespaceId, String date);

	void deleteTerminalHourStatistics(Integer namespaceId, String date);

	void deleteTerminalAppVersionStatistics(Integer namespaceId, String startDate);

	TerminalDayStatistics getTerminalDayStatisticsByDay(String date, Integer namespaceId);

	Map<String, Integer> statisticalByInterval(Integer namespaceId, LocalDateTime start, LocalDateTime end);

	List<Long> listUserIdByInterval(Integer namespaceId, LocalDateTime start, LocalDateTime end);

	Integer countVersionCumulativeUserNumber(String version, Integer namespaceId, String date);

	Integer countVersionActiveUserNumberByDay(String date, String version, Integer namespaceId);

	List<TerminalHourStatistics> listTerminalHourStatisticsByDay(String date, Integer namespaceId);

	List<TerminalDayStatistics> listTerminalDayStatisticsByDate(String startDate, String endDate, Integer namespaceId);

	List<AppVersion> listAppVersions(Integer namespaceId);

	TerminalStatisticsTask getTerminalStatisticsTaskByTaskNo(Integer namespaceId, String taskNo);

	void createTerminalStatisticsTask(TerminalStatisticsTask task);

	void updateTerminalStatisticsTask(TerminalStatisticsTask task);

	List<TerminalAppVersionStatistics> listTerminalAppVersionStatisticsByDay(String date, Integer namespaceId);

	void createTerminalAppVersionCumulatives(TerminalAppVersionCumulatives terminalAppVersionCumulatives);

	void deleteTerminalAppVersionCumulativeById(Long id);

	void createTerminalAppVersionActives(TerminalAppVersionActives terminalAppVersionActives);

	void deleteTerminalAppVersionActivesById(Long id);

	List<TerminalAppVersionActives> getTerminalAppVersionActive(String date, String version, String imei, Integer namespaceId);

	void createAppVersion(AppVersion appVersion);

	AppVersion findAppVersion(Integer namespaceId, String name, String type);

    void deleteTerminalStatTask(Integer namespaceId, String startDate, String endDate);

    void cleanTerminalAppVersionCumulativeByCondition(Integer namespaceId);

    AppVersion findLastAppVersion(Integer namespaceId);

    void cleanUserActivitiesWithNullAppVersion(Integer namespaceId);

    void createTerminalHourStatistics(List<TerminalHourStatistics> hourStats);

    void deleteTerminalAppVersionCumulative(String imeiNumber, Integer namespaceId);

	void deleteTerminalStatTask(Integer namespaceId, String taskNo);

    void cleanInvalidAppVersion(Integer namespaceId);

	List<String> listUserActivityAppVersions(Integer namespaceId);

	void correctUserActivity(Integer namespaceId);
}
