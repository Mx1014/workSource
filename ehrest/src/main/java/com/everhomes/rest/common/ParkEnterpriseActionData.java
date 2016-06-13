package com.everhomes.rest.common;

import java.io.Serializable;



import com.everhomes.util.StringHelper;


/**
 * <ul>actionType为PARKENTERPRISE(34)，园区企业
 * <li>type：类型，参考{@link com.everhomes.rest.yellowPage.YellowPageType}</li>
 * </ul>
 */
public class ParkEnterpriseActionData implements Serializable{
    private static final long serialVersionUID = -3083004609712658148L;
    
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
