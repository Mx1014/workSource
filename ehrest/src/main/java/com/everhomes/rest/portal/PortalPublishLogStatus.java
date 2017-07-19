// @formatter:off
package com.everhomes.rest.portal;

/**
 * <ul>
 * <li>PUBLISHING: 正在发布</li>
 * <li>FAILING: 发布失败</li>
 * <li>SUCCESS: 发布成功</li>
 * </ul>
 */
public enum PortalPublishLogStatus {
    PUBLISHING((byte)0), FAILING((byte)1), SUCCESS((byte)2);

    private byte code;

    private PortalPublishLogStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static PortalPublishLogStatus fromCode(Byte code) {
        if(null != code){
            for (PortalPublishLogStatus value: PortalPublishLogStatus.values()) {
                if(value.code == code.byteValue()){
                    return value;
                }
            }
        }
        return null;
    }
}
