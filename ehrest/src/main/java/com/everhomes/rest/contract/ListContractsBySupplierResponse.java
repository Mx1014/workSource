//@formatter:off
package com.everhomes.rest.contract;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * Created by Wentian Wang on 2018/1/10.
 */
/**
 *<ul>
 * <li>nextPageAnchor:下一个锚点</li>
 * <li>dtos:合同数据列表，参考{@link com.everhomes.rest.contract.ContractLogDTO}</li>
 *</ul>
 */
public class ListContractsBySupplierResponse {
    private Long nextPageAnchor;
    @ItemType(ContractLogDTO.class)
    private List<ContractLogDTO> dtos;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<ContractLogDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<ContractLogDTO> dtos) {
        this.dtos = dtos;
    }
}
