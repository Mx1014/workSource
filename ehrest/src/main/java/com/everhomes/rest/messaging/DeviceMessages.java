package com.everhomes.rest.messaging;

import java.util.ArrayList;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>messages: 设备信息里表，参考{@link com.everhomes.rest.messaging.DeviceMessage}</li>
 * <li>anchor: 消息游标</li>
 * </ul>
 * @author janson
 *
 */
public class DeviceMessages {
    
    @ItemType(DeviceMessage.class)
    private List<DeviceMessage> messages;
    
    private Long anchor;
    
    public DeviceMessages() {
        messages = new ArrayList<DeviceMessage>();
    }
    
    public void add(DeviceMessage msg) {
        messages.add(msg);
    }
    
    public List<DeviceMessage> getMessages() {
        return messages;
    }
    public void setMessages(List<DeviceMessage> messages) {
        this.messages = messages;
    }
    public Long getAnchor() {
        return anchor;
    }
    public void setAnchor(Long anchor) {
        this.anchor = anchor;
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }    
    
}
