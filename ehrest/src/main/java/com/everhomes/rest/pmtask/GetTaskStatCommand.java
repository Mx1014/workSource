package com.everhomes.rest.pmtask;

/**
 * <ul>
 * <li>namespaceId: 域空间</li>
 * <li>ownerType: 所属类型</li>
 * <li>ownerId: 所属项目id</li>
 * <li>dateStart: 起始日期</li>
 * <li>dateEnd: 截止日期</li>
 * <li>exportType: 导出统计类型：1 导出按类型统计数据 2 导出按来源统计数据 3 导出按状态统计数据 4 导出按区域统计数据</li>
 * <li>appId: 应用id 6 物业报修（正中会为1） 9 投诉建议</li>
 * <li>currentPMId: 当前管理公司ID</li>
 * <li>originId: 当前模块ID</li>
 * </ul>
 */
public class GetTaskStatCommand {

    private Integer namespaceId;
    private String ownerType;
    private Long ownerId;
    private long dateStart;
    private long dateEnd;
    private Integer exportType;

    private Long appId;
    private Long currentPMId;
    private Long originId;

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }


    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public long getDateStart() {
        return dateStart;
    }

    public void setDateStart(long dateStart) {
        this.dateStart = dateStart;
    }

    public long getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(long dateEnd) {
        this.dateEnd = dateEnd;
    }

    public Integer getExportType() {
        return exportType;
    }

    public void setExportType(Integer exportType) {
        this.exportType = exportType;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public Long getCurrentPMId() {
        return currentPMId;
    }

    public void setCurrentPMId(Long currentPMId) {
        this.currentPMId = currentPMId;
    }

    public Long getOriginId() {
        return originId;
    }

    public void setOriginId(Long originId) {
        this.originId = originId;
    }
}
