//@formatter:off
package com.everhomes.rest.asset;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.util.Date;

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
 *</ul>
 */
public class OneKeyNoticeCommand {
    @NotNull
    private Long ownerId;
    private Long addressId;
    @NotNull
    private String ownerType;
    private Date dateStrBegin;
    private Date dateStrEnd;
    private Byte billStatus;
    private String targetName;
    private String billGroupName;

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

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Date getDateStrBegin() {
        return dateStrBegin;
    }

    public void setDateStrBegin(Date dateStrBegin) {
        this.dateStrBegin = dateStrBegin;
    }

    public Date getDateStrEnd() {
        return dateStrEnd;
    }

    public void setDateStrEnd(Date dateStrEnd) {
        this.dateStrEnd = dateStrEnd;
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

    public OneKeyNoticeCommand() {

    }
}
