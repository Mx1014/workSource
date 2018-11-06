package com.everhomes.rest.investmentAd;

//广告类型:1-房源出租，2-房源出售
public enum InvestmentType {
	
	RENT((byte)1,"房源出租"),SALE((byte)2,"房源出售");
    
	private byte code;
    private String content;
    
    private InvestmentType(byte code,String content) {
        this.code = code;
        this.content = content;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public String getContent() {
        return this.content;
    }
    
    public static InvestmentType fromCode(Byte code) {
        if(code == null)
            return null;
        switch(code.byteValue()) {
        case 1 :
            return RENT;
        case 2 :
            return SALE;
        default :
            assert(false);
            break;
        }
        return null;
    }
}
