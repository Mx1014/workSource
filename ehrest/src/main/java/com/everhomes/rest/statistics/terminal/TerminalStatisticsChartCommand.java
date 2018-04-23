package com.everhomes.rest.statistics.terminal;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>namespaceId: namespaceId</li>
 *     <li>dates: 日期列表 e.g: 20180101</li>
 * </ul>
 */
public class TerminalStatisticsChartCommand {

    private Integer namespaceId;
    @ItemType(String.class)
    private List<String> dates;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public List<String> getDates() {
        return dates;
    }

    public void setDates(List<String> dates) {
        this.dates = dates;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
