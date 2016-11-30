package com.everhomes.statistics.terminal;

import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.namespace.Namespace;
import com.everhomes.namespace.NamespaceProvider;
import com.everhomes.rest.statistics.terminal.*;
import com.everhomes.sms.DateUtil;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by sfyan on 2016/11/29.
 */
@Component
public class StatTerminalServiceImpl implements StatTerminalService{

    private static final Logger LOGGER = LoggerFactory.getLogger(StatTerminalServiceImpl.class);

    @Autowired
    private StatTerminalProvider statTerminalProvider;

    @Autowired
    private NamespaceProvider namespaceProvider;

    @Autowired
    private CoordinationProvider coordinationProvider;

    @Override
    public LineChart getTerminalHourLineChart(List<String> dates, TerminalStatisticsType type){
        LineChart lineChart = new LineChart();
        List<LineChartYData> ydatas = new ArrayList<>();
        ChartXData xData = new ChartXData();
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        List<String> hours = new ArrayList<>();
        for (String date :dates) {
            List<String> datas = new ArrayList<>();
            LineChartYData yData = new LineChartYData();
            List<TerminalHourStatistics> hourStatistics = statTerminalProvider.listTerminalHourStatisticsByDay(date, namespaceId);
            for (TerminalHourStatistics hourStatistic: hourStatistics) {
                if(hours.size() < 24){
                    hours.add(hourStatistic.getHour());
                }
                if(TerminalStatisticsType.ACTIVE_USER == type){
                    datas.add(hourStatistic.getActiveUserNumber().toString());
                }else if(TerminalStatisticsType.NEW_USER == type){
                    datas.add(hourStatistic.getNewUserNumber().toString());
                }else if(TerminalStatisticsType.START == type){
                    datas.add(hourStatistic.getStartNumber().toString());
                }else if(TerminalStatisticsType.CUMULATIVE_USER == type){
                    datas.add(hourStatistic.getCumulativeUserNumber().toString());
                }
            }
            yData.setName(date);
            yData.setData(datas);
            ydatas.add(yData);
        }
        xData.setData(hours);
        lineChart.setxData(xData);
        lineChart.setyData(ydatas);
        return lineChart;
    }

    @Override
    public LineChart getTerminalDayLineChart(String startDate, String endDate, TerminalStatisticsType type){
        LineChart lineChart = new LineChart();
        List<LineChartYData> ydatas = new ArrayList<>();
        ChartXData xData = new ChartXData();
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        List<String> dates = new ArrayList<>();
        List<String> datas = new ArrayList<>();
        LineChartYData yData = new LineChartYData();
        List<TerminalDayStatistics> dayStatistics = statTerminalProvider.listTerminalDayStatisticsByDate(startDate, endDate, namespaceId);
        for (TerminalDayStatistics dayStatistic: dayStatistics) {
            dates.add(dayStatistic.getDate());
            if(TerminalStatisticsType.ACTIVE_USER == type){
                datas.add(dayStatistic.getActiveUserNumber().toString());
                yData.setName("活跃用户");
            }else if(TerminalStatisticsType.NEW_USER == type){
                datas.add(dayStatistic.getNewUserNumber().toString());
                yData.setName("新增用户");
            }else if(TerminalStatisticsType.START == type){
                datas.add(dayStatistic.getStartNumber().toString());
                yData.setName("启动次数");
            }else if(TerminalStatisticsType.CUMULATIVE_USER == type){
                datas.add(dayStatistic.getCumulativeUserNumber().toString());
                yData.setName("累计用户");
            }
        }
        yData.setData(datas);
        ydatas.add(yData);
        xData.setData(dates);
        lineChart.setxData(xData);
        lineChart.setyData(ydatas);
        return lineChart;
    }

    @Override
    public TerminalDayStatisticsDTO qryTerminalDayStatisticsByDay(String date){
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        TerminalDayStatistics dayStatistics = statTerminalProvider.getTerminalDayStatisticsByDay(date, namespaceId);
        return ConvertHelper.convert(dayStatistics, TerminalDayStatisticsDTO.class);
    }

    @Override
    public List<TerminalDayStatisticsDTO> listTerminalDayStatisticsByDate(String startDate, String endDate){
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        List<TerminalDayStatistics> dayStatistics = statTerminalProvider.listTerminalDayStatisticsByDate(startDate, endDate, namespaceId);
        return dayStatistics.stream().map(r -> {
            return ConvertHelper.convert(r, TerminalDayStatisticsDTO.class);
        }).collect(Collectors.toList());
    }

    @Override
    public List<TerminalStatisticsTaskDTO> executeStatTask(Long startDate, Long endDate) {

        List<TerminalStatisticsTaskDTO> statTaskLogs = new ArrayList<TerminalStatisticsTaskDTO>();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);

        if(null == startDate || null == endDate){
            startDate = calendar.getTimeInMillis();
            endDate = calendar.getTimeInMillis();
        }

        //如果结束时间大于昨天，结束时间就取昨天的值
        if(DateUtil.dateToStr(new java.util.Date(endDate), DateUtil.YMR_SLASH).compareTo(DateUtil.dateToStr(calendar.getTime(), DateUtil.YMR_SLASH)) > 0){
            endDate = calendar.getTimeInMillis();
        }

