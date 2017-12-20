package com.everhomes.rest.reserve;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>discountUserDTO: 折扣人员</li>
 * <li>discount: 折扣</li>
 * </ul>
 */
public class ReserveDiscountRuleDTO {

    @ItemType(ReserveDiscountUserDTO.class)
    private List<ReserveDiscountUserDTO> discountUserDTO;

    private Double discount;

    public List<ReserveDiscountUserDTO> getDiscountUserDTO() {
        return discountUserDTO;
    }

    public void setDiscountUserDTO(List<ReserveDiscountUserDTO> discountUserDTO) {
        this.discountUserDTO = discountUserDTO;
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
