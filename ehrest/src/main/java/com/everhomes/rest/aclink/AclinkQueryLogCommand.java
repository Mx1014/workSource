// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerId:所属组织Id</li>
 * <li>ownerType：所属组织类型 0小区 1企业 2家庭{@link com.everhomes.rest.aclink.DoorAccessOwnerType}</li>
 * <li>eventType:事件类型 null 全部 ,0 蓝牙开门, 1 二维码开门,2 远程开门,3 人脸开门</li>
 * <li>keyWord:关键字</li>
 * <li>doorId:门禁id</li>
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
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
