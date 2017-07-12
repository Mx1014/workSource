package com.everhomes.rest.yellowPage;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>namespaceId: 域空间id</li>
 *     <li>moduleName: 模块名</li>
 *     <li>moduleUrl: 跳转链接</li>
 * </ul>
 */
public class JumpModuleDTO {

    private Long id;

    private Integer namespaceId;

    private String moduleName;

    private String moduleUrl;

    private Long parentId;

    @ItemType(JumpModuleDTO.class)
    private List<JumpModuleDTO> children;

    public List<JumpModuleDTO> getChildren() {
        return children;
    }

    public void setChildren(List<JumpModuleDTO> children) {
        this.children = children;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getModuleUrl() {
        return moduleUrl;
    }

    public void setModuleUrl(String moduleUrl) {
        this.moduleUrl = moduleUrl;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
