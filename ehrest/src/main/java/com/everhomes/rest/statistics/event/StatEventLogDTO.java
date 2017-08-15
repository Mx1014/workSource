// @formatter:off
package com.everhomes.rest.statistics.event;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.math.BigDecimal;
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
    private BigDecimal val;
    private String version;
    @ItemType(String.class)
    private Map<String, String> param;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public Long getDeviceTime() {
        return deviceTime;
    }

    public void setDeviceTime(Long deviceTime) {
        this.deviceTime = deviceTime;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Integer getAcc() {
        return acc;
    }

    public void setAcc(Integer acc) {
        this.acc = acc;
    }

    public Map<String, String> getParam() {
        return param;
    }

    public void setParam(Map<String, String> param) {
        this.param = param;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public BigDecimal getVal() {
        return val;
    }

    public void setVal(BigDecimal val) {
        this.val = val;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
