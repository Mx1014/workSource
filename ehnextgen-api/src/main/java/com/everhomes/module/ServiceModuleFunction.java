package com.everhomes.module;

import com.everhomes.server.schema.tables.pojos.EhServiceModuleFunctions;
import com.everhomes.util.StringHelper;

/**
 * Created by ying.xiong on 2017/12/4.
 */
public class ServiceModuleFunction extends EhServiceModuleFunctions {
    private static final long serialVersionUID = -1436124230253660073L;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
