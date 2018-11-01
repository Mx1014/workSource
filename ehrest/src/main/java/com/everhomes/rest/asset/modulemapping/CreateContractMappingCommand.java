//@formatter:off
package com.everhomes.rest.asset.modulemapping;

/**
 * @author created by ycx
 * @date 下午4:11:27
 */
public class CreateContractMappingCommand {

    private Long assetCategoryId;
    private Long contractCategoryId;
    private Integer namespaceId;
    private Long contractOriginId;
    private Byte contractChangeFlag;
    private String config;
    private String sourceType;
    private Long sourceId;
    
	public Long getAssetCategoryId() {
		return assetCategoryId;
	}
	public void setAssetCategoryId(Long assetCategoryId) {
		this.assetCategoryId = assetCategoryId;
	}
	public Long getContractCategoryId() {
		return contractCategoryId;
	}
	public void setContractCategoryId(Long contractCategoryId) {
		this.contractCategoryId = contractCategoryId;
	}
	public Integer getNamespaceId() {
		return namespaceId;
	}
	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
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
