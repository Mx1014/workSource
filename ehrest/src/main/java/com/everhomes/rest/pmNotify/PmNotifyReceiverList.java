package com.everhomes.rest.pmNotify;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * Created by ying.xiong on 2017/9/12.
 */
public class PmNotifyReceiverList {
    @ItemType(PmNotifyReceiver.class)
    private List<PmNotifyReceiver> receivers;

    public List<PmNotifyReceiver> getReceivers() {
        return receivers;
    }

    public void setReceivers(List<PmNotifyReceiver> receivers) {
        this.receivers = receivers;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
