// @formatter:off
package com.everhomes.rest.messaging;

/**
 * <ul>
 *     <li>TEXT: 文本</li>
 *     <li>IMAGE: 图片</li>
 *     <li>AUDIO: 语音</li>
 *     <li>VIDEO: 视频</li>
 *     <li>LINK</li>
 *     <li>NOTIFY</li>
 *     <li>RICH_LINK</li>
 *     <li>INNER_LINK: 消息内容内部有链接</li>
 *     <li>ACTION: 带有跳转动作的消息</li>
 * </ul>
 */
public enum MessageBodyType {
    TEXT("TEXT"),
    IMAGE("IMAGE"),
    AUDIO("AUDIO"),
    VIDEO("VIDEO"),
    LINK("LINK"),
    NOTIFY("NOTIFY"),
    RICH_LINK("RICH_LINK"),
    INNER_LINK("INNER_LINK"),
    ACTION("ACTION")
    ;
    
    private String code;

    MessageBodyType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static MessageBodyType fromCode(String code) {
        for(MessageBodyType t : MessageBodyType.values()) {
            if (t.code.equals(code)) {
                return t;
            }
        }
        return null;
    }
}
