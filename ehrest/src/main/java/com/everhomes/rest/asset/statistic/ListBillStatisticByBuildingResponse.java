//@formatter:off
package com.everhomes.rest.asset.statistic;

import java.util.List;

/**
 *<ul>
 * <li>listBillStatisticByBuildingDTOs: 所有统计的集合</li>
 * <li>nextPageAnchor: 下一次锚点</li>
 *</ul>
 */
public class ListBillStatisticByBuildingResponse {
    private List<ListBillStatisticByBuildingDTO> listBillStatisticByBuildingDTOs;
    private Long nextPageAnchor;
    
	public List<ListBillStatisticByBuildingDTO> getListBillStatisticByBuildingDTOs() {
		return listBillStatisticByBuildingDTOs;
	}
	public void setListBillStatisticByBuildingDTOs(List<ListBillStatisticByBuildingDTO> listBillStatisticByBuildingDTOs) {
		this.listBillStatisticByBuildingDTOs = listBillStatisticByBuildingDTOs;
	}
	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}
	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}
}
