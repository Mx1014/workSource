package com.everhomes.rest.warehouse;

import com.everhomes.util.StringHelper;

/**
 * Created by ying.xiong on 2017/5/22.
 */
public class ImportWarehouseMaterialCategoryDataDTO {

    private String parentCategoryNumber;

    private String parentCategoryName;

    private String name;

    private String categoryNumber;

    public String getCategoryNumber() {
        return categoryNumber;
    }

    public void setCategoryNumber(String categoryNumber) {
        this.categoryNumber = categoryNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentCategoryName() {
        return parentCategoryName;
    }

    public void setParentCategoryName(String parentCategoryName) {
        this.parentCategoryName = parentCategoryName;
    }

    public String getParentCategoryNumber() {
        return parentCategoryNumber;
    }

    public void setParentCategoryNumber(String parentCategoryNumber) {
        this.parentCategoryNumber = parentCategoryNumber;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
