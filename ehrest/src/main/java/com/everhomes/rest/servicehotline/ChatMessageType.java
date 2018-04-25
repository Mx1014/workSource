package com.everhomes.rest.servicehotline;

import com.everhomes.util.StringHelper;

/**
 * <ul> 
 * <li>TEXT: 文本（包括表情）</li>
 * <li>IMAGE: 图片</li>
 * <li>VOICE: 语音</li>
 * </ul>
 */
public enum ChatMessageType {
	TEXT((byte) 0), IMAGE((byte) (1)) , VOICE((byte) (2));
	
	private Byte code;

	private ChatMessageType(Byte code) {
		this.code = code;
	}

	public Byte getCode() {
		return code;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	
	public static ChatMessageType fromCode(Byte code) {
		if (code != null) {
			for (ChatMessageType a : ChatMessageType.values()) {
				if (code.byteValue() == a.code.byteValue()) {
					return a;
				}
			}
		}
		return null;
	}
}

