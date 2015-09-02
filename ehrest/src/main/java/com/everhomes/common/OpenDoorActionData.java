package com.everhomes.common;

import java.io.Serializable;


import com.everhomes.util.StringHelper;


/**
 * <ul>开锁
 * <li>vender: 门禁厂商，参考{@link com.everhomes.launchpad.EntranceGuardVender}/li>
 * </ul>
 */
public class OpenDoorActionData implements Serializable{

    private static final long serialVersionUID = 3744458502099630999L;
    //{"vender":1}
    private Long vender;

    public Long getVender() {
        return vender;
    }

    public void setVender(Long vender) {
        this.vender = vender;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
