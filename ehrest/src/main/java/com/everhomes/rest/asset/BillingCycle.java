//@formatter:off
package com.everhomes.rest.asset;

/**
 * Created by Wentian Wang on 2018/5/14.
 */
public enum BillingCycle {
    DAY((byte)1), NATURAL_MONTH((byte)2), NATURAL_QUARTER((byte)3), NATURAL_YEAR((byte)4),
    CONTRACT_MONTH((byte)5), CONTRACT_QUARTER((byte)6), CONTRACT_YEAR((byte)7);
    private Byte code;
    BillingCycle(byte code){
        this.code = code;
    }
    public Byte getCode(){return this.code;}
    public static BillingCycle fromCode(Byte code){
        if(code == null) return null;
        for(BillingCycle status : BillingCycle.values()){
            if(status.getCode() == code){
                return status;
            }
        }
        return null;
    }
}