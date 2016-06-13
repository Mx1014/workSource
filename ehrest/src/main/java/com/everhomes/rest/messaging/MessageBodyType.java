package com.everhomes.rest.messaging;

public enum MessageBodyType {
    TEXT("TEXT"), IMAGE("IMAGE"), AUDIO("AUDIO"), VIDEO("VIDEO"), LINK("LINK"), NOTIFY("NOTIFY"), RICH_LINK("RICH_LINK");
    
    private String code;
    private MessageBodyType(String code) {
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
