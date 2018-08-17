package com.everhomes.rest.statistics.terminal;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>namespaceId: namespaceId</li>
 *     <li>date: 日期 e.g: 20180101</li>
 * </ul>
 */
public class ListTerminalStatisticsByDayCommand {

    private Integer namespaceId;
    private String date;
    private String startDate;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
