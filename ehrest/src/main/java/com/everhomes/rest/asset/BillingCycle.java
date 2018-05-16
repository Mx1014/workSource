//@formatter:off
package com.everhomes.rest.asset;

/**
 * Created by Wentian Wang on 2018/5/14.
 */
public enum BillingCycle {
    DAY((byte)1, null, false),
    NATURAL_MONTH((byte)2, 0, false),
    NATURAL_QUARTER((byte)3, 2, false),
    NATURAL_YEAR((byte)4, 11, false),
    CONTRACT_MONTH((byte)5, 0, true),
    CONTRACT_QUARTER((byte)6, 2, true),
    CONTRACT_YEAR((byte)7, 11, true);
    private byte code;
    private Integer monthOffset;
    private boolean isContract;
    BillingCycle(byte code, Integer monthOffset, boolean isContract){
        this.code = code;
        this.monthOffset = monthOffset;
        this.isContract = isContract;
    }
    public byte getCode(){return this.code;}
    public Integer getMonthOffset(){return this.monthOffset;}
    public boolean isContract(){return this.isContract;}
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