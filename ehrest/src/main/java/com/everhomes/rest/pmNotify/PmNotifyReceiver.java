package com.everhomes.rest.pmNotify;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>receiverType: 接收者类型 参考{@link com.everhomes.rest.pmNotify.PmNotifyReceiverType}</li>
 *     <li>receiverIds: 对应类型的id列表</li>
 * </ul>
 * Created by ying.xiong on 2017/9/11.
 */
public class PmNotifyReceiver {

    private Byte receiverType;
    @ItemType(Long.class)
    private List<Long> receiverIds;

    public List<Long> getReceiverIds() {
        return receiverIds;
    }

    public void setReceiverIds(List<Long> receiverIds) {
        this.receiverIds = receiverIds;
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
