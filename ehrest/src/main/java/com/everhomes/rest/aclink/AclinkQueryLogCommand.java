// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerId:所属组织Id</li>
 * <li>ownerType：所属组织类型 0小区 1企业 2家庭{@link com.everhomes.rest.aclink.DoorAccessOwnerType}</li>
 * <li>keyWord:操作用户 可输入姓名或手机号码</li>
 * <li>eventType:开门方式 null 全部 ,0 蓝牙开门, 1 二维码开门,2 远程开门,3 人脸开门</li>
 * <li>doorId:门禁名称</li>
 * <li>startTime:开门时间区间起点 yyyy-MM-dd</li>
 * <li>endTime:开门时间区间终点 yyyy-MM-dd ,只允许选择当前时间之前的日期区间</li>
 * <li>pageAnchor:锚点</li>
 * <li>pageSize:分页大小</li>
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
    public void set(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }
    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
