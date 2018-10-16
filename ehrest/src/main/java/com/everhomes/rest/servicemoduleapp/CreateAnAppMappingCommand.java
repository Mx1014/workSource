//@formatter:off
package com.everhomes.rest.servicemoduleapp;

/**
 * Created by Wentian Wang on 2018/5/24.
 */

public class CreateAnAppMappingCommand {

    private Long assetCategoryId;
    private Long contractCategoryId;
    private Byte energyFlag;
    private Integer namespaceId;
    private Long contractOriginId;
    private Byte contractChangeFlag;
    private String config;
    private String sourceType;
    private Long sourceId;
    private String ownerType;
    private Long ownerId;
    private Long billGroupId;
    private Long chargingItemId;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

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

    public Byte getEnergyFlag() {
        return energyFlag;
    }

    public void setEnergyFlag(Byte energyFlag) {
        this.energyFlag = energyFlag;
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

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public Long getBillGroupId() {
		return billGroupId;
	}

	public void setBillGroupId(Long billGroupId) {
		this.billGroupId = billGroupId;
	}

	public Long getChargingItemId() {
		return chargingItemId;
	}

	public void setChargingItemId(Long chargingItemId) {
		this.chargingItemId = chargingItemId;
	}
}
