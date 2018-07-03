package com.everhomes.rest.customer;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * Created by Rui.Jia  2018/7/3 13 :36
 */

public class CustomerPotentialResponse {

    @ItemType(PotentialCustomerDTO.class)
    private List<PotentialCustomerDTO> dtos;
    private Long nextPageAnchor;

    public List<PotentialCustomerDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<PotentialCustomerDTO> dtos) {
        this.dtos = dtos;
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
