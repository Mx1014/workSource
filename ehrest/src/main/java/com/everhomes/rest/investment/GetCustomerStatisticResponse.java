package com.everhomes.rest.investment;

import java.util.List;

public class GetCustomerStatisticResponse {

    private List<CustomerStatisticsDTO> dtos;

    private Long nextPageAnchor;

    public List<CustomerStatisticsDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<CustomerStatisticsDTO> dtos) {
        this.dtos = dtos;
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }
}
