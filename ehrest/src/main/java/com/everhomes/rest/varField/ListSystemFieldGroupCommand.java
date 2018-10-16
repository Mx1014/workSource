package com.everhomes.rest.varField;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>moduleName: 模块名</li>
 * </ul>
 * Created by ying.xiong on 2017/9/22.
 */
public class ListSystemFieldGroupCommand {

    private String moduleName;

    private String moduleType;

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getModuleType() {
        return moduleType;
    }

    public void setModuleType(String moduleType) {
        this.moduleType = moduleType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