        // 结束时间大于开始时间
        if(startDate > endDate){
            startDate = calendar.getTimeInMillis();
            endDate = calendar.getTimeInMillis();
        }

        //获取范围内的所以日期
        List<java.util.Date> dDates = DateUtil.getStartToEndDates(new java.util.Date(startDate), new java.util.Date(endDate));



        for (java.util.Date date : dDates) {
            String sDate = DateUtil.dateToStr(date, DateUtil.YMR_SLASH);
            //按日期结算数据
            this.coordinationProvider.getNamedLock(CoordinationLocks.STAT_TERMINAL.getCode() + "_" + sDate).enter(()-> {
                return null;
            });
        }

        return statTaskLogs.stream().map(r ->{
            return ConvertHelper.convert(r, TerminalStatisticsTaskDTO.class);
        }).collect(Collectors.toList());
    }




    private void generateTerminalDayStatistics(String date){
        List<Namespace> namespaces = namespaceProvider.listNamespaces();
        Long tDate = Long.valueOf(date.replaceAll("-",""));
        Long yDate = tDate - 1;
        Long sevenDate = tDate - 6;
        Long thirtyDate = tDate - 29;
        namespaces.add(null); // 统计全部的
        for (Namespace namespace:namespaces) {
            Integer namespaceId = null;
            if(null != namespace){
                namespaceId = namespace.getId();
            }
            TerminalDayStatistics yesterdayStatistics = statTerminalProvider.getTerminalDayStatisticsByDay(yDate.toString(), namespaceId);
            TerminalDayStatistics toDayStatistics = statTerminalProvider.statisticalUserActivity(tDate.toString(), null, namespaceId);
            toDayStatistics.setNamespaceId(namespaceId == null ? -1 : namespaceId);
            toDayStatistics.setDate(tDate.toString());
            toDayStatistics.setActiveChangeRate(new BigDecimal((yesterdayStatistics.getActiveUserNumber() - toDayStatistics.getActiveUserNumber()) / yesterdayStatistics.getActiveUserNumber()));
            toDayStatistics.setCumulativeChangeRate(new BigDecimal((yesterdayStatistics.getCumulativeUserNumber() - toDayStatistics.getCumulativeUserNumber()) / yesterdayStatistics.getCumulativeUserNumber()));
            toDayStatistics.setStartChangeRate(new BigDecimal((yesterdayStatistics.getStartNumber() - toDayStatistics.getStartNumber()) / yesterdayStatistics.getStartNumber()));
            toDayStatistics.setNewChangeRate(new BigDecimal((yesterdayStatistics.getNewUserNumber() - toDayStatistics.getNewUserNumber()) / yesterdayStatistics.getNewUserNumber()));
            toDayStatistics.setActiveRate(new BigDecimal(toDayStatistics.getActiveUserNumber() / toDayStatistics.getCumulativeUserNumber()));
            toDayStatistics.setSevenActiveUserNumber(statTerminalProvider.getTerminalActiveUserNumberByDate(sevenDate.toString(), tDate.toString(), namespaceId));
            toDayStatistics.setThirtyActiveUserNumber(statTerminalProvider.getTerminalActiveUserNumberByDate(thirtyDate.toString(), tDate.toString(), namespaceId));
            statTerminalProvider.createTerminalDayStatistics(toDayStatistics);
        }
    }

    private void generateTerminalHourStatistics(String date){
        List<Namespace> namespaces = namespaceProvider.listNamespaces();
        Long tDate = Long.valueOf(date.replaceAll("-",""));
        namespaces.add(null); // 统计全部的
        for (Namespace namespace:namespaces) {
            Integer namespaceId = null;
            if(null != namespace){
                namespaceId = namespace.getId();
            }
            Integer hour = 0;
            while (hour < 24){
                ++ hour;
                String hourStr = hour.toString();
                if(hour < 10){
                    hourStr = "0" + hour;
                }
                TerminalDayStatistics toDayStatistics = statTerminalProvider.statisticalUserActivity(tDate.toString(), hourStr, namespaceId);
                TerminalHourStatistics toDayHourStatistics = ConvertHelper.convert(toDayStatistics, TerminalHourStatistics.class);
                toDayHourStatistics.setNamespaceId(namespaceId == null ? -1 : namespaceId);
                toDayHourStatistics.setDate(tDate.toString());
                toDayHourStatistics.setHour(hourStr);
                toDayHourStatistics.setActiveRate(new BigDecimal(toDayHourStatistics.getActiveUserNumber() / toDayHourStatistics.getCumulativeUserNumber()));
                toDayHourStatistics.setChangeRate(new BigDecimal(0));
                statTerminalProvider.createTerminalHourStatistics(toDayHourStatistics);
            }
        }
    }

    private void generateTerminalAppVersionStatistics(String date) {
        List<Namespace> namespaces = namespaceProvider.listNamespaces();

    }

    public static void main(String[] args) {
        System.out.println(Date.valueOf("20161128"));
    }
}
