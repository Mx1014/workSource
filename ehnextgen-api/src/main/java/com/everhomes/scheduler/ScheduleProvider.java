// @formatter:off
package com.everhomes.scheduler;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.everhomes.rest.scheduler.ScheduleJobInfoDTO;


public interface ScheduleProvider {
    static final String DEFAULT_GROUP = "scheduler_group";
    
    /**
     * 用于在指定时间执行指定job
     * @param triggerName trigger的标识
     * @param jobName job的标识
     * @param startTime 要执行job的时间
     * @param jobClassName job要执行的类的名称
     * @param parameters 要传到job中的参数
     */
    @SuppressWarnings("rawtypes")
    void scheduleSimpleJob(String triggerName, String jobName, Date startTime, String jobClassName, Map<String, Object> parameters);
    
    /**
     * 用于在指定时间执行指定job
     * @param triggerName trigger的标识
     * @param jobName job的标识
     * @param startTime 要执行job的时间
     * @param jobClass job要执行的类
     * @param parameters 要传到job中的参数
     */
    @SuppressWarnings("rawtypes")
    void scheduleSimpleJob(String triggerName, String jobName, Date startTime, Class jobClass, Map<String, Object> parameters);
    
    /**
     * 按指定间隔、指定次数、重复执行指定job
     * @param triggerName trigger的标识
     * @param jobName job的标识
     * @param startTime 要执行job的时间
     * @param msInterval 间隔，单位：毫秒
     * @param repeatCount 重复次数，0表示永远重复
     * @param jobClassName job要执行的类的名称
     * @param parameters 要传到job中的参数
     */
    @SuppressWarnings("rawtypes")
    void scheduleRepeatJob(String triggerName, String jobName, Date startTime, long msInterval, int repeatCount, 
        String jobClassName, Map<String, Object> parameters);
    
    /**
     * 按指定间隔、指定次数、重复执行指定job
     * @param triggerName trigger的标识
     * @param jobName job的标识
     * @param startTime 要执行job的时间
     * @param msInterval 间隔，单位：毫秒
     * @param repeatCount 重复次数，0表示永远重复
     * @param jobClass job要执行的类
     * @param parameters 要传到job中的参数
     */
    @SuppressWarnings("rawtypes")
    void scheduleRepeatJob(String triggerName, String jobName, Date startTime, long msInterval, int repeatCount, 
        Class jobClass, Map<String, Object> parameters);
    
    /**
     * <p>使用周期表达式来调度指定job。</p>
     * @param triggerName trigger的标识
     * @param jobName job的标识
     * @param cronExpression 周期表达式，与linux的cron表达式一致
     * @param jobClassName job要执行的类的名称
     * @param parameters 要传到job中的参数
     */
    @SuppressWarnings("rawtypes")
    void scheduleCronJob(String triggerName, String jobName, String cronExpression, String jobClassName, Map<String, Object> parameters);
    
