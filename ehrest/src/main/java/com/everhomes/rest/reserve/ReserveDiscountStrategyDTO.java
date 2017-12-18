package com.everhomes.rest.reserve;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * @author sw on 2017/12/18.
 */
public class ReserveDiscountStrategyDTO {

    @ItemType(ReserveOrgmemberDTO.class)
    private List<ReserveOrgmemberDTO> orgmemberDTOS;

    private Double discount;

    public List<ReserveOrgmemberDTO> getOrgmemberDTOS() {
        return orgmemberDTOS;
    }

    public void setOrgmemberDTOS(List<ReserveOrgmemberDTO> orgmemberDTOS) {
        this.orgmemberDTOS = orgmemberDTOS;
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
