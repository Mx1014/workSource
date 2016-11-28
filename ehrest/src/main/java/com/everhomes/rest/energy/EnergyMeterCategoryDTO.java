package com.everhomes.rest.energy;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>id: 分类id</li>
 *     <li>name: 分类名称</li>
 *     <li>deleteFlag: 是否是默认分类, 不允许删除 {@link com.everhomes.rest.approval.TrueOrFalseFlag}</li>
 * </ul>
 */
public class EnergyMeterCategoryDTO {

    private Long id;
    private String name;
    private Byte deleteFlag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Byte getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Byte deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
