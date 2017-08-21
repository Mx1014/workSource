//@formatter:off
package com.everhomes.rest.asset;

import com.everhomes.util.StringHelper;

/**
 * Created by Wentian Wang on 2017/8/14.
 */

/**
 *<ul>
 * <li>dateStr:账期</li>
 * <li>OwnerId:所属者ID</li>
 * <li>OwnerType:所属者类型</li>
 * <li>targetType:客户类型</li>
 * <li>targetId:客户id</li>
 *</ul>
 */
public class ListBillDetailOnDateChangeCommand {
    private String dateStr;
    private Long OwnerId;
    private String OwnerType;
    private String targetType;
    private Long targetId;

    public ListBillDetailOnDateChangeCommand() {
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public Long getOwnerId() {
        return OwnerId;
    }

    public void setOwnerId(Long ownerId) {
        OwnerId = ownerId;
    }

    public String getOwnerType() {
        return OwnerType;
    }

    public void setOwnerType(String ownerType) {
        OwnerType = ownerType;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }
}
