package com.everhomes.rest.customer;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>dtos: 企业客户信息列表, 参考{@link EnterpriseCustomerDTO}</li>
 *     <li>nextPageAnchor: 下一页锚点，没有下一页则无</li>
 * </ul>
 * Created by ying.xiong on 2017/8/1.
 */
public class SearchEnterpriseCustomerResponse {
    @ItemType(EnterpriseCustomerDTO.class)
    private List<EnterpriseCustomerDTO> dtos;

    private Long nextPageAnchor;

    private Long totalNum;

    public List<EnterpriseCustomerDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<EnterpriseCustomerDTO> dtos) {
        this.dtos = dtos;
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public Long getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Long totalNum) {
        this.totalNum = totalNum;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
