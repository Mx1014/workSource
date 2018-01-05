package com.everhomes.rest.techpark.expansion;

import com.everhomes.util.StringHelper;

/**
 * @author sw on 2017/10/19.
 */
public class SetLeasePromotionConfigCommand {
    private Integer namespaceId;
    private Byte buildingIntroduceFlag;

    private Long categoryId;

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Byte getBuildingIntroduceFlag() {
        return buildingIntroduceFlag;
    }

    public void setBuildingIntroduceFlag(Byte buildingIntroduceFlag) {
        this.buildingIntroduceFlag = buildingIntroduceFlag;
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
