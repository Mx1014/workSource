//@formatter:off
package com.everhomes.rest.asset;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 *<ul>
 * <li>ownerId: 所属者id</li>
 * <li>ownerType: 所属者type</li>
 * <li>addressId: 楼栋id</li>
 * <li>addressName: 楼栋门牌</li>
 * <li>pageSize: 显示数量</li>
 * <li>pageAnchor: 下页锚点</li>
 * <li>targetName: 客户名称</li>
 * <li>billGroupName: 账单组名称</li>
 * <li>billGroupId: 账单组Id</li>
 *</ul>
 */
public class ListNotSettledBillCommand {
    @NotNull
    private Long ownerId;
    @NotNull
    private String ownerType;
    private Long addressId;
    private String addressName;
    private Integer pageSize;
    private Long pageAnchor;
    private String targetName;
    private String billGroupName;
    private Long billGroupId;


    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public Long getBillGroupId() {
        return billGroupId;
    }

    public void setBillGroupId(Long billGroupId) {
        this.billGroupId = billGroupId;
    }

    public String getBillGroupName() {
        return billGroupName;
    }

    public void setBillGroupName(String billGroupName) {
        this.billGroupName = billGroupName;
    }


    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
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

    public ListNotSettledBillCommand() {

    }
}
