//@formatter:off
package com.everhomes.rest.asset;

/**
 * Created by Wentian Wang on 2017/9/11.
 */

public enum AssetTargetType {
    USER("eh_user"),ORGANIZATION("eh_organization");
    private String code;
    private AssetTargetType(String code){
        this.code = code;
    }
    public String getCode(){
        return code;
    }
    public AssetTargetType fromCode(String code){
        AssetTargetType[] values = AssetTargetType.values();
        for(AssetTargetType value : values) {
            if(value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }
}
