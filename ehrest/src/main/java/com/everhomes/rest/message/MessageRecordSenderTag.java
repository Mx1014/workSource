package com.everhomes.rest.message;

/**
 * <p>发送状态标志</p>
 * <ul>
 * <li>FORWARD_EVENT("FORWARD EVENT"): forward事件</li>
 * <li>NOTIFY_EVENT("NOTIFY EVENT"): notify事件</li>
 * <li>APPIDSTATUS("APPIDSTATUS"): appId状态变化</li>
 * <li>REGISTER_LOGIN("REGISTER LOGIN"): 设备注册登录</li>
 * <li>NOTIFY_REQUEST("NOTIFY REQUEST"): notify查询请求</li>
 * <li>FETCH_PASTTORECENT_MESSAGES("FETCH PASTTORECENT MESSAGES"): 查询过去到现在的消息</li>
 * <li>ROUTE_STORE_MESSAGE("ROUTE STORE MESSAGE"): route固定消息</li>
 * <li>ROUTE_REALTIME_MESSAGE("ROUTE REALTIME MESSAGE"): route实时消息</li>
 * <li>FETCH_NOTIFY_MESSAGES("FETCH NOTIFY MESSAGES"): fetch推送的消息</li>
 * <li>ROUTE_MESSAGE("ROUTE MESSAGE"): route消息</li>
 * </ul>
 */

public enum  MessageRecordSenderTag {

    FORWARD_EVENT("FORWARD EVENT"), NOTIFY_EVENT("NOTIFY EVENT"), APPIDSTATUS("APPIDSTATUS"), REGISTER_LOGIN("REGISTER LOGIN"), NOTIFY_REQUEST("NOTIFY REQUEST"), FETCH_PASTTORECENT_MESSAGES("FETCH PASTTORECENT MESSAGES"), ROUTE_STORE_MESSAGE("ROUTE STORE MESSAGE"), ROUTE_REALTIME_MESSAGE("ROUTE REALTIME MESSAGE"), FETCH_NOTIFY_MESSAGES("FETCH NOTIFY MESSAGES"), ROUTE_MESSAGE("ROUTE MESSAGE");

    private String code;
    private MessageRecordSenderTag(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public static MessageRecordSenderTag fromCode(String code) {
        MessageRecordSenderTag[] values = MessageRecordSenderTag.values();
        for(MessageRecordSenderTag value : values) {
            if(value.code.equals(code)) {
                return value;
            }
        }

        return null;
    }

}
