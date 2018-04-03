package com.everhomes.rest.community_map;


/**
 * <ul>
 * <li>INACTIVE((byte)0)</li>
 * <li>ACTIVE((byte)2)</li>
 * </ul>
 */
public enum CommunityMapShopStatus {
	INACTIVE((byte)0),
    ACTIVE((byte)2);

    private byte code;

    private CommunityMapShopStatus(byte code) {
        this.code = code;
    }
       
    public byte getCode() {
        return this.code;
    }
       
    public static CommunityMapShopStatus fromCode(byte code) {
    	CommunityMapShopStatus[] values = CommunityMapShopStatus.values();
        for(CommunityMapShopStatus value : values) {
            if(value.code == code) {
                return value;
            }
        }
       
        return null;
    }
}
