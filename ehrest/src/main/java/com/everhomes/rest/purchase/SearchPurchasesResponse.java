//@formatter:off
package com.everhomes.rest.purchase;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * Created by Wentian Wang on 2018/1/10.
 */
/**
 *<ul>
 * <li>nextPageAnchor:下一个锚点</li>
 * <li>dtos:采购单列表，参考{@link com.everhomes.rest.purchase.SearchPurchasesDTO}</li>
 *</ul>
 */
public class SearchPurchasesResponse {
    private Long nextPageAnchor;
    private List<SearchPurchasesDTO> dtos;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<SearchPurchasesDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<SearchPurchasesDTO> dtos) {
        this.dtos = dtos;
    }
}
