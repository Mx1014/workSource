package com.everhomes.rest.customer;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * <ul>
 *     <li>memberTotalCount: 人才总数</li>
 *     <li>dtos: 统计信息 参考{@link com.everhomes.rest.customer.CustomerTalentStatisticsDTO}</li>
 * </ul>
 * Created by ying.xiong on 2017/8/3.
 */
public class CustomerTalentStatisticsResponse {
    private Long memberTotalCount;

    @ItemType(CustomerTalentStatisticsDTO.class)
    private List<CustomerTalentStatisticsDTO> dtos;

    public List<CustomerTalentStatisticsDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<CustomerTalentStatisticsDTO> dtos) {
        this.dtos = dtos;
    }

    public Long getMemberTotalCount() {
        return memberTotalCount;
    }

    public void setMemberTotalCount(Long memberTotalCount) {
        this.memberTotalCount = memberTotalCount;
    }
}
