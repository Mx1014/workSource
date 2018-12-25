package com.everhomes.rest.investment;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.customer.EnterpriseCustomerDTO;
import com.everhomes.util.StringHelper;

import java.util.List;


/**
 * <ul>
 *     <li>dtos: dtos {@link com.everhomes.rest.customer.EnterpriseCustomerDTO}</li>
 *     <li>stastics: stastics {@link InvitedCustomerStatisticsDTO}</li>
 *     <li>nextPageAnchor: nextPageAnchor</li>
 * </ul>
 */
public class SearchInvestmentResponse {
    @ItemType(EnterpriseCustomerDTO.class)
    private List<EnterpriseCustomerDTO> dtos;
    @ItemType(InvitedCustomerStatisticsDTO.class)
    private List<InvitedCustomerStatisticsDTO> statistics;

    private Long nextPageAnchor;

    private Long totalNum;

    public Long getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Long totalNum) {
        this.totalNum = totalNum;
    }

    public List<EnterpriseCustomerDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<EnterpriseCustomerDTO> dtos) {
        this.dtos = dtos;
    }

    public List<InvitedCustomerStatisticsDTO> getStatistics() {
        return statistics;
    }

    public void setStatistics(List<InvitedCustomerStatisticsDTO> statistics) {
        this.statistics = statistics;
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
