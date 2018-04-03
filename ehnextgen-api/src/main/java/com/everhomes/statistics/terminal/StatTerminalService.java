package com.everhomes.statistics.terminal;


import com.everhomes.rest.statistics.terminal.*;

import java.util.List;

public interface StatTerminalService {

    LineChart getTerminalHourLineChart(List<String> dates, TerminalStatisticsType type);

    LineChart getTerminalDayLineChart(String startDate, String endDate, TerminalStatisticsType type);

    TerminalDayStatisticsDTO qryTerminalDayStatisticsByDay(String date);

    List<TerminalDayStatisticsDTO> listTerminalDayStatisticsByDate(String startDate, String endDate);

    List<TerminalStatisticsTaskDTO> executeStatTask(Integer namespaceId, String startDate, String endDate);

    List<TerminalAppVersionStatisticsDTO> listTerminalAppVersionStatistics(String Date);

    PieChart getTerminalAppVersionPieChart(String Date, TerminalStatisticsType type);

    List<Long> executeUserSyncTask(Integer namespaceId);

    void deleteStatTaskLog(DeleteStatTaskLogCommand cmd);
}
