package com.everhomes.rest.advertisement;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>assetType：关联资产类型（1：apartment，2：building，3：community）</li>
 * 	<li>assetId：关联资产id </li>
 * </ul>
 */
public class RelatedAssetDTO {
	
	private Byte assetType;
	private Long assetId;
	
	public Byte getAssetType() {
		return assetType;
	}
	
	public void setAssetType(Byte assetType) {
		this.assetType = assetType;
	}
	
	public Long getAssetId() {
		return assetId;
	}
	
	public void setAssetId(Long assetId) {
		this.assetId = assetId;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
}
