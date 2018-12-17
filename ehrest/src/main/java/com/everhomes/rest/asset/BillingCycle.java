//@formatter:off
package com.everhomes.rest.asset;

/**
 * Created by Wentian Wang on 2018/5/14.
 * <ul>
 * <li>DAY(1): 按天</li>
 * <li>NATURAL_MONTH(2): 按自然月</li>
 * <li>NATURAL_QUARTER(3): 按自然季</li>
 * <li>NATURAL_YEAR(4): 按自然年</li>
 * <li>ONE_DEAL(5): 一次性</li>
 * <li>CONTRACT_MONTH(6): 按合同月</li>
 * <li>CONTRACT_QUARTER(7): 按合同季</li>
 * <li>CONTRACT_YEAR(8): 按合同年</li>
 * <li>CONTRACT_TWOMONTH(9): 按合同两个月</li>
 * <li>CONTRACT_SIXMONTH(10): 按合同6个月</li>
 * </ul>
 */
public enum BillingCycle {
    DAY((byte)1, null, false),
    NATURAL_MONTH((byte)2, 0, false),
    NATURAL_QUARTER((byte)3, 2, false),
    NATURAL_YEAR((byte)4, 11, false),
    ONE_DEAL((byte)5, null, false),
    CONTRACT_MONTH((byte)6, 0, true),
    CONTRACT_QUARTER((byte)7, 2, true),
    CONTRACT_YEAR((byte)8, 11, true),
	CONTRACT_TWOMONTH((byte)9, 1, true),//新增按合同两个月
	CONTRACT_SIXMONTH((byte)10, 5, true);//新增按合同6个月
	
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