    /**
     * <p>使用周期表达式来调度指定job。</p>
     * <p>周期表达式可参考{@link org.quartz.CronExpression}里的说明，下面是从里面摘录出来的说明：</p>
     * <table cellspacing="8">
     * <tbody>
     * <tr><th>Field Name</th><th>Allowed Values</th><th>Allowed Special Characters</th></tr>
     * <tr><td>Seconds</td><td>0-59</td><td>, - * /</td></tr>
     * <tr><td>Minutes</td><td>0-59</td><td>, - * /</td></tr>
     * <tr><td>Hours</td><td>0-23</td><td>, - * /</td></tr>
     * <tr><td>Day-of-month</td><td>1-31</td><td>, - * ? / L W</td></tr>
     * <tr><td>Month</td><td>1-12 or JAN-DEC</td><td>, - * /</td></tr>
     * <tr><td>Day-of-Week</td><td>1-7 or SUN-SAT</td><td>, - * ? / L #</td></tr>
     * <tr><td>Year (Optional)</td><td>empty, 1970-2199</td><td>, - * /</td></tr>
     * </tbody>
     * </table>
     * 
     * <p>The '*' character is used to specify all values. For example, "*" in the minute field means "every minute".<p>
     * <p>The '?' character is allowed for the day-of-month and day-of-week fields. It is used to specify 'no specific value'. This is useful when you need to specify something in one of the two fields, but not the other.<p>
     * <p>The '-' character is used to specify ranges For example "10-12" in the hour field means "the hours 10, 11 and 12".<p>
     * <p>The ',' character is used to specify additional values. For example "MON,WED,FRI" in the day-of-week field means "the days Monday, Wednesday, and Friday".<p>
     * <p>The '/' character is used to specify increments. For example "0/15" in the seconds field means "the seconds 0, 15, 30, and 45". And "5/15" in the seconds field means "the seconds 5, 20, 35, and 50". Specifying '*' before the '/' is equivalent to specifying 0 is the value to start with. Essentially, for each field in the expression, there is a set of numbers that can be turned on or off. For seconds and minutes, the numbers range from 0 to 59. For hours 0 to 23, for days of the month 0 to 31, and for months 1 to 12. The "/" character simply helps you turn on every "nth" value in the given set. Thus "7/6" in the month field only turns on month "7", it does NOT mean every 6th month, please note that subtlety.<p>
     * <p>The 'L' character is allowed for the day-of-month and day-of-week fields. This character is short-hand for "last", but it has different meaning in each of the two fields. For example, the value "L" in the day-of-month field means "the last day of the month" - day 31 for January, day 28 for February on non-leap years. If used in the day-of-week field by itself, it simply means "7" or "SAT". But if used in the day-of-week field after another value, it means "the last xxx day of the month" - for example "6L" means "the last friday of the month". You can also specify an offset from the last day of the month, such as "L-3" which would mean the third-to-last day of the calendar month. When using the 'L' option, it is important not to specify lists, or ranges of values, as you'll get confusing/unexpected results.<p>
     * <p>The 'W' character is allowed for the day-of-month field. This character is used to specify the weekday (Monday-Friday) nearest the given day. As an example, if you were to specify "15W" as the value for the day-of-month field, the meaning is: "the nearest weekday to the 15th of the month". So if the 15th is a Saturday, the trigger will fire on Friday the 14th. If the 15th is a Sunday, the trigger will fire on Monday the 16th. If the 15th is a Tuesday, then it will fire on Tuesday the 15th. However if you specify "1W" as the value for day-of-month, and the 1st is a Saturday, the trigger will fire on Monday the 3rd, as it will not 'jump' over the boundary of a month's days. The 'W' character can only be specified when the day-of-month is a single day, not a range or list of days.<p>
     * <p>The 'L' and 'W' characters can also be combined for the day-of-month expression to yield 'LW', which translates to "last weekday of the month".<p>
     * <p>The '#' character is allowed for the day-of-week field. This character is used to specify "the nth" XXX day of the month. For example, the value of "6#3" in the day-of-week field means the third Friday of the month (day 6 = Friday and "#3" = the 3rd one in the month). Other examples: "2#1" = the first Monday of the month and "4#5" = the fifth Wednesday of the month. Note that if you specify "#5" and there is not 5 of the given day-of-week in the month, then no firing will occur that month. If the '#' character is used, there can only be one expression in the day-of-week field ("3#1,6#3" is not valid, since there are two expressions).<p>
     * <p>The legal characters and the names of months and days of the week are not case sensitive.<p> 
     * <table cellspacing="8">
     * <tbody>
     * <tr><th>Expression</th><th>Meaning</th></tr>
     * <tr><td>"0 0 12 * * ?"</td><td>Fire at 12pm (noon) every day</td></tr>
     * <tr><td>"0 15 10 ? * *"</td><td>Fire at 10:15am every day</td></tr>
     * <tr><td>"0 15 10 * * ?"</td><td>Fire at 10:15am every day</td></tr>
     * <tr><td>"0 15 10 * * ? *"</td><td>Fire at 10:15am every day</td></tr>
     * <tr><td>"0 15 10 * * ? 2005"</td><td>Fire at 10:15am every day during the year 2005</td></tr>
     * <tr><td>"0 * 14 * * ?"</td><td>Fire every minute starting at 2pm and ending at 2:59pm, every day</td></tr>
     * <tr><td>"0 0/5 14 * * ?"</td><td>Fire every 5 minutes starting at 2pm and ending at 2:55pm, every day</td></tr>
     * <tr><td>"0 0/5 14,18 * * ?"</td><td>Fire every 5 minutes starting at 2pm and ending at 2:55pm, AND fire every 5 minutes starting at 6pm and ending at 6:55pm, every day</td></tr>
     * <tr><td>"0 0-5 14 * * ?"</td><td>Fire every minute starting at 2pm and ending at 2:05pm, every day</td></tr>
     * <tr><td>"0 10,44 14 ? 3 WED"</td><td>Fire at 2:10pm and at 2:44pm every Wednesday in the month of March.</td></tr>
     * <tr><td>"0 15 10 ? * MON-FRI"</td><td>Fire at 10:15am every Monday, Tuesday, Wednesday, Thursday and Friday</td></tr>
     * <tr><td>"0 15 10 15 * ?"</td><td>Fire at 10:15am on the 15th day of every month</td></tr>
     * <tr><td>"0 15 10 L * ?"</td><td>Fire at 10:15am on the last day of every month</td></tr>
     * <tr><td>"0 15 10 ? * 6L"</td><td>Fire at 10:15am on the last Friday of every month</td></tr>
     * <tr><td>"0 15 10 ? * 6L"</td><td>Fire at 10:15am on the last Friday of every month</td></tr>
     * <tr><td>"0 15 10 ? * 6L 2002-2005"</td><td>Fire at 10:15am on every last Friday of every month during the years 2002, 2003, 2004 and 2005</td></tr>
     * <tr><td>"0 15 10 ? * 6#3"</td><td>Fire at 10:15am on the third Friday of every month</td></tr>
     * </tbody>
     * </table>
     * 
     * @param triggerName trigger的标识
     * @param jobName job的标识
     * @param cronExpression 周期表达式，与linux的cron表达式一致
     * @param jobClass job要执行的类
     * @param parameters 要传到job中的参数
     */
    @SuppressWarnings("rawtypes")
    void scheduleCronJob(String triggerName, String jobName, String cronExpression, Class jobClass, Map<String, Object> parameters);
    
    /**
     * 检查job是否在调度
     * @param triggerName trigger的标识
     * @param jobName job的标识
     * @return 如果job存在则返回true，否则返回false
     */
    boolean checkExist(String triggerName, String jobName);
    
    /**
     * 取消job的调度
     * @param triggerName trigger的标识
     * @return 如果取消成功则返回true，否则返回false
     */
    boolean unscheduleJob(String triggerName);
    
    /**
     * 列出调度的job信息列表 
     * @return job信息列表 
     */
    List<ScheduleJobInfoDTO> listScheduleJobs();

    Byte getRunningFlag();

    void setRunningFlag(Byte runningFlag);
}
