package com.everhomes.rest.asset.modulemapping;

import com.everhomes.util.StringHelper;

/**
 * @author created by ycx
 * @date 下午7:33:30
 */
public class AssetMapEnergyConfig{
	
	private Byte energyFlag;

	public Byte getEnergyFlag() {
		return energyFlag;
	}

	public void setEnergyFlag(Byte energyFlag) {
		this.energyFlag = energyFlag;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
