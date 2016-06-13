package com.everhomes.rest.common;

import java.io.Serializable;



import com.everhomes.util.StringHelper;


/**
 * <ul>开锁
 * <li>vender: 门禁厂商，参考{@link com.everhomes.rest.launchpad.EntranceGuardVender}/li>
 * <li>remote: 开锁方式，参考{@link com.everhomes.rest.launchpad.UnlockMode}/li>
 * </ul>
 */
public class OpenDoorActionData implements Serializable{

    private static final long serialVersionUID = 3744458502099630999L;
    //{"vender":1,"remote":0}
    private Long vender;
    private Byte remote;

    public Long getVender() {
        return vender;
    }

    public void setVender(Long vender) {
        this.vender = vender;
    }

    public Byte getRemote() {
        return remote;
    }

    public void setRemote(Byte remote) {
        this.remote = remote;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
