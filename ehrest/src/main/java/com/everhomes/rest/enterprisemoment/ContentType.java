// @formatter:off
package com.everhomes.rest.enterprisemoment;

/**
 * <p>帖子内容类型</p>
 * <ul>
 * <li>TEXT("text"): 文本</li>
 * <li>IMAGE("image"): 图片</li>
 * <li>AUDIO("audio"): 音频</li>
 * <li>VIDEO("video"): 视频</li>
 * <li>RICH_TEXT("rich_text"): 富文本</li>
 * </ul>
 */
public enum ContentType {
    TEXT("text"), IMAGE("image"), AUDIO("audio"), VIDEO("video"), RICH_TEXT("rich_text");

    private String code;

    ContentType(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public static ContentType fromCode(String code) {
        if (code == null) {
            return TEXT;
        }

        for (ContentType type : ContentType.values()) {
            if (type.code.equalsIgnoreCase(code)) {
                return type;
            }
        }

        return null;
    }
}
