package com.everhomes.rest.warehouse;

import com.everhomes.util.StringHelper;
import org.apache.commons.lang.StringUtils;

/**
 * Created by ying.xiong on 2017/5/16.
 */
public enum ImportDataType {
    WAREHOUSE_MATERIAL_CATEGORIES("WarehouseMaterialCategories"), WAREHOUSE_MATERIALS("WarehouseMaterials");

    private String code;

    private ImportDataType(String code){
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static ImportDataType fromCode(String code) {
        for(ImportDataType v : ImportDataType.values()) {
            if(StringUtils.equals(v.getCode(), code))
                return v;
        }
        return null;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
