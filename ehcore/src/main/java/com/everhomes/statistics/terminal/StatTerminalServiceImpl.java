package com.everhomes.statistics.terminal;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.namespace.Namespace;
import com.everhomes.namespace.NamespaceProvider;
import com.everhomes.rest.statistics.terminal.*;
import com.everhomes.scheduler.ScheduleProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.sms.DateUtil;
import com.everhomes.user.UserActivity;
import com.everhomes.user.UserActivityProvider;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.VersionRange;
import org.jooq.Condition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by sfyan on 2016/11/29.
 */
@Component
public class StatTerminalServiceImpl implements StatTerminalService{

    private static final Logger LOGGER = LoggerFactory.getLogger(StatTerminalServiceImpl.class);

    private static final Integer versionNum = 10;

    @Autowired
    private StatTerminalProvider statTerminalProvider;

    @Autowired
    private NamespaceProvider namespaceProvider;

    @Autowired
    private CoordinationProvider coordinationProvider;

    @Autowired
    private ConfigurationProvider configurationProvider;

    @Autowired
    private ScheduleProvider scheduleProvider;

    @Autowired
    private UserActivityProvider userActivityProvider;

    @PostConstruct
    public void setup(){
        String triggerName = StatTerminalScheduleJob.SCHEDELE_NAME + System.currentTimeMillis();
        String jobName = triggerName;
        String cronExpression = configurationProvider.getValue(StatTerminalScheduleJob.STAT_CRON_EXPRESSION, StatTerminalScheduleJob.CRON_EXPRESSION);
        //启动定时任务
        scheduleProvider.scheduleCronJob(triggerName, jobName, cronExpression, StatTerminalScheduleJob.class, null);
    }

