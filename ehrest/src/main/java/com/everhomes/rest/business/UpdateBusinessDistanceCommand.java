package com.everhomes.rest.business;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * 	<li>id : 店铺id</li>
 * 	<li>visibleDistance : 店铺可见范围,单位:米</li>
 * </ul>
 *
 */
public class UpdateBusinessDistanceCommand {
	@NotNull
	private Long id;
	@NotNull
	private Double visibleDistance;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Double getVisibleDistance() {
		return visibleDistance;
	}
	public void setVisibleDistance(Double visibleDistance) {
		this.visibleDistance = visibleDistance;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	

}
