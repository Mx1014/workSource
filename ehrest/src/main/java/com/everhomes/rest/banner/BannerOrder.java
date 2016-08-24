// @formatter:off
package com.everhomes.rest.banner;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: banner id</li>
 * <li>defaultOrder: banner排序</li>
 * </ul>
 */
public class BannerOrder {
	@NotNull
	private Long id;
	@NotNull
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
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
