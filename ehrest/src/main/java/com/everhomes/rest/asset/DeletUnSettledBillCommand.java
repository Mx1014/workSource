//@formatter:off
package com.everhomes.rest.asset;


import com.everhomes.util.StringHelper;

/**
 *<ul>
 * <li>billGroupId:账单组id</li>
 * <li>targetId:所属者id</li>
 * <li>targetType:所属者类型</li>
 * <li>dateStr:账期</li>
 *</ul>
 */
public class DeletUnSettledBillCommand {
    private Long billGroupId;
    private Long targetId;
    private String targetType;
    private String dateStr;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Long getBillGroupId() {
        return billGroupId;
    }

    public void setBillGroupId(Long billGroupId) {
        this.billGroupId = billGroupId;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public DeletUnSettledBillCommand() {

    }
}
