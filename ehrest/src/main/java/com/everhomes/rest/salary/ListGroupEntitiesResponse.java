package com.everhomes.rest.salary;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 *
 * <ul>
 * <li>categories: 薪酬结构分类 {@link CategoryDTO}</li>
 * <li>operatorName: 最后操作人</li>
 * <li>updateTime: 最后操作时间</li>
 * </ul>
 */
public class ListGroupEntitiesResponse {
    @ItemType(CategoryDTO.class)
    private List<CategoryDTO> categories;

    private String operatorName;
    private Long updateTime;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public List<CategoryDTO> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryDTO> categories) {
        this.categories = categories;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }
}
