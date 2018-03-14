//@formatter:off
package com.everhomes.rest.supplier;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * Created by Wentian Wang on 2018/1/10.
 */

/**
 *<ul>
 * <li>nextPageAnchor:下一此的锚点，作为下一次请求的PageAnchor的值</li>
 * <li>dtos:数据列表，参考{@link com.everhomes.rest.supplier.ListSuppliersDTO}</li>
 *</ul>
 */
public class ListSuppliersResponse {
    private Long nextPageAnchor;
    private List<ListSuppliersDTO> dtos;

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

    public List<ListSuppliersDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<ListSuppliersDTO> dtos) {
        this.dtos = dtos;
    }
}
