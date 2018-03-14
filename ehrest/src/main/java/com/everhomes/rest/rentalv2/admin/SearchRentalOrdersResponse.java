package com.everhomes.rest.rentalv2.admin;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.rentalv2.RentalOrderDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * @author sw on 2018/1/8.
 */
public class SearchRentalOrdersResponse {
    private Long nextPageAnchor;
    @ItemType(RentalOrderDTO.class)
    private List<RentalOrderDTO> rentalBills;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public List<RentalOrderDTO> getRentalBills() {
        return rentalBills;
    }

    public void setRentalBills(List<RentalOrderDTO> rentalBills) {
        this.rentalBills = rentalBills;
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }
}
