package com.everhomes.rest.pmNotify;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * Created by ying.xiong on 2017/9/15.
 */
public class PmNotifyReceiverDTO {

    private Byte receiverType;
    @ItemType(ReceiverName.class)
    private List<ReceiverName> receivers;

    public List<ReceiverName> getReceivers() {
        return receivers;
    }

    public void setReceivers(List<ReceiverName> receivers) {
        this.receivers = receivers;
    }

    public Byte getReceiverType() {
        return receiverType;
    }

    public void setReceiverType(Byte receiverType) {
        this.receiverType = receiverType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
