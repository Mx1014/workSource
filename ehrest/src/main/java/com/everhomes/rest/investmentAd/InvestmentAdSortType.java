package com.everhomes.rest.investmentAd;
/**
 * ASC:1,DESC:2
 * @author steve
 *
 */
public enum InvestmentAdSortType {
	ASC((byte)1),DESC((byte)2);
    
    private byte code;
    private InvestmentAdSortType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static InvestmentAdSortType fromCode(Byte code) {
        if(code == null)
            return null;
        switch(code.byteValue()) {
        case 1 :
            return ASC;
        case 2 :
            return DESC;
        default :
            assert(false);
            break;
        }
        return null;
    }
}
