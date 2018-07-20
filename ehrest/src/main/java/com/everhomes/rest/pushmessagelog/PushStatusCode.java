// @formatter:off
package com.everhomes.rest.pushmessagelog;

/**
 * @author huanglm
 ** <ul>推送状态,1表示等待推送，2表示推送中，3表示推送完成
 * <li>WAITING: 等待推送</li>
 * <li>PUSHING: 推送中</li>
 * <li>FINISH: 推送完成</li>
 * </ul>
 */
public enum PushStatusCode {
	
	WAITING((byte)1),
    PUSHING((byte)2),
    FINISH((byte)3),
    ;

    private byte code;

    PushStatusCode(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }

    public static PushStatusCode fromCode(Byte  code) {
        if (code != null) {
            for (PushStatusCode value : PushStatusCode.values()) {
                if (code.byteValue() == value.code) {
                    return value;
                }
            }
        }
        return null;
    }}
