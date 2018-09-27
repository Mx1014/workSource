package com.everhomes.rest.acl;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>name: name</li>
 *     <li>parentId: parentId</li>
 *     <li>locationType: 位置，参考{@link com.everhomes.rest.module.ServiceModuleLocationType}</li>
 *     <li>leafFlag: 是否叶子节点，0-no，1-yes，参考{@link com.everhomes.rest.common.TrueOrFalseFlag}</li>
 * </ul>
 */
public class CreateAppCategoryCommand {

    private String name;

    private Long parentId;

    private Byte locationType;

    private Byte leafFlag;


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

    public Byte getLocationType() {
        return locationType;
    }

    public void setLocationType(Byte locationType) {
        this.locationType = locationType;
    }

    public Byte getLeafFlag() {
        return leafFlag;
    }

    public void setLeafFlag(Byte leafFlag) {
        this.leafFlag = leafFlag;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
