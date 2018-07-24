//@formatter:off
package com.everhomes.rest.asset;

/**
 * @author created by ycx
 * @date 下午3:57:34
 */
public enum AssetModuleType {
	CHARGING_ITEM("chargingItems"),//收费项配置
	CHARGING_STANDARDS("chargingStandards"),//收费项计算规则
	GROUPS("groups");//账单组设置
	
    private String code;
    private AssetModuleType(String code){
        this.code = code;
    }
    public String getCode(){
        return code;
    }
    public AssetModuleType fromCode(String code){
        AssetModuleType[] values = AssetModuleType.values();
        for(AssetModuleType value : values) {
            if(value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }
}
