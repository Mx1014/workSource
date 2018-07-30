//@formatter:off
package com.everhomes.rest.asset;

/**
 * @author created by ycx
 * @date 下午2:05:04
 */

public enum AssetSubtractionType {
    item("eh_payment_bill_items"),lateFine("eh_payment_late_fine");
    private String code;
    private AssetSubtractionType(String code){
        this.code = code;
    }
    public String getCode(){
        return code;
    }
    public AssetSubtractionType fromCode(String code){
        AssetSubtractionType[] values = AssetSubtractionType.values();
        for(AssetSubtractionType value : values) {
            if(value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }
}
