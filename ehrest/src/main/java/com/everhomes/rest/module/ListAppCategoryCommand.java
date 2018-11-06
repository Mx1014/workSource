package com.everhomes.rest.module;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>locationType: 分类位置，参考{@link ServiceModuleLocationType}</li>
 *     <li>parentId: parentId</li>
 * </ul>
 */
public class ListAppCategoryCommand {

    private Byte locationType;
    private Long parentId;

    public Byte getLocationType() {
        return locationType;
    }

    public void setLocationType(Byte locationType) {
        this.locationType = locationType;
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
