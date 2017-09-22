package com.everhomes.rest.address;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * <ul>
 *     <li>apartments: 参考{@link com.everhomes.rest.address.ApartmentAbstractDTO}</li>
 *     <li>nextPageAnchor: 下一页锚点</li>
 * </ul>
 * Created by ying.xiong on 2017/8/18.
 */
public class ListApartmentsResponse {

    private Long nextPageAnchor;

    @ItemType(ApartmentAbstractDTO.class)
    private List<ApartmentAbstractDTO> apartments;

    public List<ApartmentAbstractDTO> getApartments() {
        return apartments;
    }

    public void setApartments(List<ApartmentAbstractDTO> apartments) {
        this.apartments = apartments;
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }
}
