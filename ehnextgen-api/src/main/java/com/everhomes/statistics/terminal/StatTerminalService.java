package com.everhomes.statistics.terminal;


import com.everhomes.rest.statistics.terminal.*;

import java.time.LocalDate;
import java.util.List;

public interface StatTerminalService {

    LineChart getTerminalHourLineChart(List<String> dates, TerminalStatisticsType type);

    LineChart getTerminalDayLineChart(String startDate, String endDate, TerminalStatisticsType type);

    TerminalDayStatisticsDTO qryTerminalDayStatisticsByDay(String date);

    List<TerminalDayStatisticsDTO> listTerminalDayStatisticsByDate(String startDate, String endDate, Integer namespaceId);

    List<TerminalStatisticsTaskDTO> executeStatTask(Integer namespaceId, LocalDate startDate, LocalDate endDate);

    List<TerminalAppVersionStatisticsDTO> listTerminalAppVersionStatistics(String date, Integer namespaceId);

    PieChart getTerminalAppVersionPieChart(String Date, TerminalStatisticsType type);

    void executeUserSyncTask(Integer namespaceId, boolean genData, String start, String end);

    void deleteStatTaskLog(DeleteStatTaskLogCommand cmd);

    void exportTerminalHourLineChart(TerminalStatisticsChartCommand cmd);

    void exportTerminalDayLineChart(ListTerminalStatisticsByDateCommand cmd);

    void exportTerminalAppVersionPieChart(ListTerminalStatisticsByDayCommand cmd);
}
