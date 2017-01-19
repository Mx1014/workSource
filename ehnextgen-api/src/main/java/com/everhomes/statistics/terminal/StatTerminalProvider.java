package com.everhomes.statistics.terminal;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.statistics.transaction.*;
import org.jooq.Condition;

import java.util.List;


public interface StatTerminalProvider {
	
	void createTerminalDayStatistics(TerminalDayStatistics terminalDayStatistics);
	
	void createTerminalHourStatistics(TerminalHourStatistics terminalHourStatistics);
	
	void createTerminalAppVersionStatistics(TerminalAppVersionStatistics terminalAppVersionStatistics);
	
	void deleteTerminalDayStatistics(String date);

	void deleteTerminalHourStatistics(String date);

	void deleteTerminalAppVersionStatistics(String startDate);

	TerminalDayStatistics statisticalUserActivity(String date, String hour, Integer namespaceId);

	TerminalDayStatistics getTerminalDayStatisticsByDay(String date, Integer namespaceId);

	Long getTerminalActiveUserNumberByDate(String startDate, String endDate, Integer namespaceId);

	Long getTerminalCumulativeUserNumber(String version, Integer namespaceId);

	Long getTerminalAppVersionActiveUserNumberByDay(String date, String version, Integer namespaceId);

	List<TerminalHourStatistics> listTerminalHourStatisticsByDay(String date, Integer namespaceId);

	List<TerminalDayStatistics> listTerminalDayStatisticsByDate(String startDate, String endDate, Integer namespaceId);

	List<AppVersion> listAppVersions(Integer namespaceId);

	TerminalStatisticsTask getTerminalStatisticsTaskByTaskNo(String taskNo);

	void createTerminalStatisticsTask(TerminalStatisticsTask task);

	void updateTerminalStatisticsTask(TerminalStatisticsTask task);

	List<TerminalAppVersionStatistics> listTerminalAppVersionStatisticsByDay(String date, Integer namespaceId);

	void createTerminalAppVersionCumulatives(TerminalAppVersionCumulatives terminalAppVersionCumulatives);

	void deleteTerminalAppVersionCumulativeById(Long id);

	TerminalAppVersionCumulatives getTerminalAppVersionCumulative(String version, String imei, Integer namespaceId);

	void createTerminalAppVersionActives(TerminalAppVersionActives terminalAppVersionActives);

	void deleteTerminalAppVersionActivesById(Long id);

	TerminalAppVersionActives getTerminalAppVersionActive(String date, String version, String imei, Integer namespaceId);

	Long getTerminalAppVersionNewUserNumberByDay(String date, String version, Integer namespaceId);

	Long getTerminalStartNumberByDay(String date, String version, Integer namespaceId);

	void createAppVersion(AppVersion appVersion);

	AppVersion findAppVersion(Integer namespaceId, String name, String type);
}
