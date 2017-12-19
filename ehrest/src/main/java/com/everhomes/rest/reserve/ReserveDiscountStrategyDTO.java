package com.everhomes.rest.reserve;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>orgMemberDTOS: 折扣人员</li>
 * <li>discount: 折扣</li>
 * </ul>
 */
public class ReserveDiscountStrategyDTO {

    @ItemType(ReserveOrgMemberDTO.class)
    private List<ReserveOrgMemberDTO> orgMemberDTOS;

    private Double discount;

    public List<ReserveOrgMemberDTO> getOrgMemberDTOS() {
        return orgMemberDTOS;
    }

    public void setOrgMemberDTOS(List<ReserveOrgMemberDTO> orgMemberDTOS) {
        this.orgMemberDTOS = orgMemberDTOS;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
