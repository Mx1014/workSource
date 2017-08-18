//@formatter:off
package com.everhomes.rest.asset;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 *<ul>
 * <li>ownerId: 所属者id</li>
 * <li>ownerType: 所属者type</li>
 * <li>addressId: 楼栋门牌id</li>
 * <li>pageSize: 显示数量</li>
 * <li>pageAnchor: 下页锚点</li>
 * <li>begin: 账期开始</li>
 * <li>end: 账期结束</li>
 * <li>targetName: 客户名称</li>
 * <li>billGroupName: 账单名称</li>
 * <li>billStatus: 账单状态,0:未缴;1:已缴</li>
 * <li>pageAnchor:锚点</li>
 * <li>pageSize:每页数量</li>
 * <li>billGroupId:账单组id</li>
 * <li>addressName:门牌楼栋名称</li>
 * <li>status:账单属性，0:未出账单;1:已出账单</li>
 *</ul>
 */
public class ListBillsCommand {
    @NotNull
    private Long ownerId;
    private Long addressId;
    @NotNull
    private String ownerType;
    private Integer pageSize;
    private Long pageAnchor;
    private String dateStrBegin;
    private String dateStrEnd;
    private Byte billStatus;
    private String targetName;
    private String billGroupName;

    private Long billGroupId;
    private String addressName;
    private Byte status;

    public Long getBillGroupId() {
        return billGroupId;
    }

    public void setBillGroupId(Long billGroupId) {
        this.billGroupId = billGroupId;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public Byte getBillStatus() {
        return billStatus;
    }


    public void setBillStatus(Byte billStatus) {
        this.billStatus = billStatus;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getBillGroupName() {
        return billGroupName;
    }

    public void setBillGroupName(String billGroupName) {
        this.billGroupName = billGroupName;
    }

    public String getDateStrBegin() {
        return dateStrBegin;
    }

    public void setDateStrBegin(String dateStrBegin) {
        this.dateStrBegin = dateStrBegin;
    }

    public String getDateStrEnd() {
        return dateStrEnd;
    }

    public void setDateStrEnd(String dateStrEnd) {
        this.dateStrEnd = dateStrEnd;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public ListBillsCommand() {

    }
}
