package com.everhomes.rest.varField;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>namespaceId: 域空间id</li>
 *     <li>moduleName: 模块名</li>
 *     <li>communityId: 项目id</li>
 * </ul>
 * Created by ying.xiong on 2017/8/1.
 */
public class ListFieldGroupCommand {

    private String moduleName;

    private Integer namespaceId;

    private Long communityId;

    // 暂时增加用于物业巡检使用
    private String  equipmentCategoryName;

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
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

    public String getEquipmentCategoryName() {
        return equipmentCategoryName;
    }

    public void setEquipmentCategoryName(String equipmentCategoryName) {
        this.equipmentCategoryName = equipmentCategoryName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
