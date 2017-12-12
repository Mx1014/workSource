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

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
