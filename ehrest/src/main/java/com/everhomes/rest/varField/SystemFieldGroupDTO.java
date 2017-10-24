package com.everhomes.rest.varField;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * <ul>
 *     <li>id: 组在系统中的id</li>
 *     <li>moduleName: 所属模块名</li>
 *     <li>parentId: 上级组id</li>
 *     <li>path: 组路径</li>
 *     <li>title: 显示名</li>
 *     <li>name: 逻辑名</li>
 *     <li>mandatoryFlag: 是否必选</li>
 *     <li>defaultOrder: 显示顺序</li>
 *     <li>children: 子字段组 参考{@link com.everhomes.rest.varField.SystemFieldGroupDTO}</li>
 * </ul>
 * Created by ying.xiong on 2017/9/21.
 */
public class SystemFieldGroupDTO {
    private Long id;
    private String moduleName;
    private Long parentId;
    private String path;
    private String title;
    private String name;
    private Byte mandatoryFlag;
    private Integer defaultOrder;
    @ItemType(SystemFieldGroupDTO.class)
    private List<SystemFieldGroupDTO> children;

    public List<SystemFieldGroupDTO> getChildren() {
        return children;
    }

    public void setChildren(List<SystemFieldGroupDTO> children) {
        this.children = children;
    }

    public Integer getDefaultOrder() {
        return defaultOrder;
    }

    public void setDefaultOrder(Integer defaultOrder) {
        this.defaultOrder = defaultOrder;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Byte getMandatoryFlag() {
        return mandatoryFlag;
    }

    public void setMandatoryFlag(Byte mandatoryFlag) {
        this.mandatoryFlag = mandatoryFlag;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
