package com.everhomes.common;

import java.io.Serializable;


import com.everhomes.util.StringHelper;


/**
 * <ul>actionType为SERVICEALLIANCE(33)，服务联盟
 * <li>type：类型，参考{@link com.everhomes.yellowPage.YellowPageType}</li>
 * </ul>
 */
public class ServiceAllianceActionData implements Serializable{
    private static final long serialVersionUID = -742724365939053762L;
    
    private Byte type;

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
