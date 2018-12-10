package com.everhomes.rest.investment;

import java.util.List;

public class CommunityCustomerStatisticDTO {

    private Long communityId;

    private List<CustomerStatisticsDTO> dtos;

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public List<CustomerStatisticsDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<CustomerStatisticsDTO> dtos) {
        this.dtos = dtos;
    }
}
