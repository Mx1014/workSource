package com.everhomes.statistics.terminal;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.filedownload.TaskService;
import com.everhomes.listing.ListingLocator;
import com.everhomes.mail.MailHandler;
import com.everhomes.namespace.Namespace;
import com.everhomes.namespace.NamespaceProvider;
import com.everhomes.rest.filedownload.TaskRepeatFlag;
import com.everhomes.rest.filedownload.TaskType;
import com.everhomes.rest.statistics.terminal.*;
import com.everhomes.rest.version.VersionRealmType;
import com.everhomes.scheduler.ScheduleProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhUserActivities;
import com.everhomes.server.schema.tables.pojos.EhTerminalAppVersionStatistics;
import com.everhomes.server.schema.tables.pojos.EhTerminalHourStatistics;
import com.everhomes.user.*;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;
import com.everhomes.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by sfyan on 2016/11/29.
 */
@Component
public class StatTerminalServiceImpl implements StatTerminalService, ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatTerminalServiceImpl.class);

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

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

    @Autowired
    private UserProvider userProvider;

    @Autowired
    private TaskService taskService;

    // 升级平台包到1.0.1，把@PostConstruct换成ApplicationListener，
    // 因为PostConstruct存在着平台PlatformContext.getComponent()会有空指针问题 by lqs 20180516
    //@PostConstruct
    public void setup() {
        String triggerName = StatTerminalScheduleJob.SCHEDELE_NAME + System.currentTimeMillis();
        String jobName = triggerName;
        String cronExpression = configurationProvider.getValue(StatTerminalScheduleJob.STAT_CRON_EXPRESSION, StatTerminalScheduleJob.CRON_EXPRESSION);
        //启动定时任务
        scheduleProvider.scheduleCronJob(triggerName, jobName, cronExpression, StatTerminalScheduleJob.class, null);
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            setup();
        }
    }

    @Override
    public LineChart getTerminalHourLineChart(List<String> dates, TerminalStatisticsType type) {
        Integer namespaceId = UserContext.getCurrentNamespaceId();

        List<String> hours = null;
        List<LineChartYData> ydatas = new ArrayList<>(dates.size());
        for (String date : dates) {
            List<TerminalHourStatistics> hourStat = statTerminalProvider.listTerminalHourStatisticsByDay(date, namespaceId);
            Stream<Number> stream;
            switch (type) {
                case ACTIVE_USER:
                    stream = hourStat.stream().map(TerminalHourStatistics::getActiveUserNumber);
                    break;
                case NEW_USER:
                    stream = hourStat.stream().map(TerminalHourStatistics::getNewUserNumber);
                    break;
                case START:
                    stream = hourStat.stream().map(TerminalHourStatistics::getStartNumber);
                    break;
                case CUMULATIVE_ACTIVE_USER:
                    stream = hourStat.stream().map(TerminalHourStatistics::getCumulativeActiveUserNumber);
                    break;
                default:
                    stream = Stream.empty();
                    break;
            }
            List<Number> numbers = stream.collect(Collectors.toList());
            ydatas.add(new LineChartYData(date, numbers));

            if (hours == null) {
                hours = hourStat.stream().map(TerminalHourStatistics::getHour).collect(Collectors.toList());
            }
        }

        ChartXData xData = new ChartXData();
        xData.setData(hours);

        LineChart lineChart = new LineChart();
        lineChart.setxData(xData);
        lineChart.setyData(ydatas);
        return lineChart;
    }

    @Override
    public LineChart getTerminalDayLineChart(String startDate, String endDate, TerminalStatisticsType type) {
        Integer namespaceId = UserContext.getCurrentNamespaceId();

        List<TerminalDayStatistics> dayStats = statTerminalProvider.listTerminalDayStatisticsByDate(startDate, endDate, namespaceId);

        String name;
        Stream<Number> stream;
        switch (type) {
            case ACTIVE_USER:
                name = "活跃用户";
                stream = dayStats.stream().map(TerminalDayStatistics::getActiveUserNumber);
                break;
            case NEW_USER:
                name = "新增用户";
                stream = dayStats.stream().map(TerminalDayStatistics::getNewUserNumber);
                break;
            case START:
                name = "启动次数";
                stream = dayStats.stream().map(TerminalDayStatistics::getStartNumber);
                break;
            case CUMULATIVE_USER:
                name = "累计用户";
                stream = dayStats.stream().map(TerminalDayStatistics::getCumulativeUserNumber);
                break;
            default:
                name = "UNKNOWN";
                stream = Stream.empty();
                break;
        }

        List<String> dateList = dayStats.stream().map(TerminalDayStatistics::getDate).collect(Collectors.toList());

        LineChart lineChart = new LineChart();
        lineChart.setxData(new ChartXData(dateList));

        LineChartYData yData = new LineChartYData(name, stream.collect(Collectors.toList()));
        lineChart.setyData(Collections.singletonList(yData));

        return lineChart;
    }

    @Override
    public PieChart getTerminalAppVersionPieChart(String date, TerminalStatisticsType type) {
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        List<TerminalAppVersionStatistics> versionStats =
                statTerminalProvider.listTerminalAppVersionStatisticsByDay(date, namespaceId);

        List<PieChartData> list = new ArrayList<>(versionStats.size());
        switch (type) {
            case CUMULATIVE_USER: {
                for (TerminalAppVersionStatistics stat : versionStats) {
                    PieChartData data = new PieChartData();
                    data.setName(stat.getAppVersion());
                    data.setAmount(stat.getCumulativeUserNumber());
                    data.setRate(stat.getVersionCumulativeRate().doubleValue());
                    list.add(data);
                }
                break;
            }
            case ACTIVE_USER: {
                for (TerminalAppVersionStatistics stat : versionStats) {
                    PieChartData data = new PieChartData();
                    data.setName(stat.getAppVersion());
                    data.setAmount(stat.getActiveUserNumber());
                    data.setRate(stat.getVersionActiveRate().doubleValue());
                    list.add(data);
                }
                break;
            }
            default:
                LOGGER.warn("not supported type: {}", type.getCode());
                break;
        }

        if (list.size() > versionNum) {
            list.sort(Comparator.comparingLong(PieChartData::getAmount).reversed());
            List<PieChartData> subList = list.subList(0, versionNum - 1);
            List<PieChartData> otherList = list.subList(versionNum - 1, list.size());

            subList.sort((o1, o2) -> {
                Version v1 = Version.fromVersionString(o1.getName());
                Version v2 = Version.fromVersionString(o2.getName());
                return (int) (v2.getEncodedValue() - v1.getEncodedValue());
            });

            PieChartData otherData = new PieChartData();
            otherData.setName("其他");
            otherData.setAmount(otherList.stream().mapToLong(PieChartData::getAmount).sum());
            otherData.setRate(otherList.stream().mapToDouble(PieChartData::getRate).sum());

            subList.add(otherData);
            list = subList;
        } else {
            list.sort((o1, o2) -> {
                Version v1 = Version.fromVersionString(o1.getName());
                Version v2 = Version.fromVersionString(o2.getName());
                return (int) (v2.getEncodedValue() - v1.getEncodedValue());
            });
        }
        return new PieChart(list);
    }

    @Override
    public void executeUserSyncTask(Integer specNsId, boolean genData, String start, String end) {
        new Thread(() -> {
            List<Namespace> namespaces = getNamespaces(specNsId);
            for (Namespace namespace : namespaces) {
                try {
                    // step 0
                    statTerminalProvider.cleanInvalidAppVersion(namespace.getId());

                    // step 1
                    statTerminalProvider.cleanTerminalAppVersionCumulativeByCondition(namespace.getId());

                    // step 2
                    statTerminalProvider.cleanUserActivitiesWithNullAppVersion(namespace.getId());

                    // step 3
                    statTerminalProvider.correctUserActivity(namespace.getId());

                    // step 4
                    List<String> appVersionList = statTerminalProvider.listUserActivityAppVersions(namespace.getId());
                    List<AppVersion> appVersions = statTerminalProvider.listAppVersions(namespace.getId());
                    List<String> existVersion = appVersions.stream().map(AppVersion::getName).collect(Collectors.toList());
                    for (String s : appVersionList) {
                        if (!existVersion.contains(s)) {
                            AppVersion version = new AppVersion();
                            version.setName(s);
                            version.setDefaultOrder((int) Version.fromVersionString(s).getEncodedValue());
                            version.setNamespaceId(namespace.getId());
                            version.setType(OSType.Android.name());
                            version.setRealm(VersionRealmType.ANDROID.getCode());
                            statTerminalProvider.createAppVersion(version);
                        }
                    }

                    // step 5
                    List<Long> uids = statTerminalProvider.listUserIdByInterval(namespace.getId(), null, null);
                    List<User> users = userProvider.listAppAndWeiXinUserByCreateTime(namespace.getId(), null, null, uids);

                    for (User user : users) {
                        UserActivity activity = new UserActivity();
                        activity.setUid(user.getId());
                        activity.setImeiNumber(String.valueOf(user.getId()));
                        activity.setNamespaceId(user.getNamespaceId());

                        long threeDayMill = 3 * 24 * 60 * 60 * 1000 - 1;
                        Date date = new Date(user.getCreateTime().getTime());
                        long time = date.getTime();
                        Timestamp minTime = new Timestamp(time - threeDayMill);
                        Timestamp maxTime = new Timestamp(time + threeDayMill);

                        List<UserActivity> userActivities = userActivityProvider.listUserActivetys(new ListingLocator(), 1, (locator, query) -> {
                            query.addConditions(Tables.EH_USER_ACTIVITIES.NAMESPACE_ID.eq(namespace.getId()));
                            query.addConditions(Tables.EH_USER_ACTIVITIES.CREATE_TIME.between(minTime, maxTime));
                            return query;
                        });

                        String appVersion = null;
                        if (userActivities.size() > 0) {
                            appVersion = userActivities.get(0).getAppVersionName();
                        } else {
                            List<AppVersion> list = statTerminalProvider.listAppVersions(namespace.getId());
                            if (list.size() > 0) {
                                appVersion = list.iterator().next().getName();
                            } else {
                                appVersion = "5.0.0";

                                AppVersion version = new AppVersion();
                                version.setName(appVersion);
                                version.setDefaultOrder((int) Version.fromVersionString(appVersion).getEncodedValue());
                                version.setNamespaceId(namespace.getId());
                                version.setType(OSType.Android.name());
                                version.setRealm(VersionRealmType.ANDROID.getCode());
                                statTerminalProvider.createAppVersion(version);
                            }
                        }

                        activity.setAppVersionName(appVersion);
                        activity.setCreateTime(user.getCreateTime());
                        activity.setActivityType(ActivityType.BORDER_REGISTER.getCode());

                        if (LOGGER.isDebugEnabled()) {
                            LOGGER.debug("sync user to userActivity namespaceId = {}, userId = {}", namespace.getId(), user.getId());
                        }

                        userActivityProvider.addActivity(activity, activity.getUid());

                        List<TerminalAppVersionActives> terminalAppVersionActive = statTerminalProvider
                                .getTerminalAppVersionActive(null, null, activity.getImeiNumber(), namespace.getId());
                        if (terminalAppVersionActive == null || terminalAppVersionActive.size() == 0) {
                            TerminalAppVersionActives appVersionActive = new TerminalAppVersionActives();
                            appVersionActive.setDate(user.getCreateTime().toLocalDateTime().format(FORMATTER));
                            appVersionActive.setAppVersion(activity.getAppVersionName());
                            appVersionActive.setImeiNumber(activity.getImeiNumber());
                            appVersionActive.setNamespaceId(namespace.getId());
                            appVersionActive.setAppVersionRealm(activity.getVersionRealm());
                            statTerminalProvider.createTerminalAppVersionActives(appVersionActive);
                        }
                    }
                } catch (Exception e) {
                    LOGGER.error("sync user error ns = " + namespace.getId(), e);
                    sendExceptionMail(e, namespace.getId(), new TerminalStatisticsTask());
                }
            }
            if (genData) {
                LocalDate st = LocalDate.parse(start, FORMATTER);
                LocalDate ed = LocalDate.parse(end, FORMATTER);
                executeStatTask(specNsId, st, ed);
            }
        }, "term-user-syncTask-0").start();
    }

    @Override
    public void deleteStatTaskLog(DeleteStatTaskLogCommand cmd) {
        statTerminalProvider.deleteTerminalStatTask(cmd.getNamespaceId(), cmd.getStartDate(), cmd.getEndDate());
    }

    @Override
    public void exportTerminalHourLineChart(TerminalStatisticsChartCommand cmd) {

        Map<String, Object> params = new HashMap<>();
        params.put("cmd", cmd);
        params.put("exportType", "exportTerminalHourLineChart");

        String today = DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDate.now());
        Namespace namespace = namespaceProvider.findNamespaceById(cmd.getNamespaceId());

        params.put("fileType", "newUser");
        String fileName = namespace.getName() + "_时段分析_新增用户_" + today + ".xlsx";

        taskService.createTask(
                fileName,
                TaskType.FILEDOWNLOAD.getCode(),
                StatTerminalFileDownloadTaskHandler.class,
                params,
                TaskRepeatFlag.REPEAT.getCode(),
                new Date()
        );

        params.put("fileType", "start");
        fileName = namespace.getName() + "_时段分析_启动次数_" + today + ".xlsx";

        taskService.createTask(
                fileName,
                TaskType.FILEDOWNLOAD.getCode(),
                StatTerminalFileDownloadTaskHandler.class,
                params,
                TaskRepeatFlag.REPEAT.getCode(),
                new Date()
        );

        params.put("fileType", "cumulativeUser");
        fileName = namespace.getName() + "_时段分析_时段累计日活_" + today + ".xlsx";

        taskService.createTask(
                fileName,
                TaskType.FILEDOWNLOAD.getCode(),
                StatTerminalFileDownloadTaskHandler.class,
                params,
                TaskRepeatFlag.REPEAT.getCode(),
                new Date()
        );

        params.put("fileType", "activeUser");
        fileName = namespace.getName() + "_时段分析_分时活跃_" + today + ".xlsx";

        taskService.createTask(
                fileName,
                TaskType.FILEDOWNLOAD.getCode(),
                StatTerminalFileDownloadTaskHandler.class,
                params,
                TaskRepeatFlag.REPEAT.getCode(),
                new Date()
        );
    }

    @Override
    public void exportTerminalDayLineChart(ListTerminalStatisticsByDateCommand cmd) {
        Map<String, Object> params = new HashMap<>();
        params.put("cmd", cmd);
        params.put("exportType", "exportTerminalDayLineChart");

        Namespace namespace = namespaceProvider.findNamespaceById(cmd.getNamespaceId());
        String fileName = namespace.getName() + "_整体趋势_" + cmd.getStartDate() + "_" + cmd.getEndDate() + ".xlsx";

        taskService.createTask(
                fileName,
                TaskType.FILEDOWNLOAD.getCode(),
                StatTerminalFileDownloadTaskHandler.class,
                params,
                TaskRepeatFlag.REPEAT.getCode(),
                new Date()
        );
    }

    @Override
    public void exportTerminalAppVersionPieChart(ListTerminalStatisticsByDayCommand cmd) {
        Namespace namespace = namespaceProvider.findNamespaceById(cmd.getNamespaceId());
        String fileName = namespace.getName() + "_版本分布_" + cmd.getDate() + ".xlsx";

        Map<String, Object> params = new HashMap<>();
        params.put("cmd", cmd);
        params.put("exportType", "exportTerminalAppVersionPieChart");

        taskService.createTask(
                fileName,
                TaskType.FILEDOWNLOAD.getCode(),
                StatTerminalFileDownloadTaskHandler.class,
                params,
                TaskRepeatFlag.REPEAT.getCode(),
                new Date()
        );
    }

    @Override
    public List<TerminalAppVersionStatisticsDTO> listTerminalAppVersionStatistics(String date, Integer namespaceId) {
        List<TerminalAppVersionStatistics> versionStats =
                statTerminalProvider.listTerminalAppVersionStatisticsByDay(date, namespaceId);

        return versionStats.stream()
                .map(r -> {
                    TerminalAppVersionStatisticsDTO dto = ConvertHelper.convert(r, TerminalAppVersionStatisticsDTO.class);
                    dto.setVersionCumulativeRate(r.getVersionCumulativeRate().doubleValue());
                    dto.setVersionActiveRate(r.getVersionActiveRate().doubleValue());
                    dto.setOrder(Math.toIntExact(Version.fromVersionString(r.getAppVersion()).getEncodedValue()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public TerminalDayStatisticsDTO qryTerminalDayStatisticsByDay(String date) {
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        TerminalDayStatistics dayStatistics = statTerminalProvider.getTerminalDayStatisticsByDay(date, namespaceId);
        TerminalDayStatisticsDTO day = ConvertHelper.convert(dayStatistics, TerminalDayStatisticsDTO.class);
        if (null != dayStatistics) {
            day.setAverageActiveUserChangeRate(dayStatistics.getAverageActiveUserChangeRate().doubleValue());
            day.setActiveChangeRate(dayStatistics.getActiveChangeRate().doubleValue());
            // day.setCumulativeChangeRate(dayStatistics.getCumulativeChangeRate().doubleValue());
            day.setNewChangeRate(dayStatistics.getNewChangeRate().doubleValue());
            day.setStartChangeRate(dayStatistics.getStartChangeRate().doubleValue());
            // day.setActiveRate(dayStatistics.getActiveRate().doubleValue());
        }
        return day;
    }

    @Override
    public List<TerminalDayStatisticsDTO> listTerminalDayStatisticsByDate(String startDate, String endDate, Integer namespaceId) {
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
    public List<TerminalStatisticsTaskDTO> executeStatTask(Integer namespaceId, LocalDate startDate, LocalDate endDate) {
        List<TerminalStatisticsTaskDTO> tasks = new ArrayList<>();

        final LocalDate[] date = {startDate};
        do {
            //按日期结算数据
            this.coordinationProvider.getNamedLock(
                    CoordinationLocks.STAT_TERMINAL.getCode() + "_" + date[0].toString()).tryEnter(() -> {
                List<Namespace> namespaceList = getNamespaces(namespaceId);
                for (Namespace namespace : namespaceList) {
                    tasks.add(this.statisticalByDate(namespace.getId(), date[0]));
                }
            });
            date[0] = date[0].plusDays(1);
        } while (date[0].isBefore(endDate));

        return tasks;
    }

    private List<Namespace> getNamespaces(Integer namespaceId) {
        List<Namespace> namespaceList = new ArrayList<>();
        if (namespaceId == null) {
            namespaceList = namespaceProvider.listNamespaces();
        } else {
            Namespace ns = new Namespace();
            ns.setId(namespaceId);
            namespaceList.add(ns);
        }
        return namespaceList;
    }

    private TerminalStatisticsTaskDTO statisticalByDate(Integer namespaceId, LocalDate date) {
        LOGGER.debug("start production statistical data. date = {}", date);

        final String taskNo = date.format(FORMATTER);

        statTerminalProvider.deleteTerminalStatTask(namespaceId, taskNo);

        TerminalStatisticsTask task = new TerminalStatisticsTask();
        task.setStatus(TerminalStatisticsTaskStatus.CORRECT_USER_ACTIVITY.getCode());
        task.setTaskNo(taskNo);
        task.setNamespaceId(namespaceId);
        statTerminalProvider.createTerminalStatisticsTask(task);

        try {
            if (TerminalStatisticsTaskStatus.fromCode(task.getStatus()) == TerminalStatisticsTaskStatus.CORRECT_USER_ACTIVITY) {
                try {
                    this.correctUserActivity(namespaceId, date);
                } catch (Exception e) {
                    LOGGER.error("correct user activity error", e);
                    sendExceptionMail(e, namespaceId, task);
                }
                task.setStatus(TerminalStatisticsTaskStatus.GENERATE_HOUR_STAT.getCode());
                statTerminalProvider.updateTerminalStatisticsTask(task);
            }

            if (TerminalStatisticsTaskStatus.fromCode(task.getStatus()) == TerminalStatisticsTaskStatus.GENERATE_HOUR_STAT) {
                this.generateTerminalHourStatistics(namespaceId, date);
                task.setStatus(TerminalStatisticsTaskStatus.GENERATE_DAY_STAT.getCode());
                statTerminalProvider.updateTerminalStatisticsTask(task);
            }

            if (TerminalStatisticsTaskStatus.fromCode(task.getStatus()) == TerminalStatisticsTaskStatus.GENERATE_DAY_STAT) {
                this.generateTerminalDayStatistics(namespaceId, date);
                task.setStatus(TerminalStatisticsTaskStatus.GENERATE_APP_VERSION_ACTIVE.getCode());
                statTerminalProvider.updateTerminalStatisticsTask(task);
            }

            if (TerminalStatisticsTaskStatus.fromCode(task.getStatus()) == TerminalStatisticsTaskStatus.GENERATE_APP_VERSION_ACTIVE) {
                this.generateTerminalAppVersionActive(namespaceId, date);
                task.setStatus(TerminalStatisticsTaskStatus.GENERATE_APP_VERSION_STAT.getCode());
                statTerminalProvider.updateTerminalStatisticsTask(task);
            }

            if (TerminalStatisticsTaskStatus.fromCode(task.getStatus()) == TerminalStatisticsTaskStatus.GENERATE_APP_VERSION_STAT) {
                this.generateTerminalAppVersionStatistics(namespaceId, date);
                task.setStatus(TerminalStatisticsTaskStatus.FINISH.getCode());
                statTerminalProvider.updateTerminalStatisticsTask(task);
            }
        } catch (Exception e) {
            LOGGER.error("production statistical data error, date = " + date, e);
            sendExceptionMail(e, namespaceId, task);
        }
        return ConvertHelper.convert(task, TerminalStatisticsTaskDTO.class);
    }

    private void sendExceptionMail(Exception e, Integer namespaceId, TerminalStatisticsTask task) {
        // ------------------------------------------------------------------------
        String xqt = "xq.tian@zuolin.com";
        String home = configurationProvider.getValue(ConfigConstants.HOME_URL, "");
        String handlerName = MailHandler.MAIL_RESOLVER_PREFIX + MailHandler.HANDLER_JSMTP;
        MailHandler handler = PlatformContext.getComponent(handlerName);
        String account = configurationProvider.getValue(0, "mail.smtp.account", "zuolin@zuolin.com");

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             PrintStream stream = new PrintStream(out)) {
            e.printStackTrace(stream);

            stream.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
            stream.println(StringHelper.toJsonString(task));
            stream.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

            String message = out.toString("UTF-8");
            handler.sendMail(0, account, xqt,
                    String.format("terminal stat error (%s, %s)", home, namespaceId), message);
        } catch (Exception ignored) { }
        // ------------------------------------------------------------------------
    }

    private void generateTerminalAppVersionActive(Integer namespaceId, LocalDate date) {
        final String dateStr = date.format(FORMATTER);

        final LocalDateTime startOfDay = date.atTime(LocalTime.MIN);
        final LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        List<AppVersion> versions = statTerminalProvider.listAppVersions(namespaceId);
        for (AppVersion version : versions) {
            ListingLocator locator = new ListingLocator();
            do {
                List<UserActivity> userActivitys = processTime(() -> {
                    return userActivityProvider.listUserActivetys(locator, 1000, (locator1, query) -> {
                        query.addConditions(Tables.EH_USER_ACTIVITIES.NAMESPACE_ID.eq(version.getNamespaceId()));
                        query.addConditions(Tables.EH_USER_ACTIVITIES.APP_VERSION_NAME.eq(version.getName()));
                        query.addConditions(Tables.EH_USER_ACTIVITIES.CREATE_TIME
                                .between(Timestamp.valueOf(startOfDay), Timestamp.valueOf(endOfDay)));
                        return query;
                    });
                }, date, "generateTerminalAppVersionCumulative", "listUserActivetys");

                for (UserActivity activity : userActivitys) {
                    if (StringUtils.isEmpty(activity.getImeiNumber())) {
                        continue;
                    }
                    statTerminalProvider.deleteTerminalAppVersionCumulative(activity.getImeiNumber(), activity.getNamespaceId());

                    TerminalAppVersionActives active = new TerminalAppVersionActives();
                    active.setImeiNumber(String.valueOf(activity.getUid()));
                    active.setAppVersion(activity.getAppVersionName());
                    active.setAppVersionRealm(activity.getVersionRealm());
                    active.setNamespaceId(activity.getNamespaceId());
                    active.setDate(dateStr);
                    statTerminalProvider.createTerminalAppVersionActives(active);
                }
            } while (locator.getAnchor() != null);
        }
    }

    private void generateTerminalDayStatistics(Integer namespaceId, LocalDate date) {
        final String dateStr = date.format(FORMATTER);
        statTerminalProvider.deleteTerminalDayStatistics(namespaceId, dateStr);

        TerminalDayStatistics dayStat = new TerminalDayStatistics();
        dayStat.setDate(dateStr);
        dayStat.setNamespaceId(namespaceId);

        List<TerminalHourStatistics> hourStatistics = statTerminalProvider.listTerminalHourStatisticsByDay(dateStr, namespaceId);

        hourStatistics.stream().max(Comparator.comparing(EhTerminalHourStatistics::getHour)).ifPresent(stat -> {
            dayStat.setActiveUserNumber(stat.getCumulativeActiveUserNumber());
            dayStat.setCumulativeUserNumber(stat.getCumulativeUserNumber());
        });

        long newUserNumber = hourStatistics.stream().mapToLong(TerminalHourStatistics::getNewUserNumber).sum();
        dayStat.setNewUserNumber(newUserNumber);

        long startNumber = hourStatistics.stream().mapToLong(TerminalHourStatistics::getStartNumber).sum();
        dayStat.setStartNumber(startNumber);

        long activeNumber = hourStatistics.stream().mapToLong(TerminalHourStatistics::getActiveUserNumber).sum();
        dayStat.setAverageActiveUserNumber(activeNumber / hourStatistics.size());

        dayStat.setActiveChangeRate(BigDecimal.ZERO);
        dayStat.setCumulativeChangeRate(BigDecimal.ZERO);
        dayStat.setStartChangeRate(BigDecimal.ZERO);
        dayStat.setNewChangeRate(BigDecimal.ZERO);
        dayStat.setActiveRate(BigDecimal.ZERO);
        dayStat.setAverageActiveUserChangeRate(BigDecimal.ZERO);

        String yesterdayStr = date.minusDays(1).format(FORMATTER);
        TerminalDayStatistics yesterdayStat = statTerminalProvider.getTerminalDayStatisticsByDay(yesterdayStr, namespaceId);
        if (yesterdayStat != null) {
            if (0L != yesterdayStat.getActiveUserNumber()) {
                long sub = dayStat.getActiveUserNumber() - yesterdayStat.getActiveUserNumber();
                BigDecimal rate = rate(sub, yesterdayStat.getActiveUserNumber());
                dayStat.setActiveChangeRate(rate);
            }
            if (0L != yesterdayStat.getAverageActiveUserNumber()) {
                long sub = dayStat.getAverageActiveUserNumber() - yesterdayStat.getAverageActiveUserNumber();
                BigDecimal rate = rate(sub, yesterdayStat.getAverageActiveUserNumber());
                dayStat.setAverageActiveUserChangeRate(rate);
            }
            if (0L != yesterdayStat.getCumulativeUserNumber()) {
                long sub = dayStat.getCumulativeUserNumber() - yesterdayStat.getCumulativeUserNumber();
                BigDecimal rate = rate(sub, yesterdayStat.getCumulativeUserNumber());
                dayStat.setCumulativeChangeRate(rate);
            }
            if (0L != yesterdayStat.getStartNumber()) {
                long sub = dayStat.getStartNumber() - yesterdayStat.getStartNumber();
                BigDecimal rate = rate(sub, yesterdayStat.getStartNumber());
                dayStat.setStartChangeRate(rate);
            }
            if (0L != yesterdayStat.getNewUserNumber()) {
                long sub = dayStat.getNewUserNumber() - yesterdayStat.getNewUserNumber();
                BigDecimal rate = rate(sub, yesterdayStat.getNewUserNumber());
                dayStat.setNewChangeRate(rate);
            }
        }

        if (0L != dayStat.getCumulativeUserNumber()) {
            BigDecimal rate = rate(dayStat.getActiveUserNumber(), dayStat.getCumulativeUserNumber());
            dayStat.setActiveRate(rate);
        }

        final LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        final LocalDateTime aWeekAgo = date.minusWeeks(1).atTime(LocalTime.MAX);

        Map<String, Integer> map = statTerminalProvider.statisticalByInterval(namespaceId, aWeekAgo, endOfDay);
        dayStat.setSevenActiveUserNumber(map.get("activeUserNumber").longValue());

        LocalDateTime aMonthAgo = date.minusMonths(1).atTime(LocalTime.MAX);
        map = statTerminalProvider.statisticalByInterval(namespaceId, aMonthAgo, endOfDay);
        dayStat.setThirtyActiveUserNumber(map.get("activeUserNumber").longValue());

        statTerminalProvider.createTerminalDayStatistics(dayStat);
    }

    private void generateTerminalHourStatistics(Integer namespaceId, LocalDate date) {
        final String dateStr = date.format(FORMATTER);
        statTerminalProvider.deleteTerminalHourStatistics(namespaceId, dateStr);

        final LocalDateTime startOfDay = date.atTime(LocalTime.MIN);
        final LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        LocalDateTime dateTime = startOfDay;

        List<TerminalHourStatistics> hourStats = new ArrayList<>(24);
        while (dateTime.isBefore(endOfDay)) {
            TerminalHourStatistics stat = new TerminalHourStatistics();

            Map<String, Integer> hourMap = statTerminalProvider.statisticalByInterval(namespaceId, dateTime, dateTime.plusHours(1));
            stat.setActiveUserNumber(hourMap.get("activeUserNumber").longValue());
            stat.setStartNumber(hourMap.get("startupNumber").longValue());

            // 这次是查询真正的新增用户
            Integer totalNewUserNumber = userProvider.countAppAndWeiXinUserByCreateTime(namespaceId, dateTime, dateTime.plusHours(1), null);
            stat.setNewUserNumber(totalNewUserNumber.longValue());

            // 时段累计日活
            Map<String, Integer> dayMap = statTerminalProvider.statisticalByInterval(namespaceId, startOfDay, dateTime.plusHours(1));
            stat.setCumulativeActiveUserNumber(dayMap.get("activeUserNumber").longValue());

            // 总用户数
            Integer cumulativeUserNumber = userProvider.countAppAndWeiXinUserByCreateTime(namespaceId, null, dateTime.plusHours(1), null);
            stat.setCumulativeUserNumber(cumulativeUserNumber.longValue());

            if (stat.getCumulativeUserNumber() == 0L) {
                stat.setActiveRate(BigDecimal.ZERO);
            } else {
                BigDecimal rate = rate(stat.getActiveUserNumber(), stat.getCumulativeActiveUserNumber());
                stat.setActiveRate(rate);
            }
            stat.setChangeRate(BigDecimal.ZERO);

            stat.setHour(String.format("%02d", dateTime.getHour()));
            stat.setDate(dateStr);
            stat.setNamespaceId(namespaceId);

            hourStats.add(stat);

            dateTime = dateTime.plusHours(1);
        }
        statTerminalProvider.createTerminalHourStatistics(hourStats);
    }

    private void correctUserActivity(Integer namespaceId, LocalDate date) {
        final LocalDateTime startOfDay = date.atTime(LocalTime.MIN);
        final LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        List<AppVersion> appVersions = statTerminalProvider.listAppVersions(namespaceId);
        Optional<AppVersion> maxVer = appVersions.stream().max(Comparator.comparingLong(AppVersion::getDefaultOrder));
        String versionName = "1.0.0";
        if (maxVer.isPresent()) {
            versionName = maxVer.get().getName();
        }

        List<Long> uids = statTerminalProvider.listUserIdByInterval(namespaceId, startOfDay, endOfDay);
        List<User> notInUserActivityUsers = userProvider.listAppAndWeiXinUserByCreateTime(namespaceId, startOfDay, endOfDay, uids);

        List<UserActivity> activityList = new ArrayList<>(notInUserActivityUsers.size());
        for (User user : notInUserActivityUsers) {
            UserActivity activity = new UserActivity();
            activity.setUid(user.getId());
            activity.setImeiNumber(String.valueOf(user.getId()));
            activity.setNamespaceId(namespaceId);
            activity.setActivityType(ActivityType.CORRECT.getCode());
            activity.setCreateTime(user.getCreateTime());
            activity.setAppVersionName(versionName);

            activityList.add(activity);
        }
        if (activityList.size() > 0) {
            userActivityProvider.addActivities(activityList);
        }

        ListingLocator locator = new ListingLocator();
        do {
            List<UserActivity> activities = userActivityProvider.listUserActivetys(locator, 1000, (locator1, query) -> {
                EhUserActivities t = Tables.EH_USER_ACTIVITIES;
                query.addConditions(t.NAMESPACE_ID.eq(namespaceId));
                if (uids.size() > 0) {
                    query.addConditions(t.UID.notIn(uids));
                }
                query.addConditions(t.CREATE_TIME.between(Timestamp.valueOf(startOfDay), Timestamp.valueOf(endOfDay)));
                return query;
            });

            for (UserActivity activity : activities) {
                userActivityProvider.deleteUserActivity(activity);
            }
        } while (locator.getAnchor() != null);
    }

    interface Processor<T> {
        T process();
    }

    private <T> T processTime(Processor<T> processor, Object date, String... tags) {
        long start = System.currentTimeMillis();
        T t = processor.process();
        long end = System.currentTimeMillis();
        LOGGER.debug("date = {}, duration = {}s, tag = {}", date, (end - start) / 1000, org.apache.commons.lang.StringUtils.join(tags, " "));
        return t;
    }

    private void generateTerminalAppVersionStatistics(Integer namespaceId, LocalDate date) {
        final String dateStr = date.format(FORMATTER);

        List<AppVersion> versions = statTerminalProvider.listAppVersions(namespaceId);
        statTerminalProvider.deleteTerminalAppVersionStatistics(namespaceId, dateStr);

        TerminalDayStatistics dayStat = statTerminalProvider.getTerminalDayStatisticsByDay(dateStr, namespaceId);
        List<TerminalAppVersionStatistics> statList = new ArrayList<>(versions.size());
        for (AppVersion version : versions) {
            TerminalAppVersionStatistics versionStat = new TerminalAppVersionStatistics();
            versionStat.setAppVersion(version.getName());
            versionStat.setAppVersionRealm(version.getRealm());
            versionStat.setNamespaceId(version.getNamespaceId());
            versionStat.setDate(dateStr);

            // 版本总用户数量
            Integer versionCumulativeUserNumber = statTerminalProvider
                    .countVersionCumulativeUserNumber(version.getName(), version.getNamespaceId(), dateStr);
            versionStat.setCumulativeUserNumber(versionCumulativeUserNumber.longValue());

            // 版本活跃用户数
            Integer versionActiveUserNumber = statTerminalProvider
                    .countVersionActiveUserNumberByDay(dateStr, version.getName(), version.getNamespaceId());
            versionStat.setActiveUserNumber(versionActiveUserNumber.longValue());

            // 版本启动次数
            // Long versionStartUmber = processTime(() -> {
            //     return statTerminalProvider.getTerminalStartNumberByDay(date, version.getName(), version.getNamespaceId());
            // }, date, "generateTerminalAppVersionStatistics", "getTerminalStartNumberByDay");
            // 业务没使用, 不计算
            versionStat.setStartNumber(0L);

            // 版本新增用户
            // Long versionNewUserNumber = statTerminalProvider.getTerminalAppVersionNewUserNumberByDay(tDate, version.getName(), version.getNamespaceId());
            // 业务没使用, 不计算
            versionStat.setNewUserNumber(0L);

            if (0L == versionCumulativeUserNumber || 0L == dayStat.getCumulativeUserNumber()) {
                versionStat.setVersionCumulativeRate(BigDecimal.ZERO);
            } else {
                BigDecimal rate = rate(versionCumulativeUserNumber, dayStat.getCumulativeUserNumber());
                versionStat.setVersionCumulativeRate(rate);
            }

            if (0L == dayStat.getActiveUserNumber() || 0L == versionActiveUserNumber) {
                versionStat.setVersionActiveRate(BigDecimal.ZERO);
            } else {
                BigDecimal rate = rate(versionActiveUserNumber, dayStat.getActiveUserNumber());
                versionStat.setVersionActiveRate(rate);
            }
            statList.add(versionStat);
        }

        fixRate(statList);

        statTerminalProvider.createTerminalAppVersionStatistics(statList);
    }

    // 修正百分比相加后不是 100 的问题
    private void fixRate(List<TerminalAppVersionStatistics> statList) {
        statList.stream()
                .map(EhTerminalAppVersionStatistics::getVersionCumulativeRate)
                .reduce(BigDecimal::add)
                .ifPresent(r -> {
                    List<TerminalAppVersionStatistics> list =
                            statList.stream()
                            .filter(rr -> rr.getVersionCumulativeRate().compareTo(BigDecimal.ZERO) != 0)
                            .collect(Collectors.toList());

                    if (r.compareTo(BigDecimal.ZERO) == 0) {
                        return;
                    }

                    BigDecimal sub = new BigDecimal("100").subtract(r);
                    int i = sub.compareTo(BigDecimal.ZERO);
                    if (i > 0) {
                        list.sort((o1, o2) -> o2.getVersionCumulativeRate().compareTo(o1.getVersionCumulativeRate()));
                        int i1 = sub.divide(new BigDecimal("0.01"), RoundingMode.FLOOR).intValue();
                        for (int j = 0; j < i1; j++) {
                            if (list.size() > j) {
                                TerminalAppVersionStatistics s = list.get(j);
                                s.setVersionCumulativeRate(s.getVersionCumulativeRate().add(new BigDecimal("0.01")));
                            } else {
                                TerminalAppVersionStatistics s = list.get(j - 1);
                                s.setVersionCumulativeRate(s.getVersionCumulativeRate().add(new BigDecimal("0.01").multiply(BigDecimal.valueOf(i1 - j))));
                                break;
                            }
                        }
                    } else if (i < 0) {
                        list.sort(Comparator.comparing(EhTerminalAppVersionStatistics::getVersionCumulativeRate));
                        int i1 = sub.divide(new BigDecimal("0.01"), RoundingMode.FLOOR).negate().intValue();
                        for (int j = 0; j < i1; j++) {
                            if (list.size() > j) {
                                TerminalAppVersionStatistics s = list.get(j);
                                s.setVersionCumulativeRate(s.getVersionCumulativeRate().add(new BigDecimal("0.01").negate()));
                            } else {
                                TerminalAppVersionStatistics s = list.get(j - 1);
                                s.setVersionCumulativeRate(s.getVersionCumulativeRate().add(new BigDecimal("0.01").multiply(BigDecimal.valueOf(i1 - j)).negate()));
                                break;
                            }
                        }
                    }
                });

        statList.stream()
                .map(EhTerminalAppVersionStatistics::getVersionActiveRate)
                .filter(r -> r.compareTo(BigDecimal.ZERO) == 0)
                .reduce(BigDecimal::add)
                .ifPresent(r -> {
                    List<TerminalAppVersionStatistics> list =
                            statList.stream()
                                    .filter(rr -> rr.getVersionActiveRate().compareTo(BigDecimal.ZERO) != 0)
                                    .collect(Collectors.toList());

                    if (r.compareTo(BigDecimal.ZERO) == 0) {
                        return;
                    }

                    BigDecimal sub = new BigDecimal("100").subtract(r);
                    int i = sub.compareTo(BigDecimal.ZERO);
                    if (i > 0) {
                        list.sort((o1, o2) -> o2.getVersionActiveRate().compareTo(o1.getVersionActiveRate()));
                        int i1 = sub.divide(new BigDecimal("0.01"), RoundingMode.FLOOR).intValue();
                        for (int j = 0; j < i1; j++) {
                            if (list.size() > j) {
                                TerminalAppVersionStatistics s = list.get(j);
                                s.setVersionActiveRate(s.getVersionActiveRate().add(new BigDecimal("0.01")));
                            } else {
                                TerminalAppVersionStatistics s = list.get(j - 1);
                                s.setVersionActiveRate(s.getVersionActiveRate().add(new BigDecimal("0.01").multiply(BigDecimal.valueOf(i1-j))));
                                break;
                            }
                        }
                    } else if (i < 0) {
                        list.sort(Comparator.comparing(EhTerminalAppVersionStatistics::getVersionActiveRate));
                        int i1 = sub.divide(new BigDecimal("0.01"), RoundingMode.FLOOR).negate().intValue();
                        for (int j = 0; j < i1; j++) {
                            if (list.size() > j) {
                                TerminalAppVersionStatistics s = list.get(j);
                                s.setVersionActiveRate(s.getVersionActiveRate().add(new BigDecimal("0.01").negate()));
                            } else {
                                TerminalAppVersionStatistics s = list.get(j - 1);
                                s.setVersionActiveRate(s.getVersionActiveRate().add(new BigDecimal("0.01").multiply(BigDecimal.valueOf(i1-j)).negate()));
                                break;
                            }
                        }
                    }
                });
    }

    private BigDecimal rate(Number num1, Number num2) {
        if (num1.doubleValue() == 0d || num2.doubleValue() == 0d) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(num1.longValue())
                .divide(BigDecimal.valueOf(num2.longValue()), 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }
}
