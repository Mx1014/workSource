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
}
