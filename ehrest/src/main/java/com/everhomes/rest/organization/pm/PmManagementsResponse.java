package com.everhomes.rest.organization.pm;

import java.util.List;

import com.everhomes.discover.ItemType;

/**
 * <ul>
 *  <li>pmManagement: 物业公司信息</li>
 *  <li>nextPageAnchor：分页的锚点，下一页开始取数据的位置</li>
 * </ul>
 *
 */
public class PmManagementsResponse {

	
	@ItemType(value = PmManagementsDTO.class)
	private List<PmManagementsDTO> pmManagement;
	
	private Long nextPageAnchor;

	public List<PmManagementsDTO> getPmManagement() {
		return pmManagement;
	}

	public void setPmManagement(List<PmManagementsDTO> pmManagement) {
		this.pmManagement = pmManagement;
	}

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	
}
