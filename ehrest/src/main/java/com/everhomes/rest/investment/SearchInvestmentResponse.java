package com.everhomes.rest.investment;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.customer.EnterpriseCustomerDTO;
import com.everhomes.util.StringHelper;

import java.util.List;


public class SearchInvestmentResponse {
    @ItemType(EnterpriseCustomerDTO.class)
    private List<EnterpriseCustomerDTO> dtos;
    @ItemType(InvestmentStatisticsDTO.class)
    private List<InvestmentStatisticsDTO> stastics;

    private Long nextPageAnchor;

    public List<EnterpriseCustomerDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<EnterpriseCustomerDTO> dtos) {
        this.dtos = dtos;
    }

    public List<InvestmentStatisticsDTO> getStastics() {
        return stastics;
    }

    public void setStastics(List<InvestmentStatisticsDTO> stastics) {
        this.stastics = stastics;
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
