package com.everhomes.rest.investmentAd;

//招商状态:1-招商中,2-已下线,3-已出租
public enum InvestmentStatus {

	ONLINE((byte)1,"招商中"),OFFLINE((byte)2,"已下线"),RENTED((byte)3,"已出租");
    
	private byte code;
    private String content;
    
    private InvestmentStatus(byte code,String content) {
        this.code = code;
        this.content = content;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public String getContent() {
        return this.content;
    }
    
    public static InvestmentStatus fromCode(Byte code) {
        if(code == null)
            return null;
        switch(code.byteValue()) {
        case 1 :
            return ONLINE;
        case 2 :
            return OFFLINE;
        case 3 :
            return RENTED;
        default :
            assert(false);
            break;
        }
        return null;
    }
	
}
