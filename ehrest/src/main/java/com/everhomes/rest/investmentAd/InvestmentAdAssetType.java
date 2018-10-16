package com.everhomes.rest.investmentAd;

public enum InvestmentAdAssetType {
	
	COMMUNITY((byte)1), BUILDING((byte)2),APARTMENT((byte)3);
    
    private byte code;
    private InvestmentAdAssetType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static InvestmentAdAssetType fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
	        case 1 :
	            return COMMUNITY;
	        case 2 :
	            return BUILDING;
	        case 3 :
	            return APARTMENT;
	        default :
	            assert(false);
	            break;
        }
        return null;
    }
}
