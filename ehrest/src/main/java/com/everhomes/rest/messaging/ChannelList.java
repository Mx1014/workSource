package com.everhomes.rest.messaging;

import java.util.List;

import com.everhomes.discover.ItemType;

/**
 *  This class is to help generate corresponding IOS json serialization class. It is pretty tedious to actually
 *  do json encoding/decoding of an array of objects with NSJSONSerialization in objective C
 */
public class ChannelList {
    @ItemType(MessageChannel.class)
    private List<MessageChannel> channels;

    public ChannelList() {
    }

    public List<MessageChannel> getChannels() {
        return channels;
    }

    public void setChannels(List<MessageChannel> channels) {
        this.channels = channels;
    }
}
