// @formatter:off
package com.everhomes.rest.qrcode;

/**
 * <ul>二维码状态
 * <li>INACTIVE: 已失效</li>
 * <li>WAITING_FOR_CONFIRMATION: 待确认</li>
 * <li>ACTIVE: 正常</li>
 * </ul>
 */
public enum QRCodeStatus {
    INACTIVE((byte)0), WAITING_FOR_CONFIRMATION((byte)1), ACTIVE((byte)2);
    
    private byte code;
    
    private QRCodeStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static QRCodeStatus fromCode(Byte code) {
        if(code != null) {
            QRCodeStatus[] values = QRCodeStatus.values();
            for(QRCodeStatus value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
