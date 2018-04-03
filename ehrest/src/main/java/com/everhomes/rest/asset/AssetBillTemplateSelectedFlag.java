package com.everhomes.rest.asset;

import com.everhomes.util.StringHelper;
import org.apache.commons.lang.StringUtils;

/**
 * <ul>
 *     <li>0: unselected, 1: selected</li>
 * </ul>
 */
public enum AssetBillTemplateSelectedFlag {
    UNSELECTED((byte)0), SELECTED((byte)1);

    private Byte code;

    private AssetBillTemplateSelectedFlag(Byte code){
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }

    public static AssetBillTemplateSelectedFlag fromCode(Byte code) {
        for(AssetBillTemplateSelectedFlag v : AssetBillTemplateSelectedFlag.values()) {
            if(v.getCode() == code)
                return v;
        }
        return null;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
