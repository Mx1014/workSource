// @formatter:off
package com.everhomes.rest.banner;

/**
 * <ul>banner 状态
 * <li>DELETE: 删除</li>
 * <li>WAITINGCONFIRM：等待确认</li>
 * <li>ACTIVE: 正常</li>
 * <li>CLOSE: 关闭</li>
 * </ul>
 */
public enum BannerStatus {
	
    DELETE((byte)0), WAITINGCONFIRM((byte)1), ACTIVE((byte)2),CLOSE((byte)3);
    
    private byte code;
    private BannerStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static BannerStatus fromCode(Byte code) {
        if(code == null)
            return null;
        switch(code.byteValue()) {
	        case 0:
	            return DELETE;
	        case 1:
	            return WAITINGCONFIRM;
	        case 2: 
	            return ACTIVE;
	        case 3: 
	        	return CLOSE;
	        default :
	            assert(false);
	            break;
        }
        return null;
    }
}
