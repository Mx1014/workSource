package com.everhomes.rest.openapi;

import java.util.List;


/**
 *
 * <ul>
 * <li>FunctionCategory: 功能卡片的分类</li>
 * <li>jsonData: 业务返回的json数据</li>
 * </ul>
 */

public class FunctionCardDto {
    private FunctionCategory categoryName;
    private List<String> jsonData;

    public FunctionCategory getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(FunctionCategory categoryName) {
        this.categoryName = categoryName;
    }

    public List<String> getJsonData() {
        return jsonData;
    }

    public void setJsonData(List<String> jsonData) {
        this.jsonData = jsonData;
    }
}
