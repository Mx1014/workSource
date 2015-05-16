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
    TEXT("text/plain"), IMAGE("image"), AUDIO("audio/basic"), VIDEO("video");
    
    private String code;
    private PostContentType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static PostContentType fromCode(String code) {
        if(code == null)
            return null;
        
        if(code.equalsIgnoreCase(TEXT.getCode())) {
            return TEXT;
        }
        if(code.equalsIgnoreCase(IMAGE.getCode())) {
            return IMAGE;
        }
        if(code.equalsIgnoreCase(AUDIO.getCode())) {
            return AUDIO;
        }
        if(code.equalsIgnoreCase(VIDEO.getCode())) {
            return VIDEO;
        }
        
        return null;
    }
}
