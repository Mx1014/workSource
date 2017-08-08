//@formatter:off
package com.everhomes.rest.asset;


import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 *<ul>
 * <li>billGroupId:账单组id</li>
 * <li>targetId:客户id</li>
 * <li>targetType:客户type</li>
 * <li>billGroupDTOList:账单组列表，参考{@link com.everhomes.rest.asset.BillGroupDTO}</li>
 *</ul>
 */
public class ModifyNotSettledBillCommand {
    private Long billGroupId;
    private Long targetId;
    @ItemType(BillGroupDTO.class)
    private List<BillGroupDTO> billGroupDTOList;

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

    public List<BillGroupDTO> getBillGroupDTOList() {
        return billGroupDTOList;
    }

    public void setBillGroupDTOList(List<BillGroupDTO> billGroupDTOList) {
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
