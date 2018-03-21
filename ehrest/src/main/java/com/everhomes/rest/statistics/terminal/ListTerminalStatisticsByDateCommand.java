package com.everhomes.rest.statistics.terminal;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>namespaceId: namespaceId</li>
 *     <li>startDate: 开始时间日期, e.g:20180101</li>
 *     <li>endDate: 结束时间日期 e.g:20180101</li>
 * </ul>
 */
public class ListTerminalStatisticsByDateCommand {

    private Integer namespaceId;
    private String startDate;
    private String endDate;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
