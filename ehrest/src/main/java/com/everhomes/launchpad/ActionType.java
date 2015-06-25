// @formatter:off
package com.everhomes.launchpad;

/**
 * <ul>参数类型
 * <li>NONE: 默认</li>
 * <li>MORE_BUTTON: 更多</li>
 * <li>APP: 应用</li>
 * <li>ZUOLIN_URL: 左邻官方url</li>
 * <li>THIRDPART_URL: 第三方url</li>
 * <li>YELLOW_PAGE: 黄页</li>
 * <li>POST: 帖子</li>
 * <li>MESSAGE: 消息</li>
 * <li>ACTIVITY: 活动</li>
 * <li>HOTLINE: 热线</li>
 * <li>QRCODE_SCAN: 二维码扫描</li>
 * </ul>
 */
public enum ActionType {
    NONE((byte)0), MORE_BUTTON((byte)1), APP((byte)2),ZUOLIN_URL((byte)3), THIRDPART_URL((byte)4), YELLOW_PAGE((byte)5),
    POST((byte)6), MESSAGE((byte)7), ACTIVITY((byte)8), HOTLINE((byte)9), QRCODE_SCAN((byte)10);
    
    private byte code;
    
    private ActionType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static ActionType fromCode(Byte code) {
        if(code == null)
            return null;
        
        ActionType[] values = ActionType.values();
        for(ActionType value : values) {
            if(value.code == code.byteValue()) {
                return value;
            }
        }
        
        return null;
    }
}
