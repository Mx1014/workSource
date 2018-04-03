package com.everhomes.rest.statistics.terminal;

/**
 * <ul>
 *     <li>namespaceId: namespaceId</li>
 *     <li>taskNo: 时间</li>
 *     <li>status: 状态</li>
 * </ul>
 */
public class TerminalStatisticsTaskDTO {

    private Integer namespaceId;
    private String taskNo;
    private Byte status;

    public String getTaskNo() {
        return taskNo;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }
}
