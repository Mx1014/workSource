package com.everhomes.module;

import com.everhomes.server.schema.tables.pojos.EhServiceModuleExcludeFunctions;
import com.everhomes.util.StringHelper;

/**
 * Created by ying.xiong on 2017/11/30.
 */
public class ServiceModuleExcludeFunction extends EhServiceModuleExcludeFunctions {

    private static final long serialVersionUID = 936104894991898588L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