    @Override
    public LineChart getTerminalHourLineChart(List<String> dates, TerminalStatisticsType type){
        LineChart lineChart = new LineChart();
        List<LineChartYData> ydatas = new ArrayList<>();
        ChartXData xData = new ChartXData();
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        List<String> hours = new ArrayList<>();
        for (String date :dates) {
            List<Double> datas = new ArrayList<>();
            LineChartYData yData = new LineChartYData();
            List<TerminalHourStatistics> hourStatistics = statTerminalProvider.listTerminalHourStatisticsByDay(date, namespaceId);
            for (TerminalHourStatistics hourStatistic: hourStatistics) {
                if(hours.size() < 24){
                    hours.add(hourStatistic.getHour());
                }
                if(TerminalStatisticsType.ACTIVE_USER == type){
                    datas.add(hourStatistic.getActiveUserNumber().doubleValue());
                }else if(TerminalStatisticsType.NEW_USER == type){
                    datas.add(hourStatistic.getNewUserNumber().doubleValue());
                }else if(TerminalStatisticsType.START == type){
                    datas.add(hourStatistic.getStartNumber().doubleValue());
                }else if(TerminalStatisticsType.CUMULATIVE_USER == type){
                    datas.add(hourStatistic.getCumulativeUserNumber().doubleValue());
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
        List<Double> datas = new ArrayList<>();
        LineChartYData yData = new LineChartYData();
        List<TerminalDayStatistics> dayStatistics = statTerminalProvider.listTerminalDayStatisticsByDate(startDate, endDate, namespaceId);
        for (TerminalDayStatistics dayStatistic: dayStatistics) {
            dates.add(dayStatistic.getDate());
            if(TerminalStatisticsType.ACTIVE_USER == type){
                datas.add(dayStatistic.getActiveUserNumber().doubleValue());
                yData.setName("活跃用户");
            }else if(TerminalStatisticsType.NEW_USER == type){
                datas.add(dayStatistic.getNewUserNumber().doubleValue());
                yData.setName("新增用户");
            }else if(TerminalStatisticsType.START == type){
                datas.add(dayStatistic.getStartNumber().doubleValue());
                yData.setName("启动次数");
            }else if(TerminalStatisticsType.CUMULATIVE_USER == type){
                datas.add(dayStatistic.getCumulativeUserNumber().doubleValue());
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
    public PieChart getTerminalAppVersionPieChart(String Date, TerminalStatisticsType type){
        PieChart pieChart = new PieChart();
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        List<PieChartData> datas = new ArrayList<>();
        List<TerminalAppVersionStatistics> appVersionstatistics = statTerminalProvider.listTerminalAppVersionStatisticsByDay(Date, namespaceId);
        Map<String, TerminalAppVersionStatistics> appVersionStatisticsMap = new HashMap<>();
        for (TerminalAppVersionStatistics appVersionStatistic: appVersionstatistics) {
            appVersionStatisticsMap.put(appVersionStatistic.getAppVersion(), appVersionStatistic);
        }
        List<AppVersion> appVersions = statTerminalProvider.listAppVersions(namespaceId);
        PieChartData otherData = new PieChartData();
        otherData.setRate(0D);
        otherData.setAmount(0L);
        otherData.setName("其他");
        if(TerminalStatisticsType.ACTIVE_USER == type){
            for (AppVersion appVersion:appVersions) {
                PieChartData data = new PieChartData();
                data.setName(appVersion.getName());
                TerminalAppVersionStatistics statistics = appVersionStatisticsMap.get(appVersion.getName());
                if(null == statistics){
                    data.setRate(0d);
                    data.setAmount(0L);
                }else{
                    data.setRate(statistics.getVersionActiveRate().doubleValue());
                    data.setAmount(statistics.getActiveUserNumber());
                }

                if(versionNum == datas.size()){
                    otherData.setRate(otherData.getRate() + data.getRate());
                    otherData.setAmount(otherData.getAmount() + data.getAmount());
                    continue;
                }
                datas.add(data);

            }
        }else if(TerminalStatisticsType.CUMULATIVE_USER == type){
            for (AppVersion appVersion:appVersions) {
                PieChartData data = new PieChartData();
                data.setName(appVersion.getName());
                TerminalAppVersionStatistics statistics = appVersionStatisticsMap.get(appVersion.getName());
                if(null == statistics){
                    data.setRate(0d);
                    data.setAmount(0L);
                }else{
                    data.setRate(statistics.getVersionCumulativeRate().doubleValue());
                    data.setAmount(statistics.getCumulativeUserNumber());
                }

                if(versionNum == datas.size()){
                    otherData.setRate(otherData.getRate() + data.getRate());
                    otherData.setAmount(otherData.getAmount() + data.getAmount());
                    continue;
                }
                datas.add(data);
            }
        }
        if(versionNum == datas.size()){
            datas.add(otherData);
        }
        pieChart.setData(datas);
        return pieChart;
    }

    @Override
    public List<TerminalAppVersionStatisticsDTO> listTerminalAppVersionStatistics(String Date){
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        List<TerminalAppVersionStatistics> appVersionAUNStatistics = statTerminalProvider.listTerminalAppVersionStatisticsByDay(Date, namespaceId);
        Map<String, TerminalAppVersionStatistics> appVersionAUNStatisticsMap = new HashMap<>();
        for (TerminalAppVersionStatistics appVersionStatistic: appVersionAUNStatistics) {
            appVersionAUNStatisticsMap.put(appVersionStatistic.getAppVersion(), appVersionStatistic);
        }

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        String toDay = DateUtil.dateToStr(calendar.getTime(), DateUtil.NO_SLASH);
        List<TerminalAppVersionStatistics> appVersionCUMStatistics = statTerminalProvider.listTerminalAppVersionStatisticsByDay(toDay, namespaceId);
        Map<String, TerminalAppVersionStatistics> appVersionCUMStatisticsMap = new HashMap<>();
        for (TerminalAppVersionStatistics appVersionStatistic: appVersionCUMStatistics) {
            appVersionCUMStatisticsMap.put(appVersionStatistic.getAppVersion(), appVersionStatistic);
        }

        List<AppVersion> appVersions = statTerminalProvider.listAppVersions(namespaceId);

        List<TerminalAppVersionStatisticsDTO> statisticsDTOs = new ArrayList<>();
        for (AppVersion appVersion:appVersions) {
            TerminalAppVersionStatistics statistics = appVersionAUNStatisticsMap.get(appVersion.getName());
            TerminalAppVersionStatisticsDTO statisticsDTO = ConvertHelper.convert(appVersionAUNStatisticsMap.get(appVersion.getName()), TerminalAppVersionStatisticsDTO.class);
            TerminalAppVersionStatistics todayStatistics = appVersionCUMStatisticsMap.get(appVersion.getName());
            if(null == statisticsDTO){
                statisticsDTO = new TerminalAppVersionStatisticsDTO();
                statisticsDTO.setAppVersion(appVersion.getName());
                statisticsDTO.setVersionActiveRate(0d);
                statisticsDTO.setActiveUserNumber(0L);
                statisticsDTO.setVersionCumulativeRate(0d);
                statisticsDTO.setCumulativeUserNumber(0L);
                statisticsDTO.setStartNumber(0L);
                statisticsDTO.setNewUserNumber(0L);
            }else{
                statisticsDTO.setVersionActiveRate(statistics.getVersionActiveRate().doubleValue());
            }

            if(null == todayStatistics){
                statisticsDTO.setVersionCumulativeRate(0d);
                statisticsDTO.setCumulativeUserNumber(0L);
            }else{
                statisticsDTO.setVersionCumulativeRate(todayStatistics.getVersionCumulativeRate().doubleValue());
                statisticsDTO.setCumulativeUserNumber(todayStatistics.getCumulativeUserNumber());
            }
            statisticsDTO.setOrder(appVersion.getDefaultOrder());
            statisticsDTOs.add(statisticsDTO);

        }
        return statisticsDTOs;
    }

    @Override
    public TerminalDayStatisticsDTO qryTerminalDayStatisticsByDay(String date){
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        TerminalDayStatistics dayStatistics = statTerminalProvider.getTerminalDayStatisticsByDay(date, namespaceId);
        TerminalDayStatisticsDTO day = ConvertHelper.convert(dayStatistics, TerminalDayStatisticsDTO.class);
        if(null != dayStatistics){
            day.setActiveChangeRate(dayStatistics.getActiveChangeRate().doubleValue());
            day.setCumulativeChangeRate(dayStatistics.getCumulativeChangeRate().doubleValue());
            day.setNewChangeRate(dayStatistics.getNewChangeRate().doubleValue());
            day.setStartChangeRate(dayStatistics.getStartChangeRate().doubleValue());
            day.setActiveRate(dayStatistics.getActiveRate().doubleValue());
        }
        return day;
    }

    @Override
    public List<TerminalDayStatisticsDTO> listTerminalDayStatisticsByDate(String startDate, String endDate){
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        List<TerminalDayStatistics> dayStatistics = statTerminalProvider.listTerminalDayStatisticsByDate(startDate, endDate, namespaceId);
        return dayStatistics.stream().map(r -> {
            TerminalDayStatisticsDTO day = ConvertHelper.convert(r, TerminalDayStatisticsDTO.class);
            day.setActiveChangeRate(r.getActiveChangeRate().doubleValue());
            day.setCumulativeChangeRate(r.getCumulativeChangeRate().doubleValue());
            day.setNewChangeRate(r.getNewChangeRate().doubleValue());
            day.setStartChangeRate(r.getStartChangeRate().doubleValue());
            day.setActiveRate(r.getActiveRate().doubleValue());
            return day;
        }).collect(Collectors.toList());
    }

    @Override
    public List<TerminalStatisticsTaskDTO> executeStatTask(String startDate, String endDate) {

        List<TerminalStatisticsTaskDTO> tasks = new ArrayList<TerminalStatisticsTaskDTO>();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);

        if(null == startDate || null == endDate){
            startDate = DateUtil.dateToStr(calendar.getTime(), DateUtil.YMR_SLASH);
            endDate = DateUtil.dateToStr(calendar.getTime(), DateUtil.YMR_SLASH);
        }

        //如果结束时间大于昨天，结束时间就取昨天的值
        if(endDate.compareTo(DateUtil.dateToStr(calendar.getTime(), DateUtil.YMR_SLASH)) > 0){
            endDate = DateUtil.dateToStr(calendar.getTime(), DateUtil.YMR_SLASH);
        }

        // 结束时间大于开始时间
        if(startDate.compareTo(endDate) > 0){
            startDate = DateUtil.dateToStr(calendar.getTime(), DateUtil.YMR_SLASH);
            endDate = DateUtil.dateToStr(calendar.getTime(), DateUtil.YMR_SLASH);
        }

        //获取范围内的所以日期
        List<Date> dDates = DateUtil.getStartToEndDates(DateUtil.strToDate(startDate, "yyyy-MM-dd"), DateUtil.strToDate(endDate, "yyyy-MM-dd"));

        for (Date date : dDates) {
            String sDate = DateUtil.dateToStr(date, DateUtil.YMR_SLASH);
            //按日期结算数据
            this.coordinationProvider.getNamedLock(CoordinationLocks.STAT_TERMINAL.getCode() + "_" + sDate).enter(()-> {
                tasks.add(this.statisticalByDate(sDate));
                return null;
            });
        }
        return tasks;
    }


    private TerminalStatisticsTaskDTO statisticalByDate(String date){
        LOGGER.debug("start production statistical data. date = {}", date);
        TerminalStatisticsTask task = statTerminalProvider.getTerminalStatisticsTaskByTaskNo(date);
        if(null == task){
            task = new TerminalStatisticsTask();
            task.setStatus(TerminalStatisticsTaskStatus.GENERATE_APP_VERSION_CUMULATIVE.getCode());
            task.setTaskNo(date);
            statTerminalProvider.createTerminalStatisticsTask(task);
        }
        try {
            if(TerminalStatisticsTaskStatus.fromCode(task.getStatus()) == TerminalStatisticsTaskStatus.GENERATE_APP_VERSION_CUMULATIVE){
                this.generateTerminalAppVersionCumulative(date);
                task.setStatus(TerminalStatisticsTaskStatus.GENERATE_APP_VERSION_ACTIVE.getCode());
                statTerminalProvider.updateTerminalStatisticsTask(task);
            }
            if(TerminalStatisticsTaskStatus.fromCode(task.getStatus()) == TerminalStatisticsTaskStatus.GENERATE_APP_VERSION_ACTIVE){
                this.generateTerminalAppVersionActive(date);
                task.setStatus(TerminalStatisticsTaskStatus.GENERATE_DAY_STAT.getCode());
                statTerminalProvider.updateTerminalStatisticsTask(task);
            }
            if(TerminalStatisticsTaskStatus.fromCode(task.getStatus()) == TerminalStatisticsTaskStatus.GENERATE_DAY_STAT){
                this.generateTerminalDayStatistics(date);
                task.setStatus(TerminalStatisticsTaskStatus.GENERATE_HOUR_STAT.getCode());
                statTerminalProvider.updateTerminalStatisticsTask(task);
            }
            if(TerminalStatisticsTaskStatus.fromCode(task.getStatus()) == TerminalStatisticsTaskStatus.GENERATE_HOUR_STAT){
                this.generateTerminalHourStatistics(date);
                task.setStatus(TerminalStatisticsTaskStatus.GENERATE_APP_VERSION_STAT.getCode());
                statTerminalProvider.updateTerminalStatisticsTask(task);
            }
            if(TerminalStatisticsTaskStatus.fromCode(task.getStatus()) == TerminalStatisticsTaskStatus.GENERATE_APP_VERSION_STAT){
                this.generateTerminalAppVersionStatistics(date);
                task.setStatus(TerminalStatisticsTaskStatus.FINISH.getCode());
                statTerminalProvider.updateTerminalStatisticsTask(task);
            }
        }catch (Exception e) {
            LOGGER.error("production statistical data error, date = {} error = {}", date, e);
        }
        return ConvertHelper.convert(task, TerminalStatisticsTaskDTO.class);
    }

    private void generateTerminalAppVersionCumulative(String date){
        List<AppVersion> versions = statTerminalProvider.listAppVersions(null);
        Condition cond = Tables.EH_USER_ACTIVITIES.CREATE_TIME.substring(1,10).eq(date);
        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(null);
        List<String> notVersionNames = new ArrayList<>();
        for (AppVersion version: versions) {
            Condition c = Tables.EH_USER_ACTIVITIES.NAMESPACE_ID.eq(version.getNamespaceId());
            c = c.and(cond);
            Condition vCond = Tables.EH_USER_ACTIVITIES.APP_VERSION_NAME.eq(version.getName());
            if(version.getName().split("\\.").length > 2){
                vCond = vCond.or(Tables.EH_USER_ACTIVITIES.APP_VERSION_NAME.like(version.getName() + ".%"));
            }
            c = c.and(vCond);
            while(true){
                List<UserActivity> userActivetys = userActivityProvider.listUserActivetys(c,100, locator);
                for (UserActivity userActivety: userActivetys) {
                    if(StringUtils.isEmpty(userActivety.getImeiNumber())){
                        continue;
                    }
                    String v = userActivety.getAppVersionName();
                    if(v.split("\\.").length > 3){
                        v = v.substring(0, v.lastIndexOf("."));
                    }
                    TerminalAppVersionCumulatives cumulative =statTerminalProvider.getTerminalAppVersionCumulative(null , userActivety.getImeiNumber(), userActivety.getNamespaceId());
                    if(null == cumulative){
                        cumulative = new TerminalAppVersionCumulatives();
                        cumulative.setAppVersion(v);
                        cumulative.setAppVersionRealm(userActivety.getVersionRealm());
                        cumulative.setImeiNumber(userActivety.getImeiNumber());
                        cumulative.setNamespaceId(userActivety.getNamespaceId());
                        statTerminalProvider.createTerminalAppVersionCumulatives(cumulative);
                    }else if(!notVersionNames.contains(cumulative.getAppVersion())){
                        statTerminalProvider.deleteTerminalAppVersionCumulativeById(cumulative.getId());
                        cumulative.setAppVersion(v);
                        cumulative.setAppVersionRealm(userActivety.getVersionRealm());
                        statTerminalProvider.createTerminalAppVersionCumulatives(cumulative);
                    }
                }
                if(null == locator.getAnchor()){
                    notVersionNames.add(version.getName());
                    break;
                }
            }
        }
    }

    private void generateTerminalAppVersionActive(String date){
        String tDate = date.replaceAll("-","");
        List<AppVersion> versions = statTerminalProvider.listAppVersions(null);
        Condition cond = Tables.EH_USER_ACTIVITIES.CREATE_TIME.substring(1,10).eq(date);
        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(null);
        List<String> notVersionNames = new ArrayList<>();
        for (AppVersion version: versions) {
            Condition c = Tables.EH_USER_ACTIVITIES.NAMESPACE_ID.eq(version.getNamespaceId());
            c = c.and(cond);
            Condition vCond = Tables.EH_USER_ACTIVITIES.APP_VERSION_NAME.eq(version.getName());
            if(version.getName().split("\\.").length > 2){
                vCond = vCond.or(Tables.EH_USER_ACTIVITIES.APP_VERSION_NAME.like(version.getName() + ".%"));
            }
            c = c.and(vCond);
            while(true){
                List<UserActivity> userActivetys = userActivityProvider.listUserActivetys(c,100, locator);
                for (UserActivity userActivety: userActivetys) {
                    if(StringUtils.isEmpty(userActivety.getImeiNumber())){
                        continue;
                    }
                    String v = userActivety.getAppVersionName();
                    if(v.split("\\.").length > 3){
                        v = v.substring(0, v.lastIndexOf("."));
                    }
                    TerminalAppVersionActives active =statTerminalProvider.getTerminalAppVersionActive(tDate, null, userActivety.getImeiNumber(), userActivety.getNamespaceId());
                    if(null == active){
                        active = new TerminalAppVersionActives();
                        active.setAppVersion(v);
                        active.setAppVersionRealm(userActivety.getVersionRealm());
                        active.setImeiNumber(userActivety.getImeiNumber());
                        active.setNamespaceId(userActivety.getNamespaceId());
                        active.setDate(tDate);
                        statTerminalProvider.createTerminalAppVersionActives(active);
                    }else if(!notVersionNames.contains(active.getAppVersion())){
                        statTerminalProvider.deleteTerminalAppVersionActivesById(active.getId());
                        active.setAppVersion(v);
                        active.setAppVersionRealm(userActivety.getVersionRealm());
                        statTerminalProvider.createTerminalAppVersionActives(active);
                    }
                }
                if(null == locator.getAnchor()){
                    notVersionNames.add(version.getName());
                    break;
                }
            }
        }
    }

    private void generateTerminalDayStatistics(String date){
        List<Namespace> namespaces = namespaceProvider.listNamespaces();
        Long tDate = Long.valueOf(date.replaceAll("-",""));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtil.strToDate(date, "yyyy-MM-dd"));
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        String yDate = DateUtil.dateToStr(calendar.getTime(), DateUtil.NO_SLASH);
        calendar.setTime(DateUtil.strToDate(date, "yyyy-MM-dd"));
        calendar.add(Calendar.DAY_OF_MONTH, -6);
        String sevenDate = DateUtil.dateToStr(calendar.getTime(), DateUtil.YMR_SLASH);
        calendar.setTime(DateUtil.strToDate(date, "yyyy-MM-dd"));
        calendar.add(Calendar.DAY_OF_MONTH, -29);
        String thirtyDate = DateUtil.dateToStr(calendar.getTime(), DateUtil.YMR_SLASH);
        namespaces.add(new Namespace()); // 0域
        statTerminalProvider.deleteTerminalDayStatistics(tDate.toString());
        for (Namespace namespace:namespaces) {
            Integer namespaceId = null;
            if(null != namespace){
                namespaceId = null == namespace.getId() ? 0 : namespace.getId();
            }
            TerminalDayStatistics yesterdayStatistics = statTerminalProvider.getTerminalDayStatisticsByDay(yDate, namespaceId);

            TerminalDayStatistics toDayStatistics = statTerminalProvider.statisticalUserActivity(date, null, namespaceId);
            toDayStatistics.setDate(tDate.toString());
            toDayStatistics.setNamespaceId(namespaceId);
            if(null == yesterdayStatistics){
                toDayStatistics.setActiveChangeRate(new BigDecimal(0));
                toDayStatistics.setCumulativeChangeRate(new BigDecimal(0));
                toDayStatistics.setStartChangeRate(new BigDecimal(0));
                toDayStatistics.setNewChangeRate(new BigDecimal(0));
            }else{
                if(0L == yesterdayStatistics.getActiveUserNumber()){
                    toDayStatistics.setActiveChangeRate(new BigDecimal(0));
                }else{
                    toDayStatistics.setActiveChangeRate(new BigDecimal((toDayStatistics.getActiveUserNumber().doubleValue() - yesterdayStatistics.getActiveUserNumber().doubleValue()) / yesterdayStatistics.getActiveUserNumber().doubleValue()*100));
                }

                if(0L == yesterdayStatistics.getCumulativeUserNumber()){
                    toDayStatistics.setCumulativeChangeRate(new BigDecimal(0));
                }else{
                    toDayStatistics.setCumulativeChangeRate(new BigDecimal((toDayStatistics.getCumulativeUserNumber().doubleValue() - yesterdayStatistics.getCumulativeUserNumber().doubleValue()) / yesterdayStatistics.getCumulativeUserNumber().doubleValue()*100));
                }

                if(0L == yesterdayStatistics.getStartNumber()){
                    toDayStatistics.setStartChangeRate(new BigDecimal(0));
                }else{
                    toDayStatistics.setStartChangeRate(new BigDecimal((toDayStatistics.getStartNumber().doubleValue() - yesterdayStatistics.getStartNumber().doubleValue()) / yesterdayStatistics.getStartNumber().doubleValue()*100));
                }

                if(0L == yesterdayStatistics.getNewUserNumber()){
                    toDayStatistics.setNewChangeRate(new BigDecimal(0));
                }else{
                    toDayStatistics.setNewChangeRate(new BigDecimal((toDayStatistics.getNewUserNumber().doubleValue() - yesterdayStatistics.getNewUserNumber().doubleValue()) / yesterdayStatistics.getNewUserNumber().doubleValue()*100));
                }
            }
            if(0L == toDayStatistics.getCumulativeUserNumber()){
                toDayStatistics.setActiveRate(new BigDecimal(0));
            }else{
                toDayStatistics.setActiveRate(new BigDecimal(toDayStatistics.getActiveUserNumber().doubleValue() / toDayStatistics.getCumulativeUserNumber().doubleValue()*100));
            }

            toDayStatistics.setSevenActiveUserNumber(statTerminalProvider.getTerminalActiveUserNumberByDate(sevenDate, date, namespaceId));
            toDayStatistics.setThirtyActiveUserNumber(statTerminalProvider.getTerminalActiveUserNumberByDate(thirtyDate, date, namespaceId));

            statTerminalProvider.createTerminalDayStatistics(toDayStatistics);
        }
    }

    private void generateTerminalHourStatistics(String date){
        List<Namespace> namespaces = namespaceProvider.listNamespaces();
        Long tDate = Long.valueOf(date.replaceAll("-",""));
        namespaces.add(new Namespace()); // 统计全部的
        statTerminalProvider.deleteTerminalHourStatistics(tDate.toString());
        for (Namespace namespace:namespaces) {
            Integer namespaceId = null;
            if(null != namespace){
                namespaceId = null == namespace.getId() ? 0 : namespace.getId();
            }
            Integer hour = 0;
            while (hour < 24){
                ++ hour;
                String hourStr = hour.toString();
                if(hour < 10){
                    hourStr = "0" + hour;
                }
                TerminalDayStatistics toDayStatistics = statTerminalProvider.statisticalUserActivity(date, hourStr, namespaceId);
                TerminalHourStatistics toDayHourStatistics = ConvertHelper.convert(toDayStatistics, TerminalHourStatistics.class);
                toDayHourStatistics.setDate(tDate.toString());
                toDayHourStatistics.setHour(hourStr);
                toDayHourStatistics.setNamespaceId(namespaceId);
                if(0L == toDayHourStatistics.getCumulativeUserNumber()){
                    toDayHourStatistics.setActiveRate(new BigDecimal(0));
                }else{
                    toDayHourStatistics.setActiveRate(new BigDecimal(toDayHourStatistics.getActiveUserNumber().doubleValue() / toDayHourStatistics.getCumulativeUserNumber().doubleValue()*100));
                }
                toDayHourStatistics.setChangeRate(new BigDecimal(0));
                statTerminalProvider.createTerminalHourStatistics(toDayHourStatistics);
            }
        }
    }

    private void generateTerminalAppVersionStatistics(String date) {
        String tDate = date.replaceAll("-","");
        List<AppVersion> versions = statTerminalProvider.listAppVersions(null);
        statTerminalProvider.deleteTerminalAppVersionStatistics(tDate.toString());
        for (AppVersion version: versions) {
            // 总用户数量
            Long cumulativeUserNumber = statTerminalProvider.getTerminalCumulativeUserNumber(null, version.getNamespaceId());
            // 版本用户数量
            Long versionCumulativeUserNumber = statTerminalProvider.getTerminalCumulativeUserNumber(version.getName(), version.getNamespaceId());
            // 总活跃用户数量
            Long activeUserNumber = statTerminalProvider.getTerminalAppVersionActiveUserNumberByDay(tDate, null, version.getNamespaceId());
            // 版本活跃用户数量
            Long versionActiveUserNumber = statTerminalProvider.getTerminalAppVersionActiveUserNumberByDay(tDate, version.getName(), version.getNamespaceId());
            //
            Long versionStartUmber = statTerminalProvider.getTerminalStartNumberByDay(date, version.getName(), version.getNamespaceId());
            // 版本新增用户
            Long versionNewUserNumber = statTerminalProvider.getTerminalAppVersionNewUserNumberByDay(tDate, version.getName(), version.getNamespaceId());

            TerminalAppVersionStatistics appVersionStatistics = new TerminalAppVersionStatistics();
            appVersionStatistics.setAppVersion(version.getName());
            appVersionStatistics.setAppVersionRealm(version.getRealm());
            appVersionStatistics.setNamespaceId(version.getNamespaceId());
            appVersionStatistics.setDate(tDate.toString());
            if(0L == versionCumulativeUserNumber || 0L == cumulativeUserNumber){
                appVersionStatistics.setVersionCumulativeRate(new BigDecimal(0));
            }else{
                appVersionStatistics.setVersionCumulativeRate(new BigDecimal(versionCumulativeUserNumber.doubleValue()/cumulativeUserNumber.doubleValue() * 100));
            }

            if(0L == activeUserNumber || 0L == versionActiveUserNumber){
                appVersionStatistics.setVersionActiveRate(new BigDecimal(0));
            }else{
                appVersionStatistics.setVersionActiveRate(new BigDecimal(versionActiveUserNumber.doubleValue()/activeUserNumber.doubleValue()*100));
            }
            appVersionStatistics.setCumulativeUserNumber(versionCumulativeUserNumber);
            appVersionStatistics.setActiveUserNumber(versionActiveUserNumber);
            appVersionStatistics.setNewUserNumber(versionNewUserNumber);
            appVersionStatistics.setStartNumber(versionStartUmber);
            statTerminalProvider.createTerminalAppVersionStatistics(appVersionStatistics);
        }
    }

    public static void main(String[] args) {
        String androidSql = "INSERT INTO eh_app_version (`id`,`type`,`name`,`realm`,`namespace_id`,`default_order`,`create_time`) VALUES((@version_id := @version_id + 1),'android', 'version_name','', 999988, @default_order, now())";
        String iosSql = "INSERT INTO eh_app_version (`id`,`type`,`name`,`realm`,`namespace_id`,`default_order`,`create_time`) VALUES((@version_id := @version_id + 1),'ios', 'version_name','', 999988, @default_order, now())";
        String[] androidVersions = {"3.12.0","3.12.4","4.1.0","4.1.2","4.2.2"};
        String[] iosVersions = {"3.12.0","3.12.4","4.1.0","4.1.2","4.2.2"};
        for (String version:androidVersions) {
            VersionRange versionRange = new VersionRange("["+version+","+version+")");
            System.out.println(androidSql.replace("version_name", version).replace("@default_order", "" + versionRange.getUpperBound()));
        }

        for (String version:iosVersions) {
            VersionRange versionRange = new VersionRange("["+version+","+version+")");
            System.out.println(iosSql.replace("version_name", version).replace("@default_order", "" + versionRange.getUpperBound()));
        }

//        String updSql = "update `eh_app_version` set default_order = @default_order where name = '@version_name';";
//        String[] updVersions = {"1.0.0","1.0.10","1.0.12","1.0.13","1.0.15","1.0.2","1.0.8","1.0.9","1.1.0","1.1.2","2.0.0","2.0.2","2.0.4","2.1.0","2.2.0","2.2.2","2.3.0","2.4.0","2.4.1","2.4.2","2.5.0","2.6.0","2.7.0","2.7.2","2.8.0","2.8.2","3.0.0","3.0.2","3.0.4","3.0.6","3.1.0","3.10.0","3.10.1","3.10.2","3.10.3","3.10.4","3.10.6","3.11.0","3.11.2","3.11.3","3.2.0","3.2.2","3.2.4","3.4.0","3.6.0","3.6.1","3.6.2","3.7.0","3.7.1","3.7.4","3.8.0","3.8.1","3.8.2","3.9.0","3.9.2"};
//        for (String version:updVersions) {
//            VersionRange versionRange = new VersionRange("["+version+","+version+")");
//            System.out.println(updSql.replace("@version_name", version).replace("@default_order", "" + versionRange.getUpperBound()));
//        }

//        VersionRange versionRange = new VersionRange("["+"3.10.2"+","+"3.10.2"+")");
//        System.out.println(versionRange.getUpperBound());
    }
}
