//@formatter:off
package com.everhomes.rest.asset.modulemapping;

/**
 * @author created by ycx
 * @date 下午4:11:27
 */
public class CreateEnergyMappingCommand {

    private Long assetCategoryId;
    private Byte energyFlag;
    private Integer namespaceId;
    private String config;
    private String sourceType;
    private Long sourceId;
    
	public Long getAssetCategoryId() {
		return assetCategoryId;
	}
	public void setAssetCategoryId(Long assetCategoryId) {
		this.assetCategoryId = assetCategoryId;
	}
	public Byte getEnergyFlag() {
		return energyFlag;
	}
	public void setEnergyFlag(Byte energyFlag) {
		this.energyFlag = energyFlag;
	}
	public Integer getNamespaceId() {
		return namespaceId;
	}
	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}
	public String getConfig() {
		return config;
	}
	public void setConfig(String config) {
		this.config = config;
	}
	public String getSourceType() {
		return sourceType;
	}
	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}
	public Long getSourceId() {
		return sourceId;
	}
	public void setSourceId(Long sourceId) {
		this.sourceId = sourceId;
	}
	
}
