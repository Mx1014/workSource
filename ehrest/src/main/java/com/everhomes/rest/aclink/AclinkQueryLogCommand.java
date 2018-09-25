// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerId:所属组织Id</li>
 * <li>ownerType：所属组织类型 0小区 1企业 2家庭{@link com.everhomes.rest.aclink.DoorAccessOwnerType}</li>
 * <li>keyWord:操作用户 可输入姓名或手机号码</li>
 * <li>eventType:开门方式 null 全部 ,0 蓝牙开门, 1 二维码开门,2 远程开门,3 按键开门，4 人脸开门</li>
 * <li>doorId:门禁id</li>
 * <li>startTime:开门时间区间起点 时间戳，精确到日期</li>
 * <li>endTime:开门时间区间终点 时间戳,只允许选择当前时间之前的日期区间</li>
 * <li>pageAnchor:锚点</li>
 * <li>pageSize:分页大小</li>
 * <li>namespaceId: 域空间ID</li>
 * <li>currentPMId: 当前管理公司ID(organizationID)</li>
 * <li>currentProjectId: 当前选中项目Id，如果是全部则不传</li>
 * <li>appId: 应用id</li>
 * </ul>
 *
 */
public class AclinkQueryLogCommand {
    private Byte ownerType;
    private Long ownerId;
    private Long eventType;
    private String keyword;
    private Long pageAnchor;
    private Integer pageSize;
    private Long doorId;
    private Long startTime;
    private Long endTime;
    private String startStr;
    private String endStr;
    private Integer namespaceId;
    //权限
    private Long appId;
    private Long currentPMId;
    private Long currentProjectId;

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

    public Long getCurrentProjectId() {
        return currentProjectId;
    }

    public void setCurrentProjectId(Long currentProjectId) {
        this.currentProjectId = currentProjectId;
    }

    public Long getEventType() {
        return eventType;
    }
    public void setEventType(Long eventType) {
        this.eventType = eventType;
    }
    public String getKeyword() {
        return keyword;
    }
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
    public Long getPageAnchor() {
        return pageAnchor;
    }
    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
    }
    public Integer getPageSize() {
        return pageSize;
    }
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Byte getOwnerType() {
        return ownerType;
    }
    public void setOwnerType(Byte ownerType) {
        this.ownerType = ownerType;
    }
    public Long getOwnerId() {
        return ownerId;
    }
    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getDoorId() {
        return doorId;
    }
    public void setDoorId(Long doorId) {
        this.doorId = doorId;
    }

    public Long getStartTime() {
        return startTime;
    }
    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }
    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public String getStartStr() {
        return startStr;
    }
    public void setStartStr(String startStr) {
        this.startStr = startStr;
    }

    public String getEndStr() {
        return endStr;
    }
    public void setEndStr(String endStr) {
        this.endStr = endStr;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }
    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
