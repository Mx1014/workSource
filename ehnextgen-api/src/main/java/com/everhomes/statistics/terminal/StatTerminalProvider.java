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

	TerminalAppVersionStatistics statisticalAppVersionUserActivity(List<String> notVersions,String version, String date, Integer namespaceId);

	TerminalDayStatistics getTerminalDayStatisticsByDay(String date, Integer namespaceId);

	Long getTerminalActiveUserNumberByDate(String startDate, String endDate, Integer namespaceId);

	List<TerminalHourStatistics> listTerminalHourStatisticsByDay(String date, Integer namespaceId);

	List<TerminalDayStatistics> listTerminalDayStatisticsByDate(String startDate, String endDate, Integer namespaceId);
	
}
