//@formatter:off
package com.everhomes.rest.asset;

/**
 * @author created by ycx
 * @date 下午2:05:04
 */

public enum AssetItemFineType {
    item("eh_payment_bill_items"),lateFine("eh_payment_late_fine");
    private String code;
    private AssetItemFineType(String code){
        this.code = code;
    }
    public String getCode(){
        return code;
    }
    public AssetItemFineType fromCode(String code){
        AssetItemFineType[] values = AssetItemFineType.values();
        for(AssetItemFineType value : values) {
            if(value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }
}
