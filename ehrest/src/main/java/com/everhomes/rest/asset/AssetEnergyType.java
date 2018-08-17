//@formatter:off
package com.everhomes.rest.asset;

/**
 * @author created by ycx
 * @date 下午11:13:32
 */

public enum AssetEnergyType {
	//eh_payment_charging_items 4:自用水费  7：公摊水费  5:自用电费   8：公摊电费
    personWaterItem(4L),publicWaterItem(7L),personElectricItem(5L),publicElectricItem(8L);
	
    private Long code;
    private AssetEnergyType(Long code){
        this.code = code;
    }
    public Long getCode(){
        return code;
    }
    public AssetEnergyType fromCode(Long code){
        AssetEnergyType[] values = AssetEnergyType.values();
        for(AssetEnergyType value : values) {
            if(value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }
}
