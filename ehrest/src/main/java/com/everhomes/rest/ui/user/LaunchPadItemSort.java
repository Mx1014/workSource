package com.everhomes.rest.ui.user;

/**
 * <ul>
 *  <li>id: item ID</li>
 *  <li>defaultOrder: 排序号</li>
 * </ul>
 */
public class LaunchPadItemSort {

	private Long id;
	
	private Integer defaultOrder;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getDefaultOrder() {
		return defaultOrder;
	}

	public void setDefaultOrder(Integer defaultOrder) {
		this.defaultOrder = defaultOrder;
	}
	
	
}
