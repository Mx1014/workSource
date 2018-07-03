//@formatter:off
package com.everhomes.rest.asset;

/**
 * Created by Wentian Wang on 2018/5/31.
 */

public class UpdateAnAppMappingCommand {


    private Long assetCategoryId;
    private Long contractCategoryId;

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
}
