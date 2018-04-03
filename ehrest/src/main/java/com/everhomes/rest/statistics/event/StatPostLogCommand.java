// @formatter:off
package com.everhomes.rest.statistics.event;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>logs: 事件json字符串数组</li>
 * </ul>
 */
public class StatPostLogCommand {

    @ItemType(String.class)
    private List<String> logs;

    public List<String> getLogs() {
        return logs;
    }

    public void setLogs(List<String> logs) {
        this.logs = logs;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
