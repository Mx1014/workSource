package com.everhomes.statistics.event;

import com.everhomes.namespace.Namespace;
import com.everhomes.rest.statistics.event.StatEventStatTimeInterval;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Created by xq.tian on 2017/7/31.
 */
public interface StatEventHandler {

    /**
     * 返回该处理器支持的事件名称
     */
    String getEventName();

    /**
     * 事件统计函数, 在这里处理指定参数数据范围的事件记录
     *
     * 事件记录在 eh_stat_event_logs 表中
     * 事件参数在 eh_stat_event_param_logs 表中
     *
     * 这个接口主要是从上面的两张表中取出数据，然后按照需求统计好，然后保存到自己的结果表中。
     *
     * @param namespace 指定的域空间
     * @param statEvent 指定的事件
     * @param statTime  指定的日期
     * @param interval  指定的统计结果的时间区间类型, 目前 DAILY 类型
     */
    void processStat(Namespace namespace, StatEvent statEvent, LocalDate statTime, StatEventStatTimeInterval interval);

    /**
     * 用于将从客户端上传上来的事件记录参数列表转换成 param log
     * @param log   事件记录
     * @param param 客户端或者 web 端上传的事件参数字典
     * @return  返回 param log 对象, 最终会被保存到数据库
     */
    List<StatEventParamLog> processEventParamLogs(StatEventLog log, Map<String, String> param);
}
