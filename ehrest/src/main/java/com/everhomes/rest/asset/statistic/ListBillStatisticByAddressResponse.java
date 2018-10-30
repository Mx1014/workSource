//@formatter:off
package com.everhomes.rest.asset.statistic;

import java.util.List;

/**
 *<ul>
 * <li>listBillStatisticByAddressDTOs: 所有统计的集合</li>
 * <li>nextPageAnchor: 下一次锚点</li>
 *</ul>
 */
public class ListBillStatisticByAddressResponse {
    private List<ListBillStatisticByAddressDTO> listBillStatisticByAddressDTOs;
    private Long nextPageAnchor;
    
	public List<ListBillStatisticByAddressDTO> getListBillStatisticByAddressDTOs() {
		return listBillStatisticByAddressDTOs;
	}
	public void setListBillStatisticByAddressDTOs(List<ListBillStatisticByAddressDTO> listBillStatisticByAddressDTOs) {
		this.listBillStatisticByAddressDTOs = listBillStatisticByAddressDTOs;
	}
	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}
	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}
	
}
