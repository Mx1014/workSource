//@formatter:off
package com.everhomes.rest.asset;


import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.Date;
import java.util.List;

/**
 *<ul>
 * <li>ownerType:所属者type</li>
 * <li>ownerId:所属者id</li>
 * <li>dateStr:账期</li>
 * <li>addressId:楼栋门牌id</li>
 * <li>targetType:客户类别</li>
 * <li>targetId:客户id</li>
 * <li>noticeTel:催缴电话</li>
 * <li>targetName:客户名称</li>
 * <li>billGroupDTOList:账单组列表，参考{@link com.everhomes.rest.asset.BillGroupDTO}</li>
 * <li>isSettled:是否是已出账单,1:新增已出账单;0:新增未出账单</li>
 *</ul>
 */
public class CreateBillCommand {
    private String ownerType;
    private String ownerId;
    private Date dateStr;
    private Long addressId;
    private String noticeTel;
    private String targetName;
    @ItemType(BillGroupDTO.class)
    private List<BillGroupDTO> billGroupDTOList;
    private Byte isSettled;

    public CreateBillCommand() {
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public String getOwnerType() {
        return ownerType;
    }

    public Byte getIsSettled() {
        return isSettled;
    }

    public void setIsSettled(Byte isSettled) {
        this.isSettled = isSettled;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public Date getDateStr() {
        return dateStr;
    }

    public void setDateStr(Date dateStr) {
        this.dateStr = dateStr;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public List<BillGroupDTO> getBillGroupDTOList() {
        return billGroupDTOList;
    }

    public void setBillGroupDTOList(List<BillGroupDTO> billGroupDTOList) {
        this.billGroupDTOList = billGroupDTOList;
    }

    public String getNoticeTel() {
        return noticeTel;
    }

    public void setNoticeTel(String noticeTel) {
        this.noticeTel = noticeTel;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }


}
