//@formatter:off
package com.everhomes.rest.asset;


import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 *<ul>
 * <li>billId:账单id</li>
 * <li>billGroupId:账单组id</li>
 * <li>targetId:客户id</li>
 * <li>targetType:客户type</li>
 * <li>targetName:客户名称</li>
 * <li>billGroupDTO:账单组，参考{@link com.everhomes.rest.asset.BillGroupDTO}</li>
 *</ul>
 */
public class ModifyNotSettledBillCommand {
    private Long billId;
    private Long billGroupId;
    private Long targetId;
    private String targetType;
    private String targetName;
    @ItemType(BillGroupDTO.class)
    private BillGroupDTO billGroupDTOList;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }


    public Long getBillGroupId() {
        return billGroupId;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }

    public void setBillGroupId(Long billGroupId) {
        this.billGroupId = billGroupId;
    }

    public BillGroupDTO getBillGroupDTOList() {
        return billGroupDTOList;
    }

    public void setBillGroupDTOList(BillGroupDTO billGroupDTOList) {
        this.billGroupDTOList = billGroupDTOList;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public ModifyNotSettledBillCommand() {
    }
}
