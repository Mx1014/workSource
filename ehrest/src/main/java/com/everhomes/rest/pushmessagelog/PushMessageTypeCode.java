// @formatter:off
package com.everhomes.rest.pushmessagelog;

/**
 * @author huanglm
 ** <ul>推送方式
 * <li>APP: 表示应用消息推送</li>
 * <li>SMS: 表示短信推送</li>
 * </ul>
 */
public enum PushMessageTypeCode {
	
	APP((byte)1),
    SMS((byte)2),
    ;

    private byte code;

    PushMessageTypeCode(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }

    public static PushMessageTypeCode fromCode(Byte  code) {
        if (code != null) {
            for (PushMessageTypeCode value : PushMessageTypeCode.values()) {
                if (code.byteValue() == value.code) {
                    return value;
                }
            }
        }
        return null;
    }}
