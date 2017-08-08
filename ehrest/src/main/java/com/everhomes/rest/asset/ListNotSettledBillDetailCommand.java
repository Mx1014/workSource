//@formatter:off
package com.everhomes.rest.asset;


import com.everhomes.util.StringHelper;

/**
 *<ul>
 * <li>billGroupId:账单组id</li>
 * <li>dateStr:账期，格式为2017-06，参与排序</li>
 * <li>targetType:客户类型</li>
 * <li>targetId:客户id</li>
 * <li>addressName: 楼栋门牌名称</li>
 * <li>noticeTel:催缴电话</li>
 *</ul>
 */
public class ListNotSettledBillDetailCommand {
    private Long billGroupId;
    private String targetType;
    private Long targetId;
    private String noticeTel;
    private String dateStr;
    private String addressName;

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

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

    public String getNoticeTel() {
        return noticeTel;
    }

    public void setNoticeTel(String noticeTel) {
        this.noticeTel = noticeTel;
    }

    public ListNotSettledBillDetailCommand() {

    }
}
