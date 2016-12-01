package com.everhomes.statistics.terminal;


import com.everhomes.rest.statistics.terminal.LineChart;
import com.everhomes.rest.statistics.terminal.TerminalDayStatisticsDTO;
import com.everhomes.rest.statistics.terminal.TerminalStatisticsTaskDTO;
import com.everhomes.rest.statistics.terminal.TerminalStatisticsType;

import java.util.List;

public interface StatTerminalService {

    LineChart getTerminalHourLineChart(List<String> dates, TerminalStatisticsType type);

    LineChart getTerminalDayLineChart(String startDate, String endDate, TerminalStatisticsType type);

    TerminalDayStatisticsDTO qryTerminalDayStatisticsByDay(String date);

    List<TerminalDayStatisticsDTO> listTerminalDayStatisticsByDate(String startDate, String endDate);

    List<TerminalStatisticsTaskDTO> executeStatTask(String startDate, String endDate);

}
