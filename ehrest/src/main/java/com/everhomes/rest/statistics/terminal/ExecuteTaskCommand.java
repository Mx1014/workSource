package com.everhomes.rest.statistics.terminal;

/**
 * <ul>
 *     <li>startDate: startDate</li>
 *     <li>endDate: endDate</li>
 *     <li>namespaceId: namespaceId</li>
 * </ul>
 */
public class ExecuteTaskCommand {

    private String startDate;
    private String endDate;
    private Integer namespaceId;

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
}
