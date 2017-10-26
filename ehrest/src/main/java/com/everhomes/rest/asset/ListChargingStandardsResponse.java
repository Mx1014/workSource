//@formatter:off
package com.everhomes.rest.asset;

import java.util.List;

/**
 * Created by Wentian Wang on 2017/10/26.
 */
/**
 *<ul>
 * <li>list:参考{@link com.everhomes.rest.asset.ListChargingStandardsDTO}</li>
 * <li>nextPageAnchor:下一个锚点</li>
 *</ul>
 */
public class ListChargingStandardsResponse {
    private List<ListChargingStandardsDTO> list;
    private Long nextPageAnchor;

    public List<ListChargingStandardsDTO> getList() {
        return list;
    }

    public void setList(List<ListChargingStandardsDTO> list) {
        this.list = list;
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }
}
