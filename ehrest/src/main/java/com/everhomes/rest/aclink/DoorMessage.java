package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>seq: 消息序号</li>
 * <li>doorId: 门禁ID</li>
 * <li>messageType: 消息类型，目前都为 0</li>
 * <li>body: 消息实体信息</li>
 * </ul>
 * @author janson
 *
 */
public class DoorMessage {
    //DoorCommand Id as sequence
    Long seq;
    Long doorId;
    Byte messageType;
    AclinkMessage body;
    String extra;
    
    public Long getSeq() {
        return seq;
    }
    public void setSeq(Long seq) {
        this.seq = seq;
    }
    public Long getDoorId() {
        return doorId;
    }
    public void setDoorId(Long doorId) {
        this.doorId = doorId;
    }
    
    public Byte getMessageType() {
        return messageType;
    }
    public void setMessageType(Byte messageType) {
        this.messageType = messageType;
    }
    public AclinkMessage getBody() {
        return body;
    }
    public void setBody(AclinkMessage body) {
        this.body = body;
    }
    
    public String getExtra() {
        return extra;
    }
    public void setExtra(String extra) {
        this.extra = extra;
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
