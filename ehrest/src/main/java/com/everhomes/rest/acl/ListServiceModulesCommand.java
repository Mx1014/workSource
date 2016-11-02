package com.everhomes.rest.acl;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>level: 业务模块级别</li>
 * </ul>
 */
public class ListServiceModulesCommand {

    private Integer level;

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
