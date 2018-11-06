package com.everhomes.rest.module;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>locationType: 分类位置，参考{@link ServiceModuleLocationType}</li>
 * </ul>
 */
public class ListLeafAppCategoryCommand {

    private Byte locationType;

    public Byte getLocationType() {
        return locationType;
    }

    public void setLocationType(Byte locationType) {
        this.locationType = locationType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
