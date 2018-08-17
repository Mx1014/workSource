package com.everhomes.rest.asset;

/**
 * @author created by ycx
 * @date 下午1:38:48
 */
public class AssetInstanceConfigDTO {
	private String url;
	private Long categoryId;
	private Long contractOriginId;
	private Byte contractChangeFlag;
	private Byte energyFlag;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	public Long getContractOriginId() {
		return contractOriginId;
	}
	public void setContractOriginId(Long contractOriginId) {
		this.contractOriginId = contractOriginId;
	}
	public Byte getContractChangeFlag() {
		return contractChangeFlag;
	}
	public void setContractChangeFlag(Byte contractChangeFlag) {
		this.contractChangeFlag = contractChangeFlag;
	}
	public Byte getEnergyFlag() {
		return energyFlag;
	}
	public void setEnergyFlag(Byte energyFlag) {
		this.energyFlag = energyFlag;
	}
}
