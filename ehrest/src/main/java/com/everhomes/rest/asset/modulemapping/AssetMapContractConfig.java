package com.everhomes.rest.asset.modulemapping;

import com.everhomes.util.StringHelper;

/**
 * @author created by ycx
 * @date 下午7:33:30
 */
public class AssetMapContractConfig{
	
	private Long contractOriginId;
    private Byte contractChangeFlag;
    
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
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
