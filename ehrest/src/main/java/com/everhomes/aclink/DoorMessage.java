package com.everhomes.aclink;

public class DoorMessage {
    //DoorCommand Id as sequence
    Long seq;
    Long doorId;
    Byte messageType;
    AclinkMessage body;
    
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
}
