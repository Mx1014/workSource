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
 * <li>targetType:客户类别</li>
 * <li>targetId:客户id</li>
 * <li>contractNum:合同编号</li>
 * <li>noticeTel:催缴电话</li>
 * <li>targetName:客户名称</li>
 * <li>billGroupDTO:账单组数据，参考{@link com.everhomes.rest.asset.BillGroupDTO}</li>
 * <li>isSettled:是否是已出账单,1:新增已出账单;0:新增未出账单</li>
 *</ul>
 */
public class CreateBillCommand {
    private String ownerType;
    private Long ownerId;
    private String noticeTel;
    private String targetName;
    private String targetType;
    private Long targetId;
    private String contractNum;
    private String dateStr;
    @ItemType(BillGroupDTO.class)
    private BillGroupDTO billGroupDTO;
    private Byte isSettled;

    public CreateBillCommand() {
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public String getContractNum() {
        return contractNum;
    }

    public void setContractNum(String contractNum) {
        this.contractNum = contractNum;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
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

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public BillGroupDTO getBillGroupDTO() {
        return billGroupDTO;
    }

    public void setBillGroupDTO(BillGroupDTO billGroupDTO) {
        this.billGroupDTO = billGroupDTO;
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
