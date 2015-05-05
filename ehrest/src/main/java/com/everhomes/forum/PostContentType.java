// @formatter:off
package com.everhomes.forum;

/**
 * <p>帖子内容类型</p>
 * <ul>
 * <li>TEXT: 文本</li>
 * <li>IMAGE: 图片</li>
 * <li>AUDIO: 音频</li>
 * <li>VIDEO: 视频</li>
 * </ul>
 */
public enum PostContentType {
    TEXT((byte)0), IMAGE((byte)1), AUDIO((byte)2), VIDEO((byte)3);
    
    private byte code;
    private PostContentType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static PostContentType fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0:
        	return TEXT;
        case 1:
            return IMAGE;
        case 2:
            return AUDIO;
        case 3:
        	return VIDEO;
        default :
            break;
        }
        
        return null;
    }
}
