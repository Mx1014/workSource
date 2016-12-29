package com.everhomes.rest.yellowPage;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>BOTH((byte)0): 客户端和浏览器</li>
 *     <li>CLIENT_ONLY((byte)1): 仅客户端</li>
 *     <li>BROWSER_ONLY((byte)2): 仅浏览器</li>
 * </ul>
 */
public enum ServiceAllianceCategoryDisplayDestination {
    BOTH((byte)0), CLIENT_ONLY((byte)1), BROWSER_ONLY((byte)2);
    private Byte code;
    private ServiceAllianceCategoryDisplayDestination(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return this.code;
    }

    public static ServiceAllianceCategoryDisplayDestination fromCode(Byte code) {
        for(ServiceAllianceCategoryDisplayDestination v :ServiceAllianceCategoryDisplayDestination.values()){
            if(v.getCode().equals(code))
                return v;
        }
        return null;
    }


    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
