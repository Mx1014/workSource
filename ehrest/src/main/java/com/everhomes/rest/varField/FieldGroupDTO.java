package com.everhomes.rest.varField;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>id: 域下的组id，新加进去的没有</li>
 *     <li>parentId: 父id</li>
 *     <li>namespaceId: 域空间id</li>
 *     <li>moduleName: 组所属的模块类型名</li>
 *     <li>groupId: 在系统组里的id</li>
 *     <li>groupPath: 在系统组里的path</li>
 *     <li>groupDisplayName: 组名</li>
 *     <li>childrenGroup: 子字段组 参考{@link FieldGroupDTO}</li>
 * </ul>
 * Created by ying.xiong on 2017/8/1.
 */
public class FieldGroupDTO {
    private Long id;

    private Long parentId;

    private Integer namespaceId;

    private String moduleName;

    private Long groupId;

    private String groupPath;

    private String groupDisplayName;

    private Integer defaultOrder;

    @ItemType(FieldGroupDTO.class)
    private List<FieldGroupDTO> childrenGroup;

    public String getGroupPath() {
        return groupPath;
    }

    public void setGroupPath(String groupPath) {
        this.groupPath = groupPath;
    }

    public Integer getDefaultOrder() {
        return defaultOrder;
    }

    public void setDefaultOrder(Integer defaultOrder) {
        this.defaultOrder = defaultOrder;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getGroupDisplayName() {
        return groupDisplayName;
    }

    public void setGroupDisplayName(String groupDisplayName) {
        this.groupDisplayName = groupDisplayName;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public List<FieldGroupDTO> getChildrenGroup() {
        return childrenGroup;
    }

    public void setChildrenGroup(List<FieldGroupDTO> childrenGroup) {
        this.childrenGroup = childrenGroup;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
