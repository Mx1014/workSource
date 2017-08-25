// @formatter:off
package com.everhomes.statistics.event;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public interface StatEventParamLogProvider {

	void createStatEventParamLog(StatEventParamLog statEventParamLog);

	void updateStatEventParamLog(StatEventParamLog statEventParamLog);

	StatEventParamLog findStatEventParamLogById(Long id);

    List<StatEventParamLog> findStatEventParamByEventLogId(Long eventLogId);

    /**
     * 返回值是传入的paramKey的参数值与发生次数的映射
     * 比如传入的eventName是banner点击事件,输出结果是：{"1": 100, "2": 200}, 代表了id为1的banner被点击了100次,id为2的banner被点击了200次
     * @param namespaceId
     * @param eventName
     * @param eventVersion
     *@param paramKey
     * @param minTime
     * @param maxTime    @return
     */
    Map<String, Integer> countParamTotalCount(Integer namespaceId, String eventName, String eventVersion, String paramKey, Timestamp minTime, Timestamp maxTime);

    /**
     * 根据eventName查询出来参数日志
     * @param namespaceId
     * @param eventName
     * @param eventVersion
     *@param minTime
     * @param maxTime   @return
     */
    List<StatEventParamLog> listEventParamLog(Integer namespaceId, String eventName, String eventVersion, Timestamp minTime, Timestamp maxTime);

    /*
     SELECT
	    subT.v1, subT.v2, COUNT(*)
        FROM
            (SELECT
                 aa.id, aa.event_name, aa.param_key AS p1, aa.string_value AS v1, tt.param_key AS p2, tt.string_value AS v2
             FROM eh_stat_event_param_logs aa
                 JOIN (SELECT
                           id, param_key, string_value, event_log_id
                       FROM eh_stat_event_param_logs
                       WHERE event_name = 'launchpad_on_news_flash_item_click' AND param_key = 'layoutId') AS tt
                     ON tt.event_log_id = aa.event_log_id
             WHERE aa.event_name = 'launchpad_on_news_flash_item_click' AND aa.param_key = 'newsToken') AS subT
        GROUP BY subT.v1, subT.v2;
    */
    Map<Map<String, String>, Integer> countParamTotalCount(Integer namespaceId, String eventName, String eventVersion, List<String> paramKeys, Timestamp minTime, Timestamp maxTime);

    /**
     * 统计参数的独立session数
     * @param namespaceId
     * @param eventName
     * @param eventVersion
     * @param paramKey
     * @param minTime
     * @param maxTime
     * @return
     */
    Map<String,Integer> countDistinctSession(Integer namespaceId, String eventName, String eventVersion, String paramKey, Timestamp minTime, Timestamp maxTime);

    /**
     * 统计参数的独立uid数
     * @param namespaceId
     * @param eventName
     * @param eventVersion
     * @param paramKey
     * @param minTime
     * @param maxTime
     * @return
     */
    Map<String,Integer> countDistinctUid(Integer namespaceId, String eventName, String eventVersion, String paramKey, Timestamp minTime, Timestamp maxTime);

    // List<StatEventParamLog> listStatEventParamLog();

}