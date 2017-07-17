// @formatter:off
package com.everhomes.rest.statistics.event;

import com.everhomes.discover.ItemType;

import java.util.Map;

/**
 * <ul>
 *     <li>sessionId: sessionId</li>
 *     <li>logId: log id</li>
 *     <li>deviceTime: 设备时间</li>
 *     <li>eventName: 自定义的事件name</li>
 *     <li>acc: 事件发生次数, 默认为1</li>
 *     <li>val: 计算事件的value</li>
 *     <li>version: 版本</li>
 *     <li>param: 参数</li>
 * </ul>
 */
public class StatEventLogDTO {

    private String sessionId;
    private Long logId;
    private Long deviceTime;
    private String eventName;
    private Integer acc;
    private Integer val;
    private Integer version;
    @ItemType(String.class)
    private Map<String, String> param;

}
