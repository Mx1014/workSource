package com.everhomes.rest.investment;

import java.util.List;

public class GetCustomerStatisticResponse {

    private List<CustomerStatisticsDTO> dtos;

    private Integer nextPageAnchor;

    public List<CustomerStatisticsDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<CustomerStatisticsDTO> dtos) {
        this.dtos = dtos;
    }

    public Integer getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Integer netPageAnchor) {
        this.nextPageAnchor = netPageAnchor;
    }
}
