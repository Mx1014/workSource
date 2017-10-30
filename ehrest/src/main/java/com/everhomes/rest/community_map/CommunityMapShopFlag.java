package com.everhomes.rest.community_map;


/**
 * <ul>
 * <li>OPEN_IN_BIZ((byte)1)</li>
 * <li>NOT_OPEN_IN_BIZ((byte)2)</li>
 * </ul>
 */
public enum CommunityMapShopFlag {
	OPEN_IN_BIZ((byte)1),
    NOT_OPEN_IN_BIZ((byte)2);

    private byte code;

    private CommunityMapShopFlag(byte code) {
        this.code = code;
    }
       
    public byte getCode() {
        return this.code;
    }
       
    public static CommunityMapShopFlag fromCode(byte code) {
    	CommunityMapShopFlag[] values = CommunityMapShopFlag.values();
        for(CommunityMapShopFlag value : values) {
            if(value.code == code) {
                return value;
            }
        }
       
        return null;
    }
}
