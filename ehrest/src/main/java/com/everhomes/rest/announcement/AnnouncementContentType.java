// @formatter:off
package com.everhomes.rest.announcement;

/**
 * <p>公告内容类型</p>
 * <ul>
 * <li>TEXT("text"): 文本</li>
 * <li>IMAGE("image"): 图片</li>
 * <li>AUDIO("audio"): 音频</li>
 * <li>VIDEO("video"): 视频</li>
 * <li>RICH_TEXT("rich_text"): 视频</li>
 * </ul>
 */
public enum AnnouncementContentType {
    TEXT("text"), IMAGE("image"), AUDIO("audio"), VIDEO("video"), RICH_TEXT("rich_text");

    private String code;
    private AnnouncementContentType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static AnnouncementContentType fromCode(String code) {
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
        if(code.equalsIgnoreCase(RICH_TEXT.getCode())) {
        	return RICH_TEXT;
        }
        
        return null;
    }
}
