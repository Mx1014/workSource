//@formatter:off
package com.everhomes.rest.asset.statistic;

import java.util.List;

/**
 *<ul>
 * <li>listBillStatisticByCommunityDTOs: 所有统计的集合</li>
 * <li>nextPageAnchor: 下一次锚点</li>
 *</ul>
 */
public class ListBillStatisticByCommunityResponse {
    private List<ListBillStatisticByCommunityDTO> listBillStatisticByCommunityDTOs;
    private Long nextPageAnchor;
    
	public List<ListBillStatisticByCommunityDTO> getListBillStatisticByCommunityDTOs() {
		return listBillStatisticByCommunityDTOs;
	}
	public void setListBillStatisticByCommunityDTOs(
			List<ListBillStatisticByCommunityDTO> listBillStatisticByCommunityDTOs) {
		this.listBillStatisticByCommunityDTOs = listBillStatisticByCommunityDTOs;
	}
	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}
	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}
    
}
