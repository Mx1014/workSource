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

}
