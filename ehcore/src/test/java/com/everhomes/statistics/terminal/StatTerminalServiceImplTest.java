package com.everhomes.statistics.terminal;

import com.everhomes.junit.CoreServerTestCase;
import com.everhomes.rest.statistics.terminal.ListTerminalStatisticsByDateCommand;
import com.everhomes.rest.statistics.terminal.ListTerminalStatisticsByDayCommand;
import com.everhomes.rest.statistics.terminal.TerminalStatisticsChartCommand;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

/**
 * Created by xq.tian on 2018/3/21.
 */
public class StatTerminalServiceImplTest extends CoreServerTestCase {

    @Autowired
    private StatTerminalService statTerminalService;

    @Test
    public void exportTerminalHourLineChart() {
        TerminalStatisticsChartCommand cmd = new TerminalStatisticsChartCommand();
        cmd.setDates(Arrays.asList("20180101", "20180207"));
        cmd.setNamespaceId(1000000);
        statTerminalService.exportTerminalHourLineChart(cmd);
    }

    @Test
    public void exportTerminalDayLineChart() {
        ListTerminalStatisticsByDateCommand cmd = new ListTerminalStatisticsByDateCommand();
        cmd.setStartDate("20180101");
        cmd.setEndDate("20180301");
        cmd.setNamespaceId(1000000);
        statTerminalService.exportTerminalDayLineChart(cmd);
    }

    @Test
    public void exportTerminalAppVersionPieChart() {
        ListTerminalStatisticsByDayCommand cmd = new ListTerminalStatisticsByDayCommand();
        cmd.setDate("20180301");
        cmd.setNamespaceId(1000000);
        statTerminalService.exportTerminalAppVersionPieChart(cmd);
    }
}