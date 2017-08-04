package com.everhomes.rest.varField;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>namespaceId: 域空间id</li>
 *     <li>moduleName: 模块名</li>
 *     <li>rootGroupId: 所属最顶层的字段组在系统中的id</li>
 * </ul>
 * Created by ying.xiong on 2017/8/1.
 */
public class ListFieldCommand {
    private Integer namespaceId;

    private String moduleName;

    private Long rootGroupId;

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public Long getRootGroupId() {
        return rootGroupId;
    }

    public void setRootGroupId(Long rootGroupId) {
        this.rootGroupId = rootGroupId;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
