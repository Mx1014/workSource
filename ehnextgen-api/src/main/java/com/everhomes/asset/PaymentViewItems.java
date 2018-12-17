package com.everhomes.asset;

/**
 * Created by Wentian on 2018/5/15.
 */
public enum PaymentViewItems {
    CONTRACT("CONTRACT"), PAY("PAY"), CERTIFICATE("CERTIFICATE"), ENERGY("ENERGY"), CONFIRM("CONFIRM");
    private String code;
    PaymentViewItems(String code){
        this.code = code;
    }
    public String getCode() {
        return code;
    }
    public static PaymentViewItems fromCode(String code){
        if(code == null) return null;
        for(PaymentViewItems status : PaymentViewItems.values()){
            if(status.getCode() == code)
                return status;
        }
        return null;
    }
}
