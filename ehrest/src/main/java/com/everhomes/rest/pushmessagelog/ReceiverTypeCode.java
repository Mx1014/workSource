// @formatter:off
package com.everhomes.rest.pushmessagelog;

/**
 * @author huanglm
 ** <ul>推送对象的类型 ,0表示所有人，1表示按项目，2按手机号
 * <li>ALL: 所有人</li>
 * <li>COMMUNITY: 按项目</li>
 * <li>PHONE: 按手机号</li>
 * </ul>
 */
public enum ReceiverTypeCode {
	
	ALL((byte)0),
    COMMUNITY((byte)1),
    PHONE((byte)2),
    ;

    private byte code;

    ReceiverTypeCode(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }

    public static ReceiverTypeCode fromCode(Byte  code) {
        if (code != null) {
            for (ReceiverTypeCode value : ReceiverTypeCode.values()) {
                if (code.byteValue() == value.code) {
                    return value;
                }
            }
        }
        return null;
    }}
