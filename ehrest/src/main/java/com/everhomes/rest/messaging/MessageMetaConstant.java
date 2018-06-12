package com.everhomes.rest.messaging;

public interface MessageMetaConstant {
    public static final String JUMP_TYPE = "jumpType";
    public static final String JUMP_OBJECT = "jumpObj";
    public static final String META_OBJECT_TYPE = "meta-object-type";
    public static final String META_OBJECT = "meta-object";
    public static final String INCLUDE = "include";
    public static final String EXCLUDE = "exclude";
    public static final String SENDER_NAME = "sender-name";
    public static final String POPUP_FLAG = "popup-flag";
    public static final String VOICE_REMIND = "voice-remind";
    public static final String MESSAGE_SUBJECT = "message-subject";// 通知消息会有消息标题
    public static final String PERSIST_TYPE = "persist-type";// 消息持久化类型 @link com.everhomes.rest.messaging.MessagePersistType
}
